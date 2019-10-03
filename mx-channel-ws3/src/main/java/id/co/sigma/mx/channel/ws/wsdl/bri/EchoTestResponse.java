
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
 *         &lt;element name="echoTestResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "echoTestResult"
})
@XmlRootElement(name = "echoTestResponse")
public class EchoTestResponse {

    protected String echoTestResult;

    /**
     * Gets the value of the echoTestResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEchoTestResult() {
        return echoTestResult;
    }

    /**
     * Sets the value of the echoTestResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEchoTestResult(String value) {
        this.echoTestResult = value;
    }

}
