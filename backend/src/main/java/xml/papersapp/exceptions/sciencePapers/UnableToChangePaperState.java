package xml.papersapp.exceptions.sciencePapers;

public class UnableToChangePaperState extends Exception {
    public UnableToChangePaperState() {
        super("Science paper state can't be changed, because it is not in WAITING_FOR_APPROVAL state.");
    }
}