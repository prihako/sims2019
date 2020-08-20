package id.co.sigma.mx.chanel.ws;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import id.co.sigma.mx.channel.bri.syariah.ws.WsCodec;
import id.co.sigma.mx.channel.bri.syariah.ws.WsMessage;

public class WsCodecTestCase {
	private static WsCodec codec;

	@BeforeClass
	public static void beforeClass() {
		 codec = new WsCodec();
	}

	@Test
	public void shouldDecodeEncodedMessage() {
		// Given a encoded message
		WsMessage m = new WsMessage();
		m.setValue("name", "Ichsan");
		m.setValue("netnick", "sancho21");

		String res = codec.encode(m);

		// When we decode
		WsMessage back = codec.decode(res);

		// Then, we should get back all values previously set
		assertEquals("Ichsan", back.getValue("name"));
		assertEquals("sancho21", back.getValue("netnick"));
	}
}
