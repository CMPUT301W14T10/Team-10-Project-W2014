package ca.ualberta.team10projectw2014.models;

/**
 * Defines the operation and ID to either add or delete to ElasticSearch.
 * @author dvyee
 *
 */
public class QueueModel {
    private boolean delete;
    private CommentModel comment;
    
    /**
     * Either add or delete the CommentModel given by a uniqueID
     * @param operation false=add; true=delete
     * @param uniqueID The unique identifier of the CommentModel
     */
    public QueueModel(boolean operation, CommentModel comment) {
        super();
        this.delete = operation;
        this.comment = comment;
    }
    
    public boolean getOperation() {
        return delete;
    }
    public void setOperation(boolean operation) {
        this.delete = operation;
    }
    public CommentModel getComment() {
        return this.comment;
    }
    public void setComment(CommentModel comment) {
        this.comment = comment;
    }   
}
