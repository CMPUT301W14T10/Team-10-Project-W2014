package ca.ualberta.team10projectw2014;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CreateCommentActivity extends Activity{
	
	private String postTitle;
	private String postUsername;
	private String postContents;
	private LocationModel postLocation;
	private Bitmap postPhoto;
	private CommentModel parentModel;
	EditText ueditText = (EditText)findViewById(R.id.cc_username);
	EditText teditText = (EditText)findViewById(R.id.cc_title);
	EditText ceditText = (EditText)findViewById(R.id.cc_content);

	public void fillContents(String username, CommentModel parentModel){
		setLocation();
		if(username != null){
			this.postUsername = username;
			setUsernameView(username);
		}
		else{
			if (this.postUsername == null){
				this.postUsername = null;
			}
			setUsernameView("Please set a username");
		}
		if(parentModel != null){
			this.parentModel = parentModel;
			this.postTitle = "RE:" + parentModel.getTitle();
			teditText.setKeyListener(null);
			setTitle(postTitle);
		}
		else{
			if (this.postTitle == null){
				this.postTitle = null;
			}
			setTitle("Create a Name for Your Post");
		}
	}
	
	private void setUsernameView(String name){
		ueditText.setText(name, TextView.BufferType.EDITABLE);
	}
	
	private void setTitleView(String title){
		teditText.setText(title, TextView.BufferType.EDITABLE);
	}
	
	@SuppressWarnings("unused")
	private void chooseLocation(View v){
		//TODO Carry out what happens when the user presses the "Location" button
	}
	
	@SuppressWarnings("unused")
	private void choosePhoto(View v){
		//TODO Carry out what happens when the user presses the "Photo" button
	}
	
	private void setLocation(){
		if (this.postLocation == null){
			this.postLocation = new LocationModel("TITLE", "LAT", "LONG");
		}
		//TODO Set location variable to...?
		// Location should never be null
	}
	
	//Called when the user presses "Post" button
	@SuppressWarnings("unused")
	private void attemptCommentCreation(View v){
		CommentModel model;
		
		this.postContents = ceditText.getText().toString();
		this.postUsername = ueditText.getText().toString();
		this.postTitle = teditText.getText().toString();
		
		if(this.postContents == null){
			raiseContentsIncompleteError();
		}
		if(this.postUsername == null){
			raiseUsernameIncompleteError();
		}
		if(this.postTitle == null){
			raiseTitleIncompleteError();
		}
		if(this.postTitle != null && this.postUsername != null && this.postContents != null){
			
			// This is a regular expression for simplicity
			// Will change for generalizability later
			// Checks to see if the postTitle has "RE:" at the start
			if(this.postTitle.matches("^RE:[.]{*}")){
				// This should be edited so that the model handles all the getting and setting
				model = new SubCommentModel(this.parentModel);
				model.setAuthor(this.postUsername);
				model.setContent(this.postContents);
				model.setLocation(this.postLocation);
				model.setPhoto(this.postPhoto);
				model.setTitle(this.postTitle);
				
				// Sets the current date and time for the comment
				// Referenced http://stackoverflow.com/questions/16686298/string-timestamp-to-calendar-in-java on March 2
				long timestamp = System.currentTimeMillis();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(timestamp);
				model.setTimestamp(calendar);
				model.setNumFavourites(0);
				
				//Adds the newly created model to its referrent's list of subcomments
				this.parentModel.addSubComment(model);
			}
			else{
				// This should be edited so that the model handles all the getting and setting
				model = new HeadModel();
				model.setAuthor(this.postUsername);
				model.setContent(this.postContents);
				model.setLocation(this.postLocation);
				model.setPhoto(this.postPhoto);
				model.setTitle(this.postTitle);
				
				long timestamp = System.currentTimeMillis();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(timestamp);
				model.setTimestamp(calendar);
				model.setNumFavourites(0);
				
				//TODO Add this head comment to the list of head comments on the phone
			
			}
			
			//Destroy this activity so that we return to the previous one.
			finish();
		}
	}
	
	private void raiseContentsIncompleteError(){
		//TODO Send user an error telling them that they need more info
	}
	
	private void raiseUsernameIncompleteError(){
		//TODO Send user an error telling them that they need more info
	}
	
	private void raiseTitleIncompleteError(){
		//TODO Send user an error telling them that they need more info
	}
	
	
}
