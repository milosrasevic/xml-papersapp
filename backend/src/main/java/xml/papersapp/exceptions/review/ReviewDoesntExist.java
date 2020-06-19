package xml.papersapp.exceptions.review;

public class ReviewDoesntExist extends Exception{

    public ReviewDoesntExist() {
        super("Review doesn't exist");
    }
}
