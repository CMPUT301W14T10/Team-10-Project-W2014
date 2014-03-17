
package ca.ualberta.team10projectw2014.tests;

import java.util.Calendar;

import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.controllersAndViews.EditCommentActivity;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationModel;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class EditCommentActivityTests extends ActivityInstrumentationTestCase2<EditCommentActivity> {
	
	EditCommentActivity activity;
	ApplicationStateModel appState;
	EditText usernameText;
	EditText contentText;
	EditText titleText;

	public EditCommentActivityTests(){
		super(EditCommentActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appState = ApplicationStateModel.getInstance();
	}
	
	public void testEditCommentWithUsername() throws Throwable{
		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setImageUri(null);
		headComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		
		appState.setFileContext(this.getInstrumentation().getContext());
		appState.setCommentToEdit(headComment);
		
		String testUsername = "Jerry";
		appState.loadUser();
		appState.getUserModel().setUsername(testUsername);
		
		activity = getActivity();
		
		usernameText = (EditText) activity.findViewById(R.id.cc_username);
		titleText = (EditText) activity.findViewById(R.id.cc_title);
		contentText = (EditText) activity.findViewById(R.id.cc_content);
		
		assertEquals(usernameText.getText().toString(), "test author");
		assertEquals("test title", titleText.getText().toString());
		assertEquals("Body", contentText.getText().toString());
		
	}
	
	public void testEditCommentWithoutUsername() throws Throwable{
		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setImageUri(null);
		headComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		
		appState.setFileContext(this.getInstrumentation().getContext());
		appState.setCommentToEdit(headComment);
		
		activity = getActivity();
		
		usernameText = (EditText) activity.findViewById(R.id.cc_username);
		titleText = (EditText) activity.findViewById(R.id.cc_title);
		contentText = (EditText) activity.findViewById(R.id.cc_content);
		
		assertEquals(usernameText.getText().toString(), "test author");
		assertEquals("test title", titleText.getText().toString());
		assertEquals("Body", contentText.getText().toString());
		
	}
	
	
	
}

