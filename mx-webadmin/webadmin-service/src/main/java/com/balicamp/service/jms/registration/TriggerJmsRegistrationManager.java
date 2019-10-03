package com.balicamp.service.jms.registration;

import com.balicamp.model.jms.trigger.TriggerJms;
import com.balicamp.model.jms.trigger.TriggerJmsHolder;
import com.balicamp.service.common.MonitorableManager;

/**
 * timeout registration manager for JMSTrigger (request from web to scheduller)
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public interface TriggerJmsRegistrationManager extends MonitorableManager {
	/**
	 * register to time out manager
	 * @param triggerJmsHolder
	 * @return
	 */
	public String register(TriggerJmsHolder triggerJmsHolder);

	/**
	 * unregister from RegistrationManager
	 * @param id
	 * @return
	 */
	public TriggerJmsHolder unRegister(String id);

	/**
	 * update response
	 * @param id
	 * @param response
	 * @return
	 */
	public TriggerJmsHolder updateResponse(String id, TriggerJms response);
}