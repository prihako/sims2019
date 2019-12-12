
package com.balicamp.soap.ws.klbsi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InquiryRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InquiryRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="trxDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="transmissionDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="companyCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="channelID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billKey1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billKey2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billKey3" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "InquiryRequest", propOrder = {

})
public class InquiryRequest {

    @XmlElement(required = true)
    protected String language;
    @XmlElement(required = true)
    protected String trxDateTime;
    @XmlElement(required = true)
    protected String transmissionDateTime;
    @XmlElement(required = true)
    protected String companyCode;
    @XmlElement(required = true)
    protected String channelID;
    @XmlElement(required = true)
    protected String billKey1;
    @XmlElement(required = true)
    protected String billKey2;
    @XmlElement(required = true)
    protected String billKey3;
    @XmlElement(required = true)
    protected String reference1;
    @XmlElement(required = true)
    protected String reference2;
    @XmlElement(required = true)
    protected String reference3;

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the trxDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrxDateTime() {
        return trxDateTime;
    }

    /**
     * Sets the value of the trxDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrxDateTime(String value) {
        this.trxDateTime = value;
    }

    /**
     * Gets the value of the transmissionDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmissionDateTime() {
        return transmissionDateTime;
    }

    /**
     * Sets the value of the transmissionDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmissionDateTime(String value) {
        this.transmissionDateTime = value;
    }

    /**
     * Gets the value of the companyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * Sets the value of the companyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyCode(String value) {
        this.companyCode = value;
    }

    /**
     * Gets the value of the channelID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelID() {
        return channelID;
    }

    /**
     * Sets the value of the channelID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelID(String value) {
        this.channelID = value;
    }

    /**
     * Gets the value of the billKey1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillKey1() {
        return billKey1;
    }

    /**
     * Sets the value of the billKey1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillKey1(String value) {
        this.billKey1 = value;
    }

    /**
     * Gets the value of the billKey2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillKey2() {
        return billKey2;
    }

    /**
     * Sets the value of the billKey2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillKey2(String value) {
        this.billKey2 = value;
    }

    /**
     * Gets the value of the billKey3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillKey3() {
        return billKey3;
    }

    /**
     * Sets the value of the billKey3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillKey3(String value) {
        this.billKey3 = value;
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

	@Override
	public String toString() {
		return "InquiryRequest [language=" + language + ", trxDateTime=" + trxDateTime + ", transmissionDateTime="
				+ transmissionDateTime + ", companyCode=" + companyCode + ", channelID=" + channelID + ", billKey1="
				+ billKey1 + ", billKey2=" + billKey2 + ", billKey3=" + billKey3 + ", reference1=" + reference1
				+ ", reference2=" + reference2 + ", reference3=" + reference3 + "]";
	}
    
    

}
