package xml.papersapp.exceptions.sciencePapers;

public class SciencePaperAlreadyExist extends Exception {

    public SciencePaperAlreadyExist() {
        super("Science paper with that title already exist");
    }
}
