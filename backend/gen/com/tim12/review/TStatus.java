//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.17 at 10:13:59 PM CEST 
//


package com.tim12.review;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="REJECTED"/>
 *     &lt;enumeration value="ACCEPTED"/>
 *     &lt;enumeration value="REVISED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TStatus")
@XmlEnum
public enum TStatus {

    REJECTED,
    ACCEPTED,
    REVISED;

    public String value() {
        return name();
    }

    public static TStatus fromValue(String v) {
        return valueOf(v);
    }

}
