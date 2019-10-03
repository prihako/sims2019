package com.balicamp.service.impl.jms.registration;

import static com.balicamp.Constants.SystemParameter.TimeOut.GROUP;
import static com.balicamp.Constants.SystemParameter.TimeOut.REGISTRATION_TRIGGER_JMS_MONITORING_PERIODE;
import static com.balicamp.Constants.SystemParameter.TimeOut.REGISTRATION_TRIGGER_JMS_PERIODE;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.balicamp.exception.ApplicationException;
import com.balicamp.model.jms.trigger.TriggerJms;
import com.balicamp.model.jms.trigger.TriggerJmsHolder;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.service.jms.registration.TriggerJmsRegistrationManager;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.LogUtil;

/**
 * timeout registration manager for JMSTrigger (request from web to scheduller)
 * 
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class TriggerJmsRegistrationManagerImpl implements TriggerJmsRegistrationManager, InitializingBean {
	protected final Log log = LogFactory.getLog(TriggerJmsRegistrationManager.class);

	private SystemParameterManager systemParameterManager;

	private Map<String, TriggerJmsHolder> registeredMessageMap;
	private Object regKey = new Object();
	private Long timeOutMonitoringPeriod = 500l;
	private Long timeOutPeriode = 45000l;
	private Thread timeoutMonitorThread;

	/**
	 * for generate jms trigger id
	 */
	private Long counterId;

	public void afterPropertiesSet() throws Exception {
		// read from SystemParameter
		timeOutMonitoringPeriod = systemParameterManager.getLongValue(new SystemParameterId(GROUP,
				REGISTRATION_TRIGGER_JMS_MONITORING_PERIODE), timeOutMonitoringPeriod

		);
		timeOutPeriode = systemParameterManager.getLongValue(new SystemParameterId(GROUP,
				REGISTRATION_TRIGGER_JMS_PERIODE), timeOutPeriode

		);

		// initialized Map
		registeredMessageMap = new ConcurrentHashMap<String, TriggerJmsHolder>();

		// init counter
		counterId = 0l;

		// starting timeout monitoring thread
		startTimeoutMonitor();
	}

	public void startTimeoutMonitor() {
		if (timeoutMonitorThread != null && timeoutMonitorThread.isAlive()) {
			throw new ApplicationException("Fail start timeout monitor, thread already running");
		}
		timeoutMonitorThread = new Thread(new TimeoutMonitor());
		timeoutMonitorThread.setName("timeoutMonitorThread");
		timeoutMonitorThread.start();
	}

	public String register(TriggerJmsHolder triggerJmsHolder) {
		String id = null;
		synchronized (regKey) {
			id = "#" + ++counterId;
		}
		triggerJmsHolder.getRequest().setId(id);
		registeredMessageMap.put(id, triggerJmsHolder);
		return id;
	}

	public TriggerJmsHolder unRegister(String id) {
		TriggerJmsHolder value = null;
		if (registeredMessageMap.containsKey(id)) {
			value = registeredMessageMap.get(id);
			registeredMessageMap.remove(id);
		}
		// else {
		// throw new ApplicationException("Cannot find id : " + id +
		// " in current message map probably already timeout ");
		// }
		return value;
	}

	public TriggerJmsHolder updateResponse(String id, TriggerJms response) {
		TriggerJmsHolder value = null;
		if (registeredMessageMap.containsKey(id)) {
			value = registeredMessageMap.get(id);
			value.setResponse(response);
		}
		return value;
	}

	class TimeoutMonitor implements Runnable {
		public TimeoutMonitor() {
		}

		public void run() {
			log.info(" monitor message monitoring started ");
			try {
				while (true) {
					examineTimeOut();
					try {
						Thread.sleep(timeOutMonitoringPeriod);
					} catch (Exception e) {// NOPMD
						// ignore
					}
				}
			} catch (Exception e) {
				log.error("Error in monotoring thread ... retry again " + e.getMessage(), e);
			}
		}

		/**
		 * Checks message map, to make sure the map holds many waiting pages is
		 * still inside timeout waiting time. if not, notify the waiting page.
		 */
		private void examineTimeOut() {

			for (Entry<String, TriggerJmsHolder> entry : registeredMessageMap.entrySet()) {
				try {
					cekTimeout(entry.getKey(), entry.getValue());
				} catch (Exception e) {
					log.error(LogUtil.concatMessage("while cek timeout ", entry.getValue()), e);
				}
			}
		}

		/**
		 * Compare is the timestamp over the timeout window time, if yes notify
		 * the waiting thread.
		 * 
		 * @param object
		 */
		private void cekTimeout(String id, TriggerJmsHolder triggerJmsHolder) {

			long realTimeOutPeriode = timeOutPeriode;
			if (triggerJmsHolder.getTimeOutPeriode() > 0)
				realTimeOutPeriode = triggerJmsHolder.getTimeOutPeriode();

			if ((System.currentTimeMillis() - triggerJmsHolder.getTimestamp()) >= realTimeOutPeriode) {
				log.info(LogUtil.concatMessage("Time out ", triggerJmsHolder));

				updateResponse(id, null);

				synchronized (triggerJmsHolder) {
					triggerJmsHolder.notify();
				}

				if (registeredMessageMap.containsKey(id))
					unRegister(id);

			}
		}
	}

	// setter
	public void setSystemParameterManager(SystemParameterManager systemParameterManager) {
		this.systemParameterManager = systemParameterManager;
	}

	// implementation MonitorableManager
	/**
	 * {@inheritDoc}
	 */
	public Map<String, Object> getMonitorableManagerStatus() {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("timeOutMonitoringPeriod = ", timeOutMonitoringPeriod);
		result.put("timeOutPeriode = ", timeOutPeriode);

		boolean timeoutMonitorThreadStatus = false;
		if (timeoutMonitorThread != null && timeoutMonitorThread.isAlive())
			timeoutMonitorThreadStatus = true;

		result.put("timeoutMonitorThreadStatus = ", timeoutMonitorThreadStatus);

		return result;
	}

}
