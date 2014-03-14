/**
 * Copyright 2014 Cole Fudge, Steven Giang, Bradley Poulette, David Yee, and Costa Zervos

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
   */

package ca.ualberta.team10projectw2014;
import java.io.Serializable;
import java.lang.Math;

import android.location.Location;

/**
 * This class takes care of storing each location in the system.
 * Will be used more in Project Part 4 than Part 3.
 * Should not be aware of other locations.
 * @author Bradley Poulette
 * */

public class LocationModel{
	private String name;
	private double latitude;
	private double longitude;
	private Location location;
	
	//TODO Be sure to use the lat and lng from location, not latitude and longitude.

	public LocationModel(String name, Location location) {
		this.location = location;
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
	
	//uses the standard linear  distance equation to compare latitude and
	//longitude of two locations.
	public int distanceTo(LocationModel otherLocation){
		return (int) Math.sqrt(Math.pow((this.longitude - otherLocation.longitude), 2) + Math.pow((this.latitude - otherLocation.latitude), 2));
	}
	
}
