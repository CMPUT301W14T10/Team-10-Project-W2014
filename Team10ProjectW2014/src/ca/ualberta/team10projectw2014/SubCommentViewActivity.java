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

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SubCommentViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_comment_view);
		
		//Disable the Home Icon and the title on the Actionbar
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setTitle("1");
		
		
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

}
