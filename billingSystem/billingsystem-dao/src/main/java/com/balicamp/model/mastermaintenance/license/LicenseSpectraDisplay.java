package com.balicamp.model.mastermaintenance.license;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.balicamp.model.common.BaseObject;

/**
 * class for displaying license in Master Maintenance Screen
 * 
 * @author yohan
 * 
 */
public class LicenseSpectraDisplay extends BaseObject {
	private static final long serialVersionUID = 346630580550794172L;
	
	private BigDecimal serviceId;
	private BigDecimal subServiceId;
	private String addressNumber;
	private String addressCompany;
	private String licenceNumber;
	private String apRefNumber;
	private BigDecimal transmitMax;
	private BigDecimal transmitMin;
	private BigDecimal receiveMin;
	private BigDecimal receiveMax;
	
	private boolean selected;

	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public BigDecimal getServiceId() {
		return serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}






	public BigDecimal getSubServiceId() {
		return subServiceId;
	}






	public void setSubServiceId(BigDecimal subServiceId) {
		this.subServiceId = subServiceId;
	}






	public String getAddress_number() {
		return addressNumber;
	}






	public void setAddress_number(String address_number) {
		this.addressNumber = address_number;
	}






	public String getAddress_name() {
		return addressCompany;
	}






	public void setAddress_name(String address_company) {
		this.addressCompany = address_company;
	}






	public String getLicence_number() {
		return licenceNumber;
	}






	public void setLicence_number(String licence_number) {
		this.licenceNumber = licence_number;
	}






	public String getAp_ref_number() {
		return apRefNumber;
	}






	public void setAp_ref_number(String ap_ref_number) {
		this.apRefNumber = ap_ref_number;
	}






	public BigDecimal getTransmitMax() {
		return transmitMax;
	}






	public void setTransmitMax(BigDecimal transmitMax) {
		this.transmitMax = transmitMax;
	}






	public BigDecimal getTransmitMin() {
		return transmitMin;
	}






	public void setTransmitMin(BigDecimal transmitMin) {
		this.transmitMin = transmitMin;
	}






	public BigDecimal getReceiveMin() {
		return receiveMin;
	}






	public void setReceiveMin(BigDecimal receiveMin) {
		this.receiveMin = receiveMin;
	}






	public BigDecimal getReceiveMax() {
		return receiveMax;
	}






	public void setReceiveMax(BigDecimal receiveMax) {
		this.receiveMax = receiveMax;
	}



	

	public LicenseSpectraDisplay(String licenceNumber, String apRefNumber, BigDecimal transmitMax, BigDecimal transmitMin,
			BigDecimal receiveMin, BigDecimal receiveMax) {
		this.serviceId = serviceId;
		this.subServiceId = subServiceId;
		this.addressNumber = addressNumber;
		this.addressCompany = addressCompany;
		this.licenceNumber = licenceNumber;
		this.apRefNumber = apRefNumber;
		this.transmitMin = transmitMin;
		this.transmitMax = transmitMax;
		this.receiveMin = receiveMin;
		this.receiveMax = receiveMax;

	}


	

	

	@Override
	public String toString() {
		return new StringBuffer(this.apRefNumber).toString();

	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof LicenseSpectraDisplay)) {
			return false;
		}
		LicenseSpectraDisplay rhs = (LicenseSpectraDisplay) object;
		return new EqualsBuilder().append(this.serviceId, rhs.serviceId)
				.append(this.subServiceId, rhs.subServiceId)
				.append(this.licenceNumber, rhs.licenceNumber)
				.append(this.apRefNumber, rhs.apRefNumber)
				.append(this.addressNumber, rhs.addressNumber)
				.append(this.addressCompany, rhs.addressCompany)
				.append(this.transmitMin, rhs.transmitMin)
				.append(this.transmitMax, rhs.transmitMax)
				.append(this.receiveMin, rhs.receiveMin)
				.append(this.receiveMax, rhs.receiveMax).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1859313099, 613304453)
				.append(this.serviceId).append(this.subServiceId)
				.append(this.licenceNumber).append(this.apRefNumber)
				.append(this.addressNumber).append(this.addressCompany)
				.append(this.transmitMin).append(this.transmitMax)
				.append(this.receiveMin).append(this.receiveMax)
				.toHashCode();

	}

}
