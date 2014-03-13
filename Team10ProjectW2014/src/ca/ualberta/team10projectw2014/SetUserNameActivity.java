package ca.ualberta.team10projectw2014;

import ca.ualberta.team10projectw2014.controller.UserDataController;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetUserNameActivity extends Activity {
	
	private UserModel userData;
	private EditText userNameField;
	private Button setButton;
	private Button skipButton;
	private UserDataController userDataController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_user_name);
		
		userDataController = new UserDataController(this, this.getString(R.string.user_sav));
		
		//Disable Actionbar Icon and set title
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setTitle("Set Username");		
		
		userNameField = (EditText)findViewById(R.id.set_username_field);
		setButton = (Button)findViewById(R.id.button_set);
		skipButton = (Button)findViewById(R.id.button_skip);
		
		setButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(userNameField.getText().length() == 0) {
					Toast.makeText(getApplicationContext(), "Enter username in text field", Toast.LENGTH_SHORT).show();
				}else{
					userData = new UserModel(getApplicationContext());
					userData.setUsername(userNameField.getText().toString());
					
					//userDataController.saveToFile(userData);
					Intent mainListViewActivity = new Intent(getApplicationContext(),MainListViewActivity.class);
					mainListViewActivity.putExtra("userData", userData);
					getApplicationContext().startActivity(mainListViewActivity);
					finish();
				}
			}
		});
		

		
	}
}
