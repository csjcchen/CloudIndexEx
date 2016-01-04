
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;;

public class LogAnalysis {
	
	double TIME_ZERO  = 1406970000;
	//something changed
	
	private class SubQueryComparator implements Comparator{
		public int compare (Object o1, Object o2){
			SubQueryStat qry1 = (SubQueryStat)o1;
			SubQueryStat qry2 = (SubQueryStat)o2;
			if (qry1.query_id<qry2.query_id){
				return -1;
			}else if (qry1.query_id == qry2.query_id){
				if (qry1.recv_log_time_point < qry2.recv_log_time_point){
					return -1;
				}
				else if (qry1.recv_log_time_point == qry2.recv_log_time_point){
					return 0;
				}else{	
					return 1;
					}
			}
			else{
				return 1;
			}
			 
		}
	}
	
	ArrayList<FilterSubQueryStat> parseFilterLog(String filter_log_file, int qry_id_low, int qry_id_high){
		//380,172.16.0.86,29,[57016071.930583 57067994.619634 52544549.980112 52581642.893231 2463.922676 2549.572946 ],
		//0,0,2,1,1,70,0
		
		RandomAccessFile file = null;
		
		ArrayList<FilterSubQueryStat> list_queries = new ArrayList<FilterSubQueryStat>();
		
		String line = "";
		double seconds = 0, nano_secs = 0;
		try{
			file = new RandomAccessFile(filter_log_file,"r");
			line = file.readLine();
						
			while(line!=null){
				FilterSubQueryStat sub_qry = new FilterSubQueryStat();
				sub_qry.query_region = new Region(3);
				
				String token = "";
				int column_idx = -1;
				StringTokenizer st = new StringTokenizer(line, ",[] ");
				while (st.hasMoreTokens()){
					token = st.nextToken();
					switch(column_idx){
					case -1:
						seconds = Double.parseDouble(token) - TIME_ZERO;
						break;
					case 0:
						nano_secs = Double.parseDouble(token);
						break;
					case 1:
						sub_qry.filter_ip = token;
						break;
					case 2:
						sub_qry.query_id = Integer.parseInt(token);
						break;
					case 3:
						sub_qry.query_region.low[0] = Double.parseDouble(token);
						break;
					case 4:
						sub_qry.query_region.high[0] = Double.parseDouble(token);
						break;
					case 5:
						sub_qry.query_region.low[1] = Double.parseDouble(token);
						break;
					case 6:
						sub_qry.query_region.high[1] = Double.parseDouble(token);
						break;
					case 7:
						sub_qry.query_region.low[2] = Double.parseDouble(token);
						break;
					case 8:
						sub_qry.query_region.high[2] = Double.parseDouble(token);
						break;
					case 9:
						sub_qry.index_elapsed = Double.parseDouble(token);
						break;
					case 10:
						sub_qry.obtain_idle_elapsed = Double.parseDouble(token);
						break;
					case 11:
						sub_qry.send_refinement_elapsed = Double.parseDouble(token);
						break;
					case 12:
						sub_qry.requested_idle_nodes = Integer.parseInt(token);
						break;
					case 13:
						sub_qry.got_idle_nodes = Integer.parseInt(token);
						break;
					case 14: 
						sub_qry.size_after_index = Integer.parseInt(token);
						break;
					default:
						break;
					}
					column_idx ++;
				}
				
				line = file.readLine();
				
				if (sub_qry.query_id<qry_id_low || sub_qry.query_id>qry_id_high){
					continue;
				}
				
				sub_qry.recv_log_time_point = seconds * 1000.0 + nano_secs / 1e6; 
				
				sub_qry.start_qry_time_point = sub_qry.recv_log_time_point - sub_qry.index_elapsed - sub_qry.obtain_idle_elapsed - sub_qry.send_refinement_elapsed;
				sub_qry.finish_qry_time_point = sub_qry.recv_log_time_point;

				if(sub_qry.query_id % 2 == 0) {
					// == 0 means nn
				  list_queries.add(sub_qry);
				}
				//System.out.println(sub_qry);
				
			}
			file.close();
		}catch(Exception ex){
			ex.printStackTrace(System.out);
			System.out.println("line = " + line);
		}

		return list_queries;
	}
	
	ArrayList<RfmtSubQueryStat> parseRefinmentLog(String rfmt_log_file, int qry_id_low, int qry_id_high){
		RandomAccessFile file = null;
		
		ArrayList<RfmtSubQueryStat> list_queries = new ArrayList<RfmtSubQueryStat>();
		
		
		try{
			file = new RandomAccessFile(rfmt_log_file,"r");
			String line = file.readLine();
			double seconds = 0, nano_secs = 0;			
			while(line!=null){
				RfmtSubQueryStat sub_qry = new RfmtSubQueryStat();
				sub_qry.query_region = new Region(3);
				 
				String token = "";
				int column_idx = -1;
				StringTokenizer st = new StringTokenizer(line, ",[] ");
				while (st.hasMoreTokens()){
					token = st.nextToken();
					switch(column_idx){
					case -1:
						seconds = Double.parseDouble(token) - TIME_ZERO;
						break;
					case 0:
						nano_secs = Double.parseDouble(token);
					case 1:
						sub_qry.rfmt_ip = token;
						break;
					case 2:
						sub_qry.query_id = Integer.parseInt(token);
						break;
					case 3:
						sub_qry.query_region.low[0] = Double.parseDouble(token);
						break;
					case 4:
						sub_qry.query_region.high[0] = Double.parseDouble(token);
						break;
					case 5:
						sub_qry.query_region.low[1] = Double.parseDouble(token);
						break;
					case 6:
						sub_qry.query_region.high[1] = Double.parseDouble(token);
						break;
					case 7:
						sub_qry.query_region.low[2] = Double.parseDouble(token);
						break;
					case 8:
						sub_qry.query_region.high[2] = Double.parseDouble(token);
						break;
					case 9:
						sub_qry.recv_refinement_elpased = Double.parseDouble(token);
						break;
					case 10:
						sub_qry.calc_qp_elapsed = Double.parseDouble(token);
						break;
					case 11:
						sub_qry.result_size = Integer.parseInt(token);
						break;
					case 12:
						sub_qry.avg_qp = Double.parseDouble(token);
						break;
					default:
						break;
					}
					column_idx ++;
				}
				line = file.readLine();
				
				if (sub_qry.query_id<qry_id_low || sub_qry.query_id>qry_id_high){
					continue;
				}
				
				sub_qry.recv_log_time_point = seconds * 1000.0 + nano_secs / 1e6; 				
				sub_qry.start_qry_time_point = sub_qry.recv_log_time_point - sub_qry.recv_refinement_elpased - sub_qry.calc_qp_elapsed;
				sub_qry.finish_qry_time_point = sub_qry.recv_log_time_point;
				
				if(sub_qry.query_id % 2 == 0) {
				  list_queries.add(sub_qry);
				}
				
				//System.out.println(sub_qry);
			}
			file.close();
		}catch(Exception ex){
			ex.printStackTrace(System.out);
		}

		return list_queries;
	}
	
	QueryStat aggregateSubQueries(ArrayList<SubQueryStat> list_sub_queries){
		double filtering_elapsed, refinement_elapsed, start_t, end_t;
		
		if (list_sub_queries.size()==0) {
			System.out.println("aggregateSubQueries: empty sub_queries_list.");
			return null;
		}
		
		QueryStat qry = new QueryStat();
		
		
		qry.query_id = list_sub_queries.get(0).query_id;
		
		qry.query_region = list_sub_queries.get(0).query_region;
			//TODO re_construct the original query's region
		
		Iterator<SubQueryStat> iter = list_sub_queries.iterator();
		
		start_t = Double.MAX_VALUE;
		end_t = Double.MIN_VALUE;
		
		refinement_elapsed = 0;
		filtering_elapsed =  0;
		
		double index_time;
		double get_idle_time;
		double send_refinement_time;
		double recv_refinement_time;
		double calc_qp_time;
		index_time = get_idle_time = send_refinement_time =	recv_refinement_time = calc_qp_time = 0;

		int filter_num = 0;
		int rfmt_num = 0;
		int total_request_nodes = 0;
		int total_got_nodes = 0;
		int rlt_size = 0;
		int size_after_index = 0;
		while (iter.hasNext()){
			SubQueryStat sub_qry = iter.next();			
							
			if (sub_qry.getClass().getName().equals("FilterSubQueryStat")){
				start_t = Math.min(start_t, sub_qry.start_qry_time_point);	
				FilterSubQueryStat f_qry = (FilterSubQueryStat)sub_qry;
				filtering_elapsed += f_qry.index_elapsed + f_qry.obtain_idle_elapsed + f_qry.send_refinement_elapsed;				
				total_request_nodes += f_qry.requested_idle_nodes;
				total_got_nodes += f_qry.got_idle_nodes;
				size_after_index += f_qry.size_after_index;
				index_time += f_qry.index_elapsed;
				get_idle_time += f_qry.obtain_idle_elapsed;
				send_refinement_time += f_qry.send_refinement_elapsed;
				
				filter_num ++;
			}
			else{
				RfmtSubQueryStat rfmt_q = (RfmtSubQueryStat)sub_qry;		
				end_t = Math.max(rfmt_q.finish_qry_time_point, end_t);	
				refinement_elapsed += rfmt_q.recv_refinement_elpased + rfmt_q.calc_qp_elapsed;
				rlt_size += rfmt_q.result_size;
				
				recv_refinement_time += rfmt_q.recv_refinement_elpased;
				calc_qp_time += rfmt_q.calc_qp_elapsed;
				
				rfmt_num ++;
			}

		}
		
		qry.total_requested_ilde_nodes = total_request_nodes;
		qry.total_got_idle_nodes = total_got_nodes;
		qry.response_time = end_t - start_t;
		qry.size_after_index = ((double)size_after_index)/filter_num;
		
		if (filter_num == 0){
			System.out.println("empty filter set, query id=" + qry.query_id);
			qry.avg_filtering_time = 0;
			qry.avg_index_time = qry.avg_get_idle_time = qry.avg_send_refinement_time = 0;
		}
		else{
			qry.avg_filtering_time = filtering_elapsed/filter_num;
			qry.avg_index_time = index_time/filter_num;
			qry.avg_get_idle_time = get_idle_time/filter_num;
			qry.avg_send_refinement_time = send_refinement_time/filter_num;
		}
		
		if (rfmt_num == 0){
			System.out.println("empty refinement set, query id=" + qry.query_id);
			qry.avg_qp_time = 0;
			qry.avg_recv_refinement_time = qry.avg_calc_qp_time = 0;
		}
		else{
			qry.avg_qp_time = refinement_elapsed/rfmt_num;
			qry.avg_recv_refinement_time = recv_refinement_time/rfmt_num;
			qry.avg_calc_qp_time = calc_qp_time/rfmt_num;
		}
		
		qry.result_size = rlt_size;
		qry.avg_network_time = qry.response_time - qry.avg_filtering_time - qry.avg_qp_time;
		
		//System.out.println(qry);
		return qry;
	}

	QueryStat break_down_analysis(String filter_log_file, String rfmt_log_file, int qry_id_low, int qry_id_high){
		ArrayList<FilterSubQueryStat> list_filter_queries = new ArrayList<FilterSubQueryStat>();
		ArrayList<RfmtSubQueryStat> list_rfmt_queries = new ArrayList<RfmtSubQueryStat>();
		//parse filter log
		list_filter_queries = parseFilterLog(filter_log_file, qry_id_low, qry_id_high);
		
		//parse refinement log
		list_rfmt_queries = parseRefinmentLog(rfmt_log_file, qry_id_low, qry_id_high);
		
		
		//compute the overal stat
		ArrayList<SubQueryStat> list_all_sub_queries = new ArrayList<SubQueryStat>();
		list_all_sub_queries.addAll(list_filter_queries);
		list_all_sub_queries.addAll(list_rfmt_queries);
		
		
		SubQueryStat[] sub_queries = (SubQueryStat[]) list_all_sub_queries.toArray(new SubQueryStat[0]);
	 	
		//all subqueries sort by queryid and recv_qry_time_point
		Arrays.sort(sub_queries, new SubQueryComparator());
		 		
		int i;
		//System.out.println("len = " + sub_queries.length);
		int pr_qry_id = sub_queries[0].query_id;
	
		int qry_count = 0;
		double total_response = 0;
		double total_filter = 0;
		double total_rfmt = 0;
		double total_network = 0;
		double total_rlt_size = 0;
		double total_requested_nodes = 0;
		double total_got_nodes = 0;
		double total_size_after_index = 0;
		
		double total_index_time;
		double total_get_idle_time;
		double total_send_refinement_time;
		double total_recv_refinement_time;
		double total_calc_qp_time;
		total_index_time = total_get_idle_time = total_send_refinement_time =	total_recv_refinement_time = total_calc_qp_time = 0;

		
		ArrayList<QueryStat> list_queries = new ArrayList<QueryStat>();
		ArrayList<SubQueryStat> list_group_sub_q = new ArrayList<SubQueryStat>();
	
		for (i=0;i<sub_queries.length;i++){
			int qry_id = sub_queries[i].query_id;
			 
			if (qry_id!=pr_qry_id || i== sub_queries.length-1){
				//meets a new query
				//handle the last element
				if (i == sub_queries.length-1){
					list_group_sub_q.add(sub_queries[i]);		
				}
				
				QueryStat qry =  aggregateSubQueries(list_group_sub_q);
				list_queries.add(qry);
				list_group_sub_q = new ArrayList<SubQueryStat>();
				
				qry_count++;
				total_response += qry.response_time;
				total_filter += qry.avg_filtering_time;
				total_rfmt += qry.avg_qp_time;
				total_network += qry.avg_network_time;
				total_rlt_size += qry.result_size;
				total_requested_nodes += qry.total_requested_ilde_nodes;
				total_got_nodes += qry.total_got_idle_nodes;
				total_size_after_index += qry.size_after_index;
								
				total_index_time += qry.avg_index_time;
				total_get_idle_time += qry.avg_get_idle_time;
				total_send_refinement_time += qry.avg_send_refinement_time;
				total_recv_refinement_time += qry.avg_recv_refinement_time;
				total_calc_qp_time += qry.avg_calc_qp_time;				
			}
			 
			list_group_sub_q.add(sub_queries[i]);				
			
			pr_qry_id = qry_id;
		}
		
		//print the result		
		/*System.out.println("the number of queries:" + qry_count);
		System.out.println("avg_response_time=" + total_response/qry_count);
		System.out.println("avg_filtering_time=" + total_filter/qry_count);
		System.out.println("avg_refinement_time=" + total_rfmt/qry_count);
		System.out.println("avg_network_time=" + total_network/qry_count);
		System.out.println("avg_result_size=" + total_rlt_size/qry_count);
		System.out.println("avg_num_of_requested_nodes=" + total_requested_nodes/qry_count);
		System.out.println("avg_num_of_got_nodes=" + total_got_nodes/qry_count);
		*/
		QueryStat statics = new QueryStat();
		statics.query_id = qry_count;//use this to store the num of queries
		statics.response_time = total_response/qry_count;
		statics.avg_filtering_time =  total_filter/qry_count;
		statics.avg_qp_time =  total_rfmt/qry_count;
		statics.avg_network_time =  total_network/qry_count;
		statics.result_size =  total_rlt_size/qry_count;
		statics.total_requested_ilde_nodes = total_requested_nodes/qry_count;
		statics.total_got_idle_nodes = total_got_nodes/qry_count;
		statics.size_after_index = total_size_after_index/qry_count;
		statics.avg_index_time = total_index_time/qry_count;
		statics.avg_get_idle_time = total_get_idle_time/qry_count;
		statics.avg_send_refinement_time = total_send_refinement_time/qry_count;
		statics.avg_recv_refinement_time = total_recv_refinement_time/qry_count;
		statics.avg_calc_qp_time = total_calc_qp_time/qry_count;
		
		//System.out.println("num of queries in this set:" + qry_count);
		return statics;
	}
	
	
	public static void main(String[] args){
		String basedir = "D:\\Works\\student\\meibenjin\\results\\log_15_06";
		LogAnalysis la = new LogAnalysis();
		
		//String childir[] = {"log_100w_1200", "log_200w_1200", "log_300w_1200", "log_400w_1200"};
		String childir[] = {"100sample", "200sample","300sample"};
		//int total_query_num = 100;
		//int group_num = 20;
		for (int i=0;i<childir.length;i++){
			//System.out.println("i=" + i);
			String filter_log = basedir + "\\" + childir[i] + "\\filter.log";
			String rfmt_log = basedir +"\\" + childir[i] + "\\refinement.log";
			System.out.println("start processing " + filter_log );
			 
			ArrayList<QueryStat> list_stat = new ArrayList<QueryStat>();
			
			//for (int id = 1;id<total_query_num;id+=group_num){
				//System.out.println("results for id between " + id + " and " + (id+99));
				list_stat.add(la.break_down_analysis(filter_log, rfmt_log, 0, 9999));		
			//}	
		
				//print response
			System.out.print("# of queries:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.query_id + " ");
			}
			System.out.println();

			//print response
			System.out.print("avg response:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.response_time + " ");
			}
			System.out.println();
			
			//print filter
			System.out.print("avg filtering:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.avg_filtering_time + " ");
			}
			System.out.println();
			
			//print refinement
			System.out.print("avg refinement:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.avg_qp_time + " ");
			}
			System.out.println();
			
			//print # got nodes
			System.out.print("avg result size:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.result_size + " ");
			}
			System.out.println();


			
			//print # requested nodes
			System.out.print("avg # of requested rfmt nodes:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.total_requested_ilde_nodes + " ");
			}
			System.out.println();
			
			//print # got nodes
			System.out.print("avg # of obtained rfmt response:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.total_got_idle_nodes + " ");
			}
			System.out.println();		
			
			//print size_after_index
			System.out.print("avg # of size_after_index:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.size_after_index + " ");
			}
			System.out.println();	
			
			//print index_time
			System.out.print("avg index_time:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.avg_index_time + " ");
			}
			System.out.println();	
			
			
			//print get_idle_time
			System.out.print("avg get_idle_time:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.avg_get_idle_time + " ");
			}
			System.out.println();	
			
			
			//print avg_send_refinement_time
			System.out.print("avg avg_send_refinement_time:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.avg_send_refinement_time + " ");
			}
			System.out.println();	
			
			
			//print avg_recv_refinement_time
			System.out.print("avg avg_recv_refinement_time:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.avg_recv_refinement_time + " ");
			}
			System.out.println();	
			
			
			//print avg_calc_qp_time
			System.out.print("avg avg_calc_qp_time:");
			for (int k=0;k<list_stat.size();k++){
				QueryStat stat = list_stat.get(k);
				System.out.print(stat.avg_calc_qp_time + " ");
			}
			System.out.println();	
		}
	}
}
