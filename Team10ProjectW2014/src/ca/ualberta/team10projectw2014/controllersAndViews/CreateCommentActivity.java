package ca.ualberta.team10projectw2014.controllersAndViews;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationListenerModel;
import ca.ualberta.team10projectw2014.models.LocationModel;
import ca.ualberta.team10projectw2014.models.SubCommentModel;
import ca.ualberta.team10projectw2014.network.ElasticSearchOperations;

import android.R.anim;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author      Bradley Poulette <bpoulett@ualberta.ca>
 * @version     1                (current version number of program)
 * 
 * <p>
 * This class deals with creating comments by talking with the app's singleton.
 *  
 * This is where the location of a comment, its picture, and any textual contents will be set.
*/
public class CreateCommentActivity extends Activity implements CommentContentEditing{
	
	/**
	 *  These serve as temporary variables for the comment to created.
	 *  They are filled into the comment when the user presses "post".
	 */
	private String postTitle;
	private String postUsername;
	private String postContents;
	private LocationModel postLocation;
	private Bitmap postPhoto;
	
	/**
	 *  These are the layout's fields
	 */
	private EditText ueditText; // username edit field
	private EditText teditText; // title edit field
	private EditText ceditText; // content edit field
	private ImageView imageView;
	private String photoPath = null;
 	private Uri imageUri = null;
 	
	/**
	 *  This is changed multiple times in order to store the most accurate
	 *  position of the user during comment creation.
	 */
	private Location bestKnownLoc = null;
	
	/**
	 * A temporary comment model to be stored when created
	 */
	private CommentModel model;
	
	/**
	 *  A custom class used to get the user's location
	 */
	private LocationListenerModel locationListener;
 	
	/**
	 *  Our singleton, which allows us to pass application state
	 *  between activities
	 */
	private ApplicationStateModel appState;
	
	/**
	 * Temporary list of the location models
	 */
	private ArrayList<LocationModel> locationList;
	
	/**
	 * Flag that is set if the user selects a location from the location dialog spinner
	 */
	private int spinnerFlag;
	
	/**
 	 * Initiates application state singleton then sets class variables to comment values
 	 * returned in the application state singleton.
 	 */
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        appState = ApplicationStateModel.getInstance();
		appState.setFileContext(this);

        ueditText = (EditText)findViewById(R.id.cc_username);
		teditText = (EditText)findViewById(R.id.cc_title);
		ceditText = (EditText)findViewById(R.id.cc_content);
		imageView = (ImageView)findViewById(R.id.cc_image_view);
		
		appState.setLocationList(new ArrayList<LocationModel>());
		appState.loadLocations();
		locationList = appState.getLocationList();
		if (locationList == null)
			locationList = new ArrayList<LocationModel>();
		// TODO crashes when creating a new location and this is uncommented and appState's location list is empty
		//locationList = appState.getLocationList();
	}
	
	/**
	 * Sets the views using data set in {@link #onCreate()} and tells {@link #locationListener to start listening for location}
	 */
	@Override 
	protected void onResume(){
		super.onResume();
		appState.setFileContext(this);

		String receivedUsername = appState.getUserModel().getUsername();
		fillContents(receivedUsername);
		
		setPic();
		
        startListeningLocation();
	}
	
	/**
	 * Creates a LocationListenerController to start keeping track of the user's location
	 *
	 */
	private void startListeningLocation(){
		Toast.makeText(getBaseContext(), "Starting to listen for location...", Toast.LENGTH_LONG).show();
		this.locationListener = new LocationListenerModel(this);
	}
	
	/**
	 * Sets {@link #bestKnownLoc} to the most accurate location in {@link #locationListener}
	 *
	 * Implementation retrieved from http://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android on March 6 at 5:00
	 */
	private void getLastBestLocation() {
		
		bestKnownLoc = locationListener.getLastBestLocation();

	}

	/**
	 * Sets the view to show the data provided by application state in {@link #onCreate(Bundle)}
	 *
	 * @param username username taken from UserModel in singleton
	 */
	public void fillContents(String username){
		if(!checkStringIsAllWhiteSpace(username)){
			this.postUsername = username;
			setUsernameView(username);
		}
		else{
			if(checkStringIsAllWhiteSpace(username)){
				this.postUsername = "Anonymous";
				setUsernameView(this.postUsername);
			}
		}
		if (this.postTitle == null){
			this.postTitle = null;
		}
	}
	
	/**
	 * Checks if a string contains all whitespace using REGEX
	 * 
	 * @param string string to be checked 
	 * @return            true if the string is all whitespace or empty, otherwise false
	 */
	public boolean checkStringIsAllWhiteSpace(String string){
		boolean isWhitespace = string.matches("^\\s*$");
		boolean longerThan0 = (string.trim().length() > 0);
		return isWhitespace && !longerThan0;
	}
	
	private void setUsernameView(String name){
		ueditText.setText(name, TextView.BufferType.EDITABLE);
	}

	// TODO javadoc this function; custom dialog code modified from http://www.mkyong.com/android/android-custom-dialog-example/ April 1, 2014
	public void chooseLocation(View v){
		// TODO remove toast
		//Toast.makeText(getBaseContext(), "Sorry, this feature is not supported yet.", Toast.LENGTH_LONG).show();
		int i;
		//ArrayList<LocationModel> locationList = new ArrayList<LocationModel>();
		//locationList = appState.getLocationList();
		
		// Sets/resets spinner set flag
		CreateCommentActivity.this.spinnerFlag = 0;
		
		// Gets the xml custom dialog layout
		LayoutInflater li = LayoutInflater.from(this);
		View locationDialogView = li.inflate(R.layout.dialog_location, null);
		
		// Builds alert dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(locationDialogView);
		
		// Loads up spinner with location names
		final Spinner spinner = (Spinner) locationDialogView.findViewById(R.id.location_dialog_spinner);
		ArrayList<String> locationNameList = new ArrayList<String>();
		if (this.locationList.size() != 0) {
			for (i=0; i < this.locationList.size(); i++)
				locationNameList.add(this.locationList.get(i).getName());
		}
		else
			locationNameList.add("No Locations");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locationNameList);
		spinner.setAdapter(adapter);
		
		// Location dialog title
		alertDialogBuilder.setTitle("Set Location");
		
		// Location dialog set button functionality
		alertDialogBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO don't let them push set when NO LOCATION is active
				Log.e("SPINNER INDEX", CreateCommentActivity.this.locationList.get(spinner.getSelectedItemPosition()).getName());
				CreateCommentActivity.this.postLocation = CreateCommentActivity.this.locationList.get(spinner.getSelectedItemPosition());
				CreateCommentActivity.this.spinnerFlag = 1;
			}
		});
		
		// Location dialog cancel button functionality 
		alertDialogBuilder.setNegativeButton("Cancel", null);
		
		// Location dialog create location button functionality
		alertDialogBuilder.setNeutralButton("Create Location", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Gets the xml custom dialog layout
				LayoutInflater li2 = LayoutInflater.from(CreateCommentActivity.this);
				final View locationNameDialogView = li2.inflate(R.layout.dialog_location_name, null);
				
				// Build alert dialog
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(CreateCommentActivity.this);
				alertDialogBuilder2.setView(locationNameDialogView);
				
				// Location name dialog title
				alertDialogBuilder2.setTitle("Set Location Name");
				
				// Location name set button functionality
				alertDialogBuilder2.setPositiveButton("Set", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//final EditText editText = (EditText) locationDialogView.findViewById(R.id.enter_location_name);
						final EditText editText = (EditText) locationNameDialogView.findViewById(R.id.enter_location_name);
						// TODO check if no name entered
						// TODO check if name is already in location list
						stopListeningLocation();
						String locationNameString = editText.getText().toString();
						
						if (CreateCommentActivity.this.bestKnownLoc == null)
							Toast.makeText(getBaseContext(), "No current location detected - can't set location", Toast.LENGTH_LONG).show();
						else if (CreateCommentActivity.this.bestKnownLoc != null) {
							int i;
							double distance;
							int closestLocationIndex = -1;
							
							for (i=0; i < CreateCommentActivity.this.locationList.size(); i++) {
								// Determines if there is a location within 50m from location list
								distance = distFrom(bestKnownLoc.getLatitude(), bestKnownLoc.getLongitude(), CreateCommentActivity.this.locationList.get(i).getLatitude(), CreateCommentActivity.this.locationList.get(i).getLongitude());
								if (distance < 50) {
									closestLocationIndex = i;
									break;
								}
							}
							
							// If there is a nearby location, do not allow user to create location
							if (closestLocationIndex != -1) {
								locationNameString = CreateCommentActivity.this.locationList.get(closestLocationIndex).getName();
								Toast.makeText(getBaseContext(), "Cannot create new location near " + locationNameString, Toast.LENGTH_LONG).show();
							}
							// Otherwise create the location and set it as the comments location
							else {	
								CreateCommentActivity.this.postLocation = new LocationModel(locationNameString, CreateCommentActivity.this.bestKnownLoc.getLatitude(), CreateCommentActivity.this.bestKnownLoc.getLongitude());
								CreateCommentActivity.this.locationList.add(CreateCommentActivity.this.postLocation);
								CreateCommentActivity.this.appState.setLocationList(CreateCommentActivity.this.locationList);
								CreateCommentActivity.this.appState.saveLocations();
							}
						}
					}
				});
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
	 * Calls {@link #createImageFile()} to create a file to which the photo will be stored
	 *
	 * @param v   the view from which the method is called
	 */
	public void choosePhoto(View v){
		PackageManager packageManager = this.getPackageManager();
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    // Ensure that there's a camera activity to handle the intent
		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
		        // Create the File where the photo should go
		    	File photoFile = null;
		        try {
		            photoFile = createImageFile();
		        } catch (IOException ex) {
		        	Toast.makeText(getBaseContext(), "Could not write to file", Toast.LENGTH_LONG).show();

		        }
		        // Continue only if the File was successfully created
		        if (photoFile != null) {
		        	imageUri = Uri.fromFile(photoFile);
		            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
		                    imageUri);
		            startActivityForResult(takePictureIntent, 1);
		        }
		    }

	    }
		else{
			Toast.makeText(getBaseContext(), "Sorry, you don't have a camera!", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Handles the output of the camera activity upon return
	 * 
	 * Idea taken from http://www.androidhive.info/2013/09/android-working-with-camera-api/
	 *
	 * @param requestCode the type of request made to the camera (should always be 1)
	 * @param resultCode informs the method whether the camera activity was successful or not
	 * @param data an intent which stores the thumbnail of the image from camera
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
	 * Idea taken from http://www.androidhive.info/2013/09/android-working-with-camera-api/
	 *
	 * @return  the file for saving
	 * @throws any exception if the file cannot be created
	 */
    @SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {
 
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if(!storageDir.exists())
            storageDir.mkdirs();
        File image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
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
                imageView.setImageBitmap(bitmap);
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
    private void setLocation(){
    	// Gets the list of already created locations
		//ArrayList<LocationModel> locationList = new ArrayList<LocationModel>();
		int i; 
		int closestLocationIndex = -1;
		double distance = 1000; // Min distance set to 1km TODO ask group what they want this set to
		//locationList = appState.getLocationList();
		
		// If current location is known and location list is not empty, look for closest location
		if ((bestKnownLoc != null) && (this.locationList != null) ){
			if (this.spinnerFlag == 0){
				for (i=0; i < this.locationList.size(); i++) {
					// Determines if there is a nearby location from location list
					if (distFrom(bestKnownLoc.getLatitude(), bestKnownLoc.getLongitude(), this.locationList.get(i).getLatitude(), this.locationList.get(i).getLongitude()) < distance)
						closestLocationIndex = i;
				}
				// No nearby locations found
				if (closestLocationIndex == -1)
					Toast.makeText(getBaseContext(), "No nearby locations found. Please select or create a location.", Toast.LENGTH_LONG).show();
				// Nearby location found in location list
				else {
					this.postLocation = this.locationList.get(closestLocationIndex);
				}
			}
			// TODO remove old code
			//this.postLocation = new LocationModel(String.valueOf("Lat: " + bestKnownLoc.getLatitude()) + " Long: " + String.valueOf(bestKnownLoc.getLongitude()), bestKnownLoc.getLatitude(), bestKnownLoc.getLongitude());
		}
		// Current location is known and location list is empty
		else if ((bestKnownLoc != null) && (this.locationList == null))
			Toast.makeText(getBaseContext(), "No nearby locations found. Please select or create a location.", Toast.LENGTH_LONG).show();
		// Current location is not known and location list is not empty
		else if ((bestKnownLoc == null) && (this.locationList != null))
			Toast.makeText(getBaseContext(), "Current location is unknown. Please select a location.", Toast.LENGTH_LONG).show();
		// Current location is not known and location list is empty
		else{
			this.postLocation = new LocationModel("Unknown Location", 1, 2);
		}
	}
    
    /**
     * Creates a new subComment or headComment based on the information passed.
	 */
    public void attemptCommentCreation(View v){
		this.postContents = ceditText.getText().toString();
		this.postUsername = ueditText.getText().toString();
		this.postTitle = teditText.getText().toString();
		
		stopListeningLocation();
		setLocation();
		
		if(checkStringIsAllWhiteSpace(this.postContents)){
			raiseContentsIncompleteError();
		}
		else if(checkStringIsAllWhiteSpace(this.postTitle)){
			raiseTitleIncompleteError();
		}
		// Does nothing if no location has been set - error message spawned in setLocation()
		else if (this.postLocation == null)
			;
		else{
			
			if(appState.getCreateCommentParent() != null){
				
				if(checkStringIsAllWhiteSpace(this.postUsername)){
					raiseUsernameIncompleteError();
				}
				// This should be edited so that the model handles all the getting and setting
				model = new SubCommentModel(appState.getCreateCommentParent());
				model.setAuthor(this.postUsername);
				model.setContent(this.postContents);
				model.setLocation(this.postLocation);
				model.setPhoto(this.postPhoto);
				model.setTitle(this.postTitle);
				model.setPhotoPath(photoPath);
				model.setAuthorAndroidID(appState.getUserModel().getAndroidID());
				((SubCommentModel) model).setParentTitle("RE: " + appState.getCreateCommentParent().getTitle());
				
				// Sets the current date and time for the comment
				// Referenced http://stackoverflow.com/questions/16686298/string-timestamp-to-calendar-in-java on March 2
				long timestamp = System.currentTimeMillis();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(timestamp);
				model.setTimestamp(calendar);
				model.setNumFavourites(0);
				
				//Adds the newly created model to its referrent's list of subcomments
				appState.getCreateCommentParent().addSubComment((SubCommentModel) model);
				appState.updateSubAdapter();
			}
			else{
				if(checkStringIsAllWhiteSpace(this.postUsername)){
					raiseUsernameIncompleteError();
				}
				
				// This should be edited so that the model handles all the getting and setting
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
				
				appState.getCommentList().add(model);
	            ElasticSearchOperations.pushHeadComment(model);
			}

			
			model.setLocation(this.postLocation);

			
			appState.saveComments();
			appState.loadComments();
			appState.updateMainAdapter();
			//Destroy this activity so that we return to the previous one.
			goBack();
		}
	}
	
    /**
	 * Tells the locationListener to stop listening for the user's location
	 * 
	 * Not used as of version 1
	 */
	private void stopListeningLocation(){
		getLastBestLocation();
		if (bestKnownLoc != null){
			Location location = locationListener.getCurrentBestLocation();
			bestKnownLoc.setLatitude(location.getLatitude());
			bestKnownLoc.setLongitude(location.getLongitude());
			locationListener.removeUpdates();
		}
	}
	
	/**
	 * Tells the user to add content if {@link #attemptCommentCreation(View)} finds the content view is empty
	 */
	private void raiseContentsIncompleteError(){
		Toast.makeText(getBaseContext(), "Please Add Some Content", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * If not set in {@link #attemptCommentCreation(View)}, sets username to "Anonymous"
	 */
	private void raiseUsernameIncompleteError(){
		//TODO Make it so that the user is prompted to post as anonymous
	    postUsername = "Anonymous";
	}
	
	/**
	 * Tells the user to add a title if {@link #attemptCommentCreation(View)} finds the title view is empty
	 */
	private void raiseTitleIncompleteError(){
		Toast.makeText(getBaseContext(), "Please Create a Title", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Terminates the application
	 * 
	 * This is in a separate method so that it may be called by a button, should we decide
	 * to add one in the future.
	 */
	private void goBack(){
		finish();
	}
	
	// TODO java doc this method: code modified from http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java April 1, 2014
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    int meterConversion = 1609;

	    return (dist * meterConversion);
	    }
}
