//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.17 at 10:14:00 PM CEST 
//


package xml.papersapp.model.review_assignment;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TBlinded.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TBlinded">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DOUBLE_BLINDED"/>
 *     &lt;enumeration value="AUTHOR_BLINDED"/>
 *     &lt;enumeration value="REVIEWER_BLINDED"/>
 *     &lt;enumeration value="STANDARD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TBlinded")
@XmlEnum
public enum TBlinded {

    DOUBLE_BLINDED,
    AUTHOR_BLINDED,
    REVIEWER_BLINDED,
    STANDARD;

    public String value() {
        return name();
    }

    public static TBlinded fromValue(String v) {
        return valueOf(v);
    }

}
