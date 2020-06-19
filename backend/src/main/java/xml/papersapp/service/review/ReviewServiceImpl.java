package xml.papersapp.service.review;

import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.exceptions.review.*;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.sciencePapers.SciencePaperNotFound;
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
import xml.papersapp.util.XSLFOTransformer;

import javax.xml.bind.JAXBException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.List;
import java.util.Calendar;
import java.util.Optional;
import java.util.List;

import java.io.IOException;

import static xml.papersapp.constants.Namespaces.REVIEW_NAMESPACE;
import static xml.papersapp.constants.Namespaces.SCIENCE_PAPER_NAMESPACE;
import static xml.papersapp.constants.Packages.REVIEW_PACKAGE;
import static xml.papersapp.util.Util.createId;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UsersRepository usersRepository;
    private final ReviewAssignmentRepository reviewAssignmentRepository;
    private final XSLFOTransformer xslfoTransformer;
    private final SciencePaperRepository sciencePaperRepository;


    @Override
    public TReview createFromObject(TReview review) throws XMLDBException, JAXBException, SAXException, SciencePaperDoesntExist, SciencePaperNotFound {

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
        review.setDateCreated(Calendar.getInstance().getTime());



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

        // create Review
        reviewRepository.save(review);
        SciencePaper sciencePaper = sciencePaperRepository.findOneByDocumentId(review.getSciencePaperId())
                .orElseThrow(SciencePaperDoesntExist::new);

        // delete ReviewAssigment
        reviewAssignmentRepository.delete(sciencePaper.getTitle(), review.getReviewer().getEmail());

        // change science paper status
        checkSciencePaperStatus(sciencePaper);

        return review;
    }

    private void checkSciencePaperStatus(SciencePaper sciencePaper) throws XMLDBException, JAXBException, SAXException {
        List<TReviewAssignment> reviewAssignmentList = reviewAssignmentRepository.getBySciencePaperTitle(sciencePaper.getTitle());

        if(reviewAssignmentList.size() == 0) {
            // all reviews are done
            sciencePaper.setState(TState.WAITING_FOR_APPROVAL);
        } else {
            // not all reviews are done
            boolean allAreRejected = true;
            for (TReviewAssignment reviewAssigment: reviewAssignmentList) {
                // check if some of reviews are pending or accepted
                if (reviewAssigment.isAccepted() == null || reviewAssigment.isAccepted() == true) {
                    allAreRejected = false;
                }
            }

            if(allAreRejected) {
                // all remaining reviewers have rejected
                sciencePaper.setState(TState.WAITING_FOR_APPROVAL);
            }
        }

        sciencePaperRepository.update(sciencePaper);

    }

    @Override
    public TReviewAssignment createReviewAssignment(String title, String email, TBlinded blinded) throws XMLDBException,
            SAXException, SciencePaperDoesntExist, JAXBException, UserNotFound, ReviewAssignmenAlreadyExists, IOException {

        return reviewAssignmentRepository.createReviewAssignment(title, email, blinded);

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

        // if false, i nema vise assigmenta koji su null ili true, ide na waiting
        assignment.setAccepted(accept);

        return reviewAssignmentRepository.saveAssignment(assignment);

    }

    private void checkSciencePaperStatus(String title) throws XMLDBException, JAXBException, SAXException, SciencePaperDoesntExist {
        SciencePaper sciencePaper = sciencePaperRepository.findOneByTitle(title).orElseThrow(SciencePaperDoesntExist::new);


        List<TReviewAssignment> reviewAssignmentList = reviewAssignmentRepository.getBySciencePaperTitle(sciencePaper.getTitle());

        if(reviewAssignmentList.size() == 0) {
            // all reviews are done
            sciencePaper.setState(TState.WAITING_FOR_APPROVAL);
        } else {
            // not all reviews are done
            boolean allAreRejected = true;
            for (TReviewAssignment reviewAssigment: reviewAssignmentList) {
                // check if some of reviews are pending or accepted
                if (reviewAssigment.isAccepted() == null || reviewAssigment.isAccepted() == true) {
                    allAreRejected = false;
                }
            }

            if(allAreRejected) {
                // all remaining reviewers have rejected
                sciencePaper.setState(TState.WAITING);
            }
        }

        sciencePaperRepository.update(sciencePaper);


    }

    @Override
    public List<TReview> getReviews() throws XMLDBException, JAXBException, SAXException {

        return reviewRepository.getReviews();
    }

    @Override
    public List<TReview> getReviewsForSciencePaperId(String paperId) throws XMLDBException, JAXBException, SAXException {

        paperId = SCIENCE_PAPER_NAMESPACE + "/" + paperId;
        return reviewRepository.getReviewsForSciencePaperId(paperId);
    }

    @Override
    public List<TReviewAssignment> getReviewAssignementsForSciencePaperId(String paperTitle) throws XMLDBException, JAXBException, SAXException {

        return reviewAssignmentRepository.getReviewAssignementsForSciencePaperId(paperTitle);
    }

    public ByteArrayOutputStream generateHTML(String id) throws JAXBException, XMLDBException, SAXException, ReviewDoesntExist, FileNotFoundException {

        id = REVIEW_NAMESPACE +  "/" + id;

        System.out.println(id);
        TReview found = reviewRepository.findOneById(id).orElseThrow(ReviewDoesntExist::new);

        OutputStream outputStream = reviewRepository.getXMLFromObject(found, REVIEW_PACKAGE);
        String xslPath = "src/main/resources/xsl/review_html.xsl";
        return xslfoTransformer.generateHTML(outputStream, xslPath);

    }

    public ByteArrayOutputStream generatePDF(String id) throws XMLDBException, SAXException, ReviewDoesntExist, JAXBException, IOException, DocumentException {

        ByteArrayOutputStream html = generateHTML(id);

        return xslfoTransformer.generatePDF(html);

    }

    public ByteArrayOutputStream generateXML(String id) throws XMLDBException, JAXBException, SAXException, ReviewDoesntExist {

        id = REVIEW_NAMESPACE +  "/" + id;

        System.out.println(id);
        TReview found = reviewRepository.findOneById(id).orElseThrow(ReviewDoesntExist::new);

        OutputStream outputStream = reviewRepository.getXMLFromObject(found, REVIEW_PACKAGE);

        return (ByteArrayOutputStream) outputStream;

    }
}
