package dk.wallviz.skylinehotels;

import java.util.*;

public class HotelMeasure {
	
	public static int dominates(Collection<Integer> atts, Hotel h1, Hotel h2) {
		boolean notDominates = false;
		boolean notDominated = false;
		Iterator<Integer> it = atts.iterator();
		while (it.hasNext()) {
			int att = it.next();
			switch (att) {
			case Hotel.HOTEL_RATING:
			case Hotel.TRIP_ADVISOR_RATING:
				if (h1.getDouble(att) < h2.getDouble(att)) notDominates = true;
				if (h1.getDouble(att) > h2.getDouble(att)) notDominated = true;
				if (notDominates && notDominated) return 0;
				break;
			case Hotel.HIGH_RATE:
			case Hotel.PROXIMITY_DISTANCE:
			case Hotel.DIST_FROM_COLOSSEUM:
			case Hotel.DIST_FROM_TREVI_FOUNTAIN:
				if (h1.getDouble(att) > h2.getDouble(att)) notDominates = true;
				if (h1.getDouble(att) < h2.getDouble(att)) notDominated = true;
				if (notDominates && notDominated) return 0;
				break;
			case Hotel.INTERNET:
			case Hotel.POOL:
				if (!h1.getBoolean(att) && h2.getBoolean(att)) notDominates = true;
				if (h1.getBoolean(att) && !h2.getBoolean(att)) notDominated = true;
				if (notDominates && notDominated) return 0;
				break;
			}
		}
		if (!notDominated && notDominates) return -1;
		else if (notDominated && !notDominates) return 1;
		else return 0;
	}
	
	public double distance(Hotel h1, Hotel h2, Hotel[] hotels) {
		return 0;
	}
}
