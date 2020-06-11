package xml.papersapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.exceptions.sciencePapers.SciencePaperAlreadyExist;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.service.SciencePaperService;

import javax.validation.Valid;
import javax.xml.bind.JAXBException;

@RestController
@RequestMapping("/api/science-paper")
public class SciencePaperController {

    private final SciencePaperService sciencePaperService;

    public SciencePaperController(SciencePaperService sciencePaperService) {
        this.sciencePaperService = sciencePaperService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE})
    public ResponseEntity create(@RequestBody SciencePaper sciencePaper) {
        try {
            SciencePaper createdSciencePaper = sciencePaperService.create(sciencePaper);
            return new ResponseEntity(createdSciencePaper, HttpStatus.OK);
        } catch (SciencePaperAlreadyExist sciencePaperAlreadyExist) {
            sciencePaperAlreadyExist.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
