package xml.papersapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.dto.user.RegistrationDTO;
import xml.papersapp.dto.user.UserDTO;
import xml.papersapp.dto.user.WorkplaceDTO;
import xml.papersapp.exceptions.users.EmailTaken;
import xml.papersapp.model.user.TUser;
import xml.papersapp.model.user.TWorkplace;
import xml.papersapp.security.TokenUtils;
import xml.papersapp.security.auth.JwtAuthenticationRequest;
import xml.papersapp.security.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity logOut(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logIn(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        final Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(
                                        authenticationRequest.getUsername(),
                                        authenticationRequest.getPassword()
                                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TUser user = (TUser) authentication.getPrincipal();

        String token = tokenUtils.generateToken(user.getEmail());
        return ResponseEntity.ok(new UserDTO(user.getEmail(), token, user.getFirstName(), user.getLastName()));
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody RegistrationDTO registrationDTO){

        try {
            TUser registeredUser = userService.register(registrationDTO);

            TWorkplace workplace = registeredUser.getWorkplace();

            return ResponseEntity.ok(new RegistrationDTO(
                    registeredUser.getEmail(),
                    registeredUser.getFirstName(),
                    registeredUser.getLastName(),
                    registeredUser.getProfession(),
                    new WorkplaceDTO(workplace.getName(),
                            workplace.getAddress(),
                            workplace.getCountry(),
                            workplace.getCity(),
                            Integer.toString(workplace.getZip())
                    ) ,
                    registeredUser.getPhoneNumber()));

        } catch (JAXBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (XMLDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EmailTaken et) {
            return new ResponseEntity<>(et.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/testUser")
    @PreAuthorize("hasRole('USER')")
    public String testUser(){
        return "USER WORKS";
    }

    @GetMapping(value = "/testAdmin")
    @PreAuthorize("hasRole('EDITOR')")
    public String testAdmin(){
        return "ADMIN WORKS";
    }
}
