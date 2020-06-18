package xml.papersapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import xml.papersapp.model.review.TReview;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.StringReader;

import static xml.papersapp.constants.DocumentIDs.REVIEWS_ID_DOCUMENT;
import static xml.papersapp.constants.Files.SCHEME_REVIEW_PATH;
import static xml.papersapp.constants.Namespaces.REVIEWS_NAMESPACE;
import static xml.papersapp.constants.Namespaces.REVIEW_NAMESPACE;
import static xml.papersapp.constants.Packages.REVIEW_PACKAGE;
import static xml.papersapp.util.XUpdateTemplate.APPEND;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final Collection collection;
    private final XUpdateQueryService xUpdateQueryService;
    private final XPathQueryService xPathQueryService;
    private final XQueryService xQueryService;

    private final String CONTEXT_PATH_APPEND = "//reviews";

    public TReview getReviewFromResource(String resource) throws JAXBException, XMLDBException, SAXException {
        JAXBContext context = JAXBContext.newInstance(REVIEW_PACKAGE);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        // XML schema validacija
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(SCHEME_REVIEW_PATH));

        // Podešavanje unmarshaller-a za XML schema validaciju
        unmarshaller.setSchema(schema);

        JAXBElement<TReview> jaxbElement = (JAXBElement<TReview>) unmarshaller.unmarshal(new StringReader(resource));
        TReview tReview = jaxbElement.getValue();
        return tReview;

    }

    /*
     *   duplicated
     * */
    private OutputStream getXMLFromObject(Object object, String pckg) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(pckg);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        OutputStream os = new ByteArrayOutputStream();

        marshaller.marshal(object, os);

        return os;

    }

    public TReview save(TReview review) throws JAXBException, XMLDBException {

        OutputStream xml = getXMLFromObject(review, "xml.papersapp.model.review");

        long mods = xUpdateQueryService.updateResource(REVIEWS_ID_DOCUMENT, String.format(APPEND, REVIEWS_NAMESPACE, CONTEXT_PATH_APPEND, xml.toString()));
        System.out.println("[INFO] " + mods + " modifications processed.");

        return review;

    }
}
