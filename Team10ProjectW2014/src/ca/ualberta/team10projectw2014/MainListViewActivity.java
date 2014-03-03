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
import java.util.Calendar;
import java.util.Comparator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
	
	private static final NetworkConnectionController connectionController = new NetworkConnectionController();
	
	//comparator used in sorting comments by date:
	private static Comparator locCompare = new Comparator(){
		public int compare(Object comment1, Object comment2){
			LocationModel userLocation = connectionController.getUserLocation();
			LocationModel loc1 = ((HeadModel)comment1).getLocation();
			LocationModel loc2 = ((HeadModel)comment2).getLocation();
			return loc1.distanceTo(userLocation) - loc2.distanceTo(userLocation);
		}
	};
	
	//comparator used in sorting comments by location:
	private static Comparator dateCompare = new Comparator(){
		public int compare(Object comment1, Object comment2){
			Calendar time1 = ((HeadModel)comment1).getTimestamp();
			Calendar time2 = ((HeadModel)comment2).getTimestamp();
			return time1.compareTo(time2);
		}
	};
	
	private static Comparator popularityCompare = new Comparator(){
		public int compare(Object comment1, Object comment2){
			int favs1 = ((HeadModel) comment1).getNumFavourites();
			int favs2 = ((HeadModel) comment2).getNumFavourites();
			return (favs1 - favs2);
		}
	};
	
	/**
	 * Prepares the view to display the activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_head_comment_view);
		
		//Getting the head comment list view defined in the XML file:
		commentView = (ListView) findViewById(R.id.HeadCommentList);
		
		//Use the comment controller to load the head comments from file:
		commentList = (ArrayList<HeadModel>) commentDataController.loadFromFile();
		
		//Call the constructor to create a new custom Array Adapter of type HeadModel: 
		adapter = new MainListViewAdapter(this, commentList);
	}

	
	/**
	 * Sets up action bar options
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if present.
		getMenuInflater().inflate(R.menu.head_comment_view, menu);
		return true;
	}
	

	/**
	 * I do almost the same thing here as in onCreate, but refresh the view
	 * if there has already been data loaded:
	 */
	protected void onStart(){
		
		super.onStart(); 
		setContentView(R.layout.activity_head_comment_view);
		commentView = (ListView) findViewById(R.id.HeadCommentList);
		
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

	/**
	 * I do almost the same thing here as in onCreate, but refresh the view
	 * if there has already been data loaded:
	 */
	protected void onResume(){
		super.onResume(); 
		setContentView(R.layout.activity_head_comment_view);
		commentView = (ListView) findViewById(R.id.HeadCommentList);
		if(adapter != null){
			commentList.clear();
			adapter.notifyDataSetChanged();
		}
		//Use the comment controller to load the head comments from file:
		commentList = (ArrayList<HeadModel>) commentDataController.loadFromFile();
		
		//Call the constructor to create a new custom Array Adapter of type HeadModel: 
		adapter = new MainListViewAdapter(this, commentList);
		
	}	
	
	/**
	 * Responds to selection of options from the action bar:
	 */
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
