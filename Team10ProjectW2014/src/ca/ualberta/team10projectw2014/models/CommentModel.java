package ca.ualberta.team10projectw2014.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
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
	 * @return string uniqueID
	 */
	public String getUniqueID() {
        return uniqueID;
    }

    /**
	 * @param uniqueID
	 */
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    /**
	 * A method that adds the provided SubCommentModel to the
	 * CommentModel's list of SubCommentModels.
	 * @param  model - the SubCommentModel to add.
	 */
	public void addSubComment(CommentModel model){
		subComments.add(model);
	}
	
	/**
	 * @return Uri imageuri
	 */
	public Uri getImageUri() {
		return imageUri;
	}
	/**
	 * @param imageUri
	 */
	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}
	/**
	 * @return string photopath
	 */
	public String getPhotoPath() {
		return photoPath;
	}
	/**
	 * @param photoPath
	 */
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	/**
	 * @return list of commentmodels
	 */
	public ArrayList<CommentModel> getSubComments() {
		return this.subComments;
	}
	
	/**
	 * @param subComments
	 */
	public void setSubComments(ArrayList<CommentModel> subComments){
		this.subComments = new ArrayList<CommentModel>();
		this.subComments = subComments;
	}
	/**
	 * @return string title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return string content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return string author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return location
	 */
	public LocationModel getLocation() {
		return location;
	}
	/**
	 * @param location
	 */
	public void setLocation(LocationModel location) {
		this.location = location;
	}
	/**
	 * @return bitmap photo
	 */
	public Bitmap getPhoto() {
		return photo;
	}
	/**
	 * @param photo
	 */
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}
	/**
	 * @return calendar timestamp
	 */
	public Calendar getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp
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
	 * @return string timestamp
	 */
	public String getTimestamp_str() {
	    return timestamp_str;
	}
	/**
	 * @return int number of favourites
	 */
	public int getNumFavourites() {
		return numFavourites;
	}
	/**
	 * @param numFavourites
	 */
	public void setNumFavourites(int numFavourites) {
		this.numFavourites = numFavourites;
	}
	/**
	 * @param parentID
	 */
	public void setParentID(String parentID) {
		this.parentID = parentID;
		
	}
	/**
	 * @return string partentID
	 */
	public String getParentID(){
		return this.parentID;
	}
	/**
	 * @return string androidID
	 */
	public String getAuthorAndroidID() {
		return authorAndroidID;
	}
	/**
	 * @param authorAndroidID
	 */
	public void setAuthorAndroidID(String authorAndroidID) {
		this.authorAndroidID = authorAndroidID;
	}
	
	/**
	 * @param parentTitle
	 */
	public void setParentTitle(String parentTitle){
		this.parentTitle = parentTitle;
	}
	/**
	 * @return string parent title
	 */
	public String getParentTitle(){
		return this.parentTitle;
	}
	/**
	 * Compares comments in terms of ID
	 * 
	 * @param otherComment
	 * @return boolean value
	 */
	public boolean compareComments(CommentModel otherComment){
		if((this.getAuthorAndroidID().equals(otherComment.getAuthorAndroidID())) &&
				(this.getTimestamp().equals(otherComment.getTimestamp())))
			return true;
		else
			return false;
	}
	/**
	 * Checks if comment is in array list
	 * 
	 * @param commentList
	 * @return boolean value
	 */
	public boolean isInArrayList(ArrayList<CommentModel> commentList){
		for(CommentModel comment : commentList){
			if(this.compareComments(comment))
				return true;
		}
		return false;	
	}

	/**
	 * Removes comments from comment list
	 * 
	 * @param commentList
	 */
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
	
	/**
	 * Looks for a comment in a comment list
	 * 
	 * @param commentList
	 * @return comment
	 */
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
	
	/**
	 * Gets index in a comment list
	 * 
	 * @param commentList
	 * @return index
	 */
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

	/**
	 * Updates comment in comment list
	 * 
	 * @param commentList
	 */
	public void updateInArrayList(ArrayList<CommentModel> commentList){
		for(int i = 0; i < commentList.size(); i++){
			if(commentList.get(i).compareComments(this)){
				commentList.set(i, this);
			}
		}
	}
	
	/**
	 * Updates comments
	 * 
	 * @param source
	 * @param destination
	 */
	public static void updateComments(ArrayList<CommentModel> source, ArrayList<CommentModel> destination){
		 for(CommentModel sourceComment : source){
			 sourceComment.updateInArrayList(destination);
		 }
	 }
	 
}
