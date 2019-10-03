package com.balicamp.service;

import java.util.Date;
import java.util.List;

import com.balicamp.model.mx.TransactionReport;

public interface TransactionReportManager extends IManager {

	List<TransactionReport> findByDate(TransactionReport mxReport, Date startDate, Date endDate);

	List<TransactionReport> findTransactionReportByDate(String transactionType, Date startDate, Date endDate,
			String bankName);

}
