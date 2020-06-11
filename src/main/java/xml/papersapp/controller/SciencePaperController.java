package xml.papersapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.exceptions.sciencePapers.SciencePaperAlreadyExist;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.service.SciencePaperService;

import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;
import java.security.Principal;
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

    @GetMapping("/author/{authorsEmail}")
    public ResponseEntity getSciencePapersByAuthorsEmail(@PathVariable("authorsEmail") String authorsEmail) {

        List<SciencePaper> paperList = null;
        try {
            paperList = sciencePaperService.getSciencePaperByAuthorsEmail(authorsEmail);
        } catch (XMLDBException | SAXException | JAXBException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity(paperList, HttpStatus.OK);
    }
}
