package xml.papersapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.exceptions.review.*;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.sciencePapers.SciencePaperNotFound;
import xml.papersapp.exceptions.users.UserNotFound;
import xml.papersapp.model.review.TReview;
import xml.papersapp.model.review_assignment.TBlinded;
import xml.papersapp.model.review_assignment.TReviewAssignment;
import xml.papersapp.service.review.ReviewService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        } catch (XMLDBException | JAXBException | SAXException | SciencePaperNotFound | SciencePaperDoesntExist e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/assign-a-reviewer")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<?> assignAReviewer(@RequestParam("title") String title, @RequestParam("email") String email,
                                             @RequestParam("blinded") TBlinded blinded) {

        try {
            TReviewAssignment reviewAssignment = reviewService.createReviewAssignment(title, email, blinded);
            return new ResponseEntity<TReviewAssignment>(reviewAssignment, HttpStatus.OK);
        } catch (SciencePaperDoesntExist | UserNotFound | ReviewAssignmenAlreadyExists  e) {
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

    @GetMapping(value = "/science-paper/{paperId}")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<?> getReviewsForSciencePaperId(@PathVariable String paperId) {
        try {
            List<TReview> reviews = reviewService.getReviewsForSciencePaperId(paperId);
            return new ResponseEntity<List<TReview>>(reviews, HttpStatus.OK);
        } catch (XMLDBException | SAXException | JAXBException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/assignments/science-paper/{paperTitle}")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<?> getReviewAssignementsForSciencePaperId(@PathVariable String paperTitle) {
        try {
            List<TReviewAssignment> reviews = reviewService.getReviewAssignementsForSciencePaperId(paperTitle);
            return new ResponseEntity<List<TReviewAssignment>>(reviews, HttpStatus.OK);
        } catch (XMLDBException | SAXException | JAXBException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "getHTML/{id}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity<?> generateHTML(@PathVariable String id) {

        try {
            ByteArrayOutputStream out = reviewService.generateHTML(id);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
            return ResponseEntity.ok()
                    .contentLength(out.size())
                    .contentType(MediaType.parseMediaType("application/xhtml+xml"))
                    .body(resource);
        } catch (ReviewDoesntExist r) {
            r.printStackTrace();
            return new ResponseEntity<>(r.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "getPDF/{id}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity<?> generatePDF(@PathVariable String id) {


        try {
            ByteArrayOutputStream out = reviewService.generatePDF(id);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
            return ResponseEntity.ok()
                    .contentLength(out.size())
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .body(resource);
        } catch (ReviewDoesntExist r) {
            r.printStackTrace();
            return new ResponseEntity<>(r.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(path = "getXML/{id}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity<?> generateXML(@PathVariable String id) {

        try {
            ByteArrayOutputStream out = reviewService.generateXML(id);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
            return ResponseEntity.ok()
                    .contentLength(out.size())
                    .contentType(MediaType.parseMediaType("application/xml"))
                    .body(resource);
        } catch (ReviewDoesntExist r) {
            r.printStackTrace();
            return new ResponseEntity<>(r.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
