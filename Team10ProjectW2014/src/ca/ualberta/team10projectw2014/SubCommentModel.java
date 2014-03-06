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

/**
 * This class acts as the model for the sub comments.
 * @author Costa Zervos
 */
public class SubCommentModel extends CommentModel{
	private ArrayList<SubCommentModel> subComments = new ArrayList<SubCommentModel>();
	private CommentModel respondedTo;

	/**
	 * Constructor method that sets the comment this sub comment is responding to.
	 * @param text the String to name the counter.
	 */
	public SubCommentModel(SubCommentModel parentComment) {
		this.respondedTo = parentComment;
	}

	
	public ArrayList<SubCommentModel> getSubComments() {
		return subComments;
	}

	
	public void setSubComments(ArrayList<SubCommentModel> subComments) {
		this.subComments = subComments;
	}

	
	public CommentModel getRespondedTo() {
		return respondedTo;
	}

	
	public void setRespondedTo(CommentModel respondedTo) {
		this.respondedTo = respondedTo;
	}
}
