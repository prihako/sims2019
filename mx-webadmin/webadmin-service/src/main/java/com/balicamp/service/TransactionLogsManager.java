package com.balicamp.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.model.mx.InquiryReconcileDto;
import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.model.mx.Transactions;
import com.balicamp.model.user.User;

public interface TransactionLogsManager extends IManager {

	// public List<AnalisaTransactionLogsDto> findTransactions(String txId,
	// String txCode, String channelCode,
	// Date startDate, Date endDate, String rawKey, int first, int max);

	public List<AnalisaTransactionLogsDto> findTransactions(String txId, String txCode, String channelCode, String klienID, String invoiceNo, String endpoint, String responseCode, Date startDate, Date endDate, String rawKey, int first, int max);

	public List<AnalisaTransactionLogsDto> findTransactions(String txId, String transactionType, String txCode, String klienID, String invoiceNo, Date startDate, Date endDate, String rawKey, int first, int max);

	public List<AnalisaTransactionLogsDto> findTransactions(String txId, String transactionType, String txCode, String klienID, String invoiceNo, Date startDate, Date endDate, String rawKey, Transactions transaction);

	// public List<AnalisaTransactionLogsDto> findTransactionsPlusRc(String
	// txId, String transactionType, String txCode,
	// String klienID, String invoiceNo, Date startDate, Date endDate, String
	// rawKey, Transactions transaction,
	// EndpointRcs endRc);

	public List<AnalisaTransactionLogsDto> findTransactionsPlusRc(String billerCode, String txCode, String klienID, String invoiceNo, Date startDate, Date endDate, EndpointRcs endRc, String channelCode, String respCodeChannel);

	public List<AnalisaTransactionLogsDto> findTransactionsPlusRcWithMap(String billerCode, String txCode, String klienID, Set<String> invoiceNo, Date startDate, Date endDate, EndpointRcs endRc, String channelCode, String respCodeChannel);

	public List<AnalisaTransactionLogsDto> findTransactionsOrderByTimeAndInvoiceNo(String billerCode, String txCode, String klienID, Set<String> invoiceNo, Date startDate, Date endDate, EndpointRcs endRc, String channelCode, String respCodeChannel);

	public Object findTransactionsByTransactionID(String txId);

	List<Object> findByTransaction(String klienID, String invoiceNo, String transactionType, String endpoint, String responseCode, Date startDate, Date endDate);

	public Long getRowCount(String txId, String txCode, String channelCode, Date startDate, Date endDate, String rawKey);

	List<String> findMessageLogsByTxId(String txId);

	public String exportToWord(List<AnalisaTransactionLogsDto> lines, String pathFromServlet, User user);

	public Map<String, Long> getChartTotalTransaction(Date startDate, Date endDate);

	public Map<String, Long> getTotalTransactionForDashboard(Date toDate);

	public List<AnalisaTransactionLogsDto> getLastTransactionsLog(Date toTimeMilis, int i);

	public boolean findTransactionLogByInvoiceNo(String invoiceNo, String clientId);

	public String findParamValueByParamName();

	public String findParamValueByParamName(String param);

	public List<InquiryReconcileDto> findTransactionLogsWebadmin(String channel, Set<String> invoiceNo, String clientId, String transactionCode, String reconcileStatus, String successChannelStatus, Map<String, Integer> mapCount);

	public List<InquiryReconcileDto> findTransactionLogsWebadminWithMap(String channel, Set<String> invoiceNo, String clientId, Set<String> transactionCode, String reconcileStatus, String successChannelStatus, Map<String, Integer> mapCount);

	// reconcile MT940 by request

	public List<ReconcileDto> findTransactionLogsWebadmin(HashMap<String, Object[]> mt940Map, HashMap<String, Object[]> mxData, String channel, Set<String> invoiceNo, String clientId, String transactionCode, Date trxDate, String reconcileStatus, Map<String, Integer> mapCount, Map<String, Long> mapCountAmount);

	public void insertDummyDataWebadminReconcile(String trxCode, String channelCode, String billerCode, String invoiceNo, String reconFlag, Date transactionTime);

	public boolean updateReconFlag(String invoiceNo, String reconFlag, String channelRc, String billerRc, String transactionCode);
	
	public Map<String, Object[]> findAllTransactionLogsWebadminReconcileByDate(
			String channel, String transactionCode, String[] billerRc, String[] channelRc, Date trxDate);

}
