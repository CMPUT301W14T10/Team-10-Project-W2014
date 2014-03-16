package ca.ualberta.team10projectw2014;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MainListViewActivityTests extends
		ActivityInstrumentationTestCase2<MainListViewActivity> {
	
	Activity activity;
	ListView headListView;
	ApplicationStateModel appState;
	ArrayList<CommentModel> backupList = new ArrayList<CommentModel>();
	
	public MainListViewActivityTests() {
		super(MainListViewActivity.class);
		appState = ApplicationStateModel.getInstance();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	/**
	 * Test to confirm head comment list displays a comment correctly.
	 * 
	 * TODO implement getIntent in MainListViewActivity for this test to pass
	 * 
	 * @throws Throwable
	 */
	public void testDisplayCommentInList() throws Throwable {
		// Create test comment List
		ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
		// Create a test head comment
		CommentModel headComment = new CommentModel();
		headComment.setTitle("Test Title");
		headComment.setAuthor("TestUsername");
		headComment.setContent("Test Head Comment Content");
		headComment.setTimestamp(Calendar.getInstance());
		commentList.add(headComment);
		
		// Set comment list to something so that it is not null for the load
		appState.setCommentList(backupList);
		// Sets file path for comment list file
		appState.setFileContext(getInstrumentation().getContext());
		appState.loadComments();
		// Backup actual comment list
		backupList = appState.getCommentList();
		
		// Sets test comment list
		appState.setCommentList(commentList);
		// Saves comments
		appState.saveComments();
		
        // Launches MainListviewActivity
        activity = getActivity();     
        
        // Gets the head comment list
        headListView = (ListView) activity.findViewById(R.id.HeadCommentList);

        // Gets layout of head comment list item
        View commentLayout = (View) activity.findViewById(R.id.head_comment_list_item_layout);
        //View commentLayout = (View) headListView.findViewById(R.id.head_comment_list_item_layout);
        
        // Identifies list item objects required for the test
        TextView title = (TextView) commentLayout.findViewById(R.id.head_comment_title);
        TextView userName = (TextView) commentLayout.findViewById(R.id.head_comment_username);
        // TODO re-add in location when location implemented
        //TextView location = (TextView) commentLayout.findViewById(R.id.head_comment_location);
        TextView date = (TextView) commentLayout.findViewById(R.id.head_comment_time);
        
        // Test comment title is correct
        assertEquals("Head comment title should appear in list", headComment.getTitle(), title.getText());
        // Test comment username is correct
        assertEquals("Head comment username should appear in list", headComment.getAuthor(), userName.getText());
        // Test comment location is correct
        // TODO re-add in location when location implemented
        //assertEquals("Head comment location should appear in list", headComment.getLocation(), location.getText());
        
        // Converts headcomment's timestamp calendar object to a testable string
        SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:mm aa");
        String timeString = sdf.format(headComment.getTimestamp().getTime());
        
        // Test comment date is correct
        assertEquals("Head comment date should appear in list", timeString, date.getText());
        
        // Restores actual comment list
        appState.setCommentList(backupList);
        appState.saveComments();
        // Close activity
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
