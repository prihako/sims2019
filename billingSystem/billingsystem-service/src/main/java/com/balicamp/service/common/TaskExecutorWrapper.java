package com.balicamp.service.common;

import com.balicamp.model.common.MethodResponse;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public interface TaskExecutorWrapper {

	/**
	 * execute task asyncrhonous
	 * 
	 * @param taskRunnable
	 * @return
	 */
	MethodResponse executeTask(Runnable taskRunnable);
}
