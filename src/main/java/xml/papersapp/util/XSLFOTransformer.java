package xml.papersapp.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import net.sf.saxon.TransformerFactoryImpl;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

@Service
public class XSLFOTransformer {


    private  DocumentBuilderFactory documentFactory;

    private  TransformerFactory transformerFactory;

    public XSLFOTransformer() {
        transformerFactory = TransformerFactory.newInstance();

        /* Inicijalizacija DOM fabrike */
        documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setNamespaceAware(true);
        documentFactory.setIgnoringComments(true);
        documentFactory.setIgnoringElementContentWhitespace(true);

    }

    public org.w3c.dom.Document buildDocument(OutputStream outputStream) {

        ByteArrayOutputStream buffer = (ByteArrayOutputStream) outputStream;
        byte[] bytes = buffer.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);

        org.w3c.dom.Document document = null;
        try {

            DocumentBuilder builder = documentFactory.newDocumentBuilder();
            document = builder.parse(inputStream);

            if (document != null)
                System.out.println("[INFO] File parsed with no errors.");
            else
                System.out.println("[WARN] Document is null.");

        } catch (Exception e) {
            return null;

        }

        return document;
    }

    public ByteArrayOutputStream generatePDF(ByteArrayOutputStream html) throws IOException, DocumentException {

        Document document = new Document();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        InputStream stream = new ByteArrayInputStream(html.toByteArray());
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, stream);

        document.close();

        return outputStream;

    }


    public ByteArrayOutputStream generateHTML(OutputStream xml , String xslPath) throws FileNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // Initialize Transformer instance
            StreamSource transformSource = new StreamSource(new File(xslPath));
            Transformer transformer = transformerFactory.newTransformer(transformSource);
            transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Generate XHTML
            transformer.setOutputProperty(OutputKeys.METHOD, "xhtml");


            // Transform DOM to HTML
            DOMSource source = new DOMSource(buildDocument(xml));
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(source, result);

        } catch (TransformerFactoryConfigurationError | TransformerException e) {
            e.printStackTrace();
        }

        return outputStream;

    }

}
