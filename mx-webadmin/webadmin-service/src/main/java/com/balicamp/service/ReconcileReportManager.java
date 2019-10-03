package com.balicamp.service;

import java.util.Date;
import java.util.List;

import com.balicamp.model.mx.ReconcileReport;

public interface ReconcileReportManager extends IManager {

	List<ReconcileReport> findByDate(ReconcileReport mxReport, Date startDate, Date endDate);

	List<ReconcileReport> findReconcileReportByDate(String bankName, String transactionType, Date startDate,
			Date endDate);

}
