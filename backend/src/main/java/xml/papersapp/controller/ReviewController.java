package xml.papersapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.dto.reviewAssignment.ReviewAssignmentsDTO;
import xml.papersapp.exceptions.review.*;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.users.UserNotFound;
import xml.papersapp.model.review.TReview;
import xml.papersapp.model.review_assignment.TBlinded;
import xml.papersapp.model.review_assignment.TReviewAssignment;
import xml.papersapp.service.review.ReviewService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

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

    @PostMapping(value = "/assign-reviewers")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<?> assignReviewers(@RequestBody ReviewAssignmentsDTO reviewAssignmentsDTO) {

        try {
            List<TReviewAssignment> reviewAssignments = reviewService.createReviewAssignment(reviewAssignmentsDTO);
            return new ResponseEntity<List<TReviewAssignment>>(reviewAssignments, HttpStatus.OK);
        } catch (SciencePaperDoesntExist | UserNotFound | ReviewAssignmenAlreadyExists | ReviewAssignmentCantBeCreated  e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (SAXException | XMLDBException | JAXBException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/accept-review")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<?> acceptAReview(HttpServletRequest request, @RequestParam("assignmentId") String assignmentId,
                                           @RequestParam("accept") boolean accept) {
        try {
            String email = request.getUserPrincipal().getName();
            TReviewAssignment reviewAssignment = reviewService.acceptReviewAssignment(assignmentId, email, accept);
            return new ResponseEntity<TReviewAssignment>(reviewAssignment, HttpStatus.OK);
        } catch (XMLDBException | SAXException | JAXBException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ReviewAssignmentNotFound | ReviewAssignmentAlreadyAccepted | ReviewAssignmentAlreadyDenied e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<?> getReviews() {
        try {
            List<TReview> reviews = reviewService.getReviews();
            return new ResponseEntity<List<TReview>>(reviews, HttpStatus.OK);
        } catch (XMLDBException | SAXException | JAXBException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
