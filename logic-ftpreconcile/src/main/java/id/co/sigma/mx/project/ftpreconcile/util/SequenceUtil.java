package id.co.sigma.mx.project.ftpreconcile.util;

import id.co.sigma.mx.project.ftpreconcile.model.UserSequences;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class SequenceUtil {
	private static final String stanSequenceName = "SEQ_STAN";
	private static final String rrnSequenceName = "SEQ_RRN";
	private static final String isoBatchLogSequenceName = "SEQ_ISO_BATCH_LOG";
	private static final String reconcileLogSequenceName = "SEQ_RECONCILE_LOG";
	private static final String reconcileResultsSequenceName = "SEQ_RECONCILE_RESULT";
	private static final String transactionIsoSequenceName = "SEQ_TRANSACTION_ISO";
	private static final String transactionIsoMapSequenceName = "SEQ_TRANSACTION_ISO_MAP";
	private static final String transactionSettlementSequenceName = "SEQ_TRANSACTION_SETTLEMENT";
	private static final String processStatusSequenceName = "SEQ_PROCESS_STATUS";
	private static final String listenerLogSequenceName = "SEQ_LISTENER_LOG";
	private static final String transactionFileSequenceName = "SEQ_TRANSACTION_FILE";
	private static final String transactionSequenceName = "SEQ_TRANSACTION";
	private static final String parserLogSequenceName = "SEQ_PARSER_LOG";
	private static final Logger logger = Logger.getLogger(SequenceUtil.class);

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private String orclUser;

	public void setOrclUser(String orclUser) {
		this.orclUser = orclUser;
	}

	/**
	 * This method checks whether all sequence name registered in <code>payment.properties</code> file
	 * are accessible.
	 * @return <code>boolean</code>
	 * <li>true</li> if all sequence required are accessible.
	 * <li>false</li> if any of sequence are not accessible.
	 *
	 */
	private static boolean isSequenceNamesRegistered() {
		if (stanSequenceName == null) {
			logger.error("Can't get 'stan.sequence.name' in properties");
			return false;
		}
		if (rrnSequenceName == null) {
			logger.error("Can't get 'rrn.sequence.name' in properties");
			return false;
		}
		if (isoBatchLogSequenceName == null) {
			logger.error("Can't get 'isoBatchLog.sequence.name' in properties");
			return false;
		}
		if (reconcileLogSequenceName == null) {
			logger.error("Can't get 'reconcileLog.sequence.name' in properties");
			return false;
		}
		if (reconcileResultsSequenceName == null) {
			logger.error("Can't get 'reconcileResults.sequence.name' in properties");
			return false;
		}
		if (transactionIsoSequenceName == null) {
			logger.error("Can't get 'transactionIso.sequence.name' in properties");
			return false;
		}
		if (transactionIsoMapSequenceName == null) {
			logger.error("Can't get 'transactionIsoMap.sequence.name' in properties");
			return false;
		}
		if (transactionSettlementSequenceName == null) {
			logger.error("Can't get 'transactionSettlement.sequence.name' in properties");
			return false;
		}
		if (processStatusSequenceName == null) {
			logger.error("Can't get 'processStatus.sequence.name' in properties");
			return false;
		}
		return true;
	}

	/**This method checks whether SequenceUtilities are ready to use.
	 * @return <code>boolean</code>
	 * <li>true</li> SequenceUtilities are ready to use.
	 * <li>false</li> SequenceUtilities are not ready to use.
	 * @throws SQLException
	 */
	public boolean isSequenceGeneratorsReady() throws SQLException {

		boolean result = true;
		if (!isSequenceNamesRegistered()) {
			return false;
		}
		String[] sequenceNames = { stanSequenceName, rrnSequenceName,
				isoBatchLogSequenceName, reconcileLogSequenceName,
				reconcileResultsSequenceName, transactionIsoSequenceName,
				transactionIsoMapSequenceName,
				transactionSettlementSequenceName, processStatusSequenceName };
		Map<String, UserSequences> mapOfSequences = getMapOfSequences(sequenceNames);
		for (int i = 0; i < sequenceNames.length; i++) {
			if (mapOfSequences.containsKey(sequenceNames[i])) {
				UserSequences sequence = mapOfSequences.get(sequenceNames[i]);

				if ((sequence.getLastNumber().compareTo(sequence.getMinValue()) == 0 || sequence
						.getLastNumber().compareTo(sequence.getMinValue()) == 1)
						&& sequence.getLastNumber().compareTo(
								sequence.getMaxValue()) == -1) {
					result &= true;
				} else {
					boolean restartSequenceSuccess = restartSequence(sequenceNames[i]);
					logger.info("SEQUENCE_RESTART ".concat(sequenceNames[i])
							.concat(": ").concat("" + restartSequenceSuccess));
					result &= restartSequenceSuccess;
				}
			} else {
				boolean createSequenceSuccess = createSequence(sequenceNames[i]);
				logger.info("SEQUENCE_CREATE ".concat(sequenceNames[i]).concat(
						": ").concat("" + createSequenceSuccess));
				result &= createSequenceSuccess;
			}
		}
		return result;
	}

	/**
	 * This method generate SystemTraceAuditNumber from sequence whose name is registered in <code>payment.properties</code>
	 * under key <code>stan.sequence.name</code>.
	 * @return <code>String</code> Six digit SystemTraceAuditNumber.
	 * @throws SQLException
	 */
	public synchronized String generateStan() throws SQLException {
		Long stan = 1000000L + getSequenceValue(stanSequenceName);
		return stan.toString().substring(1, 7);
	}


	/**
	 * This method generate RetrievalReferenceNumber from sequence whose name is registered in <code>payment.properties</code>
	 * under key <code>rrn.sequence.name</code>.
	 * @return <code>String</code> Twelve digit RetrievalReferenceNumber.
	 * @throws SQLException
	 */
	public synchronized String generateRrn() throws SQLException {
		Long longRrn = 1000000000000L + getSequenceValue(rrnSequenceName);
		return longRrn.toString().substring(1, 13);
	}


	/**
	 * This method generate <code>Long</code> value to be used as field <code>ISO_BATCH_LOG_ID</code>.
	 * <br>Sequence's name registered under key <code>isoBatchLog.sequence.name</code> in <code>payment.properties</code>.
	 * @return <code>Long</code> Sequence value from <code>isoBatchLog.sequence.name</code>.
	 * @throws SQLException
	 */
	public synchronized Long generateIsoBatchLogId() throws SQLException {
		return getSequenceValue(isoBatchLogSequenceName);
	}

	/**
	 * This method generate <code>Long</code> value to be used as field <code>RECONCILE_LOG_ID</code>.
	 * <br>Sequence's name registered under key <code>reconcileLog.sequence.name</code> in <code>payment.properties</code>.
	 * @return <code>Long</code> Sequence value from <code>reconcileLog.sequence.name</code>.
	 * @throws SQLException
	 */
	public synchronized long generateReconcileLogId() throws SQLException {
		return getSequenceValue(reconcileLogSequenceName);
	}

	/**
	 * This method generate <code>Long</code> value to be used as field <code>RECONCILE_RESULT_ID</code>.
	 * <br>Sequence's name registered under key <code>reconcileResults.sequence.name</code> in <code>payment.properties</code>.
	 * @return <code>Long</code> Sequence value from <code>reconcileResults.sequence.name</code>.
	 * @throws SQLException

	 */
	public  synchronized long generateReconcileResultsId() throws SQLException {
		return getSequenceValue(reconcileResultsSequenceName);
	}


	/**
	 * This method generate <code>Long</code> value to be used as field <code>TRANSACTION_ISO_ID</code>.
	 * <br>Sequence's name registered under key <code>transactionIso.sequence.name</code> in <code>payment.properties</code>.
	 * @return <code>Long</code> Sequence value from <code>transactionIso.sequence.name</code>.
	 * @throws SQLException

	 */
	public synchronized long generateTransactionIsoId() throws SQLException {
		return getSequenceValue(transactionIsoSequenceName);
	}

	/**
	 * This method generate <code>Long</code> value to be used as field <code>TRANSACTION_ISO_MAP_ID</code>.
	 * <br>Sequence's name registered under key <code>transactionIsoMap.sequence.name</code> in <code>payment.properties</code>.
	 * @return <code>Long</code> Sequence value from <code>transactionIsoMap.sequence.name</code>.
	 * @throws SQLException

	 */
	public synchronized long generateTransactionIsoMapId() throws SQLException {
		return getSequenceValue(transactionIsoMapSequenceName);
	}


	/**
	 * This method generate <code>Long</code> value to be used as field <code>LISTENER_LOG_ID</code>.
	 * <br>Sequence's name registered under key <code>transactionSettlement.sequence.name</code> in <code>payment.properties</code>.
	 * @return <code>Long</code> Sequence value from <code>transactionSettlement.sequence.name</code>.
	 * @throws SQLException

	 */
	public synchronized long generateListenerLogId() throws SQLException {
		return getSequenceValue(listenerLogSequenceName);
	}

	/**
	 * This method generate <code>Long</code> value to be used as field <code>TRANSACTION_FILE_ID</code>.
	 * <br>Sequence's name registered under key <code>transactionSettlement.sequence.name</code> in <code>payment.properties</code>.
	 * @return <code>Long</code> Sequence value from <code>transactionSettlement.sequence.name</code>.
	 * @throws SQLException

	 */
	public  synchronized long generateTransactionFileId() throws SQLException {
		return getSequenceValue(transactionFileSequenceName);
	}

	/**
	 * This method generate <code>Long</code> value to be used as field <code>TRANSACTION_ID</code>.
	 * <br>Sequence's name registered under key <code>transactionSettlement.sequence.name</code> in <code>payment.properties</code>.
	 * @return <code>Long</code> Sequence value from <code>transactionSettlement.sequence.name</code>.
	 * @throws SQLException

	 */
	public synchronized long generateTransactionId() throws SQLException {
		return getSequenceValue(transactionSequenceName);
	}

	/**
	 * This method generate <code>Long</code> value to be used as field <code>PARSER_LOG_ID</code>.
	 * <br>Sequence's name registered under key <code>transactionSettlement.sequence.name</code> in <code>payment.properties</code>.
	 * @return <code>Long</code> Sequence value from <code>transactionSettlement.sequence.name</code>.
	 * @throws SQLException

	 */
	public synchronized long generateParserLogId() throws SQLException {
		return getSequenceValue(parserLogSequenceName);
	}

	/**
	 * This method generate <code>Long</code> value to be used as field <code>TRANSACTION_SETTLEMENT_ID</code>.
	 * <br>Sequence's name registered under key <code>transactionSettlement.sequence.name</code> in <code>payment.properties</code>.
	 * @return <code>Long</code> Sequence value from <code>transactionSettlement.sequence.name</code>.
	 * @throws SQLException
	 */
	public synchronized long generateTransactionSettlementId() throws SQLException {
		return getSequenceValue(transactionSettlementSequenceName);
	}

	public synchronized long generateProcessStatusId() throws SQLException {
		return getSequenceValue(processStatusSequenceName);
	}

	private synchronized Long getSequenceValue(String sequenceName) throws SQLException {
		Long sequenceValue = null;
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;
		final String sql = "SELECT ".concat(sequenceName).concat(
				".NEXTVAL AS SEQ_VALUE").concat(" FROM dual");
		try {
			con = dataSource.getConnection();
			s = con.createStatement();
			rs = s.executeQuery(sql);
			if (rs.next()) {
				sequenceValue = rs.getLong("SEQ_VALUE");
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
		return sequenceValue;
	}

	private boolean restartSequence(String sequenceName) throws SQLException {
		Connection con = null;
		Statement s = null;
		String sql = "DROP SEQUENCE ".concat(sequenceName);
		try {
			con = dataSource.getConnection();
			s = con.createStatement();
			s.addBatch(sql);
			sql = generateSequenceCreationScript(sequenceName);
			s.addBatch(sql);
			s.executeBatch();
			return true;
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

	private Map<String, UserSequences> getMapOfSequences(
			String[] sequencesName) throws SQLException {
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;
		Map<String, UserSequences> mapOfSequences = new HashMap<String, UserSequences>();
		String sql = "SELECT * FROM user_sequences WHERE sequence_name IN(";
		for (int i = 0; i < sequencesName.length; i++) {
			sql = sql.concat("'").concat(sequencesName[i]).concat("'");
			if (i < sequencesName.length - 1) {
				sql = sql.concat(", ");
			}
		}
		sql = sql.concat(")");
		try {
			con = dataSource.getConnection();
			s = con.createStatement();
			rs = s.executeQuery(sql);
			while (rs.next()) {
				UserSequences sequence = new UserSequences();
				sequence.setSequenceName(rs.getString("SEQUENCE_NAME"));
				sequence.setMinValue(rs.getBigDecimal("MIN_VALUE"));
				sequence.setMaxValue(rs.getBigDecimal("MAX_VALUE"));
				sequence.setIncrementBy(rs.getBigDecimal("INCREMENT_BY"));
				sequence.setCycleFlag(rs.getString("CYCLE_FLAG"));

				sequence.setOrderFlag(rs.getString("ORDER_FLAG"));
				sequence.setCacheSize(rs.getBigDecimal("CACHE_SIZE"));
				sequence.setLastNumber(rs.getBigDecimal("LAST_NUMBER"));
				mapOfSequences.put(sequence.getSequenceName(), sequence);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		} finally {
			try {
				rs.close();
				s.close();
				con.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return mapOfSequences;
	}

	private boolean createSequence(String sequenceName) throws SQLException {
		Connection c = null;
		Statement s = null;
		String sql = generateSequenceCreationScript(sequenceName);

		try {
			c = dataSource.getConnection();
			s = c.createStatement();
			s.execute(sql);
			return true;
		}

		catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		} finally {

			if (s != null) {
				try {
					s.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}
	}

	private String generateSequenceCreationScript(String sequenceName) {
		String sql = "";
		if (stanSequenceName.equals(sequenceName)) {
			sql = "CREATE SEQUENCE ".concat(
					orclUser).concat(".")
					.concat(stanSequenceName).concat(" START WITH 1").concat(
							" INCREMENT BY 1").concat(" MINVALUE 1").concat(
							" MAXVALUE 999999").concat(
							" NOCACHE NOCYCLE NOORDER");
		}
		if (rrnSequenceName.equals(sequenceName)) {
			sql = "CREATE SEQUENCE ".concat(
					orclUser).concat(".")
					.concat(rrnSequenceName).concat(" START WITH 1").concat(
							" INCREMENT BY 1").concat(" MINVALUE 1").concat(
							" MAXVALUE 999999999999").concat(
							" NOCACHE NOCYCLE NOORDER");
		}
		if (isoBatchLogSequenceName.equals(sequenceName)) {
			sql = "CREATE SEQUENCE ".concat(
					orclUser).concat(".")
					.concat(isoBatchLogSequenceName).concat(" START WITH 1")
					.concat(" INCREMENT BY 1").concat(" MINVALUE 1").concat(
							" MAXVALUE 9999999999999999999").concat(
							" NOCACHE NOCYCLE NOORDER");
		}
		if (reconcileLogSequenceName.equals(sequenceName)) {
			sql = "CREATE SEQUENCE ".concat(
					orclUser).concat(".")
					.concat(reconcileLogSequenceName).concat(" START WITH 1")
					.concat(" INCREMENT BY 1").concat(" MINVALUE 1").concat(
							" MAXVALUE 9999999999999999999").concat(
							" NOCACHE NOCYCLE NOORDER");
		}
		if (reconcileResultsSequenceName.equals(sequenceName)) {
			sql = "CREATE SEQUENCE ".concat(
					orclUser).concat(".")
					.concat(reconcileResultsSequenceName).concat(
							" START WITH 1").concat(" INCREMENT BY 1").concat(
							" MINVALUE 1").concat(
							" MAXVALUE 9999999999999999999").concat(
							" NOCACHE NOCYCLE NOORDER");
		}
		if (transactionIsoSequenceName.equals(sequenceName)) {
			sql = "CREATE SEQUENCE ".concat(
					orclUser).concat(".")
					.concat(transactionIsoSequenceName).concat(" START WITH 1")
					.concat(" INCREMENT BY 1").concat(" MINVALUE 1").concat(
							" MAXVALUE 9999999999999999999").concat(
							" NOCACHE NOCYCLE NOORDER");
		}
		if (transactionIsoMapSequenceName.equals(sequenceName)) {
			sql = "CREATE SEQUENCE ".concat(
					orclUser).concat(".")
					.concat(transactionIsoMapSequenceName).concat(
							" START WITH 1").concat(" INCREMENT BY 1").concat(
							" MINVALUE 1").concat(
							" MAXVALUE 9999999999999999999").concat(
							" NOCACHE NOCYCLE NOORDER");
		}
		if (transactionSettlementSequenceName.equals(sequenceName)) {
			sql = "CREATE SEQUENCE ".concat(
					orclUser).concat(".")
					.concat(transactionSettlementSequenceName).concat(
							" START WITH 1").concat(" INCREMENT BY 1").concat(
							" MINVALUE 1").concat(
							" MAXVALUE 9999999999999999999").concat(
							" NOCACHE NOCYCLE NOORDER");
		}
		if(processStatusSequenceName.equals(sequenceName)) {
			sql = "CREATE SEQUENCE ".concat(orclUser).concat(".")
					.concat(processStatusSequenceName)
					.concat(" START WITH 1").concat(" INCREMENT BY 1")
					.concat(" MINVALUE 1")
					.concat(" MAXVALUE 9999999999999999999")
					.concat(" NOCACHE NOCYCLE NOORDER");
		}
		return sql;
	}
}
