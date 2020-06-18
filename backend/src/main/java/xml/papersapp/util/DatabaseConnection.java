package xml.papersapp.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.*;

import java.io.File;
import java.io.IOException;


@Configuration
public class DatabaseConnection {

    private static AuthenticationUtilities.ConnectionProperties conn;
    private static Collection col;

    private static final String COLLECTION_ID = "db/apps/papersapp";

//    private static final String USERS_PATH = "src\\main\\resources\\data\\users.xml";
//    private static final String REVIEWS_PATH = "src\\main\\resources\\data\\reviews.xml";
//    private static final String SCIENCE_PAPERS_PATH = "src\\main\\resources\\data\\science_papers.xml";
//    private static final String REVIEW_ASSIGNMENTS_PATH = "src\\main\\resources\\data\\review_assignments.xml";
//    private static final String COVER_LETTERS_PATH = "src\\main\\resources\\data\\cover_letters.xml";

    private static final String USERS_PATH = "src/main/resources/data/users.xml";
    private static final String REVIEWS_PATH = "src/main/resources/data/reviews.xml";
    private static final String SCIENCE_PAPERS_PATH = "src/main/resources/data/science_papers.xml";
    private static final String REVIEW_ASSIGNMENTS_PATH = "src/main/resources/data/review_assignments.xml";
    private static final String COVER_LETTERS_PATH = "src/main/resources/data/cover_letters.xml";

    public static void createDBConnection() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, XMLDBException {

        try {
            conn = AuthenticationUtilities.loadProperties();

            System.out.println("[INFO] Loading driver class: " + conn.driver);
            Class<?> cl = Class.forName(conn.driver);

            // encapsulation of the database driver functionality
            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");

            // entry point for the API which enables you to get the Collection reference
            DatabaseManager.registerDatabase(database);

            createUsers();
            createSciencePapers();
            createReviews();
            createReviewAssignments();
            createCoverLetters();

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private static void create(String documentId, String filePath) {
        Collection col = null;

        XMLResource res = null; // delete
        try {

            System.out.println("[INFO] Retrieving the collection: " + COLLECTION_ID);
            col = getOrCreateCollection(COLLECTION_ID);

            System.out.println("[INFO] Inserting the document: " + documentId);
            res = (XMLResource) col.createResource(documentId, XMLResource.RESOURCE_TYPE);

            File f = new File(filePath);

            if(!f.canRead()) {
                System.out.println("[ERROR] Cannot read the file: " + filePath);
                return;
            }

            res.setContent(f);
            System.out.println("[INFO] Storing the document: " + res.getId());

            col.storeResource(res);
            System.out.println("[INFO] Done.");


        } catch (XMLDBException e) {
            e.printStackTrace();
        } finally {
            if(col != null) {
                try {
                    col.close();
                } catch (XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
        }
    }

    private static void createReviews() {
        String documentId = "reviews.xml";
        create(documentId, REVIEWS_PATH);
    }

    private static void createUsers() {
        String documentId = "users.xml";
        create(documentId, USERS_PATH);
    }

    private static void createSciencePapers() {
        String documentId = "science_papers.xml";
        create(documentId, SCIENCE_PAPERS_PATH);
    }

    private static void createReviewAssignments() {
        String documentId = "review_assignments.xml";
        create(documentId, REVIEW_ASSIGNMENTS_PATH);
    }

    private static void createCoverLetters() {
        String documentId = "cover_letters.xml";
        create(documentId, COVER_LETTERS_PATH);
    }

    private static Collection getOrCreateCollection(String collectionUri) throws XMLDBException {
        return getOrCreateCollection(collectionUri, 0);
    }

    @Bean
    public Collection collection() {
        return col;
    }

    private static Collection getOrCreateCollection(String collectionUri, int pathSegmentOffset) throws XMLDBException {

        col = DatabaseManager.getCollection(conn.uri + collectionUri, conn.user, conn.password);

        // create the collection if it does not exist
        if(col == null) {

            if(collectionUri.startsWith("/")) {
                collectionUri = collectionUri.substring(1);
            }

            String pathSegments[] = collectionUri.split("/");

            if(pathSegments.length > 0) {
                StringBuilder path = new StringBuilder();

                for(int i = 0; i <= pathSegmentOffset; i++) {
                    path.append("/" + pathSegments[i]);
                }

                Collection startCol = DatabaseManager.getCollection(conn.uri + path, conn.user, conn.password);

                if (startCol == null) {

                    // child collection does not exist

                    String parentPath = path.substring(0, path.lastIndexOf("/"));
                    Collection parentCol = DatabaseManager.getCollection(conn.uri + parentPath, conn.user, conn.password);

                    CollectionManagementService mgt = (CollectionManagementService) parentCol.getService("CollectionManagementService", "1.0");

                    System.out.println("[INFO] Creating the collection: " + pathSegments[pathSegmentOffset]);
                    col = mgt.createCollection(pathSegments[pathSegmentOffset]);

                    col.close();
                    parentCol.close();

                } else {
                    startCol.close();
                }
            }
            return getOrCreateCollection(collectionUri, ++pathSegmentOffset);
        } else {
            return col;
        }
    }

    @Bean
    public XPathQueryService xPathQueryService() {
        XPathQueryService xpathService = null;
        try {
            xpathService = (XPathQueryService) col.getService("XPathQueryService", "1.0");
        } catch (XMLDBException e) {
            e.printStackTrace();
        }
        try {
            xpathService.setProperty("indent", "yes");
        } catch (XMLDBException e) {
            e.printStackTrace();
        }

        return xpathService;
    }

    @Bean
    public XUpdateQueryService xUpdateQueryService() {
        XUpdateQueryService xupdateService = null;
        try {
            xupdateService = (XUpdateQueryService) col.getService("XUpdateQueryService", "1.0");
        } catch (XMLDBException e) {
            e.printStackTrace();
        }
        try {
            xupdateService.setProperty("indent", "yes");
        } catch (XMLDBException e) {
            e.printStackTrace();
        }

        return xupdateService;
    }

    @Bean
    public XQueryService xQueryService() {
        XQueryService xqueryService = null;
        try {
            xqueryService = (XQueryService) col.getService("XQueryService", "1.0");
        } catch (XMLDBException e) {
            e.printStackTrace();
        }
        try {
            xqueryService.setProperty("indent", "yes");
        } catch (XMLDBException e) {
            e.printStackTrace();
        }

        return xqueryService;
    }
}
