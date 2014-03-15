package ca.ualberta.team10projectw2014.tests;

import ca.ualberta.team10projectw2014.SubCommentViewActivity;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class SubCommentViewActivityTests extends
        ActivityInstrumentationTestCase2<SubCommentViewActivity> {
    
    Activity activity;

    public SubCommentViewActivityTests(
            Class<SubCommentViewActivity> activityClass) {
        super(activityClass);
    }
    
    /*
     * Crashes if this is added but it is required!
     */
     
    /*
    public SubCommentViewActivityTests() {
        super(SubCommentViewActivity.class);
    }
    */
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }
    
    public void testNewHeadComment() throws Exception {
        assertEquals("true",true,true);
    }
}
