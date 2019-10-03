package id.co.sigma.mx.channel.message;

import id.co.sigma.mx.message.ExternalMessageFactory;
import id.co.sigma.mx.message.SimpleExternalMessageFactory;
import id.co.sigma.mx.message.common.CommonCodec;
import id.co.sigma.mx.util.PropertiesUtil;

import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WsCodec extends CommonCodec<WsMessage> {
	private final ExternalMessageFactory<WsMessage> externalMessageFactory;
	private static final transient Logger LOGGER = LoggerFactory.getLogger(WsCodec.class);

	public WsCodec() {
		externalMessageFactory = new SimpleExternalMessageFactory<WsMessage>(WsMessage.class);
	}

	@Override
	public String encode(WsMessage unpackedMessage) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Encoding packed message:"+unpackedMessage);
		}
		Properties p = new Properties();

		for(String key : unpackedMessage.getValueHolder().keySet())
		{
			p.put(key, unpackedMessage.getValue(key));
		}
		return PropertiesUtil.toString(p);
	}

	@Override
	public WsMessage decode(String packedMessage) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Decoding packed message:"+packedMessage);
		}
		WsMessage m = new WsMessage();

		for (Entry<Object, Object> e : PropertiesUtil.fromString(packedMessage).entrySet()) {
			m.setValue(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
		}
		return m;
	}

	@Override
	protected String extractPackagerCode(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String readPackagerCode(WsMessage message) {
		return null;
	}
}