package xml.papersapp.security.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.user.TRoles;
import xml.papersapp.model.user.TUser;
import xml.papersapp.util.AuthenticationUtilities;

import javax.xml.bind.*;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static xml.papersapp.constants.Namespaces.USERS_NAMESPACE;
import static xml.papersapp.constants.Namespaces.USER_NAMESPACE;
import static xml.papersapp.constants.Packages.*;

@Component
public class UserRepository{

    @Autowired
    private Collection collection;

    @Autowired
    private XPathQueryService xPathQueryService;

    public TUser findByUsername(String email) {
        try {

            xPathQueryService.setNamespace("users", USERS_NAMESPACE);
            xPathQueryService.setNamespace("user", USER_NAMESPACE);
            ResourceSet result = xPathQueryService.query("//users:Users/user:User[user:email=\""+email+"\"]");

            ResourceIterator i = result.getIterator();
            Resource res = i.nextResource();

            TUser tUser =  res != null ? getUserFromResource(res) : null;
            TRoles tRoles = new TRoles();
            return tUser;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private TUser getUserFromResource(Resource resource) throws JAXBException, XMLDBException {
        JAXBContext context = JAXBContext.newInstance(USER_PACKAGE);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        JAXBElement res = (JAXBElement) unmarshaller.unmarshal(new StringReader(resource.getContent().toString()));
        TUser tUser = (TUser) res.getValue();
        return tUser;
//        return (TUser) unmarshaller.unmarshal(new StringReader(resource.getContent().toString()));

    }

}
