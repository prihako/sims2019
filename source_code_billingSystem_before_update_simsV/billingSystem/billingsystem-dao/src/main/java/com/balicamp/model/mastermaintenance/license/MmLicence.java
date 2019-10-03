package com.balicamp.model.mastermaintenance.license;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;


/**
 * The persistent class for the MM_LICENCE database table.
 *
 */
@Entity
@Table(name="MM_LICENCE")
//@NamedQuery(name="MmLicence.findAll", query="SELECT m FROM MmLicence m")
public class MmLicence extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BS_LICENCE_ID")
	@SequenceGenerator(name="MM_BS_LICENCE_ID_SEQ", sequenceName="MM_BS_LICENCE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MM_BS_LICENCE_ID_SEQ")
	private Long bsLicenceId;

	@Column(name="BHP_ANNUAL_PERCENT")
	private BigDecimal bhpAnnualPercent;

	@Column(name="BHP_ANNUAL_VALUE")
	private BigDecimal bhpAnnualValue;

	@Column(name="BHP_B")
	private BigDecimal bhpB;

	@Column(name="BHP_C")
	private BigDecimal bhpC;

	@Column(name="BHP_CALC_INDEX")
	private BigDecimal bhpCalcIndex;

	@Column(name="BHP_DELTA")
	private BigDecimal bhpDelta;

	@Column(name="BHP_I")
	private BigDecimal bhpI;

	@Column(name="BHP_IPSFR")
	private BigDecimal bhpIpsfr;

	@Column(name="BHP_ISR_X")
	private BigDecimal bhpIsrX;

	@Column(name="BHP_METHOD")
	private String bhpMethod;

	@Column(name="BHP_N_K")
	private BigDecimal bhpNK;

	@Column(name="BHP_PAYMENT_TYPE")
	private String bhpPaymentType;

	@Column(name="BHP_PHL")
	private BigDecimal bhpPhl;

	@Column(name="BHP_RATE")
	private BigDecimal bhpRate;

	@Column(name="BHP_TOTAL")
	private BigDecimal bhpTotal;

	@Column(name="BHP_UPFRONT_FEE")
	private BigDecimal bhpUpfrontFee;

	@Column(name="CLIENT_ID")
	private String clientId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_ON")
	private Date createdOn;

	@Temporal(TemporalType.DATE)
	@Column(name="EXPIRED_DATE")
	private Date expiredDate;

	@Column(name="INSTALLMENT_PENALTY")
	private String installmentPenalty;

	@Column(name="INSTALLMENT_TENOR")
	private BigDecimal installmentTenor;

	@Column(name="INSTALLMENT_TYPE")
	private String installmentType;

	@Temporal(TemporalType.DATE)
	@Column(name="LICENCE_BEGIN")
	private Date licenceBegin;

	@Temporal(TemporalType.DATE)
	@Column(name="LICENCE_DATE")
	private Date licenceDate;

	@Temporal(TemporalType.DATE)
	@Column(name="LICENCE_END")
	private Date licenceEnd;

	@Column(name="LICENCE_ID")
	private String licenceId;

	@Column(name="LICENCE_NO")
	private String licenceNo;

	@Column(name="LICENCE_NO_RATEBI")
	private String licenceNoRatebi;

	@Column(name="LICENCE_STATUS")
	private String licenceStatus;

	@Column(name="RECEIVE_MAX")
	private BigDecimal receiveMax;

	@Column(name="RECEIVE_MIN")
	private BigDecimal receiveMin;

	@Column(name="SERVICE_ID")
	private String serviceId;

	@Column(name="SUBSERVICE_ID")
	private String subServiceId;

	@Column(name="TRANSMIT_MAX")
	private BigDecimal transmitMax;

	@Column(name="TRANSMIT_MIN")
	private BigDecimal transmitMin;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_ON")
	private Date updatedOn;

	@Column(name="YEAR_TO")
	private BigDecimal yearTo;

	public MmLicence() {
	}

	public long getBsLicenceId() {
		return this.bsLicenceId;
	}

	public void setBsLicenceId(long bsLicenceId) {
		this.bsLicenceId = bsLicenceId;
	}

	public BigDecimal getBhpAnnualPercent() {
		return this.bhpAnnualPercent;
	}

	public void setBhpAnnualPercent(BigDecimal bhpAnnualPercent) {
		this.bhpAnnualPercent = bhpAnnualPercent;
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

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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

	public Date getExpiredDate() {
		return this.expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
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

	public String getLicenceNoRatebi() {
		return this.licenceNoRatebi;
	}

	public void setLicenceNoRatebi(String licenceNoRatebi) {
		this.licenceNoRatebi = licenceNoRatebi;
	}

	public String getLicenceStatus() {
		return this.licenceStatus;
	}

	public void setLicenceStatus(String licenceStatus) {
		this.licenceStatus = licenceStatus;
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

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSubServiceId() {
		return this.subServiceId;
	}

	public void setSubServiceId(String subServiceId) {
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
	public String getPsdValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPsdLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPsdDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSequenceName() {
		// TODO Auto-generated method stub
		return "mm_bs_licence_id_seq";
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}