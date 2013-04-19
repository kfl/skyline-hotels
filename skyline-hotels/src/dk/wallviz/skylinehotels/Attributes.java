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
	

	
	public int length() {
		int length = 0;
		if (hotelRating) length++;
		if (tripAdvisorRating) length++;
		if (highRate) length++;
		if (proximityDistance) length++;
		if (distFromColosseum) length++;
		if (distFromTreviFountain) length++;
		if (internet) length++;
		if (pool) length++;
		return length;
	}
}
