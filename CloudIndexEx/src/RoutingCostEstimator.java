
public class RoutingCostEstimator {

	static int cal_rout(int k, int D){
		if (k<=D/2)
			return k;
		else
			return D-k;		
	}
	
	static void routing(int l, int m, int n){
		
		int x, y, z;
		int max = 0;
		double expected = 0;
		for (x=0;x<l;x++){
			for (y=0;y<m;y++){
				for (z=0;z<n;z++){
					int r_x,r_y,r_z;
					r_x = cal_rout(x,l);
					r_y = cal_rout(y,m);
					r_z = cal_rout(z,n);
					//System.out.println(r_x + "," + r_y + "," + r_z);
					int r = r_x + r_y + r_z;
					if (r>max)
						max = r;
					expected += r;
				}				
			}			
		}
		
		expected /= (l*m*n);
		
		System.out.println("max=" + max + "; expected = " + expected);
		
	}
	
	
	public static void main(String[] args){
		routing(2,2,2);
		routing(3,3,3);
		routing(4,4,4);
		routing(5,5,5);
		routing(6,6,6);
		routing(7,7,7);
		routing(8,8,8);
		routing(9,9,9);
 		 
	}
}
