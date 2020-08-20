/**
 * 
 */
package id.co.sigma.mx.channel.bri.syariah.ws;

import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.BillPaymentServiceSoap;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.InquiryRequest;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.InquiryResponse;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.PaymentRequest;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.PaymentResponse;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.ReversalRequest;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.ReversalResponse;

/**
 * @author Yusprasetya
 *
 */
public class Test implements BillPaymentServiceSoap {

	/* (non-Javadoc)
	 * @see id.co.sigma.mx.channel.ws.model.BillPaymentServiceSoap#payment(id.co.sigma.mx.channel.ws.model.PaymentRequest)
	 */
	@Override
	public PaymentResponse payment(PaymentRequest request) {
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
	public InquiryResponse inquiry(InquiryRequest request) {

		return null;
	}

}
