package id.co.sigma.mx.channel.ws;

import id.co.sigma.mx.channel.ws.model.Data;
import id.co.sigma.mx.message.ExternalMessageAdapter;

import java.util.Map;

/**
 *
 * @author <a href="mailto:muhammad.ichsan@sigma.co.id">Muhammad Ichsan</a>
 *
 */
public class WsMessage extends ExternalMessageAdapter  {

	private static final long serialVersionUID = 1939407552804146258L;

	private String assocKey;
	private final String mappingCode;
	private boolean isRequest = true;

	private final Map<String, String> content;

	/**
	 * Create a request message containing empty data
	 */
	public WsMessage() {
		this(null, new Data());
	}

	/**
	 *
	 * @param mappingCode
	 * @param data
	 */
	public WsMessage(String mappingCode, Data data) {
		this.mappingCode = mappingCode;
		this.content = data.toMap();
	}

	@Override
	public String getAssocKey() {
		return assocKey;
	}

	@Override
	public boolean isRequest() {
		return isRequest;
	}

	@Override
	public String getMappingCode() {
		return mappingCode;
	}

	@Override
	public String getValue(String field) {
		return content.get(field);
	}

	@Override
	public void setValue(String field, String value) {
		content.put(field, value);
	}

	@Override
	public Map<String, String> getValueHolder() {
		return content;
	}

	@Override
	public String getResponseCode() {
		return "OK";
	}

	public void setAssocKey(String assocKey) {
		this.assocKey = assocKey;
	}

	public Data getData() {
		return new Data(content);
	}

	/**
	 * Mark this message as response message
	 */
	public void setResponse() {
		isRequest = false;
	}
}
