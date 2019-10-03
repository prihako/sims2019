package com.balicamp.webapp.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class RunReconcileWeeklyJob extends QuartzJobBean {
	private ReconcileWeeklyTask reconcileWeeklyTask;

	public void setReconcileWeeklyTask(ReconcileWeeklyTask reconcileWeeklyTask) {
		this.reconcileWeeklyTask = reconcileWeeklyTask;
	}

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		try {
			reconcileWeeklyTask.reconcileWeekly();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}