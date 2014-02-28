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

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Creates a custom adapter for displaying three textviews and an imageview for 
 * each item in a listview.
 */
public class MainListViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<HeadModel> headCommentList;
	
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
	 * @param commentList the ArrayList<HeadModel> object containing head
	 * comments.
	 */
	public MainListViewAdapter(Context context, 
			ArrayList<HeadModel> commentList) {
		inflater = LayoutInflater.from(context);
		this.headCommentList = commentList;
	}
	
	/**
	 * Gets the number of counters in the array of counter models.
	 * @return the number of counters in the list as an int.
	 */
	public int getCount() {
		return headCommentList.size();
	}
	
	/**
	 * Gets the view to be displayed by the listView.
	 * @return view to be displayed. 
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO complete method
		return convertView;
	}
	
	/**
	 * Gets the head comment at a specific position.
	 * @return the head comment.
	 */
	public HeadModel getItem(int position) {
		return headCommentList.get(position);
		}
	
	/**
	 * Gets the position (method needed by class type).
	 * @return position as a long.
	 */
	public long getItemId(int position) {
		return position;
		}
	
	
	
}
