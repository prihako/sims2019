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
import com.balicamp.soap.helper.PengujianRequestGenerator;
import com.balicamp.soap.ws.pengujian.InquiryResult;
import com.balicamp.soap.ws.pengujian.PaymentManagerControllerPortType;
import com.balicamp.soap.ws.pengujian.PaymentResult;

public class PengujianChannel {

	private static final Logger LOG = Logger.getLogger(PengujianChannel.class.getName());

	@Autowired
	private PengujianRequestGenerator pengujianRequestGenerate;
	
	@Autowired
	private ForcePaymentLogRunner forcePaymentLogRunner;
	
	String address;
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public InquiryResult inquiry(ReconcileDto reconcile) {

		InquiryResult result = proxy().inquiry(pengujianRequestGenerate.genereateInquiryRequest(reconcile.getInvoiceNo(),
				reconcile.getClientId()));

		forcePaymentLogRunner.saveForcePaymentLog(reconcile.getInvoiceNo(), 
				reconcile.getClientId(), WebServiceConstant.PAP_INQ, result.getStatus().getErrorCode(), 
				result.toString());
		
		LOG.info("Inquiry Response : " + result.toString());

		return result;
	}
	
	public PaymentResult payment(ReconcileDto reconcile,
			Date paymentDate, String remarks) {

		InquiryResult inquiryResult = inquiry(reconcile);
		
		PaymentResult paymentResult = null;
		if(inquiryResult.getStatus().getErrorCode().equals("00")) {
			String paymentAmount = inquiryResult.getBillDetails().getBillDetails().getItem().get(0).getBillAmount();
			paymentResult = proxy().payment(pengujianRequestGenerate.genereatePaymentRequest(reconcile.getInvoiceNo(),
				reconcile.getClientId(), paymentAmount));
			
			forcePaymentLogRunner.saveForcePaymentLog(reconcile.getInvoiceNo(), 
					reconcile.getClientId(), WebServiceConstant.PAP_PAY, paymentResult.toString(), 
					paymentResult.getStatus().getErrorCode());
			
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