package ca.ualberta.team10projectw2014.tests;

import java.util.Calendar;

import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.controllersAndViews.CreateCommentActivity;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationModel;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

/**
 * @author      Bradley Poulette <bpoulett@ualberta.ca>
 * @version     1                (current version number of program)
 * 
 * <p>
 * Runs tests for CreateCommentActivity
 * 
*/
public class CreateCommentActivityTests extends ActivityInstrumentationTestCase2<CreateCommentActivity> {
	
	CreateCommentActivity activity;
	ApplicationStateModel appState;
	EditText usernameText;
	EditText contentText;
	EditText titleText;

	public CreateCommentActivityTests() {
		super(CreateCommentActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appState = ApplicationStateModel.getInstance();
	}
	
	public void testCreateSubCommentWithUsername() throws Throwable{
		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setImageUri(null);
		headComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		
		appState.setFileContext(this.getInstrumentation().getContext());
		appState.setCreateCommentParent(headComment);
		
		String testUsername = "Jerry";
		appState.loadUser();
		appState.getUserModel().setUsername(testUsername);
		
		activity = getActivity();
		
		usernameText = (EditText) activity.findViewById(R.id.cc_username);
		titleText = (EditText) activity.findViewById(R.id.cc_title);
		contentText = (EditText) activity.findViewById(R.id.cc_content);
		
		assertEquals(usernameText.getText().toString(), testUsername);
		assertEquals("", titleText.getText().toString());
		assertEquals("", contentText.getText().toString());
		
	}
	
	public void testCreateHeadCommentWithUsername() throws Throwable{

		appState.setFileContext(this.getInstrumentation().getContext());

		String testUsername = "Jerry";
		appState.loadUser();
		appState.getUserModel().setUsername(testUsername);
		
		activity = getActivity();
		
		usernameText = (EditText) activity.findViewById(R.id.cc_username);
		titleText = (EditText) activity.findViewById(R.id.cc_title);
		contentText = (EditText) activity.findViewById(R.id.cc_content);
		
		assertEquals(usernameText.getText().toString(), testUsername);
		assertEquals("", titleText.getText().toString());
		assertEquals("", contentText.getText().toString());
		
	}
	
	public void testCreateSubCommentWithoutUsername() throws Throwable{
		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setImageUri(null);
		headComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		
		appState.setFileContext(this.getInstrumentation().getContext());
		appState.setCreateCommentParent(headComment);
		
		String testUsername = "";
		appState.loadUser();
		appState.getUserModel().setUsername(testUsername);
		
		activity = getActivity();
		
		usernameText = (EditText) activity.findViewById(R.id.cc_username);
		titleText = (EditText) activity.findViewById(R.id.cc_title);
		contentText = (EditText) activity.findViewById(R.id.cc_content);
		
		assertEquals(usernameText.getText().toString(), "Anonymous");
		assertEquals("", titleText.getText().toString());
		assertEquals("", contentText.getText().toString());
		
	}
	
	public void testCreateHeadCommentWithoutUsername() throws Throwable{

		appState.setFileContext(this.getInstrumentation().getContext());
		
		String testUsername = "";
		appState.loadUser();
		appState.getUserModel().setUsername(testUsername);
		
		activity = getActivity();
		
		usernameText = (EditText) activity.findViewById(R.id.cc_username);
		titleText = (EditText) activity.findViewById(R.id.cc_title);
		contentText = (EditText) activity.findViewById(R.id.cc_content);
		
		assertEquals(usernameText.getText().toString(), "Anonymous");
		assertEquals("", titleText.getText().toString());
		assertEquals("", contentText.getText().toString());
		
	}
	
	
}

