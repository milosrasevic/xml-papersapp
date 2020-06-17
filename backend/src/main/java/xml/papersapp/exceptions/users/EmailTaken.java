package xml.papersapp.exceptions.users;

public class EmailTaken extends Exception {
    public EmailTaken() {
        super("There is already an account with this email. Please enter another.");
    }
}
