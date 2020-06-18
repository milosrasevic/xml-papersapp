package xml.papersapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.exceptions.review.ReviewAssignmenAlreadyExists;
import xml.papersapp.exceptions.review.ReviewAssignmentNotFound;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.users.UserNotFound;
import xml.papersapp.model.review.TReview;
import xml.papersapp.model.review_assignment.TBlinded;
import xml.papersapp.model.review_assignment.TReviewAssignment;
import xml.papersapp.service.review.ReviewService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping()
    public ResponseEntity create(@RequestBody TReview resource) {
        try {
            return new ResponseEntity<>(reviewService.createFromObject(resource), HttpStatus.OK);
        } catch (XMLDBException | JAXBException | SAXException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/assign-a-reviewer")
    public ResponseEntity<?> assignAReviewer(@RequestParam("title") String title, @RequestParam("email") String email,
                                             @RequestParam("blinded") TBlinded blinded) {

        try {
            TReviewAssignment reviewAssignment = reviewService.createReviewAssignment(title, email, blinded);
            return new ResponseEntity<TReviewAssignment>(reviewAssignment, HttpStatus.OK);
        } catch (SciencePaperDoesntExist | UserNotFound | ReviewAssignmenAlreadyExists  e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (SAXException | XMLDBException | JAXBException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/accept-review")
    public ResponseEntity<?> acceptAReview(HttpServletRequest request, @RequestParam("assignmentId") String assignmentId) {
        try {
            String email = request.getUserPrincipal().getName();
            TReviewAssignment reviewAssignment = reviewService.acceptReviewAssignment(assignmentId, email);
            return new ResponseEntity<TReviewAssignment>(reviewAssignment, HttpStatus.OK);
        } catch (XMLDBException | SAXException | JAXBException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ReviewAssignmentNotFound reviewAssignmentNotFound) {
            return new ResponseEntity<String>(reviewAssignmentNotFound.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
