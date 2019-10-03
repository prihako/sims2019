package com.balicamp.model.operational;

import java.io.Serializable;

import javax.persistence.*;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EBS_BANDWIDTH_VW database table.
 * 
 */
@Entity
@Table(name="SPECTRA.EBS_BANDWIDTH_VW")
//@Table(name="EBS_BANDWIDTH_VW")

//@NamedQuery(name="EbsBandwidthVw.findAll", query="SELECT e FROM EbsBandwidthVw e")
public class ApplicationBandwidth extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable  {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LICENCE_ID")
	private BigDecimal licenseId;

	@Column(name="LICENCE_NUMBER")
	private String licenceNumber;
	
	@Column(name="KM_NO")
	private String kmNo;

	@Column(name="AD_ID")
	private BigDecimal adId;

	@Column(name="BHP_METHOD")
	private String bhpMethod;

	@Column(name="BHP_PAYMENT_TYPE")
	private String bhpPaymentType;

	private String city;

	@Column(name="CLIENT_COMPANY")
	private String clientCompany;

	@Column(name="CLIENT_NUMBER")
	private String clientNumber;

	@Temporal(TemporalType.DATE)
	@Column(name="CURRENT_BEGIN_DATE")
	private Date currentBeginDate;

	@Temporal(TemporalType.DATE)
	@Column(name="CURRENT_END_DATE")
	private Date currentEndDate;

	@Column(name="FREQ_MAX")
	private BigDecimal freqMax;

	@Column(name="FREQ_MAX_R")
	private BigDecimal freqMaxR;
	

	public BigDecimal getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(BigDecimal licenseId) {
		this.licenseId = licenseId;
	}

	public BigDecimal getFreqMaxR() {
		return freqMaxR;
	}

	public void setFreqMaxR(BigDecimal freqMaxR) {
		this.freqMaxR = freqMaxR;
	}

	public BigDecimal getFreqMinR() {
		return freqMinR;
	}

	public void setFreqMinR(BigDecimal freqMinR) {
		this.freqMinR = freqMinR;
	}

	@Column(name="FREQ_MIN")
	private BigDecimal freqMin;

	@Column(name="FREQ_MIN_R")
	private BigDecimal freqMinR;


	@Column(name="IS_BG_AVAILABLE")
	private String isBgAvailable;

	@Temporal(TemporalType.DATE)
	@Column(name="KM_DATE")
	private Date kmDate;

	@Lob
	@Column(name="KM_DOC")
	private byte[] kmDoc;

	@Column(name="KM_LOCATION")
	private String kmLocation;
	
	@Column(name="KM_FILE_NAME")
	private String kmFileName;
	

	public String getKmFileName() {
		return kmFileName;
	}

	public void setKmFileName(String kmFileName) {
		this.kmFileName = kmFileName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="LI_BEGIN_DATE")
	private Date liBeginDate;

	@Temporal(TemporalType.DATE)
	@Column(name="LI_END_DATE")
	private Date liEndDate;


	@Temporal(TemporalType.DATE)
	@Column(name="PAYMENT_DUE_DATE")
	private Date paymentDueDate;

	@Column(name="SS_ID")
	private BigDecimal ssId;

	private String status;

	@Column(name="STATUS_DB")
	private BigDecimal statusDb;

	@Column(name="SV_ID")
	private BigDecimal svId;

	@Column(name="ZONE_NAME")
	private String zoneName;

	@Column(name="ZONE_NO")
	private BigDecimal zoneNo;
	
	

	public ApplicationBandwidth() {
	}

	public BigDecimal getAdId() {
		return this.adId;
	}

	public void setAdId(BigDecimal adId) {
		this.adId = adId;
	}

//	public String getApRefNumber() {
//		return this.apRefNumber;
//	}
//
//	public void setApRefNumber(String apRefNumber) {
//		this.apRefNumber = apRefNumber;
//	}

	public String getBhpMethod() {
		return this.bhpMethod;
	}
	
	public String getBHP(){
		String str = null;
		if(getBhpMethod() == null) {
			str = "";
		}else if (getBhpMethod().equals("FR")) {
			str = "Flat BHP";
		} else if(getBhpMethod().equals("VR")) {
			str = "Variety Rate";
		}else if(getBhpMethod().equals("C")) {
			str = "Conversion";
		}
		
		return str;
	}

	public void setBhpMethod(String bhpMethod) {
		this.bhpMethod = bhpMethod;
	}

	public String getBhpPaymentType() {
		return this.bhpPaymentType;
	}

	public void setBhpPaymentType(String bhpPaymentType) {
		this.bhpPaymentType = bhpPaymentType;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getClientCompany() {
		return this.clientCompany;
	}

	public void setClientCompany(String clientCompany) {
		this.clientCompany = clientCompany;
	}

	public String getClientNumber() {
		return this.clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	public Date getCurrentBeginDate() {
		return this.currentBeginDate;
	}

	public void setCurrentBeginDate(Date currentBeginDate) {
		this.currentBeginDate = currentBeginDate;
	}

	public Date getCurrentEndDate() {
		return this.currentEndDate;
	}

	public void setCurrentEndDate(Date currentEndDate) {
		this.currentEndDate = currentEndDate;
	}

	public BigDecimal getFreqMax() {
		return this.freqMax;
	}

	public void setFreqMax(BigDecimal freqMax) {
		this.freqMax = freqMax;
	}

	public BigDecimal getFreqMin() {
		return this.freqMin;
	}

	public void setFreqMin(BigDecimal freqMin) {
		this.freqMin = freqMin;
	}

	public String getIsBgAvailable() {
		return this.isBgAvailable;
	}

	public void setIsBgAvailable(String isBgAvailable) {
		this.isBgAvailable = isBgAvailable;
	}
	
	
	public String getKmNo() {
		return kmNo;
	}

	public void setKmNo(String kmNo) {
		this.kmNo = kmNo;
	}

	public Date getKmDate() {
		return this.kmDate;
	}

	public void setKmDate(Date kmDate) {
		this.kmDate = kmDate;
	}

	public byte[] getKmDoc() {
		return this.kmDoc;
	}

	public void setKmDoc(byte[] kmDoc) {
		this.kmDoc = kmDoc;
	}

	public String getKmLocation() {
		return this.kmLocation;
	}

	public void setKmLocation(String kmLocation) {
		this.kmLocation = kmLocation;
	}

	public Date getLiBeginDate() {
		return this.liBeginDate;
	}

	public void setLiBeginDate(Date liBeginDate) {
		this.liBeginDate = liBeginDate;
	}

	public Date getLiEndDate() {
		return this.liEndDate;
	}

	public void setLiEndDate(Date liEndDate) {
		this.liEndDate = liEndDate;
	}

	public String getLicenceNumber() {
		return this.licenceNumber;
	}

	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
	}

	public Date getPaymentDueDate() {
		return this.paymentDueDate;
	}

	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public BigDecimal getSsId() {
		return this.ssId;
	}

	public void setSsId(BigDecimal ssId) {
		this.ssId = ssId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getStatusDb() {
		return this.statusDb;
	}

	public void setStatusDb(BigDecimal statusDb) {
		this.statusDb = statusDb;
	}

	public BigDecimal getSvId() {
		return this.svId;
	}

	public void setSvId(BigDecimal svId) {
		this.svId = svId;
	}

	public String getZoneName() {
		return this.zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public BigDecimal getZoneNo() {
		return this.zoneNo;
	}

	public void setZoneNo(BigDecimal zoneNo) {
		this.zoneNo = zoneNo;
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