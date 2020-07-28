package com.balicamp.webapp.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.balicamp.soap.ws.channel.ReorChannel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public class RunMissingReconcileEodJob extends QuartzJobBean {
	
	private MissingReconcileEodTask missingReconcileEodTask;

	public void setMissingReconcileEodTask(MissingReconcileEodTask missingReconcileEodTask) {
		this.missingReconcileEodTask = missingReconcileEodTask;
	}

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		try {
			missingReconcileEodTask.reconcileEod();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}