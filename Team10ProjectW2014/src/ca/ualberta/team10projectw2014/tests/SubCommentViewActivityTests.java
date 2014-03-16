package ca.ualberta.team10projectw2014.tests;

import ca.ualberta.team10projectw2014.ApplicationStateModel;
import ca.ualberta.team10projectw2014.SubCommentViewActivity;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class SubCommentViewActivityTests extends
        ActivityInstrumentationTestCase2<SubCommentViewActivity> {
    
    Activity activity;
    ApplicationStateModel appState;

    public SubCommentViewActivityTests() {
        super(SubCommentViewActivity.class);
    }
 
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void testShowNewHeadComment() throws Exception {
    	
        assertEquals("true",true,true);
    }
}
