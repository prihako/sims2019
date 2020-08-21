package id.co.sigma.mx.channel.ws;

import id.co.sigma.mx.channel.ws.model.FieldEntry;
import id.co.sigma.mx.message.Codec;
import id.co.sigma.mx.util.PropertiesUtil;

import java.util.Map.Entry;
import java.util.Properties;

/**
 * Codec for web service channel
 * @author <a href="mailto:muhammad.ichsan@sigma.co.id">Muhammad Ichsan</a>
 *
 */
public class WsCodec implements Codec<WsMessage> {

	@Override
	public String encode(WsMessage unpackedMessage) {
		Properties p = new Properties();

		for (FieldEntry  e : unpackedMessage.getData().getEntries()) {
			p.put(e.getField(), e.getValue());
		}

		return PropertiesUtil.toString(p);
	}

	@Override
	public WsMessage decode(String packedMessage) {
		WsMessage m = new WsMessage();

		for (Entry<Object, Object> e : PropertiesUtil.fromString(packedMessage).entrySet()) {
			m.setValue(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
		}

		return m;
	}

}
