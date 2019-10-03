package com.balicamp.service.impl.jms.sender;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import com.balicamp.service.impl.jms.MRM;

public class DeliveryChannelRequestSender {
	private static final Log log = LogFactory.getLog(DeliveryChannelRequestSender.class);

	private static final AtomicLong id = new AtomicLong(0);

	private MRM mrm;

	private JmsTemplate jmsTemplate;

	private String originator;

	private long timeToLive;

	private boolean relativeExpiredDate;

	public void setMrm(MRM mrm) {
		this.mrm = mrm;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public void setRelativeExpiredDate(boolean relativeExpiredDate) {
		this.relativeExpiredDate = relativeExpiredDate;
	}

	public void send(final HashMap data, boolean needResponse) throws JmsException {
		send(data, String.valueOf(id.incrementAndGet()), needResponse);
	}

	public void send(final HashMap data, String deliveryChannelId, boolean needResponse) throws JmsException {
		data.put("Originator", originator);
		data.put("DeliveryChannelId", deliveryChannelId);

		if (relativeExpiredDate) {
			data.put("ExpiredDate", "" + timeToLive);
		} else {
			data.put("ExpiredDate", "" + System.currentTimeMillis() + timeToLive);
		}

		if (needResponse && (mrm != null)) {
			// #1. REGISTER
			mrm.register((String) data.get("DeliveryChannelId"), System.currentTimeMillis() + timeToLive);
		}

		try {
			// #2. SEND
			// jmsTemplate.convertAndSend(data);

			// jmsTemplate.send(new MessageCreator() {
			// public Message createMessage(Session session) throws JMSException
			// {
			// return session.createObjectMessage(data);
			// }
			// });
		} catch (JmsException ex) {
			if (needResponse && (mrm != null)) {
				// #3. ROLLBACK
				mrm.unregister((String) data.get("DeliveryChannelId"));
			}

			log.warn("an error occured while sending message to business-engine.", ex);
			throw ex;
		}
	}
}
