package ca.ualberta.team10projectw2014.controllersAndViews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;

public class AssortedListViewActivity extends Activity
{
	private ApplicationStateModel appState;
	private ListView commentView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assort_comment_view);
		appState = ApplicationStateModel.getInstance();
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		if(title != null){
			setTitle(title);
		}
		else{
			setTitle("Assorted Comment List");
		}
		appState.loadUser();
		if(appState.getAssortList() != appState.getUserModel().getFavourites())
			Log.e("LOOKIE HERE!", "assort list is not faves");
		appState.loadComments();
		appState.setAssortAdapter(new MainListViewAdapter(this, appState.getAssortList()));
	}
	
	protected void onResume(){
		super.onResume(); 
		appState.loadUser();
		appState.loadComments();
		appState.updateAssortAdapter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assort_view, menu);
		this.commentView = (ListView) findViewById(R.id.FavCommentList);
		this.commentView.setAdapter(this.appState.getAssortAdapter());
		//Opens SubCommentViewActivity when a comment is selected:
		this.commentView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id){
				//get the comment that was selected
				CommentModel headComment = appState.getAssortList().get(position);
				Intent subCommentView = new Intent(getApplicationContext(), SubCommentViewActivity.class);
				//Set the appropriate singleton attribute to point to the comment that
				//SubCommentViewActivity is to represent:
				appState.setSubCommentViewHead(headComment);
				//start the SubCommentViewActivity on top of the activity
				//containing the view(i.e. this MainListViewActivity):
				view.getContext().startActivity(subCommentView);
				
			}});
		return true;
	}

}
