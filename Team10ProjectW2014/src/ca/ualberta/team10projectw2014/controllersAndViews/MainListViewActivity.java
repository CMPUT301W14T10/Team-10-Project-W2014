/**
 * Copyright 2014 Cole Fudge, Steven Giang, Bradley Poulete, David Yee, and Costa Zervos

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

package ca.ualberta.team10projectw2014.controllersAndViews;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.network.ElasticSearchOperations;

/**
 * This class handles the activity displaying the list view of head comments.
 * @author Cole Fudge <cfudge@ualberta.ca>
 * @version     1                (current version number of program)
 */
public class MainListViewActivity extends Activity{
	private ListView commentView;
	private static LayoutInflater layoutInflater;
	private ApplicationStateModel appState;

	
	
	// In onCreate we will prepare the view to 
	//display the activity and set up the 
	//ApplicationStateModel, which is a singleton 
	//used throughout the application
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_head_comment_view);
		layoutInflater = LayoutInflater.from(this);
		this.appState = ApplicationStateModel.getInstance();
		this.appState.setCommentList(new ArrayList<CommentModel>());
		this.appState.setFileContext(this);
		
		
		
		
		this.appState.loadComments();
		this.appState.loadUser();
		
		ElasticSearchOperations.searchForCommentModels("", this.appState.getCommentList(), this);
		Log.e("Elastic Search MLVA", appState.getCommentList().get(0).getTitle().toString());
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if present.
		getMenuInflater().inflate(R.menu.head_comment_view, menu);
		//Tells the ApplicationStateModel singleton to load comments/user 
		//from network(when implemented)/file and creates a new adapter
		//in the appState for these values.
		this.appState.setFileContext(this);
		this.appState.loadComments();
		this.appState.loadUser();
		this.appState.setMLVAdapter(new MainListViewAdapter(this, this.appState.getCommentList()));
		return true;
	}
	
	//In onResume the content view is set and the appState
	//is told to reload and update the view, in case
	//this was not done since any changes occurred.
	protected void onResume(){
		super.onResume(); 
		setContentView(R.layout.activity_head_comment_view);
		this.commentView = (ListView) findViewById(R.id.HeadCommentList);
		
		//call the ApplicationStateModel singleton's methods to update
		//its attributes from file(and/or a network connection when implemented):
		this.appState.setFileContext(this);
		this.appState.loadComments();
		this.appState.loadUser();
		
		//Head Comment Sorting:
		//Checks which selection method is active and sorts the list accordingly.
		//Sort by picture
		if (this.appState.getUserModel().isSortByPic() == true) {
			//Sort by date and picture:
			if (this.appState.getUserModel().isSortByDate() == true) {
				this.appState.setCommentList(this.appState.pictureSort(this.appState.getCommentList(), ApplicationStateModel.dateCompare));
			}
			//Sort by location and picture:
			else if(this.appState.getUserModel().isSortByLoc() == true) {
				this.appState.setCommentList(this.appState.pictureSort(this.appState.getCommentList(), ApplicationStateModel.locCompare));
			}
			//Sort by popularity(i.e. number of times favourited) and picture:
			else if(this.appState.getUserModel().isSortByPopularity())
				this.appState.setCommentList(this.appState.pictureSort(this.appState.getCommentList(), ApplicationStateModel.popularityCompare));
		}
		//Sort without sorting by picture:
		else {
			//Sort by date
			if (this.appState.getUserModel().isSortByDate() == true) {
				this.appState.setCommentList(this.appState.sort(this.appState.getCommentList(), ApplicationStateModel.dateCompare));
			}
			//Sort by location:
			else if(this.appState.getUserModel().isSortByLoc() == true) {
				this.appState.setCommentList(this.appState.sort(this.appState.getCommentList(), ApplicationStateModel.locCompare));
			}
			//Sort by popularity(i.e. number of times favourited):
			else if(this.appState.getUserModel().isSortByPopularity())
				this.appState.setCommentList(this.appState.sort(this.appState.getCommentList(), ApplicationStateModel.popularityCompare));
		}
		
		//Create an adapter to reflect the comments loaded/sorted:
		this.appState.setMLVAdapter(new MainListViewAdapter(this, this.appState.getCommentList()));
		
		//Set the commentView in this activity to reflect the corresponding 
		//adapter in the ApplicationStateModel:
		this.commentView.setAdapter(this.appState.getMLVAdapter());

		//Opens SubCommentViewActivity when a comment is selected:
		this.commentView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id){
				//get the comment that was selected
				CommentModel headComment = appState.getCommentList().get(position);
				Intent subCommentView = new Intent(getApplicationContext(), SubCommentViewActivity.class);
				//Set the appropriate singleton attribute to point to the comment that
				//SubCommentViewActivity is to represent:
				appState.setSubCommentViewHead(headComment);
				//start the SubCommentViewActivity on top of the activity
				//containing the view(i.e. this MainListViewActivity):
				view.getContext().startActivity(subCommentView);
			}});
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent assortList = new Intent(getApplicationContext(), AssortedListViewActivity.class);
		switch(item.getItemId()){
			//open the CreateCommentActivity. The parent comment
			//is null because CreateCommentActiviy creates a new
			//head comment when it is null. It creates a sub comment
			//responding to CreateCommentParent otherwise.
			case R.id.add_comment:
				//Start the create comment activity to add a comment. 
				//CreateCommentParent is null because the CreateComment
				//activity makes a subcomment inside the parent comment
				//provided, or makes a new head comment if it is null:
				Intent createComment = new Intent(getApplicationContext(), CreateCommentActivity.class);
				this.appState.setCreateCommentParent(null);
				this.startActivity(createComment);
				return true;
			case R.id.action_edit_username_main:
				editUsername();
				return true;
			case R.id.action_sort_main:
				sortComments();
				return true;

			//Display the list of favourites specified in the user model
			//when implemented:
			case R.id.action_favourites_main:
				this.appState.setAssortList(appState.getUserModel().getFavourites());
				this.appState.setAssortViewTitle("Favourites");
				this.startActivity(assortList);
				return true;
			//Display the list of want to read comments specified in the user model
			//when implemented:
			case R.id.action_want_to_read_main:
				this.appState.setAssortList(appState.getUserModel().getWantToReadComments());
				this.appState.setAssortViewTitle("Want to Read");
				this.startActivity(assortList);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	
	/**
	 * Brings up a dialog box to prompt user for a new username:
	 */
	private void editUsername(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		//set the fields of the dialog:
		alert.setTitle("Edit Username");

		//add an EditText to the dialog for the user to enter
		//the name in:
		final EditText usernameText = new EditText(this);
		alert.setView(usernameText);

		//set the positive button with its text and set up an on click listener
		//to replace the username in the ApplicationStateModel singleton:
		alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//get the text from the editable:
				Editable usernameEditable = usernameText.getText();
				String usernameString = usernameEditable.toString();
				
				//Set the new username and save the UserModel:
				appState.getUserModel().setUsername(usernameString);
				appState.saveUser();
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
	

	
	//Adapted from the android developer page 
	//http://developer.android.com/guide/topics/ui/controls/checkbox.html
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	    	//Set user preferences according
	    	//to whether the pictures checkbox is checked:
	        case R.id.pictures:
	            if (checked)
	            	this.appState.getUserModel().setSortByPic(true);
	            else
	            	this.appState.getUserModel().setSortByPic(false);
	            break;
	    }
	}
	
	/**
	 * Responds to clicks on a radio button in the sort by alert
	 * dialog. Adapted from the android developer website
	 * http://developer.android.com/guide/topics/ui/controls/radiobutton.html
	 * @param view - the radio button that was clicked.
	 * @return void, no return value.
	 */
	public void onRadioButtonClicked(View view) {
		RadioButton buttonPressed = (RadioButton) view;
		RadioGroup buttonGroup = (RadioGroup) buttonPressed.getParent();
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    buttonGroup.clearCheck();
	    //Check which radio button was clicked and set the
	    //preferences and checked radio button as appropriate:
	    switch(view.getId()) {
	        case R.id.date:
	            if (checked){
	            	this.appState.getUserModel().setSortByDate(true);
	            	buttonPressed.toggle();
	            }
	            else{
	        		this.appState.getUserModel().setSortByDate(false);
	            }
	            break;
	            
	        case R.id.location:
	            if (checked){
	            	this.appState.getUserModel().setSortByLoc(true);
	            	buttonPressed.toggle();
	            }
	            else{
	            	this.appState.getUserModel().setSortByLoc(false);
	            }
	            break;
	            
	        case R.id.number_of_favourites:
	            if (checked){
	            	this.appState.getUserModel().setSortByPopularity(true);
	            	buttonPressed.toggle();
	            }
	            else{
	            	this.appState.getUserModel().setSortByPopularity(false);
	            }
	            break;
	            
	    }
	}
	
	
	/**
	 * Brings up a dialog box to prompt user for sorting criteria:
	 */
	private void sortComments(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		//set the fields of the dialog:
		alert.setTitle("Sort By:");
	
		//get the dialogue's layout from XML:
		LinearLayout optionsView = (LinearLayout)layoutInflater.inflate(R.layout.sort_by_dialog_list, 
				null);
		
		//get the group of radio buttons that determine sorting criteria:
		ViewGroup sortRadioGroup = (ViewGroup) optionsView.getChildAt(0);
		
		RadioButton button;
		CheckBox box;
		
		//if/else statements that set the correct radio button
		//and check the sort by picture box if appropriate:
		if(this.appState.getUserModel().isSortByDate()){
			//Set the date radio button:
			button = (RadioButton) sortRadioGroup.getChildAt(0);
			button.toggle();
		}
		else if(this.appState.getUserModel().isSortByLoc()){
			//Set the location radio button:
			button = (RadioButton) sortRadioGroup.getChildAt(1);
			button.toggle();
		}
		else if(this.appState.getUserModel().isSortByPopularity()){
			//Set the Popularity radio button:
			button = (RadioButton) sortRadioGroup.getChildAt(2);
			button.toggle();
		}

		if(this.appState.getUserModel().isSortByPic()){
			//Set the sort by picture check box:
			box = (CheckBox) optionsView.getChildAt(2);
			box.setChecked(true);
		}
		
		alert.setView(optionsView);

		//set the positive button with its text and set up an on click listener
		//that saves the changes:
		alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				appState.saveUser();
				onResume();
			}
		});

		//also set a cancel negative button that loads the old user so that the
		//changes are not applied:
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				appState.loadUser();
				onResume();
			}
		});

		alert.show();
	}
	
}	
