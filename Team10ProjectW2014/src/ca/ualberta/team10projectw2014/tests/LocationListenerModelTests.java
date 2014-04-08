package ca.ualberta.team10projectw2014.tests;

import android.location.Location;
import android.location.LocationManager;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.team10projectw2014.controllersAndViews.MainListViewActivity;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.LocationListenerModel;

/**
 * @author       Bradley Poulette <bpoulett@ualberta.ca>
 * @version      1                (current version number of program)  <p>  Runs tests for CreateCommentActivity
 */
public class LocationListenerModelTests extends ActivityInstrumentationTestCase2<MainListViewActivity> {

	public LocationListenerModelTests() {
		super(MainListViewActivity.class);
	}

	/**
	 * @uml.property  name="activity"
	 * @uml.associationEnd  
	 */
	private MainListViewActivity activity;
	/**
	 * @uml.property  name="appState"
	 * @uml.associationEnd  
	 */
	private ApplicationStateModel appState;

	private LocationListenerModel listener;

	private MockLocationProvider mockLocationProvider = null;

	private LocationManager locationManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appState = ApplicationStateModel.getInstance();
	}

	public void testGetLocation() throws Throwable{
		activity = getActivity();
		listener = new LocationListenerModel(activity);
		locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
		if(!locationManager.isProviderEnabled(MockLocationProvider.MOCK_PROVIDER)){
			if(locationManager.getProvider(MockLocationProvider.MOCK_PROVIDER) == null){
				locationManager.addTestProvider(MockLocationProvider.MOCK_PROVIDER, false, false,
						false, false, true, false, false, 0, 5);
			}
			locationManager.setTestProviderEnabled(MockLocationProvider.MOCK_PROVIDER, true);
		}
		mockLocationProvider = new MockLocationProvider(activity, 3.0, 4.0);
		mockLocationProvider.execute();
		final LocationListenerModel locationListener = new LocationListenerModel(activity);
		Location userLocation = locationListener.getLastBestLocation();
		assertEquals("The location should be 3,4 as set in the MockLocationProvider", 3.0, userLocation.getLatitude());
		assertEquals("The location should be 3,4 as set in the MockLocationProvider", 4.0, userLocation.getLongitude());
		locationManager.setTestProviderEnabled(MockLocationProvider.MOCK_PROVIDER, false);
	}	
}

