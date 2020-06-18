//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.17 at 10:55:51 PM CEST 
//


package xml.papersapp.model.review_assignment;

import xml.papersapp.model.user.TUser;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for TReviewAssignment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TReviewAssignment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tim12.com/review_assignment}sciencePaperTitle"/>
 *         &lt;element ref="{http://www.tim12.com/review_assignment}Reviewer"/>
 *       &lt;/sequence>
 *       &lt;attribute name="blinded">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.tim12.com/review_assignment}TBlinded">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="accepted" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TReviewAssignment", propOrder = {
    "sciencePaperTitle",
    "reviewer"
})
@XmlRootElement(name = "ReviewAssignment")
public class TReviewAssignment {

    @XmlElement(required = true)
    protected String sciencePaperTitle;
    @XmlElement(name = "Reviewer", required = true)
    protected TUser reviewer;
    @XmlAttribute(name = "blinded")
    protected TBlinded blinded;
    @XmlAttribute(name = "accepted")
    protected Boolean accepted;

    /**
     * Gets the value of the sciencePaperTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSciencePaperTitle() {
        return sciencePaperTitle;
    }

    /**
     * Sets the value of the sciencePaperTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSciencePaperTitle(String value) {
        this.sciencePaperTitle = value;
    }

    /**
     * Gets the value of the reviewer property.
     * 
     * @return
     *     possible object is
     *     {@link TUser }
     *     
     */
    public TUser getReviewer() {
        return reviewer;
    }

    /**
     * Sets the value of the reviewer property.
     * 
     * @param value
     *     allowed object is
     *     {@link TUser }
     *     
     */
    public void setReviewer(TUser value) {
        this.reviewer = value;
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
     * Gets the value of the accepted property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccepted() {
        return accepted;
    }

    /**
     * Sets the value of the accepted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccepted(Boolean value) {
        this.accepted = value;
    }

    public TReviewAssignment() {
    }

    public TReviewAssignment(String sciencePaperTitle, TUser reviewer, TBlinded blinded) {
        this.sciencePaperTitle = sciencePaperTitle;
        this.reviewer = reviewer;
        this.blinded = blinded;
        this.accepted = false;
    }
}
