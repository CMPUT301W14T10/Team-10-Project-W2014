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

import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;

/**
 * Sets up action bar options
 */

/**

 */

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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * This class handles the activity displaying the list view of head comments.
 * @author Cole Fudge
 * @version     1                (current version number of program)
 * 
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
		appState = ApplicationStateModel.getInstance();
		appState.setCommentList(new ArrayList<CommentModel>());
		appState.setFileContext(this);
		appState.loadComments();
		appState.loadUser();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if present.
		getMenuInflater().inflate(R.menu.head_comment_view, menu);
		//Tells the ApplicationStateModel singleton to load comments/user 
		//from network(when implemented)/file and creates a new adapter
		//in the appState for these values.
		appState.setFileContext(this);
		appState.loadComments();
		appState.loadUser();
		appState.setMLVAdapter(new MainListViewAdapter(this, appState.getCommentList()));
		return true;
	}
	
	//In onResume the content view is set and the appState
	//is told to reload and update the view, in case
	//this was not done since any changes occurred.
	protected void onResume(){
		super.onResume(); 
		setContentView(R.layout.activity_head_comment_view);
		commentView = (ListView) findViewById(R.id.HeadCommentList);
		
		//call the ApplicationStateModel singleton's methods to update
		//its attributes from file(and/or a network connection when implemented):
		appState.setFileContext(this);
		appState.loadComments();
		appState.loadUser();
		
		//Head Comment Sorting:
		//Checks which selection method is active and sorts the list accordingly.
		//Sort by picture
		if (appState.getUserModel().isSortByPic() == true) {
			//Sort by date and picture:
			if (appState.getUserModel().isSortByDate() == true) {
				appState.setCommentList(appState.pictureSort(appState.getCommentList(), ApplicationStateModel.dateCompare));
			}
			//Sort by location and picture:
			else if(appState.getUserModel().isSortByLoc() == true) {
				appState.setCommentList(appState.pictureSort(appState.getCommentList(), ApplicationStateModel.locCompare));
			}
			//Sort by popularity(i.e. number of times favourited) and picture:
			else if(appState.getUserModel().isSortByPopularity())
				appState.setCommentList(appState.pictureSort(appState.getCommentList(), ApplicationStateModel.popularityCompare));
		}
		//Sort without sorting by picture:
		else {
			//Sort by date
			if (appState.getUserModel().isSortByDate() == true) {
				appState.setCommentList(appState.sort(appState.getCommentList(), ApplicationStateModel.dateCompare));
			}
			//Sort by location:
			else if(appState.getUserModel().isSortByLoc() == true) {
				appState.setCommentList(appState.sort(appState.getCommentList(), ApplicationStateModel.locCompare));
			}
			//Sort by popularity(i.e. number of times favourited):
			else if(appState.getUserModel().isSortByPopularity())
				appState.setCommentList(appState.sort(appState.getCommentList(), ApplicationStateModel.popularityCompare));
		}
		
		//Create an adapter to reflect the comments loaded/sorted:
		appState.setMLVAdapter(new MainListViewAdapter(this, appState.getCommentList()));
		
		//Set the commentView in this activity to reflect the corresponding 
		//adapter in the ApplicationStateModel:
		commentView.setAdapter(appState.getMLVAdapter());

		//Opens SubCommentViewActivity when a comment is selected:
		commentView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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
		switch(item.getItemId()){
			//open the CreateCommentActivity. The parent comment
			//is null because CreateCommentActiviy creates a new
			//head comment when it is null. It creates a sub comment
			//responding to CreateCommentParent otherwise.
			case R.id.add_comment:
				Intent createComment = new Intent(getApplicationContext(), CreateCommentActivity.class);
				appState.setCreateCommentParent(null);
				this.startActivity(createComment);
				return true;
			case R.id.action_edit_username_main:
				edit_username();
				return true;
			case R.id.action_sort_main:
				sortComments();
				return true;
			//Display the list of favourites specified in the user model
			//when implemented:
			case R.id.action_favourites_main:
				appState.setCommentList(appState.getUserModel().getFavourites());
				appState.updateMainAdapter();
				return true;
			//Display the list of want to read comments specified in the user model
			//when implemented:
			case R.id.action_want_to_read_main:
				appState.setCommentList(appState.getUserModel().getWantToReadComments());
				appState.updateMainAdapter();
				return true;
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
	
	/**
	 * Brings up a dialog box to prompt user for sorting criteria:
	 */
	private void sortComments(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		//set the fields of the dialog:
		alert.setTitle("Sort By:");
	
		LinearLayout optionsView = (LinearLayout)layoutInflater.inflate(R.layout.sort_by_dialog_list, 
				null);
		
		ViewGroup sortRadioGroup = (ViewGroup) optionsView.getChildAt(0);
				
		RadioButton button;
		CheckBox box;
		
		if(appState.getUserModel().isSortByDate()){
			button = (RadioButton) sortRadioGroup.getChildAt(0);
			button.toggle();
		}
		else if(appState.getUserModel().isSortByLoc()){
			button = (RadioButton) sortRadioGroup.getChildAt(1);
			button.toggle();
		}
		else if(appState.getUserModel().isSortByPopularity()){
			button = (RadioButton) sortRadioGroup.getChildAt(2);
			button.toggle();
		}

		if(appState.getUserModel().isSortByPic()){
			box = (CheckBox) optionsView.getChildAt(2);
			box.setChecked(true);
		}
		
		alert.setView(optionsView);

		//set the positive button with its text and set up an on click listener
		//to add the counter with the text provided when it is pressed:
		alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			
				
				appState.saveUser();
				onResume();
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
	
	//from the android developer page http://developer.android.com/guide/topics/ui/controls/checkbox.html
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.pictures:
	            if (checked)
	            	appState.getUserModel().setSortByPic(true);
	            else
	            	appState.getUserModel().setSortByPic(false);
	            break;
	    }
	}
	
	/**
	 * Responds to clicks on a radio button in the sort by alert
	 * dialog. Adapted from the android developer website
	 * http://developer.android.com/guide/topics/ui/controls/radiobutton.html
	 */
	public void onRadioButtonClicked(View view) {
		RadioButton buttonPressed = (RadioButton) view;
		RadioGroup buttonGroup = (RadioGroup) buttonPressed.getParent();
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    buttonGroup.clearCheck();
	    // Check which radio button was clicked
	    switch(view.getId()) {
	    	
	        case R.id.date:
	            if (checked){
	            	appState.getUserModel().setSortByDate(true);
	            	buttonPressed.toggle();
	            	appState.saveUser();
	            }
	            else{
	        		appState.getUserModel().setSortByDate(false);
            		appState.saveUser();
	            }
	            break;
	            
	        case R.id.location:
	            if (checked){
	            	appState.getUserModel().setSortByLoc(true);
	            	buttonPressed.toggle();
	            	appState.saveUser();
	            }
	            else{
	            	appState.getUserModel().setSortByLoc(false);
	            	appState.saveUser();
	            }
	            break;
	            
	        case R.id.number_of_favourites:
	            if (checked){
	            	appState.getUserModel().setSortByPopularity(true);
	            	buttonPressed.toggle();
	            	appState.saveUser();
	            }
	            else{
	            	appState.getUserModel().setSortByPopularity(false);
	            	appState.saveUser();
	            }
	            break;
	            
	    }
	}
	
	
}	
