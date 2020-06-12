package xml.papersapp.security.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import xml.papersapp.model.user.TRoles;
import xml.papersapp.model.user.TUser;

import javax.xml.bind.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Optional;

import static xml.papersapp.constants.DocumentIDs.USERS_ID_DOCUMENT;
import static xml.papersapp.constants.Namespaces.USERS_NAMESPACE;
import static xml.papersapp.constants.Namespaces.USER_NAMESPACE;
import static xml.papersapp.constants.Packages.*;
import static xml.papersapp.util.XUpdateTemplate.APPEND;

@Component
public class UserRepository{

    @Autowired
    private Collection collection;

    @Autowired
    private XPathQueryService xPathQueryService;

    @Autowired
    private XUpdateQueryService xUpdateQueryService;

    private final String CONTEXT_PATH_APPEND = "//Users";

    public Optional<TUser> findByUsername(String email) {
        try {

            xPathQueryService.setNamespace("users", USERS_NAMESPACE);
            xPathQueryService.setNamespace("user", USER_NAMESPACE);
            ResourceSet result = xPathQueryService.query("//users:Users/user:User[user:email=\""+email+"\"]");

            ResourceIterator i = result.getIterator();
            Resource res = i.nextResource();

            TUser tUser =  res != null ? getUserFromResource(res) : null;
            TRoles tRoles = new TRoles();
            return tUser != null ? Optional.of(tUser) : Optional.empty();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private TUser getUserFromResource(Resource resource) throws JAXBException, XMLDBException {
        JAXBContext context = JAXBContext.newInstance(USER_PACKAGE);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        JAXBElement res = (JAXBElement) unmarshaller.unmarshal(new StringReader(resource.getContent().toString()));
        TUser tUser = (TUser) res.getValue();
        return tUser;
//        return (TUser) unmarshaller.unmarshal(new StringReader(resource.getContent().toString()));

    }

    public TUser save(TUser user) throws JAXBException, XMLDBException {
        OutputStream xml = getXMLFromObject(user, "xml.papersapp.model.user");

        long mods = xUpdateQueryService.updateResource(USERS_ID_DOCUMENT, String.format(APPEND, USERS_NAMESPACE,
                CONTEXT_PATH_APPEND, xml.toString()));
        System.out.println("[INFO] " + mods + " modifications processed.");
        return user;
    }

    private OutputStream getXMLFromObject(Object object, String pckg) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(pckg);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        OutputStream os = new ByteArrayOutputStream();

        marshaller.marshal(object, os);

        return os;

    }

}
