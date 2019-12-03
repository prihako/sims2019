package com.balicamp.soap.helper;

import com.balicamp.dao.mx.ForcePaymentLogDao;
import com.balicamp.model.mx.ForcePaymentLog;

public class ForcePaymentLogThread implements Runnable {
	
	private ForcePaymentLogDao forcePaymentLogDao;
	private String invoiceNo;
	private String clientId;
	private String transactionType;
	private String responseMessage;
	private String responseCode;

	public ForcePaymentLogThread(ForcePaymentLogDao forcePaymentLogDao, String invoiceNo, String clientId,
			String transactionType, String responseCode, String responseMessage) {
		super();
		this.forcePaymentLogDao = forcePaymentLogDao;
		this.invoiceNo = invoiceNo;
		this.clientId = clientId;
		this.transactionType = transactionType;
		this.responseMessage = responseMessage;
		this.responseCode = responseCode;
	}

	@Override
	public void run() {
		ForcePaymentLog forcePaymentLog = new ForcePaymentLog();
		forcePaymentLog.setInvoiceNo(invoiceNo);
		forcePaymentLog.setClientId(clientId);
		forcePaymentLog.setTransactionType(transactionType);
		forcePaymentLog.setResponseMessage(responseMessage);
		forcePaymentLog.setResponseCode(responseCode);
		
		forcePaymentLogDao.save(forcePaymentLog);
	}
	
}
