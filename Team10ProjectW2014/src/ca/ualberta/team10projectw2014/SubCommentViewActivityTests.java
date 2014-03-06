package ca.ualberta.team10projectw2014;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

public class SubCommentViewActivityTests extends
	ActivityInstrumentationTestCase2<SubCommentViewActivity> {
	
	//Instrumentation instrumentation;
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
	 * ActionBar of the Activity. The title is expected to get truncated as 
	 * indicated by an ellipses (triple dots or "...") at the end of the 
	 * title.
	 * 
	 * Author Attribution:
	 * Adapted from code available on StackOverflow:
	 * http://stackoverflow.com/q/5708630
	 * 
	 * @author dvyee
	 * @throws Throwable
	 */
	public void testShowCondensedTitle() throws Throwable {
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
		subListView = 
			(ListView) activity.findViewById(
					ca.ualberta.team10projectw2014.R.id.sub_comment_list_view_sub);
				
		ActionBar actBar = activity.getActionBar();
		assertEquals("Actionbar Title should contain ... ","Test Hea...",actBar.getTitle());
		
		// close the activity now that we're done the JUnit test
		activity.finish();
		
		// force the next getActivity to re-open the activity
		setActivity(null);
	}
	
	
	
	/***
	 * Use Case 5.1 - BrowseSubComments 
	 * - Create a head comment 
	 * - Create a sub comment that refers to this head comment 
	 * - Add these comments to a list called testList 
	 * - Use mainListViewActivityAdapter's "createHeadComment()"
	 *   method to create the head comment in the system 
	 * - Use subCommentViewActivityAdapter's "createSubComment()" 
	 *   method to create the sub comment for the head comment 
	 * - Assert that the subComment is on the screen beneath the 
	 *   head comment.
	 */

	


}
