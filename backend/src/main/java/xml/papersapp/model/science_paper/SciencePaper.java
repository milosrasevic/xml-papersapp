//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.12 at 03:39:33 PM CEST 
//


package xml.papersapp.model.science_paper;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="title">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;whiteSpace value="preserve"/>
 *               &lt;pattern value=".+(\s.+){0,15}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="authors" type="{http://www.tim12.com/science_paper}TAuthors"/>
 *         &lt;element name="reviewers" type="{http://www.tim12.com/science_paper}TReviewers"/>
 *         &lt;element name="content" type="{http://www.tim12.com/science_paper}TContent"/>
 *         &lt;element name="abstract" type="{http://www.tim12.com/science_paper}TAbstract"/>
 *         &lt;element name="references" type="{http://www.tim12.com/science_paper}TReferences"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;pattern value=""/>
 *             &lt;pattern value="http://www.tim12.com/science_paper/.+"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="dateCreated">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}date">
 *             &lt;minInclusive value="1900-01-01"/>
 *             &lt;maxInclusive value="2200-01-01"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="dateRevised">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}date">
 *             &lt;minInclusive value="1900-01-01"/>
 *             &lt;maxInclusive value="2200-01-01"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="dateAccepted">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}date">
 *             &lt;minInclusive value="1900-01-01"/>
 *             &lt;maxInclusive value="2200-01-01"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="dateRejected">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}date">
 *             &lt;minInclusive value="1900-01-01"/>
 *             &lt;maxInclusive value="2200-01-01"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="state" type="{http://www.tim12.com/science_paper}TState" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "authors",
    "reviewers",
    "content",
    "_abstract",
    "references"
})
@XmlRootElement(name = "SciencePaper")
public class SciencePaper {

    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected TAuthors authors;
    @XmlElement(required = true)
    protected TReviewers reviewers;
    @XmlElement(required = true)
    protected TContent content;
    @XmlElement(name = "abstract", required = true)
    protected TAbstract _abstract;
    @XmlElement(required = true)
    protected TReferences references;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "dateCreated")
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date dateCreated;
    @XmlAttribute(name = "dateRevised")
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date dateRevised;
    @XmlAttribute(name = "dateAccepted")
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date dateAccepted;
    @XmlAttribute(name = "dateRejected")
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date dateRejected;
    @XmlAttribute(name = "state")
    protected TState state;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the authors property.
     * 
     * @return
     *     possible object is
     *     {@link TAuthors }
     *     
     */
    public TAuthors getAuthors() {
        return authors;
    }

    /**
     * Sets the value of the authors property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAuthors }
     *     
     */
    public void setAuthors(TAuthors value) {
        this.authors = value;
    }

    /**
     * Gets the value of the reviewers property.
     * 
     * @return
     *     possible object is
     *     {@link TReviewers }
     *     
     */
    public TReviewers getReviewers() {
        return reviewers;
    }

    /**
     * Sets the value of the reviewers property.
     * 
     * @param value
     *     allowed object is
     *     {@link TReviewers }
     *     
     */
    public void setReviewers(TReviewers value) {
        this.reviewers = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link TContent }
     *     
     */
    public TContent getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link TContent }
     *     
     */
    public void setContent(TContent value) {
        this.content = value;
    }

    /**
     * Gets the value of the abstract property.
     * 
     * @return
     *     possible object is
     *     {@link TAbstract }
     *     
     */
    public TAbstract getAbstract() {
        return _abstract;
    }

    /**
     * Sets the value of the abstract property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAbstract }
     *     
     */
    public void setAbstract(TAbstract value) {
        this._abstract = value;
    }

    /**
     * Gets the value of the references property.
     * 
     * @return
     *     possible object is
     *     {@link TReferences }
     *     
     */
    public TReferences getReferences() {
        return references;
    }

    /**
     * Sets the value of the references property.
     * 
     * @param value
     *     allowed object is
     *     {@link TReferences }
     *     
     */
    public void setReferences(TReferences value) {
        this.references = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the dateCreated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the value of the dateCreated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateCreated(Date value) {
        this.dateCreated = value;
    }

    /**
     * Gets the value of the dateRevised property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDateRevised() {
        return dateRevised;
    }

    /**
     * Sets the value of the dateRevised property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateRevised(Date value) {
        this.dateRevised = value;
    }

    /**
     * Gets the value of the dateAccepted property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDateAccepted() {
        return dateAccepted;
    }

    /**
     * Sets the value of the dateAccepted property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateAccepted(Date value) {
        this.dateAccepted = value;
    }

    /**
     * Gets the value of the dateRejected property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDateRejected() {
        return dateRejected;
    }

    /**
     * Sets the value of the dateRejected property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateRejected(Date value) {
        this.dateRejected = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link TState }
     *     
     */
    public TState getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link TState }
     *     
     */
    public void setState(TState value) {
        this.state = value;
    }

}