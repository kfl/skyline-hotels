package dk.wallviz.skylinehotels;

public class Attributes {
	boolean hotelRating;
	boolean tripAdvisorRating;
	boolean highRate;
	boolean proximityDistance;
	boolean distFromColosseum;
	boolean distFromTreviFountain;
	boolean internet;
	boolean pool;

	static final int HOTEL_RATING = 0;
	static final int TRIP_ADVISOR_RATING = 1;
	static final int HIGH_RATE = 2;
	static final int PROXIMITY_DISTANCE = 3;
	//static final int DIST_FROM_COLOSSEUM = 4;
	//static final int DIST_FROM_TREVI_FOUNTAIN = 5;

	public Attributes() {
		super();
	}
	
	public Attributes(boolean hotelRating, boolean tripAdvisorRating,
			boolean highRate, boolean proximityDistance,
			boolean distFromColosseum, boolean distFromTreviFountain,
			boolean internet, boolean pool) {
		super();
		this.hotelRating = hotelRating;
		this.tripAdvisorRating = tripAdvisorRating;
		this.highRate = highRate;
		this.proximityDistance = proximityDistance;
		this.distFromColosseum = distFromColosseum;
		this.distFromTreviFountain = distFromTreviFountain;
		this.internet = internet;
		this.pool = pool;
	}
	
	public static Attributes parse(String attributes, String sep) {
		String[] atts = attributes.split(sep);
		Attributes res = new Attributes();
		for (String val: atts) {
				if (val.equals("hotelRating"))
					res.hotelRating = true;
				else if (val.equals("tripAdvisorRating"))
					res.tripAdvisorRating = true;
				else if (val.equals("highRate"))
					res.highRate=true;
				else if (val.equals("proximityDistance"))
					res.proximityDistance = true;
				else if (val.equals("distFromColosseum"))
					res.distFromColosseum = true;
				else if (val.equals("distFromTreviFountain"))
					res.distFromTreviFountain = true;
				else if (val.equals("internet"))
					res.internet = true;
				else if (val.equals("pool"))
					res.pool = true;
				else throw new RuntimeException("Attribute " + val + " not recognized");
		}
		return res;
	}
	
	public static int getAttribute(String val) {
		if (val.equals("hotelRating"))
			return HOTEL_RATING;
		else if (val.equals("tripAdvisorRating"))
			return TRIP_ADVISOR_RATING;
		else if (val.equals("highRate"))
			return HIGH_RATE;
		else if (val.equals("proximityDistance"))
			return PROXIMITY_DISTANCE;
		else throw new RuntimeException("Sorting not allowed on this attribute");
	}
}
