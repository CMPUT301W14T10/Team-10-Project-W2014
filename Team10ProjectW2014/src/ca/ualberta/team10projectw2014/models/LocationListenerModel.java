package ca.ualberta.team10projectw2014.models;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * @author       Bradley Poulette <bpoulett@ualberta.ca>
 * @version      1                (current version number of program)  This class is used to deal with listening for the user's most accurate current location and  returning that location upon request.
 */
public class LocationListenerModel implements LocationListener {

	private LocationListenerModelErrorHandler locationListenerModelErrorHandler = new LocationListenerModelErrorHandler();
	private Location currentBestLocation = null;
	private boolean gpsEnabled;
	private boolean netEnabled;
	private LocationManager mLocationManager;
	private Location locationGPS;
 	private Location locationNet;
 	
 	private static final int TWO_MINUTES = 1000 * 60 * 2;

 	
 	/**
 	 * Checks to see if either network or GPS are enabled, and asks user to turn GPS on if it is disabled.
 	 * 
 	 * @param context
 	 */
	public LocationListenerModel(Context context) {
		locationListenerModelErrorHandler.setContext(context);
		mLocationManager = (LocationManager)
				context.getSystemService(Context.LOCATION_SERVICE);
		
		if (!mLocationManager.isProviderEnabled(
				LocationManager.GPS_PROVIDER)) {
	    	gpsEnabled = false;
	        locationListenerModelErrorHandler.noGPSError();
	    }
	    
	    else{
	    	gpsEnabled = true;
	    }
	    
	    if (!mLocationManager.isProviderEnabled(
	    		LocationManager.NETWORK_PROVIDER)) {
	    	netEnabled = false;
	    }
	    else{
	    	netEnabled = true;
	    }

    	if (gpsEnabled){
    		mLocationManager.requestLocationUpdates(
    				LocationManager.GPS_PROVIDER, 400 , 1, this);
    	}
    	if (netEnabled){
    		mLocationManager.requestLocationUpdates(
    				LocationManager.NETWORK_PROVIDER, 400 , 1, this);
    	}
    	
    	currentBestLocation = getLastBestLocation();
	}
	

	
	/**
	 * Returns the best location between GPS and network, then returns the result.
	 * 
	 * @return the current location, null if networks are not on
 	 */
	public Location getLastBestLocation() {
		if (!netEnabled && gpsEnabled){
			locationGPS = mLocationManager.getLastKnownLocation(
					LocationManager.GPS_PROVIDER);
			makeUseOfNewLocation(locationGPS);
		}
		
		if (!gpsEnabled && netEnabled){
			locationNet = mLocationManager.getLastKnownLocation(
					LocationManager.NETWORK_PROVIDER);
			makeUseOfNewLocation(locationNet);
		}
		else if (gpsEnabled && netEnabled){
		
		    Location locationGPS = mLocationManager.getLastKnownLocation(
		    		LocationManager.GPS_PROVIDER);
		    Location locationNet = mLocationManager.getLastKnownLocation(
		    		LocationManager.NETWORK_PROVIDER);
	
		    long GPSLocationTime = 0;
		    if (null != locationGPS) {GPSLocationTime = locationGPS.getTime();}
	
		    long NetLocationTime = 0;
	
		    if (null != locationNet) {NetLocationTime = locationNet.getTime();}
	
		    if (0<GPSLocationTime - NetLocationTime) {
		    	makeUseOfNewLocation(locationGPS);
		    }
		    else{
		    	makeUseOfNewLocation(locationNet);
		    }
		}
		return currentBestLocation;
	}

	/**
	 * Checks to see if the current location is a better one than what is known, and sets the known location to it
	 * if this is true.
	 * 
	 * @param location
 	 */
	private void makeUseOfNewLocation(Location location){
		if ( isBetterLocation(location, currentBestLocation) ) {
	        currentBestLocation = location;
	    }
	}
	
	public void removeUpdates(){
		mLocationManager.removeUpdates(this);
	}
	
	/**
	 * Gets the current location of the user
	 * 
	 * @return location
	 */
	public Location getCurrentBestLocation(){
		return currentBestLocation;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		makeUseOfNewLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	/** Determines whether one Location reading is better than the current 
	  * Location reading.
	  * 
	  * From http://developer.android.com/guide/topics/location/strategies.html 
	  * Accessed on March 6th at 4:00PM
	  * 
	  * @param location
	  * 		The new Location that you want to evaluate
	  * @param currentBestLocation 
	  * 		The current Location fix, to which you want to compare the 
	  * 		new one
	  * @return <code>true</code> if new location is better,
	  * 		<code>false</code> otherwise;
	  */
	protected boolean isBetterLocation(
			Location location, 
			Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, 
	    // use the new location because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) 
	    		(location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness 
	    // and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && 
	    		!isSignificantlyLessAccurate && 
	    		isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same using a string comparison. 
	 * 
	 * 	From http://developer.android.com/guide/topics/location/strategies.html 
	 *  Accessed on March 6th at 4:00PM
	 *  
	 * 	@param provider1 the first provider to compare
	 *  @param provider2 the second provider to compare
     *  @return <code>true</code> if the specified object is equal to this 
     *           string, <code>false</code> otherwise.
	 */ 
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
}
