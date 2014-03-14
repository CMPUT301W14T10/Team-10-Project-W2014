package ca.ualberta.team10projectw2014;

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
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

	private Context COMMENT_fileContext;
	private String COMMENT_FILE_NAME = "comments.sav";
	private Context USER_fileContext;
	private MainListViewAdapter MLVAdapter;
	private String USER_FILE_NAME = "user.sav";
	private ArrayList<CommentModel> commentList;
	private UserModel userModel;
	private CommentModel subCommentViewHead;
	private CommentModel createCommentParent;
	private SubCommentViewActivityAdapter SCVAdapter;
	private static Location location = null;



	//comparator used in sorting comments by location:
	private static Comparator locCompare = new Comparator(){
		public int compare(Object comment1, Object comment2){
			LocationModel userLocation = new LocationModel("here", location);
			LocationModel loc1 = ((CommentModel)comment1).getLocation();
			LocationModel loc2 = ((CommentModel)comment2).getLocation();
			return loc1.distanceTo(userLocation) - loc2.distanceTo(userLocation);
		}
	};

	//comparator used in sorting comments by date:
	private static Comparator dateCompare = new Comparator(){
		public int compare(Object comment1, Object comment2){
			Calendar time1 = ((CommentModel)comment1).getTimestamp();
			Calendar time2 = ((CommentModel)comment2).getTimestamp();
			return time1.compareTo(time2);
		}
	};

	//comparator used in sorting comments by number of likes:
	private static Comparator popularityCompare = new Comparator(){
		public int compare(Object comment1, Object comment2){
			int favs1 = ((CommentModel) comment1).getNumFavourites();
			int favs2 = ((CommentModel) comment2).getNumFavourites();
			return (favs1 - favs2);
		}
	};

	public MainListViewAdapter getMLVAdapter() {
		return MLVAdapter;
	}

	public void setMLVAdapter(MainListViewAdapter mLVAdapter) {
		MLVAdapter = mLVAdapter;
	}

	public SubCommentViewActivityAdapter getSCVAdapter() {
		return SCVAdapter;
	}

	public void setSCVAdapter(SubCommentViewActivityAdapter sCVAdapter) {
		SCVAdapter = sCVAdapter;
	}

	public void setFileContext(Context fileContext) {
		USER_fileContext = fileContext;
		COMMENT_fileContext = fileContext;
	}

	public CommentModel getCreateCommentParent() {
		return createCommentParent;
	}

	public void setCreateCommentParent(CommentModel createCommentParent) {
		this.createCommentParent = createCommentParent;
	}

	public CommentModel getSubCommentViewHead() {
		return subCommentViewHead;
	}

	public void setSubCommentViewHead(CommentModel subCommentViewHead) {
		this.subCommentViewHead = subCommentViewHead;
	}

	public ArrayList<CommentModel> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<CommentModel> commentList) {
		this.commentList = commentList;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void updateMainAdapter(){
		MLVAdapter.notifyDataSetChanged();
	}

	public void saveComments(){
		try {
			FileOutputStream fos = COMMENT_fileContext.openFileOutput(COMMENT_FILE_NAME,
					Context.MODE_PRIVATE); // TODO REMEMBER TO SET THE CONTEXT BEFORE YOU USE THIS
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			Gson gson = new Gson();
			Type fooType = new TypeToken<ArrayList<CommentModel>>() {}.getType();
			gson.toJson(commentList, fooType, osw); //TODO Initialize this list
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

	public void loadComments(){
		FileInputStream fis;
		try
		{
			fis = COMMENT_fileContext.openFileInput(COMMENT_FILE_NAME);
			InputStreamReader isr = new InputStreamReader(fis);
			Gson gson = new Gson();

			Type fooType = new TypeToken<ArrayList<CommentModel>>() {}.getType();
			ArrayList<CommentModel> list_temp = gson.fromJson(isr, fooType);
			if(list_temp != null){
				for(int i = 0; i < list_temp.size(); i++)
					commentList.add(list_temp.get(i));
			}
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

	public void saveUser(){
		try {
			FileOutputStream fos = USER_fileContext.openFileOutput(USER_FILE_NAME,
					Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			Gson gson = new Gson();
			Type fooType = new TypeToken<UserModel>() {}.getType();
			gson.toJson(userModel, fooType, osw);
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

	public void loadUser(){
		FileInputStream fis;
		userModel = new UserModel(USER_fileContext);
		try
		{
			fis = USER_fileContext.openFileInput(USER_FILE_NAME);
			InputStreamReader isr = new InputStreamReader(fis);
			Gson gson = new Gson();

			Type fooType = new TypeToken<UserModel>() {}.getType();
			UserModel list_temp = gson.fromJson(isr, fooType);
			if(list_temp != null)
				userModel = list_temp;
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
	 * @param commentList array of CommentModels to sort
	 * @param cmp comparator to compare CommentModels when sorting
	 * @return the array of head comments
	 */
	 public ArrayList<CommentModel> sort(ArrayList<CommentModel> list, Comparator cmp) {
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
	  * @param commentList array of CommentModels to sort
	  * @param cmp comparator to compare CommentModels when sorting
	  * @return the array of head comments
	  */
	 public ArrayList<CommentModel> pictureSort(ArrayList<CommentModel> list, Comparator cmp) {
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
}	


