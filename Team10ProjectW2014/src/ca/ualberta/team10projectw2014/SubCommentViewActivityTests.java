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
	
	Instrumentation instrumentation;
	Activity activity;
	ListView subListView;
	
	

	public SubCommentViewActivityTests() {
		super(SubCommentViewActivity.class);
	}
	
	protected void setUp() throws Exception {
        super.setUp();
		activity = getActivity();
		instrumentation = getInstrumentation();
		subListView = (ListView) activity
				.findViewById(ca.ualberta.team10projectw2014.R.id.sub_comment_list_view_sub);

	}


	public void testShowTitle() throws Throwable {
		
		Activity act = getActivity();
		Intent intent = new Intent(act,SubCommentViewActivity.class);
		HeadModel headComment = new HeadModel();
		headComment.setTitle("Test Head Comment Title");
		headComment.setAuthor("TestUsername");
		headComment.setContent("Test Head Comment Content");
		headComment.setTimestamp(Calendar.getInstance());

		
		intent.putExtra("HeadModel", headComment);
		
		act.startActivity(intent);
		
		runTestOnUiThread(new Runnable(){
			
			@Override
			public void run(){
				//SubCommentViewActivity activity = getActivity();
				
				//ActionBar actBar = activity.getActionBar();
				//assertEquals("Actionbar Title should contain ... ","Test Hea...",actBar.getTitle());
				
				
				
			}
		});
	
		
		
	
		
	}
	
	/*
	public void showHeadComment() throws Throwable {
			setUp();
			HeadModel headComment = new HeadModel();
			headComment.setTitle("Test Head Comment Title");
			headComment.setAuthor("TestUsername");
			headComment.setContent("Test Head Comment Content");
			headComment.setTimestamp(Calendar.getInstance());
			
			
		
		
	} */
	
	
	
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
