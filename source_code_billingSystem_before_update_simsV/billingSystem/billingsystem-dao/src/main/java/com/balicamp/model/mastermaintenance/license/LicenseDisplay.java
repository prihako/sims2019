package com.balicamp.model.mastermaintenance.license;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.balicamp.model.common.BaseObject;

/**
 * class for displaying license in Master Maintenance Screen
 * 
 * @author yohan
 * 
 */
public class LicenseDisplay extends BaseObject {
	private static final long serialVersionUID = 346630580550794172L;
	
	private Long bsLicenceId;
	private BigDecimal serviceId;
	private BigDecimal subServiceId;
	private String bhpMethod;
	private BigDecimal clientID;
	private String clientName;
	private String licenceId;
	private String licenceNo;
	private Date licenceDate;
	private BigDecimal yearTo;
	private Date licenceBegin;
	private Date licenceEnd;
	private BigDecimal transmitMax;
	private BigDecimal transmitMin;
	private BigDecimal receiveMin;
	private BigDecimal receiveMax;
	private BigDecimal bhpTotal;
	private String bhpPaymentType;
	private String licenceStatus;


	public LicenseDisplay(Long bsLicenceId, String licenceId, String licenceNo,
			BigDecimal yearTo, Date licenceDate, Date licenceBegin,
			Date licenceEnd, BigDecimal transmitMax, BigDecimal transmitMin,
			BigDecimal receiveMin, BigDecimal receiveMax, BigDecimal serviceId,
			BigDecimal subServiceId, String bhpMethod, String clientName,
			BigDecimal clientID, BigDecimal bhpTotal, String bhpPaymentType,
			String licenceStatus) {
		this.bsLicenceId = bsLicenceId;
		this.serviceId = serviceId;
		this.subServiceId = subServiceId;
		this.bhpMethod = bhpMethod;
		this.clientID = clientID;
		this.clientName = clientName;
		this.licenceId = licenceId;
		this.licenceNo = licenceNo;
		this.licenceDate = licenceDate;
		this.yearTo = yearTo;
		this.licenceBegin = licenceBegin;
		this.licenceEnd = licenceEnd;
		this.transmitMin = transmitMin;
		this.transmitMax = transmitMax;
		this.receiveMin = receiveMin;
		this.receiveMax = receiveMax;
		this.bhpTotal = bhpTotal;
		this.bhpPaymentType = bhpPaymentType;
		this.licenceStatus = licenceStatus;

	}

	public String getBhpMethod() {
		return bhpMethod;
	}

	public void setBhpMethod(String bhpMethod) {
		this.bhpMethod = bhpMethod;
	}

	public String getLicenceId() {
		return licenceId;
	}

	public void setLicenseId(String licenceId) {
		this.licenceId = licenceId;
	}

	public String getLicenseNo() {
		return licenceNo;
	}

	public void setLicenseNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	public Date getLicenceDate() {
		return licenceDate;
	}

	public void setLicenceDate(Date licenceDate) {
		this.licenceDate = licenceDate;
	}

	public BigDecimal getYearTo() {
		return yearTo;
	}

	public void setYearTo(BigDecimal yearTo) {
		this.yearTo = yearTo;
	}

	public Date getLicenceBegin() {
		return licenceBegin;
	}

	public void setLicenceBegin(Date licenceBegin) {
		this.licenceBegin = licenceBegin;
	}

	public Date getLicenceEnd() {
		return licenceEnd;
	}

	public void setLicenceEnd(Date licenceEnd) {
		this.licenceEnd = licenceEnd;
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

	public BigDecimal getBhpTotal() {
		return bhpTotal;
	}

	public void setBhpTotal(BigDecimal bhpTotal) {
		this.bhpTotal = bhpTotal;
	}

	public String getBhpPaymentType() {
		return bhpPaymentType;
	}

	public void setBhpPaymentType(String bhpPaymentType) {
		this.bhpPaymentType = bhpPaymentType;
	}

	public String getLicenceStatus() {
		return licenceStatus;
	}

	public void setLicenceStatus(String licenceStatus) {
		this.licenceStatus = licenceStatus;
	}

	public Long getBsLicenceId() {
		return bsLicenceId;
	}

	public void setBsLicenceId(Long bsLicenceId) {
		this.bsLicenceId = bsLicenceId;
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

	public BigDecimal getClientID() {
		return clientID;
	}

	public void setClientID(BigDecimal clientID) {
		this.clientID = clientID;
	}

	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	

	
	@Override
	public String toString() {
		return new StringBuffer(this.licenceNo).toString();

	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof LicenseDisplay)) {
			return false;
		}
		LicenseDisplay rhs = (LicenseDisplay) object;
		return new EqualsBuilder().append(this.licenceId, rhs.licenceId)
				.append(this.licenceNo, rhs.licenceNo)
				.append(this.licenceDate, rhs.licenceDate)
				.append(this.licenceBegin, rhs.licenceBegin)
				.append(this.licenceEnd, rhs.licenceEnd)
				.append(this.transmitMin, rhs.transmitMin)
				.append(this.transmitMax, rhs.transmitMax)
				.append(this.receiveMin, rhs.receiveMin)
				.append(this.receiveMax, rhs.receiveMax)
				.append(this.bhpTotal, rhs.bhpTotal)
				.append(this.bhpPaymentType, rhs.bhpPaymentType)
				.append(this.licenceStatus, rhs.licenceStatus).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1859313099, 613304453)
				.append(this.licenceId).append(this.licenceNo)
				.append(this.licenceDate).append(this.licenceBegin)
				.append(this.licenceEnd).append(this.transmitMin)
				.append(this.transmitMax).append(this.receiveMin)
				.append(this.receiveMax).append(this.bhpTotal)
				.append(this.bhpPaymentType).append(this.licenceStatus)
				.toHashCode();

	}

}
