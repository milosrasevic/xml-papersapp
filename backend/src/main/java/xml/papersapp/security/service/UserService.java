package xml.papersapp.security.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.dto.user.RegistrationDTO;
import xml.papersapp.dto.user.UserDTO;
import xml.papersapp.dto.user.WorkplaceDTO;
import xml.papersapp.exceptions.users.EmailTaken;
import xml.papersapp.model.user.TRoles;
import xml.papersapp.model.user.TUser;
import xml.papersapp.model.user.TWorkplace;
import xml.papersapp.security.repository.UserRepository;

import javax.xml.bind.JAXBException;

import java.util.Optional;

import static xml.papersapp.constants.Namespaces.USER_NAMESPACE;
import static xml.papersapp.util.Util.createId;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<TUser> userDetails = userRepository.findByUsername(username);
        if (!userDetails.isPresent()) {
            throw new UsernameNotFoundException("Username not found");
        }
        return userDetails.get();
    }


    public TUser register(RegistrationDTO registrationDTO) throws JAXBException, XMLDBException, EmailTaken {

        WorkplaceDTO workplaceDTO = registrationDTO.getWorkplace();


        TWorkplace workplace = new TWorkplace(workplaceDTO.getName(), workplaceDTO.getAddress(),
                workplaceDTO.getCountry(), workplaceDTO.getCity(), Integer.parseInt(workplaceDTO.getZip()));


        TUser user = new TUser(registrationDTO.getFirstName(), registrationDTO.getLastName(),
                passwordEncoder.encode(registrationDTO.getPassword()), registrationDTO.getProfession(), workplace,
                registrationDTO.getPhoneNumber(), registrationDTO.getEmail());

        Optional<TUser> existingUser = userRepository.findByUsername(registrationDTO.getEmail());

        if(existingUser.isPresent()){
            throw new EmailTaken();
        }

        String id = createId(USER_NAMESPACE);
        user.setId(id);
        user.setRoles(new TRoles());

        return userRepository.save(user);
    }
}
