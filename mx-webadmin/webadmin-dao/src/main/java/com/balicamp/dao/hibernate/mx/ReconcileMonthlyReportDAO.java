package com.balicamp.dao.hibernate.mx;

import java.util.Date;
import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mx.ReconcileReportMonthly;
import com.balicamp.model.mx.ReconcileReport;
import com.balicamp.model.mx.TransactionReport;

public interface ReconcileMonthlyReportDAO extends AdminGenericDao<ReconcileReportMonthly, Long>{

	List<ReconcileReportMonthly> findByDate (ReconcileReportMonthly mxReport, Date startDate, Date endDate);
	
	List<ReconcileReportMonthly> findReconcileReportByDate(String bankName,
			String transactionType, Date startDate, Date endDate, String reportType);

}
