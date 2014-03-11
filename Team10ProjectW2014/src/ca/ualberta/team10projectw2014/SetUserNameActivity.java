package ca.ualberta.team10projectw2014;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;

public class SetUserNameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_user_name);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		
		// Set the Title in the Actionbar to the title of the head comment
		actionbar.setTitle("Set Username");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_user_name, menu);
		return true;
	}

}
