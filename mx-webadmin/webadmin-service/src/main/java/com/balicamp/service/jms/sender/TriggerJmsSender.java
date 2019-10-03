package com.balicamp.service.jms.sender;

import com.balicamp.model.jms.trigger.TriggerJms;
import com.balicamp.model.jms.trigger.TriggerJmsHolder;

/**
 * TriggerJms sender 
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public interface TriggerJmsSender {
	/**
	 * send TriggerJms without wait response
	 * @param triggerJms
	 */
	public void sendMessage(final TriggerJms triggerJms);

	/**
	 * send TriggerJms and register to TriggerJmsRegistrationManager
	 * @param triggerJms
	 */
	public TriggerJmsHolder sendMessageAndRegister(final TriggerJms triggerJms, long timeout);

	/**
	 * send TriggerJms and wait response 
	 * @param triggerJms
	 * @return JmsTrigger reponse
	 */
	public TriggerJms sendMessageAndWaitResponse(final TriggerJms triggerJms, long timeout);
}
