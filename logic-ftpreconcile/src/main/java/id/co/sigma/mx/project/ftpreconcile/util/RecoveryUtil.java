package id.co.sigma.mx.project.ftpreconcile.util;

import id.co.sigma.mx.project.ftpreconcile.constant.PaymentConstant;
import id.co.sigma.mx.project.ftpreconcile.model.ProcessItem;
import id.co.sigma.mx.project.ftpreconcile.model.ProcessStatus;
import id.co.sigma.mx.project.ftpreconcile.model.Transaction;
import id.co.sigma.mx.project.ftpreconcile.model.TransactionIso;
import id.co.sigma.mx.project.ftpreconcile.model.TransactionStatus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class RecoveryUtil {

	private static Logger logger = Logger.getLogger(RecoveryUtil.class);

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private PaymentUtil paymentUtil;

	public void setPaymentUtil(PaymentUtil paymentUtil) {
		this.paymentUtil = paymentUtil;
	}

	/**
	 * This method retrieves last <code>PROCESS_STATUS</code> to determine whether execution of RecoveryProcess is needed or not.
	 * @return Instance of <code>ProcessStatus</code> modelling latest created <code>PROCESS_STATUS</code> record.
	 * @throws SQLException
	 */
	public ProcessStatus getLastProcessStatus() throws SQLException {
		String sql = "SELECT aa.* "
			.concat(" FROM PROCESS_STATUS aa, (SELECT process_status_id, ")
			.concat(" 	 							MAX(process_status_id) OVER (PARTITION BY NULL ORDER BY NULL) last_process_status_id")
			.concat(" 	 					   FROM process_status) bb")
			.concat(" WHERE bb.process_status_id = bb.last_process_status_id")
			.concat(" AND aa.process_status_id = bb.process_status_id	");
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			s = con.createStatement();
			rs = s.executeQuery(sql);
			while(rs.next()) {
				ProcessStatus processStatus = new ProcessStatus();
				processStatus.setProcessStatusId(rs.getLong("PROCESS_STATUS_ID"));
				processStatus.setReconcileLogId(rs.getLong("RECONCILE_LOG_ID"));
				processStatus.setIsoBatchLogId(rs.getLong("ISO_BATCH_LOG_ID"));
				processStatus.setStatus(rs.getString("STATUS"));
				return processStatus;
			}
		} catch (SQLException e) {
			logger.error(e);
			throw e;
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(s != null) {
				s.close();
			}
			if(con != null) {
				con.close();
			}
		}
		return null;
	}

	/**
	 * This method finishes a ReconciliationProcess, left unfinished by previous PaymentProcess cycle, because of hardware problem, or other fatal error.
	 * @param lastProcessStatus Instance of <code>ProcessStatus</code> class, previous uncompleted PaymentProcess cycle.
	 * @throws SQLException
	 */
	public void reconcileUnfinishedTransaction(ProcessStatus lastProcessStatus) throws SQLException {
		Long lastIsoBatchLogId = lastProcessStatus.getIsoBatchLogId();
		List<TransactionStatus> needReconcileTransactions = new ArrayList<TransactionStatus>();
		Map<Long, ProcessItem> mapOfUpdatedProcessItems = new TreeMap<Long, ProcessItem>();
		Map<Long, ProcessItem> mapOfProcessItems = paymentUtil.getCurrentProcessItems(lastProcessStatus, null, null, null, null);
		Map<Long, Transaction> mapOfTransactions = paymentUtil.getCurrentTransactions(lastProcessStatus, null, null, null, null);
		Map<String, TransactionIso> mapOfTransactionIso = null;
		if(lastIsoBatchLogId != null) {
			mapOfTransactionIso = getMapOfTransactionIso(lastIsoBatchLogId);
		}
		Iterator<Entry<Long, ProcessItem>> i = mapOfProcessItems.entrySet().iterator();
		while(i.hasNext()) {
			Entry<Long, ProcessItem> entry = i.next();
			Long transactionId = entry.getKey();
			String stan = null;
			String rrn = null;
			ProcessItem processItem = mapOfProcessItems.get(transactionId);
			if(processItem.getReconcileResultId() != null && processItem.getReconcileResultId().longValue() > 0) {
				logger.info("Skipping recovery process for " + processItem);
				continue;
			}
			Transaction t = mapOfTransactions.get(transactionId);
			String invoiceId = t.getInvoiceId();
			String clientId = t.getClientId();
			if(invoiceId != null) {
				invoiceId = invoiceId.trim();
			}
			if(clientId != null) {
				clientId = clientId.trim();
			}
			if(processItem.getStatus() == null) {
				stan = processItem.getPaymentStan();
				rrn = processItem.getPaymentRrn();
				processItem.setStatus(PaymentConstant.TRANSACTION_STATUS_FAILED);
				processItem.setResponseCode(PaymentConstant.DATABASE_DOWN_RESPONSE_CODE);
				mapOfUpdatedProcessItems.put(transactionId, processItem);
				if(stan == null || rrn == null) {
					if(mapOfTransactionIso != null && mapOfTransactionIso.containsKey(invoiceId.concat(clientId))) {
						TransactionIso transactionIso = mapOfTransactionIso.get(invoiceId.concat(clientId));
						stan = guessPaymentStan(transactionIso);
						rrn = guessPaymentRrn(transactionIso);
					}
					else {
						paymentUtil.createReconcileResult(lastProcessStatus, processItem);
					}
				}
			}
			else {
				if(processItem.getStatus().equals(PaymentConstant.TRANSACTION_STATUS_ERROR) ||
				   processItem.getStatus().equals(PaymentConstant.TRANSACTION_STATUS_UNPAID)) {
					Long reconcileResultId = processItem.getReconcileResultId();
					if(reconcileResultId != null && reconcileResultId.longValue() > 0) {
						logger.info("No Need Reconcile: " + processItem);
					}
					else {
						paymentUtil.createReconcileResult(lastProcessStatus, processItem);
					}
				}
				if(processItem.getStatus().equals(PaymentConstant.TRANSACTION_STATUS_PAID) ||
				   processItem.getStatus().equals(PaymentConstant.TRANSACTION_STATUS_FAILED)) {
					stan = processItem.getPaymentStan();
					rrn = processItem.getPaymentRrn();
					if(stan == null || rrn == null) {
						if(mapOfTransactionIso != null && mapOfTransactionIso.containsKey(invoiceId.concat(clientId))) {
							TransactionIso transactionIso = mapOfTransactionIso.get(invoiceId.concat(clientId));
							stan = guessPaymentStan(transactionIso);
							rrn = guessPaymentRrn(transactionIso);
						}
						else {
							processItem.setStatus(PaymentConstant.TRANSACTION_STATUS_FAILED);
							processItem.setResponseCode(PaymentConstant.DATABASE_DOWN_RESPONSE_CODE);
							paymentUtil.createReconcileResult(lastProcessStatus, processItem);
						}
					}
				}
			}
			if(stan != null && rrn != null) {
				TransactionStatus transactionStatus = new TransactionStatus();
				transactionStatus.setTransactionId(t.getTransactionId());
				transactionStatus.setInvoiceId(t.getInvoiceId());
				transactionStatus.setClientId(t.getClientId());
				transactionStatus.setFileId(t.getFileId());
				transactionStatus.setSystemTraceAuditNumber(stan);
				transactionStatus.setRetrievalReferenceNumber(rrn);
				transactionStatus.setTransactionStatus(processItem.getStatus());
				transactionStatus.setTransactionDate(t.getTransactionDate());
				needReconcileTransactions.add(transactionStatus);
			}
		}
		if(mapOfUpdatedProcessItems != null && mapOfUpdatedProcessItems.size() > 0) {
			paymentUtil.saveProcessItem(mapOfProcessItems);
		}
		if(needReconcileTransactions != null && needReconcileTransactions.size() > 0) {
			paymentUtil.reconcileWithSpectra(lastProcessStatus, needReconcileTransactions);
		}
		paymentUtil.updateTransactions(lastProcessStatus);
		paymentUtil.updateReconcileLog(lastProcessStatus.getReconcileLogId());
		if(lastIsoBatchLogId != null ) {
			paymentUtil.updateIsoBatchLog(lastIsoBatchLogId);
		}
		paymentUtil.markCompleted(lastProcessStatus);
		paymentUtil.activateReconcileResult(lastProcessStatus);
	}

	/**
	 * This method loads <code>TRANSACTION_ISO</code> records with specific <code>ISO_BATCH_LOG_ID</code>.
	 * @param lastIsoBatchLogId Query parameter <code>ISO_BATCH_LOG_ID</code>.
	 * @return <code>java.util.Map</code> of <code>TransactionIso</code> instances, modelling <code>TRANSACTION_ISO</code> records.
	 * @throws SQLException
	 */
	private Map<String, TransactionIso> getMapOfTransactionIso(Long lastIsoBatchLogId) throws SQLException {
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		final String sql = "SELECT DISTINCT aa.*"
				.concat(" FROM transaction_iso aa, ")
				.concat(" (SELECT a.iso_batch_log_id, a.transaction_id, max(a.transaction_iso_id) as last_transaction_iso_id")
				.concat("  FROM postel.transaction_iso_map a")
				.concat("  WHERE a.ISO_BATCH_LOG_ID =" + lastIsoBatchLogId)
				.concat("  GROUP BY iso_batch_log_id, transaction_id) bb")
				.concat(" WHERE aa.transaction_iso_id = bb.last_transaction_iso_id");
		Map<String, TransactionIso> mapOfTransactionIso = new HashMap<String, TransactionIso>();
		try {
			c = dataSource.getConnection();
			s = c.createStatement();
			rs = s.executeQuery(sql);
			while (rs.next()) {
				TransactionIso transactionIso = new TransactionIso();
				transactionIso.setTransactionIsoId(rs
						.getLong("TRANSACTION_ISO_ID"));

				String clientId = rs.getString("CLIENT_ID");
				String invoiceId = rs.getString("INVOICE_ID");
				String mti = rs.getString("MTI");

				String prevRrn = rs.getString("PREV_RRN");
				String prevStan = rs.getString("PREV_STAN");

				String processingCode = rs.getString("PROCESSING_CODE");
				String responseCode = rs.getString("RESPONSE_CODE");
				String rrn = rs.getString("RRN");

				String settlementDate = rs.getString("SETTLEMENT_DATE");
				String stan = rs.getString("STAN");
				Long transactionAmount = rs.getLong("TRANSACTION_AMOUNT");

				if (clientId != null && clientId.length() > 0) {
					transactionIso.setClientId(clientId);
				}
				if (invoiceId != null && invoiceId.length() > 0) {
					transactionIso.setInvoiceId(invoiceId);
				}
				if (mti != null && mti.length() > 0) {
					transactionIso.setMti(mti);
				}
				if (prevRrn != null && prevRrn.length() > 0) {
					transactionIso.setPrevRrn(prevRrn);
				}
				if (prevStan != null && prevStan.length() > 0) {
					transactionIso.setPrevStan(prevStan);
				}
				if (processingCode != null && processingCode.length() > 0) {
					transactionIso.setProcessingCode(processingCode);
				}
				if (responseCode != null && responseCode.length() > 0) {
					transactionIso.setResponseCode(responseCode);
				}
				if (rrn != null && rrn.length() > 0) {
					transactionIso.setRrn(rrn);
				}
				if (settlementDate != null && settlementDate.length() > 0) {
					transactionIso.setSettlementDate(settlementDate);
				}
				if (stan != null && stan.length() > 0) {
					transactionIso.setStan(stan);
				}
				if (transactionAmount != null) {
					transactionIso.setTransactionAmount(transactionAmount);
				}
				mapOfTransactionIso.put(invoiceId.concat(clientId),
						transactionIso);
			}
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (s != null) {
				s.close();
			}
			if (c != null) {
				c.close();
			}
		}
		return mapOfTransactionIso;
	}

	/**
	 * This method determines <code>RRN</code> attributes of <code>ISOMsg</code> sent as PaymentRequest.
	 * <li>If current <code>ISOMsg</code> saved in TransactionIso is PaymentRequest then it's RRN value is returned.
	 * <li>If curent <code>ISOMsg</code> saved in TransactionIso is InquiryRequest then it's RRN value + 1 will be returned.
	 * <li>If curent <code>ISOMsg</code> saved in TransactionIso is ReversalRequest then it's RRN value will be returned.
	 * @param transactionIso Instance of <code>TransactionIso</code> class, storing information about <code>ISOMsg</code> sent or received.
	 * @return 12 digit number as RRN.
	 */
	private static String guessPaymentRrn(TransactionIso transactionIso) {
		String rrn = null;
		if(transactionIso.getMti().equals(PaymentConstant.MTI_FINANCIAL_REQUEST) ||
		   transactionIso.getMti().equals(PaymentConstant.MTI_FINANCIAL_REQUEST_RESPONSE)) {
			if(transactionIso.getProcessingCode().equals(PaymentConstant.INQUIRY_TRANSACTION_CODE)) {
				try {
					long intRrn = 1000000000001L + Long.parseLong(transactionIso.getRrn());
					rrn = ("" + intRrn).substring(1);
				}
				catch(NumberFormatException e) {
					logger.error(e);

				}
			}
			if(transactionIso.getProcessingCode().equals(PaymentConstant.PAYMENT_TRANSACTION_CODE)) {
				rrn = transactionIso.getRrn();
			}
		}
		if(transactionIso.getMti().equals(PaymentConstant.MTI_REVERSAL_REQUEST) ||
		   transactionIso.getMti().equals(PaymentConstant.MTI_REVERSAL_REQUEST_RESPONSE)) {
			if(transactionIso.getProcessingCode().equals(PaymentConstant.PAYMENT_TRANSACTION_CODE)) {
				rrn = transactionIso.getRrn();
			}
		}
		return rrn;
	}

	/**
	 * This method determines <code>STAN</code> attributes of <code>ISOMsg</code> sent as PaymentRequest.
	 * <li>If current <code>ISOMsg</code> saved in TransactionIso is PaymentRequest then it's STAN value is returned.
	 * <li>If curent <code>ISOMsg</code> saved in TransactionIso is InquiryRequest then it's STAN value + 1 will be returned.
	 * <li>If curent <code>ISOMsg</code> saved in TransactionIso is ReversalRequest then it's STAN value - 1 will be returned.
	 * @param transactionIso Instance of <code>TransactionIso</code> class, storing information about <code>ISOMsg</code> sent or received.
	 * @return 6 digit STAN.
	 */
	private static String guessPaymentStan(TransactionIso transactionIso) {
		String stan = null;
		if(transactionIso.getMti().equals(PaymentConstant.MTI_FINANCIAL_REQUEST) ||
		   transactionIso.getMti().equals(PaymentConstant.MTI_FINANCIAL_REQUEST_RESPONSE)) {
			if(transactionIso.getProcessingCode().equals(PaymentConstant.INQUIRY_TRANSACTION_CODE)) {
				try {
					int intStan = 1000001 + Integer.parseInt(transactionIso.getStan());
					stan = ("" + intStan).substring(1);
				}
				catch(NumberFormatException e) {
					logger.error(e);

				}
			}
			if(transactionIso.getProcessingCode().equals(PaymentConstant.PAYMENT_TRANSACTION_CODE)) {
				stan = transactionIso.getStan();
			}
		}
		if(transactionIso.getMti().equals(PaymentConstant.MTI_REVERSAL_REQUEST) ||
		   transactionIso.getMti().equals(PaymentConstant.MTI_REVERSAL_REQUEST_RESPONSE)) {
			if(transactionIso.getProcessingCode().equals(PaymentConstant.PAYMENT_TRANSACTION_CODE)) {
				try {
					int intStan = 1000000 + Integer.parseInt(transactionIso.getStan()) - 1;
					stan = ("" + intStan).substring(1);
				}
				catch(NumberFormatException e) {
					logger.error(e);
				}
			}
		}
		return stan;
	}
}
