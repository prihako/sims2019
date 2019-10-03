package com.balicamp.model.mx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.balicamp.model.admin.BaseAdminModel;

public class InquiryReconcileDto  extends BaseAdminModel{

	private static final long serialVersionUID = 7072094790917437322L;
	
	private Integer no;
	private String trxId;
	private String clientId;
	private String clientName;
	private String invoiceNo;
	private String transactionName;
	private String delivChannel;
	private String billerRc;
	private String channelRc;
	private String simsStatus;
	private String paymentDate;
	private String transactionDate;
	private String reconcileStatus;
	private String transactionDateSims;
	private String invoiceDueDate;
	private String bankName;
	private String billerResponse;
	private String channelResponse;

	public String getTrxId() {
		return trxId;
	}



	public void setTrxId(String trxId) {
		this.trxId = trxId;
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



	public String getInvoiceNo() {
		return invoiceNo;
	}



	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}



	public String getTransactionName() {
		return transactionName;
	}



	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}



	public String getDelivChannel() {
		return delivChannel;
	}



	public void setDelivChannel(String delivChannel) {
		this.delivChannel = delivChannel;
	}



	public String getBillerRc() {
		return billerRc;
	}



	public void setBillerRc(String billerRc) {
		this.billerRc = billerRc;
	}



	public String getChannelRc() {
		return channelRc;
	}



	public void setChannelRc(String channelRc) {
		this.channelRc = channelRc;
	}



	public String getSimsStatus() {
		return simsStatus;
	}



	public void setSimsStatus(String simsStatus) {
		this.simsStatus = simsStatus;
	}



	public String getPaymentDate() {
		return paymentDate;
	}



	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}



	public String getTransactionDate() {
		return transactionDate;
	}
	
	
	public String getTransactionDate_ddMMyyyy() {
		
		if(transactionDate != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH24:MI:SS");
			try {
				Date date = sdf.parse(transactionDate);
				return (new SimpleDateFormat("dd-MM-yyyy")).format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return transactionDate;
	}



	public void setTransactionTime(String transactionDate) {
		this.transactionDate = transactionDate;
	}



	public String getReconcileStatus() {
		return reconcileStatus;
	}



	public void setReconcileStatus(String reconcileStatus) {
		this.reconcileStatus = reconcileStatus;
	}



	@Override
	public Object getPKey() {
		return trxId;
	}



	public String getTransactionDateSims() {
		return transactionDateSims;
	}



	public void setTransactionDateSims(String transactionDateSims) {
		this.transactionDateSims = transactionDateSims;
	}



	public String getInvoiceDueDate() {
		return invoiceDueDate;
	}



	public void setInvoiceDueDate(String invoiceDueDate) {
		this.invoiceDueDate = invoiceDueDate;
	}



	public String getBankName() {
		return bankName;
	}



	public void setBankName(String bankName) {
		this.bankName = bankName;
	}



	public Integer getNo() {
		return no;
	}



	public void setNo(Integer no) {
		this.no = no;
	}



	public String getBillerResponse() {
		return billerResponse;
	}



	public void setBillerResponse(String billerResponse) {
		this.billerResponse = billerResponse;
	}



	public String getChannelResponse() {
		return channelResponse;
	}



	public void setChannelResponse(String channelResponse) {
		this.channelResponse = channelResponse;
	}

}
