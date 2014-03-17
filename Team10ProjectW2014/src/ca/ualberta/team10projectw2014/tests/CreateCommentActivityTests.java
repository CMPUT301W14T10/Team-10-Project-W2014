package ca.ualberta.team10projectw2014.tests;

import java.util.Calendar;

import ca.ualberta.team10projectw2014.controllersAndViews.CreateCommentActivity;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationModel;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;


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
	
	public void testCreateNewHeadCommentWithoutUsername() throws Throwable{
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
		
		appState.loadUser();
		activity = getActivity();
		
	}
}

