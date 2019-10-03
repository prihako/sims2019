package id.co.sigma.mx.project.epayment;

import static id.co.sigma.mx.util.StringHelper.pad;
import id.co.sigma.mx.message.ExternalMessage;
import id.co.sigma.mx.message.InternalMessage;
import id.co.sigma.mx.stan.StanGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

//import org.apache.commons.lang.time.FastDateFormat;

/**
 * @author GloryElishua
 */
public class RrnGenerator {

//	private static final Format formatter = FastDateFormat.getInstance("yyMMdd");
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyMMdd");
	private static AtomicLong counter 				= new AtomicLong();
	private static final String PAYMENT 			= new String("BILL.PAY");

	public StanGenerator stanGenerator;

	public void setStanGenerator(StanGenerator stanGenerator) {
		this.stanGenerator = stanGenerator;
	}

	public String generate(InternalMessage iMsg, ExternalMessage eMsg) {
		String transactionCode	= iMsg.getValue("/info/transactionCode/text()");

		String invoiceID		= iMsg.getValue("/custom/invoiceNumber/text()");
		String transactionID	= iMsg.getValue("/custom/transactionID/text()");
		String transactionDate	= iMsg.getValue("/data/transactionDateTime/text()");
		String transmissionDate	= iMsg.getValue("/data/transmissionDateTime/text()");

		StringBuilder rrnLong	= new StringBuilder();
		String rrn				= new String();

		if(transactionCode.equals(PAYMENT)){
			if(transactionID.length()>12){
				rrnLong.append(transactionID.substring(transactionID.length()-12, transactionID.length()));
			}else{
				rrnLong.append(transactionID);
				rrnLong.append(invoiceID);
				rrnLong.append(transactionDate);
				rrnLong.append(transmissionDate);
			}
			rrn	= rrnLong.toString().substring(0,12);
		}else{
			rrn = FORMATTER.format(new Date()) + pad(counter.getAndIncrement(), 6, false, '0');
		}
		return rrn;
	}

	public String generateStan(InternalMessage iMsg, ExternalMessage eMsg) {
		String endpointCode		= iMsg.getValue("/info/channelCode/text()");
		String transactionCode	= iMsg.getValue("/info/transactionCode/text()");

		String invoiceID		= iMsg.getValue("/custom/invoiceNumber/text()");
		String transactionID	= iMsg.getValue("/custom/transactionID/text()");
		String transactionDate	= iMsg.getValue("/data/transactionDateTime/text()");
		String transmissionDate	= iMsg.getValue("/data/transmissionDateTime/text()");

		StringBuilder stanLong	= new StringBuilder();
		String stan				= new String();

		if(transactionCode.equals(PAYMENT)){
			if(transactionID.length()>6){
				stanLong.append(transactionID.substring(transactionID.length()-6, transactionID.length()));
			}else{
				stanLong.append(transactionID);
				stanLong.append(invoiceID);
				stanLong.append(transactionDate);
				stanLong.append(transmissionDate);
			}
			stan	= stanLong.toString().substring(0, 6);
		}else{
			stan	= String.valueOf(stanGenerator.generateStan(endpointCode));
		}
		return stan;
	}

	public void saveStanRrn(InternalMessage iMsg, ExternalMessage eMsg) {
		String rrn 	= FORMATTER.format(new Date()) + pad(counter.getAndIncrement(), 6, false, '0');
		iMsg.setValue("/data/retrievalReferenceNumber/text()", rrn);

		String endpointCode	= iMsg.getValue("/info/channelCode/text()");
		String stan			= String.valueOf(stanGenerator.generateStan(endpointCode));
		iMsg.setValue("/data/systemTraceAuditNumber/text()", stan);
	}
}
