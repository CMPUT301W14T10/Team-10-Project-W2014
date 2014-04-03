package ca.ualberta.team10projectw2014.models;

import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

/**
 * This class acts as the super class model for comments.
 * @author David Yee <dvyee@ualberta.ca>
 * @version     1                (current version number of program)
 */
public class CommentModel{
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
	
	/**
	 * A method that adds the provided SubCommentModel to the
	 * CommentModel's list of SubCommentModels.
	 * @param  subComment - the SubCommentModel to add.
	 * @return      void, no return value.
	 */
	public void addSubComment(SubCommentModel subComment){
		subComments.add(subComment);
	}
	
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
	public boolean compareComments(CommentModel otherComment){
		if((this.getAuthorAndroidID().equals(otherComment.getAuthorAndroidID())) &&
				(this.getTimestamp().equals(otherComment.getTimestamp())))
			return true;
		else
			return false;
	}
	public boolean isInArrayList(ArrayList<CommentModel> commentList){
		for(CommentModel comment : commentList){
			if(this.compareComments(comment))
				return true;
		}
		return false;	
	}

	public void removeFromArrayList(ArrayList<CommentModel> commentList)
	{
		CommentModel comment;
		for(int i = 0; i < commentList.size(); i++){
			comment = commentList.get(i);
			if(this.compareComments(comment))
				//while loop in case comment somehow ended up in the list 
				//numerous times, which should not happen since comments
				//are unique:
				commentList.remove(i);
		}
	}
	

}
