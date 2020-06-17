package xml.papersapp.controller;

import com.itextpdf.text.DocumentException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.dto.sciencePaper.DecisionDto;
import xml.papersapp.exceptions.sciencePapers.SciencePaperAlreadyExist;
import xml.papersapp.exceptions.sciencePapers.SciencePaperDoesntExist;
import xml.papersapp.exceptions.sciencePapers.SciencePaperNotFound;
import xml.papersapp.exceptions.sciencePapers.UnableToChangePaperState;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_paper.TState;
import xml.papersapp.model.user.TUser;
import xml.papersapp.security.repository.UserRepository;
import xml.papersapp.service.SciencePaperService;

import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/science-paper")
public class SciencePaperController {

    private final SciencePaperService sciencePaperService;

    public SciencePaperController(SciencePaperService sciencePaperService) {
        this.sciencePaperService = sciencePaperService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE})
    public ResponseEntity create(@RequestBody String sciencePaper) {
        try {
            SciencePaper createdSciencePaper = sciencePaperService.create(sciencePaper);
            return new ResponseEntity(createdSciencePaper, HttpStatus.OK);
        } catch (SciencePaperAlreadyExist | JAXBException | XMLDBException | SAXException sciencePaperAlreadyExist) {
            sciencePaperAlreadyExist.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseEntity searchSciencePapers(HttpServletRequest request,
                                                   @RequestParam("text") String text,
                                                   @RequestParam("date-from") String dateFrom,
                                                   @RequestParam("date-to") String dateTo) {
        Principal principal = request.getUserPrincipal();

        String email = "";
        String state = "";
        if (principal != null) {
            email = principal.getName();
        } else {
            email = "";
        }

        System.out.println(email);
        List<SciencePaper> paperList = null;
        try {
            paperList = sciencePaperService.searchForSciencePapers(email, text, dateFrom, dateTo);
        } catch (XMLDBException | SAXException | JAXBException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity("Date format invalid. Must be yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity(paperList, HttpStatus.OK);
    }

    @DeleteMapping("/cancel-submission")
    public ResponseEntity<?> cancelSubmission(HttpServletRequest request, @RequestParam("title") String title) throws XMLDBException {
        try {
            String email = request.getUserPrincipal().getName();
            SciencePaper sp = sciencePaperService.delete(title, email);
            return new ResponseEntity<SciencePaper>(sp, HttpStatus.OK);
        } catch (SciencePaperNotFound e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/papers-to-review")
    public ResponseEntity<?> getPapersToReview(HttpServletRequest request) {
        try {
            List<SciencePaper> papersToReview = sciencePaperService.getPapersToReview(request.getUserPrincipal().getName());
            return new ResponseEntity<List<SciencePaper>>(papersToReview, HttpStatus.OK);
        } catch (XMLDBException | SAXException | JAXBException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "getHTML/{documentId}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity<?> generateHTML(@PathVariable String documentId) {

        documentId = "http://www.tim12.com/science_paper/" + documentId;

        try {
            ByteArrayOutputStream out = sciencePaperService.generateHTML(documentId);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
            return ResponseEntity.ok()
                    .contentLength(out.size())
                    .contentType(MediaType.parseMediaType("application/xhtml+xml"))
                    .body(resource);
        } catch (SciencePaperDoesntExist sciencePaperDoesntExist) {
            sciencePaperDoesntExist.printStackTrace();
            return new ResponseEntity<>(sciencePaperDoesntExist.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "getPDF/{documentId}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity<?> generatePDF(@PathVariable String documentId) {

        documentId = "http://www.tim12.com/science_paper/" + documentId;

        try {
            ByteArrayOutputStream out = sciencePaperService.generatePDF(documentId);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
            return ResponseEntity.ok()
                    .contentLength(out.size())
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .body(resource);
        } catch (SciencePaperDoesntExist sciencePaperDoesntExist) {
            sciencePaperDoesntExist.printStackTrace();
            return new ResponseEntity<>(sciencePaperDoesntExist.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(path = "getXML/{documentId}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity<?> generateXML(@PathVariable String documentId) {

        documentId = "http://www.tim12.com/science_paper/" + documentId;

        try {
            ByteArrayOutputStream out = sciencePaperService.generateXML(documentId);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
            return ResponseEntity.ok()
                    .contentLength(out.size())
                    .contentType(MediaType.parseMediaType("application/xml"))
                    .body(resource);
        } catch (SciencePaperDoesntExist sciencePaperDoesntExist) {
            sciencePaperDoesntExist.printStackTrace();
            return new ResponseEntity<>(sciencePaperDoesntExist.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/decide")
    public ResponseEntity decideOnSciencePaper(@RequestBody DecisionDto decisionDto) {
        try {
            return new ResponseEntity<>(
                    sciencePaperService.decideOnSciencePaper(decisionDto.getPaperTitle(), decisionDto.isAccepted()),
                    HttpStatus.OK);
        } catch (XMLDBException | SAXException | JAXBException | SciencePaperNotFound | UnableToChangePaperState e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
