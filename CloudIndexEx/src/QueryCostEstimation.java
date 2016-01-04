
public class QueryCostEstimation {

	public static void run(double d_max, double qry_time_span,int traj_num){
		double loge = 2.30;
		// -ln(1-P*)
		//double SIGMA = 0.00015;
		double SIGMA = 1;
		double SAMPLE_TIME_RATE = 20;
		double SAMPLE_SPATIAL_POINTS = 200;
		double EXCHANGE_RATE_NN_QUERY = 3.6e-6;
		double PI = 3.1415927;

		//double qry_time_span = 100.0;
		// time spane of query
		//double qry_time_span = (query.intval[2].high - query.intval[2].low);
		
		double avg_time_span = qry_time_span / 2.0;

		
		//int traj_num = 100;
		//double d_max = 10;
		// d_max is computed during the filtering phase and stored in
		// refinement_stat
		
		double density = traj_num * avg_time_span / qry_time_span;
		// # of objects per snapshot (Equation 8 of the paper)


		double rho = density / (PI * Math.pow(d_max + 3 * SIGMA, 2.0));
		// to get /rho in the paper (the two lines below Equation 8).
 
		double num_snapshot_qrys = qry_time_span / SAMPLE_TIME_RATE;
		// # of snapshot queries

		double S = loge / rho;
		// Equaton 9

		// Equation 10
		double tmp = Math.sqrt(S / PI) + 3 * SIGMA;
		double h = rho * PI * Math.pow(tmp, 2.0);

		double sample_points_per_time = h * SAMPLE_SPATIAL_POINTS;

		double res_time =   (EXCHANGE_RATE_NN_QUERY * num_snapshot_qrys * Math.pow(
				sample_points_per_time, 2.0));
		// Equation 5
		
		//System.out.println("h=" + h + ", S=" + S + ", rho=" + rho + ", density=" + density );
		System.out.println( h + "," + S + ", " + rho + ", " + density );
	}
	
	public static void main(String[] args){
		//run(double d_max, double qry_time_span,int traj_num)
		double d_max = 10;
		double qry_time_span = 100; 
		int traj_num = 100;
		for (d_max = 1; d_max <= 10; d_max+=1){
			for (traj_num=100;traj_num<=1000;traj_num+=100){
				//System.out.println("d_max =" +  d_max + ", traj_num=" + traj_num);
				run(d_max, qry_time_span, traj_num);
			}
		}
	}
}
