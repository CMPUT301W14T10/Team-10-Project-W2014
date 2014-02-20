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

package ca.ualberta.team10projectw2014;

import java.util.ArrayList;

public class UserModel {
	private int androidID;
	private ArrayList<CommentModel> favourites;
	private ArrayList<CommentModel> readComments;
	private ArrayList<CommentModel> wantToReadComments;
	private String Username;

	// Constructor for new user
	public UserModel(int androidID) {
		super();
		this.androidID = androidID;
		this.favourites = new ArrayList<CommentModel>();
		this.readComments = new ArrayList<CommentModel>();
		this.wantToReadComments = new ArrayList<CommentModel>();
		String Username = "null";
	}

	// Constructor for old user (load from file)
	public UserModel(int androidID, ArrayList<CommentModel> favourites,
			ArrayList<CommentModel> readComments,
			ArrayList<CommentModel> wantToReadComments, String username) {
		super();
		this.androidID = androidID;
		this.favourites = favourites;
		this.readComments = readComments;
		this.wantToReadComments = wantToReadComments;
		Username = username;
	}

	public int getAndroidID() {
		return androidID;
	}

	public void setAndroidID(int androidID) {
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
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

}
