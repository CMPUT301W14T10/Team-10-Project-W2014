package ca.ualberta.team10projectw2014.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.util.Log;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.LocationModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * This class provides the methods to interact location data in the server.
 * 
 * @author David Yee
 * @version      1                (current version number of program)
 */
public class ElasticSearchLocationOperations {
	public static final String SERVER_URL = "http://cmput301.softwareprocess.es:8080/cmput301w14t10/location";
	public static final String LOG_TAG = "ElasticSearchLocation";
	private static Gson GSON = null;

	/**
	 * Sends a Comment to the server. Does nothing if the request fails.
	 * 
	 * @param model
	 *            a CommentModel
	 */
	public static void pushLocationList(final LocationModel location) {
		if (GSON == null)
			constructGson();

		Thread thread = new Thread() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(SERVER_URL);

				try {
					request.setEntity(new StringEntity(GSON.toJson(location)));
				} catch (UnsupportedEncodingException exception) {
					Log.w(LOG_TAG,
							"Error encoding Location List: " + exception.getMessage());
					return;
				}

				HttpResponse response;
				try {
					response = client.execute(request);
					Log.i(LOG_TAG, "Response: "
							+ response.getStatusLine().toString());
				} catch (IOException exception) {
					Log.w(LOG_TAG,
							"Error sending Location List: " + exception.getMessage());
				}
			}
		};

		thread.start();
		try
		{
			thread.join();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Searches the server for CommentModels with the given searchTerm in their
	 * text.
	 * 
	 * @param searchTerm
	 *            the single world term to search for
	 * @param model
	 *            the ArrayList of CommentModels to clear and then fill with the
	 *            new data
	 * @param activity
	 *            a MainListViewActivity
	 * 
	 * @author zjullion; adapted by dvyee
	 */
	public static void getLocationList(final Activity activity) {
		final ArrayList<LocationModel> locationList = new ArrayList<LocationModel>();
		if (GSON == null)
			constructGson();

		Thread thread = new Thread() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(SERVER_URL + "_search");
				String query = "{\"query\": {\"match_all\": {}}}";
				String responseJson = "";

				try {
					request.setEntity(new StringEntity(query));
				} catch (UnsupportedEncodingException exception) {
					Log.w(LOG_TAG,
							"Error encoding search query: "
									+ exception.getMessage());
					return;
				}

				try {
					HttpResponse response = client.execute(request);
					Log.i(LOG_TAG, "Response: "
							+ response.getStatusLine().toString());

					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent()));

					String output = reader.readLine();
					while (output != null) {
						responseJson += output;
						output = reader.readLine();
					}
				} catch (IOException exception) {
					Log.w(LOG_TAG, "Error receiving search query response: "
							+ exception.getMessage());
					return;
				}

				Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<LocationModel>>() {
				}.getType();
				final ElasticSearchSearchResponse<LocationModel> returnedData = GSON
						.fromJson(responseJson, elasticSearchSearchResponseType);

				Runnable updateModel = new Runnable() {
					@Override
					public void run() {
						locationList.clear();
						locationList.addAll(returnedData.getSources());
						ApplicationStateModel appState = ApplicationStateModel
                                .getInstance();
						appState.setLocationList(locationList);
						appState.saveLocations();
						
						Log.e(LOG_TAG, locationList.toString()); // print out the
						// entire contents of the list
					}
				};

				activity.runOnUiThread(updateModel);
			}
		};

		thread.start();
		try
		{
			thread.join();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Constructs a Gson with a custom serializer / desserializer registered for
	 * Bitmaps.
	 */
	private static void constructGson() {
		GsonBuilder builder = new GsonBuilder();
		GSON = builder.create();
	}

}
