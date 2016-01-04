import java.lang.StringBuffer;

public class RfmtSubQueryStat extends SubQueryStat {
	String rfmt_ip;
	double recv_refinement_elpased;
	double calc_qp_elapsed;
	int result_size;
	double avg_qp;
	
	public String toString(){
		StringBuffer str = new StringBuffer(super.toString());
	
		str.append(" refinement ip=" + this.rfmt_ip + "\n");
		str.append(" recv_refinemnet_elapsed=" + this.recv_refinement_elpased + "\n");
		str.append(" calc_qp_elapsed=" + this.calc_qp_elapsed + "\n");
		str.append(" result_size=" + this.result_size + "\n");
		
		return str.toString();
	}
}
