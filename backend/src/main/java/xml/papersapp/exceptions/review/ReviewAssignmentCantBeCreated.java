package xml.papersapp.exceptions.review;

public class ReviewAssignmentCantBeCreated extends Exception{
    public ReviewAssignmentCantBeCreated() {
        super("Review assignment for a science paper in this state cannot be created.");
    }
}
