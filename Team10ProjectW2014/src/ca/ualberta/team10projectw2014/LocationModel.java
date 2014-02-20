package ca.ualberta.team10projectw2014;

public class LocationModel {
	private String name;
	private String latitude;
	private String longitude;

	public LocationModel(String name, String latitude, String longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
}
