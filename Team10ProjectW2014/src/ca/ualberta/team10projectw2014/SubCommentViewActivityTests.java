package ca.ualberta.team10projectw2014;

import java.text.SimpleDateFormat;
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
		CommentModel headComment = new CommentModel();
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
		CommentModel headComment = new CommentModel();
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
		TextView textTime = (TextView) headItem.findViewById(R.id.head_comment_time_sub);
		TextView textContent = (TextView) headItem.findViewById(R.id.head_comment_text_body_sub);
		
		assertEquals("Title should be shown on layout", headComment.getTitle(),textTitle.getText());
		assertEquals("Author should be shown on layout",headComment.getAuthor(),textAuthor.getText());
		assertEquals("Content should be shown on layout", headComment.getContent(),textContent.getText());
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:00 aa",java.util.Locale.getDefault());
		String timeString = sdf.format(headComment.getTimestamp().getTime());
		
		assertEquals("Time should be shown on layout",timeString,textTime.getText());

		// close the activity now that we're done the JUnit test
		activity.finish();

		// force the next getActivity to re-open the activity
		setActivity(null);
	}
	
	
	public void testShowSubComments() throws Throwable {
		
		// setup the head comment with some data
		CommentModel headComment = new CommentModel();
		headComment.setTitle("Test Head Comment Title");
		headComment.setAuthor("TestUsername");
		headComment.setContent("Test Head Comment Content");
		headComment.setTimestamp(Calendar.getInstance());
		
		
		//setup sub comments with data
		
		//responds to head comment
		SubCommentModel subComment1 = new SubCommentModel(headComment);
		subComment1.setAuthor("TestUser1");
		subComment1.setTitle("TestSubTitle1");
		subComment1.setContent("Reply to head comment1");
		subComment1.setTimestamp(Calendar.getInstance());
		headComment.addSubComment(subComment1);
		
		//responds to head comment
		SubCommentModel subComment2 = new SubCommentModel(headComment);
		subComment2.setAuthor("TestUser2");
		subComment2.setTitle("TestSubTitle2");
		subComment2.setContent("Reply to head comment2");
		subComment2.setTimestamp(Calendar.getInstance());
		headComment.addSubComment(subComment2);
		
		//responds to subComment1
		SubCommentModel subComment3 = new SubCommentModel(subComment1);
		subComment3.setAuthor("TestUser1");
		subComment3.setTitle("TestSubTitle1");
		subComment3.setContent("Reply to head comment1");
		subComment3.setTimestamp(Calendar.getInstance());
		subComment1.getSubComments().add(subComment3);
		
		//responds to head comment
		SubCommentModel subComment4 = new SubCommentModel(headComment);
		subComment4.setAuthor("TestUser1");
		subComment4.setTitle("TestSubTitle1");
		subComment4.setContent("Reply to head comment1");
		subComment4.setTimestamp(Calendar.getInstance());
		headComment.addSubComment(subComment4);
		


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
		


		
		
		// close the activity now that we're done the JUnit test
		activity.finish();

		// force the next getActivity to re-open the activity
		setActivity(null);
	}






}
