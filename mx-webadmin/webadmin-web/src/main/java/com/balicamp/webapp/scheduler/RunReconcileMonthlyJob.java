package com.balicamp.webapp.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class RunReconcileMonthlyJob extends QuartzJobBean {
	private ReconcileMonthlyTask reconcileMonthlyTask;

	public void setReconcileMonthlyTask(ReconcileMonthlyTask reconcileMonthlyTask) {
		this.reconcileMonthlyTask = reconcileMonthlyTask;
	}

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		try {
			reconcileMonthlyTask.reconcileMonthly();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}