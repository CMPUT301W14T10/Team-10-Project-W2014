package ca.ualberta.team10projectw2014.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.util.Log;
import ca.ualberta.team10projectw2014.models.CommentModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Handles sending CommentModels to the server and executing searches on the server.
 * Most of the code in this class is based on: https://github.com/rayzhangcl/ESDemo
 * and https://github.com/zjullion/PicPosterComplete
 * @author zjullion. Edited by sgiang92
 * 
 */
public class ElasticSearchOperations {
	
	public static final String SERVER_URL = "http://cmput301.softwareprocess.es:8080/testing/team10projectw2014/";
	public static final String LOG_TAG = "ElasticSearch";
	private static Gson GSON = null;
	
	
	/**
	 * Sends a Comment to the server.  Does nothing if the request fails.
	 * @param model a CommentModel
	 */
	public static void pushHeadComment(final CommentModel comment){
		if (GSON == null) {
			constructGson();

		}

		Thread thread = new Thread() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(SERVER_URL);

				try {
					request.setEntity(new StringEntity(GSON.toJson(comment)));
				}
				catch (UnsupportedEncodingException exception) {
					Log.w(LOG_TAG, "Error encoding Comment: " + exception.getMessage());
					return;
				}

				HttpResponse response;
				try {
					response = client.execute(request);
					Log.i(LOG_TAG, "Response: " + response.getStatusLine().toString());
				} 
				catch (IOException exception) {
					Log.w(LOG_TAG, "Error sending Comment: " + exception.getMessage());
				}
			}
		};

		thread.start();
	}
	
	/**
	 * Constructs a Gson with a custom serializer / desserializer registered for Bitmaps.
	 */
	private static void constructGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		GSON = builder.create();
	}

}
