package id.co.sigma.mx.chanel.ws.model;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import id.co.sigma.mx.channel.bri.syariah.ws.model.Data;

public class XmlFormatTest {

	@Test
	public void shouldMarshalIntoXml() throws Exception {
		Map<String, String> values = new HashMap<String, String>();
		values.put("customerName", "Dania Wardhani");
		values.put("accNumber", "2323231");

		// Given a data
		Data data = new Data(values);

		// When we marshal
		JAXBContext context = JAXBContext.newInstance(Data.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(data, stringWriter);
		stringWriter.close();

		System.out.println(stringWriter);
	}
}
