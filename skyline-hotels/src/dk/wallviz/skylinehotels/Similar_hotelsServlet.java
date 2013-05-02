package dk.wallviz.skylinehotels;

import java.io.*;
import javax.servlet.http.*;
import java.util.*;

@SuppressWarnings("serial")
public class Similar_hotelsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// retrieving hotel data
		Hotel[] hotels = getData();
		//resp.getWriter().println(hotels.length+" hotels read<br/>");
		
		// get parameters
		String relAtts = req.getParameter("attributes");
		String id = req.getParameter("hotelId");
		Collection<Integer> atts;
		if (relAtts != null) {
			atts = Hotel.getAttributes(relAtts,",");
		}
		else {
			atts = new HashSet<Integer>();
			atts.add(Hotel.PRICE);
		}
		
		// sorting
		Hotel h = getById(hotels,id);
		//System.out.println(h);
		Hotel[] nearHotels = new Hotel[0];
		if (h!=null) {
			NearestNeighbor near = new NearestNeighbor(hotels,h,atts);
			nearHotels = near.execute();
		}
		
		resp.setContentType("application/json");
		resp.getWriter().println(toJSON(nearHotels));
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
			Hotel h = new Hotel(r[0], // id
					r[1], // name
					r[2], // address1
					r[3], // city
					r[4], // postalCode
					d(r[5]), // hotelRating
					d(r[6]), // tripAdvisorRating
					d(r[7]), // lowRate
					d(r[8]), // lat
					d(r[9]), // long
					r[10], // description
					d(r[11]), // distFromTreviFountain
					d(r[12]), // distFromColosseum 
					d(r[13]), // distFromVatican
					r[14].equals("Y"), // internet
					r[15].equals("Y"), // business center
					r[16].equals("Y"), // fitness center
					r[17].equals("Y"), // pool
					r[0]+".jpg", // picture
					(int)d(r[5]));
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
	
	private Hotel getById(Hotel[] hotels, String id) {
		for (Hotel h: hotels) {
			//System.out.println(h.getString(Hotel.ID));
			if (h.getString(Hotel.ID).equals(id))
				return h;
		}
		return null;
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
