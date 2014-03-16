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
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class EditCommentActivity extends Activity {

	private String postTitle;
	private String postUsername;
	private String postContents;
	private LocationModel postLocation;
	private Bitmap postPhoto;
	private EditText ueditText; // username edit field
	private EditText teditText; // title edit field
	private EditText ceditText; // content edit field
	private ImageView imageView;
	private double longitude;
	private double latitude;
	private Location bestKnownLoc = null;
	private CommentModel model;
	private LocationListenerController locationListener;
	private LocationManager mLocationManager;
	private Boolean gpsEnabled;
	private Boolean netEnabled;
 	private ApplicationStateModel appState;
 	private String photoPath = null;
 	private Uri imageUri = null;
 	private Location locationGPS;
 	private Location locationNet;
	
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
	
	
	@Override 
	protected void onResume(){
		super.onResume();
		appState.setFileContext(this);

		//Receive information from the intent
		//Bundle bundle = getIntent().getExtras();
		/////String receivedUsername = appState.getUserModel().getUsername();
		//CommentModel receivedComment = (CommentModel) bundle.getSerializable("comment"); //TODO CHANGE FROM SERIALIZABLE TO SIMPLE OBJECT
		CommentModel receivedComment = appState.getCommentToEdit();
		fillContents(receivedComment);
		
		//Set imageView to either "No Image" or the picture returned from camera
		//setPic();
		
		//TODO Check to see if GPS is enabled
        //TODO Start listening for location information
        startListeningLocation();
	}
	
	private void startListeningLocation(){
		Toast.makeText(getBaseContext(), "Starting to listen for location...", Toast.LENGTH_LONG).show();
		this.locationListener = new LocationListenerController(this);
	}
	
	// Retrieved from http://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android on March 6 at 5:00
	
	private void getLastBestLocation() {
		
		bestKnownLoc = locationListener.getLastBestLocation();

	}

	public void fillContents(CommentModel commentToEdit){
		setUsernameView(commentToEdit.getAuthor());
		setTitleView(commentToEdit.getTitle());
		setContentsView(commentToEdit.getContent());
	}
	
	public boolean checkStringIsAllWhiteSpace(String string){
		boolean isWhitespace = string.matches("^\\s*$");
		boolean longerThan0 = (string.trim().length() > 0);
		return isWhitespace && !longerThan0;
	}
	
	private void setContentsView(String contents){
		ceditText.setText(contents, TextView.BufferType.EDITABLE);
	}
	
	private void setUsernameView(String name){
		ueditText.setText(name, TextView.BufferType.EDITABLE);
	}
	
	private void setTitleView(String title){
		teditText.setText(title, TextView.BufferType.EDITABLE);
	}

	public void chooseLocation(View v){
		Toast.makeText(getBaseContext(), "You Want to Choose a Location, Eh?", Toast.LENGTH_LONG).show();
	}
	
	//Starts camera activity
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
	
	//Takes thumbnail from the camera activity and puts it into postPhoto.
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
    
    //Sends the image taken from the camera to file.
    
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

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath(); //"file:" + 

        return image;
    }
    
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
    	
		
	
	//A currently redundant method which creates a location with a title (not used yet)
	private void setLocation(){
		this.postLocation = new LocationModel(String.valueOf("Lat: " + bestKnownLoc.getLatitude()) + " Long: " + String.valueOf(bestKnownLoc.getLongitude()), bestKnownLoc.getLatitude(), bestKnownLoc.getLongitude());
		//TODO Set location variable to...?
		// Location should never be null
	}
	
	//Called when the user presses "Post" button to create and store a new comment
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
			
			appState.getCommentToEdit().setTitle(this.postTitle);
			appState.getCommentToEdit().setAuthor(this.postUsername);
			appState.getCommentToEdit().setContent(this.postContents);
			
			//appState.getCommentToEdit().setLocation(this.postLocation);

			appState.saveComments();
			appState.loadComments();
			//Destroy this activity so that we return to the previous one.
			goBack();
		}
	}
	
	
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
	
	private void raiseContentsIncompleteError(){
		Toast.makeText(getBaseContext(), "Please Add Some Content", Toast.LENGTH_LONG).show();
	}
	
	private void raiseUsernameIncompleteError(){
		//TODO Make it so that the user is prompted to post as anonymous
	    postUsername = "Anonymous";
	}
	
	private void raiseTitleIncompleteError(){
		Toast.makeText(getBaseContext(), "Please Create a Title", Toast.LENGTH_LONG).show();
	}
	
	private void goBack(){
		finish();
	}
	
	
}
