package xml.papersapp.repository;

import org.checkerframework.checker.nullness.Opt;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import xml.papersapp.exceptions.review.ReviewAssignmenAlreadyExists;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.users.UserNotFound;
import xml.papersapp.model.review.TReview;
import xml.papersapp.model.review_assignment.TBlinded;
import xml.papersapp.model.review_assignment.TReviewAssignment;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.user.TUser;
import xml.papersapp.security.repository.UserRepository;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static xml.papersapp.constants.DocumentIDs.REVIEWS_ID_DOCUMENT;
import static xml.papersapp.constants.DocumentIDs.REVIEW_ASSIGNMENTS_ID_DOCUMENT;
import static xml.papersapp.constants.Files.SCHEME_REVIEW_ASSIGNMENT_PATH;
import static xml.papersapp.constants.Namespaces.*;
import static xml.papersapp.constants.Packages.REVIEW_ASSIGNMENT_PACKAGE;
import static xml.papersapp.util.XUpdateTemplate.APPEND;

@Repository
public class ReviewAssignmentRepository {

    private final XPathQueryService xPathQueryService;
    private final SciencePaperRepository sciencePaperRepository;
    private final UserRepository userRepository;
    private final XUpdateQueryService xUpdateQueryService;


    private final String CONTEXT_PATH_APPEND = "//ReviewAssignments";

    public ReviewAssignmentRepository(XPathQueryService xPathQueryService, SciencePaperRepository sciencePaperRepository
            , UserRepository userRepository, XUpdateQueryService xUpdateQueryService) {
        this.xPathQueryService = xPathQueryService;
        this.sciencePaperRepository = sciencePaperRepository;
        this.userRepository = userRepository;
        this.xUpdateQueryService = xUpdateQueryService;
    }

    public List<String> findTitlesOfPapersToReview(String email) throws XMLDBException, JAXBException, SAXException {

        xPathQueryService.setNamespace("ras", REVIEW_ASSIGNMENTS_NAMESPACE);
        xPathQueryService.setNamespace("ra", REVIEW_ASSIGNMENT_NAMESPACE);
        xPathQueryService.setNamespace("user", USER_NAMESPACE);

        String query = "//ras:ReviewAssignments/ra:ReviewAssignment[ra:Reviewer[user:email='" + email + "']]";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();

        List<String> titles = new ArrayList<>();

        while(i.hasMoreResources()) {
            titles.add(getTitleFromResource(i.nextResource().getContent().toString()));
        }

        return titles;

    }

    public String getTitleFromResource(String resource) throws JAXBException, XMLDBException, SAXException {
        JAXBContext context = JAXBContext.newInstance(REVIEW_ASSIGNMENT_PACKAGE);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        // XML schema validacija
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(SCHEME_REVIEW_ASSIGNMENT_PATH));

        // Podešavanje unmarshaller-a za XML schema validaciju
        unmarshaller.setSchema(schema);
//        TReviewAssignment reviewAssignment = (TReviewAssignment) unmarshaller.unmarshal(new StringReader(resource));

        JAXBElement<TReviewAssignment> jaxbElement = (JAXBElement<TReviewAssignment>) unmarshaller.unmarshal(new StringReader(resource));
        TReviewAssignment tReviewAssignment = jaxbElement.getValue();
        return tReviewAssignment.getSciencePaperTitle();

    }

    public List<SciencePaper> getPapersToReview(String email) throws XMLDBException, JAXBException, SAXException {
        List<String> titles = findTitlesOfPapersToReview(email);

        List<SciencePaper> papers = new ArrayList<SciencePaper>();

        for (String title : titles) {
            Optional<SciencePaper> sp = sciencePaperRepository.findOneByTitle(title);
            if(sp.isPresent()) {
                papers.add(sp.get());
            }
        }

        return papers;
    }

    public TReviewAssignment createReviewAssignment(String title, String email, TBlinded blinded) throws JAXBException,
            XMLDBException, SAXException, SciencePaperDoesntExist, UserNotFound, ReviewAssignmenAlreadyExists {

        Optional<TUser> user = userRepository.findByUsername(email);

        if(!user.isPresent()) {
            throw new UserNotFound();
        }

        Optional<SciencePaper> sciencePaper = sciencePaperRepository.findOneByTitle(title);

        if(!sciencePaper.isPresent()) {
            throw new SciencePaperDoesntExist();
        }

        Optional<String> foundTitle = checkIfPaperAssignedToReviewer(email, title);

        if(foundTitle.isPresent()) {
            throw new ReviewAssignmenAlreadyExists();
        }

        TReviewAssignment reviewAssignment = new TReviewAssignment(title, user.get(), blinded);

        OutputStream xml = getXMLFromObject(reviewAssignment, REVIEW_ASSIGNMENT_PACKAGE);

        long mods = xUpdateQueryService.updateResource(REVIEW_ASSIGNMENTS_ID_DOCUMENT, String.format(APPEND, REVIEW_ASSIGNMENTS_NAMESPACE, CONTEXT_PATH_APPEND, xml.toString()));
        System.out.println("[INFO] " + mods + " modifications processed.");

        return reviewAssignment;
    }

    /*
     *   duplicated
     * */
    private OutputStream getXMLFromObject(Object object, String pckg) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(pckg);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        OutputStream os = new ByteArrayOutputStream();

        marshaller.marshal(object, os);

        return os;

    }

    public Optional<String> checkIfPaperAssignedToReviewer(String email, String title) throws XMLDBException, JAXBException, SAXException {

        xPathQueryService.setNamespace("ras", REVIEW_ASSIGNMENTS_NAMESPACE);
        xPathQueryService.setNamespace("ra", REVIEW_ASSIGNMENT_NAMESPACE);
        xPathQueryService.setNamespace("user", USER_NAMESPACE);

        String query = "//ras:ReviewAssignments/ra:ReviewAssignment[ra:Reviewer[user:email='" + email + "'] and " +
                "ra:sciencePaperTitle='" + title + "']";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();
        Resource res = i.nextResource();

        return res != null ? Optional.of(getTitleFromResource(res.getContent().toString())) : Optional.empty();
    }

}