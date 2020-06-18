package xml.papersapp.service.review;

import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.exceptions.review.ReviewAssignmenAlreadyExists;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.users.UserNotFound;
import xml.papersapp.model.review.TReview;
import xml.papersapp.model.review_assignment.TBlinded;
import xml.papersapp.model.review_assignment.TReviewAssignment;

import javax.xml.bind.JAXBException;

public interface ReviewService {
    /**
     * Method used for submitting editors review on some scientific paper.
     *
     * @param resource -   String representation of paper review
     * @return -   Review object representation of created review
     */
    TReview create(String resource) throws XMLDBException, JAXBException, SAXException;
    TReviewAssignment createReviewAssignment(String title, String email, TBlinded blinded) throws XMLDBException,
            SAXException, SciencePaperDoesntExist, JAXBException, UserNotFound, ReviewAssignmenAlreadyExists;
}
