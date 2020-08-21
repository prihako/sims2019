package id.co.sigma.mx.channel.ws.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
public class Data {
	private Collection<FieldEntry> entries = new ArrayList<FieldEntry>();

	public Data() {}

	public Data(Map<String, String> content) {
		for (Entry<String, String> entry : content.entrySet()) {
			entries.add(new FieldEntry(entry.getKey(), entry.getValue()));
		}
	}

	@XmlElement(name = "entry")
	public Collection<FieldEntry> getEntries() {
		return entries;
	}

	public void setEntries(Collection<FieldEntry> entries) {
		this.entries = entries;
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		for (FieldEntry entry : getEntries()) {
			map.put(entry.getField(), entry.getValue());
		}
		return map;
	}

	@Override
	public String toString() {
		return "Data [entries=" + entries + "]";
	}
}
