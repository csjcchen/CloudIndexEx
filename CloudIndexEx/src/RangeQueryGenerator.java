
public class RangeQueryGenerator {

	
	void generateQueriesByVaringRadius(){
		int DIMENSION = 3;
		double domain_low[] = new double[]{0,0,0};
		double domain_high[] = new double[]{10000,10000,5000};
		
		Region domain = new Region(DIMENSION);
		domain.setDimension(DIMENSION);
		domain.setLow(domain_low);
		domain.setHigh(domain_high);
		
		int num = 10; // # of queries per radius
		int radius = 0;
		int i = 0;
		for (radius = 10; radius <= 100; radius += 10){
			for (int k = 0; k < num; k++) {
				double l[] = new double[DIMENSION];
				double h[] = new double[DIMENSION];
				for (i = 0; i < DIMENSION; i++) {
					System.out.print(" ");
					l[i] = Math.random()
							* (domain_high[i] - domain_low[i] - radius);
					h[i] = l[i] + radius;
					System.out.print(l[i] + " " + h[i]);
				}
				System.out.println();
			}
		}
		
	}
	
	public static void main(String[] args){
		RangeQueryGenerator rg = new RangeQueryGenerator();
		rg.generateQueriesByVaringRadius();
	}
}
