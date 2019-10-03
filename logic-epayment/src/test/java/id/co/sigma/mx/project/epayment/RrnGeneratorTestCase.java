package id.co.sigma.mx.project.epayment;

import static org.junit.Assert.assertEquals;
import id.co.sigma.mx.message.ExternalMessage;
import id.co.sigma.mx.message.ExternalMessageAdapter;
import id.co.sigma.mx.message.InternalMessage;

import org.junit.BeforeClass;
import org.junit.Test;

public class RrnGeneratorTestCase {

	private static RrnGenerator helper;

	@BeforeClass
	public static void beforeClass() {
		helper 	= new RrnGenerator();
//		ChannelHelper.PROPS_LOCATION = UtilDao.PROPS_LOCATION = "src/test/file";
	}

	@Test
	public void testGenerate37() {
		// Given: We have external message with RC 00
		ExternalMessage eMsg = new ExternalMessageAdapter();
		InternalMessage iMsg = InternalMessage.getInstance();
		iMsg.setValue("/info/transactionCode/text()","BILL.PAY");
		iMsg.setValue("/custom/invoiceNumber/text()","1234567");
		iMsg.setValue("/custom/transactionID/text()","3260544000");
		iMsg.setValue("/data/transactionDateTime/text()","0731145200");

		// When: We search it
		String rcDesc = helper.generate(iMsg, eMsg);

		// Then: We'll have correct internal message
		assertEquals("326054400012", rcDesc);
	}

	public static void main(String[] args) {

		String transactionID	= "3260544000";
		String transactionDate	= "0731145200";
		String rrn				= "";

		rrn					= transactionID+transactionDate;
		System.out.println(rrn);
		String rrn1			= rrn.substring(0, 6);
		String rrn2			= rrn.substring(6, 18);

		System.out.println(rrn1+" --- "+rrn2);
	}

}
