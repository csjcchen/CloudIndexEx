import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.StringTokenizer;

//�����Ŀ�ģ� ������count refinment_log��distinct ��ѯ�ĸ���
public class QueryCount {

	public static int count(String fileName){
		//1417367487,538904070,172.16.0.240,1,[57855228.258015 57861272.431349 52338326.357164 52342992.803830 4575.861923 4592.528590 ],0,0,0,0.000000

		RandomAccessFile file = null;
		double TIME_ZERO  = 1406970000;
		HashMap<Integer,String> hmap = new HashMap<Integer,String>();
		
		try{
			file = new RandomAccessFile(fileName,"r");
			String line = file.readLine();
			
			double time_min = Double.MAX_VALUE;
			double time_max = Double.MIN_VALUE;
			
			while (line!=null){
				String token = "";
				int column_idx = -1;
				StringTokenizer st = new StringTokenizer(line, ",[] ");
				int qry_id = -1; 
				boolean find_qid = false;
				
				while (st.hasMoreTokens()){
					token = st.nextToken();
					switch(column_idx){
					case -1:
						double time = Double.parseDouble(token);
						time_min = Math.min(time_min, time);
						time_max = Math.max(time_max, time);
						break;
					case 0:						
						break;
					case 1:						 
						break;
					case 2:
						qry_id = Integer.parseInt(token);
						hmap.put(qry_id, "");
						find_qid = true;
						break;					
					default:
						break;
					}
					column_idx ++;
					
					if (find_qid) {
						//System.out.println(qry_id);
						break;
					}
				}
				
				line = file.readLine();
			}
			System.out.println(hmap.keySet().size());
			System.out.println(hmap.keySet().size()/(time_max-time_min));
			file.close();
		}
		catch(Exception ex){
			ex.printStackTrace(System.out);
		}
		return hmap.keySet().size();
	}
	
	public static void main(String[] args){
		count("D:\\results\\log_11_30\\4_40\\refinement_1130151724.log");
	}

}

