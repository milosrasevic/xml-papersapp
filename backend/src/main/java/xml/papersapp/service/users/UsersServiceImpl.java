package xml.papersapp.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.model.users.Users;
import xml.papersapp.repository.UsersRepository;

import javax.xml.bind.JAXBException;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public Users getAll() throws XMLDBException, JAXBException, SAXException {
        return usersRepository.getAll();
    }
}
