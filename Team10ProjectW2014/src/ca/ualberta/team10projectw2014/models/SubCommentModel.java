package ca.ualberta.team10projectw2014.models;

/**
 * This class acts as the model for the sub comments.
 * @author      Costa Zervos <czervos@ualberta.ca>
 * @version     1            (current version number of program)
 */
public class SubCommentModel extends CommentModel{
	private String parentTitle;

	/**
	 * Retrieves the parent title as a string
	 * @return the parent title as a string
	 */
	public String getParentTitle() {
		return parentTitle;
	}

	/**
	 * Sets the parent title
	 * @param parentTitle string to set the title to
	 */
	public void setParentTitle(String parentTitle) {
		this.parentTitle = parentTitle;
	}

	/**
	 * Constructor method
	 * @param text the String to name the counter.
	 */
	public SubCommentModel(CommentModel parentComment) {
		//this.respondedTo = parentComment;
	}
}
