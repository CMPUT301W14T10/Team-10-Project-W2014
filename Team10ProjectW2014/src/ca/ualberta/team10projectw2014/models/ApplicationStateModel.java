package ca.ualberta.team10projectw2014.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import android.content.Context;
import android.location.Location;

import ca.ualberta.team10projectw2014.controllersAndViews.MainListViewAdapter;
import ca.ualberta.team10projectw2014.controllersAndViews.SubCommentViewActivityAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
* This class is used as a singleton to contain data
* available to all activities.
* The code for setting up a singleton structure was
* borrowed from http://www.javaworld.com/article/2073352/core-java/simply-singleton.html
* @author Cole Fudge
* @version     1                (current version number of program)
*/
public class ApplicationStateModel {

	//to set up this class as a singleton:
	private static ApplicationStateModel instance = null;

	protected ApplicationStateModel() {
		// Exists only to defeat instantiation.		
	}
	public static ApplicationStateModel getInstance() {
		if(instance == null) {
			instance = new ApplicationStateModel();
		}

		return instance;
	}

	/**
	*The required context for opening files
	*(for saving/loading comments)
	*/
	private static Context COMMENT_fileContext;
	
	/**
	*The required filename for opening files
	*(for saving/loading comments)
	*/
	private String COMMENT_FILE_NAME = "comments.sav";
	
	
	/**
	*The required context for opening files
	*(for saving/loading user)
	*/
	private Context USER_fileContext;
	
	/**
	*The required filename for opening user file
	*(for saving/loading user preferences)
	*/
	private String USER_FILE_NAME = "user.sav";


	/**
	*Adapter for displaying the list of comments
	*in the MainListViewActivity.
	*/
	private MainListViewAdapter MLVAdapter;
	
	/**
	*Adapter for displaying the list of comments
	*in the SubCommentViewActivity.
	*/
	private SubCommentViewActivityAdapter SCVAdapter;
	

	/**
	*The list of all head comments that in turn
	*contain all subcomments, i.e. the following
	*list contains all comments and subcomments(recursively)
	*and is used in saving/loading.
	*/
	private ArrayList<CommentModel> commentList;
	
	/**
	*Holds all of the user's preferences and cached
	*comments. Can be accessed from any activity.
	*/
	private UserModel userModel;
	
	/**
	*The head comment that is to be displayed by
	*the SubCommentViewActivity. The activity
	*will show this head comment in full, followed
	*by its subcomments in list form.
	*/
	private CommentModel subCommentViewHead;
	
	/**
	*The comment that CreateComment is to
	*create a subcomment for. This will
	*be null if CreateComment is to create
	*a new head comment.
	*/
	private CommentModel createCommentParent;
	
	/**
	*The comment that EditComment is to
	*change.
	*/
	private CommentModel commentToEdit;

	/**
	*A comparator used in sorting comments by location.
	*/
	public static Comparator<CommentModel> locCompare = new Comparator<CommentModel>(){
		
		public int compare(CommentModel comment1, CommentModel comment2){
			
			//Get the user's current location:
			final LocationListenerModel locationListener = new LocationListenerModel(COMMENT_fileContext);
			Location userLocation = locationListener.getLastBestLocation();
			
			//create a location with the latitude and longitude each of the comments under consideration:
			Location loc1 = new Location("provider");
			loc1.setLatitude(comment1.getLocation().getLatitude());
			loc1.setLongitude(comment1.getLocation().getLongitude());

			Location loc2 = new Location("provider");
			loc2.setLatitude(comment2.getLocation().getLatitude());
			loc2.setLongitude(comment2.getLocation().getLongitude());
			
			//get the difference in their distance from the user:
			double difference = (loc2.distanceTo(userLocation) - loc1.distanceTo(userLocation));
			
			//The difference in distances is a double and the precision is lost if it is cast
			//to an integer, which is what the comparator needs to return. Since the difference 
			//might be between -1 and 1, I call floor or ceiling respectively in order to maintain 
			//their ordering(e.g. the t is still negative if the difference is -0.11, it will be
			//floored to -1.0 so that it remains negative when cast to an int).
			if(difference < 0)
					difference = Math.floor(difference);
			else if(difference > 0)
					difference = Math.ceil(difference);
			return (int) difference;
		}
	};

	/**
	*A comparator used in sorting comments by date.
	*/
	public static Comparator<CommentModel> dateCompare = new Comparator<CommentModel>(){
		public int compare(CommentModel comment1, CommentModel comment2){
			Calendar time1 = comment1.getTimestamp();
			Calendar time2 = comment2.getTimestamp();
			return time1.compareTo(time2);
		}
	};

	/**
	*A comparator used in sorting comments by number of likes.
	*/
	public static Comparator<CommentModel> popularityCompare = new Comparator<CommentModel>(){
		public int compare(CommentModel comment1, CommentModel comment2){
			int favs1 = comment1.getNumFavourites();
			int favs2 = comment2.getNumFavourites();
			return (favs1 - favs2);
		}
	};

	//Getters and Setters:
	public CommentModel getCommentToEdit()
	{
	
		return this.commentToEdit;
	}
	
	public void setCommentToEdit(CommentModel commentToEdit)
	{
	
		this.commentToEdit = commentToEdit;
	}
	
	public MainListViewAdapter getMLVAdapter() {
		return this.MLVAdapter;
	}

	public void setMLVAdapter(MainListViewAdapter mLVAdapter) {
		this.MLVAdapter = mLVAdapter;
	}

	public SubCommentViewActivityAdapter getSCVAdapter() {
		return this.SCVAdapter;
	}

	public void setSCVAdapter(SubCommentViewActivityAdapter sCVAdapter) {
		this.SCVAdapter = sCVAdapter;
	}

	public void setFileContext(Context fileContext) {
		this.USER_fileContext = fileContext;
		ApplicationStateModel.COMMENT_fileContext = fileContext;
	}

	public CommentModel getCreateCommentParent() {
		return this.createCommentParent;
	}

	public void setCreateCommentParent(CommentModel createCommentParent) {
		this.createCommentParent = createCommentParent;
	}

	public CommentModel getSubCommentViewHead() {
		return this.subCommentViewHead;
	}

	public void setSubCommentViewHead(CommentModel subCommentViewHead) {
		this.subCommentViewHead = subCommentViewHead;
	}

	public ArrayList<CommentModel> getCommentList() {
		return this.commentList;
	}

	public void setCommentList(ArrayList<CommentModel> commentList) {
		this.commentList = commentList;
	}

	public UserModel getUserModel() {
		return this.userModel;
	}

	/**
	 * A method for updating the MainListViewAdapter from outside of the
	 * singleton.
	 * @param  void no arguments
	 * @return      void, no return value.
	 */
	public void updateMainAdapter(){
		this.MLVAdapter.notifyDataSetChanged();
	}

	/**
	 * A method for updating the SubCommentViewActivtyAdapter
	 * from outside of the singleton.
	 * @param  void, no arguments
	 * @return      void, no return value.
	 */
	public void updateSubAdapter()
	{
		this.SCVAdapter.notifyDataSetChanged();
	}

	/**
	 * A method for saving the list of head
	 * comments(i.e. commentList) to file
	 * @param  void, no arguments
	 * @return      void, no return value.
	 * @see #commentList
	 */
	public void saveComments(){
		try {
			//open the file for writing:
			FileOutputStream fos = ApplicationStateModel.COMMENT_fileContext.openFileOutput(COMMENT_FILE_NAME,
					Context.MODE_PRIVATE); // TODO REMEMBER TO SET THE CONTEXT BEFORE YOU USE THIS
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			
			Gson gson = new Gson();
			
			//A type token to pass GSON, indicating that we want to load
			//a list of CommentModels:
			Type fooType = new TypeToken<ArrayList<CommentModel>>() {}.getType();
			
			//Conver the list to a JSON string, saving it to file:
			gson.toJson(commentList, fooType, osw); 
			
			//close the file:
			osw.close();
			fos.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * A method for loading the list of head
	 * comments(i.e. commentList) from file.
	 * The commentList is changed in place,
	 * i.e. the reference is not changed, in
	 * order to avoid conflicting references.
	 * @param  void, no arguments
	 * @return      void, no return value.
	 * @see #commentList
	 */
	public void loadComments(){
		FileInputStream fis;
		//clear the comment list so that their
		//are no duplicates and we have only 
		//comments from file:
		this.commentList.clear();
		try
		{
			//Get the file for reading:
			fis = ApplicationStateModel.COMMENT_fileContext.openFileInput(COMMENT_FILE_NAME);
			InputStreamReader isr = new InputStreamReader(fis);
			
			Gson gson = new Gson();

			//create a type token to tell GSON what type of object it is saving:
			Type fooType = new TypeToken<ArrayList<CommentModel>>() {}.getType();
			
			//use GSON object, type token and file stream to get list of
			//head comments from file:
			ArrayList<CommentModel> list_temp = gson.fromJson(isr, fooType);
			
			//if the comments in the file were null, we can't call list_temp.size()
			//or list_temp.get(i)
			if(list_temp != null){
				//iterate through the list to add each element, as opposed to
				//changing the commentList's reference by assignment:
				for(int i = 0; i < list_temp.size(); i++)
					this.commentList.add(list_temp.get(i));
			}
			
			//Close the files:
			isr.close();
			fis.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * A method for saving the user's
	 * preferences to file.
	 * @param  void, no arguments
	 * @return      void, no return value.
	 * @see #userModel
	 */
	public void saveUser(){
		try {
			//Obtain the file to write, creating it if it does not exist:
			FileOutputStream fos = USER_fileContext.openFileOutput(USER_FILE_NAME,
					Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			
			//Create a GSON object and type token:
			Gson gson = new Gson();
			Type fooType = new TypeToken<UserModel>() {}.getType();
			
			//Obtain the JSON string for the object/type provided
			//and save it to file:
			gson.toJson(userModel, fooType, osw);
			
			//Close the files:
			osw.close();
			fos.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * A method for loading the user's
	 * preferences from file.
	 * @param  void, no arguments
	 * @return      void, no return value.
	 * @see #userModel
	 */
	public void loadUser(){
		FileInputStream fis;
		userModel = new UserModel(USER_fileContext);
		try
		{
			//Obtain the file to read:
			fis = USER_fileContext.openFileInput(USER_FILE_NAME);
			InputStreamReader isr = new InputStreamReader(fis);
			
			//Create the GSON object and type token:
			Gson gson = new Gson();
			Type fooType = new TypeToken<UserModel>() {}.getType();
			
			//Get the UserModel from the File using the GSON object
			UserModel loadedUser = gson.fromJson(isr, fooType);
			
			//if the file was empty, keep an empty list, not null.
			//Otherwise, set the user to whatever was in the file:
			if(loadedUser != null)
				this.userModel = loadedUser;
			
			//close the file:
			isr.close();
			fis.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * Selection sort algorithm to sort an array of comments by a given comparator
	 * 
	 * @param list - array of CommentModels to sort
	 * @param cmp - comparator to compare CommentModels when sorting
	 * @return the sorted array of head comments
	 */
	 public ArrayList<CommentModel> sort(ArrayList<CommentModel> list, Comparator<CommentModel> cmp) {
		 for (int i=0; i < list.size()-1; i++) {
			 // Sets current comment as the one that should appear first
			 CommentModel maxComment = list.get(i);
			 int maxIndex = i;
			 // Iterates through remaining comments in the list
			 for (int j=i+1; j < list.size(); j++) {
				 // If i compared to j = -1, j should be max value
				 if (cmp.compare(list.get(i), list.get(j)) < 0) {
					 maxComment = list.get(j);
					 maxIndex = j;
				 }
			 }
			 // Swap current comment with the maxComment
			 CommentModel tempComment;
			 tempComment = list.get(i);
			 list.set(i, maxComment);
			 list.set(maxIndex, tempComment);
		 }
		 return list;
	 }

	 /**
	  * Separates given array into two arrays with one containing comments with
	  * pictures, and the other without. Sorts each array by given comparator,
	  * then combines them.
	  * 
	  * @param list - the array of CommentModels to sort
	  * @param cmp - the comparator to compare CommentModels when sorting
	  * @return the sorted array of head comments
	  */
	 public ArrayList<CommentModel> pictureSort(ArrayList<CommentModel> list, Comparator<CommentModel> cmp) {
		 ArrayList<CommentModel> noPicArray = new ArrayList<CommentModel>();
		 for (int i=0; i < list.size(); i++) {
			 // If comment does not have a photo
			 if (list.get(i).getPhotoPath() == null) {
				 // Add it to the array containing comments without pictures
				 noPicArray.add(list.get(i));
				 // Remove it from the array containing comments with pictures
				 list.remove(i);
				 i--;
			 }
		 }
		 // Sort each array
		 list = sort(list, cmp);
		 noPicArray = sort(noPicArray, cmp);
		 // Combine both arrays
		 list.addAll(noPicArray);
		 return list;
	 }
	 
	 
	 /**
	  * Separates given array into two arrays with one containing comments with
	  * pictures, and the other without. Sorts each array by given comparator,
	  * then combines them.
	  * 
	  * @param list - the array of SubCommentModels to sort.
	  * @param cmp - the comparator to compare SubCommentModels when sorting.
	  * @return the sorted array of SubCommentModels.
	  */
	 public ArrayList<SubCommentModel> sort2(ArrayList<SubCommentModel> list, Comparator<CommentModel> cmp) {
		 for (int i=0; i < list.size()-1; i++) {
			 // Sets current comment as the one that should appear first
			 SubCommentModel maxComment = list.get(i);
			 int maxIndex = i;
			 // Iterates through remaining comments in the list
			 for (int j=i+1; j < list.size(); j++) {
				 // If i compared to j = -1, j should be max value
				 if (cmp.compare(list.get(i), list.get(j)) < 0) {
					 maxComment = list.get(j);
					 maxIndex = j;
				 }
			 }
			 // Swap current comment with the maxComment
			 SubCommentModel tempComment;
			 tempComment = list.get(i);
			 list.set(i, maxComment);
			 list.set(maxIndex, tempComment);
		 }
		 return list;
	 }
	 


	 

}	


