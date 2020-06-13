package xml.papersapp.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.model.review.TBlinded;
import xml.papersapp.model.review.TReview;
import xml.papersapp.repository.ReviewRepository;

import javax.xml.bind.JAXBException;

import static xml.papersapp.constants.Namespaces.REVIEW_NAMESPACE;
import static xml.papersapp.util.Util.createId;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public TReview create(String resource) throws XMLDBException, JAXBException, SAXException {

        // validate xml
        TReview review = reviewRepository.getReviewFromResource(resource);

        String id = createId(REVIEW_NAMESPACE);
        review.setId(id);
//        if (review.getBlinded().equals(TBlinded.STANDARD)) {
//            System.out.println("");
//
//        }

        /*
        * Todo:
        *  get authors and reviewers by email and put them in review if needed
        */

        return review;
    }
}
