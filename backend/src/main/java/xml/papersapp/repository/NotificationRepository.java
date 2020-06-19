package xml.papersapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import xml.papersapp.model.notification.TNotification;
import xml.papersapp.model.review.TReview;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static xml.papersapp.constants.DocumentIDs.NOTIFICATIONS_ID_DOCUMENT;
import static xml.papersapp.constants.DocumentIDs.REVIEWS_ID_DOCUMENT;
import static xml.papersapp.constants.Namespaces.NOTIFICATIONS_NAMESPACE;
import static xml.papersapp.constants.Namespaces.REVIEWS_NAMESPACE;
import static xml.papersapp.util.XUpdateTemplate.APPEND;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {

    private final Collection collection;
    private final XUpdateQueryService xUpdateQueryService;
    private final XPathQueryService xPathQueryService;
    private final XQueryService xQueryService;

    private final String CONTEXT_PATH_APPEND = "//notifications";


    public TNotification save(TNotification notification) throws JAXBException, XMLDBException {

        OutputStream xml = getXMLFromObject(notification, "xml.papersapp.model.notification");

        long mods = xUpdateQueryService.updateResource(NOTIFICATIONS_ID_DOCUMENT, String.format(APPEND, NOTIFICATIONS_NAMESPACE,
                CONTEXT_PATH_APPEND, xml.toString()));
        System.out.println("[INFO] " + mods + " modifications processed.");

        return notification;

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
