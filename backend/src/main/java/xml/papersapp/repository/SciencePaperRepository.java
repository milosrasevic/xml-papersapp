package xml.papersapp.repository;

import org.exist.xmldb.EXistResource;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import xml.papersapp.exceptions.sciencePapers.SciencePaperNotFound;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_paper.TState;
import xml.papersapp.model.science_papers.SciencePapers;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static xml.papersapp.constants.DocumentIDs.SCIENCE_PAPER_ID_DOCUMENT;
import static xml.papersapp.constants.Files.SCHEME_SCIENCE_PAPER_PATH;
import static xml.papersapp.constants.Namespaces.*;
import static xml.papersapp.constants.Packages.SCIENCE_PAPER_PACKAGE;
import static xml.papersapp.util.Util.getDateFromString;
import static xml.papersapp.util.XUpdateTemplate.*;


@Repository
public class SciencePaperRepository {


    // Database
    private final Collection collection;
    private final XUpdateQueryService xUpdateQueryService;
    private final XPathQueryService xPathQueryService;
    private final XQueryService xQueryService;

    private final String CONTEXT_PATH_APPEND = "//SciencePapers";

    public SciencePaperRepository(Collection collection, XUpdateQueryService xUpdateQueryService,
                                  XPathQueryService xPathQueryService, XQueryService xQueryService) {
        this.collection = collection;
        this.xUpdateQueryService = xUpdateQueryService;
        this.xPathQueryService = xPathQueryService;
        this.xQueryService = xQueryService;
    }

    public SciencePaper save(SciencePaper sciencePaper) throws JAXBException, XMLDBException {

        OutputStream xml = getXMLFromObject(sciencePaper, "xml.papersapp.model.science_paper");

        long mods = xUpdateQueryService.updateResource(SCIENCE_PAPER_ID_DOCUMENT, String.format(APPEND, SCIENCE_PAPERS_NAMESPACE, CONTEXT_PATH_APPEND, xml.toString()));
        System.out.println("[INFO] " + mods + " modifications processed.");

        return sciencePaper;



        // all xml documents
//        XMLResource resource = (XMLResource) collection.getResource(SCIENCE_PAPER_ID_DOCUMENT);

        // convert xml with all documents to object
//        SciencePapers sciencePapers = getSciencePapersFromXMLResource(resource, SCIENCE_PAPERS_PACKAGE);


        // add new science paper
//        sciencePapers.addSciencePaper(sciencePaper);

        // convert object to outputStream
//        OutputStream newAllSciencePapersXML = getXMLFromObject(sciencePapers, SCIENCE_PAPERS_PACKAGE);

//        System.out.println(newAllSciencePapersXML);

        // add outputstream to resource
//        resource.setContent(newAllSciencePapersXML);

        // save to db
//        collection.storeResource(resource);



    }

    public Optional<SciencePaper> findOneByTitle(String title) throws XMLDBException, JAXBException, SAXException {


        xPathQueryService.setNamespace("spp", SCIENCE_PAPERS_NAMESPACE);
        xPathQueryService.setNamespace("sp", SCIENCE_PAPER_NAMESPACE);

        String query = "//spp:SciencePapers/sp:SciencePaper[sp:title='" + title + "']";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();
        Resource res = i.nextResource();

        return res != null ? Optional.of(getSciencePaperFromResource(res.getContent().toString())) : Optional.empty();
    }

    public Optional<SciencePaper> findOneByDocumentId(String documentId) throws XMLDBException, JAXBException, SAXException {


        xPathQueryService.setNamespace("spp", SCIENCE_PAPERS_NAMESPACE);
        xPathQueryService.setNamespace("sp", SCIENCE_PAPER_NAMESPACE);

        String query = "//spp:SciencePapers/sp:SciencePaper[@id='" + documentId + "']";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();
        Resource res = i.nextResource();

        return res != null ? Optional.of(getSciencePaperFromResource(res.getContent().toString())) : Optional.empty();
    }


//    private SciencePapers getSciencePapersFromXMLResource(XMLResource resource, String pckg) throws JAXBException, XMLDBException {
//        JAXBContext context = JAXBContext.newInstance(pckg);
//
//        Unmarshaller unmarshaller = context.createUnmarshaller();
//
//        return (SciencePapers) unmarshaller.unmarshal(resource.getContentAsDOM());
//    }

    public SciencePaper getSciencePaperFromResource(String resource) throws JAXBException, XMLDBException, SAXException {
        JAXBContext context = JAXBContext.newInstance(SCIENCE_PAPER_PACKAGE);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        // XML schema validacija
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(SCHEME_SCIENCE_PAPER_PATH));

        // Podešavanje unmarshaller-a za XML schema validaciju
        unmarshaller.setSchema(schema);

        return (SciencePaper) unmarshaller.unmarshal(new StringReader(resource));

    }

    public OutputStream getXMLFromObject(Object object, String pckg) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(pckg);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        OutputStream os = new ByteArrayOutputStream();

        marshaller.marshal(object, os);

        return os;

    }

    public List<SciencePaper> findAllByAuthorsEmail(String email) throws XMLDBException, JAXBException, SAXException {

        xPathQueryService.setNamespace("spp", SCIENCE_PAPERS_NAMESPACE);
        xPathQueryService.setNamespace("", SCIENCE_PAPER_NAMESPACE);

        String query = "//spp:SciencePapers/SciencePaper[./authors//author[email='" + email + "']]";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();

        List<SciencePaper> papers = new ArrayList<>();

        while(i.hasMoreResources()) {
            papers.add(getSciencePaperFromResource(i.nextResource().getContent().toString()));
        }

        return papers;

    }

    public List<SciencePaper> searchSciencePapers(String email, String text, String dateFromString, String dateToString, String state) throws XMLDBException, JAXBException, SAXException, IOException, ParseException {

        String xqueryFilePath = "src/main/resources/xquery/find_science_papers.xqy";

        xQueryService.setNamespace("spp", SCIENCE_PAPERS_NAMESPACE);
        xQueryService.setNamespace("", SCIENCE_PAPER_NAMESPACE);

        xQueryService.declareVariable("authorsEmail", email);
        xQueryService.declareVariable("text", text);
        xQueryService.declareVariable("dateFrom", dateFromString);
        xQueryService.declareVariable("dateTo", dateToString);
        xQueryService.declareVariable("state", state);

        // read xquery
        System.out.println("[INFO] Invoking XQuery service for: " + xqueryFilePath);
        String xqueryExpression = readFile(xqueryFilePath, StandardCharsets.UTF_8);

        // compile and execute the expression
        CompiledExpression compiledXquery = xQueryService.compile(xqueryExpression);
        ResourceSet resourceSet = xQueryService.execute(compiledXquery);

        ResourceIterator i = resourceSet.getIterator();

        List<SciencePaper> papers = new ArrayList<>();

        while(i.hasMoreResources()) {
            papers.add(getSciencePaperFromResource(i.nextResource().getContent().toString()));
        }


        return papers;


    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public SciencePaper delete(String title, String email) throws XMLDBException, SciencePaperNotFound, JAXBException, SAXException {

        xPathQueryService.setNamespace("spp", SCIENCE_PAPERS_NAMESPACE);
        xPathQueryService.setNamespace("sp", SCIENCE_PAPER_NAMESPACE);

        String query = "//spp:SciencePapers/sp:SciencePaper[sp:title='" + title
                + "' and .//sp:author[sp:email='" + email + "'] and (@state='waiting' or @state='revision_needed')]";
        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();
        Resource res = i.nextResource();

        if (res == null) {
            throw new SciencePaperNotFound();
        }

        String resourceString = res.getContent().toString();

        long mods = xUpdateQueryService.updateResource(SCIENCE_PAPER_ID_DOCUMENT, String.format(REMOVE,
                SCIENCE_PAPERS_NAMESPACE, query));
        System.out.println("[INFO] " + mods + " modifications processed.");

        return getSciencePaperFromResource(resourceString);
    }

    public SciencePaper update(SciencePaper sciencePaper) throws JAXBException, XMLDBException{
        OutputStream xml = getXMLFromObject(sciencePaper, SCIENCE_PAPER_PACKAGE);
        String xmlString = xml.toString();

        String query = "//spp:SciencePapers/sp:SciencePaper[sp:title='" + sciencePaper.getTitle() + "']";


        long mods = xUpdateQueryService.updateResource(SCIENCE_PAPER_ID_DOCUMENT, String.format(REMOVE,
                SCIENCE_PAPERS_NAMESPACE, query));
        System.out.println("[INFO] " + mods + " modifications processed.");

        long mods1 = xUpdateQueryService.updateResource(SCIENCE_PAPER_ID_DOCUMENT, String.format(APPEND,
                SCIENCE_PAPERS_NAMESPACE, CONTEXT_PATH_APPEND , xmlString));
        System.out.println("[INFO] " + mods1 + " modifications processed.");

        return sciencePaper;

    }
}
