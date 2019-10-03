package com.balicamp.service;

import java.util.Date;
import java.util.List;

import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.model.mx.TransactionLog;
import com.balicamp.model.mx.TransactionLogs;

public interface TransactionLogManager extends IManager {

	List<TransactionLogs> findByClientID(String clientID);

	List<TransactionLogs> findByInvoiceNo(String invoiceNo);

	List<TransactionLogs> findByCriteriaTransaction(TransactionLog transactionLog, Date startDate, Date endDate);

	List<AnalisaTransactionLogsDto> findByTransaction(String klienID, String invoiceNo, String transactionType,
			String endpoint, String responseCode, Date startDate, Date endDate);
}
