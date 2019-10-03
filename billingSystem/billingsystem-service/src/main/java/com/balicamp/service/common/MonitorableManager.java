package com.balicamp.service.common;

import java.util.Map;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public interface MonitorableManager {
	
	/**
	 * get status
	 * @return
	 */
	Map<String, Object> getMonitorableManagerStatus();
}
