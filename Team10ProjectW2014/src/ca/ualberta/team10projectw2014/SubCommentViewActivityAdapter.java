/**
 * Copyright 2014 Cole Fudge, Steven Giang, Bradley Poullet, David Yee, and Costa Zervos

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package ca.ualberta.team10projectw2014;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Creates a custom adapter for displaying a comment's infomation in
 * SubCommentListViewActivity's listview.
 */

public class SubCommentViewActivityAdapter extends
		ArrayAdapter<SubCommentModel> {

	private Context context;
	private int layoutResourceId;
	private ArrayList<SubCommentModel> subCommentList;
	private SimpleDateFormat sdf;
	private UserModel userData;
	

	/**
	 * Contains the all objects that are in the sub comment layout.
	 */
	private class ViewHolder {
		TextView textReplyTitle;
		TextView textSubTitle;
		TextView textUsername;
		TextView textLocation;
		TextView textTime;
		ImageView imageView;
		
		Button replyButton;
		Button favouriteButton;
		Button moreButton;
	}


	public SubCommentViewActivityAdapter(
			Context context, 
			int layoutResourceId,
			ArrayList<SubCommentModel> subCommentList,UserModel userData) {

		super(context, layoutResourceId, subCommentList);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.subCommentList = subCommentList;
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
			
			
			// Add buttons used in each list entry to the holder
			holder.replyButton = (Button) view.findViewById(R.id.reply_option);
			holder.favouriteButton = (Button) view.findViewById(R.id.favourite_option);
			holder.moreButton = (Button) view.findViewById(R.id.more_option);
			holder.replyButton.setTag(position);
			holder.favouriteButton.setTag(position);
			holder.moreButton.setTag(position);
			
			// Add the holder data to the view
						view.setTag(holder);
			
			holder.replyButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			
			holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			
			holder.moreButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
				}
			});
			
			
			
			
			

		} else {
			// If View is not empty, set viewholder to this view via tag
			holder = (ViewHolder) view.getTag();
		}
		
		//Grabs the title of the comment being replied by the getReplyTitle function
		holder.textReplyTitle.setText(this.getReplyTitle(subCommentList.get(position)));
		
		//Grabs the Time.
		holder.textTime.setText(this.TimeToString(subCommentList.get(position).getTimestamp()));
		
		//Grabs the Location of the model.
		//holder.textLocation.setText(subCommentList.get(position).getLocation().getName());
		
		// Grabs strings to be displayed for each sub comment in the list
		holder.textSubTitle.setText(subCommentList.get(position).getTitle());
		holder.textUsername.setText(subCommentList.get(position).getAuthor());
		
		
		// Sets the image attached to the comment
		if(subCommentList.get(position).getPhoto() != null){
			holder.imageView.setImageBitmap(subCommentList.get(position).getPhoto());
		}
		
		

		return view;
	}
	
	/**
	 * Takes in the timestamp as a Calendar object and converts it to a string
	 * that can be used in a textView.
	 * @param calendar object to retrieve string from
	 * @return string of the formatted date of the timestamp
	 */
	private String TimeToString (Calendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:00 aa",java.util.Locale.getDefault());
		String timeString = sdf.format(calendar.getTime());
		return timeString;
	}

	
	
	/**
	 * Takes the current subComment and gets the title from the comment it is
	 * replying to and appends it to "Re: ".
	 * @param subCommentModel object to get the title of the comment the current
	 * 		  comment is replying to.
	 * @return a string that contains the title of the comment being replied to.
	 */
	private String getReplyTitle(SubCommentModel subComment){
		return "Re: " + subComment.getRespondedTo().getTitle();
		
	}
	

	

}
