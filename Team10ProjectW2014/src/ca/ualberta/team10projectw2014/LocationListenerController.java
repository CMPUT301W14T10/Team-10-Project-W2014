package ca.ualberta.team10projectw2014;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationListenerController implements LocationListener {

	private Location currentBestLocation = null;
	private boolean gpsEnabled;
	private boolean netEnabled;
	private Context context;
	protected LocationManager mLocationManager;
	private Location locationGPS;
 	private Location locationNet;

	public LocationListenerController(Context context) {
		this.context = context;
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	    	gpsEnabled = false;
	        noGPSError();
	    }
	    
	    else{
	    	gpsEnabled = true;
	    }
	    
	    if ( !mLocationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {
	    	netEnabled = false;
	    }
	    else{
	    	netEnabled = true;
	    }

    	if (gpsEnabled){
    		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400 , 1, this);
    	}
    	if (netEnabled){
    		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400 , 1, this);
    	}
    	
    	Toast.makeText(context, "Current Best Location on create: " + currentBestLocation, Toast.LENGTH_LONG).show();
    	
	}
	
	// The following method is a direct copy from http://stackoverflow.com/questions/843675/how-do-i-find-out-if-the-gps-of-an-android-device-is-enabled received on March 9 at 2:00PM
		protected void noGPSError(){
			final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
		           .setCancelable(false)
		           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		                   context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		               }
		           })
		           .setNegativeButton("No", new DialogInterface.OnClickListener() {
		               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		                    dialog.cancel();
		               }
		           });
		    final AlertDialog alert = builder.create();
		    alert.show();
		}
	
	public Location getLastBestLocation() {
		if (!netEnabled && gpsEnabled){
			locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			makeUseOfNewLocation(locationGPS);
		}
		
		if (!gpsEnabled && netEnabled){
			locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			makeUseOfNewLocation(locationNet);
		}
		else if (gpsEnabled && netEnabled){
		
		    Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		    Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	
		    long GPSLocationTime = 0;
		    if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }
	
		    long NetLocationTime = 0;
	
		    if (null != locationNet) {
		        NetLocationTime = locationNet.getTime();
		    }
	
		    if ( 0 < GPSLocationTime - NetLocationTime ) {
		    	makeUseOfNewLocation(locationGPS);
		    }
		    else{
		    	makeUseOfNewLocation(locationNet);
		    }
		}
		Toast.makeText(context, "Current Best Location on search: " + currentBestLocation, Toast.LENGTH_LONG).show();
		return currentBestLocation;
	}

	private void makeUseOfNewLocation(Location location){
		if ( isBetterLocation(location, currentBestLocation) ) {
	        currentBestLocation = location;
	    }
	}
	
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

	// Taken from http://developer.android.com/guide/topics/location/strategies.html on March 6th at 4:00PM
	// Methods used to determine the most accurate location possible
	
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
}
