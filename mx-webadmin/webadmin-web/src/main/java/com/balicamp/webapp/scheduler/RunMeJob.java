package com.balicamp.webapp.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import test.Constants;

public class RunMeJob extends QuartzJobBean {
	private InsertIntoTransactionReportTask insertIntoTransactionReportTask;
	private ReconcileEodTask reconcileEodTask;

	public void setReconcileEodTask(ReconcileEodTask reconcileEodTask) {
		this.reconcileEodTask = reconcileEodTask;
	}

	public void setInsertIntoTransactionReportTask(InsertIntoTransactionReportTask insertIntoTransactionReportTask) {
		this.insertIntoTransactionReportTask = insertIntoTransactionReportTask;
	}

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		try {
			insertIntoTransactionReportTask.updateTableTransactionReport(Constants.DirectoryFTPConstants.BHP_DIR,
					Constants.BillerConstants.BHP_CODE);
			insertIntoTransactionReportTask.updateTableTransactionReport(Constants.DirectoryFTPConstants.PERANGKAT_DIR,
					Constants.BillerConstants.PERANGKAT_CODE);
			insertIntoTransactionReportTask.updateTableTransactionReport(Constants.DirectoryFTPConstants.PAP_DIR,
					 Constants.BillerConstants.PAP_CODE);
			// insertIntoTransactionReportTask.updateTableTransactionReport(Constants.DirectoryFTPConstants.SKOR_DIR,
			// Constants.BillerConstants.SKOR_CODE);
			// insertIntoTransactionReportTask.updateTableTransactionReport(Constants.DirectoryFTPConstants.REOR_DIR,
			// Constants.BillerConstants.REOR_CODE);
			// insertIntoTransactionReportTask.updateTableTransactionReport(Constants.DirectoryFTPConstants.IAR_DIR,
			// Constants.BillerConstants.IAR_CODE);
			// insertIntoTransactionReportTask.updateTableTransactionReport(Constants.DirectoryFTPConstants.IKRAP_DIR,
			// Constants.BillerConstants.IKRAP_CODE);
			// insertIntoTransactionReportTask.updateTableTransactionReport(Constants.DirectoryFTPConstants.UNAR_DIR,
			// Constants.BillerConstants.UNAR_CODE;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}