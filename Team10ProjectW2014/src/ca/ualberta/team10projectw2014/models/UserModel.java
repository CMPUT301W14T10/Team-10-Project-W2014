package ca.ualberta.team10projectw2014.models;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings.Secure;

/**
 * This class acts as the model for the User.
 * @author Steven Giang <giang2@ualberta.ca>, David Yee <
 * @version     1                (current version number of program)
 */
public class UserModel{
	private String androidID;
	private ArrayList<CommentModel> favourites;
	private ArrayList<CommentModel> readComments;
	private ArrayList<CommentModel> wantToReadComments;
	private String userName;
	
	// These values are associated with the user's profile
	private String biography;
	private String twitter;
	private String email;
	private String givenName;
	private Bitmap profilePic;
	private String photoPath;
	private Uri imageUri;
	
	//These boolean values dictate which criteria to sort comments by:
	private boolean sortByPic = false;
	private boolean sortByDate = false;
	private boolean sortByLoc = false;
	private boolean sortByPopularity = false;

	// Constructor for new user
	public UserModel(Context context) {
		super();
		
		// get the unique android ID
		// code for getting unique android ID is from:
		// http://blog.vogella.com/2011/04/11/android-unique-identifier/
		this.androidID = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
		this.favourites = new ArrayList<CommentModel>();
		this.readComments = new ArrayList<CommentModel>();
		this.wantToReadComments = new ArrayList<CommentModel>();
		this.userName = " ";
	}


	// Constructor for old user (load from file)
	public UserModel(String androidID, 
			ArrayList<CommentModel> favourites,
			ArrayList<CommentModel> readComments,
			ArrayList<CommentModel> wantToReadComments, 
			String userName) {
		super();
		this.androidID = androidID;
		this.favourites = favourites;
		this.readComments = readComments;
		this.wantToReadComments = wantToReadComments;
		this.userName = userName;
	}

	public String getAndroidID() {
		return androidID;
	}

	public void setAndroidID(String androidID) {
		this.androidID = androidID;
	}

	public ArrayList<CommentModel> getFavourites() {
		return favourites;
	}

	public void setFavourites(ArrayList<CommentModel> favourites) {
		this.favourites = favourites;
	}

	public ArrayList<CommentModel> getReadComments() {
		return readComments;
	}

	public void setReadComments(ArrayList<CommentModel> readComments) {
		this.readComments = readComments;
	}

	public ArrayList<CommentModel> getWantToReadComments() {
		return wantToReadComments;
	}

	public void setWantToReadComments(ArrayList<CommentModel> wantToReadComments) {
		this.wantToReadComments = wantToReadComments;
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String username) {
		userName = username;
	}

	public boolean isSortByPic() {
		return sortByPic;
	}
	
	public void setSortByPic(boolean sortByPic) {
		this.sortByPic = sortByPic;
	}
	
	public boolean isSortByDate() {
		return sortByDate;
	}
	
	public void setSortByDate(boolean sortByDate) {
		this.sortByDate = sortByDate;
		if(this.sortByDate){
			this.sortByLoc = false;
			this.sortByPopularity = false;
		}
	}
	
	public boolean isSortByLoc() {
		return sortByLoc;
	}
	
	public void setSortByLoc(boolean sortByLoc) {
		this.sortByLoc = sortByLoc;
		if(this.sortByLoc){
			this.sortByDate = false;
			this.sortByPopularity = false;
		}
	}
	
	public boolean isSortByPopularity() {
		return sortByPopularity;
	}
	
	public void setSortByPopularity(boolean sortByPopularity) {
		this.sortByPopularity = sortByPopularity;
		if(this.sortByPopularity){
			this.sortByLoc = false;
			this.sortByDate = false;
		}
	}
	
	public String getBiography() {
		return biography;
	}


	public void setBiography(String biography) {
		this.biography = biography;
	}


	public String getTwitter() {
		return twitter;
	}


	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getGivenName() {
		return givenName;
	}


	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}


	public Bitmap getProfilePic() {
		return profilePic;
	}


	public void setProfilePic(Bitmap profilePic) {
		this.profilePic = profilePic;
	}
	
	public String getPhotoPath() {
		return photoPath;
	}


	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}


	public Uri getImageUri() {
		return imageUri;
	}


	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}
	
}
