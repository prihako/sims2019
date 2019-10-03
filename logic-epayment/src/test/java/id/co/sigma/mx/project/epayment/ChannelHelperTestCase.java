package id.co.sigma.mx.project.epayment;

import static org.junit.Assert.assertEquals;
import id.co.sigma.mx.message.ExternalMessage;
import id.co.sigma.mx.message.ExternalMessageAdapter;
import id.co.sigma.mx.message.InternalMessage;

import org.junit.BeforeClass;
import org.junit.Test;

public class ChannelHelperTestCase {

	private static ChannelHelper helper;
	@BeforeClass
	public static void beforeClass() {
		helper 	= new ChannelHelper();
		ChannelHelper.PROPS_LOCATION = UtilDao.PROPS_LOCATION = "src/test/file";
	}

	@Test
	public void testRcDescription_Rc00() {
		// Given: We have external message with RC 00
		ExternalMessage eMsg = new ExternalMessageAdapter();
		InternalMessage iMsg = InternalMessage.getInstance();
		iMsg.setValue("/data/responseCode/text()", "91");
		iMsg.setValue("/info/channelCode/text()", "chws");
		iMsg.setValue("/info/transactionCode/text()", "BILL.INQ");

		// When: We search it
		String rcDesc = helper.rcDescription(iMsg, eMsg);

		// Then: We'll have correct internal message
		assertEquals("WebService: Link down", rcDesc);
	}

}
