package ca.ualberta.team10projectw2014;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author      Bradley Poulette <bpoulett@ualberta.ca>
 * @version     1                (current version number of program)
*/
public class CreateCommentActivity extends Activity{
	
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
	private LocationListenerController locationListener;
 	
	/**
	 *  Our singleton, which allows us to pass application state
	 *  between activities
	 */
	private ApplicationStateModel appState;
	
	/**
 	 * Initiates application state singleton then sets class variables to comment values
 	 * returned in the application state singleton.
 	 */
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_comment_activity);
        appState = ApplicationStateModel.getInstance();
		appState.setFileContext(this);

        ueditText = (EditText)findViewById(R.id.cc_username);
		teditText = (EditText)findViewById(R.id.cc_title);
		ceditText = (EditText)findViewById(R.id.cc_content);
		imageView = (ImageView)findViewById(R.id.cc_image_view);
	}
	
	/**
	 * Sets the views using data set in {@link #onCreate()} and tells {@link #locationListener to start listening for location}
	 */
	@Override 
	protected void onResume(){
		super.onResume();
		appState.setFileContext(this);

		String receivedUsername = appState.getUserModel().getUsername();fillContents(receivedUsername);
		
		//setPic();
		
		//TODO Check to see if GPS is enabled
        //TODO Start listening for location information
        startListeningLocation();
	}
	
	/**
	 * Creates a LocationListenerController to start keeping track of the user's location
	 *
	 */
	private void startListeningLocation(){
		Toast.makeText(getBaseContext(), "Starting to listen for location...", Toast.LENGTH_LONG).show();
		this.locationListener = new LocationListenerController(this);
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

	public void chooseLocation(View v){
		Toast.makeText(getBaseContext(), "You Want to Choose a Location, Eh?", Toast.LENGTH_LONG).show();
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


         // Taken from http://www.androidhive.info/2013/09/android-working-with-camera-api/
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
		if (bestKnownLoc != null){
			this.postLocation = new LocationModel(String.valueOf("Lat: " + bestKnownLoc.getLatitude()) + " Long: " + String.valueOf(bestKnownLoc.getLongitude()), bestKnownLoc.getLatitude(), bestKnownLoc.getLongitude());
		}
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
		
		if(checkStringIsAllWhiteSpace(this.postContents)){
			raiseContentsIncompleteError();
		}
		else if(checkStringIsAllWhiteSpace(this.postTitle)){
			raiseTitleIncompleteError();
		}
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
				//TODO Add this head comment to the list of head comments on the phone
			}
			
			//TODO Stop listening for location information

			stopListeningLocation();
			setLocation();
			
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
		if (bestKnownLoc == null){
			Toast.makeText(getBaseContext(), "Ain't no location here", Toast.LENGTH_LONG).show();
		}
		else{
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
	
	
}
