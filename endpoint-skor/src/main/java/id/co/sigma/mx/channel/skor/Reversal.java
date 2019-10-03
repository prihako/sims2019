
package id.co.sigma.mx.channel.skor;

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
 *       &lt;sequence>
 *         &lt;element name="req" type="{urn:PaymentManagerControllerwsdl}ReversalRequest"/>
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
    "req"
})
@XmlRootElement(name = "reversal")
public class Reversal {

    @XmlElement(required = true, nillable = true)
    protected ReversalRequest req;

    /**
     * Gets the value of the req property.
     * 
     * @return
     *     possible object is
     *     {@link ReversalRequest }
     *     
     */
    public ReversalRequest getReq() {
        return req;
    }

    /**
     * Sets the value of the req property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReversalRequest }
     *     
     */
    public void setReq(ReversalRequest value) {
        this.req = value;
    }

}
