/**
 *
 */
package id.co.sigma.mx.project.epayment;

import id.co.sigma.mx.message.ExternalMessage;
import id.co.sigma.mx.message.InternalMessage;
import id.co.sigma.mx.util.PropertiesUtil;
import id.co.sigma.mx.util.StringHelper;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hendy
 *
 */
public class BillerHelper {

	private static transient final Logger LOGGER 	= LoggerFactory.getLogger(BillerHelper.class);
	static String PROPS_LOCATION 					= "sigma-data/mx";
	static String RC_INVALID_INVOICE 				= "25";
	static String RC_INVALID_CLIENT 				= "26";

	/* Used in:
	 * Mapping.MTC2MX_POS
	 */
	public void parseF48Inquiry(InternalMessage iMsg, ExternalMessage eMsg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inject from F48 to internal message.");
		}

		Properties properties 	= PropertiesUtil.fromFile(PROPS_LOCATION + "/main.properties");

		String billName			= properties.getProperty("full.payment.bill.name");
		String billShortName	= properties.getProperty("full.payment.bill.short.name");

		String f39 = eMsg.getValue("39");
		String f48 = eMsg.getValue("48");

		LOGGER.debug("bit48 received from sims : \"" + f48 + "\"");
		
		String[] f48Parsed = null;

		if(!f39.equals(RC_INVALID_CLIENT) && !f39.equals(RC_INVALID_INVOICE)){
			f48Parsed = StringHelper.parse(new int[] {
							16, 	// 0 - invoice id
							8, 		// 1 - client id
							32,		// 2 - client name
							8, 		// 3 - period begin
							8,		// 4 - period end
							1,      // 5 - Flag partial amount / Full Payment
						},f48);
			
			// flag 3, 4 = partial, selain itu full payment
			String flagPartialOrFull = f48Parsed[5].trim();
			
			LOGGER.debug("Flag Partial Or Full Payment : " + flagPartialOrFull);
			
			if(flagPartialOrFull.equals("3") || flagPartialOrFull.equals("4")){
				billName		= properties.getProperty("partial.payment.bill.name");
				billShortName	= properties.getProperty("partial.payment.bill.short.name");
				
				LOGGER.debug("billName : " + billName);
				LOGGER.debug("billShortName : " + billShortName);
			}

			iMsg.setValue("/custom/clientName/text()", 	f48Parsed[2].trim());
			iMsg.setValue("/custom/periodBegin/text()", f48Parsed[3].trim());
			iMsg.setValue("/custom/periodEnd/text()", 	f48Parsed[4].trim());

		}else{
			f48Parsed = StringHelper.parse(new int[] {
							16, 	// 0 - invoice id
							8		// 1 - client id
						},f48);
		}

		iMsg.setValue("/custom/invoiceNumber/text()", 	f48Parsed[0].trim());
		iMsg.setValue("/custom/clientID/text()", 		f48Parsed[1].trim());

		iMsg.setValue("/custom/billName/text()", billName);
		iMsg.setValue("/custom/billShortName/text()", billShortName);

	}
	/* Used in:
	 * Mapping.MX2MDS_POS
	 */
	public void parseF48Payment(InternalMessage iMsg, ExternalMessage eMsg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inject from F48 to internal message.");
		}

		String f48 			= eMsg.getValue("48");
		String[] f48Parsed 	= StringHelper.parse(new int[] {
				16, 	// 0 - invoice id
				8, 		// 1 - client id
				},f48);

		iMsg.setValue("/custom/invoiceNumber/text()", 	f48Parsed[0].trim());
		iMsg.setValue("/custom/clientID/text()", 		f48Parsed[1].trim());
	}

	public String generateBit90Rev(InternalMessage iMsg, ExternalMessage eMsg) {
		LOGGER.debug("Generate bit 90 for external message.");

		String invoiceID				= iMsg.getValue("/custom/invoiceNumber/text()");
		String transactionID			= iMsg.getValue("/custom/transactionID/text()");
		String originalTransactionDate	= iMsg.getValue("/custom/origLocalTransactionTime/text()");
		String originalTransmissionDate	= iMsg.getValue("/custom/origTransmissionDateTime/text()");

		StringBuilder rrnLong	= new StringBuilder();
		String rrn				= new String();

		StringBuilder stanLong	= new StringBuilder();
		String stan				= new String();

		StringBuilder f90Long	= new StringBuilder();
		String f90	 			= new String();

		if(transactionID.length()>12){
			rrnLong.append(transactionID.substring(transactionID.length()-12, transactionID.length()));
		}else{
			rrnLong.append(transactionID);
			rrnLong.append(invoiceID);
			rrnLong.append(originalTransactionDate);
			rrnLong.append(originalTransmissionDate);
		}
		rrn	= rrnLong.toString().substring(0,12);

		if(transactionID.length()>6){
			stanLong.append(transactionID.substring(transactionID.length()-6, transactionID.length()));
		}else{
			stanLong.append(transactionID);
			stanLong.append(invoiceID);
			stanLong.append(originalTransactionDate);
			stanLong.append(originalTransmissionDate);
		}
		stan	= stanLong.toString().substring(0, 6);

		f90Long.append(stan);
		f90Long.append(rrn);

		f90 = f90Long.toString().substring(0,18);

		if(f90!=null){
			for(int i=f90.length();i<42;i++){
				f90+="0";
			}
		}else{
			for(int i=0;i<42;i++){
				f90+="0";
			}
		}
		return f90;
	}
}
