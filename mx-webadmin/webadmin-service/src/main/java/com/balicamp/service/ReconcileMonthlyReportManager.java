package com.balicamp.service;

import java.util.Date;
import java.util.List;

import com.balicamp.model.mx.ReconcileReportMonthly;

public interface ReconcileMonthlyReportManager extends IManager {

	List<ReconcileReportMonthly> findByDate(ReconcileReportMonthly mxReport, Date startDate, Date endDate);

	List<ReconcileReportMonthly> findReconcileReportByDate(String bankName, String transactionType, Date startDate,
			Date endDate, String reportType);

}
