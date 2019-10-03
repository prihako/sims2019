package com.balicamp.dao.hibernate.mx;

import java.util.Date;
import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mx.ReconcileReport;
import com.balicamp.model.mx.ReconcileReportWeekly;
import com.balicamp.model.mx.TransactionReport;

public interface ReconcileReportWeeklyDAO extends AdminGenericDao<ReconcileReportWeekly, Long>{

	List<ReconcileReportWeekly> findByDate (ReconcileReportWeekly mxReport, Date startDate, Date endDate);
	
	List<ReconcileReportWeekly> findReconcileReportWeeklyByDateAndReportType(String bankName,
			String transactionType, Date startDate, Date endDate, String reportType);

}
