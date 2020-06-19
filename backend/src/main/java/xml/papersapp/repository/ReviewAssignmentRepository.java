package xml.papersapp.repository;

import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import xml.papersapp.exceptions.review.ReviewAssignmenAlreadyExists;
import xml.papersapp.exceptions.review.ReviewAssignmentCantBeCreated;
import xml.papersapp.exceptions.review.ReviewAssignmentNotFound;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.sciencePapers.SciencePaperNotFound;
import xml.papersapp.exceptions.users.UserNotFound;
import xml.papersapp.model.notification.TNotification;
import xml.papersapp.model.review.TReview;
import xml.papersapp.model.review_assignment.TBlinded;
import xml.papersapp.model.review_assignment.TReviewAssignment;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_paper.TState;
import xml.papersapp.model.user.TUser;
import xml.papersapp.security.repository.UserRepository;
import xml.papersapp.service.mail.AssigmentEmailService;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static xml.papersapp.constants.DocumentIDs.REVIEW_ASSIGNMENTS_ID_DOCUMENT;
import static xml.papersapp.constants.Files.SCHEME_REVIEW_ASSIGNMENT_PATH;
import static xml.papersapp.constants.Namespaces.*;
import static xml.papersapp.constants.Packages.REVIEW_ASSIGNMENT_PACKAGE;
import static xml.papersapp.util.Util.createId;
import static xml.papersapp.util.XUpdateTemplate.APPEND;
import static xml.papersapp.util.XUpdateTemplate.REMOVE;

@Repository
public class ReviewAssignmentRepository {

    private final XPathQueryService xPathQueryService;
    private final SciencePaperRepository sciencePaperRepository;
    private final UserRepository userRepository;
    private final XUpdateQueryService xUpdateQueryService;

    private final AssigmentEmailService assigmentEmailService;
    private final NotificationRepository notificationRepository;


    private final String CONTEXT_PATH_APPEND = "//ReviewAssignments";

    public ReviewAssignmentRepository(XPathQueryService xPathQueryService, SciencePaperRepository sciencePaperRepository
            , UserRepository userRepository, XUpdateQueryService xUpdateQueryService,
                                      AssigmentEmailService assigmentEmailService, NotificationRepository notificationRepository) {
        this.xPathQueryService = xPathQueryService;
        this.sciencePaperRepository = sciencePaperRepository;
        this.userRepository = userRepository;
        this.xUpdateQueryService = xUpdateQueryService;
        this.assigmentEmailService = assigmentEmailService;
        this.notificationRepository = notificationRepository;
    }

    public List<String> findTitlesOfPapersToReview(String email) throws XMLDBException, JAXBException, SAXException {

        xPathQueryService.setNamespace("ras", REVIEW_ASSIGNMENTS_NAMESPACE);
        xPathQueryService.setNamespace("ra", REVIEW_ASSIGNMENT_NAMESPACE);
        xPathQueryService.setNamespace("user", USER_NAMESPACE);

//        String query = "//ras:ReviewAssignments/ra:ReviewAssignment[ra:Reviewer[user:email='" + email + "'] and " + "@accepted=true]";
        String query = "//ras:ReviewAssignments/ra:ReviewAssignment[ra:Reviewer[user:email='" + email + "']]";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();

        List<String> titles = new ArrayList<>();

        while (i.hasMoreResources()) {
            TReviewAssignment reviewAssignment = getReviewAssignmentFromResource(i.nextResource().getContent().toString());
            if (reviewAssignment.isAccepted() != null) {
                if (reviewAssignment.isAccepted()) {
                    titles.add(reviewAssignment.getSciencePaperTitle());
                }
            }
        }

        return titles;

    }

    public TReviewAssignment getReviewAssignmentFromResource(String resource) throws JAXBException, XMLDBException, SAXException {
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
        return tReviewAssignment;

    }

    public List<SciencePaper> getPapersToReview(String email) throws XMLDBException, JAXBException, SAXException {
        List<String> titles = findTitlesOfPapersToReview(email);

        List<SciencePaper> papers = new ArrayList<SciencePaper>();

        for (String title : titles) {
            Optional<SciencePaper> sp = sciencePaperRepository.findOneByTitle(title);
            if (sp.isPresent()) {
                papers.add(sp.get());
            }
        }

        return papers;
    }

    public TReviewAssignment createReviewAssignment(String title, String email, TBlinded blinded) throws JAXBException,
            XMLDBException, SAXException, SciencePaperDoesntExist, UserNotFound, ReviewAssignmenAlreadyExists, IOException, ReviewAssignmentCantBeCreated {

        Optional<TUser> user = userRepository.findByUsername(email);

        if (!user.isPresent()) {
            throw new UserNotFound();
        }

        Optional<TReviewAssignment> foundAssignment = checkIfPaperAssignedToReviewer(email, title);

        if (foundAssignment.isPresent()) {
            throw new ReviewAssignmenAlreadyExists();
        }

        TReviewAssignment reviewAssignment = new TReviewAssignment(title, user.get(), blinded, createId(REVIEW_ASSIGNMENT_NAMESPACE));

        OutputStream xml = getXMLFromObject(reviewAssignment, REVIEW_ASSIGNMENT_PACKAGE);

        long mods = xUpdateQueryService.updateResource(REVIEW_ASSIGNMENTS_ID_DOCUMENT, String.format(APPEND, REVIEW_ASSIGNMENTS_NAMESPACE, CONTEXT_PATH_APPEND, xml.toString()));
        System.out.println("[INFO] " + mods + " modifications processed.");

        assigmentEmailService.sendEmailForPurchasedTickets(user.get().getEmail(), title);
        TNotification notification = new TNotification();
        notification.setReviewerEmail(user.get().getEmail());
        notification.setSciencePaperTitle(title);
        notification.setContent("Review assigment");
        notificationRepository.save(notification);

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

    public Optional<TReviewAssignment> checkIfPaperAssignedToReviewer(String email, String title) throws XMLDBException, JAXBException, SAXException {

        xPathQueryService.setNamespace("ras", REVIEW_ASSIGNMENTS_NAMESPACE);
        xPathQueryService.setNamespace("ra", REVIEW_ASSIGNMENT_NAMESPACE);
        xPathQueryService.setNamespace("user", USER_NAMESPACE);

        String query = "//ras:ReviewAssignments/ra:ReviewAssignment[ra:Reviewer[user:email='" + email + "'] and " +
                "ra:sciencePaperTitle='" + title + "']";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();
        Resource res = i.nextResource();

        return res != null ? Optional.of(getReviewAssignmentFromResource(res.getContent().toString())) : Optional.empty();
    }

    public Optional<TReviewAssignment> findAssignmentByIdAndEmail(String id, String email) throws XMLDBException,
            JAXBException, SAXException {

        xPathQueryService.setNamespace("ras", REVIEW_ASSIGNMENTS_NAMESPACE);
        xPathQueryService.setNamespace("ra", REVIEW_ASSIGNMENT_NAMESPACE);

        String query = "//ras:ReviewAssignments/ra:ReviewAssignment[@Id='" + id + "' and ra:Reviewer[user:email='"
                + email + "']]";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();
        Resource res = i.nextResource();

        return res != null ? Optional.of(getReviewAssignmentFromResource(res.getContent().toString())) : Optional.empty();

    }

    public TReviewAssignment saveAssignment(TReviewAssignment assignment) throws XMLDBException, JAXBException, SAXException, ReviewAssignmentNotFound {
        OutputStream xml = getXMLFromObject(assignment, REVIEW_ASSIGNMENT_PACKAGE);
        String xmlString = xml.toString();

//        List<TReviewAssignment> allAssignments = getAllAssignments();

//        int index = -1;
//        int counter = 0;
//        for (TReviewAssignment reviewAssignment : allAssignments) {
//            if (reviewAssignment.getId().equalsIgnoreCase(assignment.getId()) &&
//                    reviewAssignment.getReviewer().getEmail().equalsIgnoreCase(assignment.getReviewer().getEmail())) {
//                index = counter;
//            }
//            counter++;
//        }
////
        String query = "//ras:ReviewAssignments/ra:ReviewAssignment[@Id='" + assignment.getId() + "' and ra:Reviewer[user:email='"
                + assignment.getReviewer().getEmail() + "']]";

//        if (index == -1) {
//            throw new ReviewAssignmentNotFound();
//        }
//
//        String contextPath = "//ReviewAssignments/ReviewAssignment[" + index + "]";
//
//
//        long mods = xUpdateQueryService.updateResource(REVIEW_ASSIGNMENTS_ID_DOCUMENT, String.format(UPDATE,
//                REVIEW_ASSIGNMENTS_NAMESPACE, contextPath, xmlString));

        long mods = xUpdateQueryService.updateResource(REVIEW_ASSIGNMENTS_ID_DOCUMENT, String.format(REMOVE,
                REVIEW_ASSIGNMENTS_NAMESPACE, query));
        System.out.println("[INFO] " + mods + " modifications processed.");

        long mods1 = xUpdateQueryService.updateResource(REVIEW_ASSIGNMENTS_ID_DOCUMENT, String.format(APPEND,
                REVIEW_ASSIGNMENTS_NAMESPACE, CONTEXT_PATH_APPEND, xmlString));
        System.out.println("[INFO] " + mods1 + " modifications processed.");


        return assignment;
    }

    public List<TReviewAssignment> getMyAssignments(String email) throws XMLDBException, JAXBException, SAXException {

        xPathQueryService.setNamespace("ras", REVIEW_ASSIGNMENTS_NAMESPACE);
        xPathQueryService.setNamespace("ra", REVIEW_ASSIGNMENT_NAMESPACE);

        String query = "//ras:ReviewAssignments/ra:ReviewAssignment";
        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();

        List<TReviewAssignment> reviewAssignments = new ArrayList<>();

        while (i.hasMoreResources()) {
            TReviewAssignment reviewAssignment = getReviewAssignmentFromResource(i.nextResource().getContent().toString());
            if (reviewAssignment.getReviewer().getEmail().equals(email) && reviewAssignment.isAccepted() == null) {
                reviewAssignments.add(reviewAssignment);
            }
        }

        return reviewAssignments;
    }

    public TReviewAssignment delete(String title, String email) throws XMLDBException, SciencePaperNotFound, JAXBException, SAXException {

        xPathQueryService.setNamespace("ras", REVIEW_ASSIGNMENTS_NAMESPACE);
        xPathQueryService.setNamespace("ra", REVIEW_ASSIGNMENT_NAMESPACE);

        String query = "//ras:ReviewAssignments/ra:ReviewAssignment[ra:sciencePaperTitle='" + title + "' and ra:Reviewer[user:email='"
                + email + "']]";
        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();
        Resource res = i.nextResource();

        if (res == null) {
            throw new SciencePaperNotFound();
        }

        String resourceString = res.getContent().toString();

        long mods = xUpdateQueryService.updateResource(REVIEW_ASSIGNMENTS_ID_DOCUMENT, String.format(REMOVE,
                REVIEW_ASSIGNMENT_NAMESPACE, query));
        System.out.println("[INFO] " + mods + " modifications processed.");

        return getReviewAssignmentFromResource(resourceString);
    }

    public List<TReviewAssignment> getBySciencePaperTitle(String title) throws XMLDBException, JAXBException, SAXException {

        xPathQueryService.setNamespace("ras", REVIEW_ASSIGNMENTS_NAMESPACE);
        xPathQueryService.setNamespace("ra", REVIEW_ASSIGNMENT_NAMESPACE);

        String query = "//ras:ReviewAssignments/ra:ReviewAssignment";
        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();

        List<TReviewAssignment> reviewAssignments = new ArrayList<>();

        while (i.hasMoreResources()) {
            TReviewAssignment reviewAssignment = getReviewAssignmentFromResource(i.nextResource().getContent().toString());
            if (reviewAssignment.getSciencePaperTitle().equals(title)) {
                reviewAssignments.add(reviewAssignment);
            }
        }

        return reviewAssignments;
    }

    public List<TReviewAssignment> getReviewAssignementsForSciencePaperId(String paperTitle) throws XMLDBException, JAXBException, SAXException {

        xPathQueryService.setNamespace("ras", REVIEW_ASSIGNMENTS_NAMESPACE);
        xPathQueryService.setNamespace("ra", REVIEW_ASSIGNMENT_NAMESPACE);

        String query = "//ras:ReviewAssignments/ra:ReviewAssignment[ra:sciencePaperTitle='" + paperTitle + "']";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();

        List<TReviewAssignment> reviewAssignments = new ArrayList<>();

        while (i.hasMoreResources()) {
            reviewAssignments.add(getReviewAssignmentFromResource(i.nextResource().getContent().toString()));
        }

        return reviewAssignments;

    }
}
