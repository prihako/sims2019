package id.co.sigma.mx.project.ftpreconcile.util;

import id.co.sigma.mx.project.ftpreconcile.constant.PaymentConstant;
import id.co.sigma.mx.project.ftpreconcile.constant.PaymentConstant.MESSAGE_TYPE;
import id.co.sigma.mx.project.ftpreconcile.constant.PostelConstant;
import id.co.sigma.mx.project.ftpreconcile.model.FileTransactionReceive;
import id.co.sigma.mx.project.ftpreconcile.model.IsoBatchLog;
import id.co.sigma.mx.project.ftpreconcile.model.ListenerLog;
import id.co.sigma.mx.project.ftpreconcile.model.ParserLog;
import id.co.sigma.mx.project.ftpreconcile.model.ProcessItem;
import id.co.sigma.mx.project.ftpreconcile.model.ProcessStatus;
import id.co.sigma.mx.project.ftpreconcile.model.ReconcileLog;
import id.co.sigma.mx.project.ftpreconcile.model.ReconcileResult;
import id.co.sigma.mx.project.ftpreconcile.model.Transaction;
import id.co.sigma.mx.project.ftpreconcile.model.TransactionFile;
import id.co.sigma.mx.project.ftpreconcile.model.TransactionSettlement;
import id.co.sigma.mx.project.ftpreconcile.model.TransactionStatus;
import id.co.sigma.mx.project.ftpreconcile.process.ProcessorPayment;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ws.billpayment.h2h.bankmandiri.ArrayOfString;
import ws.billpayment.h2h.bankmandiri.BillPaymentService;
import ws.billpayment.h2h.bankmandiri.InquiryRequest;
import ws.billpayment.h2h.bankmandiri.InquiryResponse2;
import ws.billpayment.h2h.bankmandiri.PaymentRequest;
import ws.billpayment.h2h.bankmandiri.PaymentResponse2;
import ws.billpayment.h2h.bankmandiri.ReversalRequest;
import ws.billpayment.h2h.bankmandiri.ReversalResponse;

public class PaymentUtil {
	private static Logger logger = Logger.getLogger(PaymentUtil.class);

	public static final SimpleDateFormat MMdd = new SimpleDateFormat("MMdd");
	public static final SimpleDateFormat HHmmss = new SimpleDateFormat("HHmmss");
	public static final SimpleDateFormat ddMMyyyy = new SimpleDateFormat("ddMMyyyy");

	public static final String className = PaymentUtil.class.getCanonicalName();

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private SequenceUtil sequenceUtil;

	public void setSequenceUtil(SequenceUtil sequenceUtil) {
		this.sequenceUtil = sequenceUtil;
	}

	/**
	 * This method retrieves status of a <code>Transaction</code>, based on STAN and RRN.
	 * @param stan STAN <code>ISOMsg</code>'s STAN.
	 * @param rrn RRN <code>ISOMsg</code>'s RRN.
	 * @return <code>TransactionStatus</code> status of a <code>Transaction</code>.
	 * @throws SQLException
	 */

	public TransactionStatus getTransactionStatus(String stan,
			String rrn) throws SQLException {
		Connection con = null;
		CallableStatement callable = null;
		TransactionStatus status = new TransactionStatus();
		status.setSingleRecordFound(new Boolean(false));
		final String sql = "{? = call GETTRANSACTIONSTATUS(?, ?, ?, ?, ?, ?)}";

		try {
			con = dataSource.getConnection();
			callable = con.prepareCall(sql);
			callable.registerOutParameter(1, Types.VARCHAR);
			callable.registerOutParameter(4, Types.VARCHAR);
			callable.registerOutParameter(5, Types.VARCHAR);
			callable.registerOutParameter(6, Types.VARCHAR);
			callable.registerOutParameter(7, Types.VARCHAR);
			callable.setString(2, stan);
			callable.setString(3, rrn);
			if (!callable.execute()) {
				if ("true".equals(callable.getString(1))) {
					status.setSingleRecordFound(new Boolean(true));
					status.setTransactionStatus(callable.getString(4));
					status.setReceiptCode(callable.getString(5));
					status.setInvoiceId(callable.getString(6));
					status.setClientId(callable.getString(7));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		} finally {
			try {
				if (callable != null) {
					callable.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
		return status;
	}

	/**
	 * This method creates record in table <code>RECONCILE_LOG</code>.
	 * @return <code>ReconcileLog</code> object just created.
	 * @throws SQLException
	 */
	public ReconcileLog createReconcileLog() throws SQLException {

		ReconcileLog log = new ReconcileLog();
		Connection con = null;
		Statement s = null;
		try {
			con = dataSource.getConnection();
			log.setReconcileLogId(sequenceUtil.generateReconcileLogId());
			log.setReconcileDate(new Date());
			log.setTotalFullMatch(0);
			log.setTotalPartialMatch(0);
			log.setTotalNotMatch(0);
			log.setTotalRecord(0);
			String sql = "INSERT INTO reconcile_log(reconcile_log_id, reconcile_date, ";
			sql += " total_full_match, total_partial_match, total_not_match, total_pending, total_record) ";
			sql += " VALUES(" + log.getReconcileLogId()
					+ ", SYSDATE, 0, 0, 0, 0, 0)";
			s = con.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		} finally {
			try {
				s.close();
				con.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return log;
	}

	/**
	 * This method retrieves <code>TRANSACTION</code> where <code>TRANSACTION_STATUS</code> is 'F', or 'R'.
	 * @return <code>Map<Long, Transaction></code> of <code>TRANSACTION</code> where
	 * TRANSACTION_STATUS is 'F', 'R'.
	 * @throws SQLException
	*/
	public Map<Integer, Transaction> getUnprocessedTransactions() throws SQLException {
		Map<Integer, Transaction> map = new TreeMap<Integer, Transaction>();
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		String sql = "SELECT aa.FILE_ID, aa.TRANSACTION_ID, aa.INVOICE_ID, aa.CLIENT_ID, aa.TRANSACTION_DATE, aa.TRANSACTION_AMOUNT"
			 .concat(" FROM(SELECT a.FILE_ID, a.TRANSACTION_ID, a.INVOICE_ID, a.CLIENT_ID, a.TRANSACTION_DATE, a.TRANSACTION_AMOUNT")
			 .concat("      FROM transaction a, transaction_file b, listener_log cc")
			 .concat("      WHERE a.TRANSACTION_STATUS IN ('F', 'R')")
			 .concat("      AND (( a.TRANSACTION_SRC = 'F' AND a.FILE_ID = b.FILE_ID")
			 .concat("      AND    b.LISTENER_LOG_ID = cc.LISTENER_LOG_ID")
			 .concat("      AND    cc.LISTENER_STATUS = 'VA') )")
			 .concat("      AND a.INVOICE_ID IS NOT NULL")
			 .concat("      AND a.CLIENT_ID IS NOT NULL")
			 .concat("      AND NVL(a.TRANSACTION_AMOUNT, 0) > 0")
			 .concat("      UNION ALL")
			 .concat("      SELECT a.FILE_ID, a.TRANSACTION_ID, a.INVOICE_ID, a.CLIENT_ID, a.TRANSACTION_DATE, a.TRANSACTION_AMOUNT")
			 .concat("      FROM transaction a")
			 .concat("      WHERE a.TRANSACTION_STATUS IN ('F', 'R')")
			 .concat("      AND a.TRANSACTION_SRC = 'S' AND a.FILE_ID IS NULL")
			 .concat("      AND a.INVOICE_ID IS NOT NULL")
			 .concat("      AND a.CLIENT_ID IS NOT NULL")
			 .concat("      AND NVL(a.TRANSACTION_AMOUNT, 0) > 0) aa")
			 .concat(" ORDER BY aa.TRANSACTION_DATE, aa.FILE_ID NULLS FIRST, aa.INVOICE_ID, aa.CLIENT_ID");
		try {
			c = dataSource.getConnection();
			s = c.createStatement();
			rs = s.executeQuery(sql);
			while(rs.next()) {
				Transaction t = new Transaction();
				t.setFileId(rs.getLong("FILE_ID"));
				t.setTransactionId(rs.getLong("TRANSACTION_ID"));
				t.setInvoiceId(rs.getString("INVOICE_ID"));
				t.setClientId(rs.getString("CLIENT_ID"));
				t.setTransactionDate(new Date(rs.getDate("TRANSACTION_DATE").getTime()));
				t.setTransactionAmount(rs.getBigDecimal("TRANSACTION_AMOUNT"));
				map.put(map.size(), t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		} finally {
			try {
				rs.close();
				s.close();
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * This method validates a <code>Transaction</code> based on three condition:
	 * <li> InvoiceID must not be null.
	 * <li> ClientID must not be null.
	 * <li> Transaction Amount must be greater than 0
	 * @param transaction
	 * @return <code>boolean</code> value indicating validity of a <code>Transaction</code>.
	 */
	public boolean transactionIsValid(Transaction transaction) {
		boolean isValid = true;
		boolean returnValue = true;
		isValid &= transaction.getInvoiceId() != null
				&& transaction.getInvoiceId().length() > 0;
		if (!isValid) {
			logger.warn(" Invalid, InvoiceID is NULL or empty\n");
			returnValue &= isValid;
		}
		isValid &= true & transaction.getClientId() != null
				&& transaction.getClientId().length() > 0;
		if (!isValid) {
			logger.warn(" Invalid, ClientID is NULL or empty\n");
			returnValue &= isValid;
		}
		isValid &= true & transaction.getTransactionAmount().signum() > 0;
		if (!isValid) {
			logger.warn(" Invalid, TransactionAmount is\n "
					+ transaction.getTransactionAmount());
			returnValue &= isValid;
		}
		return returnValue;
	}

	/**
	 * This method retrieves <code>Transaction</code> from database whose TRANSACTION_STATUS is 'E'.
	 * @return <code>List</code> of <code>Transaction</code> whose status is 'E' (Error).
	 * @throws SQLException
	 */
	public Map<Integer, Transaction> getErrorTransaction() throws SQLException {
		Map<Integer, Transaction> errorTransactions = new TreeMap<Integer, Transaction>();
		String sql = "SELECT TRANSACTION_ID, TRANSACTION_STATUS";
		sql += " FROM TRANSACTION a";
		sql += " WHERE a.TRANSACTION_STATUS = 'E'";
		sql += " AND a.NEED_RECONCILE='Y'";
		sql += " AND a.TRANSACTION_ID NOT IN (SELECT transaction_id";
		sql += "							  FROM reconcile_result b";
		sql += " 							  WHERE b.transaction_id = a.transaction_id)";
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			s = con.createStatement();
			rs = s.executeQuery(sql);
			while (rs.next()) {
				Transaction t = new Transaction();
				t.setTransactionId(rs.getLong("TRANSACTION_ID"));
				t.setTransactionStatus(rs.getString("TRANSACTION_STATUS"));
				errorTransactions.put(errorTransactions.size(), t);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		} finally {
			try {
				s.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return errorTransactions;
	}

	/**
	 * This method creates a record in <code>ISO_BATCH_LOG</code> table.
	 * @return <code>IsoBatchLog</code> object for record just created.
	 * @throws SQLException
	 */
	public IsoBatchLog createIsoBatchLog() throws SQLException {

		long isoBatchLogId = 0;
		isoBatchLogId = sequenceUtil.generateIsoBatchLogId();
		IsoBatchLog ibl = new IsoBatchLog();
		ibl.setIsoBatchLogId(isoBatchLogId);
		ibl.setBatchDate(new Date());
		ibl.setTotalPaid(0);
		ibl.setTotalUnpaid(0);
		ibl.setTotalFailed(0);
		ibl.setTotalRecord(0);

		String sql = "INSERT INTO iso_batch_log(ISO_BATCH_LOG_ID, BATCH_DATE, TOTAL_PAID, TOTAL_UNPAID, TOTAL_FAILED, TOTAL_RECORD)";
		sql += " VALUES(".concat("" + ibl.getIsoBatchLogId());
		sql += ", ".concat("SYSDATE");
		sql += ", ".concat("" + ibl.getTotalPaid());
		sql += ", ".concat("" + ibl.getTotalUnpaid());
		sql += ", ".concat("" + ibl.getTotalFailed());
		sql += ", ".concat("" + ibl.getTotalRecord() + ")");

		Connection con = null;
		Statement s = null;
		try {
			con = dataSource.getConnection();
			s = con.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		} finally {
			try {
				if (s != null) {
					s.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return ibl;
	}

	/**
	 * This method create <code>PROCESS_STATUS</code> record storing information about a PaymentProcess cycle.
	 * @param isoBatchLog Foreign Key to table <code>ISO_BATCH_LOG</code>, field <code>ISO_BATCH_LOG_ID</code>.
	 * @param reconcileLog Foreign Key to table <code>RECONCILE_LOG</code>, field <code>RECONCILE_LOG_ID</code>.
	 * @return <code>ProcessStatus</code> object modelling <code>PROCESS_STATUS</code>.
	 * @throws SQLException
	 */
	public ProcessStatus createProcessStatus(IsoBatchLog isoBatchLog, ReconcileLog reconcileLog) throws SQLException {
		ProcessStatus processStatus = null;

		Connection c = null;
		Statement s = null;
		try {
			processStatus = new ProcessStatus();
			processStatus.setProcessStatusId(sequenceUtil.generateProcessStatusId());
			processStatus.setReconcileLogId(reconcileLog.getReconcileLogId());
			if(isoBatchLog != null) {
				processStatus.setIsoBatchLogId(isoBatchLog.getIsoBatchLogId());
			}
			processStatus.setStatus("S");
			String sql = "INSERT INTO PROCESS_STATUS(PROCESS_STATUS_ID, RECONCILE_LOG_ID, ISO_BATCH_LOG_ID, STATUS)";
			sql += " VALUES(" + processStatus.getProcessStatusId();
			sql += ", " + processStatus.getReconcileLogId();
			if(isoBatchLog != null) {
				sql += ", " + processStatus.getIsoBatchLogId();
			}
			else {
				sql += ", NULL";
			}
			sql += ", '" + processStatus.getStatus() + "')";
			logger.debug(sql);
			c = dataSource.getConnection();
			s = c.createStatement();
			s.executeUpdate(sql);
			return processStatus;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(s != null) {
				s.close();
			}
			if(c != null) {
				c.close();
			}
		}
	}

	/**
	 * This method create <code>PROCESS_ITEM</code> records.
	 * @param transactions <code>java.util.Collection</code> of <code>TRANSACTION</code> in which a <code>PROCESS_ITEM</code> record will be created for each one of them.
	 * @param processStatus Instance of <code>ProcessStatus</code> class whose it's <code>PROCESS_STATUS_ID</code> will be recorded in newly created <code>PROCESS_ITEM</code> records.
	 * @return <code>java.util.Map</code> of newly created <code>PROCESS_ITEM</code> records.
	 * @throws SQLException
	 */
	public Map<Long, ProcessItem> createProcessItems(Map<Integer, Transaction> transactions, ProcessStatus processStatus) throws SQLException {
		Connection c = null;
		PreparedStatement s = null;
		Iterator<Entry<Integer,Transaction>> i = transactions.entrySet().iterator();
		Map<Long, ProcessItem> processed = new TreeMap<Long, ProcessItem>();
		try {
			c = dataSource.getConnection();
			while(i.hasNext()) {
				s = c.prepareStatement("INSERT INTO PROCESS_ITEM(PROCESS_STATUS_ID, TRANSACTION_ID, STATUS)"
					       .concat(" VALUES(?, ?, ?)"));
				Long processStatusId = processStatus.getProcessStatusId();
				Entry<Integer,Transaction> entry = i.next();
				Transaction t = entry.getValue();
				Long transactionId = t.getTransactionId();
				String status = t.getTransactionStatus();
				ProcessItem process = new ProcessItem();
				process.setProcessStatusId(processStatusId);
				process.setTransactionId(transactionId);
				s.setLong(1, processStatusId.longValue());
				s.setLong(2, transactionId.longValue());
				s.setString(3, status);
				s.addBatch();
				processed.put(transactionId, process);
			}
			if(i.hasNext()) s.executeBatch();
			c.commit();
			return processed;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(s != null) {
				s.close();
			}
			if(c != null) {
				c.close();
			}
		}
	}

	/**
	 * This method creates <code>RECONCILE_RESULT</code> record for <code>TRANSACTION</code> which;
	 * <p><li><code>TRANSACTION_STATUS</code> field equal to 'E'.
	 * <p><li><code>NEED_RECONCILE</code> field equal to 'Y'.
	 * <p><li>No <code>RECONCILE_RESULT</code> record have been created for that <code>TRANSACTION</code>.
	 * @param errorTransactions <code>java.util.Map</code> of unreconciled error <code>TRANSACTION</code>.
	 * @param processStatus Instance of <code>ProcessStatus</code> class.
	 * @throws SQLException
	 */
	public void reconcileErrorTransaction(Map<Integer, Transaction> errorTransactions, ProcessStatus processStatus) throws SQLException {
		try {
			createProcessItems(errorTransactions, processStatus);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}

	}

	/**
	 * This method performs ReconciliationProcess for a PaymentProcess cycle.
	 * @param processStatus Instance of <code>ProcessStatus</code>, storing information about a PaymentProcess cycle.
	 * @throws SQLException
	 */
	public void reconcile(ProcessStatus processStatus) throws SQLException {
		Map<Long, ProcessItem> mapOfStatuses = null;
		List<TransactionStatus> needReconcileTransactions = new ArrayList<TransactionStatus>();
		Map<Long, Transaction> mapOfTransactions = null;
		try {
			mapOfTransactions = getCurrentTransactions(processStatus, null, null, null, null );
			mapOfStatuses = getCurrentProcessItems(processStatus, null, null, null, null );
			Iterator<Entry<Long, ProcessItem>> i = mapOfStatuses.entrySet().iterator();
			while(i.hasNext()) {
				Entry<Long, ProcessItem> entry = i.next();
				ProcessItem processItem =  entry.getValue();
				String stan = processItem.getPaymentStan();
				String rrn = processItem.getPaymentRrn();
				String status = processItem.getStatus();
				if(status.equals(PaymentConstant.TRANSACTION_STATUS_ERROR)) {
					processItem.setResponseCode(PaymentConstant.INVALID_TRANSACTION_ERROR_CODE.toString());
					createReconcileResult(processStatus, processItem);
				}
				if(status.equals(PaymentConstant.TRANSACTION_STATUS_UNPAID)) {
					createReconcileResult(processStatus, processItem);
				}
				if(status.equals(PaymentConstant.TRANSACTION_STATUS_FAILED) ||
				   status.equals(PaymentConstant.TRANSACTION_STATUS_PAID)) {
					if(stan != null && stan.length() > 0 && rrn != null && rrn.length() > 0) {
						Transaction t = mapOfTransactions.get(processItem.getTransactionId());
						TransactionStatus transactionStatus = new TransactionStatus();
						transactionStatus.setTransactionId(t.getTransactionId());
						transactionStatus.setInvoiceId(t.getInvoiceId());
						transactionStatus.setClientId(t.getClientId());
						transactionStatus.setFileId(t.getFileId());
						transactionStatus.setSystemTraceAuditNumber(stan);
						transactionStatus.setRetrievalReferenceNumber(rrn);
						transactionStatus.setTransactionStatus(status);
						transactionStatus.setTransactionDate(t.getTransactionDate());
						needReconcileTransactions.add(transactionStatus);
					}
					else {
						createReconcileResult(processStatus, processItem);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		if(needReconcileTransactions.size() > 0) {
			reconcileWithSpectra(processStatus, needReconcileTransactions);
		}

	}

	/**
	 * This method performs groupping on <code>Transaction</code>(s) already selected for processing.
	 * @param processStatus Instance of <code>ProcessStatus</code> class, storing information about a PaymentProcess cycle.
	 * @return <code>java.util.Map</code> of groupped <code>Transaction</code>(s).
	 * @throws SQLException
	 */
	public Map<Integer, Transaction> groupTransactionToProcess(ProcessStatus processStatus) throws SQLException {
		Map<Integer, Transaction> grouppedTransaction = new TreeMap<Integer, Transaction>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			Long processStatusId = processStatus.getProcessStatusId();
			c = dataSource.getConnection();
			String sql = "SELECT           a.FILE_ID,  a.INVOICE_ID,  a.CLIENT_ID,  a.TRANSACTION_DATE, SUM(a.TRANSACTION_AMOUNT) AS TRANSACTION_AMOUNT"
					.concat(" FROM(SELECT bb.FILE_ID, bb.INVOICE_ID, bb.CLIENT_ID, bb.TRANSACTION_DATE, bb.TRANSACTION_AMOUNT")
					.concat("      FROM process_item aa, transaction bb, transaction_file cc, listener_log dd")
					.concat("      WHERE aa.PROCESS_STATUS_ID = ?")
					.concat("      AND aa.TRANSACTION_ID = bb.TRANSACTION_ID ")
					.concat("      AND bb.TRANSACTION_STATUS IN ('F', 'R')")
					.concat("      AND (( bb.TRANSACTION_SRC = 'F' AND bb.FILE_ID = cc.FILE_ID")
					.concat("	   AND    cc.LISTENER_LOG_ID = dd.LISTENER_LOG_ID")
					.concat("	   AND    dd.LISTENER_STATUS = 'VA') )")
					.concat(" 	   AND bb.INVOICE_ID IS NOT NULL")
					.concat("	   AND bb.CLIENT_ID IS NOT NULL")
					.concat("      AND NVL(bb.TRANSACTION_AMOUNT, 0) > 0")
					.concat("      UNION ALL")
					.concat("      SELECT bb.FILE_ID, bb.INVOICE_ID, bb.CLIENT_ID, bb.TRANSACTION_DATE, bb.TRANSACTION_AMOUNT")
					.concat("      FROM process_item aa, transaction bb")
					.concat("      WHERE aa.PROCESS_STATUS_ID = ?")
					.concat("	   AND aa.TRANSACTION_ID = bb.TRANSACTION_ID")
					.concat("      AND bb.TRANSACTION_STATUS IN ('F', 'R')")
					.concat("      AND bb.TRANSACTION_SRC = 'S' AND bb.FILE_ID IS NULL")
					.concat("      AND bb.INVOICE_ID IS NOT NULL")
					.concat("      AND bb.CLIENT_ID IS NOT NULL")
					.concat("      AND NVL(bb.TRANSACTION_AMOUNT, 0) > 0) a")
					.concat(" GROUP BY FILE_ID, TRANSACTION_DATE, INVOICE_ID, CLIENT_ID")
					.concat(" ORDER BY TRANSACTION_DATE, FILE_ID NULLS FIRST, INVOICE_ID, CLIENT_ID");
			logger.debug(sql);
			s = c.prepareStatement(sql);
			s.setLong(1, processStatusId);
			s.setLong(2, processStatusId);
			if(s.execute()) {
				rs = s.getResultSet();
				while (rs.next()) {
					Transaction t = new Transaction();
					Long fileId = rs.getLong("FILE_ID");

					String invoiceId = rs.getString("INVOICE_ID");
					String clientId = rs.getString("CLIENT_ID");
					BigDecimal transactionAmount = rs.getBigDecimal("TRANSACTION_AMOUNT");
					if(fileId != null && fileId.longValue() > 0 ) {
						t.setFileId(fileId);
					}
					if(invoiceId != null && invoiceId.length() > 0) {
						t.setInvoiceId(Converter.pad(invoiceId, ' ', 16, Converter.LEFT_JUSTIFIED));
					}
					if(clientId != null && clientId.length() > 0) {
						t.setClientId(Converter.pad(clientId, ' ', 8, Converter.LEFT_JUSTIFIED));
					}
					if(transactionAmount != null) {
						t.setTransactionAmount(transactionAmount);
					}
					t.setInvalidAmountFlag("N");
					t.setTransactionDate(rs.getDate("TRANSACTION_DATE"));
					grouppedTransaction.put(grouppedTransaction.size(), t);
				}
			}
			return grouppedTransaction;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(s != null) {
				s.close();
			}
			if(c != null) {
				c.close();
			}
		}
	}

	/**
	 * This method sends Inquiry request for a <code>Transaction</code>, whose <code>TRANSACTION_ISO</code> will be summarized to table <code>ISO_BATCH_LOG</code>, and reconciliation result will be summarized to table <code>RECONCILE_LOG</code>.
	 * @param t <code>Transaction</code> to process.
	 * @param ibl <code>IsoBatchLog</code> object.
	 * @param processStatus <code>ReconcileLog</code> object.
	 * @param packagerFile
	 * @return <code>boolean</code> value indicating result of Inquiry request.
	 * @throws Exception
	 * @throws ISOException
	 * @throws ParseException
	 */
	public boolean sendInquiryRequest(Transaction t, IsoBatchLog ibl,
			ProcessStatus processStatus) throws Exception {

		Object objReq = null;
		Object objRsp = null;
		boolean isInquirySuccess = false;

		try {
			//Invoking inquiry
			InquiryRequest inqReq = new InquiryRequest();
	        inqReq.setLanguage("01");
	        inqReq.setTrxDateTime(t.getTransactionDate().toString());
	        inqReq.setTransmissionDateTime("");
	        inqReq.setCompanyCode(PaymentConstant.COMPANY_CODE_MANDIRI);
	        inqReq.setChannelID("5");
	        inqReq.setBillKey1(t.getInvoiceId());
	        inqReq.setBillKey2(t.getClientId());
	        //....

	        InquiryResponse2 inqRsp = new BillPaymentService().getBillPaymentServiceSoap().inquiry(inqReq);

			if (PaymentConstant.SUCCESS_RESPONSE_CODE.equals(inqRsp.getStatus().getErrorCode())) { //sukses
				if (inqRsp.getBillAmount().compareTo(t.getTransactionAmount()) != 0) {
					logger.info("InvoiceID: " + t.getInvoiceId()
							+ " can't be processed, PaymentAmount: "
							+ t.getTransactionAmount()
							+ ", AmountOutstanding: " + inqRsp.getBillAmount());
				} else {
					isInquirySuccess = true;
				}
			}

			objReq = inqReq;
			objRsp = inqRsp;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			t.setTransactionStatus(PaymentConstant.TRANSACTION_STATUS_FAILED);
		} finally {

			updateProcessItemStatus(t, objReq, objRsp, processStatus, MESSAGE_TYPE.INQUIRY);
		}
		return isInquirySuccess;
	}

	/**
	 * This method updates <code>PROCESS_ITEM</code> record(s) based on execution information gathered so far.
	 * @param t <code>Transaction</code> currently processed.
	 * @param inqReq <code>ISOMsg</code> sent as request.
	 * @param inqRsp <code>ISOMsg</code> received as response.
	 * @param processStatus Instance of <code>ProcessStatus</code> class, storing information about PaymentProcess cycle.
	 * @param inquiry
	 * @throws Exception
	 * @throws ISOException
	 */
	private void updateProcessItemStatus(Transaction t, Object objReq, Object objRsp
			, ProcessStatus processStatus, MESSAGE_TYPE type) throws Exception {
		//Message lastIso = inqRsp == null ? inqReq : inqRsp;
		String status = null;
		String clientName = null;
		String mti = null;
		String processingCode = null;
		String responseCode = null;
		String reversedFlag = null;
		String invalidAmount = null;
		BigDecimal outstandingAmount = null;
//		String additionalData = null;
//		String invoiceId = null;
//		String clientId = null;
		String periodBegin = null;
		String periodEnd = null;
		String paymentStan = null;
		String paymentRrn = null;
		Map<Long, ProcessItem> mapOfProcessItems = null;
		InquiryRequest inqReq = null;
		InquiryResponse2 inqRsp = null;

		try {
			mapOfProcessItems = getCurrentProcessItems(processStatus, t.getInvoiceId(), t.getClientId(), t.getFileId(), t.getTransactionDate());
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}

		try {
			//klo inquiry
			if (MESSAGE_TYPE.INQUIRY.equals(type)) {
				if (objRsp != null) {
					inqRsp = (InquiryResponse2) objRsp;

//					invoiceId = inqRsp.getBillInfo1();
//					clientId = inqRsp.getBillInfo2();
					clientName = inqRsp.getBillInfo3();
					periodBegin = inqRsp.getBillInfo4();
					periodEnd = inqRsp.getBillInfo5();
					responseCode = inqRsp.getStatus().getErrorCode();

					outstandingAmount = inqRsp.getBillAmount();


					if (responseCode != null && responseCode.equals(PaymentConstant.SUCCESS_RESPONSE_CODE)) {
						if (outstandingAmount.compareTo(t.getTransactionAmount()) != 0) {
							invalidAmount = "Y";
							status = PaymentConstant.TRANSACTION_STATUS_UNPAID;
						}
					}

					if (responseCode != null && responseCode.equals(PaymentConstant.TIMEOUT_RESPONSE_CODE)) {
						responseCode = PaymentConstant.NO_REPLY_RESPONSE_CODE;
					}
				} else { //no reply - timeout
					inqReq = (InquiryRequest) objReq;

//					invoiceId = inqReq.getBillKey1();
//					clientId = inqReq.getBillKey2();
					responseCode = PaymentConstant.NO_REPLY_RESPONSE_CODE;
				}
			}
			// klo reversal
			else if (MESSAGE_TYPE.REVERSAL.equals(type)) {
				reversedFlag = "Y";
				status = PaymentConstant.TRANSACTION_STATUS_FAILED;
			}
			else if (MESSAGE_TYPE.PAYMENT.equals(type)) {
				PaymentRequest payReq = null;
				PaymentResponse2 payRsp = null;
				payReq = (PaymentRequest) objReq;
				payRsp = (PaymentResponse2) objRsp;

				responseCode = payRsp.getStatus().getErrorCode();
				paymentStan = payReq.getTransactionID(); //lastIso.getString(11);
				paymentRrn = payReq.getTransactionID(); //lastIso.getString(37);

				if (responseCode.equals(PaymentConstant.TIMEOUT_RESPONSE_CODE)) {
					reversedFlag = "Y";
					status = PaymentConstant.TRANSACTION_STATUS_FAILED;
				}
			}

			if(t.getTransactionStatus() != null) {
				status = t.getTransactionStatus();
			}

			if(status == null) {
				status = determineStatus(t, inqReq, inqRsp, type);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}

		Iterator<Entry<Long, ProcessItem>> i = mapOfProcessItems.entrySet().iterator();
		while(i.hasNext()) {
			Entry<Long, ProcessItem> entry = i.next();
			Long key = (Long) entry.getKey();
			ProcessItem processItem = (ProcessItem) entry.getValue();

			if(mti != null) {
				processItem.setMti(mti);
			}
			if(processingCode != null) {
				processItem.setProcessingCode(processingCode);
			}
			if(responseCode != null) {
				processItem.setResponseCode(responseCode);
			}
			if(outstandingAmount != null) {
				processItem.setOutstandingAmount(outstandingAmount);
			}
			if(invalidAmount != null) {
				processItem.setInvalidAmount(invalidAmount);
			}
			if(clientName != null) {
				processItem.setClientName(clientName);
			}
			if(periodBegin != null) {
				processItem.setPeriodBegin(periodBegin);
			}
			if(periodEnd != null) {
				processItem.setPeriodEnd(periodEnd);
			}
			if(reversedFlag != null) {
				processItem.setReversedFlag(reversedFlag);
			}
			if(status != null) {
				processItem.setStatus(status);
			}
			if(paymentStan != null) {
				processItem.setPaymentStan(paymentStan);
			}
			if(paymentRrn != null) {
				processItem.setPaymentRrn(paymentRrn);
			}
			mapOfProcessItems.put(key, processItem);

		}
		if(mapOfProcessItems != null && mapOfProcessItems.size() > 0) {
			saveProcessItem(mapOfProcessItems);
		}
	}

	/**
	 * This method determine Status of a <code>Transaction</code>, based on <code>ISOMsg</code> sent,
	 * and <code>ISOMsg</code> received.
	 * @param t <code>Transaction</code> currently processed.
	 * @param inqReq <code>ISOMsg</code> sent as request.
	 * @param inqRsp <code>ISOMsg</code> received as response.
	 * @return
	 * <li>'F' (Failed).
	 * <li>'P' (Paid).
	 * <li>'U' (Unpaid).
	 */
	private String determineStatus(Transaction t, InquiryRequest inqReq, InquiryResponse2 inqRsp, MESSAGE_TYPE type) {
		if(inqRsp == null) {
			return PaymentConstant.TRANSACTION_STATUS_FAILED;
		} else {
			if(inqRsp.getStatus().getErrorCode() == null) {
				return PaymentConstant.TRANSACTION_STATUS_FAILED;
			} else {
				try {
					int intResponseCode = Integer.parseInt(inqRsp.getStatus().getErrorCode());
					switch(intResponseCode) {
						case 0 :
							if (MESSAGE_TYPE.INQUIRY.equals(type)) {
								return null;
							}
							if (MESSAGE_TYPE.PAYMENT.equals(type)) {
								return PaymentConstant.TRANSACTION_STATUS_PAID;
							}
							break;
						case 6:
						case 7:
						case 8:
						case 9:
						case 10:
						case 11:
						case 50:
							return PaymentConstant.TRANSACTION_STATUS_FAILED;
						case 13:
						case 25:
						case 26:
						case 97:
							return PaymentConstant.TRANSACTION_STATUS_UNPAID;
					}
				} catch (NumberFormatException e) {
					logger.error(e);
					return null;
				} catch (Exception e) {
					logger.error(e);
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * This method sends Payment request for a <code>Transaction</code>, whose <code>TRANSACTION_ISO</code> will be summarized to table <code>ISO_BATCH_LOG</code>, and reconciliation result will be summarized to table <code>RECONCILE_LOG</code>.
	 * @param transaction <code>Transaction</code> to process.
	 * @param ibl <code>IsoBatchLog</code> object.
	 * @param processStatus Instance of <code>ProcessStatus</code> class, storing information about a PaymentProcess cycle.
	 * @param packagerFile
	 * @return <code>boolean</code> value indicating result of Payment request.
	 * @throws Exception
	 * @throws ISOException
	 * @throws ParseException
	 */
	public boolean sendPaymentRequest(Transaction t,
			IsoBatchLog ibl, ProcessStatus processStatus) throws Exception {

		boolean timeOut = false;

		PaymentRequest payReq = null;
		PaymentResponse2 payRsp = null;
		Object objReq = null;
		Object objRsp = null;
		boolean isPaymentSuccess = false;

		try {
			//Invoking payment
			payReq = new PaymentRequest();
			payReq.setLanguage("01");
			payReq.setTrxDateTime("");
			payReq.setTransmissionDateTime("");
			payReq.setCompanyCode("");
			payReq.setChannelID("");
			payReq.setBillKey1("");
			payReq.setBillKey2("");
			payReq.setPaidBills(new ArrayOfString());
			payReq.setPaymentAmount("");
			payReq.setCurrency("");
			payReq.setTransactionID("");
	        //....

			payRsp = new BillPaymentService().getBillPaymentServiceSoap().payment(payReq);

			if (PaymentConstant.SUCCESS_RESPONSE_CODE.equals(payRsp.getStatus().getErrorCode())) { //sukses
				isPaymentSuccess = true;
			}

			objReq = payReq;
			objRsp = payRsp;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			t.setTransactionStatus(PaymentConstant.TRANSACTION_STATUS_FAILED);
		} finally {
			updateProcessItemStatus(t, objReq, objRsp, processStatus, MESSAGE_TYPE.PAYMENT);
			if(timeOut) {
				sendReversalRequest(t, ibl, processStatus);
			}
		}
		return isPaymentSuccess;


//		try {
//			urlToPackagerXml = ClassLoader.getSystemClassLoader().getResource("resource/packager/iso87ascii_mandiri.xml");
//			logger.info(className + ", urlToPackager.URI: " + urlToPackagerXml.toURI());
//			packager = new GenericPackager(urlToPackagerXml.openStream());
//			pay = new PayExecution(socketConnection, processStatus, transaction, packager);
//			isoRequest = pay.prepareISORequest();
//
//			logger.info(new Date().toString() + ", processing INVOICE_ID: "
//					+ transaction.getInvoiceId() + ", CLIENT_ID: "
//					+ transaction.getClientId());
//			isoResponse = pay.executeISORequest(isoRequest, ibl);
//			result = pay.isSuccess();
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e);
//			transaction.setTransactionStatus(PaymentConstant.TRANSACTION_STATUS_FAILED);
//		} finally {
//			updateProcessItemStatus(t, isoRequest, isoResponse, processStatus);
//			if(timeOut) {
//				sendReversalRequest(transaction, isoRequest, ibl, processStatus);
//			}
//		}
//		return result;
	}

	/**
	 * This method sends ReversalRequest to reverse an ISOMsg.
	 * @param t Instance of <code>Transaction</code> class currently processed.
	 * @param originalISORequest PaymentRequest in <code>ISOMsg</code> format.
	 * @param isoBatchLog Instance of <code>IsoBatchLog</code> class.
	 * @param processStatus Instance of <code>ProcessStatus</code> class, storing information about current PaymentProcess cycle.
	 * @return <li><code>true</code> if ReversalRequest is success.
	 * <li><code>false</code> if ReversalRequest is not success.
	 * @throws SQLException
	 * @throws ISOException
	 * @throws ParseException
	 */
	private boolean sendReversalRequest(Transaction t,
			IsoBatchLog isoBatchLog,
			ProcessStatus processStatus) throws Exception {
		boolean isReversalSuccess = false;
		Object objReq = null;
		Object objRsp = null;
		try {

			//Invoking inquiry
			ReversalRequest revReq = new ReversalRequest();
			revReq.setLanguage("01");
			revReq.setTrxDateTime("");
			revReq.setTransmissionDateTime("");
			revReq.setCompanyCode("");
			revReq.setChannelID("");
			revReq.setBillKey1("");
			revReq.setBillKey2("");
			revReq.setPaymentAmount("");
			revReq.setCurrency("");
			revReq.setTransactionID("");
	        //....

			ReversalResponse revRsp = new BillPaymentService().getBillPaymentServiceSoap().reverse(revReq);

			if (PaymentConstant.SUCCESS_RESPONSE_CODE.equals(revRsp.getStatus().getErrorCode())) { //sukses
				isReversalSuccess = true;
			}

			objReq = revReq;
			objRsp = revRsp;

//			urlToPackagerXml = ClassLoader.getSystemClassLoader().getResource("resource/packager/iso87ascii_mandiri.xml");
//			logger.info(className + ", urlToPackager.URI: " + urlToPackagerXml.toURI());
//			packager = new GenericPackager(urlToPackagerXml.openStream());
//			ReversalPaymentExecution reversal = new ReversalPaymentExecution(
//					socketConnection, processStatus, t, originalISORequest, packager);
//			reversalISORequest = reversal.prepareISORequest();
//
//			reversalISOResponse = reversal.executeISORequest(
//					reversalISORequest, isoBatchLog);
//			result = reversal.isSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			updateProcessItemStatus(t, objReq, objRsp, processStatus, MESSAGE_TYPE.REVERSAL);
		}
		return isReversalSuccess;
	}

	/**
	 * This method retrieves <code>TRANSACTION</code> under process that have a particular <code>invoiceId</code>, and <code>clientId</code>.
	 * @param processStatus POJO of <code>PROCESS_STATUS</code> table.
	 * @param invoiceId <code>invoiceId</code> to search.
	 * @param clientId <code>clientId</code> to search.
	 * @return <code>Map<Long, Transaction></code> to store the results.
	 * @throws SQLException
	 */
	public Map<Long, Transaction> getCurrentTransactions(ProcessStatus processStatus, String invoiceId, String clientId, Long fileId, Date transactionDate) throws SQLException {
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;
		Map<Long, Transaction> mapOfTransaction = new TreeMap<Long, Transaction>();
		String sql = "SELECT bb.*"
			.concat(" FROM process_item aa, transaction bb")
			.concat(" WHERE bb.TRANSACTION_ID = aa.TRANSACTION_ID")
			.concat(" AND aa.PROCESS_STATUS_ID = ").concat(processStatus.getProcessStatusId().toString());
		try {
			con = dataSource.getConnection();
			s = con.createStatement();
			if(invoiceId != null) {
				sql = sql.concat(" AND NVL(bb.INVOICE_ID, 'NULL') = '").concat(invoiceId.trim()).concat("'");
			}
			if(clientId != null) {
				sql = sql.concat(" AND NVL(bb.CLIENT_ID, 'NULL') = '").concat(clientId.trim()).concat("'");
			}
			if(fileId != null) {
				sql = sql.concat(" AND NVL(bb.FILE_ID, 0) = ").concat(fileId.toString());
			}
			if(transactionDate != null) {
				sql = sql.concat( " AND TO_CHAR(bb.TRANSACTION_DATE, 'ddmmyyyy') = '").concat(ddMMyyyy.format(transactionDate)).concat("'");
			}
			sql = sql.concat(" ORDER BY aa.TRANSACTION_ID");
			logger.debug(sql);

			rs = s.executeQuery(sql);
			while (rs.next()) {
				Transaction t = new Transaction();
				t.setTransactionId(rs.getLong("TRANSACTION_ID"));
				t.setOutstandingAmount(rs.getLong("OUTSTANDING_AMOUNT"));
				t.setErrorDesc(rs.getString("ERROR_DESC"));
				t.setRawTransactionMsg(rs.getString("RAW_TRANSACTION_MSG"));
				t.setMessageType(rs.getString("MSG_TYPE"));

				t.setTransactionType(rs.getString("TRANSACTION_TYPE"));
				t.setTransactionDate(new Date(rs.getDate("TRANSACTION_DATE")
						.getTime()));
				t.setPrevTransactionId(rs.getLong("PREV_TRANSACTION_ID"));
				t.setClientId(rs.getString("CLIENT_ID"));
				t.setClientPhone(rs.getString("CLIENT_PHONE"));

				t.setInvalidAmountFlag(rs.getString("INVALID_AMOUNT_FLAG"));
				t.setInvoiceId(rs.getString("INVOICE_ID"));
				t.setLastMti(rs.getString("LAST_MTI"));
				t.setLastProcessingCode(rs.getString("LAST_PROCESSING_CODE"));
				t.setLastResponseCode(rs.getString("LAST_RESPONSE_CODE"));

				t.setPaymentMethod(rs.getString("PAYMENT_METHOD"));
				t.setTransactionSrc(rs.getString("TRANSACTION_SRC"));
				t.setTransactionAmount(rs.getBigDecimal("TRANSACTION_AMOUNT"));
				t.setTransactionStatus(rs.getString("TRANSACTION_STATUS"));
				t.setFileId(rs.getLong("FILE_ID"));
				mapOfTransaction.put(t.getTransactionId(), t);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if( rs != null) {
				rs.close();
			}
			if( s != null) {
				s.close();
			}
			if( con != null) {
				con.close();
			}
		}

		return mapOfTransaction;
	}

	/**
	 * This method creates <code>RECONCILE_RESULT</code> record using information stored in <code>PROCESS_ITEM</code> record.
	 * @param processStatus Instance of <code>ProcessStatus</code> class.
	 * @param processItem Instance of <code>ProcessItem</code> class, where information will be taken from.
	 * @throws SQLException
	 */
	public void createReconcileResult(ProcessStatus processStatus, ProcessItem processItem) throws SQLException {
		String deactivateOldReconcileResultSql = "UPDATE reconcile_result SET active_flag = 'N'"
			.concat(" WHERE transaction_id=?");
		String createNewReconcileResultSql = "INSERT INTO reconcile_result"
			.concat("(reconcile_result_id, reconcile_msg_id, transaction_id, reconcile_log_id, reconcile_status, ")
			.concat(" settlement_flag, active_flag)")
			.concat(" VALUES(?, ?, ?, ?, ?, ?, ?)");
		String updateProcessItemReconcileResultId =	"UPDATE process_item SET reconcile_result_id=?, status=?"
			.concat(" WHERE process_status_id=?")
			.concat(" AND transaction_id=?");

		Connection con = null;
		PreparedStatement s = null;
		Long reconcileResultId = null;
		Long reconcileMsgId = null;
		String reconcileStatus = null;
		String invalidAmount = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			s = con.prepareStatement(deactivateOldReconcileResultSql);
			s.setLong(1, processItem.getTransactionId());
			s.executeUpdate();
			reconcileResultId = sequenceUtil.generateReconcileResultsId();
			processItem.setReconcileResultId(reconcileResultId);
			s = con.prepareStatement(createNewReconcileResultSql);
			s.setLong(1, reconcileResultId);
			if(processItem.getReversedFlag().equals(PaymentConstant.YES)) {
				reconcileMsgId = Long.parseLong(PaymentConstant.TIMEOUT_RESPONSE_CODE);
				s.setLong(2, reconcileMsgId);
			}
			else {
				if(processItem.getResponseCode() == null) {
					reconcileMsgId = PaymentConstant.INVALID_TRANSACTION_ERROR_CODE;
				}else {
					reconcileMsgId = Long.parseLong(processItem.getResponseCode());
				}

				s.setLong(2, reconcileMsgId);
			}
			if(PaymentConstant.TRANSACTION_STATUS_ERROR.equals(processItem.getStatus())) {
				s.setLong(2, PaymentConstant.INVALID_TRANSACTION_ERROR_CODE);
			}
			invalidAmount = processItem.getInvalidAmount();
			if(PaymentConstant.YES.equals(invalidAmount)) {
				reconcileMsgId = Long.parseLong(PaymentConstant.INVALID_AMOUNT_RESPONSE_CODE);
				s.setLong(2, reconcileMsgId);
			}
			s.setLong(3, processItem.getTransactionId());
			s.setLong(4, processStatus.getReconcileLogId());
			switch(reconcileMsgId.intValue()) {
				case 0: if(processItem.getInvalidAmount() != null && processItem.getInvalidAmount().equals(PaymentConstant.YES)) {
							reconcileStatus = PaymentConstant.RECONCILE_STATUS_PARTIAL_MATCH;
							s.setString(5, reconcileStatus);
						}
					break;
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
						reconcileStatus = PaymentConstant.RECONCILE_STATUS_NOT_AVAILABLE;
						s.setString(5, reconcileStatus);
					break;
				case 13:
				case 26:
				case 97:
					reconcileStatus = PaymentConstant.RECONCILE_STATUS_PARTIAL_MATCH;
					s.setString(5, reconcileStatus);
					break;
				case 25:
				case 66:
					reconcileStatus = PaymentConstant.RECONCILE_STATUS_NOT_MATCH;
					s.setString(5, reconcileStatus);
					break;
			}
			s.setString(6, PaymentConstant.NO);
			s.setString(7, PaymentConstant.NO);
			s.executeUpdate();
			s = con.prepareStatement(updateProcessItemReconcileResultId);
			s.setLong(1, reconcileResultId);
			s.setString(2, processItem.getStatus());
			s.setLong(3, processStatus.getProcessStatusId());
			s.setLong(4, processItem.getTransactionId());
			s.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(s != null) {
				s.close();
			}
			if(con != null) {
				con.close();
			}
		}
	}

	/**
	 * This method saves <code>ProcessItem</code>(s) to <code>PROCESS_ITEM</code> table.
	 * @param mapOfProcessItems <code>java.util.Map</code> storing <code>ProcessItem</code>(s) to be saved.
	 * @throws SQLException
	 */
	public void saveProcessItem(Map<Long, ProcessItem> mapOfProcessItems) throws SQLException {
		Connection con = null;
		PreparedStatement s = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			Iterator<Entry<Long, ProcessItem>> i = mapOfProcessItems.entrySet().iterator();
			while(i.hasNext()) {
				Entry<Long, ProcessItem> entry = (Entry<Long, ProcessItem>) i.next();
				ProcessItem processItem = (ProcessItem) entry.getValue();
				String sql = "UPDATE PROCESS_ITEM SET RECONCILE_RESULT_ID=?, STATUS=?, CLIENT_NAME=?, MTI=?, PROCESSING_CODE=?,"
					.concat(" RESPONSE_CODE=?, REVERSED_FLAG=?, INVALID_AMOUNT=?, PERIOD_BEGIN=?, PERIOD_END=?,")
					.concat(" OUTSTANDING_AMOUNT=?, PAYMENT_STAN=?, PAYMENT_RRN=?, UNIQUE_RECEIPT_CODE=?")
					.concat(" WHERE PROCESS_STATUS_ID=?")
					.concat(" AND TRANSACTION_ID=?");
				logger.debug(sql);
				s = con.prepareStatement(sql);
				Long processStatusId = processItem.getProcessStatusId();
				Long transactionId = processItem.getTransactionId();

				Long reconcileResultId = processItem.getReconcileResultId();
				String status = processItem.getStatus();
				String clientName = processItem.getClientName();
				String mti = processItem.getMti();
				String processingCode = processItem.getProcessingCode();
				String responseCode = processItem.getResponseCode();
				String reversedFlag = processItem.getReversedFlag();
				String invalidAmount = processItem.getInvalidAmount();
				String periodBegin = processItem.getPeriodBegin();
				String periodEnd = processItem.getPeriodEnd();
				BigDecimal outstandingAmount = processItem.getOutstandingAmount();
				String paymentStan = processItem.getPaymentStan();
				String paymentRrn = processItem.getPaymentRrn();
				String uniqueReceiptCode = processItem.getUniqueReceiptCode();
				if(reconcileResultId != null) {
					s.setLong(1, reconcileResultId);
				}
				else {
					s.setNull(1, Types.NUMERIC);
				}
				s.setString(2, status);
				s.setString(3, clientName);
				s.setString(4, mti);
				s.setString(5, processingCode);

				s.setString(6, responseCode);
				s.setString(7, reversedFlag);
				s.setString(8, invalidAmount);
				s.setString(9, periodBegin);
				s.setString(10, periodEnd);
				s.setBigDecimal(11, outstandingAmount);
				s.setString(12, paymentStan);
				s.setString(13, paymentRrn);
				s.setString(14, uniqueReceiptCode);

				s.setLong(15, processStatusId);
				s.setLong(16, transactionId);
				s.addBatch();
			}
			if(i.hasNext()) s.executeBatch();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if (s != null) {
				s.close();
			}
			if(con != null) {
				con.close();
			}
		}
	}

	/**
	 * This method updates <code>TRANSACTION</code> table, using new information from <code>PROCESS_ITEM</code> table and <code>RECONCILE_RESULT</code> table.
	 * @param processStatus Instance of <code>ProcessStatus</code> class, storing information about a PaymentProcess cycle.
	 * @throws SQLException
	 */
	public void updateTransactions(ProcessStatus processStatus) throws SQLException {
		Connection c = null;
		PreparedStatement s = null;
		Map<Long, ProcessItem> mapOfProcessItems = null;
		try {
			c = dataSource.getConnection();
			mapOfProcessItems = getCurrentProcessItems(processStatus, null, null, null, null);
			Iterator<Entry<Long, ProcessItem>> i = mapOfProcessItems.entrySet().iterator();
			while(i.hasNext()) {
				Entry<Long, ProcessItem> entry = (Entry<Long, ProcessItem>) i.next();
				Long transactionId = (Long) entry.getKey();
				ProcessItem processItem = mapOfProcessItems.get(transactionId);
				String sql = "UPDATE transaction"
				.concat(" SET outstanding_amount=?, invalid_amount_flag=?, last_mti=?, last_processing_code=?,")
				.concat(" last_response_code=?, transaction_status=?, client_name=?")
				.concat(" WHERE transaction_id=?");
				logger.debug(sql + transactionId);
				s = c.prepareStatement(sql);
				s.setBigDecimal(1, processItem.getOutstandingAmount());
				s.setString(2, processItem.getInvalidAmount());
				s.setString(3, processItem.getMti());
				s.setString(4, processItem.getProcessingCode());
				s.setString(5, processItem.getResponseCode());
				s.setString(6, processItem.getStatus());
				s.setString(7, processItem.getClientName());
				s.setLong(8, processItem.getTransactionId());
				s.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(s != null) {
				s.close();
			}
			if(c != null) {
				c.close();
			}
		}
	}

	/**
	 * This method summarizes table <code>RECONCILE_RESULT</code>, then updates table <code>RECONCILE_LOG</code> with summary result.
	 * @param reconcileLogId Query Parameter <code>RECONCILE_LOG_ID</code>.
	 * @throws SQLException
	 */
	public void updateReconcileLog(Long reconcileLogId) throws SQLException {
		String sql = "SELECT RECONCILE_LOG_ID, SUM(a.T_REC) AS T_REC, SUM(a.T_FM) AS T_FM, SUM(a.T_PM) AS T_PM, SUM(a.T_NM) AS T_NM, SUM(a.T_NA) AS T_NA";
		sql += " FROM";
		sql += " (SELECT RECONCILE_LOG_ID, COUNT(TRANSACTION_ID) AS T_REC, 0 AS T_FM, 0 AS T_PM, 0 AS T_NM, 0 AS T_NA";
		sql += "  FROM RECONCILE_RESULT WHERE RECONCILE_LOG_ID = "
				+ reconcileLogId;
		sql += "  GROUP BY RECONCILE_LOG_ID";
		sql += "  UNION ALL";
		sql += "  SELECT RECONCILE_LOG_ID, 0 AS T_REC, COUNT(TRANSACTION_ID) AS T_FM, 0 AS T_PM, 0 AS T_NM, 0 AS T_NA";
		sql += "  FROM RECONCILE_RESULT WHERE RECONCILE_LOG_ID = "
				+ reconcileLogId;
		sql += "  AND RECONCILE_STATUS = 'FM'";
		sql += "  GROUP BY RECONCILE_LOG_ID";
		sql += "  UNION ALL";
		sql += "  SELECT RECONCILE_LOG_ID, 0 AS T_REC, 0 AS T_FM, COUNT(TRANSACTION_ID) AS T_PM, 0 AS T_NM, 0 AS T_NA";
		sql += "  FROM RECONCILE_RESULT WHERE RECONCILE_LOG_ID = "
				+ reconcileLogId;
		sql += "  AND RECONCILE_STATUS = 'PM'";
		sql += "  GROUP BY RECONCILE_LOG_ID";
		sql += "  UNION ALL";
		sql += "  SELECT RECONCILE_LOG_ID, 0 AS T_REC, 0 AS T_FM, 0 AS T_PM, COUNT(TRANSACTION_ID) AS T_NM, 0 AS T_NA";
		sql += "  FROM RECONCILE_RESULT WHERE RECONCILE_LOG_ID = "
				+ reconcileLogId;
		sql += "  AND RECONCILE_STATUS = 'NM'";
		sql += "  GROUP BY RECONCILE_LOG_ID";
		sql += "  UNION ALL";
		sql += "  SELECT RECONCILE_LOG_ID, 0 AS T_REC, 0 AS T_FM, 0 AS T_PM, 0 AS T_NM, COUNT(TRANSACTION_ID) AS T_NA";
		sql += "  FROM RECONCILE_RESULT WHERE RECONCILE_LOG_ID = "
				+ reconcileLogId;
		sql += "  AND RECONCILE_STATUS = 'NA'";
		sql += "  GROUP BY RECONCILE_LOG_ID) a";
		sql += " GROUP BY a.RECONCILE_LOG_ID";

		Connection con = null;
		Statement s = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			s = con.createStatement();
			rs = s.executeQuery(sql);
			if (rs.next()) {
				sql = "UPDATE RECONCILE_LOG SET";
				sql += " TOTAL_RECORD = " + rs.getInt("T_REC") + ", ";
				sql += " TOTAL_FULL_MATCH = " + rs.getInt("T_FM") + ", ";
				sql += " TOTAL_PARTIAL_MATCH = " + rs.getInt("T_PM") + ", ";
				sql += " TOTAL_NOT_MATCH = " + rs.getInt("T_NM") + ", ";
				sql += " TOTAL_PENDING = " + rs.getInt("T_NA");
				sql += " WHERE RECONCILE_LOG_ID = " + reconcileLogId;
				s.executeUpdate(sql);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		} finally {
			try {
				s.close();
				con.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * This method summarizes records in table <code>TRANSACTION_ISO</code>, then updates table <code>TRANSACTION_ISO</code> based on those summary results.
	 * @param isoBatchLogId Query Parameter <code>ISO_BATCH_LOG_ID</code>
	 * @throws SQLException
	 */
	public void updateIsoBatchLog(Long isoBatchLogId) throws SQLException {

		String sql = " SELECT dd.ISO_BATCH_LOG_ID AS ISO_BATCH_LOG_ID, SUM(dd.T_REC) AS RECORD, SUM(dd.T_PAID) AS PAID, SUM(dd.T_UP) AS UNPAID, ";
		sql += " SUM(dd.T_FA) AS FAILED, SUM(dd.T_ERR) AS ERROR";
		sql += " FROM (";
		sql += "       SELECT b.ISO_BATCH_LOG_ID, COUNT(a.TRANSACTION_ID) AS T_REC, 0 AS T_PAID, 0 AS T_UP, 0 AS T_FA, 0 AS T_ERR";
		sql += "       FROM  transaction a, (SELECT DISTINCT transaction_id, iso_batch_log_id FROM transaction_iso_map b";
		sql += " 					   WHERE iso_batch_log_id = " + isoBatchLogId + ") b";
		sql += "       WHERE a.transaction_id = b.transaction_id ";
		sql += " 	   GROUP BY b.iso_batch_log_id";
		sql += " 	   UNION ALL";
		sql += " 	   SELECT b.ISO_BATCH_LOG_ID, 0 AS T_REC, COUNT(a.TRANSACTION_ID) AS T_PAID, 0 AS T_UP, 0 AS T_FA, 0 AS T_ERR";
		sql += "       FROM  transaction a, (SELECT DISTINCT transaction_id, iso_batch_log_id FROM transaction_iso_map b";
		sql += " 					         WHERE iso_batch_log_id = " + isoBatchLogId
				+ ") b";
		sql += "       WHERE a.transaction_id = b.transaction_id ";
		sql += "	   AND a.transaction_status = 'P'";
		sql += " 	   GROUP BY b.iso_batch_log_id";
		sql += " 	   UNION ALL";
		sql += "       SELECT b.ISO_BATCH_LOG_ID, 0 AS T_REC, 0 AS T_PAID, COUNT(a.TRANSACTION_ID) AS T_UP, 0 AS T_FA, 0 AS T_ERR";
		sql += "       FROM  transaction a, (SELECT DISTINCT transaction_id, iso_batch_log_id FROM transaction_iso_map b";
		sql += " 					         WHERE iso_batch_log_id = " + isoBatchLogId
				+ ") b";
		sql += "       WHERE a.transaction_id = b.transaction_id ";
		sql += "	   AND a.transaction_status = 'U'";
		sql += " 	   GROUP BY b.iso_batch_log_id";
		sql += " 	   UNION ALL";
		sql += "       SELECT b.ISO_BATCH_LOG_ID, 0 AS T_REC, 0 AS T_PAID, 0 AS T_UP, COUNT(a.TRANSACTION_ID) AS T_FA, 0 AS T_ERR";
		sql += "       FROM  transaction a, (SELECT DISTINCT transaction_id, iso_batch_log_id FROM transaction_iso_map b";
		sql += " 					         WHERE iso_batch_log_id = " + isoBatchLogId
				+ ") b";
		sql += "       WHERE a.transaction_id = b.transaction_id ";
		sql += "	   AND a.transaction_status = 'F'";
		sql += " 	   GROUP BY b.iso_batch_log_id";
		sql += " 	   UNION ALL";
		sql += " 	   SELECT b.ISO_BATCH_LOG_ID, 0 AS T_REC, 0 AS T_PAID, 0 AS T_UP, 0 AS T_FA, COUNT(a.TRANSACTION_ID) AS T_ERR";
		sql += "       FROM  transaction a, (SELECT DISTINCT transaction_id, iso_batch_log_id FROM transaction_iso_map b";
		sql += " 					         WHERE iso_batch_log_id = " + isoBatchLogId
				+ ") b";
		sql += "       WHERE a.transaction_id = b.transaction_id ";
		sql += "	   AND a.transaction_status = 'E'";
		sql += " 	   GROUP BY b.iso_batch_log_id) dd";
		sql += " GROUP BY dd.ISO_BATCH_LOG_ID";
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			s = con.createStatement();
			rs = s.executeQuery(sql);
			sql = null;
			if (rs.next()) {
				sql = "UPDATE ISO_BATCH_LOG SET";
				sql += " TOTAL_RECORD=" + rs.getInt("RECORD") + ", ";
				sql += " TOTAL_PAID=" + rs.getInt("PAID") + ", ";
				sql += " TOTAL_UNPAID=" + rs.getInt("UNPAID") + ", ";
				sql += " TOTAL_FAILED=" + rs.getInt("FAILED") + " ";
				sql += " WHERE ISO_BATCH_LOG_ID=" + isoBatchLogId;
			}
			rs.close();
			s.close();

			s = con.createStatement();
			if(sql != null && sql.length() > 0 ) {
				s.addBatch(sql);
				s.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		} finally {
			try {
				if (s != null) {
					s.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * This method set <code>PROCESS_STATUS.STATUS</code> flag, from 'S' (Started) to 'C' (Completed).
	 * @param processStatus Instance of <code>ProcessStatus</code> class to be modified.
	 * @throws SQLException
	 */
	public void markCompleted(ProcessStatus processStatus) throws SQLException {
		String sql = "UPDATE process_status SET STATUS='C'"
			.concat(" WHERE process_status_id=").concat(processStatus.getProcessStatusId().toString());
		logger.debug(sql);
		Connection c = null;
		Statement s = null;
		try {
			c = dataSource.getConnection();
			s = c.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error(e);
			throw e;
		}
		finally {
			if(s != null) {
				s.close();
			}
			if(c != null) {
				c.close();
			}
		}
	}

	/**
	 * This method changes <code>RECONCILE_RESULT.ACTIVE_FLAG</code> from <code>'N'</code> to <code>'Y'</code>.
	 * @param processStatus Instance of <code>ProcessStatus</code> class, every PaymentProcess cycle will have distinct
	 * <code>ProcessStatus</code> instance to store information about PaymentProcess' status, it's RECONCILE_LOG_ID, and it's ISO_BATCH_LOG_ID.
	 * <br>Only <code>RECONCILE_RESULT</code> records with it's <code>RECONCILE_LOG_ID</code> equal to <code>PROCESS_STATUS.RECONCILE_LOG_ID</code> will be updated by this method.
	 * @throws SQLException
	 */
	public void activateReconcileResult(ProcessStatus processStatus) throws SQLException {
		Connection con = null;
		PreparedStatement s = null;
		try {
			Map<Long, ProcessItem> mapOfProcessItems = getCurrentProcessItems(processStatus, null, null, null, null);
			if(mapOfProcessItems != null && mapOfProcessItems.size() > 0) {
				String index = mapOfProcessItems.keySet().toString().replaceAll("\\[", "(").replaceAll("\\]", ")");
				String sql = "UPDATE reconcile_result SET active_flag='Y'"
					.concat(" WHERE reconcile_log_id=?")
					.concat(" AND transaction_id IN").concat(index);
				logger.debug(sql);
				con = dataSource.getConnection();
				s = con.prepareStatement(sql);
				s.setLong(1, processStatus.getReconcileLogId());
				s.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(s != null) {
				s.close();
			}
			if(con != null) {
				con.close();
			}
		}
	}

	/**
	 * This method perform Reconciliation against data stored in SPECTRA system.
	 * @param processStatus Instance of <code>ProcessStatus</code> class, storing information about a PaymentProcess cycle.
	 * @param needReconcileTransactions <code>java.util.List</code> of <code>TransactionStatus</code> to reconciled against of SPECTRA system.
	 * @throws SQLException
	 */
	public void reconcileWithSpectra(ProcessStatus processStatus, List<TransactionStatus> needReconcileTransactions) throws SQLException {
		Calendar reconcileTime = Calendar.getInstance();
		reconcileTime.add(Calendar.SECOND, ProcessorPayment.getPaymentReversalPeriod());
		logger.info("Reconciliation with SIMF scheduled at: " + reconcileTime.getTime());
		try {
			Thread.sleep(ProcessorPayment.getPaymentReversalPeriod() * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error(e);
		}
		Iterator<TransactionStatus> i = needReconcileTransactions.iterator();
		while(i.hasNext()) {
			TransactionStatus status = (TransactionStatus) i.next();
			String stan = status.getSystemTraceAuditNumber().trim();
			String rrn = status.getRetrievalReferenceNumber().trim();
			if(stan != null && stan.length() > 0 && rrn != null && rrn.length() > 0) {
				TransactionStatus spectraStatus = getTransactionStatus(stan, rrn);
				autoSettlement(processStatus, status, spectraStatus);
			}
		}
	}

	/**
	 * This method performs auto settlement based on information stored in TransactionIssuer, and information stored in SPECTRAsimf.
	 * @param processStatus Instance of <code>ProcessStatus</code> storing information about current PaymentProcess cycle.
	 * @param status TransactionStatus according to TransactionIssuer.
	 * @param spectraStatus
	 * @throws SQLException
	 */
	private void autoSettlement(ProcessStatus processStatus, TransactionStatus status, TransactionStatus spectraStatus) throws SQLException {
		String receiptCode = spectraStatus.getReceiptCode();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Map<Long, ProcessItem> mapOfProcessItems = getCurrentProcessItems(processStatus, null, null, null, status.getTransactionDate());
			Long transactionId = status.getTransactionId();
			ProcessItem processItem = mapOfProcessItems.get(transactionId);
			if(PaymentConstant.TRANSACTION_STATUS_PAID.equals(status.getTransactionStatus()) ||
			   PaymentConstant.TRANSACTION_STATUS_FAILED.equals(status.getTransactionStatus())) {
				updateActiveFlag(connection, transactionId, PaymentConstant.NO);
				ReconcileResult newReconcileResult = createReconcileResult(processStatus, processItem, status, spectraStatus);
				processItem.setReconcileResultId(newReconcileResult.getReconcileResultId());
				if(receiptCode != null && receiptCode.length() > 0) {
					processItem.setUniqueReceiptCode(receiptCode);
				}
				TransactionSettlement transactionSettlement = createTransactionSettlement(processStatus, processItem, newReconcileResult, status, spectraStatus);
				if(transactionSettlement != null) {
					newReconcileResult.setSettlementFlag(PaymentConstant.YES);
				}
				else {
					newReconcileResult.setSettlementFlag(PaymentConstant.NO);
				}
				newReconcileResult.setActiveFlag(PaymentConstant.NO);
				save(connection, processItem, newReconcileResult, transactionSettlement);
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(connection != null) {
				connection.close();
			}
		}
	}

	/**
	 * This method updates <code>ACTIVE_FLAG</code> field in <code>RECONCILE_RESULT</code> table.
	 * @param c <code>java.sql.Connection</code> used to perform queries.
	 * @param transactionId Only <code>RECONCILE_RESULT</code> with <code>TRANSACTION_ID</code> field equal to this value will be changed.
	 * @param newActiveFlag New <code>String</code> value for <code>ACTIVE_FLAG</code> field.
	 * @throws SQLException
	 */
	private void updateActiveFlag(Connection c, Long transactionId, String newActiveFlag) throws SQLException {
		String updateActiveFlagSql = "UPDATE reconcile_result SET active_flag = '"
			.concat(newActiveFlag).concat("'")
			.concat(" WHERE transaction_id = ").concat(transactionId.toString());
		logger.debug(updateActiveFlagSql);
		Statement s = null;
		try {
			if(c == null) {
				c = dataSource.getConnection();
			}
			s = c.createStatement();
			s.executeUpdate(updateActiveFlagSql);
		} catch (SQLException e) {
			logger.error(e);
			throw e;
		}
		finally {
			if(s != null) {
				s.close();
			}
		}
	}

	/**
	 * This method create an instance of <code>ReconcileResult</code> class, after ReconciliationProcess completed.
	 * @param processStatus Instance of <code>ProcessStatus</code> class, storing information about a PaymentProcess cycle.
	 * @param processItem Instance of <code>ProcessItem</code> class, which some information will be taken from.
	 * @param status Instance of <code>TransactionStatus</code> class, storing TransactionIssuer's status of a Transaction.
	 * @param spectraStatus Instance of <code>TransactionStatus</code> class, storing SPECTRAsimf's status of a Transaction.
	 * @return Instance of <code>ReconcileResult</code> class.
	 * @throws SQLException
	 */
	private ReconcileResult createReconcileResult(ProcessStatus processStatus, ProcessItem processItem, TransactionStatus status, TransactionStatus spectraStatus) throws SQLException {
		ReconcileResult reconcileResult = new ReconcileResult();
		int responseCode = Integer.parseInt(processItem.getResponseCode());
		String reversedFlag = processItem.getReversedFlag();
		Long reconcileResultId = null;
		try {
			reconcileResultId = sequenceUtil.generateReconcileResultsId();
			reconcileResult.setReconcileResultId(reconcileResultId);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		reconcileResult.setReconcileMsgId(responseCode);
		reconcileResult.setTransactionId(processItem.getTransactionId());
		reconcileResult.setReconcileLogId(processStatus.getReconcileLogId());
		switch(responseCode) {
			case 0:
					if(reversedFlag != null && reversedFlag.equals(PaymentConstant.YES)) {
						reconcileResult.setReconcileStatus(PaymentConstant.RECONCILE_STATUS_NOT_AVAILABLE);
					}
					else {
						reconcileResult.setReconcileStatus(PaymentConstant.RECONCILE_STATUS_FULL_MATCH);
					}
					break;
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
					reconcileResult.setReconcileStatus(PaymentConstant.RECONCILE_STATUS_NOT_AVAILABLE);
					break;
			case 13:
			case 26:
			case 97:
					reconcileResult.setReconcileStatus(PaymentConstant.RECONCILE_STATUS_PARTIAL_MATCH);
					break;
			case 25:
			case 66:
					reconcileResult.setReconcileStatus(PaymentConstant.RECONCILE_STATUS_NOT_MATCH);
					break;
			case 50:
					reconcileResult.setReconcileStatus(PaymentConstant.RECONCILE_STATUS_NOT_AVAILABLE);
					break;
		}
		return reconcileResult;
	}

	/**
	 * This method creates <code>TRANSACTION_SETTLEMENT</code> record for specified <code>PROCESS_ITEM</code> record.
	 * @param processStatus Instance of <code>ProcessStatus</code> class, storing information about PaymentProcess cycle.
	 * @param processItem Instance of <code>ProcessItem</code> class, where some information will be taken from.
	 * @param newReconcileResult Instance of <code>ReconcileResult</code> class, store information about to be stored to <code>RECONCILE_RESULT</code> table.
	 * @param status Instance of <code>TransactionStatus</code> class, storing TransactionIssuer's status of a Transaction.
	 * @param spectraStatus Instance of <code>TransactionStatus</code> class, storing SPECTRAsimf's status of a Transaction.
	 * @return Instance of <code>TransactionSettlement</code> class, ready to store to database.
	 * @throws SQLException
	 */
	private TransactionSettlement createTransactionSettlement(ProcessStatus processStatus, ProcessItem processItem, ReconcileResult newReconcileResult, TransactionStatus status, TransactionStatus spectraStatus) throws SQLException {
		TransactionSettlement transactionSettlement = null;
		if(PaymentConstant.SIMF_STATUS_PAID.equals(spectraStatus.getTransactionStatus())) {
			String receiptCode = spectraStatus.getReceiptCode();
			transactionSettlement = new TransactionSettlement();
			try {
				transactionSettlement.setSettlementId(sequenceUtil.generateTransactionSettlementId());
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(e);
				throw e;
			}
			transactionSettlement.setReconcileResultId(processItem.getReconcileResultId());
			transactionSettlement.setTransactionId(processItem.getTransactionId());
			transactionSettlement.setSettlementDate(new Date());
			transactionSettlement.setSettlementRemarks(PaymentConstant.SETTLEMENT_REMARKS);
			transactionSettlement.setCreatedWho(PaymentConstant.AUTO_SETTLEMENT_CREATOR);

			transactionSettlement.setPrevAmount(processItem.getOutstandingAmount());
			transactionSettlement.setPrevInvoiceId(status.getInvoiceId());
			transactionSettlement.setPrevClientId(status.getClientId());
			transactionSettlement.setPrevTransactionStatus(processItem.getStatus());
			if(PaymentConstant.TRANSACTION_STATUS_PAID.equals(status.getTransactionStatus())) {
				transactionSettlement.setSettlementMsgId(PaymentConstant.SYSTEM_MESSAGE_NO_SETTLEMENT_REQUIRED);
			}
			if(PaymentConstant.TRANSACTION_STATUS_FAILED.equals(status.getTransactionStatus())) {
				processItem.setStatus(spectraStatus.getTransactionStatus());
				if(receiptCode != null) {
					processItem.setUniqueReceiptCode(receiptCode);
				}
				newReconcileResult.setReconcileStatus(PaymentConstant.RECONCILE_STATUS_FULL_MATCH);
				newReconcileResult.setReconcileMsgId(Integer.parseInt(PaymentConstant.SYS_MSG_ID_SUCCESS));
				transactionSettlement.setSettlementMsgId(PaymentConstant.SYSTEM_MESSAGE_SETTLEMENT_SUBMITTED);
			}
		}
		else {
			if(PaymentConstant.TRANSACTION_STATUS_PAID.equals(status.getTransactionStatus())) {
				transactionSettlement = new TransactionSettlement();
				try {
					transactionSettlement.setSettlementId(sequenceUtil.generateTransactionSettlementId());
				} catch (SQLException e) {
					e.printStackTrace();
					logger.error(e);
					throw e;
				}
				transactionSettlement.setReconcileResultId(processItem.getReconcileResultId());
				transactionSettlement.setTransactionId(processItem.getTransactionId());
				transactionSettlement.setSettlementDate(new Date());
				transactionSettlement.setSettlementRemarks(PaymentConstant.SETTLEMENT_REMARKS);
				transactionSettlement.setCreatedWho(PaymentConstant.AUTO_SETTLEMENT_CREATOR);
				transactionSettlement.setPrevAmount(processItem.getOutstandingAmount());
				transactionSettlement.setPrevInvoiceId(status.getInvoiceId());
				transactionSettlement.setPrevClientId(status.getClientId());
				transactionSettlement.setPrevTransactionStatus(processItem.getStatus());

				processItem.setStatus(PaymentConstant.TRANSACTION_STATUS_FAILED);
				newReconcileResult.setReconcileStatus(PaymentConstant.RECONCILE_STATUS_NOT_AVAILABLE);
				newReconcileResult.setReconcileMsgId(Integer.parseInt(PaymentConstant.SYS_MSG_ID_DATA_MISMATCH));
				transactionSettlement.setSettlementMsgId(PaymentConstant.SYSTEM_MESSAGE_SETTLEMENT_SUBMITTED);
				logger.fatal("-------------------------------------------------------------------");
				logger.fatal("Please Reconcile Transaction with TransactionId " + processItem.getTransactionId() + " Manually");
				logger.fatal("");
			}
			if(processItem.getReversedFlag().equals(PaymentConstant.YES)) {
				processItem.setResponseCode(PaymentConstant.TIMEOUT_RESPONSE_CODE);
				newReconcileResult.setReconcileMsgId(Integer.parseInt(PaymentConstant.TIMEOUT_RESPONSE_CODE));
			}
		}
		return transactionSettlement;
	}

	/**
	 * This method update information stored in instance of <code>ProcessItem</code> class to <code>PROCESS_ITEM</code> table,
	 * insert information stored in <code>ReconcileResult</code> class to <code>RECONCILE_RESULT</code> table,
	 * and insert information stored in <code>TransactionSettlement</code> class to <code>TRANSACTION_SETTLEMENT</code> table.
	 * @param connection <code>java.sql.Connection</code> to execute database update.
	 * @param processItem Instance of <code>ProcessItem</code> class of which information will be saved to <code>PROCESS_ITEM</code> table.
	 * @param newReconcileResult Instance of <code>ReconcileResult</code> class of which information will be saved to <code>RECONCILE_RESULT</code> table.
	 * @param newTransactionSettlement Instance of <code>TransactionSettlement</code> class of which information will be saved to <code>TRANSACTION_SETTLEMENT</code> table.
	 * @throws SQLException
	 */
	private void save(Connection connection, ProcessItem processItem, ReconcileResult newReconcileResult, TransactionSettlement newTransactionSettlement) throws SQLException {
		String insertReconcileResultSql = "INSERT INTO reconcile_result(reconcile_result_id, reconcile_msg_id, transaction_id, reconcile_log_id, reconcile_status,"
			.concat(" settlement_flag, active_flag)")
			.concat(" VALUES(?, ?, ?, ?, ?, ?, ?)");
		String insertTransactionSettlementSql = "INSERT INTO transaction_settlement"
			.concat("(settlement_id, reconcile_result_id, settlement_msg_id, transaction_id, settlement_date, ")
			.concat(" settlement_remarks, created_who, prev_amount, prev_invoice_id, prev_client_id, prev_transaction_status)")
			.concat(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?)");
		String updateProcessItemSql = "UPDATE process_item SET reconcile_result_id=?, unique_receipt_code=?, status=?"
							 .concat(" WHERE process_status_id=?")
							 .concat(" AND transaction_id=?");
		try {
			PreparedStatement s = connection.prepareStatement(insertReconcileResultSql);
			s.setLong(1, newReconcileResult.getReconcileResultId());
			s.setLong(2, newReconcileResult.getReconcileMsgId());
			s.setLong(3, newReconcileResult.getTransactionId());
			s.setLong(4, newReconcileResult.getReconcileLogId());
			s.setString(5, newReconcileResult.getReconcileStatus());
			s.setString(6, newReconcileResult.getSettlementFlag());
			s.setString(7, newReconcileResult.getActiveFlag());
			s.executeUpdate();
			if(newTransactionSettlement != null) {
				s = connection.prepareStatement(insertTransactionSettlementSql);
				s.setLong(1, newTransactionSettlement.getSettlementId());
				s.setLong(2, newTransactionSettlement.getReconcileResultId());
				s.setLong(3, newTransactionSettlement.getSettlementMsgId());
				s.setLong(4, newTransactionSettlement.getTransactionId());
				s.setDate(5, new java.sql.Date(newTransactionSettlement.getSettlementDate().getTime()));

				s.setString(6, newTransactionSettlement.getSettlementRemarks());
				s.setString(7, newTransactionSettlement.getCreatedWho());
				s.setBigDecimal(8, newTransactionSettlement.getPrevAmount());
				s.setString(9, newTransactionSettlement.getPrevInvoiceId());
				s.setString(10, newTransactionSettlement.getPrevClientId());

				s.setString(11, newTransactionSettlement.getPrevTransactionStatus());
				s.executeUpdate();
			}
			s = connection.prepareStatement(updateProcessItemSql);
			s.setLong(1, processItem.getReconcileResultId());
			s.setString(2, processItem.getUniqueReceiptCode());
			s.setString(3, processItem.getStatus());
			s.setLong(4, processItem.getProcessStatusId());
			s.setLong(5, processItem.getTransactionId());
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}

	}

	/**
	 * This method retrieves currently processed <code>ProcessItem</code> instance.
	 * @param processStatus Instance of <code>ProcessStatus</code> class, storing information of current PaymentProcess cycle.
	 * @param invoiceId Query parameter, retrieves only <code>Transaction</code>(s) with InvoiceId equal to specified value.
	 * @param clientId Query parameter, retrieves only <code>Transaction</code>(s) with ClientId equal to specified value.
	 * @param fileId Query parameter, retrieves only <code>Transaction</code>(s) with FileId equal to specified value.
	 * @param transactionDate Query parameter, retrieves only <code>Transaction</code>(s) with TransactionDate equal to specified value.
	 * @return <code>java.util.Map</code> of currently processed <code>ProcessItem</code> class.
	 * @throws SQLException
	 */
	public Map<Long, ProcessItem> getCurrentProcessItems(ProcessStatus processStatus, String invoiceId, String clientId, Long fileId, Date transactionDate) throws SQLException {
		Map<Long, ProcessItem> mapOfProcessItems = new TreeMap<Long, ProcessItem>();
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;
		String sql = "SELECT aa.*, bb.*"
			.concat(" FROM process_item aa, transaction bb")
			.concat(" WHERE aa.TRANSACTION_ID = bb.TRANSACTION_ID")
			.concat(" AND aa.PROCESS_STATUS_ID = ").concat(processStatus.getProcessStatusId().toString());

		try {
			con = dataSource.getConnection();
			s = con.createStatement();
			if(invoiceId != null) {
				sql = sql.concat(" AND NVL(bb.INVOICE_ID, 'NULL') = '").concat(invoiceId.trim()).concat("'");
			}
			if(clientId != null) {
				sql = sql.concat(" AND NVL(bb.CLIENT_ID, 'NULL') = '").concat(clientId.trim()).concat("'");
			}
			if(fileId != null) {
				sql = sql.concat(" AND NVL(bb.FILE_ID, 0) = " + fileId.longValue());
			}
			if(transactionDate != null) {
				sql = sql.concat( " AND TO_CHAR(bb.TRANSACTION_DATE, 'ddmmyyyy') = '").concat(ddMMyyyy.format(transactionDate)).concat("'");
			}
			sql = sql.concat(" ORDER BY aa.TRANSACTION_ID");
			logger.debug(sql);

			rs = s.executeQuery(sql);
			while(rs.next()) {
				ProcessItem processItem = new ProcessItem();
				processItem.setProcessStatusId(rs.getLong("PROCESS_STATUS_ID"));
				processItem.setTransactionId(rs.getLong("TRANSACTION_ID"));
				Long reconcileResultId = rs.getLong("RECONCILE_RESULT_ID");
				if(reconcileResultId != null && reconcileResultId.longValue() > 0) {
					processItem.setReconcileResultId(reconcileResultId);
				}
				String status = rs.getString("STATUS");
				if(status != null) {
					processItem.setStatus(status);
				}

				String clientName = rs.getString("CLIENT_NAME");
				if(clientName != null) {
					processItem.setClientName(clientName);
				}
				String mti = rs.getString("MTI");
				if(mti != null) {
					processItem.setMti(mti);
				}
				String processingCode = rs.getString("PROCESSING_CODE");
				if(processingCode != null) {
					processItem.setProcessingCode(processingCode);
				}
				String responseCode = rs.getString("RESPONSE_CODE");
				if(responseCode != null) {
					processItem.setResponseCode(responseCode);
				}
				String reversedFlag = rs.getString("REVERSED_FLAG");
				if(reversedFlag != null) {
					processItem.setReversedFlag(reversedFlag);
				}

				String invalidAmount = rs.getString("INVALID_AMOUNT");
				if(invalidAmount != null) {
					processItem.setInvalidAmount(invalidAmount);
				}
				String periodBegin = rs.getString("PERIOD_BEGIN");
				if(periodBegin != null) {
					processItem.setPeriodBegin(periodBegin);
				}
				String periodEnd = rs.getString("PERIOD_END");
				if(periodEnd != null) {
					processItem.setPeriodEnd(periodEnd);
				}
				BigDecimal outstandingAmount = rs.getBigDecimal("OUTSTANDING_AMOUNT");
				if(outstandingAmount != null) {
					processItem.setOutstandingAmount(outstandingAmount);
				}
				String paymentStan = rs.getString("PAYMENT_STAN");
				if(paymentStan != null) {
					processItem.setPaymentStan(paymentStan);
				}

				String paymentRrn = rs.getString("PAYMENT_RRN");
				if(paymentRrn != null) {
					processItem.setPaymentRrn(paymentRrn);
				}
				String uniqueReceiptCode = rs.getString("UNIQUE_RECEIPT_CODE");
				if(uniqueReceiptCode != null) {
					processItem.setUniqueReceiptCode(uniqueReceiptCode);
				}
				fileId = rs.getLong("FILE_ID");
				if(fileId != null && fileId.longValue() > 0) {
					processItem.setFileId(fileId);
				}
				mapOfProcessItems.put(processItem.getTransactionId(), processItem);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if( s != null) {
				s.close();
			}
			if(con != null) {
				con.close();
			}
		}
		return mapOfProcessItems;
	}

	public long saveFileTransactionLog(FileTransactionReceive fileTransac,String status, String bankName) throws SQLException {

		String fileFrom = fileTransac.getTypeListener();
		String fileName = fileTransac.getFileNameTransaction();
		byte[] fileData = fileTransac.getContentFileTransaction();
		int fileSize = fileData.length;
		Calendar transactionDate = fileTransac.getTransactionDate();

		long seqListenerLogId = sequenceUtil.generateListenerLogId();

		// create obj table Listener Log and set status = new_status
		ListenerLog listenerLog = new ListenerLog();
		listenerLog.setListenerLogId(seqListenerLogId);
		listenerLog.setListenerStatus(status);
		listenerLog.setSourceType(fileFrom);
		listenerLog.setTransactionDate(new Timestamp(transactionDate
				.getTimeInMillis()));
		listenerLog.setFilenameTransaction(fileName);
		listenerLog.setBankName(bankName);

		// insert record to listener_log
		saveListenerLog(listenerLog);

		long seqTransactionFileId = sequenceUtil.generateTransactionFileId();

		// create obj table transaction_file
		TransactionFile transactionFile = new TransactionFile();
		transactionFile.setFileId(seqTransactionFileId);
		transactionFile.setListenerLogId(seqListenerLogId);
		transactionFile.setFileName(fileName);
		transactionFile.setFileSize(fileSize);
		transactionFile.setData(fileData);

		// (add : 24-01-07) write username and 'koreksi' flag for uploading file
		// transaction
		String username = fileTransac.getUserName();
		String koreksiFlag = fileTransac.getKoreksiFlag();

		transactionFile.setUserName(username);
		transactionFile.setKoreksiFlag(koreksiFlag);

		// insert record to transaction_file
		saveTransactionFile(transactionFile);

		return seqTransactionFileId;
	}

	public void saveListenerLog(ListenerLog listenerLog) throws SQLException {
		final String sql = "INSERT INTO listener_log(LISTENER_LOG_ID, RECEIVED_DATE, LISTENER_STATUS, SOURCE_TYPE, TRANSACTION_DATE, FILENAME_TRANSACTION, BANK_NAME)"
					.concat(" VALUES(?, ?, ?, ?, ?, ?, ?)");
		Connection con = null;
		PreparedStatement s = null;
		try {
			Calendar cal = Calendar.getInstance();
			java.sql.Timestamp now = new java.sql.Timestamp(cal.getTimeInMillis());

			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setLong(1, listenerLog.getListenerLogId());
			s.setTimestamp(2, now);
			s.setString(3, listenerLog.getListenerStatus());
			s.setString(4, listenerLog.getSourceType());
			s.setDate(5, new java.sql.Date(listenerLog.getTransactionDate().getTime()));
			s.setString(6, listenerLog.getFilenameTransaction());
			s.setString(7, listenerLog.getBankName());
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(s != null) {
				s.close();
			}
			if(con != null) {
				con.close();
			}
		}
	}

	public void saveTransactionFile(TransactionFile transactionFile) throws SQLException {
		final String sql = "INSERT INTO transaction_file(file_id, data, filename, file_size, listener_log_id, user_upload, koreksi_flag)"
					.concat(" VALUES(?, ?, ?, ?, ?, ?, ?)");
		Connection con = null;
		PreparedStatement s = null;
		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setLong(1, transactionFile.getFileId());
			s.setBytes(2, transactionFile.getData());
			s.setString(3, transactionFile.getFileName());
			s.setInt(4, transactionFile.getFileSize());
			s.setLong(5, transactionFile.getListenerLogId());
			s.setString(6, transactionFile.getUserName());
			s.setString(7, transactionFile.getKoreksiFlag());
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(s != null) {
				s.close();
			}
			if(con != null) {
				con.close();
			}
		}
	}

	public void saveParserLog(ParserLog parserLog) throws SQLException {

		final String sql = "INSERT INTO parser_log(PARSER_LOG_ID, PARSE_DATE, PARSE_STATUS, FILE_ID, TOTAL_FAILED, TOTAL_PROCESSED, TOTAL_RECORD, TOTAL_HOST_TO_HOST, TOTAL_NOT_HOST_TO_HOST)"
				.concat(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
		Connection con = null;
		PreparedStatement s = null;

		try {
			Calendar cal = Calendar.getInstance();
			java.sql.Timestamp now = new java.sql.Timestamp(cal.getTimeInMillis());

			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setLong(1, parserLog.getParserLogId());
			s.setTimestamp(2, now);
			s.setString(3, parserLog.getParseStatus());
			s.setLong(4, parserLog.getFileId());
			s.setInt(5, parserLog.getTotalFailed());
			s.setInt(6, parserLog.getTotalProcessed());
			s.setInt(7, parserLog.getTotalRecord());
			s.setInt(8, parserLog.getTotalSuccessHostToHost());
			s.setInt(9, parserLog.getTotalSuccessNotHostToHost());
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		} finally {
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		}
	}

	/**
	 * This method save instance(s) of <code>Transaction</code> class to <code>TRANSACTION</code> table.
	 * <br> This method used only for JUnit testing purposes.
	 * @param transactions Array of <code>Transaction</code> instances.
	 * @throws SQLException
	 */
	public void saveTransactions(Transaction transaction) throws SQLException {
		final String sql = "INSERT INTO transaction"
			.concat("(transaction_id, outstanding_amount, error_desc, raw_transaction_msg, msg_type,")
			.concat("transaction_type, transaction_date, prev_transaction_id, client_id, client_phone,")
			.concat("invalid_amount_flag, invoice_id, last_mti, last_processing_code, last_response_code,")
			.concat("payment_method, transaction_src, transaction_amount, transaction_status, file_id,")
			.concat("client_name, need_reconcile, transaction_code, payment_channel, payment_type)")
			.concat(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		Connection con = null;
		PreparedStatement s = null;
		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			Long transactionId = transaction.getTransactionId();
			Long outstandingAmount = transaction.getOutstandingAmount();
			String errorDesc = transaction.getErrorDesc();
			String rawTransactionMsg = transaction.getRawTransactionMsg();
			String msgType = transaction.getMessageType();

			String transactionType = transaction.getTransactionType();
			java.util.Date transactionDate = transaction.getTransactionDate();
			Long prevTransactionId = transaction.getPrevTransactionId();
			String clientId = transaction.getClientId();
			String clientPhone = transaction.getClientPhone();

			String invalidAmountFlag = transaction.getInvalidAmountFlag();
			String invoiceId = transaction.getInvoiceId();
			String lastMti = transaction.getLastMti();
			String lastProcessingCode = transaction.getLastProcessingCode();
			String lastResponseCode = transaction.getLastResponseCode();

			String paymentMethod = transaction.getPaymentMethod();
			String transactionSrc = transaction.getTransactionSrc();
			BigDecimal transactionAmount = transaction
					.getTransactionAmount();
			String transactionStatus = transaction.getTransactionStatus();
			Long fileId = transaction.getFileId();

			String clientName = transaction.getClientName();
			String needReconcile = transaction.getNeedReconcile();
			
			//add 05-01-2015
			String transactionCode = transaction.getTransactionCode();
			
			//add 23-10-2015
			String paymentChannel = transaction.getPaymentChannel();
			
			//add 14-12-2015
			String paymentType = transaction.getPaymentType();

			if (transactionId != null && transactionId.longValue() > 0) {
				s.setLong(1, transactionId);
			} else {
				s.setNull(1, Types.NUMERIC);
			}
			if (outstandingAmount != null) {
				s.setLong(2, outstandingAmount);
			} else {
				s.setNull(2, Types.NUMERIC);
			}
			if (errorDesc != null) {
				s.setString(3, errorDesc);
			} else {
				s.setNull(3, Types.VARCHAR);
			}
			if (rawTransactionMsg != null) {
				s.setString(4, rawTransactionMsg);
			} else {
				s.setNull(4, Types.VARCHAR);
			}
			if (msgType != null) {
				s.setString(5, msgType);
			} else {
				s.setNull(5, Types.VARCHAR);
			}

			if (transactionType != null) {
				s.setString(6, transactionType);
			} else {
				s.setNull(6, Types.VARCHAR);
			}
			if (transactionDate != null) {
				s.setDate(7, new java.sql.Date(transactionDate.getTime()));
			} else {
				s.setNull(7, Types.DATE);
			}
			if (prevTransactionId != null && prevTransactionId.longValue() > 0) {
				s.setLong(8, prevTransactionId);
			} else {
				s.setNull(8, Types.NUMERIC);
			}
			if (clientId != null) {
				s.setString(9, clientId);
			} else {
				s.setNull(9, Types.VARCHAR);
			}
			if (clientPhone != null) {
				s.setString(10, clientPhone);
			} else {
				s.setNull(10, Types.VARCHAR);
			}

			if (invalidAmountFlag != null) {
				s.setString(11, invalidAmountFlag);
			} else {
				s.setNull(11, Types.VARCHAR);
			}
			if (invoiceId != null) {
				s.setString(12, invoiceId);
			} else {
				s.setNull(12, Types.VARCHAR);
			}
			if (lastMti != null) {
				s.setString(13, lastMti);
			} else {
				s.setNull(13, Types.VARCHAR);
			}
			if (lastProcessingCode != null) {
				s.setString(14, lastProcessingCode);
			} else {
				s.setNull(14, Types.VARCHAR);
			}
			if (lastResponseCode != null) {
				s.setString(15, lastResponseCode);
			} else {
				s.setNull(15, Types.VARCHAR);
			}

			if (paymentMethod != null) {
				s.setString(16, paymentMethod);
			} else {
				s.setNull(16, Types.VARCHAR);
			}
			if (transactionSrc != null) {
				s.setString(17, transactionSrc);
			} else {
				s.setNull(17, Types.VARCHAR);
			}
			if (transactionAmount != null) {
				s.setBigDecimal(18, transactionAmount);
			} else {
				s.setNull(18, Types.NUMERIC);
			}
			if (transactionStatus != null) {
				s.setString(19, transactionStatus);
			} else {
				s.setNull(19, Types.VARCHAR);
			}
			if (fileId != null && fileId.longValue() > 0) {
				s.setLong(20, fileId);
			} else {
				s.setNull(20, Types.NUMERIC);
			}

			if (clientName != null) {
				s.setString(21, clientName);
			} else {
				s.setNull(21, Types.VARCHAR);
			}
			if (needReconcile != null) {
				s.setString(22, needReconcile);
			} else {
				s.setNull(22, Types.NUMERIC);
			}
			
			//add 05-01-2015
			if (transactionCode != null) {
				s.setString(23, transactionCode);
			} else {
				s.setNull(23, Types.VARCHAR);
			}
			
			//add 23-10-2015
			if (paymentChannel != null) {
				s.setString(24, paymentChannel);
			} else {
				s.setNull(24, Types.VARCHAR);
			}
			
			//add 14-12-2015
			if (paymentType != null) {
				s.setString(25, paymentType);
			} else {
				s.setNull(25, Types.VARCHAR);
			}
			
			s.execute();
		} catch (SQLException e) {
			logger.error(e);
			throw e;
		}
		finally {
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		}
	}
	
	/**
	 * This method save instance(s) of <code>Transaction</code> class to <code>TRANSACTION</code> table.
	 * <br> This method used only for JUnit testing purposes.
	 * @param transactions Array of <code>Transaction</code> instances.
	 * @throws SQLException
	 */
	public void saveTransactionsSkorReor(Transaction transaction) throws SQLException {
		
		logger.info("###Enter saveTransactionsSkorReor function ###");
		
		final String sql = "INSERT INTO transaction"
			.concat("(transaction_id, outstanding_amount, error_desc, raw_transaction_msg, msg_type,")
			.concat("transaction_type, transaction_date, prev_transaction_id, client_id, client_phone,")
			.concat("invalid_amount_flag, invoice_id, last_mti, last_processing_code, last_response_code,")
			.concat("payment_method, transaction_src, transaction_amount, transaction_status, file_id,")
			.concat("client_name, need_reconcile, transaction_code, payment_channel)")
			.concat(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		Connection con = null;
		PreparedStatement s = null;
		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			Long transactionId = transaction.getTransactionId();
			Long outstandingAmount = transaction.getOutstandingAmount();
			String errorDesc = transaction.getErrorDesc();
			String rawTransactionMsg = transaction.getRawTransactionMsg();
			String msgType = transaction.getMessageType();

			String transactionType = transaction.getTransactionType();
			java.util.Date transactionDate = transaction.getTransactionDate();
			Long prevTransactionId = transaction.getPrevTransactionId();
			String clientId = transaction.getClientId();
			String clientPhone = transaction.getClientPhone();

			String invalidAmountFlag = transaction.getInvalidAmountFlag();
			String invoiceId = transaction.getInvoiceId();
			String lastMti = transaction.getLastMti();
			String lastProcessingCode = transaction.getLastProcessingCode();
			String lastResponseCode = transaction.getLastResponseCode();

			String paymentMethod = transaction.getPaymentMethod();
			String transactionSrc = transaction.getTransactionSrc();
			BigDecimal transactionAmount = transaction
					.getTransactionAmount();
			String transactionStatus = transaction.getTransactionStatus();
			Long fileId = transaction.getFileId();

			String clientName = transaction.getClientName();
			String needReconcile = transaction.getNeedReconcile();
			
			//Only use for reor and Skor
			String transactionCode = transaction.getTransactionCode();
			
			//add 23-10-2014
			String paymentChannel = transaction.getPaymentChannel();

			if (transactionId != null && transactionId.longValue() > 0) {
				s.setLong(1, transactionId);
			} else {
				s.setNull(1, Types.NUMERIC);
			}
			if (outstandingAmount != null) {
				s.setLong(2, outstandingAmount);
			} else {
				s.setNull(2, Types.NUMERIC);
			}
			if (errorDesc != null) {
				s.setString(3, errorDesc);
			} else {
				s.setNull(3, Types.VARCHAR);
			}
			if (rawTransactionMsg != null) {
				s.setString(4, rawTransactionMsg);
			} else {
				s.setNull(4, Types.VARCHAR);
			}
			if (msgType != null) {
				s.setString(5, msgType);
			} else {
				s.setNull(5, Types.VARCHAR);
			}

			if (transactionType != null) {
				s.setString(6, transactionType);
			} else {
				s.setNull(6, Types.VARCHAR);
			}
			if (transactionDate != null) {
				s.setDate(7, new java.sql.Date(transactionDate.getTime()));
			} else {
				s.setNull(7, Types.DATE);
			}
			if (prevTransactionId != null && prevTransactionId.longValue() > 0) {
				s.setLong(8, prevTransactionId);
			} else {
				s.setNull(8, Types.NUMERIC);
			}
			if (clientId != null) {
				s.setString(9, clientId);
			} else {
				s.setNull(9, Types.VARCHAR);
			}
			if (clientPhone != null) {
				s.setString(10, clientPhone);
			} else {
				s.setNull(10, Types.VARCHAR);
			}

			if (invalidAmountFlag != null) {
				s.setString(11, invalidAmountFlag);
			} else {
				s.setNull(11, Types.VARCHAR);
			}
			if (invoiceId != null) {
				s.setString(12, invoiceId);
			} else {
				s.setNull(12, Types.VARCHAR);
			}
			if (lastMti != null) {
				s.setString(13, lastMti);
			} else {
				s.setNull(13, Types.VARCHAR);
			}
			if (lastProcessingCode != null) {
				s.setString(14, lastProcessingCode);
			} else {
				s.setNull(14, Types.VARCHAR);
			}
			if (lastResponseCode != null) {
				s.setString(15, lastResponseCode);
			} else {
				s.setNull(15, Types.VARCHAR);
			}

			if (paymentMethod != null) {
				s.setString(16, paymentMethod);
			} else {
				s.setNull(16, Types.VARCHAR);
			}
			if (transactionSrc != null) {
				s.setString(17, transactionSrc);
			} else {
				s.setNull(17, Types.VARCHAR);
			}
			if (transactionAmount != null) {
				s.setBigDecimal(18, transactionAmount);
			} else {
				s.setNull(18, Types.NUMERIC);
			}
			if (transactionStatus != null) {
				s.setString(19, transactionStatus);
			} else {
				s.setNull(19, Types.VARCHAR);
			}
			if (fileId != null && fileId.longValue() > 0) {
				s.setLong(20, fileId);
			} else {
				s.setNull(20, Types.NUMERIC);
			}

			if (clientName != null) {
				s.setString(21, clientName);
			} else {
				s.setNull(21, Types.VARCHAR);
			}
			if (needReconcile != null) {
				s.setString(22, needReconcile);
			} else {
				s.setNull(22, Types.NUMERIC);
			}
			
			//Only use For Reor And Skor
			if (transactionCode != null) {
				s.setString(23, transactionCode);
			} else {
				s.setNull(23, Types.VARCHAR);
			}
			
			//add 23-10-2014
			if (paymentChannel != null) {
				s.setString(24, paymentChannel);
			} else {
				s.setNull(24, Types.VARCHAR);
			}
			
			s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		}
	}

	public boolean isProcessedFileTransac(Calendar transactionDate, String filename) throws SQLException {
		
		java.util.Date transDate = transactionDate.getTime();
		String listnerStatus = PostelConstant.STATUS_VALID;
		
		logger.info("mandiri, Search in table listener_log is this file already process, with parameter :  " );
		logger.info("mandiri, TRANSACTION_DATE = " + transactionDate.getTime());
		logger.info("mandiri, FILENAME_TRANSACTION = " + filename);
		logger.info("mandiri, LISTENER_STATUS = " + listnerStatus);
		
		String sql = "SELECT COUNT(*) FROM LISTENER_LOG WHERE LISTENER_STATUS = ? " + 
				 "AND TRANSACTION_DATE = ? " + 
				 "AND FILENAME_TRANSACTION = ?";
		
		Connection con = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		boolean flag = false;

		try {
			con = dataSource.getConnection();
			stat = con.prepareStatement(sql);
			stat.setString(1, listnerStatus);
			stat.setDate(2, new java.sql.Date(transDate.getTime()));
			stat.setString(3, filename);
			rs = stat.executeQuery();
			if(rs.next()) {
				long count = rs.getLong(1);
				if(count > 0){
					flag = true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if( stat != null) {
				stat.close();
			}
			if(con != null) {
				con.close();
			}
		}
		
		logger.info("mandiri, Result : File already process = " + flag);
		
		return flag;
	}

	public boolean isEmptyTableFileTransac() throws SQLException {

		String sql = "SELECT COUNT(*) FROM LISTENER_LOG WHERE LISTENER_STATUS=?";
		Connection con = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		boolean flag = false;

		String listnerStatus = PostelConstant.STATUS_VALID;

		try {
			con = dataSource.getConnection();
			stat = con.prepareStatement(sql);
			stat.setString(1, listnerStatus);
			rs = stat.executeQuery();
			if (rs.next()) {
				long count = rs.getLong(1);
				if (count == 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if( stat != null) {
				stat.close();
			}
			if(con != null) {
				con.close();
			}
		}
        return flag;
    }

	public void insertParserLog(long seqTransactionFileId, int totalFail,
			int totalSuccess, int totalSuccessHostToHost, int totalSuccessNotHostToHost,
			int totalRecord, String status) throws SQLException {

		long seqParserLogId = sequenceUtil.generateParserLogId();

		ParserLog parserLog = new ParserLog();
		parserLog.setFileId(seqTransactionFileId);
		parserLog.setParserLogId(seqParserLogId);
		parserLog.setParseStatus(status);
		parserLog.setTotalFailed(totalFail);
		parserLog.setTotalSuccessHostToHost(totalSuccessHostToHost);
		parserLog.setTotalSuccessNotHostToHost(totalSuccessNotHostToHost);
		parserLog.setTotalProcessed(totalSuccess);
		parserLog.setTotalRecord(totalRecord);

		saveParserLog(parserLog);
	}

	/**
	 * This method checks if a stored procedure required for ReconciliationProcess is accessible.
	 * <br> This method used only for JUnit testing purposes.
	 * <br>For now, ReconciliationProcess use stored procedure named GetTransactionStatus.
	 * @return <li><code>true</code> Stored Procedure can be accessed.
	 * <li><code>false</code> Stored Procedure cannot be accessed.
	 */
	public boolean isReconciliationStoredProcedureReady() {
		String stan = "000000";
		String rrn = "000000000000";
		boolean result = false;
		try {
			TransactionStatus transactionStatus = getTransactionStatus(stan, rrn);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			e.printStackTrace();
		}
		return result;
	}
	
	public String getChannelPayment(String channelId) throws SQLException {

		String sql = "SELECT value FROM parameter WHERE param_id = ? ";
		
		Connection con = null;
		PreparedStatement stat = null;
		ResultSet rs = null;

		try {
			con = dataSource.getConnection();
			stat = con.prepareStatement(sql);
			stat.setString(1, channelId);
			rs = stat.executeQuery();
			if(rs.next()) {
				return rs.getString("value");
			}else{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if( stat != null) {
				stat.close();
			}
			if(con != null) {
				con.close();
			}
		}
	}
	
	public boolean getTransactionCode(String accountNo, String transactionCode){
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		
		try {
			c = dataSource.getConnection();
			String sql = "SELECT * from job_mt940 where ACCOUNT_NO = ? and TRANSACTION_CODE = ? ";
			s = c.prepareStatement(sql);
			s.setString(1, accountNo);
			s.setString(2, transactionCode);
			if(s.execute()) {
				rs = s.getResultSet();
				if(rs.next()){
					return true;
				}
			}
		} catch (SQLException e) {
			logger.trace(e);
		}finally {
			if(s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					logger.trace(e);
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					logger.trace(e);
				}
			}
		}
		
		return false;
	}
	
	public String getBitFlag(String accountNo, String transactionCode){
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		
		try {
			c = dataSource.getConnection();
			String sql = "SELECT * from job_mt940 where ACCOUNT_NO = ? and TRANSACTION_CODE = ? ";
			s = c.prepareStatement(sql);
			s.setString(1, accountNo);
			s.setString(2, transactionCode);
			if(s.execute()) {
				rs = s.getResultSet();
				if(rs.next()){
					return rs.getString("BIT_FLAG");
				}
			}
		} catch (SQLException e) {
			logger.trace(e);
		}finally {
			if(s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					logger.trace(e);
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					logger.trace(e);
				}
			}
		}
		
		//This is the default value
		return "86";
	}
}
