//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.12 at 03:39:33 PM CEST 
//


package com.tim12.science_paper;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter2
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (src/main/java/xml/papersapp/util/MyDatatypeConverter.parseDate(value));
    }

    public String marshal(Date value) {
        return (src/main/java/xml/papersapp/util/MyDatatypeConverter.printDate(value));
    }

}
