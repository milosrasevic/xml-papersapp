//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.11 at 02:09:07 PM CEST 
//


package xml.papersapp.model.user;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tim12.user package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _User_QNAME = new QName("http://www.tim12.com/user", "User");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tim12.user
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TUser }
     * 
     */
    public TUser createTUser() {
        return new TUser();
    }

    /**
     * Create an instance of {@link TWorkplace }
     * 
     */
    public TWorkplace createTWorkplace() {
        return new TWorkplace();
    }

    /**
     * Create an instance of {@link TRoles }
     * 
     */
    public TRoles createTRoles() {
        return new TRoles();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tim12.com/user", name = "User")
    public JAXBElement<TUser> createUser(TUser value) {
        return new JAXBElement<TUser>(_User_QNAME, TUser.class, null, value);
    }

}
