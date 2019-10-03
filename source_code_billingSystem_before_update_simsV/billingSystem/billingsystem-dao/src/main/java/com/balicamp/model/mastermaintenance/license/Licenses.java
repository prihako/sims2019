package com.balicamp.model.mastermaintenance.license;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
//import com.balicamp.model.mastermaintenance.service.Services;
//import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.model.ui.PropertySelectionData;


/**
 * The persistent class for the MM_LICENCES database table.
 *
 */
@Entity
@Table(name="MM_LICENCES")
public class Licenses extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BS_LICENCE_ID")
	@SequenceGenerator(name="MM_BS_LICENCE_ID_SEQ", sequenceName="MM_BS_LICENCE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MM_BS_LICENCE_ID_SEQ")
	public Long bsLicenceId;

	@Column(name="BHP_ANNUAL_PERCENT")
	public BigDecimal bhpAnnualPercent;

	@Column(name="BHP_ANNUAL_RATE_ALL")
	public String bhpAnnualRateAll;

	@Column(name="BHP_ANNUAL_VALUE")
	public BigDecimal bhpAnnualValue;

	@Column(name="BHP_B")
	public BigDecimal bhpB;

	@Column(name="BHP_C")
	public BigDecimal bhpC;

	@Column(name="BHP_CALC_INDEX")
	public BigDecimal bhpCalcIndex;

	@Column(name="BHP_DELTA")
	public BigDecimal bhpDelta;

	@Column(name="BHP_I")
	public BigDecimal bhpI;

	@Column(name="BHP_IPSFR")
	public BigDecimal bhpIpsfr;

	@Column(name="BHP_ISR_X")
	public BigDecimal bhpIsrX;

	@Column(name="BHP_METHOD")
	public String bhpMethod;

	@Column(name="BHP_N_K")
	public BigDecimal bhpNK;

	@Column(name="BHP_PAYMENT_TYPE")
	public String bhpPaymentType;

	@Column(name="BHP_PHL")
	public BigDecimal bhpPhl;

	@Column(name="BHP_HL")
	public BigDecimal bhpHl;

	public BigDecimal getBhpHl() {
		return bhpHl;
	}

	public void setBhpHl(BigDecimal bhpHl) {
		this.bhpHl = bhpHl;
	}

	@Column(name="BHP_RATE")
	public BigDecimal bhpRate;

	@Column(name="BHP_TOTAL")
	public BigDecimal bhpTotal;

	@Column(name="BHP_UPFRONT_FEE")
	public BigDecimal bhpUpfrontFee;

	@Column(name="CLIENT_ID")
	public BigDecimal clientID;

	@Column(name="CLIENT_NAME")
	public String clientName;

	@Column(name="CREATED_BY")
	public String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_ON")
	public Date createdOn;

	@Column(name="INSTALLMENT_PENALTY")
	public String installmentPenalty;

	@Column(name="INSTALLMENT_TENOR")
	public BigDecimal installmentTenor;

	@Column(name="INSTALLMENT_TYPE")
	public String installmentType;

	@Column(name="INVOICE_CREATED")
	public String invoiceCreated;

	@Temporal(TemporalType.DATE)
	@Column(name="LICENCE_BEGIN")
	public Date licenceBegin;

	@Temporal(TemporalType.DATE)
	@Column(name="LICENCE_DATE")
	public Date licenceDate;

	@Temporal(TemporalType.DATE)
	@Column(name="LICENCE_END")
	public Date licenceEnd;

	@Column(name="LICENCE_ID")
	public String licenceId;

	@Column(name="LICENCE_NO")
	public String licenceNo;

	@Column(name="LICENCE_NO_BIRATE")
	public String licenceNoBirate;

	@Column(name="LICENCE_EXPIRED_DATE")
	public Date licenceExpireDate;

	@Column(name="LICENCE_COMMENT")
	public String licenceComment;

	public String getLicenceComment() {
		return licenceComment;
	}

	public void setLicenceComment(String licenceComment) {
		this.licenceComment = licenceComment;
	}

	@Column(name="LICENCE_STATUS")
	public String licenceStatus;

	@Temporal(TemporalType.DATE)
	@Column(name="PAYMENT_DUE_DATE")
	public Date paymentDueDate;

	@Column(name="RECEIVE_MAX")
	public BigDecimal receiveMax;

	@Column(name="RECEIVE_MIN")
	public BigDecimal receiveMin;

	@Column(name="SERVICE_ID")
	public BigDecimal serviceId;

	@Column(name="SUBSERVICE_ID")
	public BigDecimal subServiceId;

	@Column(name="TRANSMIT_MAX")
	public BigDecimal transmitMax;

	@Column(name="TRANSMIT_MIN")
	public BigDecimal transmitMin;

	@Column(name="UPDATED_BY")
	public String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_ON")
	public Date updatedOn;

	@Column(name="YEAR_TO")
	public BigDecimal yearTo;

	public Licenses() {
	}

	public Long getBsLicenceId() {
		return this.bsLicenceId;
	}

	public void setBsLicenceId(Long bsLicenceId) {
		this.bsLicenceId = bsLicenceId;
	}

	public BigDecimal getBhpAnnualPercent() {
		return this.bhpAnnualPercent;
	}

	public void setBhpAnnualPercent(BigDecimal bhpAnnualPercent) {
		this.bhpAnnualPercent = bhpAnnualPercent;
	}

	public String getBhpAnnualRateAll() {
		return this.bhpAnnualRateAll;
	}

	public void setBhpAnnualRateAll(String bhpAnnualRateAll) {
		this.bhpAnnualRateAll = bhpAnnualRateAll;
	}

	public BigDecimal getBhpAnnualValue() {
		return this.bhpAnnualValue;
	}

	public void setBhpAnnualValue(BigDecimal bhpAnnualValue) {
		this.bhpAnnualValue = bhpAnnualValue;
	}

	public BigDecimal getBhpB() {
		return this.bhpB;
	}

	public void setBhpB(BigDecimal bhpB) {
		this.bhpB = bhpB;
	}

	public BigDecimal getBhpC() {
		return this.bhpC;
	}

	public void setBhpC(BigDecimal bhpC) {
		this.bhpC = bhpC;
	}

	public BigDecimal getBhpCalcIndex() {
		return this.bhpCalcIndex;
	}

	public void setBhpCalcIndex(BigDecimal bhpCalcIndex) {
		this.bhpCalcIndex = bhpCalcIndex;
	}

	public BigDecimal getBhpDelta() {
		return this.bhpDelta;
	}

	public void setBhpDelta(BigDecimal bhpDelta) {
		this.bhpDelta = bhpDelta;
	}

	public BigDecimal getBhpI() {
		return this.bhpI;
	}

	public void setBhpI(BigDecimal bhpI) {
		this.bhpI = bhpI;
	}

	public BigDecimal getBhpIpsfr() {
		return this.bhpIpsfr;
	}

	public void setBhpIpsfr(BigDecimal bhpIpsfr) {
		this.bhpIpsfr = bhpIpsfr;
	}

	public BigDecimal getBhpIsrX() {
		return this.bhpIsrX;
	}

	public void setBhpIsrX(BigDecimal bhpIsrX) {
		this.bhpIsrX = bhpIsrX;
	}

	public String getBhpMethod() {
		return this.bhpMethod;
	}

	public void setBhpMethod(String bhpMethod) {
		this.bhpMethod = bhpMethod;
	}

	public BigDecimal getBhpNK() {
		return this.bhpNK;
	}

	public void setBhpNK(BigDecimal bhpNK) {
		this.bhpNK = bhpNK;
	}

	public String getBhpPaymentType() {
		return this.bhpPaymentType;
	}

	public void setBhpPaymentType(String bhpPaymentType) {
		this.bhpPaymentType = bhpPaymentType;
	}

	public BigDecimal getBhpPhl() {
		return this.bhpPhl;
	}

	public void setBhpPhl(BigDecimal bhpPhl) {
		this.bhpPhl = bhpPhl;
	}

	public BigDecimal getBhpRate() {
		return this.bhpRate;
	}

	public void setBhpRate(BigDecimal bhpRate) {
		this.bhpRate = bhpRate;
	}

	public BigDecimal getBhpTotal() {
		return this.bhpTotal;
	}

	public void setBhpTotal(BigDecimal bhpTotal) {
		this.bhpTotal = bhpTotal;
	}

	public BigDecimal getBhpUpfrontFee() {
		return this.bhpUpfrontFee;
	}

	public void setBhpUpfrontFee(BigDecimal bhpUpfrontFee) {
		this.bhpUpfrontFee = bhpUpfrontFee;
	}

	public BigDecimal getClientId() {
		return this.clientID;
	}

	public void setClientId(BigDecimal clientId) {
		this.clientID = clientId;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getInstallmentPenalty() {
		return this.installmentPenalty;
	}

	public void setInstallmentPenalty(String installmentPenalty) {
		this.installmentPenalty = installmentPenalty;
	}

	public BigDecimal getInstallmentTenor() {
		return this.installmentTenor;
	}

	public void setInstallmentTenor(BigDecimal installmentTenor) {
		this.installmentTenor = installmentTenor;
	}

	public String getInstallmentType() {
		return this.installmentType;
	}

	public void setInstallmentType(String installmentType) {
		this.installmentType = installmentType;
	}

	public String getInvoiceCreated() {
		return this.invoiceCreated;
	}

	public void setInvoiceCreated(String invoiceCreated) {
		this.invoiceCreated = invoiceCreated;
	}

	public Date getLicenceBegin() {
		return this.licenceBegin;
	}

	public void setLicenceBegin(Date licenceBegin) {
		this.licenceBegin = licenceBegin;
	}

	public Date getLicenceDate() {
		return this.licenceDate;
	}

	public void setLicenceDate(Date licenceDate) {
		this.licenceDate = licenceDate;
	}

	public Date getLicenceEnd() {
		return this.licenceEnd;
	}

	public void setLicenceEnd(Date licenceEnd) {
		this.licenceEnd = licenceEnd;
	}

	public String getLicenceId() {
		return this.licenceId;
	}

	public void setLicenceId(String licenceId) {
		this.licenceId = licenceId;
	}

	public String getLicenceNo() {
		return this.licenceNo;
	}

	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	public String getLicenceNoBirate() {
		return this.licenceNoBirate;
	}

	public void setLicenceNoBirate(String licenceNoBirate) {
		this.licenceNoBirate = licenceNoBirate;
	}

	public Date getLicenceExpireDate() {
		return licenceExpireDate;
	}

	public void setLicenceExpireDate(Date licenceExpireDate) {
		this.licenceExpireDate = licenceExpireDate;
	}

	public String getLicenceStatus() {
		return this.licenceStatus;
	}

	public void setLicenceStatus(String licenceStatus) {
		this.licenceStatus = licenceStatus;
	}

	public Date getPaymentDueDate() {
		return this.paymentDueDate;
	}

	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public BigDecimal getReceiveMax() {
		return this.receiveMax;
	}

	public void setReceiveMax(BigDecimal receiveMax) {
		this.receiveMax = receiveMax;
	}

	public BigDecimal getReceiveMin() {
		return this.receiveMin;
	}

	public void setReceiveMin(BigDecimal receiveMin) {
		this.receiveMin = receiveMin;
	}

	public BigDecimal getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	public BigDecimal getSubServiceId() {
		return this.subServiceId;
	}

	public void setSubServiceId(BigDecimal subServiceId) {
		this.subServiceId = subServiceId;
	}

	public BigDecimal getTransmitMax() {
		return this.transmitMax;
	}

	public void setTransmitMax(BigDecimal transmitMax) {
		this.transmitMax = transmitMax;
	}

	public BigDecimal getTransmitMin() {
		return this.transmitMin;
	}

	public void setTransmitMin(BigDecimal transmitMin) {
		this.transmitMin = transmitMin;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public BigDecimal getYearTo() {
		return this.yearTo;
	}

	public void setYearTo(BigDecimal yearTo) {
		this.yearTo = yearTo;
	}

	@Override
	public Object getPKey() {
		return null;
	}

	@Override
	public String getPsdValue() {
		return null;
	}

	@Override
	public String getPsdLabel() {
		return null;
	}

	@Override
	public boolean isPsdDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSequenceName() {
		return "mm_bs_licence_id_seq";
	}

	@Override
	public Long getId() {

		return null;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub

	}

	@Transient
	public boolean selected;

	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Licenses(Licenses license) {
		super();
		this.setBsLicenceId(license.getBsLicenceId());
		this.setLicenceNo(license.getLicenceNo());
		this.setLicenceExpireDate(license.getLicenceExpireDate());
		this.setLicenceId(license.getLicenceId());
		this.setServiceId(license.getServiceId());
		this.setSubServiceId(license.getSubServiceId());
		this.setClientName(license.getClientName());
		this.setClientId(license.getClientId());
		this.setLicenceBegin(license.getLicenceBegin());
		this.setLicenceEnd(license.getLicenceEnd());
		this.setTransmitMin(license.getTransmitMin());
		this.setTransmitMax(license.getTransmitMax());
		this.setReceiveMin(license.getReceiveMin());
		this.setReceiveMax(license.getReceiveMax());
		this.setLicenceDate(license.getLicenceDate());
		this.setYearTo(license.getYearTo());
		this.setBhpMethod(license.getBhpMethod());
		this.setBhpHl(license.getBhpHl());
		this.setBhpUpfrontFee(license.getBhpUpfrontFee());
		this.setBhpCalcIndex(license.getBhpCalcIndex());
		this.setBhpPaymentType(license.getBhpPaymentType());
		this.setPaymentDueDate(license.getPaymentDueDate());
		this.setBhpTotal(license.getBhpTotal());
	}



}