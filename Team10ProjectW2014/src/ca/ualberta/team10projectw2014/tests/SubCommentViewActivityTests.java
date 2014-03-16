package ca.ualberta.team10projectw2014.tests;

import java.util.Calendar;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.team10projectw2014.R;
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
		headComment.setImageUri(null);
		headComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));

		appState.setSubCommentViewHead(headComment);

		Activity subCommentViewActivity = getActivity();
		
		View subCommentView = subCommentViewActivity.getWindow().getDecorView();
		
		TextView textAuthor = (TextView) subCommentViewActivity.findViewById(R.id.head_comment_author);
		TextView textTitle = (TextView) subCommentViewActivity.findViewById(R.id.long_title);
		TextView textTime = (TextView) subCommentViewActivity.findViewById(R.id.head_comment_time_sub);
		TextView textContent = (TextView) subCommentViewActivity.findViewById(R.id.head_comment_text_body_sub);
		TextView textLocation = (TextView) subCommentViewActivity.findViewById(R.id.head_comment_location_sub);
		ImageView image = (ImageView) subCommentViewActivity.findViewById(R.id.head_comment_image);
		
		ViewAsserts.assertOnScreen(subCommentView, textAuthor);
		ViewAsserts.assertOnScreen(subCommentView, textTitle);
		ViewAsserts.assertOnScreen(subCommentView, textTime);
		ViewAsserts.assertOnScreen(subCommentView, textContent);
		ViewAsserts.assertOnScreen(subCommentView, textLocation);
		ViewAsserts.assertOnScreen(subCommentView, image);
		
		
		
		
		

	}
}
