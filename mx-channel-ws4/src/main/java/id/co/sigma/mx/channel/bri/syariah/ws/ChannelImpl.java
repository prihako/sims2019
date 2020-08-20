package id.co.sigma.mx.channel.bri.syariah.ws;

import id.co.sigma.mx.channel.bri.syariah.ws.model.Data;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.BillPaymentServiceSoap;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.InquiryRequest;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.InquiryResponse;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.InquiryResponse2;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.PaymentRequest;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.PaymentResponse;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.PaymentResponse2;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.ReversalRequest;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.ReversalResponse;
import id.co.sigma.mx.channel.bri.syariah.ws.wsdl.bri.Status;
import id.co.sigma.mx.transport.MessageListener;
import id.co.sigma.mx.transport.Transport;
import id.co.sigma.mx.transport.TransportNotReadyException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 *
 * @author <a href="mailto:muhammad.ichsan@sigma.co.id">Muhammad Ichsan</a>
 *
 */
//public class ChannelImpl implements Channel, Transport<WsMessage>, BillPaymentServiceSoap {
public class ChannelImpl implements Transport<WsMessage>, BillPaymentServiceSoap {
	private static AtomicLong lastCounter = new AtomicLong();

	private final Map<String, CountDownLatch> latches = new HashMap<String, CountDownLatch>();
	private final Map<String, WsMessage> responses = new HashMap<String, WsMessage>();

	private Collection<MessageListener<WsMessage>> listeners =
		new ArrayList<MessageListener<WsMessage>>();

	/*@Override
	public void login(String username, String password) {
		if (!"arium01".equals(username) || !"pwd".equals(password)) {
			throw new RuntimeException("Invalid credential");
		}
	}*/

	/*@Override
	public Data execute(String mappingCode,
			Data requestData) {

		if ("DUMMY123".equals(mappingCode)) {
			return createDummyResponse(requestData);
		}

		String assoc = nextUniqueAssoc();
		WsMessage message = new WsMessage(mappingCode, requestData);
		message.setAssocKey(assoc);

		CountDownLatch latch = new CountDownLatch(1);
		latches.put(assoc, latch);
		tellListeners(message);

		try {
			latch.await(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {}
		WsMessage response = responses.remove(assoc);
		latches.remove(assoc);

		if (response == null) {
			throw new RuntimeException("Timeout");
		}

		return response.getData();
	}*/

	private String nextUniqueAssoc() {
		return FastDateFormat.getInstance("ddMMHHmm.ss.").format(System.currentTimeMillis()) + lastCounter.getAndIncrement();
	}

	/*private Data createDummyResponse(Data reqData) {
		Map<String, String> resData = new HashMap<String, String>();
		resData.put("fullName", reqData.toMap().get("name") + " bin Mahmud");
		resData.put("accountNumber", "1234.5678");
		return new Data(resData);
	}*/

	private InquiryResponse createDummyInquiryResponse2(InquiryRequest request) {
		InquiryResponse response 	= new InquiryResponse();
		Status status				= new Status();

		status.setIsError(false);
		status.setErrorCode("");
		status.setStatusDescription(null);

		response.setCurrency("IDR");
		response.setBillInfo1("Client 001");
		response.setBillInfo2("20130101");
		response.setBillInfo3("20130606");
		response.setStatus(status);

		return response;
	}

	private PaymentResponse createDummyPaymentResponse2(PaymentRequest request) {
		PaymentResponse response 	= new PaymentResponse();
		Status status				= new Status();

		status.setIsError(false);
		status.setErrorCode("");
		status.setStatusDescription(null);

		response.setStatus(status);
		response.setBillInfo1("Client 001");
		response.setBillInfo2("20130101");
		response.setBillInfo3("20130606");

		return response;
	}

	private ReversalResponse createDummyReversalResponse(ReversalRequest request) {
		ReversalResponse response 	= new ReversalResponse();
		Status status				= new Status();

		status.setIsError(false);
		status.setErrorCode("");
		status.setStatusDescription(null);

		response.setStatus(status);

		return response;
	}

	private void tellListeners(WsMessage message) {
		System.out.println("*** listener : "+listeners.size());
		for (MessageListener<WsMessage> listener : listeners) {
			System.out.println("*** listener : "+listener);
			System.out.println("*** message : "+message);
			listener.messageReceived(message);
		}
	}

	@Override
	public void write(WsMessage message) throws TransportNotReadyException {
		// Only receives response from MX
		System.out.println("------- write : "+latches.containsKey(message.getAssocKey()));

		if (latches.containsKey(message.getAssocKey())) {
			System.out.println("------- message write : "+message+" -- message assoc : "+message.getAssocKey().toString());
			System.out.println("------- responses write : "+responses.size());
			System.out.println("------- latches write : "+latches.size());
			responses.put(message.getAssocKey(), message);
			System.out.println("------- responses write : "+responses.size()+" -- value : "+responses.toString());
			latches.remove(message.getAssocKey()).countDown();
		}
	}

	@Override
	public void addMessageListener(MessageListener<WsMessage> listener) {
		listeners.add(listener);
	}

	/*@Override
	public Data showSample() {
		Map<String, String> content = new HashMap<String, String>();
		content.put("name", "Uni Sarah");
		content.put("age", "23");
		content.put("school", "SmunDel");

		return new Data(content);
	}*/

	public void setMessageListeners(Collection<MessageListener<WsMessage>> listeners) {
		this.listeners = listeners;
	}

	@Override
	public PaymentResponse payment(PaymentRequest request) {
		PaymentResponse paymentResponse = new PaymentResponse();

		String mappingCode	= "PAY_BBRISYA";
		Data requestData 	= new Data(request.toMap());

		if ("DUMMY123".equals(mappingCode)) {
			return createDummyPaymentResponse2(request);
		}

		String assoc = nextUniqueAssoc();
		WsMessage message = new WsMessage(mappingCode, requestData);
		message.setAssocKey(assoc);

		System.out.println("------------- pay message : "+message.toString());

		CountDownLatch latch = new CountDownLatch(1);
		latches.put(assoc, latch);
		tellListeners(message);

		try {
			latch.await(120, TimeUnit.SECONDS);
		} catch (InterruptedException e) {}

		System.out.println("------------- pay responses : "+responses.size()+" -- "+responses.toString()+" -- assoc : "+assoc);
		System.out.println("------------- pay latches : "+latches.size()+" -- assoc : "+assoc);
		WsMessage response = responses.remove(assoc);
		latches.remove(assoc);
		System.out.println("------------- pay response : "+response);

		if (response == null) {
			throw new RuntimeException("Timeout");
		}

		Data responseData 	= response.getData();

		System.out.println("------------- responseData : "+responseData.toString());

		return paymentResponse.convertFromMap(responseData.toMap());
	}

	@Override
	public String echoTest(String tx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReversalResponse reverse(ReversalRequest request) {
		ReversalResponse reversalResponse = new ReversalResponse();

		String mappingCode	= "REV_BBRISYA";
		Data requestData 	= new Data(request.toMap());

		if ("DUMMY123".equals(mappingCode)) {
			return createDummyReversalResponse(request);
		}

		String assoc = nextUniqueAssoc();
		WsMessage message = new WsMessage(mappingCode, requestData);
		message.setAssocKey(assoc);
		
		CountDownLatch latch = new CountDownLatch(1);
		latches.put(assoc, latch);
		tellListeners(message);

		try {
			latch.await(120, TimeUnit.SECONDS);
		} catch (InterruptedException e) {}

		WsMessage response = responses.remove(assoc);
		latches.remove(assoc);

		if (response == null) {
			throw new RuntimeException("Timeout");
		}

		Data responseData 	= response.getData();

		return reversalResponse.convertFromMap(responseData.toMap());
	}

	@Override
	public InquiryResponse inquiry(InquiryRequest request) {

		InquiryResponse inquiryResponse = new InquiryResponse();
		
		String mappingCode	= "INQ_BBRISYA";
		
		Data requestData 	= new Data(request.toMap());

		if ("DUMMY123".equals(mappingCode)) {
			return createDummyInquiryResponse2(request);
		}

		String assoc = nextUniqueAssoc();
		WsMessage message = new WsMessage(mappingCode, requestData);
		message.setAssocKey(assoc);

		System.out.println("------------- inq message : "+message.toString()+" -- assoc : "+assoc);

		CountDownLatch latch = new CountDownLatch(1);
		latches.put(assoc, latch);
		tellListeners(message);

		try {
			latch.await(120, TimeUnit.SECONDS);
		} catch (InterruptedException e) {}

		System.out.println("------------- inq responses size : "+responses.size()+" -- assoc : "+assoc);
		System.out.println("------------- inq latches size : "+latches.size()+" -- assoc : "+assoc);

		WsMessage response = responses.remove(assoc);
		System.out.println("------------- inq response : "+response);

		latches.remove(assoc);


		if (response == null) {
			throw new RuntimeException("Timeout");
		}

		Data responseData 	= response.getData();

		return inquiryResponse.convertFromMap(responseData.toMap());
	}

}
