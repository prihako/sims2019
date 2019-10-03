package com.balicamp.service.impl.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.balicamp.model.common.MethodResponse;
import com.balicamp.service.common.TaskExecutorWrapper;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
@Service("taskExecutorWrapper")
public class TaskExecutorWrapperImpl implements TaskExecutorWrapper {
	protected final Log log = LogFactory.getLog(TaskExecutorWrapperImpl.class);

	protected ThreadPoolTaskExecutor taskExecutor;
	protected Object taskExecutorSync = new Object();

	/**
	 * {@inheritDoc}
	 */
	public MethodResponse executeTask(Runnable taskRunnable) {
		MethodResponse methodResponse = new MethodResponse();
		synchronized (taskExecutorSync) {
			if (!isTaskExecutorEmpty()) {
				taskExecutor.execute(taskRunnable);
				methodResponse.setStatus(true);
				methodResponse.setMessageKey("sharedTaskExecutor.empty");
			} else {
				methodResponse.setStatus(true);
				methodResponse.setMessageKey("sharedTaskExecutor.available");
			}
		}
		return methodResponse;
	}

	/**
	 * cek if pool empty
	 * 
	 * @return
	 */
	private boolean isTaskExecutorEmpty() {
		boolean status = false;

		if ((this.taskExecutor.getCorePoolSize() - this.taskExecutor
				.getActiveCount()) < 1) {
			status = true;
		}

		return status;
	}

	// FIXME : @Autowired belum ketauan dimana dipake nya..
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	// setter
	// @Autowired
	/*
	 * public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
	 * this.taskExecutor = taskExecutor; }
	 */

}
