/**
 * Copyright 2014 Cole Fudge, Steven Giang, Bradley Poullet, David Yee, and Costa Zervos
 * @author dvyee, sgiang92

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SubCommentViewActivity extends Activity {

	private HeadModel headCommentData;
	private ListView subListView;
	private UserModel userData;
	private SubCommentViewActivityAdapter adapter;
	private ArrayList<SubCommentModel> subCommentsList = new ArrayList<SubCommentModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_comment_view);
		Bundle bundle = getIntent().getExtras();
		headCommentData = (HeadModel) bundle.getSerializable("HeadModel");
		userData = (UserModel) bundle.getSerializable("UserModel");
		

		// Disable the Home Icon on the Actionbar
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);

		// Set the Title in the Actionbar to the title of the head comment
		actionbar.setTitle(headCommentData.getTitle());

		subListView = (ListView) findViewById(R.id.sub_comment_list_view_sub);

		// Gets all the SubComments and all its subComments 
		AddCommentToList(headCommentData.getSubComments());

		adapter = new SubCommentViewActivityAdapter(this,
				R.layout.sub_comment_view_sub_comment_item, subCommentsList);

		// Set the first item in the list to the header Comment
		subListView.addHeaderView((View) SetHeader(headCommentData));
		
		subListView.setAdapter(adapter);



	}

	protected void onResume() {
		super.onResume();

		/*
		 * this.adapter = new SubCommentViewActivityAdapter(
		 * SubCommentViewActivity.this, R.layout.activity_sub_comment_view,
		 * this.headCommentData);
		 */
	}

	/**
	 * Inflate the menu.
	 * 
	 * @author dvyee, sgiang92
	 * @return boolean
	 * @param Menu
	 *            menu
	 * 
	 *            Code adapted from:
	 *            https://developer.android.com/training/basics
	 *            /actionbar/adding-buttons.html
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.subcommentview, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Capture the cases where the menu items are selected.
	 * 
	 * @author dvyee, sgiang92
	 * @return boolean
	 * @param MenuItem
	 *            item
	 * 
	 *            Code adapted from:
	 *            https://developer.android.com/training/basics
	 *            /actionbar/adding-buttons.html
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_reply:
			openReply();
			return true;
		case R.id.action_favourite:
			//Add the head comment to the users favourite list
			addFavourite(headCommentData);
			return true;
		case R.id.action_edit_username:
			//Bring up dialog box for the user to edit username
			editUserName();
			return true;
		case R.id.action_sort:
			//Bring up the dialog box for the user to sort comments by
			return true;
		case R.id.action_favourites:
			//Bring up the user's favourite list
			return true;
		case R.id.action_want_to_read:
			//Bring up the user's want to read list
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Sends an intent with the model information so that a reply can be made.
	 * Can be called via ActionBar or reply buttons in Head or Sub comments.
	 * 
	 * @author dvyee, sgiang92
	 * @return
	 * @param
	 */
	private void openReply() {
		// TODO: Implement openReply
		// send an intent to CreateCommentActivity
		// send info:
		// - title
	}



	/**
	 * Adds the post and sub-posts to user's favourites. Post will get cached
	 * for later review. Can only be called via ActionBar.
	 * 
	 * @author dvyee, sgiang92
	 * @return
	 * @param
	 */
	private void addFavourite(CommentModel comment) {
		// TODO: Implement addFavourite
		userData.getFavourites().add(comment);
	}
	
	private void editUserName(){
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
				
				userData.setUsername(usernameString);
		
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
	 * Sets the items in the head comment item layout to the contents of the
	 * head comment.
	 * 
	 * @author sgiang92, dvyee
	 * @param headComment
	 * @return HeadModel headComment
	 */
	private View SetHeader(HeadModel headComment) {

		// Get the head comment item layout view
		View header = (View) getLayoutInflater().inflate(
				R.layout.sub_comment_view_head_comment_item, null);

		// assign the proper layout item so it can be set
		TextView textTitle = (TextView) header.findViewById(R.id.long_title);
		TextView textAuthor = (TextView) header
				.findViewById(R.id.head_comment_author);
		// TextView textLocation = (TextView) header
		// .findViewById(R.id.head_comment_location_sub);
		TextView textTime = (TextView) header
				.findViewById(R.id.head_comment_time_sub);
		TextView textContent = (TextView) header
				.findViewById(R.id.head_comment_text_body_sub);

		// Set the items to the contents of the Head Comment
		textTitle.setText(headComment.getTitle());
		textAuthor.setText(headComment.getAuthor());
		// textLocation.setText(headComment.getLocation().getName());
		textTime.setText(TimeToString(headComment.getTimestamp()));
		textContent.setText(headComment.getContent());

		return header;

	}

	/**
	 * Takes in the timestamp as a Calendar object and converts it to a string
	 * that can be used in a textView.
	 * 
	 * @param calendar
	 *            object to retrieve string from
	 * @return string of the formatted date of the timestamp
	 */
	private String TimeToString(Calendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:00 aa",
				java.util.Locale.getDefault());
		String timeString = sdf.format(calendar.getTime());
		return timeString;
	}

	/***
	 * Takes in a subComment array list to be added to the adapter to be shown
	 * in the listView. Uses recursion to get all the subComment's subcomments
	 * 
	 * @author sgiang92
	 * @param subCommentList
	 */
	private void AddCommentToList(ArrayList<SubCommentModel> subCommentList) {
		if (subCommentList.size() == 0) {
			return;
		} else {
			for (SubCommentModel subComment : subCommentList) {
				this.subCommentsList.add(subComment);
				if (subComment.getSubComments().size() > 0) {
					AddCommentToList(subComment.getSubComments());
				}
			}
		}
	}
	
	

}
