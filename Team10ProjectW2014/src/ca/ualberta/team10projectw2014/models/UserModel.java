/**
 * Copyright 2014 Cole Fudge, Steven Giang, Bradley Poullet, David Yee, and Costa Zervos

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package ca.ualberta.team10projectw2014.models;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.provider.Settings.Secure;

/**
 * This class acts as the model for the User.
 * @author Steven Giang, David Yee
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
		if(sortByDate){
			this.sortByLoc = false;
			this.sortByPopularity = false;
		}
	}
	
	public boolean isSortByLoc() {
		return sortByLoc;
	}
	
	public void setSortByLoc(boolean sortByLoc) {
		this.sortByLoc = sortByLoc;
		if(sortByDate){
			this.sortByDate = false;
			this.sortByPopularity = false;
		}
	}
	
	public boolean isSortByPopularity() {
		return sortByPopularity;
	}
	
	public void setSortByPopularity(boolean sortByPopularity) {
		this.sortByPopularity = sortByPopularity;
		if(sortByDate){
			this.sortByLoc = false;
			this.sortByDate = false;
		}
	}
	
}
