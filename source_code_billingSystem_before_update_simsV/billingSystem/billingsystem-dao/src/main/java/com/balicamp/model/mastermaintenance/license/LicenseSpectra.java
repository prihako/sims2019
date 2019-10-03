package com.balicamp.model.mastermaintenance.license;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;


/**
 * The persistent class for the V_EBS_BANDWIDTH_ETX_ERX_TEMP database table.
 *
 */
@Entity
//@Table(name="SPECTRA.V_EBS_BANDWIDTH_ETX_ERX")
@Table(name="V_EBS_BANDWIDTH_ETX_ERX_TEMP")
//@Table(name="SPECTRA.V_EBS_BANDWIDTH_ETX_ERX")
//@NamedQuery(name="VEbsBandwidthEtxErxTemp.findAll", query="SELECT v FROM VEbsBandwidthEtxErxTemp v")
public class LicenseSpectra extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LICENCE_NUMBER")
	private String licenceNumber; //billing system equal to LICENSE ID

	@Column(name="AP_REF_NUMBER")
	private String apRefNumber; //billing system equal to LICENSE NUMBER (SKM)

	@Column(name="AP_CONCESSION_IDENT")
	private String apConcessionIdent;

	@Column(name="SV")
	private String sv;

	@Column(name="SV_ID")
	private BigDecimal svId;

	@Column(name="SS")
	private String ss;

	@Column(name="SS_ID")
	private BigDecimal ssId;

	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;


	@Column(name="LI_PERIOD_CAT_ID")
	private String liPeriodCatId;

	@Column(name="LICENCE_PERIOD_CATEGORY")
	private String licencePeriodCategory;

	@Column(name="ADDRESS_NUMBER")
	private String addressNumber;

	@Column(name="ADDRESS_COMPANY")
	private String addressCompany;

	@Column(name="CITY")
	private String city;

	@Column(name="EQUIP_TYPE")
	private String equipType;

	@Column(name="EQUIP_NAME")
	private String equipName;

	@Column(name="FREQUENCY_HZ", unique=true, nullable=false)
	private BigDecimal frequencyHz;

	@Column(name="BANDWIDTN_HZ")
	private BigDecimal bandwidtnHz;

	@Column(name="CHANNEL", unique=true)
	private String channel;

	@Column(name="EQ_FREQ_RANGE_MAX_ERX")
	private BigDecimal eqFreqRangeMaxErx;

	@Column(name="EQ_FREQ_RANGE_MAX_ETX")
	private BigDecimal eqFreqRangeMaxEtx;

	@Column(name="EQ_FREQ_RANGE_MIN_ERX")
	private BigDecimal eqFreqRangeMinErx;

	@Column(name="EQ_FREQ_RANGE_MIN_ETX")
	private BigDecimal eqFreqRangeMinEtx;


	public LicenseSpectra() {
	}

	public String getAddressCompany() {
		return this.addressCompany;
	}

	public void setAddressCompany(String addressCompany) {
		this.addressCompany = addressCompany;
	}

	public String getAddressNumber() {
		return this.addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public String getApConcessionIdent() {
		return this.apConcessionIdent;
	}

	public void setApConcessionIdent(String apConcessionIdent) {
		this.apConcessionIdent = apConcessionIdent;
	}

	public String getApRefNumber() {
		return this.apRefNumber;
	}

	public void setApRefNumber(String apRefNumber) {
		this.apRefNumber = apRefNumber;
	}

	public BigDecimal getBandwidtnHz() {
		return this.bandwidtnHz;
	}

	public void setBandwidtnHz(BigDecimal bandwidtnHz) {
		this.bandwidtnHz = bandwidtnHz;
	}

	public String getChannel() {
		return this.channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getEqFreqRangeMaxErx() {
		return this.eqFreqRangeMaxErx;
	}

	public void setEqFreqRangeMaxErx(BigDecimal eqFreqRangeMaxErx) {
		this.eqFreqRangeMaxErx = eqFreqRangeMaxErx;
	}

	public BigDecimal getEqFreqRangeMaxEtx() {
		return this.eqFreqRangeMaxEtx;
	}

	public void setEqFreqRangeMaxEtx(BigDecimal eqFreqRangeMaxEtx) {
		this.eqFreqRangeMaxEtx = eqFreqRangeMaxEtx;
	}

	public BigDecimal getEqFreqRangeMinErx() {
		return this.eqFreqRangeMinErx;
	}

	public void setEqFreqRangeMinErx(BigDecimal eqFreqRangeMinErx) {
		this.eqFreqRangeMinErx = eqFreqRangeMinErx;
	}

	public BigDecimal getEqFreqRangeMinEtx() {
		return this.eqFreqRangeMinEtx;
	}

	public void setEqFreqRangeMinEtx(BigDecimal eqFreqRangeMinEtx) {
		this.eqFreqRangeMinEtx = eqFreqRangeMinEtx;
	}

	public String getEquipName() {
		return this.equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public String getEquipType() {
		return this.equipType;
	}

	public void setEquipType(String equipType) {
		this.equipType = equipType;
	}

	public BigDecimal getFrequencyHz() {
		return this.frequencyHz;
	}

	public void setFrequencyHz(BigDecimal frequencyHz) {
		this.frequencyHz = frequencyHz;
	}

	public String getLiPeriodCatId() {
		return this.liPeriodCatId;
	}

	public void setLiPeriodCatId(String liPeriodCatId) {
		this.liPeriodCatId = liPeriodCatId;
	}

	public String getLicenceNumber() {
		return this.licenceNumber;
	}

	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
	}

	public String getLicencePeriodCategory() {
		return this.licencePeriodCategory;
	}

	public void setLicencePeriodCategory(String licencePeriodCategory) {
		this.licencePeriodCategory = licencePeriodCategory;
	}

	public String getSs() {
		return this.ss;
	}

	public void setSs(String ss) {
		this.ss = ss;
	}

	public BigDecimal getSsId() {
		return this.ssId;
	}

	public void setSsId(BigDecimal ssId) {
		this.ssId = ssId;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getSv() {
		return this.sv;
	}

	public void setSv(String sv) {
		this.sv = sv;
	}

	public BigDecimal getSvId() {
		return this.svId;
	}

	public void setSvId(BigDecimal svId) {
		this.svId = svId;
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
		return null;
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