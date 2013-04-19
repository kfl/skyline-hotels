package dk.wallviz.skylinehotels;

import java.util.Comparator;

public class HotelComparator implements Comparator<Hotel> {
	
	private int field;
	private boolean asc;
	
	public HotelComparator(int field) {
		super();
		this.field = field;
		this.asc = true;
	}
	
	public HotelComparator(int field, boolean asc) {
		this(field);
		this.asc = asc;
	}

	@Override
	public int compare(Hotel o1, Hotel o2) {
		int result = 0;
		if (o1.getDouble(field) < o2.getDouble(field))
			result = -1;
		else if (o1.getDouble(field) > o2.getDouble(field))
			result = 1;
		else result = 0;
		// break tie: if same value, sort by identifier
		if (result==0)
			result = o1.getString(Hotel.ID).compareTo(o2.getString(Hotel.ID)); // to have a fixed tie order
		// ascending or descending
		if (asc) return result;
		else return -result;
	}
	
}
