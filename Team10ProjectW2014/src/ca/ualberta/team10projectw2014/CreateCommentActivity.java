package ca.ualberta.team10projectw2014;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

public class CreateCommentActivity extends Activity{
	
	private String postTitle;
	private String postUsername;
	private String postContents;
	private LocationModel postLocation;
	private Bitmap postPhoto;
	private CommentModel parentModel;

	public void fillContents(String username, CommentModel parentModel){
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
			setTitle(postTitle);
			//TODO Make title field "RE: title"
		}
		else{
			if (this.postTitle == null){
				this.postTitle = null;
			}
			setTitle("Create a Name for Your Post");
			//TODO Make title field blank
		}
	}
	
	private void setUsernameView(String name){
		//TODO Set username field to the given value
	}
	
	private void setTitleView(String title){
		//TODO Set title field to given value
	}
	
	private void chooseLocation(){
		//TODO Carry out what happens when the user presses the "Location" button
	}
	
	private void choosePhoto(){
		//TODO Carry out what happens when the user presses the "Photo" button
	}
	
	private void setLocation(){
		//TODO Set location variable 
	}
	
	private void setPhoto(){
		//TODO Set photo variable
	}
	
	//Called when the user presses "Post" button
	private void attemptCommentCreation(View v){
		CommentModel model;
		
		if(this.postContents == null){
			raiseContentsIncompleteError();
		}
		if(this.postUsername == null){
			raiseUsernameIncompleteError();
		}
		if(this.postTitle == null){
			raiseTitleIncompleteError();
		}
		else{
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
				
				//TODO Add this subcomment to its head's list of subcomments
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
