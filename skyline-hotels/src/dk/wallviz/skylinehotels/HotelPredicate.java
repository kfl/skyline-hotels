package dk.wallviz.skylinehotels;

public class HotelPredicate {
	int propertyCategoryStart = 0;
	int propertyCategoryEnd = 5;
	
	double hotelRatingStart = Double.NEGATIVE_INFINITY;
	double hotelRatingEnd = Double.POSITIVE_INFINITY;
	
	double tripAdvisorRatingStart = Double.NEGATIVE_INFINITY;
	double tripAdvisorRatingEnd = Double.POSITIVE_INFINITY;
	
	double highRateStart = Double.NEGATIVE_INFINITY;
	double highRateEnd = Double.POSITIVE_INFINITY;
	
	double proximityDistanceStart = Double.NEGATIVE_INFINITY;
	double proximityDistanceEnd = Double.POSITIVE_INFINITY;
	
	boolean internet = false;
	
	boolean pool = false;
	
	double distFromColosseumStart = Double.NEGATIVE_INFINITY;
	double distFromColosseumEnd = Double.POSITIVE_INFINITY;
	
	double distFromTreviFountainStart = Double.NEGATIVE_INFINITY;
	double distFromTreviFountainEnd = Double.POSITIVE_INFINITY;
		
	public boolean check(Hotel h) {
		if (h.propertyCategory<propertyCategoryStart) return false;
		if (h.propertyCategory>propertyCategoryEnd) return false;
		if (h.hotelRating<hotelRatingStart) return false;
		if (h.hotelRating>hotelRatingEnd) return false;
		if (h.tripAdvisorRating<tripAdvisorRatingStart) return false;
		if (h.tripAdvisorRating>tripAdvisorRatingEnd) return false;
		if (h.highRate<highRateStart) return false;
		if (h.highRate>highRateEnd) return false;
		if (h.proximityDistance<proximityDistanceStart) return false;
		if (h.proximityDistance>proximityDistanceEnd) return false;
		if (h.distFromColosseum<distFromColosseumStart) return false;
		if (h.distFromColosseum>distFromColosseumEnd) return false;
		if (h.distFromTreviFountain<distFromTreviFountainStart) return false;
		if (h.distFromTreviFountain>distFromTreviFountainEnd) return false;
		//if (h.<Start) return false;
		//if (h.>End) return false;
		if (!h.internet && internet) return false;
		if (!h.pool && pool) return false;
		return true;
	}
}
