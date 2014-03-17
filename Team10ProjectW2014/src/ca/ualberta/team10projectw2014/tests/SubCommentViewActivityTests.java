package ca.ualberta.team10projectw2014.tests;

import java.text.SimpleDateFormat;
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
import ca.ualberta.team10projectw2014.models.SubCommentModel;

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
	
	
	/***
	 * User Story 1: As a user, I want to sort comments by proximity to me
	 * 
	 * Test:
	 * 		testSortByProximity
	 * 
	 */
	
	public void testSortByProximity() throws Exception{
		
	}


	/***
	 * User Store 5. As a user, I want to browse comment replies Test:
	 * 
	 * testShowHeadComment 
	 * testHeadCommentText
	 * 
	 * showSubComment 
	 * testSubCommentText
	 * 
	 * 
	 */

	/***
	 * test to test if the elements of the head comments are shown.
	 * 
	 * @throws Exception
	 */

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

		TextView textAuthor = (TextView) subCommentViewActivity
				.findViewById(R.id.head_comment_author);
		TextView textTitle = (TextView) subCommentViewActivity
				.findViewById(R.id.long_title);
		TextView textTime = (TextView) subCommentViewActivity
				.findViewById(R.id.head_comment_time_sub);
		TextView textContent = (TextView) subCommentViewActivity
				.findViewById(R.id.head_comment_text_body_sub);
		TextView textLocation = (TextView) subCommentViewActivity
				.findViewById(R.id.head_comment_location_sub);
		ImageView image = (ImageView) subCommentViewActivity
				.findViewById(R.id.head_comment_image);

		ViewAsserts.assertOnScreen(subCommentView, textAuthor);
		ViewAsserts.assertOnScreen(subCommentView, textTitle);
		ViewAsserts.assertOnScreen(subCommentView, textTime);
		ViewAsserts.assertOnScreen(subCommentView, textContent);
		ViewAsserts.assertOnScreen(subCommentView, textLocation);
		ViewAsserts.assertOnScreen(subCommentView, image);

	}

	/***
	 * test to confirm that the values of the head comment are displayed
	 * correctly
	 * 
	 * @throws Exception
	 */
	public void testHeadCommentText() throws Exception {
		
		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		
		appState.setSubCommentViewHead(headComment);


		Activity subCommentViewActivity = getActivity();


		TextView textAuthor = (TextView) subCommentViewActivity
				.findViewById(R.id.head_comment_author);
		TextView textTitle = (TextView) subCommentViewActivity
				.findViewById(R.id.long_title);
		TextView textTime = (TextView) subCommentViewActivity
				.findViewById(R.id.head_comment_time_sub);
		TextView textContent = (TextView) subCommentViewActivity
				.findViewById(R.id.head_comment_text_body_sub);
		TextView textLocation = (TextView) subCommentViewActivity
				.findViewById(R.id.head_comment_location_sub);

		// Converts new headcomment's timestamp calendar object to a testable
		// string
		SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:mm aa",java.util.Locale.getDefault());
		String timeString = sdf.format(headComment.getTimestamp().getTime());

		assertEquals("Head comment displays correct Title",
				headComment.getTitle(), textTitle.getText());
		assertEquals("Head comment displays correct Author",
				headComment.getAuthor(), textAuthor.getText());
		assertEquals("Head comment displays correct Time", timeString,
				textTime.getText());
		assertEquals("Head comment displays correct Location", headComment
				.getLocation().getName(), textLocation.getText());
		assertEquals("Head comment displays correct Content",
				headComment.getContent(),textContent.getText());

	}

	/***
	 * test to test if the elements of the sub comments are shown.
	 * 
	 * @throws Exception
	 */

	public void testShowSubComment() throws Exception {

		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setImageUri(null);
		headComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));

		SubCommentModel subComment = new SubCommentModel(headComment);
		subComment.setAuthor("test author");
		subComment.setTitle("test sub title");
		subComment.setTimestamp(Calendar.getInstance());
		subComment.setContent("Body");
		subComment.setImageUri(null);
		subComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));

		headComment.addSubComment(subComment);
		appState.setSubCommentViewHead(headComment);

		Activity subCommentViewActivity = getActivity();

		View subCommentView = subCommentViewActivity.getWindow().getDecorView();

		TextView textAuthor = (TextView) subCommentViewActivity
				.findViewById(R.id.sub_comment_author);
		TextView textReplyTitle = (TextView) subCommentViewActivity
				.findViewById(R.id.reply_title);
		TextView textTitle = (TextView) subCommentViewActivity
				.findViewById(R.id.sub_title);
		TextView textTime = (TextView) subCommentViewActivity
				.findViewById(R.id.sub_comment_time_sub);
		TextView textContent = (TextView) subCommentViewActivity
				.findViewById(R.id.sub_comment_text_body);
		TextView textLocation = (TextView) subCommentViewActivity
				.findViewById(R.id.sub_comment_location_sub);
		ImageView image = (ImageView) subCommentViewActivity
				.findViewById(R.id.sub_comment_image);

		ViewAsserts.assertOnScreen(subCommentView, textAuthor);
		ViewAsserts.assertOnScreen(subCommentView, textReplyTitle);
		ViewAsserts.assertOnScreen(subCommentView, textTitle);
		ViewAsserts.assertOnScreen(subCommentView, textTime);
		ViewAsserts.assertOnScreen(subCommentView, textContent);
		ViewAsserts.assertOnScreen(subCommentView, textLocation);
		ViewAsserts.assertOnScreen(subCommentView, image);

	}
	
	/***
	 * test to test if the elements of the sub comments are shown.
	 * 
	 * @throws Exception
	 */

	public void testSubCommentText() throws Exception {

		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setImageUri(null);
		headComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));

		SubCommentModel subComment = new SubCommentModel(headComment);
		subComment.setAuthor("test author");
		subComment.setTitle("test sub title");
		subComment.setTimestamp(Calendar.getInstance());
		subComment.setContent("Body");
		subComment.setImageUri(null);
		subComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		subComment.setParentTitle("RE: " + headComment.getTitle());

		headComment.addSubComment(subComment);
		appState.setSubCommentViewHead(headComment);

		Activity subCommentViewActivity = getActivity();
  

		TextView textAuthor = (TextView) subCommentViewActivity
				.findViewById(R.id.sub_comment_author);
		TextView textReplyTitle = (TextView) subCommentViewActivity
				.findViewById(R.id.reply_title);
		TextView textTitle = (TextView) subCommentViewActivity
				.findViewById(R.id.sub_title);
		TextView textTime = (TextView) subCommentViewActivity
				.findViewById(R.id.sub_comment_time_sub);
		TextView textContent = (TextView) subCommentViewActivity
				.findViewById(R.id.sub_comment_text_body);
		TextView textLocation = (TextView) subCommentViewActivity
				.findViewById(R.id.sub_comment_location_sub);

		// Converts new headcomment's timestamp calendar object to a testable
		// string
		SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:mm aa",java.util.Locale.getDefault());
		String timeString = sdf.format(headComment.getTimestamp().getTime());

		assertEquals("Sub comment displays correct Reply Title",
				subComment.getParentTitle(), textReplyTitle.getText());
		assertEquals("Sub comment displays correct Title",
				subComment.getTitle(), textTitle.getText());
		assertEquals("Sub comment displays correct Author",
				subComment.getAuthor(), textAuthor.getText());
		assertEquals("Sub comment displays correct Time", timeString,
				textTime.getText());
		assertEquals("Sub comment displays correct Location", subComment
				.getLocation().getName(), textLocation.getText());
		assertEquals("Sub comment displays correct Content",
				 subComment.getContent(),textContent.getText());
	}
	
	
	/***
	 * User Story 6. As a user, I want to reply to comments.
	 * 
	 * Tests:
	 * 		testReplyToHeadComment();
	 * 		testReplyToSubComment();
	 */
	
	public void testReplyToHeadComment(){
		
		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setImageUri(null);
		headComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		
		SubCommentModel subComment = new SubCommentModel(headComment);
		subComment.setAuthor("test author");
		subComment.setTitle("test sub title");
		subComment.setTimestamp(Calendar.getInstance());
		subComment.setContent("Body");
		subComment.setImageUri(null);
		subComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		subComment.setParentTitle("RE: " + headComment.getTitle());
		
		headComment.addSubComment(subComment);
		appState.setSubCommentViewHead(headComment);

		assertEquals("Head comment should have one sub comment",1,
				headComment.getSubComments().size());
		assertEquals( "The title of the respond to should be equal to the head title",
				"RE: " + headComment.getTitle(), subComment.getParentTitle());
		
	}
	
	public void testReplyToComment(){
		
		CommentModel headComment = new CommentModel();
		headComment.setAuthor("test author");
		headComment.setTitle("test title");
		headComment.setTimestamp(Calendar.getInstance());
		headComment.setContent("Body");
		headComment.setImageUri(null);
		headComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		
		SubCommentModel subComment = new SubCommentModel(headComment);
		subComment.setAuthor("test author");
		subComment.setTitle("test sub title");
		subComment.setTimestamp(Calendar.getInstance());
		subComment.setContent("Body");
		subComment.setImageUri(null);
		subComment.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		subComment.setParentTitle("RE: " + headComment.getTitle());
		
		headComment.addSubComment(subComment);
		
		SubCommentModel subComment2 = new SubCommentModel(subComment);
		subComment2.setAuthor("test author");
		subComment2.setTitle("test sub title");
		subComment2.setTimestamp(Calendar.getInstance());
		subComment2.setContent("Body");
		subComment2.setImageUri(null);
		subComment2.setLocation(new LocationModel("Test Location name", 10.4,
				10.4));
		subComment2.setParentTitle("RE: " + subComment.getTitle());
		subComment.addSubComment(subComment2);
		
		appState.setSubCommentViewHead(headComment);

		assertEquals("Sub comment should have one sub comment",1,subComment.getSubComments().size());
		assertEquals(
				"The sub comment 2 title should be equal to the sub comment title",
				"RE: " + subComment.getTitle(), subComment2.getParentTitle());
		
		
	}
	
	
	


}