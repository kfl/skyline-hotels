package dk.wallviz.skylinehotels;

import java.io.*;
import javax.servlet.http.*;
import java.util.*;

@SuppressWarnings("serial")
public class Skyline_hotelsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// retrieving hotel data
		Hotel[] hotels = getData();
		//resp.getWriter().println(hotels.length+" hotels read<br/>");
		
		// filtering - not efficient, but simple code
		HotelPredicate predicate = buildFilter(req);
		hotels = filter(hotels,predicate);
		//resp.getWriter().println(hotels.length+" hotels filtered<br/>");
		
		// skyline and sorting
		String skylineOf = req.getParameter("skylineOf");
		String sortBy = req.getParameter("sortBy");
		if (skylineOf != null) {
			Collection<Integer> atts = Hotel.getAttributes(skylineOf,",");
			Skyline sky = new Skyline(atts);
			hotels = sky.execute(hotels);
			
			// sort by representative skyline?
			if (sortBy != null && sortBy.equals("skyline")) {
					RepresentativeSkyline rep = new RepresentativeSkyline(hotels,atts);
					hotels = rep.execute();
					//System.out.println(hotels.length);
				}
			//resp.getWriter().println(hotels.length+" hotels in the skyline<br/>");
		}
		
		// sorting
		if (sortBy != null && !sortBy.equals("skyline")) {
			int attribute = Hotel.getAttribute(sortBy); 
			String sortType = req.getParameter("sortType");
			boolean asc = true;
			if (sortType != null && sortType.equals("desc"))
				asc = false;
			Arrays.sort(hotels,new HotelComparator(attribute,asc));
		}
		
		resp.setContentType("application/json");
		resp.getWriter().println(toJSON(hotels));
	}
	
	private Hotel[] getData() throws IOException {
		/*	DATA:
		* 	0 order	id	name	address1	city
		*	5 postalCode	propertyCategory	hotelRating	confidenceRating	tripAdvisorRating
		*	10 highRate	lat	long	proximityDistance	Business Center
		*   15 Fitness Center	Hot Tub On-site	Internet Access Available	Kids Activities	Kitchen or Kitchenette	
		*   20 Pets Allowed	Pool	Restaurant On-site	Spa On-site	Whirlpool Bath Available
		*   25 Breakfast	Babysitting	Jacuzzi	Parking	Room Service
		*   30 Accessible Path of Travel	Accessible Bathroom	Roll-in Shower	Handicapped Parking	In-room Accessibility	Accessibility Equipment for the Deaf	
		*   35 Braille or Raised Signage	Free Airport Shuttle	Indoor Pool	Outdoor Pool	Extended Parking	
		*   39 Free Parking	DistFromColosseum	DistFromTreviFountain	picture
		*   OBJECT:
		*    id,  name,  address1,  city,
			 postalCode,  propertyCategory,  hotelRating,
			 tripAdvisorRating,  highRate,  lat,  lon,
			 proximityDistance,  internet,  pool,
			 distFromColosseum,  distFromTreviFountain,
			 picture
		*/
		LineNumberReader in = new LineNumberReader(new FileReader("assets/data/hotel_data.tsv"));
		in.readLine(); // skip header
		String hotel;
		ArrayList<Hotel> list = new ArrayList<Hotel>();
		while ((hotel=in.readLine())!=null) {
			String[] r = hotel.split("\t");
			Hotel h = new Hotel(r[1], // id
					r[2], // name
					r[3], // address1
					r[4], // city
					r[5], // postalCode
					d(r[7]), // hotelRating
					d(r[9]), // tripAdvisorRating
					d(r[10]), // highRate
					d(r[11]), // lat
					d(r[12]), // long
					d(r[13]), // proximityDistance
					r[14].equals("Y"), // business center
					r[15].equals("Y"), // fitness center
					r[17].equals("Y"), // internet
					r[38].equals("Y"), // pool
					d(r[42]), // distFromColosseum 
					d(r[43]), // distFromTreviFountain
					r[44], // picture
					"4");
			list.add(h);
		}
		in.close();
		return list.toArray(new Hotel[0]);
	}
	
	HotelPredicate buildFilter(HttpServletRequest req) {
		HotelPredicate pred = new HotelPredicate();
		String val;
		if ((val = req.getParameter("hotelRatingStart"))!=null)
			pred.setMin(Hotel.HOTEL_RATING,Double.parseDouble(val));
		if ((val = req.getParameter("hotelRatingEnd"))!=null)
			pred.setMax(Hotel.HOTEL_RATING,Double.parseDouble(val));
		if ((val = req.getParameter("tripAdvisorRatingStart"))!=null)
			pred.setMin(Hotel.TRIP_ADVISOR_RATING,Double.parseDouble(val));
		if ((val = req.getParameter("tripAdvisorRatingEnd"))!=null)
			pred.setMax(Hotel.TRIP_ADVISOR_RATING,Double.parseDouble(val));
		if ((val = req.getParameter("priceStart"))!=null)
			pred.setMin(Hotel.PRICE,Double.parseDouble(val));
		if ((val = req.getParameter("priceEnd"))!=null)
			pred.setMax(Hotel.PRICE,Double.parseDouble(val));
		if ((val = req.getParameter("distFromVaticanStart"))!=null)
			pred.setMin(Hotel.DIST_FROM_VATICAN,Double.parseDouble(val));
		if ((val = req.getParameter("distFromVaticanEnd"))!=null)
			pred.setMax(Hotel.DIST_FROM_VATICAN,Double.parseDouble(val));
		if ((val = req.getParameter("distFromColosseumStart"))!=null)
			pred.setMin(Hotel.DIST_FROM_COLOSSEUM,Double.parseDouble(val));
		if ((val = req.getParameter("distFromColosseumEnd"))!=null)
			pred.setMax(Hotel.DIST_FROM_COLOSSEUM,Double.parseDouble(val));
		if ((val = req.getParameter("distFromTreviFountainStart"))!=null)
			pred.setMin(Hotel.DIST_FROM_TREVI_FOUNTAIN,Double.parseDouble(val));
		if ((val = req.getParameter("distFromTreviFountainEnd"))!=null)
			pred.setMax(Hotel.DIST_FROM_TREVI_FOUNTAIN,Double.parseDouble(val));
		if ((val = req.getParameter("internet"))!=null)
			pred.setFlag(Hotel.INTERNET,Boolean.parseBoolean(val));
		if ((val = req.getParameter("pool"))!=null)
			pred.setFlag(Hotel.POOL,Boolean.parseBoolean(val));
		if ((val = req.getParameter("businessCenter"))!=null)
			pred.setFlag(Hotel.BUSINESS_CENTER,Boolean.parseBoolean(val));
		if ((val = req.getParameter("fitnessCenter"))!=null)
			pred.setFlag(Hotel.FITNESS_CENTER,Boolean.parseBoolean(val));
		return pred;
	}
	
	private Hotel[] filter(Hotel[] hotels, HotelPredicate pred) {
		ArrayList<Hotel> list = new ArrayList<Hotel>();
		for (Hotel h: hotels)
			if (pred.check(h))
				list.add(h);
		return list.toArray(new Hotel[0]);
	}
	
	private String toJSON(Hotel[] hotels) {
		StringBuffer buf = new StringBuffer("[");
		for (int i = 0; i<hotels.length; i++) {
			buf.append(hotels[i].toJSON(i));
			if (i!=hotels.length-1) buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}
	
	private double d(String s) {
		if (s.equals("")) return -1;
		return Double.parseDouble(s);
	}
}
