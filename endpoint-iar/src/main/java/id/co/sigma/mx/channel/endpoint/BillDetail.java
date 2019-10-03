
package id.co.sigma.mx.channel.endpoint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BillDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="billCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billShortName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billAmount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reference1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reference2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reference3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BillDetail", propOrder = {

})
public class BillDetail {

    @XmlElement(required = true)
    protected String billCode;
    @XmlElement(required = true)
    protected String billName;
    @XmlElement(required = true)
    protected String billShortName;
    @XmlElement(required = true)
    protected String billAmount;
    @XmlElement(required = true)
    protected String reference1;
    @XmlElement(required = true)
    protected String reference2;
    @XmlElement(required = true)
    protected String reference3;

    /**
     * Gets the value of the billCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillCode() {
        return billCode;
    }

    /**
     * Sets the value of the billCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillCode(String value) {
        this.billCode = value;
    }

    /**
     * Gets the value of the billName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillName() {
        return billName;
    }

    /**
     * Sets the value of the billName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillName(String value) {
        this.billName = value;
    }

    /**
     * Gets the value of the billShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillShortName() {
        return billShortName;
    }

    /**
     * Sets the value of the billShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillShortName(String value) {
        this.billShortName = value;
    }

    /**
     * Gets the value of the billAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillAmount() {
        return billAmount;
    }

    /**
     * Sets the value of the billAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillAmount(String value) {
        this.billAmount = value;
    }

    /**
     * Gets the value of the reference1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference1() {
        return reference1;
    }

    /**
     * Sets the value of the reference1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference1(String value) {
        this.reference1 = value;
    }

    /**
     * Gets the value of the reference2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference2() {
        return reference2;
    }

    /**
     * Sets the value of the reference2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference2(String value) {
        this.reference2 = value;
    }

    /**
     * Gets the value of the reference3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference3() {
        return reference3;
    }

    /**
     * Sets the value of the reference3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference3(String value) {
        this.reference3 = value;
    }

}
