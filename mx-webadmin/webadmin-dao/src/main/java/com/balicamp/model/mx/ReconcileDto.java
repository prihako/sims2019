package com.balicamp.model.mx;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.parameter.SystemParameter;

public class ReconcileDto extends BaseAdminModel {

	private static final long serialVersionUID = 1049160728433482866L;

	private Integer no;
	private String invoiceNo;
	private String trxId;
	private String trxType;
	private String transactionName;
	private String clientId;
	private String clientName;
	private String billerRc;
	private String channelRc;
	private String mt940Status;
	private String simsStatus;
	private String paymentDateSims;
	private String reconcileStatus;
	private String delivChannel;
	private String invoiceDueDate;
	private String invoiceDendaNo;
	private String transactionTime;
	private String paymentChannel;
	private String statusDenda;
	private String remarks;
	private String reconFlag;
	private String trxAmount;
	private String trxAmountDenda;
	
	private String bankName;
	private String bankBranch;
	private String rawData;

	

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String raw, String channelRc,
			String transactionCode, String klienID) {

		Properties prop = new Properties();
		try {
			prop.load(new StringReader(raw));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (prop.getProperty("/custom/clientID/text()") != null) {
			this.clientId = prop.getProperty("/custom/clientID/text()");

		} else {
			this.clientId = clientId;
		}

	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String raw, String channelRc,
			String transactionCode) {

		Properties prop = new Properties();
		try {
			prop.load(new StringReader(raw));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(" TES PROPERTY CLIENT NAME " +
		// prop.getProperty("/custom/clientName/text()"));

		if (prop.getProperty("/custom/clientName/text()") != null) {
			this.clientName = prop.getProperty("/custom/clientName/text()");

		} else {
			this.clientName = clientName;
		}

	}

	public String getInvoiceNo() {

		return invoiceNo;
	}

	public void setInvoiceNo(String raw, String channelRc,
			String transactionCode, String invoiceNo) {

		// System.out.println("raw lastIndex of /custom/invoiceNumber/text()= "
		// + raw.lastIndexOf("/custom/invoiceNumber/text()="));
		//
		// System.out.println("raw lastindex of custom/channelID/text() "
		// + raw.lastIndexOf("custom/channelID/text()="));
		// System.out.println("raw lastindex of data/responseCode/text() "
		// + raw.lastIndexOf("data/responseCode/text()="));

		// System.out.println("MEMILIKI INVOICE NO -->" + invoiceNo);

		Properties prop = new Properties();
		try {
			prop.load(new StringReader(raw));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(" TES PROPERTY INVOICE NUMBER " +
		// prop.getProperty("/custom/invoiceNumber/text()"));

		if (prop.getProperty("/custom/invoiceNumber/text()") != null) {
			this.invoiceNo = prop.getProperty("/custom/invoiceNumber/text()");

		} else {
			this.invoiceNo = invoiceNo;
		}

		// Properties capitals = new Properties();
		//
		// capitals.put("Illinois", "Springfield");
		// capitals.put("Missouri", "Jefferson City");
		// capitals.put("Washington", "Olympia");
		// capitals.put("California", "Sacramento");
		// capitals.put("Indiana", "Indianapolis");

		// if (raw.contains("/custom/invoiceNumber/text()=")) {
		//
		// String haha = "/custom/invoiceNumber/text()" + invoiceNo;
		//
		// if (raw.contains(haha)) {
		// String invoiceNumber = haha.substring(
		// haha.lastIndexOf("=") + 1, haha.length() - 1);
		// System.out.println("invoice no/channelRc  " + invoiceNo + "/"
		// + channelRc);
		// this.invoiceNo = invoiceNumber;
		// } else {
		// String invoiceNoRAW = null;
		// if (raw.lastIndexOf("/custom/invoiceNumber/text()=") < raw
		// .lastIndexOf("custom/channelID/text()")) {
		//
		// invoiceNoRAW = raw.substring(
		// raw.lastIndexOf("/custom/invoiceNumber/text()="),
		// raw.lastIndexOf("custom/channelID/text()"));
		// } else if (raw.lastIndexOf("/custom/invoiceNumber/text()=") < raw
		// .lastIndexOf("data/responseCode/text()")) {
		// invoiceNoRAW = raw.substring(
		// raw.lastIndexOf("/custom/invoiceNumber/text()="),
		// raw.lastIndexOf("data/responseCode/text()"));
		// } else if (raw.lastIndexOf("/custom/invoiceNumber/text()=") < raw
		// .lastIndexOf("")) {
		// invoiceNoRAW = raw.substring(
		// raw.lastIndexOf("/custom/invoiceNumber/text()="),
		// raw.lastIndexOf(""));
		// }
		//
		// String invoiceNumber = invoiceNoRAW.substring(
		// invoiceNoRAW.lastIndexOf("=") + 1,
		// invoiceNoRAW.length() - 1);
		// System.out.println("invoice no/channelRc  " + invoiceNumber
		// + "/" + channelRc);
		// this.invoiceNo = invoiceNumber;
		// }
		//
		// } else {
		// this.invoiceNo = invoiceNo;
		//
		// }

	}


	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getChannelRc() {
		return channelRc;
	}

	public void setChannelRc(String channelRc) {
		this.channelRc = channelRc;
	}


	@Override
	public Object getPKey() {
		return trxId;
	}

	public String getBillerRc() {
		return billerRc;
	}

	public void setBillerRc(String billerRc) {
		this.billerRc = billerRc;
	}

	public String getMt940Status() {
		return mt940Status;
	}

	public void setMt940Status(String mt940Status) {
		this.mt940Status = mt940Status;
	}

	public String getSimsStatus() {
		return simsStatus;
	}

	public void setSimsStatus(String simsStatus) {
		this.simsStatus = simsStatus;
	}

	public String getReconcileStatus() {
		return reconcileStatus;
	}

	public void setReconcileStatus(String reconcileStatus) {
		this.reconcileStatus = reconcileStatus;
	}



	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

	public String getInvoiceDueDate() {
		return invoiceDueDate;
	}

	public void setInvoiceDueDate(String invoiceDueDate) {
		this.invoiceDueDate = invoiceDueDate;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getPaymentDateSims() {
		return paymentDateSims;
	}

	public void setPaymentDateSims(String paymentDateSims) {
		this.paymentDateSims = paymentDateSims;
	}

	public String getInvoiceDendaNo() {
		return invoiceDendaNo;
	}

	public void setInvoiceDendaNo(String invoiceDendaNo) {
		this.invoiceDendaNo = invoiceDendaNo;
	}

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getStatusDenda() {
		return statusDenda;
	}

	public void setStatusDenda(String statusDenda) {
		this.statusDenda = statusDenda;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getReconFlag() {
		return reconFlag;
	}

	public void setReconFlag(String reconFlag) {
		this.reconFlag = reconFlag;
	}

	public String getTrxAmount() {
		return trxAmount;
	}

	public void setTrxAmount(String trxAmount) {
		this.trxAmount = trxAmount;
	}

	public String getTrxAmountDenda() {
		return trxAmountDenda;
	}

	public void setTrxAmountDenda(String trxAmountDenda) {
		this.trxAmountDenda = trxAmountDenda;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the bankBranch
	 */
	public String getBankBranch() {
		return bankBranch;
	}

	/**
	 * @param bankBranch the bankBranch to set
	 */
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	@Override
	public String toString() {
		return "ReconcileDto [no=" + no + ", invoiceNo=" + invoiceNo + ", trxId=" + trxId + ", trxType=" + trxType
				+ ", transactionName=" + transactionName + ", clientId=" + clientId + ", clientName=" + clientName
				+ ", billerRc=" + billerRc + ", channelRc=" + channelRc + ", mt940Status=" + mt940Status
				+ ", simsStatus=" + simsStatus + ", paymentDateSims=" + paymentDateSims + ", reconcileStatus="
				+ reconcileStatus + ", delivChannel=" + delivChannel + ", invoiceDueDate=" + invoiceDueDate
				+ ", invoiceDendaNo=" + invoiceDendaNo + ", transactionTime=" + transactionTime + ", paymentChannel="
				+ paymentChannel + ", statusDenda=" + statusDenda + ", remarks=" + remarks + ", reconFlag=" + reconFlag
				+ ", trxAmount=" + trxAmount + ", trxAmountDenda=" + trxAmountDenda + ", bankName=" + bankName
				+ ", bankBranch=" + bankBranch + ", rawData=" + rawData + "]";
	}

}
