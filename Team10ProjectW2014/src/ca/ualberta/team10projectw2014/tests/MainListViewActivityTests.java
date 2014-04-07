package ca.ualberta.team10projectw2014.tests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;
import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.controllersAndViews.MainListViewActivity;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationListenerModel;
import ca.ualberta.team10projectw2014.models.LocationModel;

/**
 * This class tests various aspects of the MainListViewActivity
 * @author       Costa Zervos <czervos@ualberta.ca>
 * @version      1            (current version number of program)
 */
public class MainListViewActivityTests extends
		ActivityInstrumentationTestCase2<MainListViewActivity> {
	
	Activity activity;
	/**
	 * @uml.property  name="appState"
	 * @uml.associationEnd  
	 */
	ApplicationStateModel appState; // Application singleton
	ArrayList<CommentModel> backupList = new ArrayList<CommentModel>(); // Backs up original comment list
	
	/**
	 * Initializes test suite on MainListViewActivity
	 */
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
	 * @throws Throwable
	 */
	@SuppressLint("SimpleDateFormat")
	public void testDisplayCommentInList() throws Throwable {
		// Create test comment List
				ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
				// Creates two test head comments
				CommentModel headCommentOld = new CommentModel();
				headCommentOld.setTitle("Old Test Title");
				headCommentOld.setAuthor("TestUsername");
				headCommentOld.setContent("Old Test Head Comment Content");
				headCommentOld.setTimestamp(Calendar.getInstance());
				
				// Create test location model
				LocationModel tstLocationModel = new LocationModel("Lat: 100 Long: 100", 100, 100);
				headCommentOld.setLocation(tstLocationModel);
				
				// Add head comment to test list
				commentList.add(headCommentOld);

				CommentModel headCommentNew = new CommentModel();
				headCommentNew.setTitle("New Test Title");
				headCommentNew.setAuthor("TestUsername");
				headCommentNew.setContent("New Test Head Comment Content");
				headCommentNew.setTimestamp(Calendar.getInstance());
				headCommentNew.setLocation(tstLocationModel);
				commentList.add(headCommentNew);
				
				// Set comment list to something so that it is not null for the load
				appState.setCommentList(backupList);
				// Sets file path for comment list file
				appState.setFileContext(getInstrumentation().getContext());
				appState.loadComments();
				// Backup actual comment list
				backupList = appState.getCommentList();
				
				// Sets test comment list
				appState.setCommentList(commentList);
				// Sorts list by date
				Collections.sort(commentList, ApplicationStateModel.dateCompare);
				// Saves comments
				appState.saveComments();
				
		        // Launches MainListviewActivity
		        activity = getActivity();     

		        // Gets layout of head comment list item
		        View commentLayout = (View) activity.findViewById(R.id.head_comment_list_item_layout);
		        
		        fail();
		        // Identifies list item objects required for the test
		        TextView title = (TextView) commentLayout.findViewById(R.id.head_comment_title);
		        TextView userName = (TextView) commentLayout.findViewById(R.id.head_comment_username);
		        TextView location = (TextView) commentLayout.findViewById(R.id.head_comment_location);
		        TextView date = (TextView) commentLayout.findViewById(R.id.head_comment_time);
		        
		        // Test new comment title is correct
		        assertEquals("Head comment title should appear in list", headCommentNew.getTitle(), title.getText());
		        // Test new comment username is correct
		        assertEquals("Head comment username should appear in list", headCommentNew.getAuthor(), userName.getText());
		        // Test new comment location is correct
		        assertEquals("Head comment location should appear in list", headCommentNew.getLocation().getName(), location.getText());
		        
		        // Converts new headcomment's timestamp calendar object to a testable string
		        SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:mm aa");
		        String timeString = sdf.format(headCommentNew.getTimestamp().getTime());
		        
		        // Test new comment date is correct
		        assertEquals("Head comment date should appear in list", timeString, date.getText());
		        
		        // Restores actual comment list
		        appState.setCommentList(backupList);
		        appState.saveComments();
		        // Close activity
		        activity.finish();
		        setActivity(null);
	}
}
