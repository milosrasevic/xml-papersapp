package xml.papersapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.user.TUser;
import xml.papersapp.model.users.Users;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static xml.papersapp.constants.Files.SCHEME_USER_PATH;
import static xml.papersapp.constants.Namespaces.*;
import static xml.papersapp.constants.Namespaces.SCIENCE_PAPER_NAMESPACE;
import static xml.papersapp.constants.Packages.USER_PACKAGE;

@Repository
@RequiredArgsConstructor
public class UsersRepository {

    private final XPathQueryService xPathQueryService;

    public TUser getUserFromResource(String resource) throws JAXBException, SAXException {
        JAXBContext context = JAXBContext.newInstance(USER_PACKAGE);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        // XML schema validacija
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(SCHEME_USER_PATH));

        // Podešavanje unmarshaller-a za XML schema validaciju
        unmarshaller.setSchema(schema);

        JAXBElement<TUser> jaxbElement = (JAXBElement<TUser>) unmarshaller.unmarshal(new StringReader(resource));
        TUser tUser = jaxbElement.getValue();
        return tUser;

    }


    public TUser findUserByEmail(String email) throws XMLDBException, JAXBException, SAXException {

        xPathQueryService.setNamespace("users", USERS_NAMESPACE);
        xPathQueryService.setNamespace("user", USER_NAMESPACE);

        String query = "//users:Users/user:User[user:email='" + email + "']";

        ResourceSet result = xPathQueryService.query(query);

        if (result.getSize() > 0) {
            return getUserFromResource(result.getResource(0).getContent().toString());
        } else {
            return new TUser();
        }

    }

    public Users getAll() throws XMLDBException, JAXBException, SAXException {
        xPathQueryService.setNamespace("users", USERS_NAMESPACE);
        xPathQueryService.setNamespace("user", USER_NAMESPACE);

        String query = "//users:Users/user:User";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();

        Users users = new Users();

        while(i.hasMoreResources()) {
            users.getUser().add(getUserFromResource(i.nextResource().getContent().toString()));
        }

        return users;
    }
}
