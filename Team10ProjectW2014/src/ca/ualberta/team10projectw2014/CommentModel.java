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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * This class acts as the super class model for comments.
 * @author David Yee
 */
public class CommentModel{
	/**
	 * 
	 */
	private String title;
	private String content;
	private String author;
	private LocationModel location;
	private Bitmap photo;
	private Calendar timestamp;
	private int numFavourites;
	private String authorAndroidID;
	private ArrayList<SubCommentModel> subComments = new ArrayList<SubCommentModel>();
	private String photoPath;
	private Uri imageUri;
	
	public Uri getImageUri() {
		return imageUri;
	}

	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public void addSubComment(SubCommentModel subComment){
		subComments.add(subComment);
	}
	
	public ArrayList<SubCommentModel> getSubComments() {
		return subComments;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public LocationModel getLocation() {
		return location;
	}
	public void setLocation(LocationModel location) {
		this.location = location;
	}
	public Bitmap getPhoto() {
		return photo;
	}
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}
	public Calendar getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}
	public int getNumFavourites() {
		return numFavourites;
	}
	public void setNumFavourites(int numFavourites) {
		this.numFavourites = numFavourites;
	}
	public String getAuthorAndroidID() {
		return authorAndroidID;
	}
	public void setAuthorAndroidID(String authorAndroidID) {
		this.authorAndroidID = authorAndroidID;
	}
	
}
