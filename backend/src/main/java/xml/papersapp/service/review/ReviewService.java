package xml.papersapp.service.review;

import com.itextpdf.text.DocumentException;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.dto.reviewAssignment.ReviewAssignmentsDTO;
import xml.papersapp.exceptions.review.*;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.sciencePapers.SciencePaperNotFound;
import xml.papersapp.exceptions.users.UserNotFound;
import xml.papersapp.model.review.TReview;
import xml.papersapp.model.review_assignment.TBlinded;
import xml.papersapp.model.review_assignment.TReviewAssignment;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ReviewService {
    /**
     * Method used for submitting editors review on some scientific paper.
     *
     * @param resource -   String representation of paper review
     * @return -   Review object representation of created review
     */
    TReview createFromObject(TReview resource) throws XMLDBException, JAXBException, SAXException, SciencePaperDoesntExist, SciencePaperNotFound;

    List<TReviewAssignment> createReviewAssignment(ReviewAssignmentsDTO reviewAssignmentsDTO) throws XMLDBException,
            SAXException, SciencePaperDoesntExist, JAXBException, UserNotFound, ReviewAssignmenAlreadyExists, IOException, ReviewAssignmentCantBeCreated;

    TReviewAssignment acceptReviewAssignment(String assignmentId, String email, boolean accept) throws XMLDBException,
            JAXBException, SAXException, ReviewAssignmentNotFound, ReviewAssignmentAlreadyAccepted, ReviewAssignmentAlreadyDenied, SciencePaperDoesntExist;

    List<TReview> getReviews() throws XMLDBException, JAXBException, SAXException;

    ByteArrayOutputStream generateHTML(String id) throws JAXBException, XMLDBException, SAXException, ReviewDoesntExist, FileNotFoundException;

    ByteArrayOutputStream generatePDF(String id) throws XMLDBException, JAXBException, SAXException, ReviewDoesntExist, IOException, DocumentException;

    ByteArrayOutputStream generateXML(String id) throws ReviewDoesntExist, XMLDBException, JAXBException, SAXException;

    List<TReview> getReviewsForSciencePaperId(String paperId) throws XMLDBException, JAXBException, SAXException;

    List<TReviewAssignment> getReviewAssignementsForSciencePaperId(String paperId) throws XMLDBException, JAXBException, SAXException;
}
