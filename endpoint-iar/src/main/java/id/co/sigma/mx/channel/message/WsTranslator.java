package id.co.sigma.mx.channel.message;

import id.co.sigma.mx.message.BaseMessageTranslator;
import id.co.sigma.mx.message.InternalMessage;
import id.co.sigma.mx.message.SimpleExternalMessageFactory;
import id.co.sigma.mx.stan.StanGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translator for Web Service channel
 * @author <a href="mailto:muhammad.ichsan@sigma.co.id">Muhammad Ichsan</a>
 * TODO: need integration test
 */
public class WsTranslator extends BaseMessageTranslator<WsMessage> {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(WsTranslator.class);
	private StanGenerator stanGenerator;

	public void setStanGenerator(StanGenerator stanGenerator) {
		this.stanGenerator = stanGenerator;
	}

	public WsTranslator() {
		setExternalMessageFactory(SimpleExternalMessageFactory.newInstance(WsMessage.class));
	}

	/**
	 * Translate request message from channel into MX
	 */
	@Override
	public InternalMessage from(WsMessage from, String endpoint) {
		InternalMessage imsg = super.from(from, endpoint);

		if(from.getValue("sessionId")!=null)
			imsg.setValue("/custom/sessionId/text()", from.getValue("sessionId"));

		return imsg;
	}

	/**
	 * Translate MX message into response message for channel
	 */
	@Override
	public WsMessage to(InternalMessage from, String endpoint) {
		WsMessage message = super.to(from, endpoint);
		message.setRequest();

		if(message.getValue("sessionId")==null)
		{
			//String stan = StringHelper.lpad(stanGenerator.generateStan(endpoint), 7, '0') ;
			String stan = ""+stanGenerator.generateStan(endpoint) ;
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("Setting Stan/sessionId:"+stan);
			message.setValue("sessionId", stan);

		}
		message.setAssocKey(message.getValue("sessionId"));
		return message;
	}
}
