package ca.ualberta.team10projectw2014.tests;

import java.util.Calendar;

import com.mapquest.android.maps.GeoPoint;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import ca.ualberta.team10projectw2014.controllersAndViews.CreateCommentActivity;
import ca.ualberta.team10projectw2014.controllersAndViews.MapsViewActivity;
import ca.ualberta.team10projectw2014.controllersAndViews.SubCommentViewActivity;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationModel;
import junit.framework.TestCase;

/**
 * @author       Bradley Poulette <bpoulett@ualberta.ca>
 * @version      1                (current version number of program)  <p>  Runs tests for MapsViewActivity
 */
public class MapsViewActivityTests extends ActivityInstrumentationTestCase2<MapsViewActivity> {

	MapsViewActivity activity;
	ApplicationStateModel appState;
	EditText usernameText;
	EditText contentText;
	EditText titleText;
	
	public MapsViewActivityTests() {
		super(MapsViewActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appState = ApplicationStateModel.getInstance();
	}
	
	public void testMapThread() throws Throwable{
		fail();
		
		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setImageUri(null);
		headComment.setLocation(new LocationModel("Test Location name", 0, 90));
		
		CommentModel subComment = new CommentModel();
		subComment.setAuthor("test author");
		subComment.setTitle("test title");
		subComment.setTimestamp(Calendar.getInstance());
		subComment.setContent("Body");
		subComment.setImageUri(null);
		subComment.setLocation(new LocationModel("Test Location name", 0, 90));
		
		headComment.addSubComment(subComment);
		
		
		appState.setFileContext(this.getInstrumentation().getContext());
		appState.setSubCommentViewHead(headComment);
		
		activity = getActivity();
		
		assertTrue(activity.getFlattenedList().contains(subComment));
		
	}
	
	
	
}
