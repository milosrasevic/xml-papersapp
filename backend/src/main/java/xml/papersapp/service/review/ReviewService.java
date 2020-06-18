package xml.papersapp.service.review;

import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.model.review.TReview;

import javax.xml.bind.JAXBException;

public interface ReviewService {
    /**
     * Method used for submitting editors review on some scientific paper.
     *
     * @param resource -   String representation of paper review
     * @return -   Review object representation of created review
     */
    TReview create(String resource) throws XMLDBException, JAXBException, SAXException;

    TReview createFromObject(TReview resource) throws XMLDBException, JAXBException, SAXException;
}
