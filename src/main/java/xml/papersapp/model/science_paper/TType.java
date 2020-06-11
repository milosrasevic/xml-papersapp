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
 * <p>Java class for TType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="research_paper"/>
 *     &lt;enumeration value="viewpoint"/>
 *     &lt;enumeration value="technical_paper"/>
 *     &lt;enumeration value="conceptual_paper"/>
 *     &lt;enumeration value="case_study"/>
 *     &lt;enumeration value="literature_review"/>
 *     &lt;enumeration value="general_review"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TType")
@XmlEnum
public enum TType {

    @XmlEnumValue("research_paper")
    RESEARCH_PAPER("research_paper"),
    @XmlEnumValue("viewpoint")
    VIEWPOINT("viewpoint"),
    @XmlEnumValue("technical_paper")
    TECHNICAL_PAPER("technical_paper"),
    @XmlEnumValue("conceptual_paper")
    CONCEPTUAL_PAPER("conceptual_paper"),
    @XmlEnumValue("case_study")
    CASE_STUDY("case_study"),
    @XmlEnumValue("literature_review")
    LITERATURE_REVIEW("literature_review"),
    @XmlEnumValue("general_review")
    GENERAL_REVIEW("general_review");
    private final String value;

    TType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TType fromValue(String v) {
        for (TType c: TType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
