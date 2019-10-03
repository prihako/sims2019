package id.co.sigma.mx.project.epayment;

import static org.junit.Assert.assertEquals;
import id.co.sigma.mx.message.ExternalMessage;
import id.co.sigma.mx.message.ExternalMessageAdapter;
import id.co.sigma.mx.message.InternalMessage;

import org.junit.BeforeClass;
import org.junit.Test;

public class BillerHelperTestCase {

	private static BillerHelper helper;

	@BeforeClass
	public static void beforeClass() {
		helper 	= new BillerHelper();
		BillerHelper.PROPS_LOCATION = UtilDao.PROPS_LOCATION = "src/test/file";
	}

	@Test
	public void testParseF48Inquiry_Rc00() {
		// Given: We have external message with RC 00
		ExternalMessage eMsg = new ExternalMessageAdapter();
		eMsg.setValue("39", "00");
//		eMsg.setValue("48", "1234567890      12345   Joko Susilo                     2013010120140101");
		eMsg.setValue("48", "0213789         1644    ROYAL ORIENTAL LTD, PT          2013050620140505                                                ");

		// When: We parse it
		InternalMessage iMsg = InternalMessage.getInstance();
		iMsg.setValue("/info/channelCode/text()", "chws");
		helper.parseF48Inquiry(iMsg, eMsg);

		// Then: We'll have correct internal message
		assertEquals("ROYAL ORIENTAL LTD, PT", iMsg.getValue("/custom/clientName/text()"));
		assertEquals("20130506", iMsg.getValue("/custom/periodBegin/text()"));
		assertEquals("20140505", iMsg.getValue("/custom/periodEnd/text()"));
		assertEquals("1644", iMsg.getValue("/custom/clientID/text()"));
		assertEquals("0213789", iMsg.getValue("/custom/invoiceNumber/text()"));
	}

	@Test
	public void testParseF48Inquiry_RcXX() {
		// Given: We have external message with RC 00
		ExternalMessage eMsg = new ExternalMessageAdapter();
		eMsg.setValue("39", "25"); //please set wanted value
//		eMsg.setValue("48", "1234567890      12345   Joko Susilo                     2013010120140101");
		eMsg.setValue("48", "0213789         1644    ");

		// When: We parse it
		InternalMessage iMsg = InternalMessage.getInstance();
		iMsg.setValue("/info/channelCode/text()", "chws");
		helper.parseF48Inquiry(iMsg, eMsg);

		// Then: We'll have correct internal message
		assertEquals("1644", iMsg.getValue("/custom/clientID/text()"));
		assertEquals("0213789", iMsg.getValue("/custom/invoiceNumber/text()"));
	}

	@Test
	public void testParseF48Payment_Rc00() {
		// Given: We have external message with RC 00
		ExternalMessage eMsg = new ExternalMessageAdapter();
		eMsg.setValue("39", "00");
//		eMsg.setValue("48", "1234567890      12345   Joko Susilo                     2013010120140101");
		eMsg.setValue("48", "0213789         1644    ");

		// When: We parse it
		InternalMessage iMsg = InternalMessage.getInstance();
		iMsg.setValue("/info/channelCode/text()", "bils0");
		helper.parseF48Payment(iMsg, eMsg);

		// Then: We'll have correct internal message
		assertEquals("1644", iMsg.getValue("/custom/clientID/text()"));
		assertEquals("0213789", iMsg.getValue("/custom/invoiceNumber/text()"));
	}

	@Test
	public void testGenerate90() {
		// Given: We have internal message with transaction ID
		ExternalMessage eMsg = new ExternalMessageAdapter();
		InternalMessage iMsg = InternalMessage.getInstance();

		// When: We parse it
		iMsg.setValue("/custom/invoiceNumber/text()", "777777");
		iMsg.setValue("/custom/transactionID/text()", "1234567890");
		iMsg.setValue("/custom/origLocalTransactionTime/text()", "0728210000");
		iMsg.setValue("/custom/origTransmissionDateTime/text()", "0728210000");

		String f90 = helper.generateBit90Rev(iMsg, eMsg);

		// Then: We'll have correct internal message
		assertEquals("567890123456789077000000000000000000000000", f90);
	}
}
