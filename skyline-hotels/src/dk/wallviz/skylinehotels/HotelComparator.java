package dk.wallviz.skylinehotels;

import java.util.Comparator;

public class HotelComparator implements Comparator<Hotel> {
	
	private int field;
	private boolean asc;
	
	public HotelComparator(int field) {
		super();
		if (field<0 || field>3) {
			throw new RuntimeException("Attribute does not exist");
		}
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
		switch (field) {
		case Attributes.HOTEL_RATING:
			if (o1.hotelRating < o2.hotelRating)
				result = -1;
			else if (o1.hotelRating > o2.hotelRating)
				result = 1;
			else result = 0;
		break;
		case Attributes.TRIP_ADVISOR_RATING:
			if (o1.tripAdvisorRating < o2.tripAdvisorRating)
				result = -1;
			else if (o1.tripAdvisorRating > o2.tripAdvisorRating)
				result = 1;
			else result = 0;
		break;
		case Attributes.HIGH_RATE:
			if (o1.highRate < o2.highRate)
				result = -1;
			else if (o1.highRate > o2.highRate)
				result = 1;
			else result = 0;
		break;
		case Attributes.PROXIMITY_DISTANCE:
			if (o1.proximityDistance < o2.proximityDistance)
				result = -1;
			else if (o1.proximityDistance > o2.proximityDistance)
				result = 1;
			else result = 0;
		break;
		/*
		case DIST_FROM_COLOSSEUM:
		case DIST_FROM_TREVI_FOUNTAIN:
		*/
		}
		if (result==0)
			result = o1.id.compareTo(o2.id); // to have a fixed tie order
		if (asc) return result;
		else return -result;
	}
	
}
