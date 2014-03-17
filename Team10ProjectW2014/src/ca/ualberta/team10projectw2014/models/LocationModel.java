package ca.ualberta.team10projectw2014.models;

/**
 * @author      Bradley Poulette <bpoulett@ualberta.ca>
 * @version     1                (current version number of program)
 * 
 * This class stores the latitude and longitude of a comment's location, along with a name for that location.
 */
public class LocationModel{
	private String name;
	private double latitude;
	private double longitude;
	
	public LocationModel(String name, double lat, double lng) {
		this.latitude = lat;
		this.longitude = lng;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
