package xml.papersapp.exceptions.review;

public class ReviewAssignmenAlreadyExists extends Exception{
    public ReviewAssignmenAlreadyExists() {
        super("This science paper is already assigned for review to this author.");
    }
}
