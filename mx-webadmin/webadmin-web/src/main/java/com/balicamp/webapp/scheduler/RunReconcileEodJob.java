package com.balicamp.webapp.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class RunReconcileEodJob extends QuartzJobBean {
	private ReconcileEodTask reconcileEodTask;

	public void setReconcileEodTask(ReconcileEodTask reconcileEodTask) {
		this.reconcileEodTask = reconcileEodTask;
	}

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		try {
			reconcileEodTask.reconcileEod();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}