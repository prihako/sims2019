package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TransactionIso implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -431979247238536589L;

	private long transactionIsoId;

	private String rawIsoMsg;

	private String mti;

	private String accountNum;

	private String processingCode;

	private long transactionAmount;

	private Date transmissionDate;

	private String stan;

	private String transactionTime;

	private String transactionDate;

	private String settlementDate;

	private String merchantType;

	private String acqInstitutionId;

	private String rrn;

	private String currencyCode;

	private String institutionId;

	private String responseCode;

	private String netManageCode;

	private String invoiceId;

	private String clientId;

	private String clientName;

	private String receiptCode;

	private String periodBegin;

	private String periodEnd;

	private String prevStan;

	private String prevRrn;

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getAcqInstitutionId() {
		return acqInstitutionId;
	}

	public void setAcqInstitutionId(String acqInstitutionId) {
		this.acqInstitutionId = acqInstitutionId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getMti() {
		return mti;
	}

	public void setMti(String mti) {
		this.mti = mti;
	}

	public String getNetManageCode() {
		return netManageCode;
	}

	public void setNetManageCode(String netManageCode) {
		this.netManageCode = netManageCode;
	}

	public String getPeriodBegin() {
		return periodBegin;
	}

	public void setPeriodBegin(String periodBegin) {
		this.periodBegin = periodBegin;
	}

	public String getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(String periodEnd) {
		this.periodEnd = periodEnd;
	}

	public String getPrevRrn() {
		return prevRrn;
	}

	public void setPrevRrn(String prevRrn) {
		this.prevRrn = prevRrn;
	}

	public String getPrevStan() {
		return prevStan;
	}

	public void setPrevStan(String prevStan) {
		this.prevStan = prevStan;
	}

	public String getProcessingCode() {
		return processingCode;
	}

	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}

	public String getRawIsoMsg() {
		return rawIsoMsg;
	}

	public void setRawIsoMsg(String rawIsoMsg) {
		this.rawIsoMsg = rawIsoMsg;
	}

	public String getReceiptCode() {
		return receiptCode;
	}

	public void setReceiptCode(String receiptCode) {
		this.receiptCode = receiptCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public long getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(long transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public long getTransactionIsoId() {
		return transactionIsoId;
	}

	public void setTransactionIsoId(long transactionIsoId) {
		this.transactionIsoId = transactionIsoId;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public Date getTransmissionDate() {
		return transmissionDate;
	}

	public void setTransmissionDate(Date transmissionDate) {
		this.transmissionDate = transmissionDate;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
