package xml.papersapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.model.review.TReview;
import xml.papersapp.service.review.ReviewService;

import javax.xml.bind.JAXBException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

//    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE})
//    public ResponseEntity create(@RequestBody String resource) {
//        try {
//            return new ResponseEntity(reviewService.create(resource), HttpStatus.OK);
//        } catch (XMLDBException | JAXBException | SAXException e) {
//            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping()
    public ResponseEntity create(@RequestBody TReview resource) {
        try {
            return new ResponseEntity<>(reviewService.createFromObject(resource), HttpStatus.OK);
        } catch (XMLDBException | JAXBException | SAXException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
