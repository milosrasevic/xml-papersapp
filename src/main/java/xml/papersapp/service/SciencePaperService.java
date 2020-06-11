package xml.papersapp.service;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import xml.papersapp.exceptions.sciencePapers.SciencePaperAlreadyExist;
import xml.papersapp.model.science_paper.SciencePaper;
import xml.papersapp.model.science_paper.TState;
import xml.papersapp.repository.SciencePaperRepository;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

import static xml.papersapp.constants.Files.SCHEME_SCIENCE_PAPER_PATH;
import static xml.papersapp.constants.Namespaces.SCIENCE_PAPERS_NAMESPACE;
import static xml.papersapp.constants.Namespaces.SCIENCE_PAPER_NAMESPACE;
import static xml.papersapp.constants.Packages.SCIENCE_PAPER_PACKAGE;
import static xml.papersapp.util.Util.createId;
import static xml.papersapp.util.Util.getXMLGregorianCalendar;

@Service
public class SciencePaperService {

    private final SciencePaperRepository sciencePaperRepository;

    public SciencePaperService(SciencePaperRepository sciencePaperRepository) {
        this.sciencePaperRepository = sciencePaperRepository;
    }

    public SciencePaper create(String xml) throws JAXBException, XMLDBException, SciencePaperAlreadyExist, SAXException {

        // validate xml
        SciencePaper sciencePaper = sciencePaperRepository.getSciencePaperFromResource(xml);

        // check if paper with that title already exist
        Optional<SciencePaper> found = sciencePaperRepository.findOneByTitle(sciencePaper.getTitle());

        if (found.isPresent()) {
            throw new SciencePaperAlreadyExist();
        }

        String id = createId(SCIENCE_PAPER_NAMESPACE);
        sciencePaper.setId(id);
        sciencePaper.setState(TState.WAITING);
        sciencePaper.setDateCreated(getXMLGregorianCalendar());
        sciencePaper = sciencePaperRepository.save(sciencePaper);

        return sciencePaper;
    }


//    private SciencePaper validateSciencePaperXML(String xml) throws JAXBException, SAXException {
//
//        JAXBContext context = JAXBContext.newInstance(SCIENCE_PAPER_PACKAGE);
//
//        Unmarshaller unmarshaller = context.createUnmarshaller();
//
//        // XML schema validacija
//        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        Schema schema = schemaFactory.newSchema(new File(SCHEME_SCIENCE_PAPER_PATH));
//
//        // Podešavanje unmarshaller-a za XML schema validaciju
//        unmarshaller.setSchema(schema);
//
//        SciencePaper sciencePaper = (SciencePaper) unmarshaller.unmarshal(new File(xml));
//
//        return sciencePaper;
//
//    }

}
