//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.11 at 12:38:02 PM CEST 
//


package xml.papersapp.model.science_papers;

import xml.papersapp.model.science_paper.SciencePaper;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{http://www.tim12.com/science_paper}SciencePaper"/>
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
    "sciencePaper"
})
@XmlRootElement(name = "SciencePapers")
public class SciencePapers {

    @XmlElement(name = "SciencePaper", namespace = "http://www.tim12.com/science_paper")
    protected List<SciencePaper> sciencePaper;

    /**
     * Gets the value of the sciencePaper property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sciencePaper property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSciencePaper().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SciencePaper }
     * 
     * 
     */
    public List<SciencePaper> getSciencePaper() {
        if (sciencePaper == null) {
            sciencePaper = new ArrayList<SciencePaper>();
        }
        return this.sciencePaper;
    }

}
