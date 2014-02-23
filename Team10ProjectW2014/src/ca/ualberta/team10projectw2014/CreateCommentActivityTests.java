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
		titleText = ((EditText) activity.findViewById(ca.ualberta.cs.team10projectw2014.R.id.cc_title));
		usernameText = ((EditText) activity.findViewById(ca.ualberta.cs.team10projectw2014.R.id.cc_username));
	}
	
	public void testCreateNewHeadCommentWithoutUsername(){
		CreateCommentActivity activity = getActivity();
		
		activity.fillContents();
		
		
	}

}
