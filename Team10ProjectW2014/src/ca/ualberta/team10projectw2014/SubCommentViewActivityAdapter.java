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
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
	/**
	 * Contains the all objects that are in the sub comment layout.
	 */
	private class ViewHolder {
		TextView textReplyTitle;
		TextView textSubTitle;
		TextView textUsername;
		TextView textLocationTime;
		ImageView imageView;
	}

	public SubCommentViewActivityAdapter(Context context, int layoutResourceId,
			ArrayList<SubCommentModel> subCommentList) {

		super(context, layoutResourceId, subCommentList);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.subCommentList = subCommentList;

	}
	/**
	 * Gets the view to be displayed by the listView.
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
					.findViewById(R.id.sub_comment_username);
			holder.textLocationTime = (TextView) view
					.findViewById(R.id.sub_comment_location_time);
			holder.imageView = (ImageView) view.findViewById(R.id.sub_comment_image);
			// Add the holder data to the view
			view.setTag(holder);

		} else {
			// If View is not empty, set viewholder to this view via tag
			holder = (ViewHolder) view.getTag();
		}
		
		//Grabs the title of the comment being replied by the getReplyTitle function
		holder.textReplyTitle.setText(this.getReplyTitle(subCommentList.get(position)));
		
		//Grabs the Location and Time of the comment and appends them together
		//by the getLocationAndTime function
		holder.textLocationTime.setText(this.getLocationAndTime(subCommentList.get(position)));
		
		
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
	 * Gets the location and time from the current sub Comment and appends them
	 * together to be displayed in subCommentListView Activity
	 * @param subCommentModel object to get the title of the comment the current
	 * 		  comment is replying to.
	 * @return a string that has the location and time appending together
	 */
	private String getLocationAndTime(SubCommentModel subComment) {
		String Location = subComment.getLocation().getName();
		sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:00 aa");
		String timeString = sdf.format(subComment.getTimestamp().getTime());
		
		
		return Location + " - " + timeString;
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
