package id.co.sigma.mx.channel.message;

import id.co.sigma.mx.channel.ws.ArrayOfString;
import id.co.sigma.mx.channel.ws.InquiryRequest;
import id.co.sigma.mx.channel.ws.InquiryResult;
import id.co.sigma.mx.channel.ws.PaymentManagerControllerPortType;
import id.co.sigma.mx.channel.ws.PaymentRequest;
import id.co.sigma.mx.channel.ws.PaymentResult;
import id.co.sigma.mx.channel.ws.ReversalRequest;
import id.co.sigma.mx.channel.ws.ReversalResult;
import id.co.sigma.mx.channel.ws.StringArray;
import id.co.sigma.mx.message.ExternalMessageFactory;
import id.co.sigma.mx.message.SimpleExternalMessageFactory;
import id.co.sigma.mx.transport.MessageListener;
import id.co.sigma.mx.transport.Transport;
import id.co.sigma.mx.transport.TransportNotReadyException;
import id.co.sigma.mx.transport.commons.monitoring.EndpointStatusManager;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author Rudi Sadria Kirbrandiman - edited: Hendy H. Yusprasetya
 *
 */
public class WsService implements Transport<WsMessage>,InitializingBean,DisposableBean {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(WsService.class);
	private List<MessageListener<WsMessage>> listeners;
	private final ExternalMessageFactory<WsMessage> externalMessageFactory;
	private EndpointStatusManager endpointStatusManager;
	private String endpointCode;
	private PaymentManagerControllerPortType paymentManagerService;
	private String httpUrlREORInquiry;

	public void setHttpUrlREORInquiry(String httpUrlREORInquiry) {
		this.httpUrlREORInquiry = httpUrlREORInquiry;
	}

	public void setReorService(PaymentManagerControllerPortType paymentManagerService) {
		this.paymentManagerService = paymentManagerService;
	}
	
	public void setListeners(MessageListener<WsMessage> listener){
		listeners.add(listener);
	}

	public WsService() {
		listeners = new ArrayList<MessageListener<WsMessage>>();
		externalMessageFactory = new SimpleExternalMessageFactory<WsMessage>(WsMessage.class);
	}

	@Override
	public void write(WsMessage message) {
		
		if(!endpointStatusManager.readStatus(endpointCode).equals("ready"))
		{
			throw new TransportNotReadyException(
					"Socket connection is not established.");
		}

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("###Receiving message from MX###");
		}
		
		if(message.getValue("transactionType").equalsIgnoreCase("REOR.INQ")){
			InquiryRequest inquiryRequest = new InquiryRequest();
			
			inquiryRequest.setTransmissionDateTime(message.getValue("transmissionDateTime"));
			inquiryRequest.setCompanyCode(message.getValue("companyCode"));
			inquiryRequest.setChannelID(message.getValue("channelID"));
			inquiryRequest.setBillKey1(message.getValue("billKey1"));
			inquiryRequest.setBillKey2(message.getValue("billKey2"));
			inquiryRequest.setLanguage(message.getValue("language"));
			inquiryRequest.setTrxDateTime(message.getValue("transactionDateTime"));
			
			LOGGER.trace("+++Sent message to REOR+++");
			
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.setServiceClass(PaymentManagerControllerPortType.class);
			factory.setAddress(httpUrlREORInquiry);
			PaymentManagerControllerPortType client = (PaymentManagerControllerPortType) factory.create();
			InquiryResult response = client.inquiry(inquiryRequest);
			
			LOGGER.trace("+++Receive response from  REOR : " + response);
			
			message.setValue("errorCode", response.getStatus().getErrorCode());
			message.setValue("billInfo2", response.getBillInfo2());
			if( (response.getBillDetails() != null) && (response.getBillDetails().getBillDetails() != null) && 
					(response.getBillDetails().getBillDetails().getItem() != null) && (response.getBillDetails().getBillDetails().getItem().size() > 0)){
				message.setValue("billAmount", response.getBillDetails().getBillDetails().getItem().get(0).getBillAmount());
				message.setValue("billShortName", response.getBillDetails().getBillDetails().getItem().get(0).getBillShortName());
				message.setValue("billCode",  response.getBillDetails().getBillDetails().getItem().get(0).getBillCode());
				message.setValue("billName", response.getBillDetails().getBillDetails().getItem().get(0).getBillName());
			}
			message.setValue("billInfo1", response.getBillInfo1());
			message.setValue("responseCode", response.getStatus().getErrorCode());
			message.setValue("billInfo3", response.getBillInfo3());
			
		}else if(message.getValue("transactionType").equalsIgnoreCase("REOR.PAY")){
			
			StringArray stringArray = new StringArray();
			ArrayOfString arrayOfString = new ArrayOfString();
			stringArray.getItem().add(message.getValue("paidBills"));
			arrayOfString.setStrings(stringArray);
			
			PaymentRequest paymentRequest = new PaymentRequest();
			
			paymentRequest.setTransmissionDateTime(message.getValue("transmissionDateTime"));
			paymentRequest.setCompanyCode(message.getValue("companyCode"));
			paymentRequest.setChannelID(message.getValue("channelID"));
			paymentRequest.setBillKey1(message.getValue("billKey1"));
			paymentRequest.setBillKey2(message.getValue("billKey2"));
			paymentRequest.setLanguage(message.getValue("language"));
			paymentRequest.setTrxDateTime(message.getValue("transactionDateTime"));
			paymentRequest.setPaidBills(arrayOfString);
			paymentRequest.setPaymentAmount(message.getValue("paymentAmount"));
			paymentRequest.setCurrency(message.getValue("currency"));
			paymentRequest.setTransactionID(message.getValue("transactionID"));
			
			LOGGER.trace("+++Sent message payment to REOR+++");
			
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.setServiceClass(PaymentManagerControllerPortType.class);
			factory.setAddress(httpUrlREORInquiry);
			PaymentManagerControllerPortType client = (PaymentManagerControllerPortType) factory.create();
			PaymentResult response = client.payment(paymentRequest);
			
			LOGGER.trace("+++Receive response payment from  REOR : " + response);

			message.setValue("errorCode", response.getStatus().getErrorCode());
			message.setValue("responseCode", response.getStatus().getErrorCode());
		}else if(message.getValue("transactionType").equalsIgnoreCase("REOR.REV")){
			StringArray stringArray = new StringArray();
			ArrayOfString arrayOfString = new ArrayOfString();
			stringArray.getItem().add(message.getValue("paidBills"));
			arrayOfString.setStrings(stringArray);
			
			ReversalRequest reversalRequest = new ReversalRequest();
			
			reversalRequest.setTransmissionDateTime(message.getValue("transmissionDateTime"));
			reversalRequest.setCompanyCode(message.getValue("companyCode"));
			reversalRequest.setChannelID(message.getValue("channelID"));
			reversalRequest.setBillKey1(message.getValue("billKey1"));
			reversalRequest.setBillKey2(message.getValue("billKey2"));
			reversalRequest.setLanguage(message.getValue("language"));
			reversalRequest.setTrxDateTime(message.getValue("trxDateTime"));
			reversalRequest.setPaymentAmount(message.getValue("paymentAmount"));
			reversalRequest.setCurrency(message.getValue("currency"));
			reversalRequest.setTransactionID(message.getValue("transactionID"));
			reversalRequest.setOrigTransmissionDateTime(message.getValue("origTransmissionDateTime"));
			reversalRequest.setOrigTrxDateTime("origTrxDateTime");
			
			LOGGER.trace("+++Sent request Reversal to  REOR+++");
			
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.setServiceClass(PaymentManagerControllerPortType.class);
			factory.setAddress(httpUrlREORInquiry);
			PaymentManagerControllerPortType client = (PaymentManagerControllerPortType) factory.create();
			ReversalResult response = client.reversal(reversalRequest);
			
			LOGGER.trace("+++Receive response reversal from  REOR : " + response);

			message.setValue("errorCode", response.getStatus().getErrorCode());
			message.setValue("responseCode", response.getStatus().getErrorCode());
		}
		
		LOGGER.trace("###Message: " + message);
		message.setResponse();
		tellListeners(message);
	}

	/**
	 * Telling listeners that an external message is coming into MX
	 * @param response
	 */
	private void tellListeners(WsMessage response) {
		for (MessageListener<WsMessage> listener : listeners) {
			if(LOGGER.isTraceEnabled())
				LOGGER.trace("Notify MX for response message from REOR");
			LOGGER.trace("###Response Message: "+response);
			listener.messageReceived(response);
		}
	}

	@Override
	public void addMessageListener(MessageListener<WsMessage> listener) {
		listeners.add(listener);
	}

	@Override
	public void destroy() throws Exception {
		if(LOGGER.isInfoEnabled())
			LOGGER.info("Shutting Down REOR Endpoint");
		this.endpointStatusManager.updateStatus(endpointCode, "disconnected");
		if(LOGGER.isInfoEnabled())
			LOGGER.info("REOR Endpoint successfully stopped");
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		if(LOGGER.isInfoEnabled())
			LOGGER.info("Starting Up REOR Endpoint");
		this.endpointStatusManager.updateStatus(endpointCode, "ready");
		if(LOGGER.isInfoEnabled())
			LOGGER.info("REOR Endpoint successfully started");
	}

	public void setEndpointCode(String endpointCode) {
		this.endpointCode = endpointCode;
	}

	public void setEndpointStatusManager(EndpointStatusManager endpointStatusManager) {
		this.endpointStatusManager = endpointStatusManager;
	}

	public void setMessageListeners(List<MessageListener<WsMessage>> listeners) {
		this.listeners = listeners;
	}

	private String ifNullPutValue(Object o)
	{

		if (o == null)
		{
			return "";
		}
		else
		{
			return ((String) o);
		}
	}

	private int ifNullPutZero(Object o)
	{
		if(o==null)
		{
			return 0;
		}
		else if (o instanceof String)
		{
			return Integer.parseInt((String)o);
		}
		else
			return (Integer)o;
	}

//	public static void main (String args[])
//	{
//		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
//		        new String[] {"ws-context.xml"});
//		PaymentManagerControllerService client = (PaymentManagerControllerService) appContext.getBean("reor01");
//		InputLogin inputLogin = new InputLogin();
//		inputLogin.setDesc("tes");
//		inputLogin.setPhoneNo("+6289010000002");
//		inputLogin.setSessionId("20201");
//		inputLogin.setUserName("");
//		inputLogin.setPassword("");
//		OutputLogin output = client.login(inputLogin);
//		System.out.println(output.toString());

//		InputTransaction inputTransaction = new InputTransaction();
//		inputTransaction.setDescription("TRF ALTO 4262541060085592");
//		inputTransaction.setDest1Acc("+6281000111001");
//		inputTransaction.setDest1Amount(50000);
//		inputTransaction.setDest2Acc("");
//		inputTransaction.setDest2Amount(0);
//		inputTransaction.setNotiDesc("");
//		inputTransaction.setNotiPhone("+6281000111001");
//		inputTransaction.setPhoneNo("+6289010000002");
//		inputTransaction.setSessionId("3306");
//		inputTransaction.setSource1Acc("+6289010000002");
//		inputTransaction.setSource2Acc("");
//		inputTransaction.setSource1Amount(50000);
//		inputTransaction.setSource2Amount(0);
//		inputTransaction.setTransactionType("CASHIN");
//		inputTransaction.setTraxId("3306");
//
//		
//	}
}
