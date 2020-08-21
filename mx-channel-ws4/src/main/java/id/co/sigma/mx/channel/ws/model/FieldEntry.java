package id.co.sigma.mx.channel.ws.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FieldEntry {
	private String field;
	private String value;

	public FieldEntry() {}

	public FieldEntry(String field, String value) {
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "[" + field + "=>" + value + "]";
	}
}
