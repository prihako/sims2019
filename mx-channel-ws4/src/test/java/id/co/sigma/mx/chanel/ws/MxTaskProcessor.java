package id.co.sigma.mx.chanel.ws;



import id.co.sigma.mx.channel.bri.syariah.ws.WsMessage;
import id.co.sigma.mx.transport.MessageListener;
import id.co.sigma.mx.transport.Transport;

/**
 *
 * @author <a href="mailto:muhammad.ichsan@sigma.co.id">Muhammad Ichsan</a>
 *
 */
public class MxTaskProcessor implements MessageListener<WsMessage> {

	private Transport<WsMessage> transport;

	@Override
	public void messageReceived(WsMessage message) {
		message.setValue("result", "success");
		message.setValue("info", "Transaction is done by " + message.getValue("name"));
		transport.write(message); // write response
	}

	public void setTransport(Transport<WsMessage> transport) {
		this.transport = transport;
		transport.addMessageListener(this);
	}
}
