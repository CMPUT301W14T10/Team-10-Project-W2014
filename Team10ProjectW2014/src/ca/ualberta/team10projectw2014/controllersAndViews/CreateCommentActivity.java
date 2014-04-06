package ca.ualberta.team10projectw2014.controllersAndViews;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationListenerModel;
import ca.ualberta.team10projectw2014.models.LocationModel;
import ca.ualberta.team10projectw2014.network.ElasticSearchLocationOperations;
import ca.ualberta.team10projectw2014.network.ElasticSearchOperations;

/**
 * @author  Bradley Poulette <bpoulett@ualberta.ca>
 * @version  1 (current version number of program)  <p>  This class deals with creating comments by talking with the app's  singleton.  This is where the location of a comment, its picture, and any  textual contents will be set.
 */
public class CreateCommentActivity extends Activity implements
		CommentContentEditing {

	/**
	 * These serve as temporary variables for the comment to created. They are
	 * filled into the comment when the user presses "post".
	 */
	private String postTitle;
	private String postUsername;
	private String postContents;
	/**
	 * @uml.property  name="postLocation"
	 * @uml.associationEnd  
	 */
	private LocationModel postLocation;
	private Bitmap postPhoto;

	/**
	 * These are the layout's fields
	 */
	private EditText ueditText; // username edit field
	private EditText teditText; // title edit field
	private EditText ceditText; // content edit field
	private ImageView imageView;
	private String photoPath = null;
	private Uri imageUri = null;
	private File photoFile;

	/**
	 * This is changed multiple times in order to store the most accurate
	 * position of the user during comment creation.
	 */
	private Location bestKnownLoc = null;

	/**
	 * A temporary comment model to be stored when created
	 * @uml.property  name="model"
	 * @uml.associationEnd  
	 */
	private CommentModel model;

	/**
	 * A custom class used to get the user's location
	 * @uml.property  name="locationListener"
	 * @uml.associationEnd  
	 */
	private LocationListenerModel locationListener;

	/**
	 * Our singleton, which allows us to pass application state between activities
	 * @uml.property  name="appState"
	 * @uml.associationEnd  
	 */
	private ApplicationStateModel appState;

	/**
	 * List of the location models
	 */
	private ArrayList<LocationModel> locationList;
	
	/**
	 * Temporary list of the location models used for spinner sorting
	 */
	private ArrayList<LocationModel> tempLocationList;

	/**
	 * Flag that is set if the user selects a location from the location dialog
	 * spinner
	 */
	private int spinnerFlag;

	/**
	 * Initiates application state singleton then sets class variables to
	 * comment values returned in the application state singleton.
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_comment);
		appState = ApplicationStateModel.getInstance();
		appState.setFileContext(this);

		ueditText = (EditText) findViewById(R.id.cc_username);
		teditText = (EditText) findViewById(R.id.cc_title);
		ceditText = (EditText) findViewById(R.id.cc_content);
		imageView = (ImageView) findViewById(R.id.cc_image_view);
		
		postUsername = appState.getUserModel().getUsername();
		
		// Load location list from elastic search into appstate
		appState.setLocationList(new ArrayList<LocationModel>());
		ElasticSearchLocationOperations.getLocationList(this);
		appState.loadLocations();
		
		// Retrieve location list from appstate
		locationList = new ArrayList<LocationModel>();
		CreateCommentActivity.this.locationList = appState.getLocationList();
		
		spinnerFlag = 0;
	}
	
	@Override
	protected void onStop() {
		super.onPause();
		this.postUsername = ueditText.getText().toString();
		this.postTitle = teditText.getText().toString();
		this.postContents = ceditText.getText().toString();
	}

	/**
	 * Sets the views using data set in {@link #onCreate()} and tells
	 * {@link #locationListener to start listening for location}
	 */
	@Override
	protected void onResume() {
		super.onResume();
		appState.setFileContext(this);
	
		

		fillContents();

		setPic();

		startListeningLocation();
	}

	/**
	 * Creates a LocationListenerController to start keeping track of the user's
	 * location
	 * 
	 */
	private void startListeningLocation() {
		Toast.makeText(getBaseContext(), "Starting to listen for location...",
				Toast.LENGTH_LONG).show();
		this.locationListener = new LocationListenerModel(this);
	}

	/**
	 * Sets {@link #bestKnownLoc} to the most accurate location in
	 * {@link #locationListener}
	 * 
	 * Implementation retrieved from
	 * http://stackoverflow.com/questions/1513485/how
	 * -do-i-get-the-current-gps-location-programmatically-in-android on March 6
	 * at 5:00
	 */
	private void getLastBestLocation() {

		bestKnownLoc = locationListener.getLastBestLocation();

	}

	/**
	 * Sets the view to show the data provided by application state in
	 * {@link #onCreate(Bundle)}
	 * 
	 * @param username
	 *            username taken from UserModel in singleton
	 */
	public void fillContents() {
		if (!checkStringIsAllWhiteSpace(this.postUsername)) {
			setUsernameView(this.postUsername);
		} else {
			if (checkStringIsAllWhiteSpace(this.postUsername)) {
				this.postUsername = "Anonymous";
				setUsernameView("");
			}
		}
		if (this.postTitle == null) {
			this.postTitle = null;
		}
		if (this.postContents != null){
			//Set post contents view to postContents
			ceditText.setText(postContents, TextView.BufferType.EDITABLE);
		}
		if (this.postTitle != null){
			//Set post title view to postTitle
			teditText.setText(postTitle, TextView.BufferType.EDITABLE);
		}
	}

	/**
	 * Checks if a string contains all whitespace using REGEX
	 * 
	 * @param string
	 *            string to be checked
	 * @return true if the string is all whitespace or empty, otherwise false
	 */
	public boolean checkStringIsAllWhiteSpace(String string) {
		boolean isWhitespace = string.matches("^\\s*$");
		boolean longerThan0 = (string.trim().length() > 0);
		return isWhitespace && !longerThan0;
	}

	private void setUsernameView(String name) {
		ueditText.setText(name, TextView.BufferType.EDITABLE);
	}

	/**
	 * Allows the user to choose a location by creating the dialogs for the 
	 * location button where the user to set an existing location or create 
	 * a new one.
	 * 
	 * Custom dialog implementation retrieved from http://www.mkyong.com/
	 * android/android-custom-dialog-example/ on April 1, 2014
	 * 
	 * @param v view from which the method is called
	 */
	public void chooseLocation(View v) {
		int i = 0;

		// Gets the xml custom dialog layout
		LayoutInflater li = LayoutInflater.from(this);
		View locationDialogView = li.inflate(R.layout.dialog_location, null);

		// Builds alert dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(locationDialogView);
		
		//get location list from app state fixes spinner lag
		CreateCommentActivity.this.locationList = appState.getLocationList();
		
        // Gets best known location
        stopListeningLocation();
        
        // Create tempt list to sort
		CreateCommentActivity.this.tempLocationList = CreateCommentActivity.
				this.locationList;
		// Sort list by proximity
		Collections.sort(CreateCommentActivity.this.tempLocationList, ApplicationStateModel.locationModelCompare);
		// Loads up spinner with location names
		final Spinner spinner = (Spinner) locationDialogView
				.findViewById(R.id.location_dialog_spinner);
		// Creates and populates a list of the location names for displaying
		// in the spinner
		ArrayList<String> locationNameList = new ArrayList<String>();
		if (CreateCommentActivity.this.tempLocationList.size() != 0) {
			for (i = 0; i < CreateCommentActivity.this.tempLocationList.size(); i++)
				locationNameList.add(CreateCommentActivity.this.
						tempLocationList.get(i).getName());
		} else
			locationNameList.add("No Locations");
		
		// If a location was set, finds its position in the spinner
		if (CreateCommentActivity.this.postLocation != null) {
			for (i = 0; i < CreateCommentActivity.this.tempLocationList.size(); i++)
				if (CreateCommentActivity.this.tempLocationList.get(i).getName().matches(CreateCommentActivity.this.postLocation.getName())) {
					break;
				}
		}

		// Shows spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, locationNameList);
		spinner.setAdapter(adapter);
		// If a position was already set, makes that position the active item in the spinner
		if (CreateCommentActivity.this.postLocation != null)
			spinner.setSelection(i);


		// Location dialog title
		alertDialogBuilder.setTitle("Set Location");

		// Location dialog set button functionality
		alertDialogBuilder.setPositiveButton("Set",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Checks if no locations have been created and the user
						// is trying to set a location
						if (spinner.getSelectedItem().toString()
								.matches("No Locations"))
							Toast.makeText(getBaseContext(),
									"Please create a new location",
									Toast.LENGTH_LONG).show();
						else {
							CreateCommentActivity.this.postLocation = CreateCommentActivity.
									this.tempLocationList.get(spinner.getSelectedItemPosition());
							CreateCommentActivity.this.spinnerFlag = 1;
						}
					}
				});

		// Location dialog cancel button functionality
		alertDialogBuilder.setNegativeButton("Cancel", null);

		// Location dialog create location button functionality
		alertDialogBuilder.setNeutralButton("Create Location",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Gets the xml custom dialog layout
						LayoutInflater li2 = LayoutInflater
								.from(CreateCommentActivity.this);
						final View locationNameDialogView = li2.inflate(
								R.layout.dialog_location_name, null);

						// Build alert dialog
						AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
								CreateCommentActivity.this);
						alertDialogBuilder2.setView(locationNameDialogView);

						// Location name dialog title
						alertDialogBuilder2.setTitle("Set Location Name");

						// Location name set button functionality
						alertDialogBuilder2.setPositiveButton("Set",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										final EditText editText = (EditText) locationNameDialogView
												.findViewById(R.id.enter_location_name);
										String locationNameString = editText
												.getText().toString();

										// Check if can detect user's current
										// location
										if (CreateCommentActivity.this.bestKnownLoc == null)
											Toast.makeText(
													getBaseContext(),
													"No current location detected - can't create location",
													Toast.LENGTH_LONG).show();
										// Check if no location name has been
										// entered
										else if (locationNameString.matches(""))
											Toast.makeText(
													getBaseContext(),
													"You must enter a location name",
													Toast.LENGTH_LONG).show();
										else {
											int i;
											double distance;
											int closestLocationIndex = -1;
											int nameMatchFlag = 0;

											// Determines if there is a location
											// within 50m from location list or
											// if location name has already been
											// taken
											for (i = 0; i < CreateCommentActivity.this.locationList
													.size(); i++) {
												distance = distFrom(
														bestKnownLoc
																.getLatitude(),
														bestKnownLoc
																.getLongitude(),
														CreateCommentActivity.this.locationList
																.get(i)
																.getLatitude(),
														CreateCommentActivity.this.locationList
																.get(i)
																.getLongitude());
												if (distance < 50) {
													closestLocationIndex = i;
													break;
												}
												if (CreateCommentActivity.this.locationList
														.get(i)
														.getName()
														.matches(
																locationNameString)) {
													nameMatchFlag = 1;
													break;
												}
											}

											// If location name entered already
											// exists
											if (nameMatchFlag == 1)
												Toast.makeText(
														getBaseContext(),
														"Location name "
																+ locationNameString
																+ " already taken",
														Toast.LENGTH_LONG)
														.show();
											// If there is a nearby location, do
											// not allow user to create location
											else if (closestLocationIndex != -1) {
												locationNameString = CreateCommentActivity.this.locationList
														.get(closestLocationIndex)
														.getName();
												Toast.makeText(
														getBaseContext(),
														"Cannot create new location near "
																+ locationNameString,
														Toast.LENGTH_LONG)
														.show();
											}
											// Otherwise create the location and
											// set it as the comments location
											else {
												CreateCommentActivity.this.postLocation = new LocationModel(
														locationNameString,
														CreateCommentActivity.this.bestKnownLoc
																.getLatitude(),
														CreateCommentActivity.this.bestKnownLoc
																.getLongitude());
												// Adds new location to the local location list
												CreateCommentActivity.this.locationList
														.add(CreateCommentActivity.this.postLocation);
												// Adds the new location to the appstate location list
												appState.setLocationList(CreateCommentActivity.this.locationList);
												// Sets appstate location list to the newly updated one
												CreateCommentActivity.this.appState
														.saveLocations();
												// Saves location list to elastic search
												ElasticSearchLocationOperations.pushLocationList(CreateCommentActivity.this.postLocation);
												// Sets/resets spinner set flag
												CreateCommentActivity.this.spinnerFlag = 0;
											}
										}
									}
								});
						
						// Location name dialog cancel button functionality
						alertDialogBuilder2.setNegativeButton("Cancel", null);

						// Creates alert dialog
						AlertDialog alertDialog2 = alertDialogBuilder2.create();
						alertDialog2.show();
					}
				});

		// Creates alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	/**
	 * Starts camera activity when the "Photo" button is pressed in the view
	 * 
	 * Calls {@link #createImageFile()} to create a file to which the photo will
	 * be stored
	 * 
	 * @param v
	 *            the view from which the method is called
	 */
	public void choosePhoto(View v) {
		PackageManager packageManager = this.getPackageManager();
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// Ensure that there's a camera activity to handle the intent
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				// Create the File where the photo should go
				if (photoFile == null){
					try {
						photoFile = createImageFile();
					} catch (IOException ex) {
						Toast.makeText(getBaseContext(), "Could not write to file",
								Toast.LENGTH_LONG).show();
					}
					// Continue only if the File was successfully created
					if (photoFile != null) {
						imageUri = Uri.fromFile(photoFile);
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								imageUri);
						startActivityForResult(takePictureIntent, 1);
					}
				}
				else if (photoFile != null){
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							imageUri);
					startActivityForResult(takePictureIntent, 1);
				}
			}

		} else {
			Toast.makeText(getBaseContext(), "Sorry, you don't have a camera!",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Handles the output of the camera activity upon return
	 * 
	 * Idea taken from
	 * http://www.androidhive.info/2013/09/android-working-with-camera-api/
	 * 
	 * @param requestCode
	 *            the type of request made to the camera (should always be 1)
	 * @param resultCode
	 *            informs the method whether the camera activity was successful
	 *            or not
	 * @param data
	 *            an intent which stores the thumbnail of the image from camera
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// display it in image view
				setPic();
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
					.show();
				setPic();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * Creates the file to which the camera will send the full image
	 * 
	 * Idea taken from
	 * http://www.androidhive.info/2013/09/android-working-with-camera-api/
	 * 
	 * @return the file for saving
	 * @throws any
	 *             exception if the file cannot be created
	 */
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {

		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		if (!storageDir.exists())
			storageDir.mkdirs();
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);
		photoPath = image.getAbsolutePath();

		return image;
	}

	/**
	 * Sets the view which shows the photo as a thumbnail in this activity
	 */
	private void setPic() {
		try {

			BitmapFactory.Options options = new BitmapFactory.Options();

			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(),
					options);
			this.postPhoto = bitmap;
			imageView.setImageBitmap(this.postPhoto);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the temporary comment location to the location of the user from
	 * {@link #locationListener}
	 * 
	 * Not used as of version 1.
	 */
	private void setLocation() {
		int i;
		int closestLocationIndex = -1;
		double distance = 1000; // Set to 1 km
		double distFrom;

		// If current location is known and location list is not empty, look for
		// closest location
		if ((bestKnownLoc != null) && (this.locationList != null)) {
			if (this.spinnerFlag == 0) {
				for (i = 0; i < this.locationList.size(); i++) {
					// Determines if there is a nearby location from location
					// list
					distFrom = distFrom(bestKnownLoc.getLatitude(),
							bestKnownLoc.getLongitude(),
							this.locationList.get(i).getLatitude(),
							this.locationList.get(i).getLongitude());
					if (distFrom < distance) {
						distance = distFrom;
						closestLocationIndex = i;
					}
				}
				// No nearby locations found
				if (closestLocationIndex == -1)
					Toast.makeText(
							getBaseContext(),
							"No nearby locations found. Please select or create a location.",
							Toast.LENGTH_LONG).show();
				// Nearby location found in location list
				else {
					this.postLocation = this.locationList
							.get(closestLocationIndex);
				}
			}
		}
		// Current location is known and location list is empty
		else if ((bestKnownLoc != null) && (this.locationList == null))
			Toast.makeText(
					getBaseContext(),
					"No nearby locations found. Please select or create a location.",
					Toast.LENGTH_LONG).show();
		// Current location is not known and location list is not empty and spinner wasn't set
		else if ((bestKnownLoc == null) && (this.locationList != null) && (CreateCommentActivity.this.spinnerFlag != 1))
			Toast.makeText(getBaseContext(),
					"Current location is unknown. Please select a location.",
					Toast.LENGTH_LONG).show();
		// Current location is not known and location list is not empty and spinner was set
		else if ((bestKnownLoc == null) && (this.locationList != null) && (CreateCommentActivity.this.spinnerFlag == 1))
			; // Doesn't change anything, allows attemtCommentCreation to post the comment set in the spinner
		// Current location is not known and location list is empty
		else {
			this.postLocation = new LocationModel("Unknown Location", 0, 0);
		}
	}

	/**
	 * Creates a new subComment or headComment based on the information passed.
	 */
	public void attemptCommentCreation(View v) {
		this.postContents = ceditText.getText().toString();
		this.postUsername = ueditText.getText().toString();
		this.postTitle = teditText.getText().toString();

		stopListeningLocation();
		setLocation();

		if (checkStringIsAllWhiteSpace(this.postContents)) {
			raiseContentsIncompleteError();
		} else if (checkStringIsAllWhiteSpace(this.postTitle)) {
			raiseTitleIncompleteError();
		}
		// Does nothing if no location has been set - error message spawned in
		// setLocation()
		else if (this.postLocation == null)
			;
		else {

			if (appState.getCreateCommentParent() != null) {

				if (checkStringIsAllWhiteSpace(this.postUsername)) {
					raiseUsernameIncompleteError();
				}
				// This should be edited so that the model handles all the
				// getting and setting
				model = new CommentModel();
				model.setAuthor(this.postUsername);
				model.setContent(this.postContents);
				model.setLocation(this.postLocation);
				model.setPhoto(this.postPhoto);
				model.setTitle(this.postTitle);
				model.setPhotoPath(photoPath);
				model.setParentID(appState.getCreateCommentParent().getUniqueID());
				model.setAuthorAndroidID(appState.getUserModel().getAndroidID());
				model.setParentTitle("RE: "
						+ appState.getCreateCommentParent().getTitle());

				// Sets the current date and time for the comment
				// Referenced
				// http://stackoverflow.com/questions/16686298/string-timestamp-to-calendar-in-java
				// on March 2
				long timestamp = System.currentTimeMillis();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(timestamp);
				model.setTimestamp(calendar);
				model.setNumFavourites(0);
				
				String uniqueID = new String(model.getAuthorAndroidID());
                uniqueID = uniqueID.concat(model.getTimestamp_str());
                uniqueID = uniqueID.replaceAll(" ", "");
                model.setUniqueID(uniqueID);

				// Adds the newly created model to its referrent's list of
				// subcomments
				appState.getCreateCommentParent().addSubComment(model);
				ElasticSearchOperations.pushComment(model,"SubComment");
				appState.updateSubAdapter();
			} else {
				if (checkStringIsAllWhiteSpace(this.postUsername)) {
					raiseUsernameIncompleteError();
				}

				// This should be edited so that the model handles all the
				// getting and setting
				model = new CommentModel();
				model.setAuthor(this.postUsername);
				model.setContent(this.postContents);
				model.setLocation(this.postLocation);
				model.setPhoto(this.postPhoto);
				model.setTitle(this.postTitle);
				model.setPhotoPath(photoPath);

				long timestamp = System.currentTimeMillis();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(timestamp);
				model.setTimestamp(calendar);
				model.setNumFavourites(0);
				model.setAuthorAndroidID(appState.getUserModel().getAndroidID());
				
				String uniqueID = new String(model.getAuthorAndroidID());
				uniqueID = uniqueID.concat(model.getTimestamp_str());
				uniqueID = uniqueID.replaceAll(" ", "");
				model.setUniqueID(uniqueID);

				appState.getCommentList().add(model);
				ElasticSearchOperations.pushComment(model, "HeadComment");
			}

			model.setLocation(this.postLocation);

			appState.saveComments();
			appState.loadComments();
			appState.updateMainAdapter();
			// Destroy this activity so that we return to the previous one.
			goBack();
		}
	}

	/**
	 * Tells the locationListener to stop listening for the user's location
	 * 
	 * Not used as of version 1
	 */
	private void stopListeningLocation() {
		getLastBestLocation();
		if (bestKnownLoc != null) {
			Location location = locationListener.getCurrentBestLocation();
			bestKnownLoc.setLatitude(location.getLatitude());
			bestKnownLoc.setLongitude(location.getLongitude());
			locationListener.removeUpdates();
		}
	}

	/**
	 * Tells the user to add content if {@link #attemptCommentCreation(View)}
	 * finds the content view is empty
	 */
	private void raiseContentsIncompleteError() {
		Toast.makeText(getBaseContext(), "Please Add Some Content",
				Toast.LENGTH_LONG).show();
	}

	/**
	 * If not set in {@link #attemptCommentCreation(View)}, sets username to
	 * "Anonymous"
	 */
	private void raiseUsernameIncompleteError() {
		// TODO Make it so that the user is prompted to post as anonymous
		postUsername = "Anonymous";
	}

	/**
	 * Tells the user to add a title if {@link #attemptCommentCreation(View)}
	 * finds the title view is empty
	 */
	private void raiseTitleIncompleteError() {
		Toast.makeText(getBaseContext(), "Please Create a Title",
				Toast.LENGTH_LONG).show();
	}

	/**
	 * Terminates the application
	 * 
	 * This is in a separate method so that it may be called by a button, should
	 * we decide to add one in the future.
	 */
	private void goBack() {
		finish();
	}

	/**
	 * Calculates the distance between two coordinates in meters.
	 * 
	 * Implementation retrieved from http://stackoverflow.com/questions/837872/
	 * calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java
	 * on April 1, 2014
	 * 
	 * @param lat1 latitude coordinate of location 1
	 * @param lng1 longitude coordinate of location 1
	 * @param lat2 latitude coordinate of location 2
	 * @param lng2 longitude coordinate of location 2
	 * @return distance between two coordinates in meters
	 */
	public static double distFrom(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (dist * meterConversion);
	}
}