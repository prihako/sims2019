package com.balicamp.soap.ws.channel;

import java.util.Date;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.soap.WebServiceConstant;
import com.balicamp.soap.helper.ForcePaymentLogRunner;
import com.balicamp.soap.helper.IarRequestGenerator;
import com.balicamp.soap.helper.UnarRequestGenerator;
import com.balicamp.soap.ws.unar.InquiryRequest;
import com.balicamp.soap.ws.unar.PaymentRequest;
import com.balicamp.soap.ws.unar.InquiryResult;
import com.balicamp.soap.ws.unar.PaymentManagerControllerPortType;
import com.balicamp.soap.ws.unar.PaymentResult;

public class unarChannel {

	private static final Logger LOG = Logger.getLogger(unarChannel.class.getName());

	@Autowired
	private UnarRequestGenerator unarRequestGenerate;
	
	@Autowired
	private ForcePaymentLogRunner forcePaymentLogRunner;
	
	String address;
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public InquiryResult inquiry(ReconcileDto reconcile) {

		InquiryRequest inquiryRequest = unarRequestGenerate.genereateInquiryRequest(reconcile.getInvoiceNo(),
				reconcile.getClientId());
		
		forcePaymentLogRunner.saveForcePaymentLog(reconcile.getInvoiceNo(), 
				reconcile.getClientId(), WebServiceConstant.UNAR_INQ, null, 
				inquiryRequest.toString(), true);
		
		InquiryResult result = proxy().inquiry(inquiryRequest);

		forcePaymentLogRunner.saveForcePaymentLog(reconcile.getInvoiceNo(), 
				reconcile.getClientId(), WebServiceConstant.UNAR_INQ, result.getStatus().getErrorCode(), 
				result.toString(), false);
		
		LOG.info("Inquiry Response : " + result.toString());

		return result;
	}
	
	public PaymentResult payment(ReconcileDto reconcile,
			Date paymentDate, String remarks) {

		InquiryResult inquiryResult = inquiry(reconcile);
		
		PaymentResult paymentResult = null;
		if(inquiryResult.getStatus().getErrorCode().equals("00")) {
			String paymentAmount = inquiryResult.getBillDetails().getBillDetails().getItem().get(0).getBillAmount();
			
			PaymentRequest paymentRequest = unarRequestGenerate.genereatePaymentRequest(reconcile.getInvoiceNo(),
					reconcile.getClientId(), paymentAmount);
			
			forcePaymentLogRunner.saveForcePaymentLog(reconcile.getInvoiceNo(), 
					reconcile.getClientId(), WebServiceConstant.UNAR_PAY,
					null, paymentRequest.toString(), true);
			
			paymentResult = proxy().payment(paymentRequest);
			
			forcePaymentLogRunner.saveForcePaymentLog(reconcile.getInvoiceNo(), 
					reconcile.getClientId(), WebServiceConstant.UNAR_PAY, 
					paymentResult.getStatus().getErrorCode() ,paymentResult.toString(), false);
			
			LOG.info("Payment Response : " + paymentResult.toString());
		}else {
			LOG.info("Inquiry Failed, not continue to payment");
			LOG.info("Payment Response code : " + inquiryResult.getStatus().getErrorCode());
		}

		return paymentResult;
	}

	public PaymentManagerControllerPortType proxy() {

		JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
		jaxWsProxyFactoryBean.setServiceClass(PaymentManagerControllerPortType.class);
		jaxWsProxyFactoryBean.setAddress(address);

		jaxWsProxyFactoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		jaxWsProxyFactoryBean.getInInterceptors().add(new LoggingInInterceptor());
		jaxWsProxyFactoryBean.getInFaultInterceptors().add(new LoggingOutInterceptor());

		return (PaymentManagerControllerPortType) jaxWsProxyFactoryBean.create();

	}
}
