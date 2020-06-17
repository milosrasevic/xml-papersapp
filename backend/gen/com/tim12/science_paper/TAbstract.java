//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.12 at 03:39:33 PM CEST 
//


package com.tim12.science_paper;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TAbstract complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TAbstract">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="purpose" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="findings" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="designMethodologyApproach" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="researchLimitationsAndImplications" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="practicalImplications" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="socialImlications" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="originalityAndValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="keywords">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="keyword" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="12"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="type" type="{http://www.tim12.com/science_paper}TType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TAbstract", propOrder = {
    "purpose",
    "findings",
    "designMethodologyApproach",
    "researchLimitationsAndImplications",
    "practicalImplications",
    "socialImlications",
    "originalityAndValue",
    "keywords",
    "type"
})
public class TAbstract {

    @XmlElement(required = true)
    protected String purpose;
    @XmlElement(required = true)
    protected String findings;
    @XmlElement(required = true)
    protected String designMethodologyApproach;
    @XmlElement(required = true)
    protected String researchLimitationsAndImplications;
    @XmlElement(required = true)
    protected String practicalImplications;
    @XmlElement(required = true)
    protected String socialImlications;
    @XmlElement(required = true)
    protected String originalityAndValue;
    @XmlElement(required = true)
    protected TAbstract.Keywords keywords;
    @XmlElement(required = true)
    protected TType type;

    /**
     * Gets the value of the purpose property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * Sets the value of the purpose property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurpose(String value) {
        this.purpose = value;
    }

    /**
     * Gets the value of the findings property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFindings() {
        return findings;
    }

    /**
     * Sets the value of the findings property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFindings(String value) {
        this.findings = value;
    }

    /**
     * Gets the value of the designMethodologyApproach property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesignMethodologyApproach() {
        return designMethodologyApproach;
    }

    /**
     * Sets the value of the designMethodologyApproach property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesignMethodologyApproach(String value) {
        this.designMethodologyApproach = value;
    }

    /**
     * Gets the value of the researchLimitationsAndImplications property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResearchLimitationsAndImplications() {
        return researchLimitationsAndImplications;
    }

    /**
     * Sets the value of the researchLimitationsAndImplications property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResearchLimitationsAndImplications(String value) {
        this.researchLimitationsAndImplications = value;
    }

    /**
     * Gets the value of the practicalImplications property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPracticalImplications() {
        return practicalImplications;
    }

    /**
     * Sets the value of the practicalImplications property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPracticalImplications(String value) {
        this.practicalImplications = value;
    }

    /**
     * Gets the value of the socialImlications property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSocialImlications() {
        return socialImlications;
    }

    /**
     * Sets the value of the socialImlications property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSocialImlications(String value) {
        this.socialImlications = value;
    }

    /**
     * Gets the value of the originalityAndValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalityAndValue() {
        return originalityAndValue;
    }

    /**
     * Sets the value of the originalityAndValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalityAndValue(String value) {
        this.originalityAndValue = value;
    }

    /**
     * Gets the value of the keywords property.
     * 
     * @return
     *     possible object is
     *     {@link TAbstract.Keywords }
     *     
     */
    public TAbstract.Keywords getKeywords() {
        return keywords;
    }

    /**
     * Sets the value of the keywords property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAbstract.Keywords }
     *     
     */
    public void setKeywords(TAbstract.Keywords value) {
        this.keywords = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link TType }
     *     
     */
    public TType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link TType }
     *     
     */
    public void setType(TType value) {
        this.type = value;
    }


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
     *         &lt;element name="keyword" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="12"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "keyword"
    })
    public static class Keywords {

        @XmlElement(required = true)
        protected List<String> keyword;

        /**
         * Gets the value of the keyword property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the keyword property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getKeyword().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getKeyword() {
            if (keyword == null) {
                keyword = new ArrayList<String>();
            }
            return this.keyword;
        }

    }

}