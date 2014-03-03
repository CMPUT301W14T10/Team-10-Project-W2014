package ca.ualberta.team10projectw2014;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;

/**
 * This class handles the activity displaying the list view of head comments.
 * @author Cole Fudge
 */
public class MainListViewActivity extends Activity{

	private ListView commentView;
	
	private MainListViewAdapter adapter;
	
	private ArrayList<HeadModel> commentList;
	
	private static final CommentDataController commentDataController = new CommentDataController();
	
	//preparing the view to display the activity:
	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_head_comment_view);
		
		//Getting the head comment list view defined in the XML file:
		commentView = (ListView) findViewById(R.id.HeadCommentList);
		
		//We're going to need a context menu for each comment, so:
		registerForContextMenu(commentView);
		
		//Use the comment controller to load the head comments from file:
		commentList = (ArrayList<HeadModel>) commentDataController.loadFromFile();
		
		//Call the constructor to create a new custom Array Adapter of type HeadModel: 
		adapter = new MainListViewAdapter(this, commentList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if present.
		getMenuInflater().inflate(R.menu.head_comment_view, menu);
		return true;
	}
	
	//I do almost the same thing here as in onCreate, but refresh the view
	//if there has already been data loaded:
	protected void onStart(){
		
		super.onStart(); 
		setContentView(R.layout.activity_head_comment_view);
		commentView = (ListView) findViewById(R.id.HeadCommentList);
		registerForContextMenu(commentView);
		
		//If adapter exists, clear it and reload comments(i.e. refresh)
		if(adapter != null){
			commentList.clear();
			adapter.notifyDataSetChanged();
		}
		//Use the comment controller to load the head comments from file:
		commentList = (ArrayList<HeadModel>) commentDataController.loadFromFile();
		
		//Call the constructor to create a new custom Array Adapter of type HeadModel: 
		adapter = new MainListViewAdapter(this, commentList);

	}

	//I do almost the same thing here as in onCreate, but refresh the view
	//if there has already been data loaded:
	protected void onResume(){
		super.onResume(); 
		setContentView(R.layout.activity_head_comment_view);
		commentView = (ListView) findViewById(R.id.HeadCommentList);
		registerForContextMenu(commentView);
		commentDataController.clearCommentList();
		if(adapter != null){
			commentList.clear();
			adapter.notifyDataSetChanged();
		}
		//Use the comment controller to load the head comments from file:
		commentList = (ArrayList<HeadModel>) commentDataController.loadFromFile();
		
		//Call the constructor to create a new custom Array Adapter of type HeadModel: 
		adapter = new MainListViewAdapter(this, commentList);
		
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.add_comment:
				//Use the controller to bring up an add counter dialog
				commentDataController.addComment(this);
				//save changes:
				commentDataController.saveInFile();
				adapter.notifyDataSetChanged();
				return true;
			case R.id.sort_comments:
				//call the controller method for sorting the counters from
				//largest count to lowest count:
				commentDataController.sort();
				//save:
				commentDataController.saveInFile();
				adapter.notifyDataSetChanged();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	
}
