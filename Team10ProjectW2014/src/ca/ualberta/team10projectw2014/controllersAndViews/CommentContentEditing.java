package ca.ualberta.team10projectw2014.controllersAndViews;

import android.view.View;

/**
 * @author      Bradley Poulette <bpoulett@ualberta.ca>
 * @version     1                (current version number of program)
 * 
 * <p>
 * This interface gives an outline of the basic functionality of both {@link #CreateCommentActivity} and {@link #EditCommentActivity}
 * 
 * All of these methods are described more in detail in the implementing classes' files.
 * 
*/
public interface CommentContentEditing {
	
	public void attemptCommentCreation(View v);
	
	public boolean checkStringIsAllWhiteSpace(String string);
	
	public void chooseLocation(View v);
	
	public void choosePhoto(View v);
}
