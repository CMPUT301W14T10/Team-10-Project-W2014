package ca.ualberta.team10projectw2014.controllersAndViews;

import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.R.id;
import ca.ualberta.team10projectw2014.R.layout;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.UserModel;
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
	private ApplicationStateModel appState;
	//private UserDataController userDataController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_user_name);
		appState = ApplicationStateModel.getInstance();
		appState.loadComments();
		appState.loadUser();
		//userDataController = new UserDataController(this, this.getString(R.string.user_sav));
		
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
				
				if(userNameField.getText().length() == 0) {
					Toast.makeText(getApplicationContext(), "Enter username in text field", Toast.LENGTH_SHORT).show();
				}else{
					//create a new user model and set the username to the one provided by the user.
					userData = new UserModel(getApplicationContext());
					userData.setUsername(userNameField.getText().toString());
					
					startNewActivity(userData);
		
				}
			}
		});
		
		skipButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(getApplicationContext(), "This can be set later", Toast.LENGTH_LONG).show();
				//Create a new user with an anonymous username. This can be edited later
				userData  = new UserModel(getApplicationContext());
				startNewActivity(userData);
				
			}
		});
		

		
	}
	
	/***
	 * Function takes the userModel created with or without a provided username from
	 * the user and sends it to the mainListView activity
	 * @author sgiang92
	 * 
	 * @param UserModel user
	 */
	private void startNewActivity(UserModel user){
		//userDataController.saveToFile(user);
		Intent mainListViewActivity = new Intent(getApplicationContext(),MainListViewActivity.class);
		//mainListViewActivity.putExtra("userData", user);
		appState.saveUser();
		getApplicationContext().startActivity(mainListViewActivity);
		finish();
		
	}
}
