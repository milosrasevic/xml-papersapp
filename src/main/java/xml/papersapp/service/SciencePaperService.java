package xml.papersapp.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import xml.papersapp.exceptions.sciencePapers.SciencePaperAlreadyExist;
import xml.papersapp.exceptions.sciencePapers.SciencePaperNotFound;
import xml.papersapp.exceptions.sciencePapers.UnableToChangePaperState;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_paper.TState;
import xml.papersapp.model.user.TRoles;
import xml.papersapp.model.user.TUser;
import xml.papersapp.repository.SciencePaperRepository;
import xml.papersapp.security.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.*;

import static xml.papersapp.constants.Files.SCHEME_SCIENCE_PAPER_PATH;
import static xml.papersapp.constants.Namespaces.SCIENCE_PAPERS_NAMESPACE;
import static xml.papersapp.constants.Namespaces.SCIENCE_PAPER_NAMESPACE;
import static xml.papersapp.constants.Packages.SCIENCE_PAPER_PACKAGE;
import static xml.papersapp.util.Util.createId;
import static xml.papersapp.util.Util.getXMLGregorianCalendar;

@Service
public class SciencePaperService {

    private final SciencePaperRepository sciencePaperRepository;
    private final UserRepository userRepository;

    public SciencePaperService(SciencePaperRepository sciencePaperRepository, UserRepository userRepository) {
        this.sciencePaperRepository = sciencePaperRepository;
        this.userRepository = userRepository;
    }

    public SciencePaper create(String xml) throws JAXBException, XMLDBException, SciencePaperAlreadyExist, SAXException {

        // validate xml
        SciencePaper sciencePaper = sciencePaperRepository.getSciencePaperFromResource(xml);

        // check if paper with that title already exist
        Optional<SciencePaper> found = sciencePaperRepository.findOneByTitle(sciencePaper.getTitle());

        if (found.isPresent()) {
            throw new SciencePaperAlreadyExist();
        }

        String id = createId(SCIENCE_PAPER_NAMESPACE);
        sciencePaper.setId(id);
        sciencePaper.setState(TState.WAITING);
        sciencePaper.setDateCreated(Calendar.getInstance().getTime());
//        sciencePaper.setDateCreated(getXMLGregorianCalendar());
        sciencePaper = sciencePaperRepository.save(sciencePaper);

        return sciencePaper;
    }

    public List<SciencePaper> getSciencePaperByAuthorsEmail(String email) throws XMLDBException, JAXBException, SAXException {

        return sciencePaperRepository.findAllByAuthorsEmail(email);

    }

    public List<SciencePaper> searchForSciencePapers(String email, String text, String dateFrom, String dateTo) throws XMLDBException, JAXBException, SAXException, IOException, ParseException {

        String state = "";
        if (email.equals("")) {
            // nonauthenticated user search only accepted sps
            state = TState.ACCEPTED.toString().toLowerCase();
        } else {
            Optional<TUser> user = userRepository.findByUsername(email);
            if (user.isPresent()) {
                if (user.get().getRoles().getRole().contains("ROLE_EDITOR")) {
                    email = "";
                }
            }
        }

        return sciencePaperRepository.searchSciencePapers(email, text, dateFrom, dateTo, state);

    }


//    private SciencePaper validateSciencePaperXML(String xml) throws JAXBException, SAXException {
//
//        JAXBContext context = JAXBContext.newInstance(SCIENCE_PAPER_PACKAGE);
//
//        Unmarshaller unmarshaller = context.createUnmarshaller();
//
//        // XML schema validacija
//        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        Schema schema = schemaFactory.newSchema(new File(SCHEME_SCIENCE_PAPER_PATH));
//
//        // Podešavanje unmarshaller-a za XML schema validaciju
//        unmarshaller.setSchema(schema);
//
//        SciencePaper sciencePaper = (SciencePaper) unmarshaller.unmarshal(new File(xml));
//
//        return sciencePaper;
//
//    }

    public SciencePaper delete(String title, String email) throws XMLDBException, SciencePaperNotFound, JAXBException, SAXException {
        return sciencePaperRepository.delete(title, email);
    }

    public List<SciencePaper> getPapersToReview(String email) throws XMLDBException, JAXBException, SAXException {
        return sciencePaperRepository.getPapersToReview(email);
    }

    public SciencePaper decideOnSciencePaper(String paperTitle, boolean isAccepted) throws XMLDBException, JAXBException, SAXException, SciencePaperNotFound, UnableToChangePaperState {
        Optional<SciencePaper> sciencePaper = sciencePaperRepository.findOneByTitle(paperTitle);

        if (!sciencePaper.isPresent()) {
            throw new SciencePaperNotFound();
        }

        if(!sciencePaper.get().getState().equals(TState.WAITING_FOR_APPROVAL)) {
            throw new UnableToChangePaperState();
        }

        if(isAccepted) {
            sciencePaper.get().setState(TState.ACCEPTED);
        } else {
            sciencePaper.get().setState(TState.REJECTED);
        }

        return sciencePaperRepository.update(sciencePaper.get());
    }
}
