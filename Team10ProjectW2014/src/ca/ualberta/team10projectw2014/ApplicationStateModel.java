package ca.ualberta.team10projectw2014;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ApplicationStateModel {
	private static ApplicationStateModel instance = null;
	private Context COMMENT_fileContext;
    private String COMMENT_FILE_NAME = "comments.sav";
    private Context USER_fileContext;
    private MainListViewAdapter MLVAdapter;
    
    public MainListViewAdapter getMLVAdapter() {
		return MLVAdapter;
	}

	public void setMLVAdapter(MainListViewAdapter mLVAdapter) {
		MLVAdapter = mLVAdapter;
	}

	private SubCommentViewActivityAdapter SCVAdapter;

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

	private String USER_FILE_NAME = "user.sav";
    private ArrayList<CommentModel> commentList;
    private UserModel userModel;
    private CommentModel subCommentViewHead;
    private CommentModel createCommentParent;
    
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
protected ApplicationStateModel() {
      // Exists only to defeat instantiation.
   }
   public static ApplicationStateModel getInstance() {
      if(instance == null) {
         instance = new ApplicationStateModel();
      }
      
      return instance;
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
	    commentList = new ArrayList<CommentModel>();
	        try
	        {
	        	if(COMMENT_fileContext == null)
	        			Log.e("No file Context", "Context is null");
	            fis = COMMENT_fileContext.openFileInput(COMMENT_FILE_NAME);
	            InputStreamReader isr = new InputStreamReader(fis);
	            Gson gson = new Gson();
	            
	            Type fooType = new TypeToken<ArrayList<CommentModel>>() {}.getType();
	            ArrayList<CommentModel> list_temp = gson.fromJson(isr, fooType);
	            if(list_temp != null)
	            	commentList = list_temp;
	            isr.close();
	            fis.close();
	            MLVAdapter.notifyDataSetChanged();
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
   
   
   public ArrayList<CommentModel> getCommentList() {
		return commentList;
	}

	public UserModel getUserModel() {
		return userModel;
	}

   
}
