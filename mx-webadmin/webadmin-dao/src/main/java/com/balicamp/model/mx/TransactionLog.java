package com.balicamp.model.mx;

import java.io.Serializable;

import javax.persistence.*;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;

/**
 * The persistent class for the transaction_logs database table.
 * 
 */
@Entity
@Table(name = "transaction_logs")
// @NamedQuery(name="TransactionLog.findAll",
// query="SELECT t FROM TransactionLog t")
public class TransactionLog extends BaseAdminModel implements ISequencesModel,
		PropertySelectionData, Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "channel_code")
	private String channelCode;

	@Column(name = "channel_rc")
	private String channelRc;

	@Column(name = "client_id")
	private String clientID;

	@Column(name = "client_name")
	private String clientName;

	@Column(name = "endpoint_biller")
	private String endpoint;

	@Column(name = "mx_rc")
	private String mxRc;

	@Column(name = "invoice_no")
	private BigDecimal noInvoice;

	private String raw;

	@Column(name = "response_code")
	private String responseCode;

	@Column(name = "retrieval_key")
	private String retrievalKey;

	@Column(name = "transaction_code")
	private String transactionCode;

	@Id
	@Column(name = "transaction_id")
	private String transactionId;

//	@Column(name = "transaction_name")
//	private String transactionName;

	@Column(name = "transaction_time")
	private Timestamp transactionTime;

	@Column(name = "transaction_type")
	private BigDecimal transactionType;
	
	

	private String type;

	public TransactionLog() {
	}
	
	public String getTransactionLogTime(){
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String output = formatter.format(getTransactionTime());
		return output;
	}
	
	public String getRawInvoiceNo(){
		String invoiceNoRAW = getRaw().substring(getRaw().lastIndexOf("/custom/invoiceNumber/text()="),
				getRaw().lastIndexOf("custom/channelID/text()"));
		System.out.println("RAW invoice no  " + invoiceNoRAW);

		String invoiceNo = invoiceNoRAW.substring(invoiceNoRAW.lastIndexOf("=") + 1, invoiceNoRAW.length() - 1);
		System.out.println("invoice no  " + invoiceNo);
		
		return invoiceNo;
	}
	
	public String getRawClientID(){
		String clientIdRAW = getRaw().substring(getRaw().lastIndexOf("/custom/clientID/text()="),
				getRaw().lastIndexOf("info/result/order/text()"));
		System.out.println("RAW Client ID  " + clientIdRAW);

		String clientID = clientIdRAW.substring(clientIdRAW.lastIndexOf("=") + 1, clientIdRAW.length() - 1);
		System.out.println("client id  " + clientID);
		
		return clientID;
	}
	
	public String getRawCompanyCode(){
		String companycodeRAW = getRaw().substring(getRaw().lastIndexOf("/custom/companyCode/text()="),
				getRaw().lastIndexOf("info/backToChannel/text()"));
		System.out.println("RAW Company Code  " + companycodeRAW);

		String companycode = companycodeRAW.substring(companycodeRAW.lastIndexOf("=") + 1, companycodeRAW.length() - 1);
		System.out.println("companyCode  " + companycode);
		
		return companycode;
	}
	
	public String getRawTransactionCode(){
		String transactioncodeRAW = getRaw().substring(getRaw().lastIndexOf("/info/transactionCode/text()="),
				getRaw().lastIndexOf("custom/billName/text()"));
		System.out.println("RAW Transaction Code  " + transactioncodeRAW);

		String transactioncode = transactioncodeRAW.substring(transactioncodeRAW.lastIndexOf("=") + 1, transactioncodeRAW.length() - 1);
		System.out.println("transactionCode  " + transactioncode);
		
		return transactioncode;
	}
	
	public String getRawTransactionType(){
		String transactiontypeRAW = getRaw().substring(getRaw().lastIndexOf("/info/transactionType/text()="),
				getRaw().lastIndexOf("data/amountTransaction/text()"));
		System.out.println("RAW Transaction type  " + transactiontypeRAW);

		String transactiontype = transactiontypeRAW.substring(transactiontypeRAW.lastIndexOf("=") + 1, transactiontypeRAW.length() - 1);
		System.out.println("transactionType  " + transactiontype);
		
		return transactiontype;
	}
	
	public String getRawPeriodBegin(){
		String periodBeginRAW = getRaw().substring(getRaw().lastIndexOf("/custom/periodBegin/text()"),
				getRaw().lastIndexOf("info/channelMappingCode/text()"));
		System.out.println("RAW Period Begin  " + periodBeginRAW);

		String periodbegin = periodBeginRAW.substring(periodBeginRAW.lastIndexOf("=") + 1, periodBeginRAW.length() - 1);
		System.out.println("periodBegin  " + periodbegin);
		
		return periodbegin;
	}
	
	public String getRawRouteId(){
		String routeIdRAW = getRaw().substring(getRaw().lastIndexOf("/info/routeId/text()="),
				getRaw().lastIndexOf("data/transactionTime/text()"));
		System.out.println("RAW Route Id  " + routeIdRAW);

		String routeid = routeIdRAW.substring(routeIdRAW.lastIndexOf("=") + 1, routeIdRAW.length() - 1);
		System.out.println("routeId  " + routeid);
		
		return routeid;
	}
	
	public String getRawClientName(){
		String clientNameRAW = getRaw().substring(getRaw().lastIndexOf("/custom/clientName/text()="),
				getRaw().lastIndexOf("data/currencyCodeTransaction/text()"));
		System.out.println("RAW Client Name  " + clientNameRAW);

		String clientName = clientNameRAW.substring(clientNameRAW.lastIndexOf("=") + 1, clientNameRAW.length() - 1);
		System.out.println("clientName  " + clientName);
		
		return clientName;
	}
	
	public String getRawPeriodEnd(){
		String periodEndRAW = getRaw().substring(getRaw().lastIndexOf("/custom/periodEnd/text()="),
				getRaw().lastIndexOf("info/project/text()"));
		System.out.println("RAW Period End  " + periodEndRAW);

		String periodEnd = periodEndRAW.substring(periodEndRAW.lastIndexOf("=") + 1, periodEndRAW.length() - 1);
		System.out.println("periodEnd  " + periodEnd);
		
		return periodEnd;
	}
	
	public String getRawTransactionDate(){
		String transactionDateRAW = getRaw().substring(getRaw().lastIndexOf("/data/transactionDate/text()="),
				getRaw().lastIndexOf("custom/periodEnd/text()"));
		System.out.println("RAW Transaction Date  " + transactionDateRAW);

		String transactionDate = transactionDateRAW.substring(transactionDateRAW.lastIndexOf("=") + 1, transactionDateRAW.length() - 1);
		System.out.println("transactionDate  " + transactionDate);
		
		return transactionDate;
	}
	

	public String getChannelCode() {
		return this.channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelRc() {
		return this.channelRc;
	}

	public void setChannelRc(String channelRc) {
		this.channelRc = channelRc;
	}

	public String getClientID() {
		return this.clientID;
	}

	public void setClientID(String clientId) {
		this.clientID = clientId;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getEndpoint() {
		return this.endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getMxRc() {
		return this.mxRc;
	}

	public void setMxRc(String mxRc) {
		this.mxRc = mxRc;
	}

	public BigDecimal getNoInvoice() {
		return this.noInvoice;
	}

	public void setNoInvoice(BigDecimal noInvoice) {
		this.noInvoice = noInvoice;
	}

	public String getRaw() {
		return this.raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}


	public String getRetrievalKey() {
		return this.retrievalKey;
	}

	public void setRetrievalKey(String retrievalKey) {
		this.retrievalKey = retrievalKey;
	}

	public String getTransactionCode() {
		return this.transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

//	public String getTransactionName() {
//		return this.transactionName;
//	}
//
//	public void setTransactionName(String transactionName) {
//		this.transactionName = transactionName;
//	}

	public Timestamp getTransactionTime() {
		return this.transactionTime;
	}

	public void setTransactionTime(Timestamp transactionTime) {
		this.transactionTime = transactionTime;
	}

	public BigDecimal getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(BigDecimal transactionType) {
		this.transactionType = transactionType;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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
		return null;
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