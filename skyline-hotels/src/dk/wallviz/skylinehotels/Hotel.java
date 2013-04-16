package dk.wallviz.skylinehotels;

public class Hotel implements Record {
	String id;
	String name;
	String address1;
	String city;
	String postalCode;
	int propertyCategory;
	double hotelRating;
	//double confidenceRating":84,
	double tripAdvisorRating;
	double highRate;
	double lat;
	double lon;
	double proximityDistance;
	boolean internet;
	boolean pool;
	double distFromColosseum;
	double distFromTreviFountain;
	String picture;
	
	public Hotel(String id, String name, String address1, String city,
			String postalCode, int propertyCategory, double hotelRating,
			double tripAdvisorRating, double highRate, double lat, double lon,
			double proximityDistance, boolean internet, boolean pool,
			double distFromColosseum, double distFromTreviFountain,
			String picture) {
		super();
		this.id = id;
		this.name = name;
		this.address1 = address1;
		this.city = city;
		this.postalCode = postalCode;
		this.propertyCategory = propertyCategory;
		this.hotelRating = hotelRating;
		this.tripAdvisorRating = tripAdvisorRating;
		this.highRate = highRate;
		this.lat = lat;
		this.lon = lon;
		this.proximityDistance = proximityDistance;
		this.internet = internet;
		this.pool = pool;
		this.distFromColosseum = distFromColosseum;
		this.distFromTreviFountain = distFromTreviFountain;
		this.picture = picture;
	}
	
	public String toJSON(int order) {
		return "{\"order\":"+order+","+
			  "\"id\":"+id+","+
			  "\"name\":\""+name+"\","+
			  "\"address1\":\""+address1+"\","+
			  "\"city\":\""+city+"\","+
			  "\"postalCode\":\""+postalCode+"\","+
			  "\"propertyCategory\":"+propertyCategory+","+
			  "\"hotelRating\":"+hotelRating+","+
			  //"\"confidenceRating\":"+confidenceRating+","+
			  "\"tripAdvisorRating\":"+tripAdvisorRating+","+
			  "\"highRate\":"+highRate+","+
			  "\"lat\":"+lat+","+
			  "\"lon\":"+lon+","+
			  "\"proximityDistance\":"+proximityDistance+","+
			  "\"internet\":"+internet+","+
			  "\"pool\":"+pool+","+
			  "\"distFromColosseum\":"+distFromColosseum+","+
			  "\"distFromTreviFountain\":"+distFromTreviFountain+","+
			  "\"picture\":\""+picture+"\"}";
	} 
}
