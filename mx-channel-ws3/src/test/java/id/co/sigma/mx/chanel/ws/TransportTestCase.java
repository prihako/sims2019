package id.co.sigma.mx.chanel.ws;

import org.junit.Before;

/**
 *
 * @author <a href="mailto:muhammad.ichsan@sigma.co.id">Muhammad Ichsan</a>
 *
 */
public class TransportTestCase {
	//private ChannelImpl transport;

	@Before
	public void before() {
		//ApplicationContext context = new ClassPathXmlApplicationContext("transportTestCase-context.xml");

		//transport = context.getBean(ChannelImpl.class);
	}

	/*@Test
	public void shouldSendFinancialMessage() {
		Map<String, String>	data = new HashMap<String, String>();
		data.put("name", "Sanchez");
		data.put("accNumber", "AccountNumber");

		// When: a service method is called
		Data result = transport.execute("BALANCE-INQ", new Data(data));

		// Then: we should get the result
		assertEquals("success", result.toMap().get("result"));
		assertEquals("Transaction is done by Sanchez", result.toMap().get("info"));
	}*/
	
}
