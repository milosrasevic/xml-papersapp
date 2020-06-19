package xml.papersapp.exceptions.review;

public class ReviewAssignmentAlreadyAccepted extends Exception {

    public ReviewAssignmentAlreadyAccepted() {
        super("You already accepted this review assignment");
    }
}
