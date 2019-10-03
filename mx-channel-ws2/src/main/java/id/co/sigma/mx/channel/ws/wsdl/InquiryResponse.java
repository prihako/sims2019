
package id.co.sigma.mx.channel.ws.wsdl;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InquiryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InquiryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="currency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo19" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo21" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo22" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo23" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo24" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billInfo25" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billKey1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billKey2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billKey3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billDetails" type="{bni.h2h.billpayment.ws}ArrayOfBillDetail" minOccurs="0"/>
 *         &lt;element name="status" type="{bni.h2h.billpayment.ws}Status" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InquiryResponse", propOrder = {
    "currency",
    "billInfo1",
    "billInfo2",
    "billInfo3",
    "billInfo4",
    "billInfo5",
    "billInfo6",
    "billInfo7",
    "billInfo8",
    "billInfo9",
    "billInfo10",
    "billInfo11",
    "billInfo12",
    "billInfo13",
    "billInfo14",
    "billInfo15",
    "billInfo16",
    "billInfo17",
    "billInfo18",
    "billInfo19",
    "billInfo21",
    "billInfo22",
    "billInfo23",
    "billInfo24",
    "billInfo25",
    "billKey1",
    "billKey2",
    "billKey3",
    "billDetails",
    "status"
})
public class InquiryResponse {

    protected String currency;
    protected String billInfo1;
    protected String billInfo2;
    protected String billInfo3;
    protected String billInfo4;
    protected String billInfo5;
    protected String billInfo6;
    protected String billInfo7;
    protected String billInfo8;
    protected String billInfo9;
    protected String billInfo10;
    protected String billInfo11;
    protected String billInfo12;
    protected String billInfo13;
    protected String billInfo14;
    protected String billInfo15;
    protected String billInfo16;
    protected String billInfo17;
    protected String billInfo18;
    protected String billInfo19;
    protected String billInfo21;
    protected String billInfo22;
    protected String billInfo23;
    protected String billInfo24;
    protected String billInfo25;
    protected String billKey1;
    protected String billKey2;
    protected String billKey3;
    protected ArrayOfBillDetail billDetails;
    protected Status status;

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the billInfo1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo1() {
        return billInfo1;
    }

    /**
     * Sets the value of the billInfo1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo1(String value) {
        this.billInfo1 = value;
    }

    /**
     * Gets the value of the billInfo2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo2() {
        return billInfo2;
    }

    /**
     * Sets the value of the billInfo2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo2(String value) {
        this.billInfo2 = value;
    }

    /**
     * Gets the value of the billInfo3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo3() {
        return billInfo3;
    }

    /**
     * Sets the value of the billInfo3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo3(String value) {
        this.billInfo3 = value;
    }

    /**
     * Gets the value of the billInfo4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo4() {
        return billInfo4;
    }

    /**
     * Sets the value of the billInfo4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo4(String value) {
        this.billInfo4 = value;
    }

    /**
     * Gets the value of the billInfo5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo5() {
        return billInfo5;
    }

    /**
     * Sets the value of the billInfo5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo5(String value) {
        this.billInfo5 = value;
    }

    /**
     * Gets the value of the billInfo6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo6() {
        return billInfo6;
    }

    /**
     * Sets the value of the billInfo6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo6(String value) {
        this.billInfo6 = value;
    }

    /**
     * Gets the value of the billInfo7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo7() {
        return billInfo7;
    }

    /**
     * Sets the value of the billInfo7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo7(String value) {
        this.billInfo7 = value;
    }

    /**
     * Gets the value of the billInfo8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo8() {
        return billInfo8;
    }

    /**
     * Sets the value of the billInfo8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo8(String value) {
        this.billInfo8 = value;
    }

    /**
     * Gets the value of the billInfo9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo9() {
        return billInfo9;
    }

    /**
     * Sets the value of the billInfo9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo9(String value) {
        this.billInfo9 = value;
    }

    /**
     * Gets the value of the billInfo10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo10() {
        return billInfo10;
    }

    /**
     * Sets the value of the billInfo10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo10(String value) {
        this.billInfo10 = value;
    }

    /**
     * Gets the value of the billInfo11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo11() {
        return billInfo11;
    }

    /**
     * Sets the value of the billInfo11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo11(String value) {
        this.billInfo11 = value;
    }

    /**
     * Gets the value of the billInfo12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo12() {
        return billInfo12;
    }

    /**
     * Sets the value of the billInfo12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo12(String value) {
        this.billInfo12 = value;
    }

    /**
     * Gets the value of the billInfo13 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo13() {
        return billInfo13;
    }

    /**
     * Sets the value of the billInfo13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo13(String value) {
        this.billInfo13 = value;
    }

    /**
     * Gets the value of the billInfo14 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo14() {
        return billInfo14;
    }

    /**
     * Sets the value of the billInfo14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo14(String value) {
        this.billInfo14 = value;
    }

    /**
     * Gets the value of the billInfo15 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo15() {
        return billInfo15;
    }

    /**
     * Sets the value of the billInfo15 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo15(String value) {
        this.billInfo15 = value;
    }

    /**
     * Gets the value of the billInfo16 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo16() {
        return billInfo16;
    }

    /**
     * Sets the value of the billInfo16 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo16(String value) {
        this.billInfo16 = value;
    }

    /**
     * Gets the value of the billInfo17 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo17() {
        return billInfo17;
    }

    /**
     * Sets the value of the billInfo17 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo17(String value) {
        this.billInfo17 = value;
    }

    /**
     * Gets the value of the billInfo18 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo18() {
        return billInfo18;
    }

    /**
     * Sets the value of the billInfo18 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo18(String value) {
        this.billInfo18 = value;
    }

    /**
     * Gets the value of the billInfo19 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo19() {
        return billInfo19;
    }

    /**
     * Sets the value of the billInfo19 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo19(String value) {
        this.billInfo19 = value;
    }

    /**
     * Gets the value of the billInfo21 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo21() {
        return billInfo21;
    }

    /**
     * Sets the value of the billInfo21 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo21(String value) {
        this.billInfo21 = value;
    }

    /**
     * Gets the value of the billInfo22 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo22() {
        return billInfo22;
    }

    /**
     * Sets the value of the billInfo22 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo22(String value) {
        this.billInfo22 = value;
    }

    /**
     * Gets the value of the billInfo23 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo23() {
        return billInfo23;
    }

    /**
     * Sets the value of the billInfo23 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo23(String value) {
        this.billInfo23 = value;
    }

    /**
     * Gets the value of the billInfo24 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo24() {
        return billInfo24;
    }

    /**
     * Sets the value of the billInfo24 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo24(String value) {
        this.billInfo24 = value;
    }

    /**
     * Gets the value of the billInfo25 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillInfo25() {
        return billInfo25;
    }

    /**
     * Sets the value of the billInfo25 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillInfo25(String value) {
        this.billInfo25 = value;
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
     * Gets the value of the billDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfBillDetail }
     *     
     */
    public ArrayOfBillDetail getBillDetails() {
        return billDetails;
    }

    /**
     * Sets the value of the billDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfBillDetail }
     *     
     */
    public void setBillDetails(ArrayOfBillDetail value) {
        this.billDetails = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }
    
    @SuppressWarnings("unchecked")
	public InquiryResponse convertFromMap(Map map) {
			
//    	note based on BNI WS Specification Documents
    	InquiryResponse inquiryResponse = new InquiryResponse();
    	inquiryResponse.setCurrency(map.get("currency").toString());//value = 360
    	inquiryResponse.setBillInfo1(map.get("billInfo1").toString());//client name
    	inquiryResponse.setBillInfo2(map.get("billInfo2").toString());//period begin - yyyyMMdd
    	inquiryResponse.setBillInfo3(map.get("billInfo3").toString());//period end - yyyyMMdd
    	inquiryResponse.setBillKey1(map.get("billKey1").toString());
    	inquiryResponse.setBillKey2(map.get("billKey2").toString());
    	inquiryResponse.setBillKey3(map.get("billKey3").toString());

    	    	
    	BillDetail bill = new BillDetail();
    	bill.setBillCode(map.get("billCode").toString());
    	bill.setBillName(map.get("billName").toString());
    	bill.setBillShortName(map.get("billShortName").toString());
    	bill.setBillAmount(map.get("billAmount").toString());
    	
    	ArrayOfBillDetail billDetails = new ArrayOfBillDetail();
    	billDetails.getBillDetail().add(bill);
    	
    	inquiryResponse.setBillDetails(billDetails);
    	
    	Status status = new Status();
    	status.setErrorCode(map.get("errorCode").toString());
    	if(status.getErrorCode()!=null && status.getErrorCode().equals("00")){
    		status.setIsError(false);
    	}else{
    		status.setIsError(true);
    	}
    	status.setStatusDescription(map.get("statusDescription").toString());
    	inquiryResponse.setStatus(status);
        
		return inquiryResponse;
	}

}
