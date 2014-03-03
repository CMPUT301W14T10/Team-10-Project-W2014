package ca.ualberta.team10projectw2014;

import android.app.Activity;
import android.graphics.Bitmap;

public class CreateCommentActivity extends Activity{
	
	private String postTitle;
	private String postUsername;
	private String postContents;
	private LocationModel postLocation;
	private Bitmap postPhoto;

	public void fillContents(String username, String title){
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
		if(title != null){
			this.postTitle = title;
			setTitle(title);
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
	private void attemptCommentCreation(){
		if(this.postContents == null){
			raiseContentsIncompleteError();
		}
		if(this.postUsername == null){
			raiseUsernameIncompleteError();
		}
		if(this.postTitle == null){
			raiseUsernameIncompleteError();
		}
		else{
			//Create a new head/sub comment model based on if there is a reference to a head
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
