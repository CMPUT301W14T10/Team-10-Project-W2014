package ca.ualberta.team10projectw2014;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

public class MainListViewActivityTests extends
		ActivityInstrumentationTestCase2<MainListViewActivity> {
	
	Activity activity;
	ListView headListView;
	
	public MainListViewActivityTests() {
		super(MainListViewActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	/**
	 * Test to confirm head comment list displays a comment correctly
	 * 
	 * @throws Throwable
	 */
	public void displayCommentInList() throws Throwable {
		// Create a test head comment
		HeadModel headComment = new HeadModel();
		headComment.setTitle("Test Title");
		headComment.setAuthor("TestUsername");
		headComment.setContent("Test Head Comment Content");
		headComment.setTimestamp(Calendar.getInstance());
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName("ca.ualberta.team10projectw2014", 
        		"ca.ualberta.team10projectw2014.MainListViewActivity");
        intent.putExtra("HeadComment", headComment);
        setActivityIntent(intent);
        
        activity = getActivity();
        
        headListView = (ListView) activity.findViewById(ca.ualberta.team10projectw2014.R.id.HeadCommentList);
        // TODO assert equal title matches textview containing title
        // TODO assert equal title matches textview containing username
        // TODO assert equal title matches textview containing location
        // TODO assert equal title matches textview containing date
        
        activity.finish();
        
        setActivity(null);
	}
	
	
	/**
	 * TODO when sorting implemented.
	 * Test Comments are sorted:
	 * - Create head comments with specific test locations
	 * - Add comments to a list called testList
	 * - Sort list using MainListViewActivty sort method
	 * - Assert comments in correct location
	 * 
	 */

}
