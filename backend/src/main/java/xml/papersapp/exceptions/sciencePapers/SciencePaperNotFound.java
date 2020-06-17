package xml.papersapp.exceptions.sciencePapers;

public class SciencePaperNotFound extends Exception {
    public SciencePaperNotFound() {
        super("You do not have a pending submission with the specified title.");
    }
}
