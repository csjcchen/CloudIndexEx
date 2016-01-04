import java.util.Arrays;


public class Region {
	int dimension;
	double[] low = new double[dimension];
	double[] high = new double[dimension];
	
	public Region(int dimension){
		this.dimension = dimension;
		low = new double[dimension];
		high = new double[dimension];
	}
	public void setLow(double[] l){
		low = Arrays.copyOf(l, dimension);
	}
	
	public void setHigh(double[] h){
		high = Arrays.copyOf(h,dimension);
	}
	
	public int getDimension(){
		return dimension;
	}
	public void setDimension(int d){
		this.dimension = d;
	}
}
