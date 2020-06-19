package xml.papersapp.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.dto.reviewAssignment.ReviewAssignmentDTO;
import xml.papersapp.dto.reviewAssignment.ReviewAssignmentsDTO;
import xml.papersapp.exceptions.review.*;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.users.UserNotFound;
import xml.papersapp.model.review.*;
import xml.papersapp.model.review_assignment.TBlinded;
import xml.papersapp.model.review_assignment.TReviewAssignment;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_paper.TState;
import xml.papersapp.model.user.TUser;
import xml.papersapp.repository.ReviewAssignmentRepository;
import xml.papersapp.repository.ReviewRepository;
import xml.papersapp.repository.SciencePaperRepository;
import xml.papersapp.repository.UsersRepository;

import javax.xml.bind.JAXBException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.io.IOException;

import static xml.papersapp.constants.Namespaces.REVIEW_NAMESPACE;
import static xml.papersapp.util.Util.createId;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UsersRepository usersRepository;
    private final ReviewAssignmentRepository reviewAssignmentRepository;
    private final SciencePaperRepository sciencePaperRepository;

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
    public List<TReviewAssignment> createReviewAssignment(ReviewAssignmentsDTO reviewAssignmentsDTO) throws XMLDBException,
            SAXException, SciencePaperDoesntExist, JAXBException, UserNotFound, ReviewAssignmenAlreadyExists, IOException, ReviewAssignmentCantBeCreated {

        Optional<SciencePaper> sciencePaper = sciencePaperRepository.findOneByTitle(reviewAssignmentsDTO.getTitle());

        if (!sciencePaper.isPresent()) {
            throw new SciencePaperDoesntExist();
        }

        SciencePaper paper = sciencePaper.get();

        if (!paper.getState().equals(TState.WAITING)) {
            throw new ReviewAssignmentCantBeCreated();
        }

        List<TReviewAssignment> assignments = new ArrayList<>();

        for (ReviewAssignmentDTO reviewAssignmentDTO : reviewAssignmentsDTO.getReviewAssignmentDTO()) {
            assignments.add(reviewAssignmentRepository.createReviewAssignment(reviewAssignmentsDTO.getTitle(),
                    reviewAssignmentDTO.getEmail(), reviewAssignmentDTO.getBlinded()));
        }

        paper.setState(TState.REVIEW_IN_PROGRESS);

        sciencePaperRepository.update(paper);

        return assignments;
    }

    @Override
    public TReviewAssignment acceptReviewAssignment(String assignmentId, String email, boolean accept) throws XMLDBException, JAXBException, SAXException,
            ReviewAssignmentNotFound, ReviewAssignmentAlreadyAccepted, ReviewAssignmentAlreadyDenied {

        Optional<TReviewAssignment> assignmentOptional = reviewAssignmentRepository.findAssignmentByIdAndEmail(assignmentId, email);

        if(!assignmentOptional.isPresent()) {
            throw new ReviewAssignmentNotFound();
        }

        TReviewAssignment assignment = assignmentOptional.get();

        if (assignment.isAccepted() != null) {
            if(assignment.isAccepted()) {
                throw new ReviewAssignmentAlreadyAccepted();
            }

            if(!assignment.isAccepted()) {
                throw new ReviewAssignmentAlreadyDenied();
            }
        }

        assignment.setAccepted(accept);

        return reviewAssignmentRepository.saveAssignment(assignment);

    }

    @Override
    public List<TReview> getReviews() throws XMLDBException, JAXBException, SAXException {

        return reviewRepository.getReviews();

    }
}
