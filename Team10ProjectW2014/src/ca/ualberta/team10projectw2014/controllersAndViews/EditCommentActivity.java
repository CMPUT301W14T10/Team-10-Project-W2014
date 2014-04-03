package ca.ualberta.team10projectw2014.controllersAndViews;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationListenerModel;
import ca.ualberta.team10projectw2014.models.LocationModel;

/**
 * @author      Bradley Poulette <bpoulett@ualberta.ca>
 * @version     1                (current version number of program)
 * 
 * This class deals with a comment which has already been created in the system, but
 * the user wants to change. 
 * 
 * As of version 1, this class will not change the location of the comment.
 */
public class EditCommentActivity extends Activity implements CommentContentEditing{

	/**
	 *  These serve as temporary variables for the comment to created.
	 *  They are filled into the comment when the user presses "post".
	 */
	private String postTitle;
	private String postUsername;
	private String postContents;
	@SuppressWarnings("unused")
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
	 *  A custom class used to get the user's location
	 */
	private LocationListenerModel locationListener;
	
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
        setContentView(R.layout.activity_create_comment);
        appState = ApplicationStateModel.getInstance();
		appState.setFileContext(this);

        ueditText = (EditText)findViewById(R.id.cc_username);
		teditText = (EditText)findViewById(R.id.cc_title);
		ceditText = (EditText)findViewById(R.id.cc_content);
		imageView = (ImageView)findViewById(R.id.cc_image_view);
	}
	
	/**
	 * Sets the views using data set in {@link #onCreate()} and tells {@link #locationListener to start listening for location}
	 * Location is not supported as of version 1.
	 */
	@Override 
	protected void onResume(){
		super.onResume();
		appState.setFileContext(this);

		
		CommentModel receivedComment = appState.getCommentToEdit();
		fillContents(receivedComment);
		
		//Set imageView to either "No Image" or the picture returned from camera
		setPic();
		

        // Start listening for location information
        //startListeningLocation();
	}
	
	/**
	 * Creates a LocationListenerController to start keeping track of the user's location
	 *
	 */
	@SuppressWarnings("unused")
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
	 * @param commentToEdit comment with data to set in the view
	 */
	public void fillContents(CommentModel commentToEdit){
		setUsernameView(commentToEdit.getAuthor());
		setTitleView(commentToEdit.getTitle());
		setContentsView(commentToEdit.getContent());
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
	
	/**
	 * Sets the view which represents comment contents
	 *
	 * @param contents   a string to put in the contents field
	 */
	private void setContentsView(String contents){
		ceditText.setText(contents, TextView.BufferType.EDITABLE);
	}
	
	/**
	 * Sets the view which represents comment username
	 *
	 * @param name   a string to put in the username field
	 */
	private void setUsernameView(String name){
		ueditText.setText(name, TextView.BufferType.EDITABLE);
	}
	
	/**
	 * Sets the view which represents comment title
	 *
	 * @param title   a string to put in the title field
	 */
	private void setTitleView(String title){
		teditText.setText(title, TextView.BufferType.EDITABLE);
	}
	
	/**
	 * Gives the user a menu from which they can select a new location
	 * as the default.
	 * 
	 * Not implemented as of version 1.
	 *
	 * @param v   the view which calls this method (in this case, "Location")
	 */
	public void chooseLocation(View v){
		Toast.makeText(getBaseContext(), "Sorry, this feature is not supported yet", Toast.LENGTH_LONG).show();
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

        // Save the path of the image created
        photoPath = image.getAbsolutePath();

        return image;
    }
    
    /**
	 * Sets the view which shows the photo as a thumbnail in this activity
	 */
    private void setPic() {
            try {
            	if (photoPath != null){
            		appState.getCommentToEdit().setPhotoPath(this.photoPath);
            	}
            	
                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize = 8;
     
                final Bitmap bitmap = BitmapFactory.decodeFile(appState.getCommentToEdit().getPhotoPath(),
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
	@SuppressWarnings("unused")
	private void setLocation(){
		this.postLocation = new LocationModel(String.valueOf("Lat: " + bestKnownLoc.getLatitude()) + " Long: " + String.valueOf(bestKnownLoc.getLongitude()), bestKnownLoc.getLatitude(), bestKnownLoc.getLongitude());
	}
	
	/**
	 * Updates the current comment and saves it to file if all the views are filled in appropriately.
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
		else if(checkStringIsAllWhiteSpace(this.postUsername)){
			raiseUsernameIncompleteError();
		}
		
		else{
			
			appState.getCommentToEdit().setTitle(this.postTitle);
			appState.getCommentToEdit().setAuthor(this.postUsername);
			appState.getCommentToEdit().setContent(this.postContents);
			appState.getCommentToEdit().setPhoto(this.postPhoto);
			
			//appState.getCommentToEdit().setLocation(this.postLocation);

			appState.saveComments();
			appState.loadComments();
			
			//Destroy this activity so that we return to the previous one.
			goBack();
		}
	}
	
	/**
	 * Tells the locationListener to stop listening for the user's location
	 * 
	 * Not used as of version 1
	 */
	@SuppressWarnings("unused")
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
