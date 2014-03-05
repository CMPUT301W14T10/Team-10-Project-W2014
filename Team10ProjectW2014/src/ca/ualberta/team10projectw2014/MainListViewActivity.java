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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * This class handles the activity displaying the list view of head comments.
 * @author Cole Fudge
 */
public class MainListViewActivity extends Activity{

	private ListView commentView;
	
	private MainListViewAdapter adapter;
	
	private ArrayList<HeadModel> commentList;
	
	private UserModel user;
	
	private static final CommentDataController commentDataController = new CommentDataController();
	
	private static final NetworkConnectionController connectionController = new NetworkConnectionController();
	
	private static final UserDataController userController = new UserDataController();
	
	private static LayoutInflater layoutInflater;
	
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
		
		user = userController.loadFromFile();
		layoutInflater = LayoutInflater.from(this);
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
			commentList = (ArrayList<HeadModel>) commentDataController.loadFromFile();
			adapter.notifyDataSetChanged();
		}
		//Use the comment controller to load the head comments from file:
		commentList = (ArrayList<HeadModel>) commentDataController.loadFromFile();
		
		//Call the constructor to create a new custom Array Adapter of type HeadModel: 
		adapter = new MainListViewAdapter(this, commentList);

		user = userController.loadFromFile();

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
			commentList = (ArrayList<HeadModel>) commentDataController.loadFromFile();
			adapter.notifyDataSetChanged();
		}
		//Use the comment controller to load the head comments from file:
		commentList = (ArrayList<HeadModel>) commentDataController.loadFromFile();
		
		//Call the constructor to create a new custom Array Adapter of type HeadModel: 
		adapter = new MainListViewAdapter(this, commentList);
		
		user = userController.loadFromFile();
	}	
	
	/**
	 * Responds to selection of options from the action bar:
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.add_comment:
				Intent createComment = new Intent(getApplicationContext(), CreateCommentActivity.class);
				createComment.putExtra("is_new", true);
				createComment.putExtra("is_head", true);
				this.startActivity(createComment);
				return true;
			case R.id.action_edit_username_main:
				edit_username();
				return true;
			case R.id.action_sort_main:
				sort_comments();
				return true;
			case R.id.action_favourites_main:
			case R.id.action_want_to_read_main:
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	
	/**
	 * Brings up a dialog box to prompt user for a new username:
	 */
	private void edit_username(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		//set the fields of the dialog:
		alert.setTitle("Edit Username");

		//add an EditText to the dialog for the user to enter
		//the name in:
		final EditText usernameText = new EditText(this);
		alert.setView(usernameText);

		//set the positive button with its text and set up an on click listener
		//to add the counter with the text provided when it is pressed:
		alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//get the text from the editable:
				Editable usernameEditable = usernameText.getText();
				String usernameString = usernameEditable.toString();
				
				user.setUsername(usernameString);
				userController.saveInFile();
			}
		});

		//also set a cancel negative button that does nothing but close the 
		//dialog window:
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.show();
		
	}
	
	/**
	 * Brings up a dialog box to prompt user for sorting criteria:
	 */
	private void sort_comments(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		//set the fields of the dialog:
		alert.setTitle("Sort By:");
	
		LinearLayout optionsView = (LinearLayout)layoutInflater.inflate(R.layout.sort_by_dialog_list, 
				null);
		
		ViewGroup sortRadioGroup = (ViewGroup) optionsView.getChildAt(0);
				
		RadioButton button;
		
		if(user.isSortByDate()){
			button = (RadioButton) sortRadioGroup.getChildAt(0);
			button.toggle();
		}
		else if(user.isSortByLoc()){
			button = (RadioButton) sortRadioGroup.getChildAt(1);
			button.toggle();
		}
		else if(user.isSortByPopularity()){
			button = (RadioButton) sortRadioGroup.getChildAt(2);
			button.toggle();
		}

		if(user.isSortByPic()){
			button = (RadioButton) ((ViewGroup) optionsView.getChildAt(2)).getChildAt(0);
			button.toggle();
		}
		
		alert.setView(optionsView);

		//set the positive button with its text and set up an on click listener
		//to add the counter with the text provided when it is pressed:
		alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			
				
				userController.saveInFile();
			}
		});

		//also set a cancel negative button that does nothing but close the 
		//dialog window:
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.show();
	}
	
	/**
	 * Responds to clicks on a radio button in the sort by alert
	 * dialog. Adapted from the android developer website
	 * http://developer.android.com/guide/topics/ui/controls/radiobutton.html
	 */
	public void onRadioButtonClicked(View view) {
		RadioGroup buttonGroup = (RadioGroup) buttonPressed.getParent();
		RadioButton buttonPressed = (RadioButton) view;
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	    	
	        case R.id.date:
	            if (checked){
	            	user.setSortByDate(true);
	            }
	            else
	        		buttonGroup.clearCheck();
	            break;
	            
	        case R.id.location:
	            if (checked){
	            	user.setSortByLoc(true);
	            }
	            else
	        		buttonGroup.clearCheck();
	            break;
	            
	        case R.id.number_of_favourites:
	            if (checked){
	            	user.setSortByPopularity(true);
	            }
	            else
	        		buttonGroup.clearCheck();
	            break;
	            
	        case R.id.pictures:
	        	if(checked){
	        		user.setSortByPic(true);
	        	}
	        	else{
	        		buttonGroup.clearCheck();
	            break;
	    }
	}
}
