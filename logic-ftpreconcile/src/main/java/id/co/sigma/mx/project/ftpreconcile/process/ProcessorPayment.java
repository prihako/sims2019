package id.co.sigma.mx.project.ftpreconcile.process;

import id.co.sigma.mx.project.ftpreconcile.constant.PaymentConstant;
import id.co.sigma.mx.project.ftpreconcile.model.IsoBatchLog;
import id.co.sigma.mx.project.ftpreconcile.model.ProcessStatus;
import id.co.sigma.mx.project.ftpreconcile.model.ReconcileLog;
import id.co.sigma.mx.project.ftpreconcile.model.Transaction;
import id.co.sigma.mx.project.ftpreconcile.util.PaymentUtil;
import id.co.sigma.mx.project.ftpreconcile.util.RecoveryUtil;
import id.co.sigma.mx.project.ftpreconcile.util.SequenceUtil;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * This class contains <code>Runnable</code> issuing payment transaction
 * stored in TRANSACTION table to SPECTRAsimf through MX / Transaction Switching.
 */
public class ProcessorPayment {
	private static Logger logger = Logger.getLogger(ProcessorPayment.class);

	private static boolean sequenceGeneratorReady = false;
	private static boolean reconciliationStoredProcedureReady = false;

	private static int paymentReversalPeriod;

	public ProcessorPayment(String reversalPeriod) {
		paymentReversalPeriod = Integer.parseInt(reversalPeriod);
	}

	private PaymentUtil paymentUtil;

	public void setPaymentUtil(PaymentUtil paymentUtil) {
		this.paymentUtil = paymentUtil;
	}

	private SequenceUtil sequenceUtil;

	public void setSequenceUtil(SequenceUtil sequenceUtil) {
		this.sequenceUtil = sequenceUtil;
	}

	private RecoveryUtil recoveryUtil;

	public void setRecoveryUtil(RecoveryUtil recoveryUtil) {
		this.recoveryUtil = recoveryUtil;
	}

	public void execute() {
		Calendar startDate = Calendar.getInstance();
		Map<Integer, Transaction> transactionsPending = null;
		Map<Integer, Transaction> grouppedTransaction = null;
		Map<Integer, Transaction> mapOfErrorTransaction = null;
		ProcessStatus lastProcessStatus = null;
		try {
			sequenceGeneratorReady = sequenceUtil.isSequenceGeneratorsReady();
			logger.info("Sequence Generator is ready: ".concat(""
					+ sequenceGeneratorReady));
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(e);
		}
		reconciliationStoredProcedureReady = paymentUtil.isReconciliationStoredProcedureReady();
		logger.info("Reconciliation Stored Procedure ready: " + reconciliationStoredProcedureReady);
		if (//connectionToAdapterEstablished &&
				sequenceGeneratorReady && reconciliationStoredProcedureReady) {
			try {
				lastProcessStatus = recoveryUtil.getLastProcessStatus();
				transactionsPending = paymentUtil.getUnprocessedTransactions();
				mapOfErrorTransaction = paymentUtil.getErrorTransaction();
			} catch (Exception e) {
				e.printStackTrace();
				logger.fatal(e);
			}
		}

		if(lastProcessStatus != null && PaymentConstant.BATCH_STATUS_STARTED.equals(lastProcessStatus.getStatus())) {
			try {
				logger.info("RecoveryProcess started at " + new Date());
				recoveryUtil.reconcileUnfinishedTransaction(lastProcessStatus);
				logger.info("RecoveryProcess completed at " + new Date());
			} catch (SQLException e) {
				e.printStackTrace();
				logger.fatal(e);
			}
		}

		if (transactionsPending != null && transactionsPending.size() > 0 ||
			mapOfErrorTransaction != null && mapOfErrorTransaction.size() > 0) {
			ReconcileLog reconcileLog = null;
			IsoBatchLog isoBatchLog = null;
			ProcessStatus processStatus = null;
			if(transactionsPending != null && transactionsPending.size() > 0 ) {
				try {
					isoBatchLog = paymentUtil.createIsoBatchLog();
				} catch (Exception e) {
					e.printStackTrace();
					logger.fatal(e);
				}
			}
			try {
				reconcileLog = paymentUtil.createReconcileLog();
				processStatus = paymentUtil.createProcessStatus(isoBatchLog, reconcileLog);
				paymentUtil.createProcessItems(transactionsPending, processStatus);
				grouppedTransaction = paymentUtil.groupTransactionToProcess(processStatus);
			} catch (Exception e) {
				e.printStackTrace();
				logger.fatal(e);
			}
			Iterator<Entry<Integer,Transaction>> i = grouppedTransaction.entrySet().iterator();
			while (i.hasNext()) {
				Entry<Integer,Transaction> entry = i.next();
				Transaction t = entry.getValue();
				logger.info("Processing: ".concat(t.toString()));
				boolean validTransaction = paymentUtil.transactionIsValid(t);
				logger.info("Transaction is valid: ".concat(""
						+ validTransaction));
				try {
					if (validTransaction) {
						validTransaction = paymentUtil.sendInquiryRequest(t, isoBatchLog, processStatus);
					}
					logger
							.info("InvoiceID and ClientID validation results: "
									+ validTransaction);
					if (validTransaction) {
						validTransaction = paymentUtil.sendPaymentRequest(t, isoBatchLog, processStatus);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.fatal(e);
					break;
				}
			}
			try {
				paymentUtil.reconcileErrorTransaction(mapOfErrorTransaction, processStatus);
				paymentUtil.reconcile(processStatus);
				paymentUtil.updateTransactions(processStatus);
				paymentUtil.updateReconcileLog(reconcileLog.getReconcileLogId());
				if(transactionsPending != null && transactionsPending.size() > 0) {
					paymentUtil.updateIsoBatchLog(isoBatchLog.getIsoBatchLogId());
				}
				paymentUtil.markCompleted(processStatus);
				paymentUtil.activateReconcileResult(processStatus);
			} catch (Exception e) {
				e.printStackTrace();
				logger.fatal(e);
			}
		} else {
			logger.info("No transaction to process at " + startDate.getTime().toString());
		}

	}

	public static int getPaymentReversalPeriod() {
		return paymentReversalPeriod;
	}
}
