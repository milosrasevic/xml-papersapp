package xml.papersapp.service;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.springframework.stereotype.Service;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import xml.papersapp.exceptions.sciencePapers.SciencePaperAlreadyExist;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_paper.TState;
import xml.papersapp.repository.SciencePaperRepository;

import javax.xml.bind.JAXBException;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

import static xml.papersapp.constants.Namespaces.SCIENCE_PAPERS_NAMESPACE;
import static xml.papersapp.util.Util.createId;
import static xml.papersapp.util.Util.getXMLGregorianCalendar;

@Service
public class SciencePaperService {

    private final SciencePaperRepository sciencePaperRepository;

    public SciencePaperService(SciencePaperRepository sciencePaperRepository) {
        this.sciencePaperRepository = sciencePaperRepository;
    }

    public SciencePaper create(SciencePaper sciencePaper) throws JAXBException, XMLDBException, SciencePaperAlreadyExist {

        // check if paper with that title already exist
        Optional<SciencePaper> found = sciencePaperRepository.findOneByTitle(sciencePaper.getTitle());

        if (found.isPresent()) {
            // already exist
            throw new SciencePaperAlreadyExist();
        }

        String id = createId(SCIENCE_PAPERS_NAMESPACE);
        sciencePaper.setId(id);
        sciencePaper.setState(TState.WAITING);
        sciencePaper.setDateCreated(getXMLGregorianCalendar());
        sciencePaperRepository.save(sciencePaper);

        return sciencePaper;
    }

}
