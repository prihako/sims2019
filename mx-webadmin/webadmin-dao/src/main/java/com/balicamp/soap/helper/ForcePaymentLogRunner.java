package com.balicamp.soap.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.balicamp.dao.mx.ForcePaymentLogDao;

@Component
public class ForcePaymentLogRunner {
	
	@Autowired
	private ForcePaymentLogDao forcePaymentLogDao;
	
	public void saveForcePaymentLog(String invoiceNo, String clientId, 
			String transactionType, String responseCode, String responseMessage) {
		ForcePaymentLogThread forcePaymentThread  = new ForcePaymentLogThread(forcePaymentLogDao,
				invoiceNo, clientId, transactionType,  responseCode, responseMessage);
		forcePaymentThread.run();
		
	}
}
