package com.balicamp.dao.hibernate.mx;

import java.util.Date;
import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mx.ReconcileReport;
import com.balicamp.model.mx.TransactionReport;

public interface ReconcileReportDAO extends AdminGenericDao<ReconcileReport, Long>{

	List<ReconcileReport> findByDate (ReconcileReport mxReport, Date startDate, Date endDate);
	
	List<ReconcileReport> findReconcileReportByDate(String bankName,
			String transactionType, Date startDate, Date endDate);

}
