package xml.papersapp.exceptions.review;

public class ReviewAssignmentAlreadyDenied extends Exception{
    public ReviewAssignmentAlreadyDenied() {
        super("You already denied this review assignment.");
    }
}
