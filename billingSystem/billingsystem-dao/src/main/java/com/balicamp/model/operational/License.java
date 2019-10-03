package com.balicamp.model.operational;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;




import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.ui.PropertySelectionData;

/**
 * The persistent class for the T_LICENCE database table.
 * 
 */
@Entity
@Table(name = "T_LICENCE")
@NamedQuery(name = "License.findAll", query = "SELECT t FROM License t")
public class License extends BaseAdminModel implements ISequencesModel,
		PropertySelectionData, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "T_LICENCE_ID")
	@SequenceGenerator(name = "T_LICENCE_ID_SEQ", sequenceName = "T_LICENCE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "T_LICENCE_ID_SEQ")
	private Long tLicenceId;

	@Column(name = "BG_AVAILABLE_STATUS")
	private String bgAvailableStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "BG_DUE_DATE")
	private Date bgDueDate;

	@Column(name = "BHP_METHOD")
	private String bhpMethod;

	@Column(name = "CLIENT_ID")
	private BigDecimal clientNo;

	@Column(name = "CLIENT_NAME")
	private String clientName;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON", columnDefinition="DATE")
	private Date createdOn;

	@Temporal(TemporalType.DATE)
	@Column(name = "CURRENT_BEGIN_DATE")
	private Date currentBeginDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "CURRENT_END_DATE")
	private Date currentEndDate;

	@Column(name = "FREQ_R_MAX")
	private BigDecimal freqRMax;

	@Column(name = "FREQ_R_MIN")
	private BigDecimal freqRMin;

	@Column(name = "FREQ_T_MAX")
	private BigDecimal freqTMax;

	@Column(name = "FREQ_T_MIN")
	private BigDecimal freqTMin;

	@Temporal(TemporalType.DATE)
	@Column(name = "KM_DATE")
	private Date kmDate;

	@Column(name = "KM_NO")
	private String kmNo;

	@Temporal(TemporalType.DATE)
	@Column(name = "LICENCE_BEGIN_DATE")
	private Date licenceBeginDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "LICENCE_END_DATE")
	private Date licenceEndDate;

	@Column(name = "LICENCE_NO")
	private String licenceNo;

	@Column(name = "LICENCE_STATUS")
	private String licenceStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "PAYMENT_DUE_DATE")
	private Date paymentDueDate;

	@Column(name = "PAYMENT_TYPE")
	private String paymentType;

	@Column(name = "SERVICE_ID")
	private BigDecimal serviceId;

	@Column(name = "SUBSERVICE_ID")
	private BigDecimal subserviceId;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_ON", columnDefinition="DATE")
	private Date updatedOn;

	@Column(name = "ZONE_NAME")
	private String zoneName;

	@Column(name = "ZONE_NO")
	private String zoneNo;

	// bi-directional many-to-one association to TBankGuarantee
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "License")
	private List<BankGuarantee> BankGuarantees;

	// bi-directional many-to-one association to TInvoice
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "License")
	private List<Invoice> Invoices;

	// bi-directional many-to-one association to TVarAnnualPercent
	@ManyToOne
	@JoinColumn(name = "ANNUAL_PERCENT_ID")
	private VariableAnnualPercentage variableAnnualPercent;

	public License() {
	}

	public String getBHP() {
		String str = null;
		if (getBhpMethod().equals("FR")) {
			str = "Flat BHP";
		} else if (getBhpMethod().equals("VR")) {
			str = "Variety Rate";
		} else if (getBhpMethod().equals("C")) {
			str = "Conversion";
		}

		return str;
	}
	
	public String getPaymentMethod(){
		String str = null;
		if (getPaymentType().equals("FP")) {
			str = "Full Payment";
		} else if (getBhpMethod().equals("SP")) {
			str = "Stage Payment";
		}

		return str;
	}
	
//	public String getAvailableBG(){
//		String str = null;
//		if (getBgAvailableStatus().equals("Y")) {
//			str = "Y";
//		} else if (getBgAvailableStatus().equals("N")) {
//			str = "N";
//		}
//
//		return str;
//	}

	public Long getTLicenceId() {
		return this.tLicenceId;
	}

	public void setTLicenceId(Long tLicenceId) {
		this.tLicenceId = tLicenceId;
	}

	public String getBgAvailableStatus() {
		return this.bgAvailableStatus;
	}

	public void setBgAvailableStatus(String bgAvailableStatus) {
		this.bgAvailableStatus = bgAvailableStatus;
	}

	public Date getBgDueDate() {
		return this.bgDueDate;
	}

	public void setBgDueDate(Date bgDueDate) {
		this.bgDueDate = bgDueDate;
	}

	public String getBhpMethod() {
		return this.bhpMethod;
	}

	public void setBhpMethod(String bhpMethod) {
		this.bhpMethod = bhpMethod;
	}

	public BigDecimal getClientNo() {
		return this.clientNo;
	}

	public void setClientNo(BigDecimal clientNo) {
		this.clientNo = clientNo;
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

	public BigDecimal getFreqRMax() {
		return this.freqRMax;
	}

	public void setFreqRMax(BigDecimal freqRMax) {
		this.freqRMax = freqRMax;
	}

	public BigDecimal getFreqRMin() {
		return this.freqRMin;
	}

	public void setFreqRMin(BigDecimal freqRMin) {
		this.freqRMin = freqRMin;
	}

	public BigDecimal getFreqTMax() {
		return this.freqTMax;
	}

	public void setFreqTMax(BigDecimal freqTMax) {
		this.freqTMax = freqTMax;
	}

	public BigDecimal getFreqTMin() {
		return this.freqTMin;
	}

	public void setFreqTMin(BigDecimal freqTMin) {
		this.freqTMin = freqTMin;
	}

	public Date getKmDate() {
		return this.kmDate;
	}

	public void setKmDate(Date kmDate) {
		this.kmDate = kmDate;
	}

	public String getKmNo() {
		return this.kmNo;
	}

	public void setKmNo(String kmNo) {
		this.kmNo = kmNo;
	}

	public Date getLicenceBeginDate() {
		return this.licenceBeginDate;
	}

	public void setLicenceBeginDate(Date licenceBeginDate) {
		this.licenceBeginDate = licenceBeginDate;
	}

	public Date getLicenceEndDate() {
		return this.licenceEndDate;
	}

	public void setLicenceEndDate(Date licenceEndDate) {
		this.licenceEndDate = licenceEndDate;
	}

	public String getLicenceNo() {
		return this.licenceNo;
	}

	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
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

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public BigDecimal getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	public BigDecimal getSubserviceId() {
		return this.subserviceId;
	}

	public void setSubserviceId(BigDecimal subserviceId) {
		this.subserviceId = subserviceId;
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

	public String getZoneName() {
		return this.zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getZoneNo() {
		return this.zoneNo;
	}

	public void setZoneNo(String zoneNo) {
		this.zoneNo = zoneNo;
	}

	public List<BankGuarantee> getTBankGuarantees() {
		return this.BankGuarantees;
	}

	public void setTBankGuarantees(List<BankGuarantee> BankGuarantees) {
		this.BankGuarantees = BankGuarantees;
	}

	public BankGuarantee addTBankGuarantee(BankGuarantee BankGuarantee) {
		getTBankGuarantees().add(BankGuarantee);
		BankGuarantee.setTLicence(this);

		return BankGuarantee;
	}

	public BankGuarantee removeTBankGuarantee(BankGuarantee BankGuarantee) {
		getTBankGuarantees().remove(BankGuarantee);
		BankGuarantee.setTLicence(null);

		return BankGuarantee;
	}

	public List<Invoice> getTInvoices() {
		return this.Invoices;
	}

	public void setTInvoices(List<Invoice> Invoices) {
		this.Invoices = Invoices;
	}

	public Invoice addTInvoice(Invoice Invoice) {
		getTInvoices().add(Invoice);
		Invoice.setTLicence(this);

		return Invoice;
	}

	public Invoice removeTInvoice(Invoice Invoice) {
		getTInvoices().remove(Invoice);
		Invoice.setTLicence(null);

		return Invoice;
	}
	
	public VariableAnnualPercentage getVariableAnnualPercent() {
		return variableAnnualPercent;
	}

	public void setVariableAnnualPercent(
			VariableAnnualPercentage variableAnnualPercent) {
		this.variableAnnualPercent = variableAnnualPercent;
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
		return "t_licence_id_seq";
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

	public License(License license) {
		super();
		this.licenceNo = license.getLicenceNo();
		this.Invoices = license.getTInvoices();
		this.licenceStatus = license.getLicenceStatus();
		this.kmNo = license.getKmNo();

	}

}