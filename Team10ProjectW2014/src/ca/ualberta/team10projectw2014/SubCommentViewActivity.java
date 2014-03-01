package ca.ualberta.team10projectw2014;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SubCommentViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_comment_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sub_comment_view, menu);
		return true;
	}

}
