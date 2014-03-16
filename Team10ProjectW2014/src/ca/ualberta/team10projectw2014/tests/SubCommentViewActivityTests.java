package ca.ualberta.team10projectw2014.tests;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;
import ca.ualberta.team10projectw2014.ApplicationStateModel;
import ca.ualberta.team10projectw2014.CommentModel;
import ca.ualberta.team10projectw2014.LocationModel;
import ca.ualberta.team10projectw2014.SubCommentViewActivity;

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
		
		Activity subCommentViewActivity = getActivity();
		
		


		
		
		
		
		}
}
