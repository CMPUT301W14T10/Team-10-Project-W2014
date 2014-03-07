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
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class SubCommentViewActivity extends Activity {

	private HeadModel headCommentData;
	private ListView subListView;
	private SubCommentViewActivityAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_comment_view);
		Bundle bundle = getIntent().getExtras();
		headCommentData = (HeadModel) bundle.getSerializable("HeadModel");


		// Disable the Home Icon on the Actionbar
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		
		// Set the Title in the Actionbar to the title of the head comment
		actionbar.setTitle(headCommentData.getTitle());
		
		
		subListView = (ListView) findViewById(R.id.sub_comment_list_view_sub);
		
		//Set the first item in the list to the header Comment
		subListView.addHeaderView((View)SetHeader(headCommentData));
		subListView.setAdapter(adapter);
	}
	
	protected void onResume() {
		super.onResume();
		
		/*
		this.adapter = new SubCommentViewActivityAdapter(
				SubCommentViewActivity.this,
				R.layout.activity_sub_comment_view,
				this.headCommentData);
				*/
	}

	/**
	 * Inflate the menu.
	 * @author dvyee, sgiang92
	 * @return boolean
	 * @param Menu menu
	 * 
	 * Code adapted from:
	 * https://developer.android.com/training/basics/actionbar/adding-buttons.html
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
	 * @author dvyee, sgiang92
	 * @return boolean
	 * @param MenuItem item
	 * 
	 * Code adapted from:
	 * https://developer.android.com/training/basics/actionbar/adding-buttons.html
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_reply:
	            openReply();
	            return true;
	        case R.id.action_favourite:
	        	addFavourite();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Sends an intent with the model information so that a reply can be made.
	 * Can be called via ActionBar or reply buttons in Head or Sub comments.
	 * @author dvyee, sgiang92
	 * @return
	 * @param
	 */
	private void openReply(){
		// TODO: Implement openReply
		// send an intent to CreateCommentActivity
		// send info:
		// - title
	}
	
	/**
	 * Opens the drop-down settings for posting.
	 * Can only be called via ActionBar.
	 * @author dvyee, sgiang92
	 * @return
	 * @param
	 */
	private void openSettings(){
		// TODO: Implement openReply
	}

	/**
	 * Adds the post and sub-posts to user's favourites.
	 * Post will get cached for later review.
	 * Can only be called via ActionBar.
	 * @author dvyee, sgiang92
	 * @return
	 * @param
	 */
	private void addFavourite(){
		// TODO: Implement addFavourite
	}
	
	/**
	 * Sets the items in the head comment item layout to the contents
	 * of the head comment.
	 * @author sgiang92, dvyee
	 * @param headComment
	 * @return HeadModel headComment
	 */
	private View SetHeader(HeadModel headComment){
		
		//Get the head comment item layout view
		View header = (View) getLayoutInflater().inflate(R.layout.sub_comment_view_head_comment_item, null);
		
		//assign the proper layout item so it can be set
		TextView textTitle = (TextView) header.findViewById(R.id.long_title);
		TextView textAuthor = (TextView) header.findViewById(R.id.head_comment_author);
		TextView textLocaTime = (TextView) header.findViewById(R.id.head_comment_location_time);
		TextView textContent = (TextView) header.findViewById(R.id.head_comment_text_body);
		
		//Set the items to the contents of the Head Comment
		textTitle.setText(headComment.getTitle());
		textAuthor.setText(headComment.getAuthor());
		textLocaTime.setText(getLocaTimeString(headComment));
		textContent.setText(headComment.getContent());
	
		return header;
		
	}
	
	/**
	 * Gets the location and time from the current sub Comment and appends them
	 * together to be displayed in subCommentListView Activity
	 * @author sgiang92, dvyee
	 * @param subCommentModel object to get the title of the comment the current
	 * 		  comment is replying to.
	 * @return a string that has the location and time appending together
	 */
	private String getLocaTimeString(HeadModel headComment){
		String Location = headComment.getLocation().getName();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:00 aa",java.util.Locale.getDefault());
		String timeString = sdf.format(headComment.getTimestamp().getTime());
		
		return Location + " - " + timeString;
	}

}
