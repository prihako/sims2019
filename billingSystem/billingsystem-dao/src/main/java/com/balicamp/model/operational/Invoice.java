package com.balicamp.model.operational;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import org.hibernate.annotations.Type;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;
import com.balicamp.util.DateUtil;

/**
 * The persistent class for the T_INVOICE database table.
 * 
 */
@Entity
@Table(name = "T_INVOICE")
@NamedQuery(name = "Invoice.findAll", query = "SELECT t FROM Invoice t")
public class Invoice extends BaseAdminModel implements ISequencesModel,
		PropertySelectionData, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INVOICE_ID")
	@SequenceGenerator(name = "T_INVOICE_ID_SEQ", sequenceName = "T_INVOICE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "T_INVOICE_ID_SEQ")
	private Long invoiceId;

	@Temporal(TemporalType.DATE)
	@Column(name = "BG_DUE_DATE")
	private Date bgDueDate;

	@Column(name = "BG_TOTAL")
	private BigDecimal bgTotal;

	@Column(name = "BHP_ANNUAL")
	private BigDecimal bhpAnnual;

	@Column(name = "BHP_ANNUAL_PERCENT")
	private BigDecimal bhpAnnualPercent;

	@Column(name = "BHP_CALC_INDEX")
	private BigDecimal bhpCalcIndex;

	@Column(name = "BHP_FINE_ACCUMULATE")
	private BigDecimal bhpFineAccumulate;

	@Column(name = "BHP_FINE_CURRENT")
	private BigDecimal bhpFineCurrent;

	@Column(name = "BHP_FINE_PERCENT")
	private BigDecimal bhpFinePercent;

	@Column(name = "BHP_HL")
	private BigDecimal bhpHl;

	@Column(name = "BHP_PHL")
	private BigDecimal bhpPhl;

	@Column(name = "BHP_RATE")
	private BigDecimal bhpRate;

	@Column(name = "BHP_TOTAL")
	private BigDecimal bhpTotal;

	@Column(name = "BHP_UPFRONT_FEE")
	private BigDecimal bhpUpfrontFee;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON", columnDefinition="DATE")
	private Date createdOn;

	@Temporal(TemporalType.DATE)
	@Column(name = "INV_BEGIN_DATE")
	private Date invBeginDate;

	@Temporal(TemporalType.DATE) 
	@Column(name = "INV_CREATED_DATE")
	private Date invCreatedDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "INV_END_DATE")
	private Date invEndDate;

	@Column(name = "INVOICE_ID_BASE")
	private BigDecimal invoiceIdBase;

	@Column(name = "INVOICE_ID_PREV")
	private BigDecimal invoiceIdPrev;

	@Column(name = "INVOICE_NO")
	private String invoiceNo;

	@Column(name = "INVOICE_STATUS")
	private String invoiceStatus;

	@Column(name = "INVOICE_TYPE")
	private String invoiceType;

	@Column(name = "MONTH_TO")
	private BigDecimal monthTo;

	@Column(name = "PAYMENT_AMOUNT")
	private BigDecimal paymentAmount;

	@Column(name = "SAVE_STATUS")
	private String saveStatus;

	@Column(name = "INVOICE_COMMENT")
	private String invoiceComment;

	@Column(name = "IPSFR_NO")
	private String ipsfrNO;

	@Temporal(TemporalType.DATE)
	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_ON", columnDefinition="DATE")
	private Date updatedOn;

	@Column(name = "YEAR_TO")
	private BigDecimal yearTo;

	@Temporal(TemporalType.DATE)
	@Column(name = "PAYMENT_DUE_DATE")
	private Date paymentDueDate;

	@Column(name = "LETTER_ID")
	private BigDecimal letterID;

	@Column(name = "IS_MANUAL_CANCEL")
	private String isManualCancel;
	
	@Column(name = "BHP_TOTAL_ORI")
	private BigDecimal bhpTotalOri;
	
	@Column(name = "INVOICE_COMMENT_BHP")
	private String invoiceCommentBhp;
	
	public String getIsManualCancel() {
		return isManualCancel;
	}

	public void setIsManualCancel(String isManualCancel) {
		this.isManualCancel = isManualCancel;
	}

	// bi-directional many-to-one association to TLicence
	@ManyToOne
	@JoinColumn(name = "T_LICENCE_ID")
	private License License;

	// bi-directional many-to-one association to TBankGuarantee
	@OneToOne(mappedBy = "invoice")
	private BankGuarantee bankGuarantee;

	public BankGuarantee getBankGuarantee() {
		return bankGuarantee;
	}

	public void setBankGuarantee(BankGuarantee bankGuarantee) {
		this.bankGuarantee = bankGuarantee;
	}

	// //bi-directional many-to-one association to TBankGuarantee
	// @OneToMany(mappedBy="TInvoice")
	// private List<BankGuarantee> BankGuarantees;
	//
	// public BankGuarantee addTBankGuarantee(TBankGuarantee TBankGuarantee) {
	// getTBankGuarantees().add(TBankGuarantee);
	// TBankGuarantee.setTInvoice(this);
	//
	// return TBankGuarantee;
	// }
	//
	// public TBankGuarantee removeTBankGuarantee(TBankGuarantee TBankGuarantee)
	// {
	// getTBankGuarantees().remove(TBankGuarantee);
	// TBankGuarantee.setTInvoice(null);
	//
	// return TBankGuarantee;
	// }

	public String getTypeInvoice() {
		String str = null;
		if (getInvoiceType().equals("1")) {
			str = "Pokok";
		} else if (getInvoiceType().equals("2")) {
			str = "Denda";
		}

		return str;
	}

	public String getYear() {
		String year = null;
		if (getInvBeginDate() != null) {
			year = DateUtil.convertDateToString(getInvBeginDate(), "yyyy");
		} else {
			year = DateUtil.convertDateToString(getLicense()
					.getCurrentBeginDate(), "yyyy");
		}
		return year;
	}

	public String getYears() {
		String year = DateUtil.convertDateToString(invBeginDate, "yyyy");
		return year;
	}

	public String getUpfrontFee() {

		DecimalFormat moneyFormat = new DecimalFormat("Rp ###,###");
		String str = null;

		if (getBhpUpfrontFee() != null) {
			str = moneyFormat.format(getBhpUpfrontFee().longValue());
			System.out.println("UPFRONTFEE : "
					+ moneyFormat.format(getBhpUpfrontFee().longValue()));

		} else {
			str = "-";
		}

		return str;
	}

	public String getAnnualBHP() {
		DecimalFormat moneyFormat = new DecimalFormat("Rp ###,###");

		String str = null;
		if (getBhpAnnual() != null) {

			str = moneyFormat.format(getBhpAnnual().longValue());
			System.out.println("BHP IPSFR TAHUNAN : "
					+ moneyFormat.format(getBhpAnnual().longValue()));

		} else {
			str = "-";
		}
		return str;
	}

	public String getTotal() {
		// DecimalFormat moneyFormat = new DecimalFormat("Rp. ###,###");
		// System.out.println(moneyFormat.format(1234.56));

		DecimalFormat moneyFormat = new DecimalFormat("Rp ###,###");
		String str = null;

		if (getBhpTotal() != null) {
			str = moneyFormat.format(getBhpTotal().longValue());
			System.out.println("NILAI BHP TOTAL : "
					+ moneyFormat.format(getBhpTotal().longValue()));

		} else {
			str = "-";
		}

		return str;
	}

	public License getLicense() {
		return License;
	}

	public void setLicense(License license) {
		this.License = license;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Long getInvoiceId(){
		return invoiceId;
	}
	
	public Invoice() {
	}

	public Date getBgDueDate() {
		return this.bgDueDate;
	}

	public void setBgDueDate(Date bgDueDate) {
		this.bgDueDate = bgDueDate;
	}

	public BigDecimal getBgTotal() {
		return this.bgTotal;
	}

	public void setBgTotal(BigDecimal bgTotal) {
		this.bgTotal = bgTotal;
	}

	public BigDecimal getBhpAnnual() {
		return this.bhpAnnual;
	}

	public void setBhpAnnual(BigDecimal bhpAnnual) {
		this.bhpAnnual = bhpAnnual;
	}

	public BigDecimal getBhpAnnualPercent() {
		return this.bhpAnnualPercent;
	}

	public void setBhpAnnualPercent(BigDecimal bhpAnnualPercent) {
		this.bhpAnnualPercent = bhpAnnualPercent;
	}

	public BigDecimal getBhpCalcIndex() {
		return this.bhpCalcIndex;
	}

	public void setBhpCalcIndex(BigDecimal bhpCalcIndex) {
		this.bhpCalcIndex = bhpCalcIndex;
	}

	public BigDecimal getBhpFineAccumulate() {
		return this.bhpFineAccumulate;
	}

	public void setBhpFineAccumulate(BigDecimal bhpFineAccumulate) {
		this.bhpFineAccumulate = bhpFineAccumulate;
	}

	public BigDecimal getBhpFineCurrent() {
		return this.bhpFineCurrent;
	}

	public void setBhpFineCurrent(BigDecimal bhpFineCurrent) {
		this.bhpFineCurrent = bhpFineCurrent;
	}

	public BigDecimal getBhpFinePercent() {
		return this.bhpFinePercent;
	}

	public void setBhpFinePercent(BigDecimal bhpFinePercent) {
		this.bhpFinePercent = bhpFinePercent;
	}

	public BigDecimal getBhpHl() {
		return this.bhpHl;
	}

	public void setBhpHl(BigDecimal bhpHl) {
		this.bhpHl = bhpHl;
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

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(String saveStatus) {
		this.saveStatus = saveStatus;
	}

	public Date getInvBeginDate() {
		return this.invBeginDate;
	}

	public void setInvBeginDate(Date invBeginDate) {
		this.invBeginDate = invBeginDate;
	}

	public Date getInvCreatedDate() {
		return this.invCreatedDate;
	}

	public void setInvCreatedDate(Date invCreatedDate) {
		this.invCreatedDate = invCreatedDate;
	}

	public Date getInvEndDate() {
		return this.invEndDate;
	}

	public void setInvEndDate(Date invEndDate) {
		this.invEndDate = invEndDate;
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

	public BigDecimal getMonthTo() {
		return this.monthTo;
	}

	public void setMonthTo(BigDecimal monthTo) {
		this.monthTo = monthTo;
	}

	public Date getPaymentDate() {
		return this.paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
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

	public String getInvoiceComment() {
		return invoiceComment;
	}

	public void setInvoiceComment(String invoiceComment) {
		this.invoiceComment = invoiceComment;
	}

	public String getIpsfrNO() {
		return ipsfrNO;
	}

	public void setIpsfrNO(String ipsfrNO) {
		this.ipsfrNO = ipsfrNO;
	}

	public License getTLicence() {
		return this.License;
	}

	public void setTLicence(License license) {
		setLicense(license);
	}
	
	public BigDecimal getLetterID() {
		return letterID;
	}

	public void setLetterID(BigDecimal letterID) {
		this.letterID = letterID;
	}

	public Date getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}


	public Invoice(Invoice invoice) {
		super();
		this.License = invoice.getTLicence();
		this.invoiceId = invoice.getId();
		this.invoiceNo = invoice.getInvoiceNo();
		this.invoiceStatus = invoice.getInvoiceStatus();
		this.invoiceType = invoice.getInvoiceType();
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
		return "t_invoice_id_seq";
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
	
	public BigDecimal getBhpTotalOri() {
		return this.bhpTotalOri;
	}

	public void setBhpTotalOri(BigDecimal bhpTotalOri) {
		this.bhpTotalOri = bhpTotalOri;
	}
	
	public String getInvoiceCommentBhp() {
		return this.invoiceCommentBhp;
	}

	public void setInvoiceCommentBhp(String invoiceCommentBhp) {
		this.invoiceCommentBhp = invoiceCommentBhp;
	}

}