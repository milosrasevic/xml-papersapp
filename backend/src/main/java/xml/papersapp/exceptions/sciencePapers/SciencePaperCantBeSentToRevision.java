package xml.papersapp.exceptions.sciencePapers;

public class SciencePaperCantBeSentToRevision extends  Exception{

    public SciencePaperCantBeSentToRevision() {
        super("This paper can't be sent to revision right now.");
    }
}
