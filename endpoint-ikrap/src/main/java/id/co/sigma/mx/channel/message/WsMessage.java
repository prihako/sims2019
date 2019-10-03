package id.co.sigma.mx.channel.message;

import id.co.sigma.mx.message.ExternalMessageAdapter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:rudi.sadria@sigma.co.id">Rudi Sadria</a>
 *
 */
public class WsMessage extends ExternalMessageAdapter  {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(WsMessage.class);
	private static final long serialVersionUID = 1939407552804146258L;

	private String assocKey;
	private boolean isRequest;

	/**
	 * Create a request message containing empty data
	 */
	public WsMessage() {

	}


	@Override
	public String getAssocKey() {
		return assocKey;
	}

	@Override
	public String getValue(String field) {
		return valueHolder.get(field);
	}

	@Override
	public void setValue(String field, String value) {
		valueHolder.put(field, value);
	}

	@Override
	public Map<String, String> getValueHolder() {
		return valueHolder;
	}

	@Override
	public String getResponseCode() {
		return this.getValue("responseCode");
	}

	@Override
	public boolean isRequest() {
		return isRequest;
	}
	public void setAssocKey(String assocKey) {
		this.assocKey = assocKey;
	}

	/**
	 * Mark this message as response message
	 */
	public void setResponse() {
		isRequest = false;
	}

	public void setRequest() {
		isRequest = true;
	}

	public void setRequest(boolean isRequest) {
		this.isRequest = isRequest;
	}

	@Override
	public String toString() {
		return "WS Message assocKey="+this.assocKey+" [valueHolder=" + this.valueHolder + "]";
	}
}
