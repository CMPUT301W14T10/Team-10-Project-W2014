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
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import ca.ualberta.team10projectw2014.network.BitmapJsonConverter;
import ca.ualberta.team10projectw2014.controllersAndViews.MainListViewAdapter;
import ca.ualberta.team10projectw2014.controllersAndViews.SubCommentViewActivityAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * This class is used as a singleton to contain data available to all activities. The code for setting up a singleton structure was borrowed from http://www.javaworld.com/article/2073352/core-java/simply-singleton.html
 * @author  Cole Fudge <cfudge@ualberta.ca>
 * @version      1                (current version number of program)
 */
public class ApplicationStateModel {

	//to set up this class as a singleton:
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static ApplicationStateModel instance = null;

	protected ApplicationStateModel() {
		// Exists only to defeat instantiation.		
	}
	/**
	 * @return
	 * @uml.property  name="instance"
	 */
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
	*The required context for opening files
	*(for saving/loading locations)
	*/
	private static Context LOCATION_fileContext;
	
	/**
	*The required filename for opening files
	*(for saving/loading locations)
	*/
	private String LOCATION_FILE_NAME = "locations.sav";

	/**
	 * Adapter for displaying the list of comments in the MainListViewActivity.
	 * @uml.property  name="mLVAdapter"
	 * @uml.associationEnd  
	 */
	private MainListViewAdapter MLVAdapter;
	
	/**
	 * Adapter for displaying the list of comments in the SubCommentViewActivity.
	 * @uml.property  name="sCVAdapter"
	 * @uml.associationEnd  
	 */
	private SubCommentViewActivityAdapter SCVAdapter;
	
	/**
	 * Adapter for displaying the list of favourites in the FavouritesViewActivity.
	 * @uml.property  name="assortAdapter"
	 * @uml.associationEnd  
	 */
	private MainListViewAdapter assortAdapter;

	/**
	*The list of all head comments that in turn
	*contain all subcomments, i.e. the following
	*list contains all comments and subcomments(recursively)
	*and is used in saving/loading.
	*/
	private ArrayList<CommentModel> commentList;
	
	/**
	 * The list of all the locations that have been
	 * used in comments.
	 */
	private ArrayList<LocationModel> locationList;
	
	/**
	 * Holds all of the user's preferences and cached comments. Can be accessed from any activity.
	 * @uml.property  name="userModel"
	 * @uml.associationEnd  
	 */
	private UserModel userModel;
	
	/**
	 * The head comment that is to be displayed by the SubCommentViewActivity. The activity will show this head comment in full, followed by its subcomments in list form.
	 * @uml.property  name="subCommentViewHead"
	 * @uml.associationEnd  
	 */
	private CommentModel subCommentViewHead;
	
	/**
	 * The comment that CreateComment is to create a subcomment for. This will be null if CreateComment is to create a new head comment.
	 * @uml.property  name="createCommentParent"
	 * @uml.associationEnd  
	 */
	private CommentModel createCommentParent;
	
	/**
	 * The comment that EditComment is to change.
	 * @uml.property  name="commentToEdit"
	 * @uml.associationEnd  
	 */
	private CommentModel commentToEdit;
	
	private ArrayList<CommentModel> assortList;
	
	private static Gson GSON = null;
	
	private ArrayList<CommentModel> replyList = new ArrayList<CommentModel>();
		
	private static Location cmpLocation;
	

	/**
	*A comparator used in sorting comments by location.
	*/
	public static Comparator<CommentModel> locCompare = new Comparator<CommentModel>(){
		
		public int compare(CommentModel comment1, CommentModel comment2){
			
			//If the user's location is unavailable, simply set all of the comparisons to equal:
			if(cmpLocation == null){
				return 0;
			}
			//create a location with the latitude and longitude each of the comments under consideration:
			Location loc1 = new Location("provider");
			loc1.setLatitude(comment1.getLocation().getLatitude());
			loc1.setLongitude(comment1.getLocation().getLongitude());

			Location loc2 = new Location("provider");
			loc2.setLatitude(comment2.getLocation().getLatitude());
			loc2.setLongitude(comment2.getLocation().getLongitude());
			
			//get the difference in their distance from the user:
			double difference = (loc2.distanceTo(cmpLocation) - loc1.distanceTo(cmpLocation));
			
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
	*A comparator used in sorting locationModels.
	*/
	public static Comparator<LocationModel> locationModelCompare = new Comparator<LocationModel>(){
		
		public int compare(LocationModel location1, LocationModel location2){
			
			//Get the user's current location:
			final LocationListenerModel locationListener = new LocationListenerModel(COMMENT_fileContext);
			Location userLocation = locationListener.getLastBestLocation();
			//If the user's location is unavailable, simply set all of the comparisons to equal:
			if(userLocation == null){
				return 0;
			}
			//create a location object with the latitude and longitude each location under consideration:
			Location loc1 = new Location("provider");
			loc1.setLatitude(location1.getLatitude());
			loc1.setLongitude(location1.getLongitude());

			Location loc2 = new Location("provider");
			loc2.setLatitude(location2.getLatitude());
			loc2.setLongitude(location2.getLongitude());
			
			//get the difference in their distance from the user:
			double difference = (loc1.distanceTo(userLocation) - loc2.distanceTo(userLocation));
			
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
			return time2.compareTo(time1);
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
	/**
	 * @return
	 * @uml.property  name="commentToEdit"
	 */
	public CommentModel getCommentToEdit()
	{
	
		return this.commentToEdit;
	}
	
	/**
	 * @param commentToEdit
	 * @uml.property  name="commentToEdit"
	 */
	public void setCommentToEdit(CommentModel commentToEdit)
	{
	
		this.commentToEdit = commentToEdit;
	}
	
	/**
	 * @return
	 * @uml.property  name="mLVAdapter"
	 */
	public MainListViewAdapter getMLVAdapter() {
		return this.MLVAdapter;
	}

	/**
	 * @param mLVAdapter
	 * @uml.property  name="mLVAdapter"
	 */
	public void setMLVAdapter(MainListViewAdapter mLVAdapter) {
		this.MLVAdapter = mLVAdapter;
	}

	/**
	 * @return
	 * @uml.property  name="sCVAdapter"
	 */
	public SubCommentViewActivityAdapter getSCVAdapter() {
		return this.SCVAdapter;
	}

	/**
	 * @param sCVAdapter
	 * @uml.property  name="sCVAdapter"
	 */
	public void setSCVAdapter(SubCommentViewActivityAdapter sCVAdapter) {
		this.SCVAdapter = sCVAdapter;
	}

	public void setFileContext(Context fileContext) {
		this.USER_fileContext = fileContext;
		ApplicationStateModel.COMMENT_fileContext = fileContext;
		ApplicationStateModel.LOCATION_fileContext = fileContext;
	}

	/**
	 * @return
	 * @uml.property  name="createCommentParent"
	 */
	public CommentModel getCreateCommentParent() {
		return this.createCommentParent;
	}

	/**
	 * @param createCommentParent
	 * @uml.property  name="createCommentParent"
	 */
	public void setCreateCommentParent(CommentModel createCommentParent) {
		this.createCommentParent = createCommentParent;
	}

	/**
	 * @return
	 * @uml.property  name="subCommentViewHead"
	 */
	public CommentModel getSubCommentViewHead() {
		return this.subCommentViewHead;
	}

	/**
	 * @param subCommentViewHead
	 * @uml.property  name="subCommentViewHead"
	 */
	public void setSubCommentViewHead(CommentModel subCommentViewHead) {
		this.subCommentViewHead = subCommentViewHead;
	}

	/**
	 * @return
	 * @uml.property  name="commentList"
	 */
	public ArrayList<CommentModel> getCommentList() {
		return this.commentList;
	}

	/**
	 * @param commentList
	 * @uml.property  name="commentList"
	 */
	public void setCommentList(ArrayList<CommentModel> commentList) {
		this.commentList = commentList;
	}

	/**
	 * @return
	 * @uml.property  name="userModel"
	 */
	public UserModel getUserModel() {
		return this.userModel;
	}
	
	/**
	 * @param locationList
	 * @uml.property  name="locationList"
	 */
	public void setLocationList(ArrayList<LocationModel> locationList) {
		this.locationList = locationList;
	}
	
	/**
	 * @return
	 * @uml.property  name="locationList"
	 */
	public ArrayList<LocationModel> getLocationList(){
		return this.locationList;
	}

	/**
	 * @return
	 * @uml.property  name="assortAdapter"
	 */
	public MainListViewAdapter getAssortAdapter()
	{

		return assortAdapter;
	}
	
	/**
	 * @param favsAdapter
	 * @uml.property  name="assortAdapter"
	 */
	public void setAssortAdapter(MainListViewAdapter favsAdapter)
	{
		this.assortAdapter = favsAdapter;
	}
	
	/**
	 * @return
	 * @uml.property  name="assortList"
	 */
	public ArrayList<CommentModel> getAssortList()
	{

		return assortList;
	}
	/**
	 * @param assortList
	 * @uml.property  name="assortList"
	 */
	public void setAssortList(ArrayList<CommentModel> assortList)
	{

		this.assortList = assortList;
	}
	

	public ArrayList<CommentModel> getReplyList() {
		return replyList;
	}
	public void setReplyList(ArrayList<CommentModel> replyList) {
		this.replyList = replyList;
	}

	public Location getCmpLocation()
	{
	
		return cmpLocation;
	}
	
	public void setCmpLocation(Location cmpLocation)
	{
	
		this.cmpLocation = cmpLocation;
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
	
	public void updateAssortAdapter()
	{
		this.assortAdapter.notifyDataSetChanged();
	}

	/**
	 * A method for saving the list of head
	 * comments(i.e. commentList) to file
	 * @param  void, no arguments
	 * @return      void, no return value.
	 * @see #commentList
	 */
	public void saveComments(){
	    if (GSON == null)
            constructGson();
		try {
			//open the file for writing:
			FileOutputStream fos = ApplicationStateModel.COMMENT_fileContext.openFileOutput(COMMENT_FILE_NAME,
					Context.MODE_PRIVATE); // TODO REMEMBER TO SET THE CONTEXT BEFORE YOU USE THIS
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			
			//Gson gson = new Gson();
			
			//A type token to pass GSON, indicating that we want to load
			//a list of CommentModels:
			Type fooType = new TypeToken<ArrayList<CommentModel>>() {}.getType();
			
			//Conver the list to a JSON string, saving it to file:
			GSON.toJson(commentList, fooType, osw); 
			
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
	    if (GSON == null)
            constructGson();
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
			
			//Gson gson = new Gson();

			//create a type token to tell GSON what type of object it is saving:
			Type fooType = new TypeToken<ArrayList<CommentModel>>() {}.getType();
			
			//use GSON object, type token and file stream to get list of
			//head comments from file:
			ArrayList<CommentModel> list_temp = GSON.fromJson(isr, fooType);
			
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
			if(userModel != null){
				CommentModel.updateComments(this.commentList, this.userModel.getFavourites());
				CommentModel.updateComments(this.commentList, this.userModel.getWantToReadComments());
			}
			if(this.getSubCommentViewHead() != null){
				this.subCommentViewHead = this.subCommentViewHead.findInArrayList(commentList);
				//If the sub comment head exists but is no longer in the list after loading,
				//something has gone wrong since comments cannot be deleted:
				if(this.subCommentViewHead == null){
					Log.e("Comment Missing", "Couldn't find subCommentViewHead in list of comments after loading.");
				}
			}
			if(this.MLVAdapter != null){
				this.updateMainAdapter();
			}
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
	    if (GSON == null)
            constructGson();
		try {
			//Obtain the file to write, creating it if it does not exist:
			FileOutputStream fos = USER_fileContext.openFileOutput(USER_FILE_NAME,
					Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			
			//Create a GSON object and type token:
			//Gson gson = new Gson();
			Type fooType = new TypeToken<UserModel>() {}.getType();
			
			//Obtain the JSON string for the object/type provided
			//and save it to file:
			GSON.toJson(userModel, fooType, osw);
			
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
	    if (GSON == null)
            constructGson();
//		boolean assortIsFavList = false;
//		boolean assortIsWantReadList = false;
//		if((this.assortList != null) && (this.assortList == this.userModel.getFavourites())){
//			assortIsFavList = true;
//		}
//		if((this.assortList != null) && (this.assortList == this.userModel.getWantToReadComments())){
//			assortIsWantReadList = true;
//		}
		ArrayList<CommentModel> favsReference;
		ArrayList<CommentModel> readLaterReference;
		FileInputStream fis;
		try
		{
			//Obtain the file to read:
			fis = USER_fileContext.openFileInput(USER_FILE_NAME);
			InputStreamReader isr = new InputStreamReader(fis);

			//Create the GSON object and type token:
			//Gson gson = new Gson();
			Type fooType = new TypeToken<UserModel>() {}.getType();
			
			//Get the UserModel from the File using the GSON object
			UserModel loadedUser = GSON.fromJson(isr, fooType);
			
			//if the file was empty, keep an empty list, not null.
			//Otherwise, set the user to whatever was in the file:
			if(loadedUser != null){
				//If we already had the usermodel loaded, then
				//other classes might rely on the references to the 
				//
				if(this.userModel != null){
					favsReference = this.userModel.getFavourites();
					readLaterReference = this.userModel.getWantToReadComments();
					favsReference.clear();
					readLaterReference.clear();
					favsReference.addAll(loadedUser.getFavourites());
					readLaterReference.addAll(loadedUser.getWantToReadComments());
					loadedUser.setFavourites(favsReference);
					loadedUser.setWantToReadComments(readLaterReference);
				}
			}
			else{
				loadedUser = new UserModel(USER_fileContext);
			}
			this.userModel = loadedUser;

			//close the file:
			isr.close();
			fis.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			this.userModel = new UserModel(USER_fileContext);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * A method for saving the list of location
	 * models(i.e. locationList) to file
	 * @param  void, no arguments
	 * @return      void, no return value.
	 * @see #locationList
	 */
	public void saveLocations(){
	    if (GSON == null)
            constructGson();
		try {
			//open the file for writing:
			FileOutputStream fos = ApplicationStateModel.LOCATION_fileContext.openFileOutput(LOCATION_FILE_NAME,
					Context.MODE_PRIVATE); // TODO REMEMBER TO SET THE CONTEXT BEFORE YOU USE THIS
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			
			//Gson gson = new Gson();
			
			//A type token to pass GSON, indicating that we want to load
			//a list of CommentModels:
			Type fooType = new TypeToken<ArrayList<LocationModel>>() {}.getType();
			
			//Convert the list to a JSON string, saving it to file:
			GSON.toJson(locationList, fooType, osw); 
			
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
	 * A method for loading the list of location
	 * models(i.e. locationList) from file.
	 * The locationList is changed in place,
	 * i.e. the reference is not changed, in
	 * order to avoid conflicting references.
	 * @param  void, no arguments
	 * @return      void, no return value.
	 * @see #commentList
	 */
	public void loadLocations(){
	    if (GSON == null)
            constructGson();
		FileInputStream fis;
		//clear the comment list so that their
		//are no duplicates and we have only 
		//comments from file:
		if(this.locationList != null){
			this.locationList.clear();
		}
		else{
			this.locationList = new ArrayList<LocationModel>();
		}
		try
		{
			//Get the file for reading:
			fis = ApplicationStateModel.LOCATION_fileContext.openFileInput(LOCATION_FILE_NAME);
			InputStreamReader isr = new InputStreamReader(fis);
			
			//Gson gson = new Gson();

			//create a type token to tell GSON what type of object it is saving:
			Type fooType = new TypeToken<ArrayList<LocationModel>>() {}.getType();
			
			//use GSON object, type token and file stream to get list of
			//head comments from file:
			ArrayList<LocationModel> list_temp = GSON.fromJson(isr, fooType);
			
			//if the comments in the file were null, we can't call list_temp.size()
			//or list_temp.get(i)
			if(list_temp != null){
				//iterate through the list to add each element, as opposed to
				//changing the commentList's reference by assignment:
				for(int i = 0; i < list_temp.size(); i++)
					this.locationList.add(list_temp.get(i));
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

	 
	 public boolean isNetworkAvailable(Context context) {
		    ConnectivityManager connectivityManager 
		          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
	 public void pictureSort(ArrayList<CommentModel> commentList, Comparator<CommentModel> cmp) {
		 ArrayList<CommentModel> noPicArray = new ArrayList<CommentModel>();
		 ArrayList<CommentModel> picArray = new ArrayList<CommentModel>();
		 for (CommentModel comment : commentList) {
			 // If comment does not have a photo
			 if (comment.getPhotoPath() == null) {
				 // Add it to the array containing comments without pictures
				 noPicArray.add(comment);
			 }
			 else{
				 // Remove it from the array containing comments with pictures
				 picArray.add(comment);
			 }
		 }
		 if(cmp != null){
			 // Sort each array
			 Collections.sort(picArray, cmp);
			 Collections.sort(noPicArray, cmp);
		 }
		 commentList.clear();
		 // Combine both arrays
		 commentList.addAll(picArray);
		 commentList.addAll(noPicArray);
	 }
	 
	 /**
     * Constructs a Gson with a custom serializer / desserializer registered for Bitmaps.
     */
    private static void constructGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
        GSON = builder.create();
    }
	 
}	


