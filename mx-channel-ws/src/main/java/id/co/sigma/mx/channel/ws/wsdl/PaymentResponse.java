
package id.co.sigma.mx.channel.ws.wsdl;

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
 *         &lt;element name="paymentResult" type="{bankmandiri.h2h.billpayment.ws}PaymentResponse" minOccurs="0"/>
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
    "paymentResult"
})
@XmlRootElement(name = "paymentResponse")
public class PaymentResponse {

    protected PaymentResponse2 paymentResult;

    /**
     * Gets the value of the paymentResult property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentResponse2 }
     *     
     */
    public PaymentResponse2 getPaymentResult() {
        return paymentResult;
    }

    /**
     * Sets the value of the paymentResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentResponse2 }
     *     
     */
    public void setPaymentResult(PaymentResponse2 value) {
        this.paymentResult = value;
    }

}
