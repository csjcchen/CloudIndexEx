
import java.util.*;

public class NNSampleEstimate {
	
	double NUM_SAMPLES = 500;
	double SIGMA = 900;
	double VELOCITY = SIGMA;
	double[] RANGE_QUERY = {0,1.5*SIGMA};
	double EPSILON = 1.0e-7;

	//给定 time_points and num_candidates, 产生一组trajectory
	//重复计算N 组QP之后，计算variance
	
	
	public void simulation(int time_points, int num_candidates, int qry_type){
		int num_sims = 50;
		
		Trajectory[] trjs = generate_trajectories(time_points, num_candidates);
		
		ArrayList<double[]> listQP = new ArrayList<double[]>();
		
		int i;
		for (i=0;i<num_sims;i++){
			listQP.add(QPEval(time_points,trjs,qry_type));
		}
		
		int j;
		//将每个obj在多次simulation的qp抽出来存入一个数组，并计算其variance
		double vars[] = new double[num_candidates];
		for (j=0;j<num_candidates;j++){
			double[] qp_obj = new double[num_sims];
			for (i=0;i<num_sims;i++){
				double[] qp_sim = listQP.get(i);
				qp_obj[i] = qp_sim[j];
			}	
			//计算一个obj在多次simulation中qp的variance
			vars[j] = calculate_variance(qp_obj);
		}
		
		//计算最终的平均值
		double avg_var = calculate_avg(vars);
		System.out.println("samples:" +  NUM_SAMPLES + ", variance = " + avg_var);
	}
	
	public double calculate_avg(double[] x){
		int i;
		double avg = 0;
		for (i=0;i<x.length;i++){
			avg += x[i];
		}
		
		return avg/x.length ; 
	}
	
	public double calculate_variance(double[] x){
		int i;
		double var = 0;
		double avg = calculate_avg(x);
		
		for (i=0;i<x.length;i++){
			var += (x[i]-avg)*(x[i]-avg);
		}
		
		return var/x.length ; 
	}
	
	
	public double[] QPEval(int time_points, Trajectory[] trjs , int qry_type){
		int i,j;
		double QP[] = new double[trjs.length]; 
		for (j=0;j<trjs.length;j++){
			QP[j] = 1.0;
		}
				
		for (i=0;i<time_points;i++){
			double[] QP_per_t = null;
			if (qry_type ==1 )
				QP_per_t = NNQPPerPoint(trjs, i);
			else
				QP_per_t = RangeQPPerPoint(trjs, i);
			
			for (j=0;j<trjs.length;j++){
				QP[j] *= (1 - QP_per_t[j]);
				if (QP[j] < EPSILON){
					QP[j] = 0;
				}
			}
		}
		
		for (j=0;j<trjs.length;j++){
			QP[j] = 1.0 - QP[j];
		}
		return QP;	 
	}
	
	public double[] RangeQPPerPoint(Trajectory[] trjs, int time_point){
		double[] qp = new double[trjs.length];
		
		for (int i=0;i<trjs.length;i++){
			Random sampleGenerator =  new Random(System.nanoTime());
			for(int j=0;j<NUM_SAMPLES;j++){
				double p = sampleGenerator.nextGaussian();
				p = p*SIGMA;//transform from N(0,1) to N(0, SIGMA)
				p = trjs[i].getPosition(time_point) + p;
				
				if (p>RANGE_QUERY[0] && p<RANGE_QUERY[1]){
					qp[i] += 1.0/NUM_SAMPLES;
				}				
			}			
		}
		return qp;
	}
	
	public double[] NNQPPerPoint( Trajectory[] trjs, int time_point){
		ArrayList<SamplePoint> listSamples = new ArrayList<SamplePoint>();
		for (int i=0;i<trjs.length;i++){
			Random sampleGenerator =  new Random(System.nanoTime());
			for(int j=0;j<NUM_SAMPLES;j++){
				double p = sampleGenerator.nextGaussian();
				p = p*SIGMA;//transform from N(0,1) to N(0, SIGMA)
				p = trjs[i].getPosition(time_point) + p;
				
				SamplePoint sp = new SamplePoint();
				sp._tr_id = i;
				sp.position = p;
				listSamples.add(sp);
			}			
			
		}
		
		SamplePoint SPs[] = listSamples.toArray(new SamplePoint[0]);
		Arrays.sort(SPs, new SPComparator());
		double cdf[] = new double[trjs.length];
		double qp[] = new double[trjs.length];
		for (int i=0;i<trjs.length;i++){
			cdf[i] = 0;
			qp[i] = 0;
		}
		
		for (int k=0;k<SPs.length;k++){
			SamplePoint sp = SPs[k];
			int tr_id = sp._tr_id;
			double pr = 1.0/NUM_SAMPLES;
			cdf[tr_id] += 1.0/NUM_SAMPLES;
			
			for (int i=0;i<trjs.length;i++){
				if (i!= tr_id){
					pr *= (1-cdf[i]);
				}
			}
			
			qp[tr_id] += pr;
		}
		
		//printArray(qp);
		
		return qp;
	}
	
	void printArray(double[] x){
		double sum = 0;
		for (int i=0;i<x.length;i++){
			sum += x[i];
			System.out.print(x[i] + ",");
		}
		System.out.println();
		System.out.println("sum=" + sum);
	}
	
	//产生trajectory方法(一维空间, 假设查询点在坐标0)：
	//初始，在[0,6*SIGMA]范围内随机生成n个点，作为初始时刻的位置。
	//设定最大速度为 VELOCITY
	//对于每个点，其下一时刻位置为 当前位置 + [-VELOCITY,VELOCITY]范围内某个值, 取绝对值
	Trajectory[] generate_trajectories(int time_points, int num_candidates){
		Trajectory[] trjs = new Trajectory[num_candidates];
		int i,j;
		
		//产生0时刻位置
		for (j=0;j<num_candidates;j++){
			trjs[j] = new Trajectory();
			trjs[j]._tr_id = j;
			trjs[j]._positions = new double[time_points];
			trjs[j]._positions[0] = Math.random() * (6*SIGMA);
		}
		
		for (i=1; i<time_points; i++){
			for (j=0;j<num_candidates;j++){
				double delta = Math.random()* (2*VELOCITY) - VELOCITY;
					//变化量为 [-VELOCITY,VELOCITY]
				trjs[j]._positions[i] = trjs[j]._positions[i-1] + delta;
				trjs[j]._positions[i] = Math.abs(trjs[j]._positions[i]);
			}
		}
		
		
		return trjs;
	}
	
	private class Trajectory{
		int _tr_id;
		double[] _positions;
		
		double getPosition(int time_point){
			return _positions[time_point];
		}
		
	}
	
	private class SamplePoint{
		double position;
		int _tr_id;
	}
	
	private class SPComparator implements java.util.Comparator<SamplePoint>{
		
		public int compare(SamplePoint sp1, SamplePoint sp2){
			if (sp1.position<sp2.position)
				return -1;
			else
				return 1;
		}
	}
	
	public static void main(String[] args){
		NNSampleEstimate est = new NNSampleEstimate();
		
		est.simulation(500, 20,2);
	}
}
