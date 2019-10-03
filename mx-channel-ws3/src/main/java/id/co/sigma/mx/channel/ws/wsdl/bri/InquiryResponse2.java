
package id.co.sigma.mx.channel.ws.wsdl.bri;

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
 *         &lt;element name="inquiryResult" type="{bri.h2h.billpayment.ws}InquiryResponse" minOccurs="0"/>
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
@XmlRootElement(name = "inquiryResponse2")
public class InquiryResponse2 {

    protected InquiryResponse inquiryResult;

    /**
     * Gets the value of the inquiryResult property.
     * 
     * @return
     *     possible object is
     *     {@link InquiryResponse }
     *     
     */
    public InquiryResponse getInquiryResult() {
        return inquiryResult;
    }

    /**
     * Sets the value of the inquiryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link InquiryResponse }
     *     
     */
    public void setInquiryResult(InquiryResponse value) {
        this.inquiryResult = value;
    }

}
