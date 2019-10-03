
package id.co.sigma.mx.channel.ws;

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
 *         &lt;element name="inquiryResult" type="{urn:PaymentManagerControllerwsdl}InquiryResult"/>
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
    "inquiryResult"
})
@XmlRootElement(name = "inquiryResponse")
public class InquiryResponse {

    @XmlElement(required = true)
    protected InquiryResult inquiryResult;

    /**
     * Gets the value of the inquiryResult property.
     * 
     * @return
     *     possible object is
     *     {@link InquiryResult }
     *     
     */
    public InquiryResult getInquiryResult() {
        return inquiryResult;
    }

    /**
     * Sets the value of the inquiryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link InquiryResult }
     *     
     */
    public void setInquiryResult(InquiryResult value) {
        this.inquiryResult = value;
    }

}
