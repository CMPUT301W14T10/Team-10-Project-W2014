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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class SubCommentViewActivity extends Activity {
	private ListView subListView;
	private ApplicationStateModel appState;
	private ArrayList<CommentModel> commentList;
	private ActionBar actionbar;
	private Bundle bundle;
	private View headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_comment_view);
		
		appState = ApplicationStateModel.getInstance();
		appState.setFileContext(this);

		subListView = (ListView) findViewById(R.id.sub_comment_list_view_sub);
		
		headerView = (View) SetHeader(appState.getSubCommentViewHead());
		// Set the first item in the list to the header Comment
		subListView.addHeaderView(headerView);
		

	}

	@Override
	protected void onResume() {
		super.onResume();

		appState.setFileContext(this);
		subListView.removeHeaderView(headerView);
		headerView = (View) SetHeader(appState.getSubCommentViewHead());
		subListView.addHeaderView(headerView);

		// Disable the Home Icon on the Actionbar
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		
		// Set the Title in the Actionbar to the title of the head comment
		actionbar.setTitle(appState.getSubCommentViewHead().getTitle());

		// Gets all the SubComments and all its subComments
		commentList = new ArrayList<CommentModel>();
		AddCommentToList(appState.getSubCommentViewHead().getSubComments());

		appState.setSCVAdapter(new SubCommentViewActivityAdapter(this,
				R.layout.sub_comment_view_sub_comment_item, commentList,
				appState.getUserModel()));

		/*
		 * } else if(bundle.containsKey("favourite")) { // Set the Title in the
		 * Actionbar to the title to favourites
		 * actionbar.setTitle("Favourites");
		 * 
		 * appState.setSCVAdapter(new SubCommentViewActivityAdapter(this,
		 * R.layout.sub_comment_view_sub_comment_item,
		 * appState.getUserModel().getFavourites(), appState.getUserModel()));
		 * 
		 * 
		 * 
		 * 
		 * } else if(bundle.containsKey("wantToRead")) {
		 * 
		 * // Set the Title in the Actionbar to the title to Want to read
		 * actionbar.setTitle("Want to Read");
		 * 
		 * appState.setSCVAdapter(new SubCommentViewActivityAdapter(this,
		 * R.layout.sub_comment_view_sub_comment_item,
		 * appState.getUserModel().getWantToReadComments(),
		 * appState.getUserModel())); }
		 */

		subListView.setAdapter(appState.getSCVAdapter());

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
			// Add the head comment to the users favourite list
			addFavourite(appState.getSubCommentViewHead());
			return true;
		case R.id.action_edit_username:
			// Bring up dialog box for the user to edit username
			editUserName();
			return true;
		case R.id.action_sort:
			// Bring up the dialog box for the user to sort comments by
			SortComments();
			return true;
		case R.id.action_favourites:
			// Bring up the user's favourite list
			return true;
		case R.id.action_want_to_read:
			// Bring up the user's want to read list
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
		// send an intent to CreateCommentActivity
		// send info:
		// - title
		Intent createComment = new Intent(getApplicationContext(),
				CreateCommentActivity.class);
		appState.setCreateCommentParent(appState.getSubCommentViewHead());
		// createComment.putExtra("comment",headCommentData);
		createComment.putExtra("username", appState.getUserModel()
				.getUsername());
		this.startActivity(createComment);

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
		appState.getUserModel().getFavourites().add(comment);
	}

	/***
	 * Will bring up the dialog box for the user to edit username
	 */
	private void editUserName() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		// set the fields of the dialog:
		alert.setTitle("Edit Username");

		// add an EditText to the dialog for the user to enter
		// the name in:
		final EditText usernameText = new EditText(this);
		alert.setView(usernameText);

		// set the positive button with its text and set up an on click listener
		// to add the counter with the text provided when it is pressed:
		alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// get the text from the editable:
				Editable usernameEditable = usernameText.getText();
				String usernameString = usernameEditable.toString();

				appState.getUserModel().setUsername(usernameString);

			}
		});

		// also set a cancel negative button that does nothing but close the
		// dialog window:
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
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
	private View SetHeader(CommentModel headComment) {

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
		Button moreButton = (Button) header.findViewById(R.id.head_more_option);
		ImageView imageView = (ImageView) header
				.findViewById(R.id.head_comment_image);

		// Set the items to the contents of the Head Comment
		textTitle.setText(headComment.getTitle());
		textAuthor.setText(headComment.getAuthor());
		// textLocation.setText(headComment.getLocation().getName());
		textTime.setText(TimeToString(headComment.getTimestamp()));
		textContent.setText(headComment.getContent());

		// Sets the image attached to the comment
		if (headComment.getPhotoPath() != null) {

			String imagePath = headComment.getPhotoPath();
			// Get the dimensions of the View
			// int targetW = holder.imageView.getWidth();
			// int targetH = holder.imageView.getHeight();

			// Get the dimensions of the bitmap
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imagePath, bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;

			// Determine how much to scale down the image
			int scaleFactor = Math.min(photoW / 50, photoH / 50);

			// Decode the image file into a Bitmap sized to fill the View
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
			imageView.setImageBitmap(bitmap);

		}

		moreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				openMoreDialog(appState.getSubCommentViewHead());

			}
		});

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
				commentList.add(subComment);
				if (subComment.getSubComments().size() > 0) {
					AddCommentToList(subComment.getSubComments());
				}
			}
		}
	}

	/***
	 * Open the More... dialog box for the user to add the comment to a read
	 * later list, or if the user is the author of that comment, they can edit
	 * the comment.
	 * 
	 * @param comment
	 */
	public void openMoreDialog(CommentModel comment) {

		final CommentModel commentData = comment;
		AlertDialog.Builder moreDialog = new AlertDialog.Builder(this);

		// set dialog title
		moreDialog.setTitle("More...");

		// set Add to Read later
		moreDialog.setNeutralButton("Add to Read Later",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						appState.getUserModel().getFavourites()
								.add(commentData);

					}
				});

		final Context editCommentContext = this;
		// User will only have the option if the
		if (appState.getUserModel().getAndroidID()
				.equals(comment.getAuthorAndroidID())) {
			moreDialog.setNeutralButton("Edit Comment",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							// Open CreateComment Activity
							appState.setCommentToEdit(commentData);
							Intent editComment = new Intent(getApplicationContext(), EditCommentActivity.class);
							//subCommentView.putExtra("comment", (Object) headComment);
							editCommentContext.startActivity(editComment);
						}
					});
		}

		moreDialog.setNegativeButton("Cancel", null);

		moreDialog.show();

	}

	/**
	 * Brings up a dialog box to prompt user for sorting criteria:
	 * 
	 * @see sort_comments() in MainListViewActivity
	 * 
	 */
	private void SortComments() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		// set the fields of the dialog:
		alert.setTitle("Sort By:");

		LinearLayout optionsView = (LinearLayout) layoutInflater.inflate(
				R.layout.sort_by_dialog_list, null);

		ViewGroup sortRadioGroup = (ViewGroup) optionsView.getChildAt(0);

		RadioButton button;

		if (appState.getUserModel().isSortByDate()) {
			button = (RadioButton) sortRadioGroup.getChildAt(0);
			button.toggle();
		} else if (appState.getUserModel().isSortByLoc()) {
			button = (RadioButton) sortRadioGroup.getChildAt(1);
			button.toggle();
		} else if (appState.getUserModel().isSortByPopularity()) {
			button = (RadioButton) sortRadioGroup.getChildAt(2);
			button.toggle();
		}

		if (appState.getUserModel().isSortByPic()) {
			button = (RadioButton) ((ViewGroup) optionsView.getChildAt(2))
					.getChildAt(0);
			button.toggle();
		}

		alert.setView(optionsView);

		// set the positive button with its text and set up an on click listener
		// to add the counter with the text provided when it is pressed:
		alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				// userController.saveInFile();
			}
		});

		// also set a cancel negative button that does nothing but close the
		// dialog window:
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
	}

}
