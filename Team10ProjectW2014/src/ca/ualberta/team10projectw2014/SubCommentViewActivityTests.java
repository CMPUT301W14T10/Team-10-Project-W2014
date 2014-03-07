package ca.ualberta.team10projectw2014;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SubCommentViewActivityTests extends
		ActivityInstrumentationTestCase2<SubCommentViewActivity> {

	// Instrumentation instrumentation;
	Activity activity;
	ListView subListView;

	public SubCommentViewActivityTests() {
		super(SubCommentViewActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		// must call super setUp before tests
		super.setUp();

	}

	/**
	 * This test checks that an arbitrarily long title gets shortened in the
	 * ActionBar of the Activity.
	 * 
	 * Author Attribution: Adapted from code available on StackOverflow:
	 * http://stackoverflow.com/q/5708630
	 * 
	 * @author dvyee
	 * @throws Throwable
	 */

	public void testShowTitle() throws Throwable {
		// setup the head comment with some data
		HeadModel headComment = new HeadModel();
		headComment.setTitle("Test Head Comment Title");
		headComment.setAuthor("TestUsername");
		headComment.setContent("Test Head Comment Content");
		headComment.setTimestamp(Calendar.getInstance());

		// create a new intent
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClassName("ca.ualberta.team10projectw2014",
				"ca.ualberta.team10projectw2014.SubCommentViewActivity");
		intent.putExtra("HeadModel", headComment);
		setActivityIntent(intent);

		// get the activity and call it when necessary (start the activity)
		// keep a record of the activity
		// we have to start the activity AFTER our intent is ready and prepared
		// do not start the activity BEFORE or NullPointerException will occur!
		activity = getActivity();

		// get the sub list view object from its defined R.id
		subListView = (ListView) activity
				.findViewById(ca.ualberta.team10projectw2014.R.id.sub_comment_list_view_sub);

		ActionBar actBar = activity.getActionBar();
		assertEquals(
				"Actionbar Title should contain the Title of the head comment",
				headComment.getTitle(), actBar.getTitle());

		// close the activity now that we're done the JUnit test
		activity.finish();

		// force the next getActivity to re-open the activity
		setActivity(null);
	}
	
	/**
	 * This test checks that the Head comment is shown in the sub comment
	 * list view activity.
	 * 
	 * @author sgiang92
	 * @throws Throwable
	 */
	public void testShowHeadComment() throws Throwable {
		// setup the head comment with some data
		HeadModel headComment = new HeadModel();
		headComment.setTitle("Test Head Comment Title");
		headComment.setAuthor("TestUsername");
		headComment.setContent("Test Head Comment Content");
		headComment.setTimestamp(Calendar.getInstance());

		// create a new intent
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClassName("ca.ualberta.team10projectw2014",
				"ca.ualberta.team10projectw2014.SubCommentViewActivity");
		intent.putExtra("HeadModel", headComment);
		setActivityIntent(intent);

		// get the activity and call it when necessary (start the activity)
		// keep a record of the activity
		// we have to start the activity AFTER our intent is ready and prepared
		// do not start the activity BEFORE or NullPointerException will occur!
		activity = getActivity();

		
		// get the sub list view object from its defined R.id
		subListView = (ListView) activity
				.findViewById(ca.ualberta.team10projectw2014.R.id.sub_comment_list_view_sub);
		
		//Get the layout from the activity
		View headItem = (View) activity.findViewById(ca.ualberta.team10projectw2014.R.id.head_comment_item_layout);

		//Find each element of the layout.
		TextView textTitle = (TextView) headItem.findViewById(R.id.long_title);
		TextView textAuthor = (TextView) headItem.findViewById(R.id.head_comment_author);
		//TextView textLocaTime = (TextView) header.findViewById(R.id.head_comment_location_time);
		TextView textContent = (TextView) headItem.findViewById(R.id.head_comment_text_body);
		
		assertEquals("Title should be shown on layout", headComment.getTitle(),textTitle.getText());
		assertEquals("Author should be shown on layout",headComment.getAuthor(),textAuthor.getText());
		assertEquals("Content should be shown on layout", headComment.getContent(),textContent.getText());

		// close the activity now that we're done the JUnit test
		activity.finish();

		// force the next getActivity to re-open the activity
		setActivity(null);
	}



	/***
	 * Use Case 5.1 - BrowseSubComments - Create a head comment - Create a sub
	 * comment that refers to this head comment - Add these comments to a list
	 * called testList - Use mainListViewActivityAdapter's "createHeadComment()"
	 * method to create the head comment in the system - Use
	 * subCommentViewActivityAdapter's "createSubComment()" method to create the
	 * sub comment for the head comment - Assert that the subComment is on the
	 * screen beneath the head comment.
	 */

}
