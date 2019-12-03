package com.balicamp.webapp.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.balicamp.soap.ws.channel.ReorChannel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

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