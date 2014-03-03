package ca.ualberta.team10projectw2014;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;


public class CreateCommentActivityTests extends
		ActivityInstrumentationTestCase2<CreateCommentActivity> {
	
	Instrumentation instrumentation;
	Activity activity;
	EditText usernameText;
	EditText contentText;
	EditText titleText;

	public CreateCommentActivityTests() {
		super(CreateCommentActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		activity = getActivity();
		
		contentText = ((EditText) activity.findViewById(ca.ualberta.team10projectw2014.R.id.cc_content));
		titleText = ((EditText) activity.findViewById(ca.ualberta.team10projectw2014.R.id.cc_title));
		usernameText = ((EditText) activity.findViewById(ca.ualberta.team10projectw2014.R.id.cc_username));
	}
	
	public void testCreateNewHeadCommentWithoutUsername() throws Throwable{
		
		runTestOnUiThread(new Runnable(){
			@Override
			public void run(){
				CreateCommentActivity activity = getActivity();
				
				activity.fillContents(null, null);
				
				assertEquals("contentText should be blank", contentText.getText().toString(), "");
				assertEquals("titleText should be blank", titleText.getText().toString(), "");
				assertEquals("usernameText should be blank", usernameText.getText().toString(), "");
				
			}
		});
		
	}

	public void testCreateNewHeadCommentWithUsername() throws Throwable{
		
		final String username = "Joseph";
		runTestOnUiThread(new Runnable(){
			@Override
			public void run(){
				CreateCommentActivity activity = getActivity();
				
				activity.fillContents(username, null);
				
				assertEquals("contentText should be blank", contentText.getText().toString(), "");
				assertEquals("titleText should be blank", titleText.getText().toString(), "");
				assertEquals("usernameText should be username", usernameText.getText().toString(), username);
				
			}
		});
		
	}
	
	public void testCreateNewSubCommentWithoutUsername() throws Throwable{
		
		final String title = "TITLE STRING";
		final HeadModel sampleHead = new HeadModel();
		sampleHead.setTitle(title);
		runTestOnUiThread(new Runnable(){
			@Override
			public void run(){
				CreateCommentActivity activity = getActivity();
				
				activity.fillContents(null, sampleHead);
				
				assertEquals("contentText should be blank", contentText.getText().toString(), "");
				assertEquals("titleText should be title", titleText.getText().toString(), title);
				assertEquals("usernameText should be blank", usernameText.getText().toString(), "");
				
			}
		});
		
	}
	
	public void testCreateNewSubCommentWithUsername() throws Throwable{
		
		final String title = "TITLE STRING";
		final String username = "USERNAME";
		final HeadModel sampleHead = new HeadModel();
		sampleHead.setTitle(title);
		runTestOnUiThread(new Runnable(){
			@Override
			public void run(){
				CreateCommentActivity activity = getActivity();
				
				activity.fillContents(username, sampleHead);
				
				assertEquals("contentText should be blank", contentText.getText().toString(), "");
				assertEquals("titleText should be title", titleText.getText().toString(), title);
				assertEquals("usernameText should be username", usernameText.getText().toString(), username);
				
			}
		});
		
	}

}
