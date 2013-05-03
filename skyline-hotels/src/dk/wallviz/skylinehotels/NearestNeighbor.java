package dk.wallviz.skylinehotels;

import java.util.*;

public class NearestNeighbor {
	private Collection<Integer> atts;
	private Hotel[] hotels;
	private Hotel target;
	private HotelPredicate minmax;
	
	public NearestNeighbor(Hotel[] hotels, Hotel target, Collection<Integer> atts) {
		super();
		this.atts = atts;
		this.target = target;
		this.hotels = hotels;
	}
	
	Hotel[] execute() {
		ArrayList<Hotel> result = new ArrayList<Hotel>();
	
		// find min/max values
		minmax = HotelPredicate.fit(hotels);
		// select one and collect the others in a decent data structure
		
		SortedSet<HotelDistance> set = new TreeSet<HotelDistance>();
		for (Hotel h: hotels) {
			if (h.equals(target)) continue;
			set.add(new HotelDistance(dist(h,target),h));
		}
		int r=0;
		for (HotelDistance hd: set) {
			result.add(hd.h);
			r++;
			if (r==5) break;
		} 
		return result.toArray(new Hotel[0]);
	}
	
	/**
	 * Representative distance
	 */
	private double representativeDistance(Hotel h, Collection<Hotel> representativeHotels) {
		double currentDistance = Double.POSITIVE_INFINITY;
		for (Hotel representativeHotel: representativeHotels) {
			double dist = dist(h, representativeHotel);
			if (dist<currentDistance) currentDistance = dist;
		}
		return currentDistance;
	}
	
	private double dist(Hotel h1, Hotel h2) {
		double dist = 0;
		for (int att: atts) {
			if (Hotel.isBoolean(att)) {
				if (h1.getBoolean(att) ^ h2.getBoolean(att))
					dist += 1;
			}
			else if (Hotel.isDouble(att)) {
				if (minmax.getMax(att)!=minmax.getMin(att))
					dist += Math.abs((h1.getDouble(att)-h2.getDouble(att))/(minmax.getMax(att)-minmax.getMin(att)));
			}
		}
		return dist/atts.size();
		
		/*int numIntermediatePoints = 0;
		for (Hotel h: skylineHotels) {
			if (atts.hotelRating) {
				if (((h1.hotelRating >= h.hotelRating) && (h.hotelRating >= h2.hotelRating)) ||
					((h2.hotelRating >= h.hotelRating) && (h.hotelRating >= h1.hotelRating)))
					numIntermediatePoints++;
			}
			if (atts.tripAdvisorRating) {
				if (((h1.tripAdvisorRating >= h.tripAdvisorRating) && (h.tripAdvisorRating >= h2.tripAdvisorRating)) ||
						((h2.tripAdvisorRating >= h.tripAdvisorRating) && (h.tripAdvisorRating >= h1.tripAdvisorRating)))
						numIntermediatePoints++;
				}
			if (atts.highRate) {
				if (((h1.highRate >= h.highRate) && (h.highRate >= h2.highRate)) ||
						((h2.highRate >= h.highRate) && (h.highRate >= h1.highRate)))
						numIntermediatePoints++;
				}
			if (atts.proximityDistance) {
				if (((h1.proximityDistance >= h.proximityDistance) && (h.proximityDistance >= h2.proximityDistance)) ||
						((h2.proximityDistance >= h.proximityDistance) && (h.proximityDistance >= h1.proximityDistance)))
						numIntermediatePoints++;
			}
			if (atts.distFromColosseum) {
				if (((h1.distFromColosseum >= h.distFromColosseum) && (h.distFromColosseum >= h2.distFromColosseum)) ||
						((h2.distFromColosseum >= h.distFromColosseum) && (h.distFromColosseum >= h1.distFromColosseum)))
						numIntermediatePoints++;
			}
			if (atts.distFromTreviFountain) {
				if (((h1.distFromTreviFountain >= h.distFromTreviFountain) && (h.distFromTreviFountain >= h2.distFromTreviFountain)) ||
						((h2.distFromTreviFountain >= h.distFromTreviFountain) && (h.distFromTreviFountain >= h1.distFromTreviFountain)))
						numIntermediatePoints++;
			}
			if (atts.pool) {
				if (h1.pool ^ h2.pool)
					numIntermediatePoints++;
			}
			if (atts.internet) {
				if (h1.internet ^ h2.internet)
					numIntermediatePoints++;
			}
		}
		return (double)numIntermediatePoints/(skylineHotels.length-1)/atts.length();*/
	}
}

class HotelDistance implements Comparable<HotelDistance> {
	public double dist;
	public Hotel h;
	public HotelDistance(double dist, Hotel h) {
		super();
		this.dist = dist;
		this.h = h;
	}
	
	@Override
	public int compareTo(HotelDistance o) {
		// TODO Auto-generated method stub
		if (dist>o.dist) return 1;
		if (dist<o.dist) return -1;
		return 0;
	}
	
	
}