package com.balicamp.model.operational;

import java.io.Serializable;

import javax.persistence.*;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.ui.PropertySelectionData;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the OP_INVOICES database table.
 * 
 */
@Entity
@Table(name="OP_INVOICES")
//@NamedQuery(name="OpInvoice.findAll", query="SELECT o FROM OpInvoice o")
public class Invoices extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="INVOICE_ID")
	@SequenceGenerator(name="OP_INVOICE_ID_SEQ", sequenceName="OP_INVOICE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="OP_INVOICE_ID_SEQ")
	private Long invoiceId;

	@Column(name="BASE_INV_AMOUNT")
	private BigDecimal baseInvAmount;

	@Column(name="BG_STATUS")
	private String bgStatus;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_ON")
	private Date createdOn;

	@Column(name="INSTALLMENT_LAST_VALUE")
	private BigDecimal installmentLastValue;

	@Column(name="INSTALLMENT_TERM")
	private BigDecimal installmentTerm;

	@Column(name="INSTALLMENT_TERM_VALUE")
	private BigDecimal installmentTermValue;

	@Temporal(TemporalType.DATE)
	@Column(name="INVOICE_DATE")
	private Date invoiceDate;

	@Column(name="INVOICE_ID_BASE")
	private BigDecimal invoiceIdBase;

	@Column(name="INVOICE_ID_PREV")
	private BigDecimal invoiceIdPrev;

	@Column(name="INVOICE_NO")
	private String invoiceNo;

	@Column(name="INVOICE_STATUS")
	private String invoiceStatus;

	@Column(name="INVOICE_TYPE")
	private String invoiceType;

	@Column(name="LICENCE_ID")
	private String licenceId;

	@Column(name="LICENCE_NO")
	private String licenceNo;

	@Column(name="MONTH_TO")
	private BigDecimal monthTo;

	@Column(name="OTHER_INV_STATUS")
	private String otherInvStatus;

	@Temporal(TemporalType.DATE)
	@Column(name="PAYMENT_DATE")
	private Date paymentDate;

	@Temporal(TemporalType.DATE)
	@Column(name="PAYMENT_DUE_DATE")
	private Date paymentDueDate;

	@Column(name="PENALTY_ACCUMULATE")
	private BigDecimal penaltyAccumulate;

	@Column(name="PENALTY_AMOUNT")
	private BigDecimal penaltyAmount;

	@Column(name="TOTAL_INV_AMOUNT")
	private BigDecimal totalInvAmount;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_ON")
	private Date updatedOn;

	@Column(name="YEAR_TO")
	private BigDecimal yearTo;

	public Invoices() {
	}

	public Long getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public BigDecimal getBaseInvAmount() {
		return this.baseInvAmount;
	}

	public void setBaseInvAmount(BigDecimal baseInvAmount) {
		this.baseInvAmount = baseInvAmount;
	}

	public String getBgStatus() {
		return this.bgStatus;
	}

	public void setBgStatus(String bgStatus) {
		this.bgStatus = bgStatus;
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

	public BigDecimal getInstallmentLastValue() {
		return this.installmentLastValue;
	}

	public void setInstallmentLastValue(BigDecimal installmentLastValue) {
		this.installmentLastValue = installmentLastValue;
	}

	public BigDecimal getInstallmentTerm() {
		return this.installmentTerm;
	}

	public void setInstallmentTerm(BigDecimal installmentTerm) {
		this.installmentTerm = installmentTerm;
	}

	public BigDecimal getInstallmentTermValue() {
		return this.installmentTermValue;
	}

	public void setInstallmentTermValue(BigDecimal installmentTermValue) {
		this.installmentTermValue = installmentTermValue;
	}

	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public BigDecimal getInvoiceIdBase() {
		return this.invoiceIdBase;
	}

	public void setInvoiceIdBase(BigDecimal invoiceIdBase) {
		this.invoiceIdBase = invoiceIdBase;
	}

	public BigDecimal getInvoiceIdPrev() {
		return this.invoiceIdPrev;
	}

	public void setInvoiceIdPrev(BigDecimal invoiceIdPrev) {
		this.invoiceIdPrev = invoiceIdPrev;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceStatus() {
		return this.invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
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

	public BigDecimal getMonthTo() {
		return this.monthTo;
	}

	public void setMonthTo(BigDecimal monthTo) {
		this.monthTo = monthTo;
	}

	public String getOtherInvStatus() {
		return this.otherInvStatus;
	}

	public void setOtherInvStatus(String otherInvStatus) {
		this.otherInvStatus = otherInvStatus;
	}

	public Date getPaymentDate() {
		return this.paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Date getPaymentDueDate() {
		return this.paymentDueDate;
	}

	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public BigDecimal getPenaltyAccumulate() {
		return this.penaltyAccumulate;
	}

	public void setPenaltyAccumulate(BigDecimal penaltyAccumulate) {
		this.penaltyAccumulate = penaltyAccumulate;
	}

	public BigDecimal getPenaltyAmount() {
		return this.penaltyAmount;
	}

	public void setPenaltyAmount(BigDecimal penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}

	public BigDecimal getTotalInvAmount() {
		return this.totalInvAmount;
	}

	public void setTotalInvAmount(BigDecimal totalInvAmount) {
		this.totalInvAmount = totalInvAmount;
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
		return "op_invoice_id_seq";
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
	
	public Invoices(Invoices invoices) {
		super();
		this.setInvoiceId(invoices.getInvoiceId());
		this.setInvoiceIdBase(invoices.getInvoiceIdBase());
		this.setInvoiceIdPrev(invoices.getInvoiceIdPrev());
		this.setInvoiceNo(invoices.getInvoiceNo());
		this.setInvoiceType(invoices.getInvoiceType());
		this.setLicenceId(invoices.getLicenceId());
		this.setLicenceNo(invoices.getLicenceNo());
		this.setYearTo(invoices.getYearTo());
		this.setMonthTo(invoices.getMonthTo());
		this.setInvoiceDate(invoices.getInvoiceDate());
		this.setPaymentDueDate(invoices.getPaymentDueDate());
		this.setPaymentDate(invoices.getPaymentDate());
		this.setBgStatus(invoices.getBgStatus());
		this.setOtherInvStatus(invoices.getOtherInvStatus());
		this.setInvoiceStatus(invoices.getInvoiceStatus());
		this.setBaseInvAmount(invoices.getBaseInvAmount());
		this.setPenaltyAmount(invoices.getPenaltyAmount());
		this.setPenaltyAccumulate(invoices.getPenaltyAccumulate());
		this.setInstallmentTerm(invoices.getInstallmentTerm());
		this.setInstallmentTermValue(invoices.getInstallmentTermValue());
		this.setInstallmentLastValue(invoices.getInstallmentLastValue());
		this.setTotalInvAmount(invoices.getTotalInvAmount());
		this.setCreatedOn(invoices.getCreatedOn());
		this.setCreatedBy(invoices.getCreatedBy());
		this.setUpdatedOn(invoices.getUpdatedOn());
		this.setUpdatedBy(invoices.getUpdatedBy());
		this.setUpdatedOn(invoices.getUpdatedOn());
		
	}

}