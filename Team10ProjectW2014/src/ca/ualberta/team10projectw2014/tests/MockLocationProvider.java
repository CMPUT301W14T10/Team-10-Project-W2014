package ca.ualberta.team10projectw2014.tests;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

//Borrowed from MockLocationProvider from CMPUT 301 lab
/** Define a mock GPS provider as an asynchronous task of this Activity. */
public class MockLocationProvider extends AsyncTask<String, Void, Void> {
	public static final String MOCK_PROVIDER = "mockLocationProvider";
	private Context context;
	private double latitude;
	private double longitude;

	public MockLocationProvider(Context context, double latitude, double longitude){
		this.context = context;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	protected Void doInBackground(String... data) {			

		// set the Location object
		Location location = new Location(MOCK_PROVIDER);
		location.setLatitude(this.latitude);
		location.setLongitude(this.longitude);
		location.setTime(System.currentTimeMillis());

		// provide the new location
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.setTestProviderLocation(MOCK_PROVIDER, location);

		// wait for a while and then process the next line
		SystemClock.sleep(200);

		return null;
	}

}