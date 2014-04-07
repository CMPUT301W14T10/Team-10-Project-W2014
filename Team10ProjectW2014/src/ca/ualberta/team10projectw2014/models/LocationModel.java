package ca.ualberta.team10projectw2014.models;

import android.location.Location;

/**
 * @author       Bradley Poulette <bpoulett@ualberta.ca>
 * @version      1                (current version number of program)  This class stores the latitude and longitude of a comment's location, along with a name for that location.
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

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 * @uml.property  name="latitude"
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 * @uml.property  name="latitude"
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return
	 * @uml.property  name="longitude"
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 * @uml.property  name="longitude"
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Generates a location
	 * 
	 * @return location
	 */
	public Location generateLocation(){
		Location loc = new Location("provider");
		loc.setLatitude(this.latitude);
		loc.setLongitude(this.longitude);
		return loc;
	}
	
}
