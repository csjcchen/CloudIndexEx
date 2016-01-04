
public class QueryStat {
	int query_id;
	Region query_region;
	
	double response_time;
	double avg_filtering_time;
	double avg_qp_time;
	double avg_network_time;
	double avg_index_time;
	double avg_get_idle_time;
	double avg_send_refinement_time;
	double avg_recv_refinement_time;
	double avg_calc_qp_time;
	
	double result_size;
	double size_after_index;
	double avg_qp;
	double total_requested_ilde_nodes;
	double total_got_idle_nodes;
	
	public String toString() {

		StringBuffer str = new StringBuffer();
		str.append(" query id=" + this.query_id + "\n");
		str.append(" response_time=" + this.response_time + "\n");
		str.append(" avg_filtering_time=" + this.avg_filtering_time + "\n");
		str.append(" avg_qp_time=" + this.avg_qp_time + "\n");
		str.append(" avg_network_time=" + this.avg_network_time + "\n");
		str.append(" result_size=" + this.result_size + "\n");
		str.append(" total_requested_ilde_nodes=" + this.total_requested_ilde_nodes + "\n");
		str.append(" total_got_idle_nodes=" + this.total_got_idle_nodes + "\n");
		return str.toString();
		 
	}
	
	
}
