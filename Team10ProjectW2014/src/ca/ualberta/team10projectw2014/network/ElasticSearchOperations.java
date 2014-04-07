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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import android.graphics.Bitmap;
import android.util.Log;
import ca.ualberta.team10projectw2014.controllersAndViews.MainListViewActivity;
import ca.ualberta.team10projectw2014.controllersAndViews.SubCommentViewActivity;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * This class handles sending CommentModels to the server and executing searches on the
 * server. Most of the code in this class is based on:
 * https://github.com/rayzhangcl/ESDemo and
 * https://github.com/zjullion/PicPosterComplete
 * 
 * @author zjullion. Edited by sgiang92
 * @version      1                (current version number of program)
 */
public class ElasticSearchOperations {

	// public static final String SERVER_URL =
	// "http://cmput301.softwareprocess.es:8080/testing2/team10projectw2014/";
	public static final String SERVER_URL = "http://cmput301.softwareprocess.es:8080/testing/team10projectw2014push/";
	public static final String SERVER_URL_SUBCOMMENTS = "http://cmput301.softwareprocess.es:8080/testing/team10projectw2014sub/";
	public static final String SERVER_URL_LOCATIONS = "http://cmput301.softwareprocess.es:8080/testing/team10projectw2014location/";
	public static final String LOG_TAG = "ElasticSearch";
	private static Gson GSON = null;

	
	/**
	 * Sends a Comment to the server. Does nothing if the request fails.
	 * 
	 * @param model
	 *            a CommentModel
	 */
	public static void pushComment(final CommentModel comment) {
		if (GSON == null)
			constructGson();

		Thread thread = new Thread() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = null;
				if (comment.getParentTitle().isEmpty()) {
				    request = new HttpPost(SERVER_URL);
				} else {
				    request = new HttpPost(SERVER_URL_SUBCOMMENTS);
				}

				try {
					request.setEntity(new StringEntity(GSON.toJson(comment)));
				} catch (UnsupportedEncodingException exception) {
					Log.w(LOG_TAG,
							"Error encoding Comment: " + exception.getMessage());
					return;
				}

				HttpResponse response;
				try {
					response = client.execute(request);
					Log.i(LOG_TAG, "Response: "
							+ response.getStatusLine().toString());
				} catch (IOException exception) {
					Log.w(LOG_TAG,
							"Error sending Comment: " + exception.getMessage());
				}
			}
		};

		thread.start();
	}

	/**
	 * Deletes a comment model in the server
	 * 
	 * @param uniqueID
	 */
	public static void delCommentModel(final CommentModel comment) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				String url = null;
				if(comment.getParentTitle().isEmpty())
    				url = SERVER_URL;
				else
				    url = SERVER_URL_SUBCOMMENTS;
				
				HttpDelete delete = new HttpDelete(url + "_query"
                        + "?q=uniqueID:" + comment.getUniqueID());

				try {
					HttpResponse response = client.execute(delete);
					Log.i(LOG_TAG, "Response: "
							+ response.getStatusLine().toString());
				} catch (IOException exception) {
					Log.w(LOG_TAG, "Error receiving delete query response: "
							+ exception.getMessage());
					return;
				}
			}
		};

		thread.start();
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
	public static void searchForCommentModels(final String searchTerm,
			final ArrayList<CommentModel> model,
			final MainListViewActivity activity) {
		if (GSON == null)
			constructGson();

		Thread thread = new Thread() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(SERVER_URL + "_search");
				// String query =
				// "{\"query\": {\"query_string\": {\"default_field\": \"content\",\"query\": \"*"
				// + searchTerm + "*\"}}}";
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

				Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<CommentModel>>() {
				}.getType();
				final ElasticSearchSearchResponse<CommentModel> returnedData = GSON
						.fromJson(responseJson, elasticSearchSearchResponseType);

				Runnable updateModel = new Runnable() {
					@Override
					public void run() {
						model.clear();
						model.addAll(returnedData.getSources());
						// Log.e(LOG_TAG, model.toString()); // print out the
						// entire contents of the list
						ApplicationStateModel appState = ApplicationStateModel
								.getInstance();
						appState.setCommentList(model);
						appState.saveComments();
						appState.loadComments();
						appState.updateMainAdapter();
						activity.sortMainList();
						// Log.e(LOG_TAG, appState.getCommentList().toString());
						// // print out the entire contents of the list
						// appState.getCommentList().addAll(returnedData.getSources());
						// Log.e("Inside ESO appState CommentList",
						// appState.getCommentList().toString()); // print out
						// the entire contents of the list
						// appState.getMLVAdapter().notifyDataSetChanged();
					}
				};

				activity.runOnUiThread(updateModel);
			}
		};

		thread.start();

		// Log.e(LOG_TAG, model.toString()); // print out the entire contents of
		// the list
	}
	
	
	
	/**
	 * Searches for replies in the server
	 * 
	 * @param activity
	 * @param appState
	 * @param parentID
	 */
	public static void searchForReplies(final SubCommentViewActivity activity,final ApplicationStateModel appState, final String parentID) {
		final ArrayList<CommentModel> replyCommentList = new ArrayList<CommentModel>();
		
		if (GSON == null)
			constructGson();

		Thread thread = new Thread() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(SERVER_URL_SUBCOMMENTS+"_search");
				String query = "{\"query\": {\"match\": {\"parentID\" :\"*" + parentID + "*\"}}}";
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

				Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<CommentModel>>() {
				}.getType();
				final ElasticSearchSearchResponse<CommentModel> returnedData = GSON
						.fromJson(responseJson, elasticSearchSearchResponseType);

						replyCommentList.clear();
						appState.addCommentsToReplyList(returnedData.getSources());
						appState.getSubCommentViewHead().setSubComments(appState.getReplyList());

			}
		};

		thread.start();
		
		try{
			thread.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Constructs a Gson with a custom serializer / desserializer registered for
	 * Bitmaps.
	 */
	private static void constructGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		GSON = builder.create();
	}

}