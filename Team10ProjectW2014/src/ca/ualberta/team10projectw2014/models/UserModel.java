package ca.ualberta.team10projectw2014.models;

import java.util.ArrayList;

import android.content.Context;
import android.provider.Settings.Secure;

/**
 * This class acts as the model for the User.
 * @author  Steven Giang <giang2@ualberta.ca>, David Yee <
 * @version      1                (current version number of program)
 */
public class UserModel{
	private String androidID;
	private ArrayList<CommentModel> favourites;
	private ArrayList<CommentModel> readComments;
	private ArrayList<CommentModel> wantToReadComments;
	private String userName;
	
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

	/**
	 * @return
	 * @uml.property  name="androidID"
	 */
	public String getAndroidID() {
		return androidID;
	}

	/**
	 * @param androidID
	 * @uml.property  name="androidID"
	 */
	public void setAndroidID(String androidID) {
		this.androidID = androidID;
	}

	/**
	 * @return
	 * @uml.property  name="favourites"
	 */
	public ArrayList<CommentModel> getFavourites() {
		return favourites;
	}

	/**
	 * @param favourites
	 * @uml.property  name="favourites"
	 */
	public void setFavourites(ArrayList<CommentModel> favourites) {
		this.favourites = favourites;
	}

	/**
	 * @return
	 * @uml.property  name="readComments"
	 */
	public ArrayList<CommentModel> getReadComments() {
		return readComments;
	}

	/**
	 * @param readComments
	 * @uml.property  name="readComments"
	 */
	public void setReadComments(ArrayList<CommentModel> readComments) {
		this.readComments = readComments;
	}

	/**
	 * @return
	 * @uml.property  name="wantToReadComments"
	 */
	public ArrayList<CommentModel> getWantToReadComments() {
		return wantToReadComments;
	}

	/**
	 * @param wantToReadComments
	 * @uml.property  name="wantToReadComments"
	 */
	public void setWantToReadComments(ArrayList<CommentModel> wantToReadComments) {
		this.wantToReadComments = wantToReadComments;
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String username) {
		userName = username;
	}

	/**
	 * @return
	 * @uml.property  name="sortByPic"
	 */
	public boolean isSortByPic() {
		return sortByPic;
	}
	
	/**
	 * @param sortByPic
	 * @uml.property  name="sortByPic"
	 */
	public void setSortByPic(boolean sortByPic) {
		this.sortByPic = sortByPic;
	}
	
	/**
	 * @return
	 * @uml.property  name="sortByDate"
	 */
	public boolean isSortByDate() {
		return sortByDate;
	}
	
	/**
	 * @param sortByDate
	 * @uml.property  name="sortByDate"
	 */
	public void setSortByDate(boolean sortByDate) {
		this.sortByDate = sortByDate;
		if(this.sortByDate){
			this.sortByLoc = false;
			this.sortByPopularity = false;
		}
	}
	
	/**
	 * @return
	 * @uml.property  name="sortByLoc"
	 */
	public boolean isSortByLoc() {
		return sortByLoc;
	}
	
	/**
	 * @param sortByLoc
	 * @uml.property  name="sortByLoc"
	 */
	public void setSortByLoc(boolean sortByLoc) {
		this.sortByLoc = sortByLoc;
		if(this.sortByLoc){
			this.sortByDate = false;
			this.sortByPopularity = false;
		}
	}
	
	/**
	 * @return
	 * @uml.property  name="sortByPopularity"
	 */
	public boolean isSortByPopularity() {
		return sortByPopularity;
	}
	
	/**
	 * @param sortByPopularity
	 * @uml.property  name="sortByPopularity"
	 */
	public void setSortByPopularity(boolean sortByPopularity) {
		this.sortByPopularity = sortByPopularity;
		if(this.sortByPopularity){
			this.sortByLoc = false;
			this.sortByDate = false;
		}
	}
	
}
