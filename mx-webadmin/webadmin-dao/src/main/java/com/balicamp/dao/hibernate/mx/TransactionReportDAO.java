package com.balicamp.dao.hibernate.mx;

import java.util.Date;
import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mx.TransactionReport;

public interface TransactionReportDAO extends AdminGenericDao<TransactionReport, Long>{

	List<TransactionReport> findByDate (TransactionReport mxReport, Date startDate, Date endDate);
	
	List<TransactionReport> findTransactionReportByDate (String transactionType, Date startDate, Date endDate, String bankName);

}
