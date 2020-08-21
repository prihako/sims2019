
package id.co.sigma.mx.channel.ws.wsdl.bni.sya;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="reverseResult" type="{bni.h2h.billpayment.ws}ReversalResponse" minOccurs="0"/>
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
    "reverseResult"
})
@XmlRootElement(name = "reverseResponse")
public class ReverseResponse {

    protected ReversalResponse reverseResult;

    /**
     * Gets the value of the reverseResult property.
     * 
     * @return
     *     possible object is
     *     {@link ReversalResponse }
     *     
     */
    public ReversalResponse getReverseResult() {
        return reverseResult;
    }

    /**
     * Sets the value of the reverseResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReversalResponse }
     *     
     */
    public void setReverseResult(ReversalResponse value) {
        this.reverseResult = value;
    }

}
