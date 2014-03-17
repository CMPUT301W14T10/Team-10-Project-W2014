package ca.ualberta.team10projectw2014.controllersAndViews;

import android.view.View;

public interface CommentContentEditing {
	
	public void attemptCommentCreation(View v);
	
	public boolean checkStringIsAllWhiteSpace(String string);
	
	public void chooseLocation(View v);
	
	public void choosePhoto(View v);
}
