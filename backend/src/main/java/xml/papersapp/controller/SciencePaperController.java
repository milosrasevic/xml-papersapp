package xml.papersapp.controller;

import com.itextpdf.text.DocumentException;
import org.exist.security.PermissionRequired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.dto.sciencePaper.DecisionDto;
import xml.papersapp.exceptions.sciencePapers.*;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_paper.TState;
import xml.papersapp.model.user.TUser;
import xml.papersapp.security.repository.UserRepository;
import xml.papersapp.service.SciencePaperService;

import javax.annotation.security.RolesAllowed;
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
    public ResponseEntity searchSciencePapersNonAuthenticated(@RequestParam("text") String text, @RequestParam("date-from") String dateFrom,
                                                   @RequestParam("date-to") String dateTo) {

        List<SciencePaper> paperList = null;
        try {
            paperList = sciencePaperService.searchForSciencePapers(text, dateFrom, dateTo);
        } catch (XMLDBException | SAXException | JAXBException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity("Date format invalid. Must be yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity(paperList, HttpStatus.OK);
    }

    @GetMapping("/authenticated/search")
    public ResponseEntity searchSciencePapersAuthenticated(HttpServletRequest request,
                                              @RequestParam("text") String text,
                                              @RequestParam("date-from") String dateFrom,
                                              @RequestParam("date-to") String dateTo,
                                              @RequestParam("state") String state) {
        Principal principal = request.getUserPrincipal();

        String email = "";
        if (principal != null) {
            email = principal.getName();
        } else {
            email = "";
        }

        System.out.println(email);
        List<SciencePaper> paperList = null;
        try {
            paperList = sciencePaperService.searchForSciencePapersAuthenticated(email, text, dateFrom, dateTo, state);
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

    @GetMapping("/my-assigments")
    public ResponseEntity getMyAssigments(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(sciencePaperService.getMyAssigments(request.getUserPrincipal().getName()), HttpStatus.OK);
        } catch (SAXException | JAXBException | XMLDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "getHTML/{title}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity<?> generateHTML(@PathVariable String title) {

        try {
            ByteArrayOutputStream out = sciencePaperService.generateHTML(title);
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

    @GetMapping(path = "getPDF/{title}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity<?> generatePDF(@PathVariable String title) {


        try {
            ByteArrayOutputStream out = sciencePaperService.generatePDF(title);
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

    @GetMapping(path = "getXML/{title}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity<?> generateXML(@PathVariable String title) {

        try {
            ByteArrayOutputStream out = sciencePaperService.generateXML(title);
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


    @GetMapping("/get/{title}")
    public ResponseEntity getSciencePaper(@PathVariable String title) {
        try {
            return new ResponseEntity<>(sciencePaperService.getSciencePaper(title), HttpStatus.OK);
        } catch (XMLDBException | SAXException | JAXBException | SciencePaperDoesntExist e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/send-to-revision")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<?> sendToRevision(@RequestParam("title") String title) {
        try {
            SciencePaper paper = sciencePaperService.sendToRevision(title);
            return new ResponseEntity<SciencePaper>(paper, HttpStatus.OK);
        } catch (XMLDBException | SAXException | JAXBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (SciencePaperDoesntExist | SciencePaperCantBeSentToRevision e ) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
