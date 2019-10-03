/**
 *
 */
package com.balicamp.dao.mx;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.balicamp.dao.GenericDao;
import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.model.mx.InquiryReconcileDto;
import com.balicamp.model.mx.TransactionLogs;
import com.balicamp.model.mx.Transactions;
import com.balicamp.model.report.ReportModel;
import com.balicamp.util.wrapper.ReportCriteria;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionLogDao.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
public interface TransactionLogDao extends GenericDao<TransactionLogs, Long> {

	public List<String> findRawData(SearchCriteria searchCriteris);

	public List<?> findRawMessage(String txId);

	public List<?> findTransactions(String txCode, String channelCode,
			String txId, String klienID, String invoiceNo,
			String endpointTransactionType, Date startDate, Date endDate,
			String rawKey, int first, int max);

	public List<?> findTransactions(String txId, String transactionType,
			String txCode, String klienID, String invoiceNo, Date startDate,
			Date endDate, String rawKey, int first, int max);

	public List<?> findTransactions(String txId, String transactionType,
			String txCode, String klienID, String invoiceNo, Date startDate,
			Date endDate, String rawKey, Transactions transaction);

	public List<?> findTransactionsPlusRc(String billerCode,
			String txCode, String klienID, String invoiceNo, Date startDate,
			Date endDate,   EndpointRcs endRc,String channelCode, String respCodeChannel);
	
	public List<?> findTransactionsPlusRcWithMap(String billerCode,
			String txCode, String klienID, Set<String> invoiceNo, Date startDate,
			Date endDate,   EndpointRcs endRc,String channelCode, String respCodeChannel);
	
	public List<?> findTransactionsOrderByTimeAndInvoiceNo(String billerCode,
			String txCode, String klienID, Set<String> invoiceNo, Date startDate,
			Date endDate,   EndpointRcs endRc,String channelCode, String respCodeChannel);

	public Object findTransactionsByTransactionID(String txId);

	List<Object> findByTransaction(String klienID, String invoiceNo,
			String transactionType, String endpoint, String responseCode,
			Date startDate, Date endDate);

	public Long findTransactionCount(String txId, String txCode,
			String channelCode, Date startDate, Date endDate, String rawKey);

	public Long findTransactionCount(String txId, String txCode,
			Date startDate, Date endDate, String rawKey);

	public List<String> findRawData(String txCode, String channelRc,
			Date startDate, Date endDate);

	public List<String> findRawData(List<ReportCriteria> criterias,
			ReportModel model);

	public Long getTotalCountTransaction(Date startDate, Date endDate, String rc);

	public List<Object> findLastTransactionsLog(int i);

	public boolean findTransactionLogByInvoiceNo(String invoiceNo,
			String clientId);
	
	public Map<String, Object[]> findLastTransactionLogsWebadmin(String channel, Set<String> invoiceNo, String clientId,
			String transactionCode);
	
	public Map<String, Object[]> findLastSuccessTransactionLogsWebadmin(String channel, Set<String> invoiceNo, String clientId,
			String transactionCode, String channelRcSuccessStatus);
	
	public Map<String, Object[]> findAllTransactionLogsWebadminPayment(String channel, Set<String> invoiceNo, String clientId,
			Set<String> transactionCode);

	public Map<String, Object[]> findAllTransactionLogsWebadminReconcile(
			String channel, Set<String> invoiceNo, String clientId,
			String transactionCode, String[] billerRc, String[] channelRc, Date trxDate);

	public void insertDummyDataWebadminReconcile(String trxCode, String channelCode, String billerCode, String invoiceNo, String reconFlag, Date transactionDate);
	
	public boolean updateReconFlag(String invoiceNo, String reconFlag, String channelRc, String billerRc, String transactionCode);
	
	public Map<String, Object[]> findAllTransactionLogsWebadminReconcileByDate(
			String channel, String transactionCode, String[] billerRc, String[] channelRc, Date trxDate);
	

	
	
}
