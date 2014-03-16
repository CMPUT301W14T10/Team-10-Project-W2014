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

package ca.ualberta.team10projectw2014;
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

/**
 * This class handles the activity displaying the list view of head comments.
 * @author Cole Fudge
 */
public class MainListViewActivity extends Activity{
	private ListView commentView;
	private static LayoutInflater layoutInflater;
	private ApplicationStateModel appState;
	

	
	/**
	 * Prepares the view to display the activity.
	 */
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
	 * if there has already been data loaded and sort comments
	 * according to the options selected:
	 */
	protected void onResume(){
		super.onResume(); 
		setContentView(R.layout.activity_head_comment_view);
		commentView = (ListView) findViewById(R.id.HeadCommentList);
		appState.setFileContext(this);
		appState.loadComments();
		appState.loadUser();
		appState.setMLVAdapter(new MainListViewAdapter(this, appState.getCommentList()));
		
		/**
		 * Head Comment Sorting:
		 * Checks which selection method is active and sorts the list accordingly
		 */
		// Sort by picture
		if (appState.getUserModel().isSortByPic() == true) {
			// Sort by date
			if (appState.getUserModel().isSortByDate() == true) {
				appState.setCommentList(appState.pictureSort(appState.getCommentList(), ApplicationStateModel.dateCompare));
			}
			else if(appState.getUserModel().isSortByLoc() == true) {
				appState.setCommentList(appState.pictureSort(appState.getCommentList(), ApplicationStateModel.locCompare));
			}
			else if(appState.getUserModel().isSortByPopularity())
				appState.setCommentList(appState.pictureSort(appState.getCommentList(), ApplicationStateModel.popularityCompare));
		}
		else {
			// Sort by date
			if (appState.getUserModel().isSortByDate() == true) {
				appState.setCommentList(appState.sort(appState.getCommentList(), ApplicationStateModel.dateCompare));
			}
			else if(appState.getUserModel().isSortByLoc() == true) {
				appState.setCommentList(appState.sort(appState.getCommentList(), ApplicationStateModel.locCompare));
			}
			else if(appState.getUserModel().isSortByPopularity())
				appState.setCommentList(appState.sort(appState.getCommentList(), ApplicationStateModel.popularityCompare));
		}
		
		commentView.setAdapter(appState.getMLVAdapter());
		appState.updateMainAdapter();

		commentView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id){
				CommentModel headComment = appState.getCommentList().get(position);
				Intent subCommentView = new Intent(getApplicationContext(), SubCommentViewActivity.class);
				//subCommentView.putExtra("comment", (Object) headComment);
				appState.setSubCommentViewHead(headComment);
				view.getContext().startActivity(subCommentView);
				
			}});
		appState.updateMainAdapter();
	}	
	
	/**
	 * Responds to selection of options from the action bar:
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.add_comment:
				Intent createComment = new Intent(getApplicationContext(), CreateCommentActivity.class);
				createComment.putExtra("username", appState.getUserModel().getUsername());
				appState.setCreateCommentParent(null);
				this.startActivity(createComment);
				return true;
			case R.id.action_edit_username_main:
				edit_username();
				return true;
			case R.id.action_sort_main:
				sort_comments();
				return true;
			case R.id.action_favourites_main:
				appState.setCommentList(appState.getUserModel().getFavourites());
				appState.updateMainAdapter();
				return true;
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
	private void sort_comments(){
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
