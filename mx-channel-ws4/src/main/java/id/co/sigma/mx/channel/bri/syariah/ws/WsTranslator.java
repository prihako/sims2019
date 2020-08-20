package id.co.sigma.mx.channel.bri.syariah.ws;

import id.co.sigma.mx.message.BaseMessageTranslator;
import id.co.sigma.mx.message.InternalMessage;
import id.co.sigma.mx.message.SimpleExternalMessageFactory;

/**
 * Translator for Web Service channel
 * @author <a href="mailto:muhammad.ichsan@sigma.co.id">Muhammad Ichsan</a>
 * TODO: need integration test
 */
public class WsTranslator extends BaseMessageTranslator<WsMessage> {

	public WsTranslator() {
		setExternalMessageFactory(SimpleExternalMessageFactory.newInstance(WsMessage.class));
	}

	/**
	 * Translate request message from channel into MX
	 */
	@Override
	public InternalMessage from(WsMessage from, String endpoint) {
		InternalMessage imsg = super.from(from, endpoint);

		// Preserve STAN
		imsg.setValue("/custom/channelInternalStan/text()", from.getAssocKey());
		return imsg;
	}

	/**
	 * Translate MX message into response message for channel
	 */
	@Override
	public WsMessage to(InternalMessage from, String endpoint) {
		WsMessage message = super.to(from, endpoint);

		// Sending out always means creating response message
		message.setResponse();

		// Set back STAN
		message.setAssocKey(from.getValue("/custom/channelInternalStan/text()"));

		return message;
	}
}
