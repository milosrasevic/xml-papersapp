//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.19 at 01:49:38 PM CEST 
//


package com.tim12.science_paper;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="waiting"/>
 *     &lt;enumeration value="review_in_progress"/>
 *     &lt;enumeration value="rejected"/>
 *     &lt;enumeration value="accepted"/>
 *     &lt;enumeration value="deleted"/>
 *     &lt;enumeration value="waiting_for_approval"/>
 *     &lt;enumeration value="revision_needed"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TState")
@XmlEnum
public enum TState {

    @XmlEnumValue("waiting")
    WAITING("waiting"),
    @XmlEnumValue("review_in_progress")
    REVIEW_IN_PROGRESS("review_in_progress"),
    @XmlEnumValue("rejected")
    REJECTED("rejected"),
    @XmlEnumValue("accepted")
    ACCEPTED("accepted"),
    @XmlEnumValue("deleted")
    DELETED("deleted"),
    @XmlEnumValue("waiting_for_approval")
    WAITING_FOR_APPROVAL("waiting_for_approval"),
    @XmlEnumValue("revision_needed")
    REVISION_NEEDED("revision_needed");
    private final String value;

    TState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TState fromValue(String v) {
        for (TState c: TState.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
