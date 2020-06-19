package xml.papersapp.service;

import com.itextpdf.text.DocumentException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import xml.papersapp.exceptions.sciencePapers.*;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_paper.TState;
import xml.papersapp.model.user.TRoles;
import xml.papersapp.model.user.TUser;
import xml.papersapp.repository.ReviewAssignmentRepository;
import xml.papersapp.repository.SciencePaperRepository;
import xml.papersapp.security.repository.UserRepository;
import xml.papersapp.util.XSLFOTransformer;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.*;
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
    private final XSLFOTransformer xslfoTransformer;
    private final ReviewAssignmentRepository reviewAssignmentRepository;

    public SciencePaperService(SciencePaperRepository sciencePaperRepository, UserRepository userRepository,
                               XSLFOTransformer xslfoTransformer, ReviewAssignmentRepository reviewAssignmentRepository) {
        this.sciencePaperRepository = sciencePaperRepository;
        this.userRepository = userRepository;
        this.xslfoTransformer = xslfoTransformer;
        this.reviewAssignmentRepository = reviewAssignmentRepository;
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

    public List<SciencePaper> searchForSciencePapers(String text, String dateFrom, String dateTo) throws XMLDBException, JAXBException, SAXException, IOException, ParseException {

        String state = TState.ACCEPTED.toString().toLowerCase();
        String email = "";
        return sciencePaperRepository.searchSciencePapers(email, text, dateFrom, dateTo, state);

    }

    public List<SciencePaper> searchForSciencePapersAuthenticated(String email, String text, String dateFrom, String dateTo, String state) throws XMLDBException, JAXBException, SAXException, IOException, ParseException {

        Optional<TUser> user = userRepository.findByUsername(email);
        if (user.isPresent()) {
            if (user.get().getRoles().getRole().contains("ROLE_EDITOR")) {
                email = "";
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
        return reviewAssignmentRepository.getPapersToReview(email);
    }

    public ByteArrayOutputStream generateHTML(String documentId) throws XMLDBException, JAXBException, SAXException, FileNotFoundException, SciencePaperDoesntExist {

        SciencePaper found = sciencePaperRepository.findOneByDocumentId(documentId).orElseThrow(SciencePaperDoesntExist::new);

        OutputStream outputStream = sciencePaperRepository.getXMLFromObject(found, SCIENCE_PAPER_PACKAGE);
        String xslPath = "src/main/resources/xsl/science_paper_html.xsl";
        return xslfoTransformer.generateHTML(outputStream, xslPath);

    }

    public ByteArrayOutputStream generatePDF(String documentId) throws XMLDBException, JAXBException, SAXException, IOException, DocumentException, SciencePaperDoesntExist {

        ByteArrayOutputStream html = generateHTML(documentId);


        return xslfoTransformer.generatePDF(html);

    }

    public ByteArrayOutputStream generateXML(String documentId) throws XMLDBException, JAXBException, SAXException, SciencePaperDoesntExist {

        SciencePaper found = sciencePaperRepository.findOneByDocumentId(documentId).orElseThrow(SciencePaperDoesntExist::new);

        OutputStream outputStream = sciencePaperRepository.getXMLFromObject(found, SCIENCE_PAPER_PACKAGE);

        return (ByteArrayOutputStream) outputStream;

    }

    public SciencePaper getSciencePaper(String title) throws XMLDBException, JAXBException, SAXException, SciencePaperDoesntExist {
        SciencePaper found = sciencePaperRepository.findOneByTitle(title).orElseThrow(SciencePaperDoesntExist::new);

        return found;
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

    public SciencePaper sendToRevision(String title) throws XMLDBException, JAXBException, SAXException, SciencePaperCantBeSentToRevision, SciencePaperDoesntExist {

        Optional<SciencePaper> sciencePaperOptional = sciencePaperRepository.findOneByTitle(title);

        if(!sciencePaperOptional.isPresent()) {
            throw new SciencePaperDoesntExist();
        }

        SciencePaper paper = sciencePaperOptional.get();

        if(!paper.getState().equals(TState.WAITING_FOR_APPROVAL)) {
            throw new SciencePaperCantBeSentToRevision();
        }

        paper.setState(TState.REVISION_NEEDED);

        return sciencePaperRepository.update(paper);

    }
}
