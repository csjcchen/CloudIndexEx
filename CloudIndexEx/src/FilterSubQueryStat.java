import java.lang.StringBuffer;

public class FilterSubQueryStat extends SubQueryStat {
	double index_elapsed;
	double obtain_idle_elapsed;
	double send_refinement_elapsed;
	int size_after_index;
	int requested_idle_nodes;
	int got_idle_nodes;
	String filter_ip;
	
	public String toString(){
		StringBuffer str = new StringBuffer(super.toString());
	
		str.append(" filter_ip ip=" + this.filter_ip + "\n");
		str.append(" index_elapsed=" + this.index_elapsed + "\n");
		str.append(" obtain_idle_elapsed=" + this.obtain_idle_elapsed + "\n");
		str.append(" send_refinement_elapsed=" + this.send_refinement_elapsed + "\n");
		str.append(" size_after_index=" + this.size_after_index + "\n");
		str.append(" requested_idle_nodes=" + this.requested_idle_nodes + "\n");
		str.append(" got_idle_nodes=" + this.got_idle_nodes + "\n");
		
		return str.toString();
	}
}
