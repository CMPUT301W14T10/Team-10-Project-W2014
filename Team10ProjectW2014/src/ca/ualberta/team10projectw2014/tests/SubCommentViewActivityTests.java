package ca.ualberta.team10projectw2014.tests;

import java.util.Calendar;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.team10projectw2014.controllersAndViews.SubCommentViewActivity;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationModel;

public class SubCommentViewActivityTests extends
		ActivityInstrumentationTestCase2<SubCommentViewActivity> {

	Activity activity;
	ApplicationStateModel appState;

	public SubCommentViewActivityTests() {
		super(SubCommentViewActivity.class);
		appState = ApplicationStateModel.getInstance();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testShowHeadComment() throws Exception {


		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setLocation(new LocationModel("Test Location name",10.4,10.4));
	
		appState.setSubCommentViewHead(headComment);
		
		//Activity subCommentViewActivity = getActivity();
		
		


		
		
		
		
		}
}
