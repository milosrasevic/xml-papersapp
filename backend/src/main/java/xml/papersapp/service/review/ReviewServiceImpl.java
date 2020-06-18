package xml.papersapp.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.exceptions.review.ReviewAssignmenAlreadyExists;
import xml.papersapp.exceptions.review.ReviewAssignmentNotFound;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.users.UserNotFound;
import xml.papersapp.model.review.*;
import xml.papersapp.model.review_assignment.TBlinded;
import xml.papersapp.model.review_assignment.TReviewAssignment;
import xml.papersapp.model.user.TUser;
import xml.papersapp.repository.ReviewAssignmentRepository;
import xml.papersapp.repository.ReviewRepository;
import xml.papersapp.repository.UsersRepository;

import javax.xml.bind.JAXBException;

import java.util.Optional;

import static xml.papersapp.constants.Namespaces.REVIEW_ASSIGNMENT_NAMESPACE;
import static xml.papersapp.constants.Namespaces.REVIEW_NAMESPACE;
import static xml.papersapp.util.Util.createId;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UsersRepository usersRepository;
    private final ReviewAssignmentRepository reviewAssignmentRepository;


    @Override
    public TReview createFromObject(TReview review) throws XMLDBException, JAXBException, SAXException {

        String id = createId(REVIEW_NAMESPACE);
        review.setId(id);

        TAuthors authors = new TAuthors();
        for (TAuthor tAuthor: review.getAuthors().getAuthor()) {
            // find that author in database
            TUser foundUser = usersRepository.findUserByEmail(tAuthor.getEmail());
            TAuthor foundAuthor = new TAuthor();
            foundAuthor.setEmail(foundUser.getEmail());
            foundAuthor.setFirstName(foundUser.getFirstName());
            foundAuthor.setLastName(foundUser.getLastName());

            authors.getAuthor().add(foundAuthor); // append found author
        }


        TUser foundUser = usersRepository.findUserByEmail(review.getReviewer().getEmail());
        TReviewer foundReviewer = new TReviewer();
        foundReviewer.setEmail(foundUser.getEmail());
        foundReviewer.setFirstName(foundUser.getFirstName());
        foundReviewer.setLastName(foundUser.getLastName());


        review.setAuthors(authors);
        review.setReviewer(foundReviewer);



        switch (review.getBlinded()) {
            case STANDARD:
                System.out.println("Review is standard");
                break;

            case AUTHOR_BLINDED:
                review.setReviewer(null);
                break;

            case REVIEWER_BLINDED:
                review.setAuthors(null);
                break;

            case DOUBLE_BLINDED:
                review.setAuthors(null);
                review.setReviewer(null);
                break;

            default:
                break;
        }

        reviewRepository.save(review);

        return review;
    }

    @Override
    public TReviewAssignment createReviewAssignment(String title, String email, TBlinded blinded) throws XMLDBException,
            SAXException, SciencePaperDoesntExist, JAXBException, UserNotFound, ReviewAssignmenAlreadyExists {

        return reviewAssignmentRepository.createReviewAssignment(title, email, blinded);

    }

    @Override
    public TReviewAssignment acceptReviewAssignment(String assignmentId, String email) throws XMLDBException, JAXBException, SAXException,
            ReviewAssignmentNotFound {

        Optional<TReviewAssignment> assignmentOptional = reviewAssignmentRepository.findAssignmentByIdAndEmail(assignmentId, email);

        if(!assignmentOptional.isPresent()) {
            throw new ReviewAssignmentNotFound();
        }

        TReviewAssignment assignment = assignmentOptional.get();

        assignment.setAccepted(true);

        return reviewAssignmentRepository.saveAssignment(assignment);

    }
}
