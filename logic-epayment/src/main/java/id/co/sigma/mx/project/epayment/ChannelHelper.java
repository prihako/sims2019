/**
 *
 */
package id.co.sigma.mx.project.epayment;

import id.co.sigma.mx.message.ExternalMessage;
import id.co.sigma.mx.message.InternalMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yusprasetya
 *
 */
public class ChannelHelper {

	private static transient final Logger LOGGER 	= LoggerFactory.getLogger(BillerHelper.class);
	static String PROPS_LOCATION 					= "sigma-data/mx";
	static String RC_SUCCESS 						= "00";
	static String BILL_INQ	 						= "BILL.INQ";

	/* Used in:
	 * Mapping.MTC2MX_POS
	 */
	public String rcDescription(InternalMessage iMsg, ExternalMessage eMsg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Insert RC description to internal message.");
		}

//		Properties properties 	= PropertiesUtil.fromFile(PROPS_LOCATION + "/main.properties");
//		String billName			= properties.getProperty("bill.name");
//		String billShortName	= properties.getProperty("bill.short.name");

		String f39 				= iMsg.getValue("/data/responseCode/text()");
		String endpointCode		= iMsg.getValue("/info/channelCode/text()");
		String trxCode			= iMsg.getValue("/info/transactionCode/text()");

		String rcDesc			= UtilDao.defineRcDescription(endpointCode, f39);

		if(trxCode.equals(BILL_INQ) && endpointCode.equals("chws")){
			if(f39.equals("SU") || f39.equals("91")){
				iMsg.setValue("/custom/clientName/text()", "N/A");
				iMsg.setValue("/custom/periodBegin/text()", "00000000");
				iMsg.setValue("/custom/periodEnd/text()", "00000000");
				iMsg.setValue("/data/amountTransaction/text()", "0");
				iMsg.setValue("/custom/billName/text()", "N/A");
				iMsg.setValue("/custom/billShortName/text()", "N/A");
			}
		}
		return rcDesc;

	}
	
	public String selectEndpointInq(InternalMessage iMsg, ExternalMessage eMsg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.trace("Select endpoint based on billKey3 value");
		}

		String trxType	= eMsg.getValue("billKey3");
		LOGGER.trace("Bill key 3: " + trxType);
		String trxCode = null;
		
		if( (trxType.equalsIgnoreCase(TransactionType.billing))){
			trxCode = "BILL.INQ"; 
		}else if(trxType.equalsIgnoreCase(TransactionType.perangkat)){
			trxCode = "PER.INQ"; 
		}else if(trxType.equalsIgnoreCase(TransactionType.skor)){
			trxCode = "SKOR.INQ"; 
		}else if(trxType.equalsIgnoreCase(TransactionType.reor)){
			trxCode = "REOR.INQ";
		}else if(trxType.equalsIgnoreCase(TransactionType.unar)){
			trxCode = "UNAR.INQ";
		}else if(trxType.equalsIgnoreCase(TransactionType.iar)){
			trxCode = "IAR.INQ";
		}else if(trxType.equalsIgnoreCase(TransactionType.ikrap)){
			trxCode = "IKRAP.INQ";
		}else if(trxType.equalsIgnoreCase(TransactionType.pap)){
			trxCode = "PAP.INQ";
		}else if(trxType.equalsIgnoreCase(TransactionType.klbsi)){
			trxCode = "KLBSI.INQ";
		}
		
		//Ini untuk mengirim balik ke endpoint aktif tanpa harus diproses oleh internal MX
//		else{
//			iMsg.setValue("/data/responseCode/text()", "01");
//			iMsg.setValue("/info/result/responseCode/text()", "01");
//			iMsg.setValue("/info/routeCompleted/text()", "true");
//		}
		
		
		LOGGER.trace("Trx Code: " + trxCode);
		return trxCode;
	}
	
	public String selectEndpointPay(InternalMessage iMsg, ExternalMessage eMsg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.trace("Select endpoint based on billKey3 value");
		}

		String trxType	= eMsg.getValue("billKey3");
		LOGGER.trace("Bill key 3: " + trxType);
		String trxCode = null;
		
		if((trxType.equalsIgnoreCase(TransactionType.billing)) ){
			trxCode = "BILL.PAY"; 
		}else if(trxType.equalsIgnoreCase(TransactionType.perangkat)){
			trxCode = "PER.PAY"; 
		}else if(trxType.equalsIgnoreCase(TransactionType.skor)){
			trxCode = "SKOR.PAY"; 
		}else if(trxType.equalsIgnoreCase(TransactionType.reor)){
			trxCode = "REOR.PAY";
		}else if(trxType.equalsIgnoreCase(TransactionType.unar)){
			trxCode = "UNAR.PAY";
		}else if(trxType.equalsIgnoreCase(TransactionType.iar)){
			trxCode = "IAR.PAY";
		}else if(trxType.equalsIgnoreCase(TransactionType.ikrap)){
			trxCode = "IKRAP.PAY";
		}else if(trxType.equalsIgnoreCase(TransactionType.pap)){
			trxCode = "PAP.PAY";
		}else if(trxType.equalsIgnoreCase(TransactionType.klbsi)){
			trxCode = "KLBSI.PAY";
		}
		
		LOGGER.trace("Trx Code: " + trxCode);
		return trxCode;
	}
	
	public String selectEndpointRev(InternalMessage iMsg, ExternalMessage eMsg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.trace("Select endpoint based on billKey3 value");
		}

		String trxType	= eMsg.getValue("billKey3");
		LOGGER.trace("Bill key 3: " + trxType);
		String trxCode = null;
		
		if((trxType.equalsIgnoreCase(TransactionType.billing)) ){
			trxCode = "BILL.REV"; 
		}else if(trxType.equalsIgnoreCase(TransactionType.perangkat)){
			trxCode = "PER.REV"; 
		}else if(trxType.equalsIgnoreCase(TransactionType.skor)){
			trxCode = "SKOR.REV"; 
		}else if(trxType.equalsIgnoreCase(TransactionType.reor)){
			trxCode = "REOR.REV";
		}else if(trxType.equalsIgnoreCase(TransactionType.unar)){
			trxCode = "UNAR.REV";
		}else if(trxType.equalsIgnoreCase(TransactionType.iar)){
			trxCode = "IAR.REV";
		}else if(trxType.equalsIgnoreCase(TransactionType.ikrap)){
			trxCode = "IKRAP.REV";
		}else if(trxType.equalsIgnoreCase(TransactionType.pap)){
			trxCode = "PAP.REV";
		}else if(trxType.equalsIgnoreCase(TransactionType.klbsi)){
			trxCode = "KLBSI.REV";
		}
		
		LOGGER.trace("Trx Code: " + trxCode);
		return trxCode;
	}
	
}
