package xml.papersapp.service;

import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.exceptions.sciencePapers.SciencePaperAlreadyExist;
import xml.papersapp.model.cover_letter.CoverLetter;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_paper.TState;
import xml.papersapp.repository.CoverLetterRepository;

import javax.xml.bind.JAXBException;
import java.util.Calendar;
import java.util.Optional;

import static xml.papersapp.constants.Namespaces.COVER_LETTER_NAMESPACE;
import static xml.papersapp.constants.Namespaces.SCIENCE_PAPER_NAMESPACE;
import static xml.papersapp.util.Util.createId;

@Service
public class CoverLetterService {

    private final CoverLetterRepository coverLetterRepository;


    public CoverLetterService(CoverLetterRepository coverLetterRepository) {
        this.coverLetterRepository = coverLetterRepository;
    }

    public CoverLetter create(String xml) throws JAXBException, SAXException, XMLDBException {

        // validate xml
        CoverLetter coverLetter = coverLetterRepository.getCoverLetterFromResource(xml);

        // check if paper with that title already exist
//        Optional<SciencePaper> found = sciencePaperRepository.findOneByTitle(sciencePaper.getTitle());
//
//        if (found.isPresent()) {
//            throw new SciencePaperAlreadyExist();
//        }

        String id = createId(COVER_LETTER_NAMESPACE);
        coverLetter.setId(id);
        coverLetter = coverLetterRepository.save(coverLetter);

        return coverLetter;

    }
}
