//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.13 at 02:20:43 AM CEST 
//


package com.tim12.review;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tim12.review package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Reviewer_QNAME = new QName("http://www.tim12.com/review", "Reviewer");
    private final static QName _Review_QNAME = new QName("http://www.tim12.com/review", "Review");
    private final static QName _Comments_QNAME = new QName("http://www.tim12.com/review", "Comments");
    private final static QName _Authors_QNAME = new QName("http://www.tim12.com/review", "Authors");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tim12.review
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TReview }
     * 
     */
    public TReview createTReview() {
        return new TReview();
    }

    /**
     * Create an instance of {@link Comment }
     * 
     */
    public Comment createComment() {
        return new Comment();
    }

    /**
     * Create an instance of {@link TComments }
     * 
     */
    public TComments createTComments() {
        return new TComments();
    }

    /**
     * Create an instance of {@link TAuthors }
     * 
     */
    public TAuthors createTAuthors() {
        return new TAuthors();
    }

    /**
     * Create an instance of {@link TReviewer }
     * 
     */
    public TReviewer createTReviewer() {
        return new TReviewer();
    }

    /**
     * Create an instance of {@link TAuthor }
     * 
     */
    public TAuthor createTAuthor() {
        return new TAuthor();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TReviewer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tim12.com/review", name = "Reviewer")
    public JAXBElement<TReviewer> createReviewer(TReviewer value) {
        return new JAXBElement<TReviewer>(_Reviewer_QNAME, TReviewer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TReview }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tim12.com/review", name = "Review")
    public JAXBElement<TReview> createReview(TReview value) {
        return new JAXBElement<TReview>(_Review_QNAME, TReview.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TComments }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tim12.com/review", name = "Comments")
    public JAXBElement<TComments> createComments(TComments value) {
        return new JAXBElement<TComments>(_Comments_QNAME, TComments.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TAuthors }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tim12.com/review", name = "Authors")
    public JAXBElement<TAuthors> createAuthors(TAuthors value) {
        return new JAXBElement<TAuthors>(_Authors_QNAME, TAuthors.class, null, value);
    }

}
