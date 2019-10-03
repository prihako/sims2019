package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7709310572776469286L;
	private long transactionId;
	private long outstandingAmount;
	private String errorDesc;
	private String rawTransactionMsg;
	private String messageType;

	private String transactionType;
	private Date transactionDate;
	private long prevTransactionId;
	private String clientId;
	private String clientName;

	private String clientPhone;
	private String invalidAmountFlag;
	private String invoiceId;
	private String lastMti;
	private String lastProcessingCode;

	private String lastResponseCode;
	private String paymentMethod;
	private String transactionSrc;
	private BigDecimal transactionAmount;
	private String transactionStatus;

	private long fileId;
	private String needReconcile;
	
	private Calendar transactionDateCal;
	private boolean transactionStatusFlag;
	
	//Use if biller have same account no, ex: skor and reor
	private String transactionCode;
	
	//Tambahan untuk menentukan channel pembayaran ex: 6014 (internet banking), 6010 (ATM), etc
	private String paymentChannel;
	private String paymentType;
	
	//Tambahan untuk menentukan kode cabang pembayaran ex: 1234, etc
	private String branchCode;
	
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



	public String getClientPhone() {
		return clientPhone;
	}



	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone;
	}



	public String getErrorDesc() {
		return errorDesc;
	}



	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}



	public long getFileId() {
		return fileId;
	}



	public void setFileId(long fileId) {
		this.fileId = fileId;
	}



	public String getInvalidAmountFlag() {
		return invalidAmountFlag;
	}



	public void setInvalidAmountFlag(String invalidAmountFlag) {
		this.invalidAmountFlag = invalidAmountFlag;
	}



	public String getInvoiceId() {
		return invoiceId;
	}



	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}



	public String getLastMti() {
		return lastMti;
	}



	public void setLastMti(String lastMti) {
		this.lastMti = lastMti;
	}



	public String getLastProcessingCode() {
		return lastProcessingCode;
	}



	public void setLastProcessingCode(String lastProcessingCode) {
		this.lastProcessingCode = lastProcessingCode;
	}



	public String getLastResponseCode() {
		return lastResponseCode;
	}



	public void setLastResponseCode(String lastResponseCode) {
		this.lastResponseCode = lastResponseCode;
	}



	public String getMessageType() {
		return messageType;
	}



	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}



	public String getNeedReconcile() {
		return needReconcile;
	}



	public void setNeedReconcile(String needReconcile) {
		this.needReconcile = needReconcile;
	}



	public long getOutstandingAmount() {
		return outstandingAmount;
	}



	public void setOutstandingAmount(long outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}



	public String getPaymentMethod() {
		return paymentMethod;
	}



	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}



	public long getPrevTransactionId() {
		return prevTransactionId;
	}



	public void setPrevTransactionId(long prevTransactionId) {
		this.prevTransactionId = prevTransactionId;
	}



	public String getRawTransactionMsg() {
		return rawTransactionMsg;
	}



	public void setRawTransactionMsg(String rawTransactionMsg) {
		this.rawTransactionMsg = rawTransactionMsg;
	}



	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}



	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}



	public Date getTransactionDate() {
		return transactionDate;
	}



	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}



	public long getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}



	public String getTransactionSrc() {
		return transactionSrc;
	}



	public void setTransactionSrc(String transactionSrc) {
		this.transactionSrc = transactionSrc;
	}



	public String getTransactionStatus() {
		return transactionStatus;
	}



	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}



	public String getTransactionType() {
		return transactionType;
	}



	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}



	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}



	public void setTransactionDateCal(Calendar transactionDateCal) {
		this.transactionDateCal = transactionDateCal;
	}



	public Calendar getTransactionDateCal() {
		return transactionDateCal;
	}



	public void setTransactionStatusFlag(boolean transactionStatusFlag) {
		this.transactionStatusFlag = transactionStatusFlag;
	}



	public boolean isTransactionStatusFlag() {
		return transactionStatusFlag;
	}



	public String getTransactionCode() {
		return transactionCode;
	}



	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}



	public String getPaymentChannel() {
		return paymentChannel;
	}



	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}



	public String getPaymentType() {
		return paymentType;
	}



	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}



	/**
	 * @return the branchCode
	 */
	public String getBranchCode() {
		return branchCode;
	}



	/**
	 * @param branchCode the branchCode to set
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}


}
