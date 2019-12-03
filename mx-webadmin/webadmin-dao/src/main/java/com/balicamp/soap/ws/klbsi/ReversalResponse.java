
package com.balicamp.soap.ws.klbsi;

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
 *         &lt;element name="reversalResult" type="{urn:PaymentManagerControllerwsdl}ReversalResult"/>
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
    "reversalResult"
})
@XmlRootElement(name = "reversalResponse")
public class ReversalResponse {

    @XmlElement(required = true)
    protected ReversalResult reversalResult;

    /**
     * Gets the value of the reversalResult property.
     * 
     * @return
     *     possible object is
     *     {@link ReversalResult }
     *     
     */
    public ReversalResult getReversalResult() {
        return reversalResult;
    }

    /**
     * Sets the value of the reversalResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReversalResult }
     *     
     */
    public void setReversalResult(ReversalResult value) {
        this.reversalResult = value;
    }

}
