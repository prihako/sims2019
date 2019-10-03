package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TransactionStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1300159034984503018L;
	private Boolean singleRecordFound = null;
	private String systemTraceAuditNumber = null;
	private String retrievalReferenceNumber = null;
	private String transactionStatus = null;
	private String receiptCode = null;

	private Long transactionId = null;
	private String invoiceId = null;
	private String clientId = null;
	private Long fileId = null;
	private Date transactionDate = null;

	
	public String getClientId() {
		return clientId;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public Long getFileId() {
		return fileId;
	}


	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}


	public String getInvoiceId() {
		return invoiceId;
	}


	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}


	public String getReceiptCode() {
		return receiptCode;
	}


	public void setReceiptCode(String receiptCode) {
		this.receiptCode = receiptCode;
	}


	public String getRetrievalReferenceNumber() {
		return retrievalReferenceNumber;
	}


	public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
		this.retrievalReferenceNumber = retrievalReferenceNumber;
	}


	public Boolean getSingleRecordFound() {
		return singleRecordFound;
	}


	public void setSingleRecordFound(Boolean singleRecordFound) {
		this.singleRecordFound = singleRecordFound;
	}


	public String getSystemTraceAuditNumber() {
		return systemTraceAuditNumber;
	}


	public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
		this.systemTraceAuditNumber = systemTraceAuditNumber;
	}


	public Date getTransactionDate() {
		return transactionDate;
	}


	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}


	public Long getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}


	public String getTransactionStatus() {
		return transactionStatus;
	}


	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}


	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
