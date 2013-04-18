package dk.wallviz.skylinehotels;

import java.util.Arrays;

public class RepresentativeSkyline {
	Hotel[] execute(Hotel[] skylineHotels) {
		Arrays.sort(skylineHotels,new HotelComparator(Attributes.HIGH_RATE));
		return skylineHotels;
	}
}
