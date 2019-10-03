/**
 * 
 */
package com.balicamp.model.mx;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This wrap object for query parameter to MessageLogs
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsParameter.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
public class MessageLogsParameter implements Serializable {

	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat formater = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

	private String transactionId;
	private String transactionCode;
	private String transactionDesc;
	private String endpointCode;
	private Date startDate;
	private Date endDate;
	private String rawKey;
	

	public MessageLogsParameter() {
	}

	public MessageLogsParameter(String transactionId, String transactionCode, String transactionDesc,
			String endpointCode, Date startDate, Date endDate) {
		super();
		this.transactionId = transactionId;
		this.transactionCode = transactionCode;
		this.transactionDesc = transactionDesc;
		this.endpointCode = endpointCode;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransactionDesc() {
		return transactionDesc;
	}

	public void setTransactionDesc(String transactionDesc) {
		this.transactionDesc = transactionDesc;
	}

	public String getEndpointCode() {
		return endpointCode;
	}

	public void setEndpointCode(String endpointCode) {
		this.endpointCode = endpointCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getConcatParameter() {
		StringBuffer bfr = new StringBuffer();
		bfr.append(transactionId != null ? " Transaction Id : " + transactionId : "");
		bfr.append(transactionCode != null ? " Transaction Code : " + transactionCode : "");
		bfr.append(transactionDesc != null ? " Transaction Desc : " + transactionDesc : "");
		bfr.append(endpointCode != null ? " Endpoint Code : " + endpointCode : "");
		bfr.append(startDate != null ? " Start Date : " + formater.format(startDate) : "");
		bfr.append(endDate != null ? " End Date : " + formater.format(endDate) : "");
		bfr.append(rawKey != null ? " Raw Key : " + rawKey : "");

		return bfr.toString();
	}

	public String getRawKey() {
		return rawKey;
	}

	public void setRawKey(String rawKey) {
		this.rawKey = rawKey;
	}

	@Override
	public String toString() {
		return "MessageLogsParameter [transactionId=" + transactionId
				+ ", transactionCode=" + transactionCode + ", transactionDesc="
				+ transactionDesc + ", endpointCode=" + endpointCode
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", rawKey=" + rawKey + "]";
	}	
	
}
