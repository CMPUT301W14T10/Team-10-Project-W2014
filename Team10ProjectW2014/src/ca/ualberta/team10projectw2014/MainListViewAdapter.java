
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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Creates a custom adapter for displaying three textviews and an imageview for 
 * each item in a listview.
 */
public class MainListViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<CommentModel> headCommentList;
	private SimpleDateFormat sdf;
	private String timeString;
	
	/**
	 * Initializes textview objects to be added to the ListView.
	 */
	private class ViewHolder {
		TextView textTitle;
		TextView textUsername;
		TextView textLocation;
		TextView textTime;
		ImageView imageView;
	}
	
	/**
	 * Constructor that sets the layout inflater and retrieves the head comment 
	 * list.
	 * @param context the layout of the activity.
	 * @param commentList the ArrayList<CommentModel> object containing head
	 * comments.
	 */
	public MainListViewAdapter(Context context, 
			ArrayList<CommentModel> commentList) {
		inflater = LayoutInflater.from(context);
		this.headCommentList = commentList;
	}
	
	/**
	 * Gets the number of head comments in the array of head comments.
	 * @return the number of head comments in the list as an int.
	 */
	public int getCount() {
		return headCommentList.size();
	}
	
	/**
	 * Gets the view to be displayed by the listView.
	 * @return view to be displayed. 
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			// If view is empty, create a new viewholder
			holder = new ViewHolder();
			// Add head_comment_list_item data to the view
			convertView = inflater.inflate(R.layout.head_comment_list_item, 
					null);
			// Add the four textviews used in each list entry to the holder
			holder.textTitle = (TextView) convertView.
					findViewById(R.id.head_comment_title);
			holder.textUsername = (TextView) convertView.
					findViewById(R.id.head_comment_username);
			holder.textLocation = (TextView) convertView.
					findViewById(R.id.head_comment_location);
			holder.textTime = (TextView) convertView.
					findViewById(R.id.head_comment_time);
			holder.imageView = (ImageView) convertView.findViewById(R.id.head_comment_image);
			// Add the holder data to the view
			convertView.setTag(holder);
		}
		else {
			// If View is not empty, set viewholder to this view via tag
			holder = (ViewHolder) convertView.getTag();
		}
		
		// Grabs strings to be displayed for each head comment in the list
		holder.textTitle.setText(headCommentList.get(position).getTitle());
		holder.textUsername.setText(headCommentList.get(position).getAuthor());
		
		// TODO implement location when its working and doesn't crash everything
		holder.textLocation.setText("at: " + headCommentList.get(position).getLocation().
				getName());
		
		// String for time retrieved using private method
		holder.textTime.setText(this.timeToString(headCommentList.get(position).
				getTimestamp()));
		
		// Sets the image attached to the comment
		if(headCommentList.get(position).getPhotoPath() != null){

				String imagePath = headCommentList.get(position).getPhotoPath();
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
			    int scaleFactor = Math.min(photoW/50, photoH/50);

			    // Decode the image file into a Bitmap sized to fill the View
			    bmOptions.inJustDecodeBounds = false;
			    bmOptions.inSampleSize = scaleFactor;
			    bmOptions.inPurgeable = true;
				

			    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
			    holder.imageView.setImageBitmap(bitmap);
			   
		}
		return convertView;
	}
	
	/**
	 * Gets the head comment at a specific position.
	 * @return the head comment.
	 */
	public CommentModel getItem(int position) {
		return headCommentList.get(position);
		}
	
	/**
	 * Gets the position (method needed by class type).
	 * @return position as a long.
	 */
	public long getItemId(int position) {
		return position;
		}
	
	/**
	 * Takes in the timestamp as a Calendar object and converts it to a string
	 * that can be used in a textView.
	 * @param calendar object to retrieve string from
	 * @return string of the formatted date of the timestamp
	 */
	private String timeToString (Calendar calendar) {
		sdf = new SimpleDateFormat("MMM. dd, yyyy - hh:mm aa");
		timeString = sdf.format(calendar.getTime());
		return timeString;
	}
	
}
