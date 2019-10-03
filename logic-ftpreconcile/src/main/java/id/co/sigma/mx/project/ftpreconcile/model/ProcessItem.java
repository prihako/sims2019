package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProcessItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3599220199075954346L;
	private Long processStatusId = null;
	private Long transactionId = null;
	private Long reconcileResultId = null;
	private String status = null;
	private String clientName = null;
	
	private String mti = null;
	private String processingCode = null;
	private String responseCode = null;
	private String invalidAmount = null;
	private String reversedFlag = null;
	
	private String periodBegin = null;
	private String periodEnd = null;
	private BigDecimal outstandingAmount = null;
	private String paymentStan = null;
	private String paymentRrn = null;
	
	private String uniqueReceiptCode = null;
	private Long fileId = null;

	

	public String getClientName() {
		return clientName;
	}



	public void setClientName(String clientName) {
		this.clientName = clientName;
	}



	public Long getFileId() {
		return fileId;
	}



	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}



	public String getInvalidAmount() {
		return invalidAmount;
	}



	public void setInvalidAmount(String invalidAmount) {
		this.invalidAmount = invalidAmount;
	}



	public String getMti() {
		return mti;
	}



	public void setMti(String mti) {
		this.mti = mti;
	}



	public BigDecimal getOutstandingAmount() {
		return outstandingAmount;
	}



	public void setOutstandingAmount(BigDecimal outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}



	public String getPaymentRrn() {
		return paymentRrn;
	}



	public void setPaymentRrn(String paymentRrn) {
		this.paymentRrn = paymentRrn;
	}



	public String getPaymentStan() {
		return paymentStan;
	}



	public void setPaymentStan(String paymentStan) {
		this.paymentStan = paymentStan;
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



	public String getProcessingCode() {
		return processingCode;
	}



	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}



	public Long getProcessStatusId() {
		return processStatusId;
	}



	public void setProcessStatusId(Long processStatusId) {
		this.processStatusId = processStatusId;
	}



	public Long getReconcileResultId() {
		return reconcileResultId;
	}



	public void setReconcileResultId(Long reconcileResultId) {
		this.reconcileResultId = reconcileResultId;
	}



	public String getResponseCode() {
		return responseCode;
	}



	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}



	public String getReversedFlag() {
		return reversedFlag;
	}



	public void setReversedFlag(String reversedFlag) {
		this.reversedFlag = reversedFlag;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Long getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}



	public String getUniqueReceiptCode() {
		return uniqueReceiptCode;
	}



	public void setUniqueReceiptCode(String uniqueReceiptCode) {
		this.uniqueReceiptCode = uniqueReceiptCode;
	}



	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
