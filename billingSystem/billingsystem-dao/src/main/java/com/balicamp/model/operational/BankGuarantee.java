package com.balicamp.model.operational;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;


/**
 * The persistent class for the T_BANK_GUARANTEE database table.
 *
 */
@Entity
@Table(name="T_BANK_GUARANTEE")
@NamedQuery(name="BankGuarantee.findAll", query="SELECT t FROM BankGuarantee t")
public class BankGuarantee extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BG_ID")
	@SequenceGenerator(name="T_BG_ID_SEQ", sequenceName="T_BG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="T_BG_ID_SEQ")
	private Long bgId;

	@Column(name="BANK_ADDRESS")
	private String bankAddress;

	@Column(name="BANK_BRANCH")
	private String bankBranch;

	@Column(name="BANK_NAME")
	private String bankName;

	@Temporal(TemporalType.DATE)
	@Column(name="BG_BEGIN_DATE")
	private Date bgBeginDate;

	@Column(name="BG_DOCUMENT_NO")
	private String bgDocumentNo;

	@Temporal(TemporalType.DATE)
	@Column(name="BG_END_DATE")
	private Date bgEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="BG_PUBLISH_DATE")
	private Date bgPublishDate;

	@Column(name="BG_VALUE")
	private BigDecimal bgValue;

	@Column(name="BG_VALUE_SUBMITTED")
	private BigDecimal bgValueSubmitted;

	@Column(name="BHP_PERCENT")
	private BigDecimal bhpPercent;

	@Column(name="BHP_VALUE")
	private BigDecimal bhpValue;

	@Column(name="BI_RATE")
	private BigDecimal biRate;

	@Column(name="CALC_INDEX")
	private BigDecimal calcIndex;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_ON", columnDefinition="DATE")
	private Date createdOn;

	@Temporal(TemporalType.DATE)
	@Column(name="BG_DUE_DATE")
	private Date dueDate;

	@Temporal(TemporalType.DATE)
	@Column(name="RECEIVED_DATE")
	private Date receivedDate;

	@Column(name="RECEIVED_STATUS")
	private String receivedStatus;

	@Column(name="SUBMIT_YEAR_TO")
	private BigDecimal submitYearTo;

	@Temporal(TemporalType.DATE)
	@Column(name="SUBMIT_DUE_DATE")
	private Date submitDueDate;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_ON", columnDefinition="DATE")
	private Date updatedOn;

	@Temporal(TemporalType.DATE)
	@Column(name="CLAIM_REQUEST_DATE")
	private Date claimRequestDate;

	@Temporal(TemporalType.DATE)
	@Column(name="CLAIM_DATE")
	private Date claimDate;

	@Column(name="CLAIM_REF_NO")
	private String claimReferenceNo;

	@Column(name="CLAIM_VALUE")
	private BigDecimal claimValue;

	//bi-directional many-to-one association to T_LICENCE
	@ManyToOne
	@JoinColumn(name="T_LICENCE_ID")
	private License License;

	//bi-directional one-to-one association to T_INVOICE
	@OneToOne
	@JoinColumn(name="INVOICE_ID")
	private Invoice invoice;

	@Column(name="INVOICE_NO_CLAIM")
	private String invoiceNoClaim;

	@Column(name="INVOICE_NO_CLAIM_DIFF")
	private String invoiceNoClaimDiff;
	
	@Column(name="INVOICE_NO")
	private String invoiceNo;

	@Column(name="INVOICE_NO_DIFF")
	private String invoiceNoDiff;

	@Column(name="BG_VALUE_DIFF")
	private BigDecimal bgValueDiff;

	@Column(name="SAVE_STATUS")
	private String saveStatus;

	@Column(name="CLAIM_STATUS")
	private String claimStatus;

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceNoDiff() {
		return invoiceNoDiff;
	}

	public void setInvoiceNoDiff(String invoiceNoDiff) {
		this.invoiceNoDiff = invoiceNoDiff;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public BankGuarantee() {
	}

	public License getLicense() {
		return License;
	}

	public void setLicense(License license) {
		this.License = license;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public long getBgId() {
		return this.bgId;
	}

	public void setBgId(Long bgId) {
		this.bgId = bgId;
	}

	public String getBankAddress() {
		return this.bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getBankBranch() {
		return this.bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Date getBgBeginDate() {
		return this.bgBeginDate;
	}

	public void setBgBeginDate(Date bgBeginDate) {
		this.bgBeginDate = bgBeginDate;
	}

	public String getBgDocumentNo() {
		return this.bgDocumentNo;
	}

	public void setBgDocumentNo(String bgDocumentNo) {
		this.bgDocumentNo = bgDocumentNo;
	}

	public Date getBgEndDate() {
		return this.bgEndDate;
	}

	public void setBgEndDate(Date bgEndDate) {
		this.bgEndDate = bgEndDate;
	}

	public Date getBgPublishDate() {
		return this.bgPublishDate;
	}

	public void setBgPublishDate(Date bgPublishDate) {
		this.bgPublishDate = bgPublishDate;
	}

	public BigDecimal getBgValue() {
		return this.bgValue;
	}

	public void setBgValue(BigDecimal bgValue) {
		this.bgValue = bgValue;
	}

	public BigDecimal getBgValueSubmitted() {
		return this.bgValueSubmitted;
	}

	public void setBgValueSubmitted(BigDecimal bgValueSubmitted) {
		this.bgValueSubmitted = bgValueSubmitted;
	}

	public BigDecimal getBhpPercent() {
		return this.bhpPercent;
	}

	public void setBhpPercent(BigDecimal bhpPercent) {
		this.bhpPercent = bhpPercent;
	}

	public BigDecimal getBhpValue() {
		return bhpValue;
	}

	public void setBhpValue(BigDecimal bhpValue) {
		this.bhpValue = bhpValue;
	}

	public BigDecimal getBiRate() {
		return this.biRate;
	}

	public void setBiRate(BigDecimal biRate) {
		this.biRate = biRate;
	}

	public BigDecimal getCalcIndex() {
		return this.calcIndex;
	}

	public void setCalcIndex(BigDecimal calcIndex) {
		this.calcIndex = calcIndex;
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

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getReceivedDate() {
		return this.receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getReceivedStatus() {
		return receivedStatus;
	}

	public void setReceivedStatus(String receivedStatus) {
		this.receivedStatus = receivedStatus;
	}

	public BigDecimal getSubmitYearTo() {
		return this.submitYearTo;
	}

	public void setSubmitYearTo(BigDecimal submitYearTo) {
		this.submitYearTo = submitYearTo;
	}

	public Date getSubmitDueDate() {
		return submitDueDate;
	}

	public void setSubmitDueDate(Date submitDueDate) {
		this.submitDueDate = submitDueDate;
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

	public Date getClaimRequestDate() {
		return claimRequestDate;
	}

	public void setClaimRequestDate(Date claimRequestDate) {
		this.claimRequestDate = claimRequestDate;
	}

	public Date getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(Date claimDate) {
		this.claimDate = claimDate;
	}

	public String getClaimReferenceNo() {
		return claimReferenceNo;
	}

	public void setClaimReferenceNo(String claimReferenceNo) {
		this.claimReferenceNo = claimReferenceNo;
	}

	public BigDecimal getClaimValue() {
		return claimValue;
	}

	public void setClaimValue(BigDecimal claimValue) {
		this.claimValue = claimValue;
	}

	public String getInvoiceNoClaim() {
		return invoiceNoClaim;
	}

	public void setInvoiceNoClaim(String invoiceNo) {
		this.invoiceNoClaim = invoiceNo;
	}

	public String getInvoiceNoClaimDiff() {
		return invoiceNoClaimDiff;
	}

	public void setInvoiceNoClaimDiff(String invoiceNoDiff) {
		this.invoiceNoClaimDiff = invoiceNoDiff;
	}

	public BigDecimal getBgValueDiff() {
		return bgValueDiff;
	}

	public String getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(String saveStatus) {
		this.saveStatus = saveStatus;
	}

	public void setBgValueDiff(BigDecimal bgValueDiff) {
		this.bgValueDiff = bgValueDiff;
	}

	public License getTLicence() {
		return this.License;
	}

	public void setTLicence(License License) {
		this.License = License;
	}

	public Invoice getTInvoice() {
		return this.invoice;
	}

	public void setTInvoice(Invoice invoice) {
		this.invoice = invoice;
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
		return "t_bg_id_seq";
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