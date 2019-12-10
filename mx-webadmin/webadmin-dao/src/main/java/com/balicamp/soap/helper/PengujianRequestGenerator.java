package com.balicamp.soap.helper;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.balicamp.soap.WebServiceConstant;
import com.balicamp.soap.ws.pengujian.ArrayOfString;
import com.balicamp.soap.ws.pengujian.InquiryRequest;
import com.balicamp.soap.ws.pengujian.PaymentRequest;
import com.balicamp.soap.ws.pengujian.StringArray;

@Component
public class PengujianRequestGenerator {
	
	public InquiryRequest genereateInquiryRequest(String invoiceNo, String clientId) {
		Date now = new Date();
		InquiryRequest inquiryRequest = new InquiryRequest();
		inquiryRequest.setTransmissionDateTime(WebServiceConstant.TRANSMISSION_DATE_TIME_FORMAT.format(now));
		inquiryRequest.setTrxDateTime(WebServiceConstant.TRANSACTION_DATE_TIME_FORMAT.format(now));
		inquiryRequest.setBillKey1(invoiceNo);
		inquiryRequest.setBillKey2(clientId);
		inquiryRequest.setBillKey3(WebServiceConstant.PAP_TRANSACTION_TYPE);
		inquiryRequest.setLanguage(WebServiceConstant.LANGUAGE);
		inquiryRequest.setChannelID(WebServiceConstant.CHANNEL_ID);
		inquiryRequest.setCompanyCode(WebServiceConstant.COMPANY_CODE);
		
		return inquiryRequest;
	}
	
	public PaymentRequest genereatePaymentRequest(String invoiceNo, 
			String clientId, String paymentAmount) {
		Date now = new Date();
		PaymentRequest paymentRequest = new PaymentRequest();
		
		StringArray stringArray = new StringArray();
		ArrayOfString arrayOfString = new ArrayOfString();
		stringArray.getItem().add(WebServiceConstant.PAID_BILS);
		arrayOfString.setStrings(stringArray);
		paymentRequest.setPaidBills(arrayOfString);
		
		paymentRequest.setPaymentAmount(paymentAmount);
		paymentRequest.setTransmissionDateTime(WebServiceConstant.TRANSMISSION_DATE_TIME_FORMAT.format(now));
		paymentRequest.setTrxDateTime(WebServiceConstant.TRANSACTION_DATE_TIME_FORMAT.format(now));
		paymentRequest.setBillKey1(invoiceNo);
		paymentRequest.setBillKey2(clientId);
		paymentRequest.setBillKey3(WebServiceConstant.PAP_TRANSACTION_TYPE);
		paymentRequest.setLanguage(WebServiceConstant.LANGUAGE);
		paymentRequest.setChannelID(WebServiceConstant.CHANNEL_ID);
		paymentRequest.setCompanyCode(WebServiceConstant.COMPANY_CODE);
		paymentRequest.setCurrency(WebServiceConstant.CURRENCY);
		
		return paymentRequest;
	}
}
