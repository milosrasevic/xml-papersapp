//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.19 at 01:49:38 PM CEST 
//


package xml.papersapp.model.cover_letter;

import xml.papersapp.model.science_paper.TAuthor;

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
 *         &lt;element name="author" type="{http://www.tim12.com/science_paper}TAuthor"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="editor" type="{http://www.tim12.com/science_paper}TAuthor"/>
 *       &lt;/sequence>
 *       &lt;attribute name="date">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}date">
 *             &lt;minInclusive value="1900-01-01"/>
 *             &lt;maxInclusive value="2200-01-01"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="id">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;pattern value="http://www.tim12.com/cover_letter/.+"/>
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
@XmlType(name = "", propOrder = {
    "author",
    "content",
    "editor"
})
@XmlRootElement(name = "CoverLetter")
public class CoverLetter {

    @XmlElement(required = true)
    protected TAuthor author;
    @XmlElement(required = true)
    protected String content;
    @XmlElement(required = true)
    protected TAuthor editor;
    @XmlAttribute(name = "date")
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date date;
    @XmlAttribute(name = "id")
    protected String id;

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link TAuthor }
     *     
     */
    public TAuthor getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAuthor }
     *     
     */
    public void setAuthor(TAuthor value) {
        this.author = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the editor property.
     * 
     * @return
     *     possible object is
     *     {@link TAuthor }
     *     
     */
    public TAuthor getEditor() {
        return editor;
    }

    /**
     * Sets the value of the editor property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAuthor }
     *     
     */
    public void setEditor(TAuthor value) {
        this.editor = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(Date value) {
        this.date = value;
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

}
