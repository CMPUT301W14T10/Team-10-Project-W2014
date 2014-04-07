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
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationListenerModel;
import ca.ualberta.team10projectw2014.models.LocationModel;
import ca.ualberta.team10projectw2014.network.ElasticSearchLocationOperations;
import ca.ualberta.team10projectw2014.models.QueueModel;
import ca.ualberta.team10projectw2014.network.ElasticSearchOperations;

/**
 * This class handles the activity displaying the list view of head comments.
 * @author  Cole Fudge <cfudge@ualberta.ca>
 * @version      1                (current version number of program)
 */
public class MainListViewActivity extends Activity{
	private ListView commentView;
	private LocationListenerModel locationListener;
	private static LayoutInflater layoutInflater;

	private ApplicationStateModel appState;

	SharedPreferences setOverlay;
	boolean showOverlay;
	private int spinnerFlag;
	private ArrayList<LocationModel> locationList;
	private ArrayList<LocationModel> tempLocationList;
	
	/**
	 * In onCreate we will prepare the view to display the activity and set up 
	 * the ApplicationStateModel, which is a singleton used throughout the 
	 * application
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_head_comment_view);
		locationListener = new LocationListenerModel(this);

		// below sharedpref code adapted from
		// http://stackoverflow.com/a/19232789/2557554
		setOverlay = PreferenceManager.getDefaultSharedPreferences(this);
		showOverlay = setOverlay.getBoolean("MainOverlayPref", true);
		    if (showOverlay == true) showOverLay();
		    
		setContentView(R.layout.activity_head_comment_view);
		layoutInflater = LayoutInflater.from(this);
		this.commentView = (ListView) findViewById(R.id.HeadCommentList);
		this.appState = ApplicationStateModel.getInstance();
		this.appState.setCommentList(new ArrayList<CommentModel>());
		this.appState.setFileContext(this);
		this.appState.loadUser();
		//this.appState.loadComments();
		appState.setLocationList(new ArrayList<LocationModel>());
		ElasticSearchLocationOperations.getLocationList(this);
		appState.loadLocations();
		//Log.e("Elastic Search MLVA", appState.getCommentList().get(0).getTitle().toString());
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
		//Create an adapter to reflect the comments loaded/sorted:
		this.appState.setMLVAdapter(new MainListViewAdapter(this, this.appState.getCommentList()));
		//Set the commentView in this activity to reflect the corresponding 
		//adapter in the ApplicationStateModel:
		this.commentView.setAdapter(this.appState.getMLVAdapter());
		ElasticSearchOperations.searchForCommentModels("", this.appState.getCommentList(), this);
		sortMainList();
	}

	/**
	 * Displays the main overlay
	 * the below method is adapted from 
	 * https://github.com/pranayairan/AndroidExamples/tree/master/AndroidHelpOverlay
	 */
    private void showOverLay(){
        final Dialog dialog = new Dialog(MainListViewActivity.this, 
                android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.main_overlay_view);
        LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.overlayLayout);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                SharedPreferences.Editor editor = setOverlay.edit();
                editor.putBoolean("MainOverlayPref", false);
                editor.commit();
            }
        });
        dialog.show();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if present.
		getMenuInflater().inflate(R.menu.head_comment_view, menu);
		return true;
	}
	
	/**
	 * Determines which sorting algorithm should be used on the list of 
	 * head comments, sorts the list, then updates the adapter to 
	 * display the newly sorted comments
	 */
	public void sortMainList(){
		appStateSortChecker();
		//Head Comment Sorting:
		//Checks which selection method is active and sorts the list accordingly.
		//Sort by picture
		if (this.appState.getUserModel().isSortByPic() == true) {
			//Sort by date and picture:
			if (this.appState.getUserModel().isSortByDate() == true) {
				appState.pictureSort(this.appState.getCommentList(), ApplicationStateModel.dateCompare);
			}
			//Sort by location and picture:
			else if(this.appState.getUserModel().isSortByUserLoc() == true) {
				appState.pictureSort(this.appState.getCommentList(), ApplicationStateModel.locCompare);
			}
			else if(this.appState.getUserModel().isSortByLoc()){
				Collections.sort(this.appState.getCommentList(), ApplicationStateModel.locCompare);
			}
			//Sort by popularity(i.e. number of times favourited) and picture:
			else if(this.appState.getUserModel().isSortByPopularity()){
				appState.pictureSort(this.appState.getCommentList(), ApplicationStateModel.popularityCompare);
			}
			else{
				appState.pictureSort(this.appState.getCommentList(), null);
			}
		}
		//Sort without sorting by picture:
		else {
			//Sort by date
			if (this.appState.getUserModel().isSortByDate() == true) {
				Collections.sort(this.appState.getCommentList(), ApplicationStateModel.dateCompare);
			}
			//Sort by location:
			else if(this.appState.getUserModel().isSortByUserLoc() == true) {
				Collections.sort(this.appState.getCommentList(), ApplicationStateModel.locCompare);
			}
			else if(this.appState.getUserModel().isSortByLoc()){
				Collections.sort(this.appState.getCommentList(), ApplicationStateModel.locCompare);
			}
			//Sort by popularity(i.e. number of times favourited):
			else if(this.appState.getUserModel().isSortByPopularity()){
				Collections.sort(this.appState.getCommentList(), ApplicationStateModel.popularityCompare);
			}
		}
		this.appState.saveComments();
		this.appState.updateMainAdapter();
	}

	private void appStateSortChecker() {
		if (this.appState.getUserModel().isSortByPic() == true) {
			if (this.appState.getUserModel().isSortByDate() == true) {
			} else {
				if (this.appState.getUserModel().isSortByUserLoc() == true) {
					appState.setCmpLocation(locationListener
							.getLastBestLocation(), "Current Location");
				} else {
					if (this.appState.getUserModel().isSortByLoc()) {
						appState.setCmpLocation(appState.getUserModel()
								.getSortLoc().generateLocation(), appState.getUserModel().getSortLoc().getName());
					}
				}
			}
		} else {
			if (this.appState.getUserModel().isSortByDate() == true) {
			} else {
				if (this.appState.getUserModel().isSortByUserLoc() == true) {
					appState.setCmpLocation(locationListener
							.getLastBestLocation(), "Current Location");
				} else {
					if (this.appState.getUserModel().isSortByLoc()) {
						appState.setCmpLocation(appState.getUserModel()
								.getSortLoc().generateLocation(), appState.getUserModel().getSortLoc().getName());
					}
				}
			}
		}
	}
	

	/**
	 * In onResume the content view is set and the appState
	 * is told to reload and update the view, in case
	 * this was not done since any changes occurred.
	 */
	protected void onResume(){
		super.onResume();
		if(this.appState.isNetworkAvailable(this)){
		    while(true){
		        QueueModel result = this.appState.pushList();
		        if(result==null) break;
		    }
		    ElasticSearchOperations.searchForCommentModels("", this.appState.getCommentList(), this);
		}
		else{
		    appState.loadComments();
		}
		sortMainList();
	}	
	
	/**
	 * Sets functionality for buttons apparing on the actionbar
	 * 
	 * @param item selected on the actionbar
	 */
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
			case R.id.refresh_comments:
				onResume();
				return true;

			//Display the list of favourites specified in the user model
			//when implemented:
			case R.id.action_favourites_main:
				this.appState.setAssortList(appState.getUserModel().getFavourites());
				assortList.putExtra("title", "Favourites");
				this.startActivity(assortList);
				return true;
			//Display the list of want to read comments specified in the user model
			//when implemented:
			case R.id.action_want_to_read_main:
				this.appState.setAssortList(appState.getUserModel().getWantToReadComments());
				assortList.putExtra("title", "Want to Read");
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
	

	/**
	 * Sets functionality of checkbox
	 * 
	 * Adapted from the android developer page 
	 * http://developer.android.com/guide/topics/ui/controls/checkbox.html
	 * 
	 * @param view of checkbox being clicked
	 */
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
	 */
	public void onRadioButtonClicked(View view) {
		final RadioButton buttonPressed = (RadioButton) view;
		RadioGroup buttonGroup = (RadioGroup) buttonPressed.getParent();
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
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
	        		ElasticSearchLocationOperations.getLocationList(this);
	        		appState.saveLocations();
	        		appState.loadLocations();
	        		if(appState.getLocationList().isEmpty()){
	        			Toast.makeText(getBaseContext(),
	        					"No other locations available.",
	        					Toast.LENGTH_LONG).show();
						RadioButton button;
						//if/else statements that set the correct radio button
						//and check the sort by picture box if appropriate:
						if(this.appState.getUserModel().isSortByDate()){
							//Set the date radio button:
							button = (RadioButton) buttonGroup.getChildAt(0);
							button.toggle();
						}
						else if(this.appState.getUserModel().isSortByLoc()){
							//Set the location radio button:
							button = (RadioButton) buttonGroup.getChildAt(1);
							button.setText("Location: " + appState.getUserModel().getSortLoc().getName());
							button.toggle();
						}
						else if(this.appState.getUserModel().isSortByUserLoc()){
							//Set the Popularity radio button:
							button = (RadioButton) buttonGroup.getChildAt(2);
							button.toggle();
						}
						else if(this.appState.getUserModel().isSortByPopularity()){
							//Set the Popularity radio button:
							button = (RadioButton) buttonGroup.getChildAt(3);
							button.toggle();
						}
						else{
							buttonGroup.clearCheck();
						}
	        		}
	        		else{
	        			int i;

	        			// Sets/resets spinner set flag
	        			MainListViewActivity.this.spinnerFlag = 0;

	        			// Gets the xml custom dialog layout
	        			LayoutInflater li = LayoutInflater.from(this);
	        			View locationDialogView = li.inflate(R.layout.dialog_location, null);

	        			// Builds alert dialog
	        			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	        			alertDialogBuilder.setView(locationDialogView);

	        			//get location list from app state fixes spinner lag
	        			MainListViewActivity.this.locationList = appState.getLocationList();

	        			// Gets best known location
	        			//stopListeningLocation();

	        			// Create tempt list to sort
	        			MainListViewActivity.this.tempLocationList = MainListViewActivity.
	        					this.locationList;
	        			if(MainListViewActivity.this.tempLocationList == null)
	        				Log.e("the null value is:", "tempLocationList");
	        			// Sort list by proximity
	        			Collections.sort(MainListViewActivity.this.tempLocationList, ApplicationStateModel.locationModelCompare);
	        			// Loads up spinner with location names
	        			final Spinner spinner = (Spinner) locationDialogView
	        					.findViewById(R.id.location_dialog_spinner);
	        			// Creates and populates a list of the location names for displaying
	        			// in the spinner
	        			ArrayList<String> locationNameList = new ArrayList<String>();
	        			if (MainListViewActivity.this.tempLocationList.size() != 0) {
	        				for (i = 0; i < MainListViewActivity.this.tempLocationList.size(); i++)
	        					locationNameList.add(MainListViewActivity.this.
	        							tempLocationList.get(i).getName());
	        			} else
	        				locationNameList.add("No Locations");

	        			// Shows spinner
	        			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        					android.R.layout.simple_spinner_item, locationNameList);
	        			spinner.setAdapter(adapter);

	        			// Location dialog title
	        			alertDialogBuilder.setTitle("Set Location");

	        			// Location dialog set button functionality
	        			alertDialogBuilder.setPositiveButton("Set",
	        					new DialogInterface.OnClickListener() {

	        				@Override
	        				public void onClick(DialogInterface dialog, int which) {
	        					// Checks if no locations have been created and the user
	        					// is trying to set a location
	        					if (spinner.getSelectedItem().toString()
	        							.matches("No Locations"))
	        						Toast.makeText(getBaseContext(),
	        								"No other locations available.",
	        								Toast.LENGTH_LONG).show();
	        					else {
	        						appState.setCmpLocation(MainListViewActivity.
	        								this.tempLocationList.get(spinner.getSelectedItemPosition()).generateLocation(), 
	        								MainListViewActivity.this.tempLocationList.get(spinner.getSelectedItemPosition()).getName());
	        						MainListViewActivity.this.spinnerFlag = 1;
	        						appState.getUserModel().setSortByLoc(true);
	        						appState.getUserModel().setSortLoc(MainListViewActivity.this.tempLocationList.get(spinner.getSelectedItemPosition()));
	        						buttonPressed.setText("Location: "+appState.getUserModel().getSortLoc().getName());
	        						buttonPressed.toggle();
	        					}
	        				}
	        			});
	        			alertDialogBuilder.show();
	        		}
	        	}
	            else{
	            	this.appState.getUserModel().setSortByLoc(false);
	            }
	            break;
	        case R.id.userlocation:
	        	if(checked){
					appState.setCmpLocation(locationListener.getLastBestLocation(), "Current Location");
					this.appState.getUserModel().setSortLoc(new LocationModel(appState.getCmpLocation(), "Current Location"));
	        		this.appState.getUserModel().setSortByUserLoc(true);
	        		buttonPressed.toggle();
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
		LinearLayout optionsView = (LinearLayout)layoutInflater.inflate(R.layout.dialog_sort_by, 
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
			button.setText("Location: " + appState.getUserModel().getSortLoc().getName());
			button.toggle();
		}
		else if(this.appState.getUserModel().isSortByUserLoc()){
			//Set the Popularity radio button:
			button = (RadioButton) sortRadioGroup.getChildAt(2);
			button.toggle();
		}
		else if(this.appState.getUserModel().isSortByPopularity()){
			//Set the Popularity radio button:
			button = (RadioButton) sortRadioGroup.getChildAt(3);
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
