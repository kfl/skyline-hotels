package dk.wallviz.skylinehotels;

import java.util.*;

public class Skyline {
	Set<Hotel> window;
	private Collection<Integer> atts;
	private HotelMeasure measure;
	
	public Skyline(Collection<Integer> atts) {
		this.atts = atts;
		this.window = new HashSet<Hotel>();
	}

	public Hotel[] execute(Hotel[] input) {
		for (Hotel h: input) {
			Set<Hotel> dominatedHotels = new HashSet<Hotel>();
			boolean dominated = false;
			for (Hotel wh: window) {
				// check dominance
				int dom = measure.dominates(atts, h, wh);
				if (dom < 0) {
					// dominated - stop checking this hotel						
					dominated = true;
					break;
				}
				else if (dom > 0) {
					// set other dominated
					dominatedHotels.add(wh);
				}
				else {}
			}
			if (!dominated) {
				window.add(h);
			}
			window.removeAll(dominatedHotels);
		}
		
		ArrayList<Hotel> result = new ArrayList<Hotel>();
		for (Hotel sh: window) {
			result.add(sh);
		}
		return result.toArray(new Hotel[0]);
	}
}
