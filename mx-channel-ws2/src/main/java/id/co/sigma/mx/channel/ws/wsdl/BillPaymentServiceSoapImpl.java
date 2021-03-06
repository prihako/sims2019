/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package id.co.sigma.mx.channel.ws.wsdl;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.1 2015-08-28T13:17:32.442+07:00
 * Generated source version: 2.7.1
 * 
 */

@javax.jws.WebService(serviceName = "BillPaymentService", 
					portName = "BillPaymentServiceSoap", 
					targetNamespace = "bni.h2h.billpayment.ws", 
					wsdlLocation = "classpath:/wsdl/BNI.wsdl", 
					endpointInterface = "ws.billpayment.h2h.bni.BillPaymentServiceSoap")
public class BillPaymentServiceSoapImpl implements BillPaymentServiceSoap {

	private static final Logger LOG = Logger
			.getLogger(BillPaymentServiceSoapImpl.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ws.billpayment.h2h.bni.BillPaymentServiceSoap#echoTest(java.lang.String
	 * tx )*
	 */
	public java.lang.String echoTest(java.lang.String tx) {
		LOG.info("Executing operation echoTest");
		System.out.println(tx);
		try {
			java.lang.String _return = "";
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ws.billpayment.h2h.bni.BillPaymentServiceSoap#payment(ws.billpayment.
	 * h2h.bni.PaymentRequest request )*
	 */
	public PaymentResponse payment(PaymentRequest request) {
		LOG.info("Executing operation payment");
		System.out.println(request);
		try {
			PaymentResponse _return = null;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ws.billpayment.h2h.bni.BillPaymentServiceSoap#reverse(ws.billpayment.
	 * h2h.bni.ReversalRequest request )*
	 */
	public ReversalResponse reverse(ReversalRequest request) {
		LOG.info("Executing operation reverse");
		System.out.println(request);
		try {
			ReversalResponse _return = null;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ws.billpayment.h2h.bni.BillPaymentServiceSoap#inquiry(ws.billpayment.
	 * h2h.bni.InquiryRequest request )*
	 */
	public InquiryResponse inquiry(InquiryRequest request) {
		LOG.info("Executing operation inquiry");
		System.out.println(request);
		try {
			InquiryResponse _return = null;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

}
