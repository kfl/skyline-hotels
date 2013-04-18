package dk.wallviz.skylinehotels;

public class HotelMeasure {
	Attributes atts;
	
	public HotelMeasure(Attributes atts) {
		this.atts = atts;
	}

	public int dominates(Hotel h1, Hotel h2) {
		boolean notDominates = false;
		boolean notDominated = false;
		if (atts.hotelRating) {
			if (h1.hotelRating < h2.hotelRating) notDominates = true;
			if (h1.hotelRating > h2.hotelRating) notDominated = true;
			if (notDominates && notDominated) return 0;
		}
		if (atts.tripAdvisorRating) {
			if (h1.tripAdvisorRating < h2.tripAdvisorRating) notDominates = true;
			if (h1.tripAdvisorRating > h2.tripAdvisorRating) notDominated = true;
			if (notDominates && notDominated) return 0;
		}
		if (atts.highRate) {
			if (h1.highRate > h2.highRate) notDominates = true;
			if (h1.highRate < h2.highRate) notDominated = true;
			if (notDominates && notDominated) return 0;
		}
		if (atts.proximityDistance) {
			if (h1.proximityDistance > h2.proximityDistance) notDominates = true;
			if (h1.proximityDistance < h2.proximityDistance) notDominated = true;
			if (notDominates && notDominated) return 0;
		}
		if (atts.distFromColosseum) {
			if (h1.distFromColosseum > h2.distFromColosseum) notDominates = true;
			if (h1.distFromColosseum < h2.distFromColosseum) notDominated = true;
			if (notDominates && notDominated) return 0;
		}
		if (atts.distFromTreviFountain) {
			if (h1.distFromTreviFountain > h2.distFromTreviFountain) notDominates = true;
			if (h1.distFromTreviFountain < h2.distFromTreviFountain) notDominated = true;
			if (notDominates && notDominated) return 0;
		}
		if (atts.pool) {
			if (!h1.pool && h2.pool) notDominates = true;
			if (h1.pool && !h2.pool) notDominated = true;
			if (notDominates && notDominated) return 0;
		}
		if (atts.internet) {
			if (!h1.internet && h2.internet) notDominates = true;
			if (h1.internet && !h2.internet) notDominated = true;
			if (notDominates && notDominated) return 0;
		}
		if (!notDominated && notDominates) return -1;
		else if (notDominated && !notDominates) return 1;
		else return 0;
	}
	
	public double distance(Hotel h1, Hotel h2, Hotel[] hotels) {
		return 0;
	}
}
