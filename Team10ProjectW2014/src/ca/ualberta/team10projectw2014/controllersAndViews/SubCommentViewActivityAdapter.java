package ca.ualberta.team10projectw2014.controllersAndViews;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.SubCommentModel;
import ca.ualberta.team10projectw2014.models.UserModel;

/**
 * Creates a custom adapter for displaying a comment's infomation in SubCommentListViewActivity's listview.
 * @author       Steven Giang <giang2@ualberta.ca>
 * @version      1            (current version number of program)
 */
public class SubCommentViewActivityAdapter extends
		ArrayAdapter<CommentModel> {

	private Context context;
	private int layoutResourceId;
	private ArrayList<CommentModel> commentList;
	/**
	 * @uml.property  name="userData"
	 * @uml.associationEnd  
	 */
	private UserModel userData;
	/**
	 * @uml.property  name="appState"
	 * @uml.associationEnd  
	 */
	private ApplicationStateModel appState;

	

	/**
	 * Contains the all objects that are in the sub comment layout.
	 */
	private class ViewHolder {
		TextView textReplyTitle;
		TextView textSubTitle;
		TextView textUsername;
		TextView textLocation;
		TextView textContent;
		TextView textTime;
		ImageView imageView;
		
		Button replyButton;
		ImageButton favouriteButton;
		ImageButton editButton;
		ImageButton wantToReadButton;
	}

	
	/**
	 * SubCommentViewActivityAdapter Contructor
	 * 
	 * @param context
	 * @param layoutResourceId
	 * @param commentList
	 * @param userData
	 */
	public SubCommentViewActivityAdapter(
			Context context, 
			int layoutResourceId,
			ArrayList<CommentModel> commentList,UserModel userData) {

		super(context, layoutResourceId, commentList);
		this.appState = ApplicationStateModel.getInstance();
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.commentList = commentList;
		this.userData = userData;
	}
	
	/**
	 * Gets the view to be displayed by the listView.
	 * 
	 * Code Adapted from:
	 * http://stackoverflow.com/questions/8121476
	 * /how-to-setonclicklistener-on-the-button-inside-the-listview
	 * 
	 * @return view to be displayed. 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;

		if (view == null) {
			// If view is empty, create a new viewholder
			holder = new ViewHolder();
			
			// Add sub_comment_view_sub_comment_item data to the view
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			view = inflater.inflate(layoutResourceId, parent, false);

			// Add the textviews used in each list entry to the holder
			holder.textReplyTitle = (TextView) view
					.findViewById(R.id.reply_title);
			holder.textSubTitle = (TextView) view.findViewById(R.id.sub_title);
			holder.textUsername = (TextView) view
					.findViewById(R.id.sub_comment_author);
			holder.textLocation = (TextView) view.findViewById(R.id.sub_comment_location_sub);
			holder.textTime = (TextView) view.findViewById(R.id.sub_comment_time_sub);
			holder.imageView = (ImageView) view.findViewById(R.id.sub_comment_image);
			holder.textContent = (TextView) view.findViewById(R.id.sub_comment_text_body);
			
			
			// Add buttons used in each list entry to the holder
			holder.replyButton = (Button) view.findViewById(R.id.reply_option);
			holder.favouriteButton = (ImageButton) view.findViewById(R.id.favourite_option);
			holder.wantToReadButton = (ImageButton) view.findViewById(R.id.want_to_read_option);
			holder.editButton = (ImageButton) view.findViewById(R.id.edit_option);
			holder.replyButton.setTag(position);
			holder.favouriteButton.setTag(position);
			holder.wantToReadButton.setTag(position);
			holder.editButton.setTag(position);
			
			
			// Add the holder data to the view
						view.setTag(holder);
			//Required to be used in an inner method
			final int pos = position;
			
			if(!commentList.get(pos).getAuthorAndroidID().contains(userData.getAndroidID())){
				holder.editButton.setVisibility(View.GONE);
			}
			
			holder.replyButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Open up Create comment activity
					//will send the title of the comment the user wants to reply to. 
					Intent createComment = new Intent(context.getApplicationContext(), CreateCommentActivity.class);
					appState.setCreateCommentParent(commentList.get(pos));
					context.startActivity(createComment);
			
					
				}
			});
			
			holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Add a Comment the user's favourite list if comment is not
					//already in the user's favourite
					if(!commentList.get(pos).isInArrayList(appState.getUserModel().getFavourites())){
						commentList.get(pos).setNumFavourites(commentList.get(pos).getNumFavourites() + 1);
						appState.getUserModel().getFavourites().add(commentList.get(pos));
						appState.saveUser();
						appState.saveComments();
						appState.loadComments();
						((ImageButton) v).setImageResource(R.drawable.ic_action_star_yellow);
						Toast.makeText(getContext(), "Comment added to favourites", Toast.LENGTH_SHORT).show();
					}
					else {
						commentList.get(pos).setNumFavourites(commentList.get(pos).getNumFavourites() - 1);
						commentList.get(pos).removeFromArrayList(appState.getUserModel().getFavourites());
						appState.saveUser();
						appState.saveComments();
						appState.loadComments();
						((ImageButton) v).setImageResource(R.drawable.ic_action_favourite);
						Toast.makeText(context, "Comment removed from Favourite List", Toast.LENGTH_LONG).show();
					}
					
					
				}
			});
			
			
			//Open the More... dialog box for the selected comment 
			holder.wantToReadButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					if(!commentList.get(pos).isInArrayList(appState.getUserModel().getWantToReadComments())){
						appState.getUserModel().getWantToReadComments().add(commentList.get(pos));
						appState.saveUser();
						((ImageButton) v).setImageResource(R.drawable.ic_action_bookmark_red);
					}
					else{
						commentList.get(pos).removeFromArrayList(appState.getUserModel().getWantToReadComments());
						appState.saveUser();
						((ImageButton) v).setImageResource(R.drawable.ic_action_bookmark);
					}
				}
			});
			

		} else {
			// If View is not empty, set viewholder to this view via tag
			holder = (ViewHolder) view.getTag();
			// Add the textviews used in each list entry to the holder
			holder.textReplyTitle.setText("");
			holder.textSubTitle.setText("");
			holder.textUsername.setText("");
			holder.textLocation.setText("");
			holder.textTime.setText("");
			holder.imageView.setVisibility(View.GONE);
			holder.textContent.setText("");
		}
		
		
		

		holder.textReplyTitle.setText(commentList.get(position).getParentTitle());

		
		
		//Grabs the Time.
		holder.textTime.setText(this.timeToString(commentList.get(position).getTimestamp()));
		
		//Grabs the Location of the model.
		holder.textLocation.setText(commentList.get(position).getLocation().getName());
		
		// Grabs strings to be displayed for each sub comment in the list
		holder.textSubTitle.setText(commentList.get(position).getTitle());
		holder.textUsername.setText(commentList.get(position).getAuthor());
		holder.textContent.setText(commentList.get(position).getContent());

		if(!commentList.get(position).isInArrayList(appState.getUserModel().getFavourites())){
			holder.favouriteButton.setImageResource(R.drawable.ic_action_favourite);
		}
		else{
			holder.favouriteButton.setImageResource(R.drawable.ic_action_star_yellow);
		}
		
		if(!commentList.get(position).isInArrayList(appState.getUserModel().getWantToReadComments())){
			holder.wantToReadButton.setImageResource(R.drawable.ic_action_bookmark);
		}
		else{
			holder.wantToReadButton.setImageResource(R.drawable.ic_action_bookmark_red);
		}
		// Sets the image attached to the comment
		// Sets the image attached to the comment
		if(commentList.get(position).getPhoto() != null){
			Bitmap bitmap = commentList.get(position).getPhoto();
			holder.imageView.setImageBitmap(bitmap);
			   
		}
		else{
			holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_camera));
		}
	
		
		

		return view;
	}
	
	/**
	 * Takes in the timestamp as a Calendar object and converts it to a string
	 * that can be used in a textView.
	 * @param calendar object to retrieve string from
	 * @return string of the formatted date of the timestamp
	 */
	private String timeToString (Calendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:mm aa",java.util.Locale.getDefault());
		String timeString = sdf.format(calendar.getTime());
		return timeString;
	}


	

}
