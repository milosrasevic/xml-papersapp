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
import xml.papersapp.model.science_paper.SciencePaper;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.StringReader;

import static xml.papersapp.constants.Files.SCHEME_SCIENCE_PAPER_PATH;
import static xml.papersapp.constants.Packages.SCIENCE_PAPER_PACKAGE;

@Repository
public class ReviewRepository {

    private final Collection collection;
    private final XUpdateQueryService xUpdateQueryService;
    private final XPathQueryService xPathQueryService;
    private final XQueryService xQueryService;

    private final String CONTEXT_PATH_APPEND = "//Review";

    public ReviewRepository(Collection collection, XUpdateQueryService xUpdateQueryService,
                                  XPathQueryService xPathQueryService, XQueryService xQueryService) {
        this.collection = collection;
        this.xUpdateQueryService = xUpdateQueryService;
        this.xPathQueryService = xPathQueryService;
        this.xQueryService = xQueryService;
    }

    public TReview getReviewFromResource(String resource) throws JAXBException, XMLDBException, SAXException {
        JAXBContext context = JAXBContext.newInstance(SCIENCE_PAPER_PACKAGE);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        // XML schema validacija
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(SCHEME_SCIENCE_PAPER_PATH));

        // Podešavanje unmarshaller-a za XML schema validaciju
        unmarshaller.setSchema(schema);

        return (TReview) unmarshaller.unmarshal(new StringReader(resource));

    }
}
