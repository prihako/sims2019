
package com.balicamp.soap.ws.iar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfBillDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfBillDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="billDetails" type="{urn:PaymentManagerControllerwsdl}BillDetailArray"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfBillDetail", propOrder = {

})
public class ArrayOfBillDetail {

    @XmlElement(required = true)
    protected BillDetailArray billDetails;

    /**
     * Gets the value of the billDetails property.
     * 
     * @return
     *     possible object is
     *     {@link BillDetailArray }
     *     
     */
    public BillDetailArray getBillDetails() {
        return billDetails;
    }

    /**
     * Sets the value of the billDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillDetailArray }
     *     
     */
    public void setBillDetails(BillDetailArray value) {
        this.billDetails = value;
    }

}
