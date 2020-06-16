package xml.papersapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.service.users.UsersService;

import javax.xml.bind.JAXBException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/getAll")
    public ResponseEntity getAll() {
        try {
            return new ResponseEntity<>(usersService.getAll(), HttpStatus.OK);
        } catch (XMLDBException | JAXBException | SAXException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
