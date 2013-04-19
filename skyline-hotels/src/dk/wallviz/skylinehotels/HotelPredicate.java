package dk.wallviz.skylinehotels;

public class HotelPredicate {
	double[] min;
	double[] max;
	boolean[] flag; 
		
	public HotelPredicate() {
		min = new double[Hotel.numDoubleAtts()];
		max = new double[Hotel.numDoubleAtts()];
		flag = new boolean[Hotel.numBooleanAtts()];
		for (int i=0; i<min.length; i++) min[i] = Double.NEGATIVE_INFINITY;
		for (int i=0; i<max.length; i++) max[i] = Double.POSITIVE_INFINITY;
		for (int i=0; i<flag.length; i++) flag[i] = false;
	}
	
	public boolean check(Hotel h) {
		for (int i=Hotel.NUM_OFFSET; i<Hotel.numDoubleAtts()+Hotel.NUM_OFFSET; i++) {
			if (h.getDouble(i)<getMin(i)) return false;
			if (h.getDouble(i)>getMax(i)) return false;
		}
		for (int i=Hotel.BOOL_OFFSET; i<Hotel.numBooleanAtts()+Hotel.BOOL_OFFSET; i++)
			if (!h.getBoolean(i) && getFlag(i)) return false;
		return true;
	}
	
	public void setMin(int field, double val) {
		min[field-Hotel.NUM_OFFSET] = val;
	}
	
	public void setMax(int field, double val) {
		max[field-Hotel.NUM_OFFSET] = val;
	}
	
	public void setFlag(int field, boolean val) {
		flag[field-Hotel.BOOL_OFFSET] = val;
	}
	
	public double getMin(int field) {
		return min[field-Hotel.NUM_OFFSET];
	}
	
	public double getMax(int field) {
		return max[field-Hotel.NUM_OFFSET];
	}
	
	public boolean getFlag(int field) {
		return flag[field-Hotel.BOOL_OFFSET];
	}
	
	
	/**
	 * Creates an HotelPredicate with the extreme values contained in the input data
	 * @return
	 */
	public static HotelPredicate fit(Hotel[] hotels) {
		HotelPredicate p = new HotelPredicate();
		for (int i=Hotel.NUM_OFFSET; i<Hotel.numDoubleAtts()+Hotel.NUM_OFFSET; i++) {
			p.setMin(i,hotels[0].getDouble(i));
			p.setMax(i,hotels[0].getDouble(i));
		}
		for (Hotel h: hotels) {
			for (int i=Hotel.NUM_OFFSET; i<Hotel.numDoubleAtts()+Hotel.NUM_OFFSET; i++) {
				if (p.getMin(i)>h.getDouble(i)) p.setMin(i,h.getDouble(i));
				if (p.getMax(i)<h.getDouble(i)) p.setMax(i,h.getDouble(i));
			}
		}
		return p;
	}
}
