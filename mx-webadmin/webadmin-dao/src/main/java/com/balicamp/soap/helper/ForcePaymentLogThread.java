package com.balicamp.soap.helper;

import java.sql.Timestamp;
import java.util.Date;

import com.balicamp.dao.mx.ForcePaymentLogDao;
import com.balicamp.model.mx.ForcePaymentLog;

public class ForcePaymentLogThread implements Runnable {
	
	private ForcePaymentLogDao forcePaymentLogDao;
	private String invoiceNo;
	private String clientId;
	private String transactionType;
	private String responseMessage;
	private String responseCode;
	private Boolean isRequest;
	private Timestamp createdOn;

	public ForcePaymentLogThread(ForcePaymentLogDao forcePaymentLogDao, String invoiceNo, String clientId,
			String transactionType, String responseCode, String responseMessage, Boolean isRequest) {
		super();
		this.forcePaymentLogDao = forcePaymentLogDao;
		this.invoiceNo = invoiceNo;
		this.clientId = clientId;
		this.transactionType = transactionType;
		this.responseMessage = responseMessage;
		this.responseCode = responseCode;
		this.isRequest = isRequest;
		this.createdOn = new Timestamp(System.currentTimeMillis());
	}

	@Override
	public void run() {
		ForcePaymentLog forcePaymentLog = new ForcePaymentLog();
		forcePaymentLog.setInvoiceNo(invoiceNo);
		forcePaymentLog.setClientId(clientId);
		forcePaymentLog.setTransactionType(transactionType);
		forcePaymentLog.setResponseMessage(responseMessage);
		forcePaymentLog.setResponseCode(responseCode);
		forcePaymentLog.setCreatedOn(createdOn);
		forcePaymentLogDao.save(forcePaymentLog);
	}
	
}
