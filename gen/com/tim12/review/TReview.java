//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.13 at 02:20:43 AM CEST 
//


package com.tim12.review;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for TReview complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TReview">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tim12.com/review}Authors"/>
 *         &lt;element ref="{http://www.tim12.com/review}Reviewer"/>
 *         &lt;element ref="{http://www.tim12.com/review}Comments"/>
 *         &lt;element name="finalDecision" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="overallOpinion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="blinded">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.tim12.com/review}TBlinded">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Id">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;pattern value="http://www.tim12.com/review/.+"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="status" type="{http://www.tim12.com/review}TStatus" />
 *       &lt;attribute name="sciencePaperId">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
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
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TReview", propOrder = {
    "authors",
    "reviewer",
    "comments",
    "finalDecision",
    "overallOpinion"
})
public class TReview {

    @XmlElement(name = "Authors", required = true)
    protected TAuthors authors;
    @XmlElement(name = "Reviewer", required = true)
    protected TReviewer reviewer;
    @XmlElement(name = "Comments", required = true)
    protected TComments comments;
    @XmlElement(required = true)
    protected String finalDecision;
    @XmlElement(required = true)
    protected String overallOpinion;
    @XmlAttribute(name = "blinded")
    protected TBlinded blinded;
    @XmlAttribute(name = "Id")
    protected String id;
    @XmlAttribute(name = "status")
    protected TStatus status;
    @XmlAttribute(name = "sciencePaperId")
    protected String sciencePaperId;
    @XmlAttribute(name = "dateCreated")
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date dateCreated;

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
     * Gets the value of the reviewer property.
     * 
     * @return
     *     possible object is
     *     {@link TReviewer }
     *     
     */
    public TReviewer getReviewer() {
        return reviewer;
    }

    /**
     * Sets the value of the reviewer property.
     * 
     * @param value
     *     allowed object is
     *     {@link TReviewer }
     *     
     */
    public void setReviewer(TReviewer value) {
        this.reviewer = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link TComments }
     *     
     */
    public TComments getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link TComments }
     *     
     */
    public void setComments(TComments value) {
        this.comments = value;
    }

    /**
     * Gets the value of the finalDecision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinalDecision() {
        return finalDecision;
    }

    /**
     * Sets the value of the finalDecision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinalDecision(String value) {
        this.finalDecision = value;
    }

    /**
     * Gets the value of the overallOpinion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOverallOpinion() {
        return overallOpinion;
    }

    /**
     * Sets the value of the overallOpinion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverallOpinion(String value) {
        this.overallOpinion = value;
    }

    /**
     * Gets the value of the blinded property.
     * 
     * @return
     *     possible object is
     *     {@link TBlinded }
     *     
     */
    public TBlinded getBlinded() {
        return blinded;
    }

    /**
     * Sets the value of the blinded property.
     * 
     * @param value
     *     allowed object is
     *     {@link TBlinded }
     *     
     */
    public void setBlinded(TBlinded value) {
        this.blinded = value;
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
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link TStatus }
     *     
     */
    public TStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link TStatus }
     *     
     */
    public void setStatus(TStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the sciencePaperId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSciencePaperId() {
        return sciencePaperId;
    }

    /**
     * Sets the value of the sciencePaperId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSciencePaperId(String value) {
        this.sciencePaperId = value;
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

}