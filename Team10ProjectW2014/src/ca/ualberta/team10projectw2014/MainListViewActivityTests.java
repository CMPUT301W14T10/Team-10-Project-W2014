package ca.ualberta.team10projectw2014;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

public class MainListViewActivityTests extends
		ActivityInstrumentationTestCase2<MainListViewActivity> {
	
	Activity activity;
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
		
		// Create test location model
		LocationModel locMod = new LocationModel("testlocationmodel", 100, 100);
		headComment.setLocation(locMod);
		
		// Add head comment to the test comment list
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
        
        // Gets layout of head comment list item
        View commentLayout = (View) activity.findViewById(R.id.head_comment_list_item_layout);
        
        // Identifies list item objects required for the test
        TextView title = (TextView) commentLayout.findViewById(R.id.head_comment_title);
        TextView userName = (TextView) commentLayout.findViewById(R.id.head_comment_username);
        // TODO re-add in location when location implemented
        //TextView location = (TextView) commentLayout.findViewById(R.id.head_comment_location);
        TextView date = (TextView) commentLayout.findViewById(R.id.head_comment_time);
        //TextView location = (TextView) commentLayout.findViewById(R.id.head_comment_location)
        
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
	 * Test to confirm head comment list sorts by date correctly.
	 * 
	 * @throws Throwable
	 */
	public void testSortCommentsByDate() throws Throwable {
		// Create test comment List
		ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
		// Creates two test head comments
		CommentModel headCommentOld = new CommentModel();
		headCommentOld.setTitle("Old Test Title");
		headCommentOld.setAuthor("TestUsername");
		headCommentOld.setContent("Old Test Head Comment Content");
		headCommentOld.setTimestamp(Calendar.getInstance());
		
		// Create test location model
		LocationModel locMod = new LocationModel("testlocationmodel", 100, 100);
		headCommentOld.setLocation(locMod);
		// Add head comment to test list
		commentList.add(headCommentOld);
		// Sleep one second to ensure second comment is created later
		TimeUnit.SECONDS.sleep(1);
		CommentModel headCommentNew = new CommentModel();
		headCommentNew.setTitle("New Test Title");
		headCommentNew.setAuthor("TestUsername");
		headCommentNew.setContent("New Test Head Comment Content");
		headCommentNew.setTimestamp(Calendar.getInstance());
		headCommentNew.setLocation(locMod);
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
		appState.sort(commentList, appState.dateCompare);
		// Saves comments
		appState.saveComments();
		
        // Launches MainListviewActivity
        activity = getActivity();     

        // Gets layout of head comment list item
        View commentLayout = (View) activity.findViewById(R.id.head_comment_list_item_layout);
        
        // Identifies list item objects required for the test
        TextView title = (TextView) commentLayout.findViewById(R.id.head_comment_title);
        TextView userName = (TextView) commentLayout.findViewById(R.id.head_comment_username);
        // TODO re-add in location when location implemented
        //TextView location = (TextView) commentLayout.findViewById(R.id.head_comment_location);
        TextView date = (TextView) commentLayout.findViewById(R.id.head_comment_time);
        
        // Test new comment title is correct
        assertEquals("Head comment title should appear in list", headCommentNew.getTitle(), title.getText());
        // Test new comment username is correct
        assertEquals("Head comment username should appear in list", headCommentNew.getAuthor(), userName.getText());
        // Test new comment location is correct
        // TODO re-add in location when location implemented
        //assertEquals("Head comment location should appear in list", headComment.getLocation(), location.getText());
        
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
	
	/**
	 * Test to confirm head comment list sorts by pictures correctly.
	 * 
	 * @throws Throwable
	 */
	public void testSortCommentsByPic() throws Throwable {
		// Create test comment List
		ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
		// Creates two test head comments
		CommentModel headCommentNoPic = new CommentModel();
		headCommentNoPic.setTitle("No Pic Test Title");
		headCommentNoPic.setAuthor("NoPicTestUsername");
		headCommentNoPic.setContent("No Pic Test Head Comment Content");
		headCommentNoPic.setTimestamp(Calendar.getInstance());
		
		// Create test location model
		LocationModel locMod = new LocationModel("testlocationmodel", 100, 100);
		headCommentNoPic.setLocation(locMod);
		
		// Add coment to test list
		commentList.add(headCommentNoPic);
		// Sleep one second to ensure second comment is created later
		TimeUnit.SECONDS.sleep(1);
		CommentModel headCommentPic = new CommentModel();
		headCommentPic.setTitle("Pic Test Title");
		headCommentPic.setAuthor("PicTestUsername");
		headCommentPic.setContent("Pic Test Head Comment Content");
		headCommentPic.setTimestamp(Calendar.getInstance());
		headCommentPic.setPhotoPath("testpath");
		headCommentPic.setLocation(locMod);
		commentList.add(headCommentPic);
		
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
		appState.pictureSort(commentList, appState.dateCompare);
		// Saves comments
		appState.saveComments();
		
        // Launches MainListviewActivity
        activity = getActivity();     

        // Gets layout of head comment list item
        View commentLayout = (View) activity.findViewById(R.id.head_comment_list_item_layout);
        
        // Identifies list item objects required for the test
        TextView title = (TextView) commentLayout.findViewById(R.id.head_comment_title);
        TextView userName = (TextView) commentLayout.findViewById(R.id.head_comment_username);
        // TODO re-add in location when location implemented
        //TextView location = (TextView) commentLayout.findViewById(R.id.head_comment_location);
        TextView date = (TextView) commentLayout.findViewById(R.id.head_comment_time);
        
        // Test new comment title is correct
        assertEquals("Head comment title should appear in list", headCommentPic.getTitle(), title.getText());
        // Test new comment username is correct
        assertEquals("Head comment username should appear in list", headCommentPic.getAuthor(), userName.getText());
        // Test new comment location is correct
        // TODO re-add in location when location implemented
        //assertEquals("Head comment location should appear in list", headComment.getLocation(), location.getText());
        
        // Converts new headcomment's timestamp calendar object to a testable string
        SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:mm aa");
        String timeString = sdf.format(headCommentPic.getTimestamp().getTime());
        
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
