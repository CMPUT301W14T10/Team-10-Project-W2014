package ca.ualberta.team10projectw2014.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;

/**
 * This class acts as the super class model for comments.
 * @author  David Yee <dvyee@ualberta.ca>
 * @version      1                (current version number of program)
 */
public class CommentModel{
	private String title;
	private String content;
	private String author;
	/**
	 * @uml.property  name="location"
	 * @uml.associationEnd  
	 */
	private LocationModel location;
	private Bitmap photo;
	private Calendar timestamp;
	private String timestamp_str;
	private int numFavourites;
	private String authorAndroidID;
	private ArrayList<CommentModel> subComments = new ArrayList<CommentModel>();
	private String photoPath;
	private Uri imageUri;
	private String parentID;
	private String uniqueID;
	private String parentTitle = "";
	
	/**
	 * @return
	 * @uml.property  name="uniqueID"
	 */
	public String getUniqueID() {
        return uniqueID;
    }

    /**
	 * @param uniqueID
	 * @uml.property  name="uniqueID"
	 */
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    /**
	 * A method that adds the provided SubCommentModel to the
	 * CommentModel's list of SubCommentModels.
	 * @param  model - the SubCommentModel to add.
	 * @return      void, no return value.
	 */
	public void addSubComment(CommentModel model){
		subComments.add(model);
	}
	
	/**
	 * @return
	 * @uml.property  name="imageUri"
	 */
	public Uri getImageUri() {
		return imageUri;
	}
	/**
	 * @param imageUri
	 * @uml.property  name="imageUri"
	 */
	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}
	/**
	 * @return
	 * @uml.property  name="photoPath"
	 */
	public String getPhotoPath() {
		return photoPath;
	}
	/**
	 * @param photoPath
	 * @uml.property  name="photoPath"
	 */
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	/**
	 * @return
	 * @uml.property  name="subComments"
	 */
	public ArrayList<CommentModel> getSubComments() {
		return this.subComments;
	}
	
	public void setSubComments(ArrayList<CommentModel> subComments){
		this.subComments = new ArrayList<CommentModel>();
		this.subComments = subComments;
	}
	/**
	 * @return
	 * @uml.property  name="title"
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title
	 * @uml.property  name="title"
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return
	 * @uml.property  name="content"
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content
	 * @uml.property  name="content"
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return
	 * @uml.property  name="author"
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author
	 * @uml.property  name="author"
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return
	 * @uml.property  name="location"
	 */
	public LocationModel getLocation() {
		return location;
	}
	/**
	 * @param location
	 * @uml.property  name="location"
	 */
	public void setLocation(LocationModel location) {
		this.location = location;
	}
	/**
	 * @return
	 * @uml.property  name="photo"
	 */
	public Bitmap getPhoto() {
		return photo;
	}
	/**
	 * @param photo
	 * @uml.property  name="photo"
	 */
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}
	/**
	 * @return
	 * @uml.property  name="timestamp"
	 */
	public Calendar getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp
	 * @uml.property  name="timestamp"
	 */
	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
		this.timestamp_str = timeToInt(timestamp);
	}
	
	 /**
     * Takes in the timestamp as a Calendar object and converts it to a string
     * that can be used in a textView.
     * @param calendar object to retrieve string from
     * @return string of the formatted date of the timestamp
     */
    @SuppressLint("SimpleDateFormat")
    private String timeToInt (Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyHHmmss");
        String timeString = sdf.format(calendar.getTime());
        return timeString;
    }
	
	/**
	 * @return
	 * @uml.property  name="timestamp_str"
	 */
	public String getTimestamp_str() {
	    return timestamp_str;
	}
	/**
	 * @return
	 * @uml.property  name="numFavourites"
	 */
	public int getNumFavourites() {
		return numFavourites;
	}
	/**
	 * @param numFavourites
	 * @uml.property  name="numFavourites"
	 */
	public void setNumFavourites(int numFavourites) {
		this.numFavourites = numFavourites;
	}
	/**
	 * @param parentID
	 * @uml.property  name="parentID"
	 */
	public void setParentID(String parentID) {
		this.parentID = parentID;
		
	}
	public String getParentID(){
		return this.parentID;
	}
	/**
	 * @return
	 * @uml.property  name="authorAndroidID"
	 */
	public String getAuthorAndroidID() {
		return authorAndroidID;
	}
	/**
	 * @param authorAndroidID
	 * @uml.property  name="authorAndroidID"
	 */
	public void setAuthorAndroidID(String authorAndroidID) {
		this.authorAndroidID = authorAndroidID;
	}
	
	public void setParentTitle(String parentTitle){
		this.parentTitle = parentTitle;
	}
	public String getParentTitle(){
		return this.parentTitle;
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
	
	public CommentModel findInArrayList(ArrayList<? extends CommentModel> commentList){
		CommentModel subCommentMatch;
		for (CommentModel arrayListComment : commentList){
			if((subCommentMatch = this.findInArrayList(arrayListComment.getSubComments()))
						!= null){
				return subCommentMatch;
			}
			if(this.compareComments(arrayListComment)){
				return arrayListComment;
			}
		}
		return null;
	}
	
	public int getIndex(ArrayList<? extends CommentModel> commentList){
		CommentModel listComment;
		for(int i = 0; i < commentList.size(); i++){
			listComment = commentList.get(i);
			if(this.compareComments(listComment)){
				return i;
			}
		}
		return -1;
	}

	public void updateInArrayList(ArrayList<CommentModel> commentList){
		for(int i = 0; i < commentList.size(); i++){
			if(commentList.get(i).compareComments(this)){
				commentList.set(i, this);
			}
		}
	}
	
	public static void updateComments(ArrayList<CommentModel> source, ArrayList<CommentModel> destination){
		 for(CommentModel sourceComment : source){
			 sourceComment.updateInArrayList(destination);
		 }
	 }
	 
}
