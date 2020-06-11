//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.11 at 06:00:34 PM CEST 
//


package xml.papersapp.model.science_paper;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TReferenceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TReferenceType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="book"/>
 *     &lt;enumeration value="book_chapter"/>
 *     &lt;enumeration value="journal"/>
 *     &lt;enumeration value="published_conference_proceeding"/>
 *     &lt;enumeration value="unpublished_conference_proceeding"/>
 *     &lt;enumeration value="working_paper"/>
 *     &lt;enumeration value="encyclopedia"/>
 *     &lt;enumeration value="newspaper_article_authored"/>
 *     &lt;enumeration value="newspaper_article_unauthored"/>
 *     &lt;enumeration value="electronic_source"/>
 *     &lt;enumeration value="data"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TReferenceType")
@XmlEnum
public enum TReferenceType {

    @XmlEnumValue("book")
    BOOK("book"),
    @XmlEnumValue("book_chapter")
    BOOK_CHAPTER("book_chapter"),
    @XmlEnumValue("journal")
    JOURNAL("journal"),
    @XmlEnumValue("published_conference_proceeding")
    PUBLISHED_CONFERENCE_PROCEEDING("published_conference_proceeding"),
    @XmlEnumValue("unpublished_conference_proceeding")
    UNPUBLISHED_CONFERENCE_PROCEEDING("unpublished_conference_proceeding"),
    @XmlEnumValue("working_paper")
    WORKING_PAPER("working_paper"),
    @XmlEnumValue("encyclopedia")
    ENCYCLOPEDIA("encyclopedia"),
    @XmlEnumValue("newspaper_article_authored")
    NEWSPAPER_ARTICLE_AUTHORED("newspaper_article_authored"),
    @XmlEnumValue("newspaper_article_unauthored")
    NEWSPAPER_ARTICLE_UNAUTHORED("newspaper_article_unauthored"),
    @XmlEnumValue("electronic_source")
    ELECTRONIC_SOURCE("electronic_source"),
    @XmlEnumValue("data")
    DATA("data");
    private final String value;

    TReferenceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TReferenceType fromValue(String v) {
        for (TReferenceType c: TReferenceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
