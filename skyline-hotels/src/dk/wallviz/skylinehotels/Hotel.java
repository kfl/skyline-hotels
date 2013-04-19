package dk.wallviz.skylinehotels;

import java.util.*;

class Hotel implements Record {
	// OFFSETS
	static final int CH_OFFSET = 0;
	static final int NUM_OFFSET = 10;
	static final int BOOL_OFFSET = 20;
	// String attributes
	static final int ID = 0;
	static final int NAME = 1;
	static final int ADDRESS1 = 2;
	static final int CITY = 3;
	static final int POSTAL_CODE = 4;
	static final int PICTURE = 5;
	// Numerical attributes
	static final int HOTEL_RATING = 0 + NUM_OFFSET;
	static final int TRIP_ADVISOR_RATING = 1 + NUM_OFFSET;
	static final int HIGH_RATE = 2 + NUM_OFFSET;
	static final int LAT = 3 + NUM_OFFSET;
	static final int LON = 4 + NUM_OFFSET;
	static final int PROXIMITY_DISTANCE = 5 + NUM_OFFSET;
	static final int DIST_FROM_COLOSSEUM = 6 + NUM_OFFSET;
	static final int DIST_FROM_TREVI_FOUNTAIN = 7 + NUM_OFFSET;
	// Boolean attributes
	static final int POOL = 0 + BOOL_OFFSET;
	static final int INTERNET = 1 + BOOL_OFFSET;
	
	
	private String[] stringAtts = new String[6];
	private double[] doubleAtts = new double[8];
	private boolean[] booleanAtts = new boolean[2];
	
	public Hotel(String id, String name, String address1, String city,
			String postalCode, double hotelRating,
			double tripAdvisorRating, double highRate, double lat, double lon,
			double proximityDistance, boolean internet, boolean pool,
			double distFromColosseum, double distFromTreviFountain,
			String picture) {
		super();
		setString(ID,id);
		setString(NAME,name);
		setString(ADDRESS1,address1);
		setString(CITY,city);
		setString(POSTAL_CODE,postalCode);
		setDouble(HOTEL_RATING,hotelRating);
		setDouble(TRIP_ADVISOR_RATING,tripAdvisorRating);
		setDouble(HIGH_RATE,highRate);
		setDouble(LAT,lat);
		setDouble(LON,lon);
		setDouble(PROXIMITY_DISTANCE,proximityDistance);
		setBoolean(INTERNET,internet);
		setBoolean(POOL,pool);
		setDouble(DIST_FROM_COLOSSEUM,distFromColosseum);
		setDouble(DIST_FROM_TREVI_FOUNTAIN,distFromTreviFountain);
		// custom conversion for this application: data might be placed in different directories
		int numericId = Integer.parseInt(id);
		//System.out.println(picture);
		String path = "";
		if (numericId <= 297078)
			path = "hotel_images/low/";
		else path = "hotel_images/high/";
		setString(PICTURE,path + picture);
	}
	
	public String toJSON(int order) {
		return "{\"order\":"+order+","+
			  "\"id\":"+getString(ID)+","+
			  "\"name\":\""+getString(NAME)+"\","+
			  "\"address1\":\""+getString(ADDRESS1)+"\","+
			  "\"city\":\""+getString(CITY)+"\","+
			  "\"postalCode\":\""+getString(POSTAL_CODE)+"\","+
			  "\"hotelRating\":"+getDouble(HOTEL_RATING)+","+
			  //"\"confidenceRating\":"+confidenceRating+","+
			  "\"tripAdvisorRating\":"+getDouble(TRIP_ADVISOR_RATING)+","+
			  "\"highRate\":"+getDouble(HIGH_RATE)+","+
			  "\"lat\":"+getDouble(LAT)+","+
			  "\"lon\":"+getDouble(LON)+","+
			  "\"proximityDistance\":"+getDouble(PROXIMITY_DISTANCE)+","+
			  "\"internet\":"+getBoolean(INTERNET)+","+
			  "\"pool\":"+getBoolean(POOL)+","+
			  "\"distFromColosseum\":"+getDouble(DIST_FROM_COLOSSEUM)+","+
			  "\"distFromTreviFountain\":"+getDouble(DIST_FROM_TREVI_FOUNTAIN)+","+
			  "\"picture\":\""+getString(PICTURE)+"\"}";
	}
	
	public String getString(int field) {
		return stringAtts[field-CH_OFFSET];
	}
	
	public double getDouble(int field) {
		return doubleAtts[field-NUM_OFFSET];
	}

	public boolean getBoolean(int field) {
		return booleanAtts[field-BOOL_OFFSET];
	}

	public void setString(int field, String val) {
		 stringAtts[field-CH_OFFSET] = val;
	}
	
	public void setDouble(int field, double val) {
		 doubleAtts[field-NUM_OFFSET] = val;
	}

	public void setBoolean(int field, boolean val) {
		 booleanAtts[field-BOOL_OFFSET] = val;
	}
	
	public static int numStringAtts() {
		return 6;
	}

	public static int numDoubleAtts() {
		return 8;
	}

	public static int numBooleanAtts() {
		return 2;
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
		else if (val.equals("distFromColosseum"))
			return DIST_FROM_COLOSSEUM;
		else if (val.equals("distFromTreviFountain"))
			return DIST_FROM_TREVI_FOUNTAIN;
		// So far, this method is only used for sorting
		else throw new RuntimeException("Sorting not allowed on this attribute");
	}
	
	public static Collection<Integer> getAttributes(String attributes, String sep) {
		String[] atts = attributes.split(sep);
		Collection<Integer> res = new HashSet<Integer>();
		for (String val: atts) {
				if (val.equals("hotelRating"))
					res.add(HOTEL_RATING);
				else if (val.equals("tripAdvisorRating"))
					res.add(TRIP_ADVISOR_RATING);
				else if (val.equals("highRate"))
					res.add(HIGH_RATE);
				else if (val.equals("proximityDistance"))
					res.add(PROXIMITY_DISTANCE);
				else if (val.equals("distFromColosseum"))
					res.add(DIST_FROM_COLOSSEUM);
				else if (val.equals("distFromTreviFountain"))
					res.add(DIST_FROM_TREVI_FOUNTAIN);
				else if (val.equals("internet"))
					res.add(INTERNET);
				else if (val.equals("pool"))
					res.add(POOL);
				else throw new RuntimeException("Attribute " + val + " not recognized");
		}
		return res;
	}
	
	public static boolean isBoolean(int att) {
		if (att>=BOOL_OFFSET) return true;
		else return false;
	}

	public static boolean isDouble(int att) {
		if (att>=NUM_OFFSET && att<BOOL_OFFSET) return true;
		else return false;
	}
}

