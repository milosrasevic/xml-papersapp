package xml.papersapp.repository;

import org.springframework.stereotype.Repository;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_papers.SciencePapers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Optional;

import static xml.papersapp.constants.DocumentIDs.SCIENCE_PAPER_ID_DOCUMENT;
import static xml.papersapp.constants.Namespaces.SCIENCE_PAPERS_NAMESPACE;
import static xml.papersapp.constants.Namespaces.SCIENCE_PAPER_NAMESPACE;
import static xml.papersapp.constants.Packages.SCIENCE_PAPER_PACKAGE;
import static xml.papersapp.util.XUpdateTemplate.APPEND;


@Repository
public class SciencePaperRepository {


    // Database
    private final Collection collection;
    private final XUpdateQueryService xUpdateQueryService;
    private final XPathQueryService xPathQueryService;

    private final String CONTEXT_PATH_APPEND = "//SciencePapers";

    public SciencePaperRepository(Collection collection, XUpdateQueryService xUpdateQueryService, XPathQueryService xPathQueryService) {
        this.collection = collection;
        this.xUpdateQueryService = xUpdateQueryService;
        this.xPathQueryService = xPathQueryService;
    }

    public void save(SciencePaper sciencePaper) throws JAXBException, XMLDBException {


        OutputStream xml = getXMLFromObject(sciencePaper, "xml.papersapp.model.science_paper");



        long mods = xUpdateQueryService.updateResource(SCIENCE_PAPER_ID_DOCUMENT, String.format(APPEND, SCIENCE_PAPERS_NAMESPACE, CONTEXT_PATH_APPEND, xml.toString()));
        System.out.println("[INFO] " + mods + " modifications processed.");


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

    public Optional<SciencePaper> findOneByTitle(String title) throws XMLDBException, JAXBException {


        xPathQueryService.setNamespace("spp", SCIENCE_PAPERS_NAMESPACE);
        xPathQueryService.setNamespace("sp", SCIENCE_PAPER_NAMESPACE);

        String query = "//spp:SciencePapers/sp:SciencePaper[sp:title='" + title + "']";

        ResourceSet result = xPathQueryService.query(query);

        ResourceIterator i = result.getIterator();
        Resource res = i.nextResource();

        return res != null ? Optional.of(getSciencePaperFromResource(res, SCIENCE_PAPER_PACKAGE)) : Optional.empty();
    }


    private SciencePapers getSciencePapersFromXMLResource(XMLResource resource, String pckg) throws JAXBException, XMLDBException {
        JAXBContext context = JAXBContext.newInstance(pckg);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        return (SciencePapers) unmarshaller.unmarshal(resource.getContentAsDOM());
    }

    private SciencePaper getSciencePaperFromResource(Resource resource, String pckg) throws JAXBException, XMLDBException {
        JAXBContext context = JAXBContext.newInstance(pckg);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        return (SciencePaper) unmarshaller.unmarshal(new StringReader(resource.getContent().toString()));

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
