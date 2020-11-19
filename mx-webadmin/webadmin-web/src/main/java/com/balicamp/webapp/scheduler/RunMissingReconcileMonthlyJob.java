package com.balicamp.webapp.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class RunMissingReconcileMonthlyJob extends QuartzJobBean {
	private MissingReconcileMonthlyTask missingReconcileMonthlyTask;

	public void setMissingReconcileMonthlyTask(MissingReconcileMonthlyTask missingReconcileMonthlyTask) {
		this.missingReconcileMonthlyTask = missingReconcileMonthlyTask;
	}

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		try {
			missingReconcileMonthlyTask.reconcileMonthly();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}