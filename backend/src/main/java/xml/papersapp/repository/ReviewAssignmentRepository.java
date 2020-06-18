package xml.papersapp.repository;

import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import xml.papersapp.model.review.TReview;
import xml.papersapp.model.review_assignment.TReviewAssignment;
import xml.papersapp.model.science_paper.SciencePaper;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static xml.papersapp.constants.Files.SCHEME_REVIEW_ASSIGNMENT_PATH;
import static xml.papersapp.constants.Namespaces.*;
import static xml.papersapp.constants.Packages.REVIEW_ASSIGNMENT_PACKAGE;

@Repository
public class ReviewAssignmentRepository {

    private final XPathQueryService xPathQueryService;
    private final SciencePaperRepository sciencePaperRepository;

    private final String CONTEXT_PATH_APPEND = "//ReviewAssignments";

    public ReviewAssignmentRepository(XPathQueryService xPathQueryService, SciencePaperRepository sciencePaperRepository) {
        this.xPathQueryService = xPathQueryService;
        this.sciencePaperRepository = sciencePaperRepository;
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
}
