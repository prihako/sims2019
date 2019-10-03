package com.balicamp.service;

import java.util.Date;
import java.util.List;

import com.balicamp.model.mx.ReconcileReportWeekly;

public interface ReconcileReportWeeklyManager extends IManager {

	List<ReconcileReportWeekly> findByDate(ReconcileReportWeekly mxReport, Date startDate, Date endDate);

	List<ReconcileReportWeekly> findReconcileReportWeeklyByDateAndReportType(String bankName, String transactionType, Date startDate, Date endDate, String reportType);

}
