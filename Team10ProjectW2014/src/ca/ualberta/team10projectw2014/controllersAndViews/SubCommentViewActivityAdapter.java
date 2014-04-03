package ca.ualberta.team10projectw2014.controllersAndViews;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.SubCommentModel;
import ca.ualberta.team10projectw2014.models.UserModel;

/**
 * Creates a custom adapter for displaying a comment's infomation in
 * SubCommentListViewActivity's listview.
 * @author      Steven Giang <giang2@ualberta.ca>
 * @version     1            (current version number of program)
 */
public class SubCommentViewActivityAdapter extends
		ArrayAdapter<CommentModel> {

	private Context context;
	private int layoutResourceId;
	private ArrayList<CommentModel> commentList;
	private UserModel userData;
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
		Button favouriteButton;
		Button moreButton;
	}

	
	//SubCommentViewActivityAdapter Contructor
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
			holder.favouriteButton = (Button) view.findViewById(R.id.favourite_option);
			holder.moreButton = (Button) view.findViewById(R.id.more_option);
			holder.replyButton.setTag(position);
			holder.favouriteButton.setTag(position);
			holder.moreButton.setTag(position);
			
			// Add the holder data to the view
						view.setTag(holder);
			//Required to be used in an inner method
			final int pos = position;
			
			if(commentList.get(pos).isInArrayList(appState.getUserModel().getFavourites())){
				holder.favouriteButton.setText("UnFavourite");
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
						((Button) v).setText("UnFavourite");
						Toast.makeText(getContext(), "Comment added to favourites", Toast.LENGTH_SHORT).show();
					}
					else {
						commentList.get(pos).setNumFavourites(commentList.get(pos).getNumFavourites() - 1);
						commentList.get(pos).removeFromArrayList(appState.getUserModel().getFavourites());
						appState.saveUser();
						appState.saveComments();
						appState.loadComments();
						((Button) v).setText("Favourite");
						Toast.makeText(context, "Comment removed from Favourite List", Toast.LENGTH_LONG).show();
					}
					
					
				}
			});
			
			
			//Open the More... dialog box for the selected comment 
			holder.moreButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((SubCommentViewActivity)context).openMoreDialog(commentList.get(pos));
					
				}
			});
			

		} else {
			// If View is not empty, set viewholder to this view via tag
			holder = (ViewHolder) view.getTag();
		}
		
		//Grabs the title of the comment being replied by the getReplyTitle function
		if(commentList.get(position).getClass() == SubCommentModel.class){
			//TO-DO get reply title from comment model.
			holder.textReplyTitle.setText(((SubCommentModel) commentList.get(position)).getParentTitle());
		}else{
			holder.textReplyTitle.setText("");
		}
		
		//Grabs the Time.
		holder.textTime.setText(this.timeToString(commentList.get(position).getTimestamp()));
		
		//Grabs the Location of the model.
		holder.textLocation.setText(commentList.get(position).getLocation().getName());
		
		// Grabs strings to be displayed for each sub comment in the list
		holder.textSubTitle.setText(commentList.get(position).getTitle());
		holder.textUsername.setText(commentList.get(position).getAuthor());
		holder.textContent.setText(commentList.get(position).getContent());
	
		
		// Sets the image attached to the comment
		if(commentList.get(position).getPhotoPath() != null){

				String imagePath = commentList.get(position).getPhotoPath();
			    // Get the dimensions of the View
			    //int targetW = holder.imageView.getWidth();
			    //int targetH = holder.imageView.getHeight();

			    // Get the dimensions of the bitmap
			    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			    bmOptions.inJustDecodeBounds = true;
			    BitmapFactory.decodeFile(imagePath, bmOptions);
			    int photoW = bmOptions.outWidth;
			    int photoH = bmOptions.outHeight;
			    
			    // Determine how much to scale down the image
			    int scaleFactor = Math.min(photoW/75, photoH/75);

			    // Decode the image file into a Bitmap sized to fill the View
			    bmOptions.inJustDecodeBounds = false;
			    bmOptions.inSampleSize = scaleFactor;
			    bmOptions.inPurgeable = true;
				

			    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
			    holder.imageView.setImageBitmap(bitmap);
			   
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
