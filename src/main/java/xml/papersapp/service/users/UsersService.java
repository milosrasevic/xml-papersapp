package xml.papersapp.service.users;

import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import xml.papersapp.model.users.Users;

import javax.xml.bind.JAXBException;

public interface UsersService {

    /**
     * Method that provides all users
     *
     * @return
     */
    Users getAll() throws XMLDBException, JAXBException, SAXException;
}
