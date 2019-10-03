/**
 * 
 */
package id.co.sigma.mx.channel.ws;

import id.co.sigma.mx.channel.ws.wsdl.BillPaymentServiceSoap;
import id.co.sigma.mx.channel.ws.wsdl.InquiryRequest;
import id.co.sigma.mx.channel.ws.wsdl.InquiryResponse2;
import id.co.sigma.mx.channel.ws.wsdl.PaymentRequest;
import id.co.sigma.mx.channel.ws.wsdl.PaymentResponse2;
import id.co.sigma.mx.channel.ws.wsdl.ReversalRequest;
import id.co.sigma.mx.channel.ws.wsdl.ReversalResponse;

/**
 * @author Yusprasetya
 *
 */
public class Test implements BillPaymentServiceSoap {

	/* (non-Javadoc)
	 * @see id.co.sigma.mx.channel.ws.model.BillPaymentServiceSoap#payment(id.co.sigma.mx.channel.ws.model.PaymentRequest)
	 */
	@Override
	public PaymentResponse2 payment(PaymentRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see id.co.sigma.mx.channel.ws.model.BillPaymentServiceSoap#echoTest(java.lang.String)
	 */
	@Override
	public String echoTest(String tx) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see id.co.sigma.mx.channel.ws.model.BillPaymentServiceSoap#reverse(id.co.sigma.mx.channel.ws.model.ReversalRequest)
	 */
	@Override
	public ReversalResponse reverse(ReversalRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see id.co.sigma.mx.channel.ws.model.BillPaymentServiceSoap#inquiry(id.co.sigma.mx.channel.ws.model.InquiryRequest)
	 */
	@Override
	public InquiryResponse2 inquiry(InquiryRequest request) {
		// TODO Auto-generated method stub
		
		
		
		return null;
	}

}
