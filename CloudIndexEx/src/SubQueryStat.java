import java.lang.StringBuffer;

public class SubQueryStat {
	int query_id;
	Region query_region;
	//time unit is millisecond
	double start_qry_time_point;
	double finish_qry_time_point;
	double recv_log_time_point;
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		str.append(" query id=" + this.query_id + "\n");
		str.append(" recv_log_time_point=" + this.recv_log_time_point + "\n");
		str.append(" start_qry_time_point=" + this.start_qry_time_point + "\n");
		str.append(" finish_qry_time_point=" + this.finish_qry_time_point + "\n");
		
		return str.toString();
	}
}
