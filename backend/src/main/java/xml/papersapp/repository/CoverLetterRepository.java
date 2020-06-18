package xml.papersapp.repository;

import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import xml.papersapp.model.cover_letter.CoverLetter;
import xml.papersapp.model.science_paper.SciencePaper;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.StringReader;

import static xml.papersapp.constants.DocumentIDs.COVER_LETTERS_ID_DOCUMENT;
import static xml.papersapp.constants.DocumentIDs.SCIENCE_PAPER_ID_DOCUMENT;
import static xml.papersapp.constants.Files.SCHEME_COVER_LETTER_PATH;
import static xml.papersapp.constants.Files.SCHEME_SCIENCE_PAPER_PATH;
import static xml.papersapp.constants.Namespaces.COVER_LETTERS_NAMESPACE;
import static xml.papersapp.constants.Namespaces.SCIENCE_PAPERS_NAMESPACE;
import static xml.papersapp.constants.Packages.COVER_LETTER_PACKAGE;
import static xml.papersapp.constants.Packages.SCIENCE_PAPER_PACKAGE;
import static xml.papersapp.util.XUpdateTemplate.APPEND;

@Component
public class CoverLetterRepository {

    private final Collection collection;
    private final XUpdateQueryService xUpdateQueryService;
    private final XPathQueryService xPathQueryService;
    private final XQueryService xQueryService;

    private final String CONTEXT_PATH_APPEND = "//CoverLetters";

    public CoverLetterRepository(Collection collection, XUpdateQueryService xUpdateQueryService,
                                  XPathQueryService xPathQueryService, XQueryService xQueryService) {
        this.collection = collection;
        this.xUpdateQueryService = xUpdateQueryService;
        this.xPathQueryService = xPathQueryService;
        this.xQueryService = xQueryService;
    }

    public CoverLetter getCoverLetterFromResource(String resource) throws JAXBException, SAXException {

        JAXBContext context = JAXBContext.newInstance(COVER_LETTER_PACKAGE);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        // XML schema validacija
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(SCHEME_COVER_LETTER_PATH));

        // Podešavanje unmarshaller-a za XML schema validaciju
        unmarshaller.setSchema(schema);

        return (CoverLetter) unmarshaller.unmarshal(new StringReader(resource));

    }

    public CoverLetter save(CoverLetter coverLetter) throws JAXBException, XMLDBException {

        OutputStream xml = getXMLFromObject(coverLetter, COVER_LETTER_PACKAGE);

        long mods = xUpdateQueryService.updateResource(COVER_LETTERS_ID_DOCUMENT, String.format(APPEND, COVER_LETTERS_NAMESPACE, CONTEXT_PATH_APPEND, xml.toString()));
        System.out.println("[INFO] " + mods + " modifications processed.");

        return coverLetter;
    }

    public OutputStream getXMLFromObject(Object object, String pckg) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(pckg);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        OutputStream os = new ByteArrayOutputStream();

        marshaller.marshal(object, os);

        return os;

    }

}
