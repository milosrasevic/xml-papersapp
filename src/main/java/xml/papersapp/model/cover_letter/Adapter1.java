//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.12 at 03:41:55 PM CEST 
//


package xml.papersapp.model.cover_letter;

import xml.papersapp.util.MyDatatypeConverter;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (MyDatatypeConverter.parseDate(value));
    }

    public String marshal(Date value) {
        return (MyDatatypeConverter.printDate(value));
    }

}
