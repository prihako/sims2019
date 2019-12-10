package com.balicamp.dao.hibernate.mx;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.dao.mx.TransactionLogDao;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.model.mx.TransactionLogs;
import com.balicamp.model.mx.Transactions;
import com.balicamp.model.report.ReportModel;
import com.balicamp.util.DateUtil;
import com.balicamp.util.TransactionsQueryUtil;
import com.balicamp.util.wrapper.ReportCriteria;
import com.balicamp.util.wrapper.ReportCriteriaWraper;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionLogDaoHibernate.java 503 2013-05-24 08:13:16Z
 *          rudi.sadria $
 */
@Repository
public class TransactionLogDaoHibernate
		extends
			MxGenericDaoHibernate<TransactionLogs, Long>
		implements
			TransactionLogDao {

	int ukurandata;
	List objectData;
	Query queryPlain;
	Query queryPlains;

	@Autowired
	private TransactionsQueryUtil queryUtil;

	@Autowired
	private DataSource dataSource;

	private String queryTransactionLogs = "select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
			+ "(  "
			+ "        select * "
			+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
			+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
			+ "        ORDER by transaction_time" + ") as tl WHERE ";

	public TransactionLogDaoHibernate() {
		super(TransactionLogs.class);
	}

	@Override
	public List<String> findRawData(SearchCriteria searchCriteris) {
		List<String> raws = null;

		List<TransactionLogs> logs = findByCriteria(searchCriteris);
		if (null != logs) {
			raws = new ArrayList<String>();
			for (TransactionLogs transactionLogs : logs) {
				raws.add(transactionLogs.getId().getRaw());
			}

		}
		return raws;
	}

	@Override
	public List<?> findTransactions(String txCode, String channelCode,
			String txId, String klienID, String invoiceNo,
			String endpointTransactionType, Date startDate, Date endDate,
			String rawKey, int first, int max) {

		// System.out.println("TransactionLogDaoHibernate FIRST " + first);
		//
		// System.out.println("TransactionLogDaoHibernate MAX " + max);
		// System.out.println("TransactionLogDaoHibernate TransactionCode "
		// + txCode);
		//
		// System.out.println("TransactionLogDaoHibernate startDate " +
		// startDate);
		// System.out.println("TransactionLogDaoHibernate endDate " + endDate);
		// System.out.println("TransactionLogDaoHibernate klienID " + klienID);
		// System.out.println("TransactionLogDaoHibernate invoiceNo " +
		// invoiceNo);
		// System.out
		// .println("TransactionLogDaoHibernate endpointTransactionType "
		// + endpointTransactionType);

		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String trxStartDate = null;
		String trxEndDate = null;

		if (startDate == null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate != null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate == null && endDate != null) {
			trxStartDate = null;
			trxEndDate = null;
		} else {
			trxStartDate = format.format(startDate);
			trxEndDate = format.format(endDate);
		}

		// System.out.println("FORMAT Start date --> " + trxStartDate);
		// System.out.println("FORMAT end date --> " + trxEndDate);

		StringBuffer sql = new StringBuffer();

		if (!endpointTransactionType.equals("all") && klienID != null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("1.DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%'  ");

		} else if (!endpointTransactionType.equals("all") && klienID != null
				&& invoiceNo == null && !txCode.equals("ALL")) {

			System.out
					.println("1A.DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%' and code ='" + txCode + "' ");

		} else if (!endpointTransactionType.equals("all") && klienID == null
				&& invoiceNo != null && txCode.equals("ALL")) {

			System.out
					.println("2. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID  NULL & INVOICENO NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' ");
		} else if (!endpointTransactionType.equals("all") && klienID == null
				&& invoiceNo != null && !txCode.equals("ALL")) {

			System.out
					.println("2A. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID  NULL & INVOICENO NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and code ='" + txCode + "' ");
		} else if (!endpointTransactionType.equals("all") && klienID != null
				&& invoiceNo != null && txCode.equals("ALL")) {

			System.out
					.println("3. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo
					+ "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' ");
		} else if (!endpointTransactionType.equals("all") && klienID != null
				&& invoiceNo != null && !txCode.equals("ALL")) {

			System.out
					.println("3A. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo
					+ "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' and code ='" + txCode + "' ");
		} else if (!endpointTransactionType.equals("all") && klienID == null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("4. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEN ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "' ");
		} else if (!endpointTransactionType.equals("all") && klienID == null
				&& invoiceNo == null && !txCode.equals("ALL")) {

			System.out
					.println("4A. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEN ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "' and code ='" + txCode + "' ");
		} else if (endpointTransactionType.equals("all") && klienID != null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("5. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%' ");

		} else if (endpointTransactionType.equals("all") && klienID != null
				&& invoiceNo == null && !txCode.equals("ALL")) {

			System.out
					.println("6. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%' and code ='" + txCode + "' ");

		} else if (endpointTransactionType.equals("all") && klienID == null
				&& invoiceNo != null && txCode.equals("ALL")) {

			System.out
					.println("7. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO NOT NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' ");
		} else if (endpointTransactionType.equals("all") && klienID == null
				&& invoiceNo != null && !txCode.equals("ALL")) {

			System.out
					.println("8. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO NOT NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and code ='" + txCode + "' ");
		} else if (endpointTransactionType.equals("all") && klienID != null
				&& invoiceNo != null && !txCode.equals("ALL")) {

			System.out
					.println("9. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo
					+ "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' and code ='" + txCode + "' ");
		} else if (endpointTransactionType.equals("all") && klienID != null
				&& invoiceNo != null && txCode.equals("ALL")) {

			System.out
					.println("10. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo
					+ "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' ");
		} else if (endpointTransactionType.equals("all") && klienID == null
				&& invoiceNo == null && !txCode.equals("ALL")) {

			System.out
					.println("11. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "' and code ='" + txCode + "' ");
		} else if (endpointTransactionType.equals("all") && klienID == null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("12. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "'");
		}

		Query query = getSession().createSQLQuery(sql.toString());
		queryPlain = getSession().createSQLQuery(sql.toString());

		// queryUtil.setQueryParameters(query, txId, txCode,
		// channelCode, startDate, endDate, rawKey);

		query.setFirstResult(first);
		// if(max>0)
		query.setMaxResults(max);
		// System.out.println("TransactionLogDaoHibernate Query SUSKES ");

		List obj = query.list();

		// System.out.println("Dao Hibernate Query Result Size : "
		// + query.list().size());

		return obj;
	}

	// TODO: Hapus Join ke message log untuk production
	@Override
	public Long findTransactionCount(String txId, String txCode,
			String channelCode, Date startDate, Date endDate, String rawKey) {

		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from "
				+ "(  "
				+ "        select * "
				+ "        From  "
				+ "        (  "
				+ "                 select * from transaction_logs "
				+ "                union ALL   "
				+ "                select * from transaction_logs_housekeeping "
				+ "        ) as temp ,transactions tr,endpoint_rcs er "
				+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
				+ "        ORDER by transaction_time   "
				+ ") as tl  WHERE 1=1 ");

		// queryUtil.addParameters(sql, txId, txCode, channelCode, startDate,
		// endDate, rawKey, 0);

		Query query = getSession().createSQLQuery(sql.toString());
		// queryUtil.setQueryParameters(query, txId, txCode, channelCode,
		// startDate, endDate, rawKey);
		return Long.parseLong("" + queryPlain.list().size());
		// return Long.parseLong(s)objectData.size();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findRawData(String txCode, String channelRc,
			Date startDate, Date endDate) {

		Query query = getSession()
				.createQuery(
						"select a.id.raw from TransactionLogs a "
								+ " where  "
								+ " a.id.transactionCode=:trxCode "
								+ " and a.id.channelRc=:channelRc "
								+ " and date(a.id.transactionTime) >= date(:startDate) "
								+ " and date(a.id.transactionTime) <= date(:endDate) "
								+ " ");
		query.setParameter("trxCode", txCode);
		query.setParameter("channelRc", channelRc);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> findReversalRecords(List<ReportCriteria> criteriasOri) {
		List<ReportCriteria> criterias = new ArrayList<ReportCriteria>(
				criteriasOri.size());
		for (int i = 2; i < criteriasOri.size(); i++) {
			criterias.add(criteriasOri.get(i));
		}

		// criterias.add(new ReportCriteria(ReportCriteria.MIDDLE_LIKE,
		// "id.raw", "/data/logRetrievalKey/text()="+retrievalKey));
		criterias.add(new ReportCriteria(ReportCriteria.EQUALS, "id.channelRc",
				"00"));
		criterias.add(new ReportCriteria(ReportCriteria.EQUALS, "id.type",
				"reversal"));

		List<String> fieldToSelect = new ArrayList<String>();
		fieldToSelect.add("id.raw");
		fieldToSelect.add("id.retrievalKey");
		String hql = ReportCriteriaWraper.getRestrictionsHQL(fieldToSelect,
				criterias, TransactionLogs.class);
		Query query = getSession().createQuery(hql);
		return query.list();
	}

	public List<Object[]> findSuccessRecords(List<ReportCriteria> criteriasOri) {
		List<ReportCriteria> criterias = new ArrayList<ReportCriteria>(
				criteriasOri.size());
		for (int i = 2; i < criteriasOri.size(); i++) {
			criterias.add(criteriasOri.get(i));
		}

		criterias.add(new ReportCriteria(ReportCriteria.EQUALS, "id.channelRc",
				"00"));
		List<String> fieldToSelect = new ArrayList<String>();
		fieldToSelect.add("id.raw");
		fieldToSelect.add("id.retrievalKey");
		String hql = ReportCriteriaWraper.getRestrictionsHQL(fieldToSelect,
				criterias, TransactionLogs.class);
		Query query = getSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findRawData(List<ReportCriteria> criterias,
			ReportModel model) {
		List<String> lstSelect = new ArrayList<String>();
		lstSelect.add("id.raw");
		lstSelect.add("id.retrievalKey");
		String strQry = ReportCriteriaWraper.getRestrictionsHQL(lstSelect,
				criterias, TransactionLogs.class);
		Query qry = getSession().createQuery(strQry);

		List<Object[]> result = qry.list();

		List<String> txLogs = new ArrayList<String>();
		List<Object[]> reversalRecords = findReversalRecords(criterias);

		if (model.getKodeReport().trim().endsWith("_SUK")) {
			for (int i = 0; i < result.size(); i++) {
				String txRetrievalKey = (String) result.get(i)[1];
				boolean revRetrievalKeyFound = false;

				for (Object[] revRaw : reversalRecords) {
					if (((String) revRaw[0]).contains(txRetrievalKey)) {
						revRetrievalKeyFound = true;
						break;
					}
				}
				if (!revRetrievalKeyFound)
					txLogs.add((String) result.get(i)[0]);
			}
		} else if (model.getKodeReport().trim().endsWith("_GAG")) {
			List<Object[]> successRecords = findSuccessRecords(criterias);
			for (int i = 0; i < result.size(); i++) {
				txLogs.add((String) result.get(i)[0]);
			}

			for (Object[] revRecord : reversalRecords) {
				for (Object[] sucRaw : successRecords) {
					if (((String) sucRaw[0]).contains((String) revRecord[1])) {
						txLogs.add((String) revRecord[0]);
					}
				}
			}
		} else {
			for (int i = 0; i < result.size(); i++) {
				txLogs.add((String) result.get(i)[0]);
			}
		}
		return txLogs;
	}

	@Override
	public Long getTotalCountTransaction(Date startDate, Date endDate, String rc) {

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("select count(*) from "
				+ "(  "
				+ "        select * "
				+ "        From  "
				+ "        (  "
				+ "                 select * from transaction_logs "
				+ "                union ALL   "
				+ "                select * from transaction_logs_housekeeping "
				+ "        ) as temp ,transactions tr,endpoint_rcs er "
				+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
				+ "        ORDER by transaction_time   "
				+ ") as tl  WHERE 1=1 ");
		if (startDate != null)
			sqlQuery.append("AND transaction_time >= :startDate ");
		if (endDate != null)
			sqlQuery.append("AND transaction_time <= :endDate ");
		if (rc != null && rc.length() > 0)
			sqlQuery.append("AND channel_rc= :rc ");
		SQLQuery query = getSession().createSQLQuery(sqlQuery.toString());
		if (startDate != null)
			query.setParameter("startDate", startDate);
		if (endDate != null)
			query.setParameter("endDate", endDate);
		if (rc != null && rc.length() > 0)
			query.setParameter("rc", rc);

		return ((BigInteger) query.uniqueResult()).longValue();
	}

	@Override
	public List<Object> findLastTransactionsLog(int i) {
		StringBuffer sql = new StringBuffer();
		sql.append("select transaction_id,transaction_time,code,name,channel_code,channel_rc,description,raw from "
				+ "(  "
				+ "        select * "
				+ "        From  "
				+ "        (  "
				+ "                 select * from transaction_logs "
				+ "                union ALL   "
				+ "                select * from transaction_logs_housekeeping "
				+ "        ) as temp ,transactions tr,endpoint_rcs er "
				+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
				+ "        ORDER by transaction_time   "
				+ ") as tl  WHERE 1=1 ORDER BY transaction_time desc LIMIT "
				+ i);

		Query query = getSession().createSQLQuery(sql.toString());
		return query.list();
	}

	@Override
	public List<Object> findByTransaction(String klienID, String invoiceNo,
			String transactionType, String endpoint, String responseCode,
			Date startDate, Date endDate) {

		String queryTransactionLog = null;

		if (transactionType.equals("0") && responseCode.equals("0")
				&& endpoint.equals("BMRI")) {
			System.out.println("BMRI endpointya xxxxxx");

			queryTransactionLog = "select transaction_id, transaction_time,code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select *   From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE 1=1 ";
		} else if (!transactionType.equals("0") && responseCode.equals("0")) {

			System.out.println("NONBMRI endpointya xxxxxx");

			queryTransactionLog = "select transaction_id, transaction_time,code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select *   From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl  WHERE 1=1 ";
		}

		List<Object> list = null;
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				queryTransactionLog);
		list = query.list();

		return list;
	}

	@Override
	public List<?> findRawMessage(String txId) {

		StringBuffer sql = new StringBuffer();

		sql.append("select transaction_id,transaction_time,code,name,channel_code,channel_rc,description,raw,mx_rc from "
				+ "(  "
				+ "        select * "
				+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
				+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
				+ "        ORDER by transaction_time"
				+ ") as tl WHERE transaction_id = '" + txId + "'");

		List<Object> list = null;
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql.toString());
		list = query.list();

		return list;
	}

	@Override
	public Object findTransactionsByTransactionID(String txId) {
		// TODO Auto-generated method stub

		StringBuffer sql = new StringBuffer();
		sql.append("select transaction_id,channel_rc,mx_rc from "
				+ "(  "
				+ "        select * "
				+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
				+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
				+ "        ORDER by transaction_time"
				+ ") as tl WHERE transaction_id ='" + txId + "'");

		Query query = getSession().createSQLQuery(sql.toString());

		return query.uniqueResult();
	}

	@Override
	public List<?> findTransactions(String txId, String transactionType,
			String txCode, String klienID, String invoiceNo, Date startDate,
			Date endDate, String rawKey, int first, int max) {

		String trxType = null;

		if (transactionType.equals("reor01")) {
			trxType = "REOR";
		} else if (transactionType.equals("per01")) {
			trxType = "PER";

		} else if (transactionType.equals("bils0")) {
			trxType = "BILL";

		} else if (transactionType.equals("skor01")) {
			trxType = "SKOR";

		}

		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String trxStartDate = null;
		String trxEndDate = null;

		if (startDate == null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate != null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate == null && endDate != null) {
			trxStartDate = null;
			trxEndDate = null;
		} else {
			trxStartDate = format.format(startDate);
			trxEndDate = format.format(endDate);
		}

		// System.out.println("FORMAT Start date --> " + trxStartDate);
		// System.out.println("FORMAT end date --> " + trxEndDate);

		StringBuffer sql = new StringBuffer();

		if ((invoiceNo != null) && (trxStartDate == null)
				&& (trxEndDate == null) && (klienID == null)) {
			System.out.println("INVOICE NOT NULL, AND THE OTHER NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE "
					+ "raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo
					+ "%' ");

		} else if (transactionType.equals("reor01") && klienID != null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("1A.DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%' and code like '%REOR%' ");

		} else if (transactionType.equals("bils0") && klienID != null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("1B.DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%' and code like '%BILL%'  ");

		} else if (transactionType.equals("per01") && klienID != null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("1C.DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%' and code like '%PER%'");

		} else if (transactionType.equals("skor01") && klienID != null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("1D.DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%' and code like '%SKOR%'");

		} else if (!transactionType.equals("all") && klienID != null
				&& invoiceNo == null && !txCode.equals("ALL")) {

			System.out
					.println("1E.DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%' and code ='" + txCode + "' ");

		} else if (!transactionType.equals("all") && klienID == null
				&& invoiceNo != null && txCode.equals("ALL")) {

			System.out.println("TIPE TRANSAKSI APA SIH  ? " + transactionType);

			System.out
					.println("2. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID  NULL & INVOICENO NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and code like '%" + trxType + "%' ");
		} else if (!transactionType.equals("all") && klienID == null
				&& invoiceNo != null && !txCode.equals("ALL")) {

			System.out
					.println("2A. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID  NULL & INVOICENO NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and code ='" + txCode + "' ");
		} else if (!transactionType.equals("all") && klienID != null
				&& invoiceNo != null && txCode.equals("ALL")) {

			System.out
					.println("3. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo
					+ "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' ");
		} else if (!transactionType.equals("all") && klienID != null
				&& invoiceNo != null && !txCode.equals("ALL")) {

			System.out
					.println("3A. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo
					+ "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' and code ='" + txCode + "' ");
		} else if (!transactionType.equals("all")
				&& transactionType.equals("reor01") && klienID == null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("4A. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEN ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "' and code like '%REOR%'");
		} else if (!transactionType.equals("all")
				&& transactionType.equals("bils0") && klienID == null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("4B. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEN ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "' and code like '%BILL%'");
		} else if (!transactionType.equals("all")
				&& transactionType.equals("per01") && klienID == null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("4C. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEN ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "' and code like '%PER%'");
		} else if (!transactionType.equals("all")
				&& transactionType.equals("skor01") && klienID == null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("4C. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEN ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "' and code like '%SKOR%'");
		} else if (!transactionType.equals("all") && klienID == null
				&& invoiceNo == null && !txCode.equals("ALL")) {

			System.out
					.println("4A. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEN ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "' and code ='" + txCode + "' ");
		} else if (transactionType.equals("all") && klienID != null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("5. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%' ");

			// sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
			// + "(  "
			// + "        select * "
			// +
			// "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
			// +
			// "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
			// + "        ORDER by transaction_time"
			// + ") as tl WHERE transaction_time between '"
			// + trxStartDate
			// + "' and '"
			// + trxEndDate
			// + "' ");

		} else if (transactionType.equals("all") && klienID != null
				&& invoiceNo == null && !txCode.equals("ALL")) {

			System.out
					.println("6. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/clientID/text()="
					+ klienID
					+ "%' and code ='" + txCode + "' ");

		} else if (transactionType.equals("all") && klienID == null
				&& invoiceNo != null && txCode.equals("ALL")) {

			System.out
					.println("7. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO NOT NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' ");
		} else if (transactionType.equals("all") && klienID == null
				&& invoiceNo != null && !txCode.equals("ALL")) {

			System.out
					.println("8. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO NOT NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and code ='" + txCode + "' ");
		} else if (transactionType.equals("all") && klienID != null
				&& invoiceNo != null && !txCode.equals("ALL")) {

			System.out
					.println("9. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo
					+ "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' and code ='" + txCode + "' ");
		} else if (transactionType.equals("all") && klienID != null
				&& invoiceNo != null && txCode.equals("ALL")) {

			System.out
					.println("10. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo
					+ "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' ");
		} else if (transactionType.equals("all") && klienID == null
				&& invoiceNo == null && !txCode.equals("ALL")) {

			System.out
					.println("11. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "' and code ='" + txCode + "' ");
		} else if (transactionType.equals("all") && klienID == null
				&& invoiceNo == null && txCode.equals("ALL")) {

			System.out
					.println("12. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO  NULL ");

			sql.append("select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select * "
					+ "        From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl WHERE transaction_time between '"
					+ trxStartDate
					+ "' and '" + trxEndDate + "'");
		}

		Query query = getSession().createSQLQuery(sql.toString());
		queryPlains = getSession().createSQLQuery(sql.toString());

		// queryUtil.setQueryParameters(query, txId, txCode,
		// channelCode, startDate, endDate, rawKey);

		query.setFirstResult(first);
		// if(max>0)
		query.setMaxResults(max);
		// System.out.println("TransactionLogDaoHibernate Query SUSKES ");

		List obj = query.list();

		// System.out.println("Dao Hibernate Query Result Size : "
		// + query.list().size());

		return obj;
	}

	@Override
	public Long findTransactionCount(String txId, String txCode,
			Date startDate, Date endDate, String rawKey) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from "
				+ "(  "
				+ "        select * "
				+ "        From  "
				+ "        (  "
				+ "                 select * from transaction_logs "
				+ "                union ALL   "
				+ "                select * from transaction_logs_housekeeping "
				+ "        ) as temp ,transactions tr,endpoint_rcs er "
				+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
				+ "        ORDER by transaction_time   "
				+ ") as tl  WHERE 1=1 ");

		// queryUtil.addParameters(sql, txId, txCode, channelCode, startDate,
		// endDate, rawKey, 0);

		Query query = getSession().createSQLQuery(sql.toString());
		// queryUtil.setQueryParameters(query, txId, txCode, channelCode,
		// startDate, endDate, rawKey);
		return Long.parseLong("" + queryPlains.list().size());
		// return Long.parseLong(s)objectData.size();
	}

	@Override
	public List<?> findTransactions(String txId, String transactionType,
			String txCode, String klienID, String invoiceNo, Date startDate,
			Date endDate, String rawKey, Transactions transaction) {

		StringBuffer strBill = new StringBuffer("bil");
		StringBuffer str01 = new StringBuffer("01");

		String trxType = transaction.getCode();

		String endpointCode = transactionType;
		if (endpointCode.contains(strBill)) {
			endpointCode = "BILL";
		} else if (endpointCode.contains(str01)) {
			endpointCode = endpointCode.substring(0, endpointCode.length() - 2);
		}

		endpointCode = endpointCode.toUpperCase();

		String queryString = "select transaction_id,to_char(transaction_time, 'dd-MM-yyyy HH24:MI:SS'),code,name,channel_code,channel_rc,description,raw,mx_rc from "
				+ "(  "
				+ "      select * "
				+ "      From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
				+ "      where temp.transaction_code = tr.code and temp.channel_rc = er.rc and "
				+ "		 er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
				+ "      ORDER by transaction_time" + ") as tl WHERE ";

		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String trxStartDate = null;
		String trxEndDate = null;

		if (startDate == null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate != null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate == null && endDate != null) {
			trxStartDate = null;
			trxEndDate = null;
		} else {
			trxStartDate = format.format(startDate);
			trxEndDate = format.format(endDate);
		}

		StringBuffer sql = new StringBuffer();
		sql.append(queryString);

		if (!transactionType.equalsIgnoreCase("all") && (invoiceNo != null)
				&& (trxStartDate == null) && (trxEndDate == null)
				&& (klienID == null) && !txCode.equalsIgnoreCase("ALL")) {
			System.out
					.println("A4. TRANSACTION TYPE SPECIFIC, INVOICE NOT NULL,  AND THE OTHER NULL, TRANSACTION CODE SPECIFIC ");

			sql.append("raw like '%/custom/invoiceNumber/text()=" + invoiceNo
					+ "%'" + " and code like '%" + endpointCode + "%'"
					+ " and code ='" + txCode + "' ");

		} else if (!transactionType.equalsIgnoreCase("all")
				&& (invoiceNo != null) && (trxStartDate == null)
				&& (trxEndDate == null) && (klienID == null)
				&& txCode.equalsIgnoreCase("ALL")) {
			System.out
					.println("A3. TRANSACTION TYPE SPECIFIC, INVOICE NOT NULL,  AND THE OTHER NULL, TRANSACTION CODE ALL ");

			sql.append("raw like '%/custom/invoiceNumber/text()=" + invoiceNo
					+ "%'" + " and code like '%" + endpointCode + "%'");

		} else if (transactionType.equalsIgnoreCase("all")
				&& (invoiceNo != null) && (trxStartDate == null)
				&& (trxEndDate == null) && (klienID == null)
				&& !txCode.equalsIgnoreCase("ALL")) {
			System.out
					.println("A2. TRANSACTION TYPE ALL, INVOICE NOT NULL,  AND THE OTHER NULL, TRANSACTION CODE SPECIFIC ");

			sql.append("raw like '%/custom/invoiceNumber/text()=" + invoiceNo
					+ "%'" + " and code ='" + txCode + "' ");

		} else if ((invoiceNo != null) && (trxStartDate == null)
				&& (trxEndDate == null) && (klienID == null)) {
			System.out.println("A1. INVOICE NOT NULL, AND THE OTHER NULL ");

			sql.append("raw like '%/custom/invoiceNumber/text()=" + invoiceNo
					+ "%' ");

		} else if (!transactionType.equalsIgnoreCase("all") && klienID != null
				&& invoiceNo == null && txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("1A.DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICE NO NULL & TRANSACTION CODE ALL ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and raw like '%/custom/clientID/text()="
					+ klienID + "%' and code like '%" + endpointCode + "%' ");

		} else if (!transactionType.equalsIgnoreCase("all") && klienID != null
				&& invoiceNo == null && !txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("1B.DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICE NO NULL & TRANSACTION CODE SPESIFIC ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and raw like '%/custom/clientID/text()="
					+ klienID + "%' and code ='" + txCode + "' ");

		} else if (!transactionType.equalsIgnoreCase("all") && klienID == null
				&& invoiceNo != null && txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("2A. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID  NULL & INVOICENO NOT NULL & TRANSACTION CODE ALL");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' ");
		} else if (!transactionType.equalsIgnoreCase("all") && klienID == null
				&& invoiceNo != null && !txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("2B. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID  NULL & INVOICENO NOT NULL & TRANSACTION CODE SPESIFIC ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and code ='" + txCode + "' ");
		} else if (!transactionType.equalsIgnoreCase("all") && klienID != null
				&& invoiceNo != null && txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("3A. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICENO NO NOTNULL & TRANSACTION CODE ALL");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' " + "and code like '%" + endpointCode
					+ "%'");
		} else if (!transactionType.equalsIgnoreCase("all") && klienID != null
				&& invoiceNo != null && !txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("3B. DENGAN JAM  & TRANSACTION TYPE SPECIFIC & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' and code ='" + txCode + "' ");
		} else if (!transactionType.equalsIgnoreCase("all") && klienID == null
				&& invoiceNo == null && txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("4A. TRANSACTION TYPE SPECIFIC & KLIEN ID  NULL & INVOICENO NULL  & TRANSACTION CODE ALL");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and code like '%" + endpointCode + "%' ");
		} else if (!transactionType.equalsIgnoreCase("all") && klienID == null
				&& invoiceNo == null && !txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("4B. TRANSACTION TYPE SPECIFIC & KLIEN ID  NULL & INVOICENO  NULL & TRANSACTION CODE SPESIFIC");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and code ='" + txCode + "' ");
		} else if (!transactionType.equalsIgnoreCase("all") && klienID == null
				&& invoiceNo != null && !txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("4C.TRANSACTION TYPE SPECIFIC & KLIEN ID  NULL & INVOICENO NOT NULL & TRANSACTION CODE SPESIFIC");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and code ='" + txCode + "'"
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%'");
		} else if (transactionType.equals("all") && klienID != null
				&& invoiceNo == null && txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("5. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and raw like '%/custom/clientID/text()="
					+ klienID + "%' ");

		} else if (transactionType.equals("all") && klienID != null
				&& invoiceNo == null && !txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("6. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICE NO NULL ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and raw like '%/custom/clientID/text()="
					+ klienID + "%' and code ='" + txCode + "' ");

		} else if (transactionType.equals("all") && klienID == null
				&& invoiceNo != null && txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("7. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO NOT NULL ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' ");
		} else if (transactionType.equals("all") && klienID == null
				&& invoiceNo != null && !txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("8. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO NOT NULL ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and code ='" + txCode + "' ");
		} else if (transactionType.equals("all") && klienID != null
				&& invoiceNo != null && !txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("9. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' and code ='" + txCode + "' ");
		} else if (transactionType.equals("all") && klienID != null
				&& invoiceNo != null && txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("10. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID NOT NULL & INVOICENO NO NOTNULL ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate
					+ "' and raw like '%/custom/invoiceNumber/text()="
					+ invoiceNo + "%' and  raw like '%/custom/clientID/text()="
					+ klienID + "%' ");
		} else if (transactionType.equals("all") && klienID == null
				&& invoiceNo == null && !txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("11. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO  NULL ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and code ='" + txCode + "' ");
		} else if (transactionType.equals("all") && klienID == null
				&& invoiceNo == null && txCode.equalsIgnoreCase("ALL")) {

			System.out
					.println("12. DENGAN JAM  & TRANSACTION TYPE ALL & KLIEND ID  NULL & INVOICENO  NULL ");

			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "'");
		}

		Query query = getSession().createSQLQuery(sql.toString());
		queryPlains = getSession().createSQLQuery(sql.toString());

		List obj = query.list();

		return obj;
	}

	@Override
	public boolean findTransactionLogByInvoiceNo(String clientId,
			String invoiceNo) {
		boolean isFind = false;
		String queryString = "SELECT * FROM transaction_logs WHERE "
				+ "transaction_code LIKE '%PAY%' " + "AND mx_rc = '00' "
				+ "AND raw LIKE '%/custom/invoiceNumber/text()=" + invoiceNo
				+ "%' " + "AND raw LIKE '%/custom/clientID/text()=" + clientId
				+ "%'";

		Query query = getSession().createSQLQuery(queryString.toString());

		if (query.uniqueResult() != null) {
			isFind = true;
		}

		return isFind;
	}

	@Override
	public List<?> findTransactionsPlusRc(String billerCode, String txCode,
			String klienID, String invoiceNo, Date startDate, Date endDate,
			EndpointRcs billerRc, String channelCode, String respCodeChannel) {

		String billerRcStr = billerRc.getRc();

		String queryString = "select "
				+ "tlw.transaction_id, "
				+ "to_char(tlw.transaction_time, 'dd-MM-yyyy HH24:MI:SS') as transaction_time, "
				+ "tlw.transaction_code, "
				+ "t.name, "
				+ "tlw.channel_code, "
				+ "tlw.channel_rc, "
				+ "erc1.description as channel_response, "
				+ "tlw.channel_id, "
				+ "tlw.biller_code, "
				+ "tlw.biller_rc, "
				+ "erc2.description as biller_description, "
				+ "tlw.mx_rc, "
				+ "tlw.invoice_no, "
				+ "tlw.client_id, "
				+ "tlw.client_name "
				+ "from "
				+ "transaction_logs_webadmin tlw "
				+ "left join transactions t on tlw.transaction_code = t.code "
				+ "left join endpoint_rcs erc1 on tlw.channel_rc = erc1.rc and erc1.endpoint_id = (select id from endpoints where code = tlw.channel_code) "
				+ "left join endpoint_rcs erc2 on erc2.endpoint_id = (select id from endpoints where code = tlw.biller_code) and tlw.biller_rc = erc2.rc "
				+ "where ";

		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String trxStartDate = null;
		String trxEndDate = null;

		if (startDate == null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate != null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate == null && endDate != null) {
			trxStartDate = null;
			trxEndDate = null;
		} else {
			trxStartDate = format.format(startDate);
			trxEndDate = format.format(endDate);
		}

		StringBuffer sql = new StringBuffer();
		sql.append(queryString);

		StringBuffer outLogger = new StringBuffer();

		if (billerCode.equalsIgnoreCase("ALL")) {
			billerCode = "";
			outLogger.append("TRANSACTION TYPE ALL & ");
		} else {
			outLogger.append("TRANSACTION TYPE SPECIFIC & ");
		}
		if (invoiceNo == null) {
			invoiceNo = "";
			outLogger.append("INVOICE NULL & ");
		} else {
			outLogger.append("INVOICE NOT NULL & ");
		}
		if (klienID == null) {
			klienID = "";
			outLogger.append("KLIEN ID NULL & ");
		} else {
			outLogger.append("KLIEN ID NOT NULL & ");
		}
		if (txCode.equalsIgnoreCase("ALL")) {
			txCode = "";
			outLogger.append("TRANSACTION CODE ALL & ");
		} else {
			outLogger.append("TRANSACTION CODE SPECIFIED & ");
		}
		if (billerRcStr == null || billerRcStr.equalsIgnoreCase("all")) {
			billerRcStr = "";
			outLogger.append("RESPONSE CODE ALL & ");
		} else {
			outLogger.append("RESPONSE CODE SPECIFIED & ");
		}

		if (trxEndDate != null && trxStartDate != null) {
			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and ");
			outLogger.append("START DATE & END DATE SPECIFIED ");
		}

		sql.append("tlw.transaction_code like '%" + txCode + "%'"
				+ " and tlw.invoice_no like '%" + invoiceNo + "%'"
				+ " and tlw.client_id like '%" + klienID + "%'"
				+ " and tlw.biller_code like'%" + billerCode + "%'"
				+ " and tlw.biller_rc like '%" + billerRcStr + "%'"
				+ "and tlw.channel_code like '%" + channelCode + "%'"
				+ "and tlw.channel_rc like '%" + respCodeChannel + "%'");

		System.out.println(outLogger);

		sql.append(" order by tlw.transaction_time");

		Query query = getSession().createSQLQuery(sql.toString());

		List obj = query.list();

		return obj;
	}

	private String sqlTransactionLogsWebadmin = "select  "
			+ "tlw2.TRANSACTION_ID, "
			+ "tlw2.client_id, "
			+ "tlw2.client_name, "
			+ "tlw2.invoice_no, "
			+ "t.name as transaction_name, "
			+ "tlw2.channel_id, "
			+ "tlw2.biller_code, "
			+ "tlw2.channel_code, "
			+ "to_char(TLW2.TRANSACTION_TIME, 'dd-MM-yyyy HH24:MI:SS') as transaction_time, "
			+ "ep.name as bank_name, "
			+ "er1.description as biller_response, "
			+ "er2.description as channel_response "
			+ "from ("
			+ "select invoice_no, "
			+ "max(transaction_time) as transaction_time "
			+ "from transaction_logs_webadmin "
			+ "group by invoice_no "
			+ ") 	tlw1, "
			+ "transaction_logs_webadmin tlw2,"
			+ "transactions t, "
			+ "endpoint_rcs er1, "
			+ "endpoint_rcs er2, "
			+ "endpoints ep "
			+ "where "
			+ "tlw2.invoice_no = tlw1.invoice_no and "
			+ "tlw1.transaction_time = tlw2.transaction_time and "
			+ "tlw2.channel_code = ep.code AND "
			+ "tlw2.transaction_code = t.code AND "
			+ "tlw2.BILLER_RC = er1.rc AND "
			+ "er1.endpoint_id = (select id from endpoints where code = tlw2.biller_code) AND "
			+ "tlw2.CHANNEL_RC = er2.rc AND "
			+ "er2.endpoint_id = (select id from endpoints where code = tlw2.channel_code) AND "
			+ "tlw2.channel_code like :channelCode and "
			+ "tlw2.invoice_no in ( :invoiceNo ) and "
			+ "tlw2.client_id like :clientId and "
			+ "tlw2.transaction_code like :transactionCode ";

	@Override
	public Map<String, Object[]> findLastTransactionLogsWebadmin(
			String channel, Set<String> invoiceNo, String clientId,
			String transactionCode) {

		Map<String, Object[]> mapResult = null;

		Query query = getSession().createSQLQuery(
				sqlTransactionLogsWebadmin.toString());
		query.setParameter("channelCode", channel.equalsIgnoreCase("all")
				? "%%"
				: "%" + channel + "%");
		query.setParameterList("invoiceNo", invoiceNo);
		query.setParameter("clientId", clientId != null
				? "%" + clientId + "%"
				: "%%");
		query.setParameter("transactionCode", transactionCode != null ? "%"
				+ transactionCode + "%" : "%%");

		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objctArray = (Object[]) obj;
				mapResult.put((String) objctArray[3], objctArray);
			}
		}

		return mapResult;
	}

	@Override
	public Map<String, Object[]> findLastSuccessTransactionLogsWebadmin(
			String channel, Set<String> invoiceNo, String clientId,
			String transactionCode, String channelRcSuccessStatus) {

		Map<String, Object[]> mapResult = null;

		sqlTransactionLogsWebadmin += "and tlw2.channel_rc = :channelRcSuccessStatus ";

		Query query = getSession().createSQLQuery(
				sqlTransactionLogsWebadmin.toString());
		query.setParameter("channelCode", channel.equalsIgnoreCase("all")
				? "%%"
				: "%" + channel + "%");
		query.setParameterList("invoiceNo", invoiceNo);
		query.setParameter("clientId", clientId != null
				? "%" + clientId + "%"
				: "%%");
		query.setParameter("transactionCode", transactionCode != null ? "%"
				+ transactionCode + "%" : "%%");
		query.setParameter("channelRcSuccessStatus", channelRcSuccessStatus);

		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objectArray = (Object[]) obj;
				mapResult.put(objectArray[3].toString(), objectArray);
			}
		}

		return mapResult;
	}

	@Override
	public Map<String, Object[]> findAllTransactionLogsWebadminPayment(
			String channel, Set<String> invoiceNo, String clientId,
			Set<String> transactionCode) {

		String sql = "select "
				+ "tlw.TRANSACTION_ID, "
				+ "tlw.client_id, "
				+ "tlw.client_name, "
				+ "tlw.invoice_no, "
				+ "t.name as transaction_name, "
				+ "tlw.channel_id, "
				+ "tlw.biller_rc, "
				+ "tlw.channel_rc, "
				+ "to_char(tlw.TRANSACTION_TIME, 'dd-MM-yyyy HH24:MI:SS') as transaction_time, "
				+ "ep.name as bank_name, "
				+ "erc1.description as channel_response, "
				+ "erc2.description as biller_response, "
				+ "tlw.transaction_code, "
				+ "tlw.biller_code "
				+ "from "
				+ "transaction_logs_webadmin tlw "
				+ "left join transactions t on tlw.transaction_code = t.code "
				+ "left join endpoints ep on tlw.channel_code = ep.code "
				+ "left join endpoint_rcs erc1 on tlw.channel_rc = erc1.rc and erc1.endpoint_id = (select id from endpoints where code = tlw.channel_code) "
				+ "left join endpoint_rcs erc2 on erc2.endpoint_id = (select id from endpoints where code = tlw.biller_code) and tlw.biller_rc = erc2.rc "
				+ "where " + "tlw.invoice_no in ( :invoiceNo ) and "
				+ "tlw.transaction_code in ( :transactionCode ) ";

		Map<String, Object[]> mapResult = null;

		if (!channel.equalsIgnoreCase("all")) {
			sql += " and tlw.channel_code = '" + channel + "' ";
		}

		if (clientId != null) {
			sql += " and tlw.client_id = '" + clientId + "' ";
		}

		Query query = getSession().createSQLQuery(sql.toString());
		query.setParameterList("invoiceNo", invoiceNo);
		query.setParameterList("transactionCode", transactionCode);

		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objctArray = (Object[]) obj;
				mapResult.put((String) objctArray[0], objctArray);
			}
		}

		return mapResult;
	}

	@Override
	public Map<String, Object[]> findAllTransactionLogsWebadminReconcile(
			String channel, Set<String> invoiceNo, String clientId,
			String transactionCode, String[] billerRc, String[] channelRc,
			Date trxDate) {

		if (invoiceNo.size() > 0) {
			String sql = "select "
					+ "tlw.TRANSACTION_ID, "
					+ "tlw.client_id, "
					+ "tlw.client_name, "
					+ "tlw.invoice_no, "
					+ "t.name as transaction_name, "
					+ "tlw.channel_id, "
					+ "tlw.biller_rc, "
					+ "tlw.channel_rc, "
					+ "to_char(tlw.TRANSACTION_TIME, 'dd-MM-yyyy HH24:MI:SS') as transaction_time, "
					+ "ep.name as bank_name, "
					+ "erc1.description as channel_response, "
					+ "erc2.description as biller_response, "
					+ "tlw.biller_code, "
					+ "tlw.recon_flag, "
					+ "tlw.channel_code, "
					+ "SPLIT_PART(SUBSTR(tlw.RAW,STRPOS(tlw.RAW, '/custom/amountTransaction2/text()')+LENGTH('/custom/amountTransaction2/text()') + 1), E'\n', 1) as payment_amount " //15
					+ "from "
					+ "transaction_logs_webadmin tlw "
					+ "left join transactions t on tlw.transaction_code = t.code "
					+ "left join endpoints ep on tlw.channel_code = ep.code "
					+ "left join endpoint_rcs erc1 on tlw.channel_rc = erc1.rc and erc1.endpoint_id = (select id from endpoints where code = tlw.channel_code) "
					+ "left join endpoint_rcs erc2 on erc2.endpoint_id = (select id from endpoints where code = tlw.biller_code) and tlw.biller_rc = erc2.rc "
					+ "where "
					+ "tlw.channel_code = :channelCode and "
					+ "tlw.client_id like :clientId and ";

			if (channelRc != null) {
				sql += "tlw.channel_rc in (:channelRc) and ";
			}

			if (billerRc != null) {
				sql += "tlw.biller_rc in (:billerRc) and ";
			}

			sql += "((tlw.transaction_time  >= to_timestamp(:trxDate, 'dd-mm-yyyy') and "
					+ "tlw.transaction_time  < to_timestamp(:trxDate, 'dd-mm-yyyy') + interval '1' day) or ";

			int sizeInvoiceList = invoiceNo.size();
			int totalList = (sizeInvoiceList / 999) + 1;

			if (totalList == 1) {
				if (sizeInvoiceList > 0) {
					sql += "tlw.invoice_no in ( :invoiceNo0 )) and ";
				}
			} else {
				if (sizeInvoiceList > 0) {
					sql += "(";
					for (int i = 0; i < totalList; i++) {
						if (i < totalList - 1) {
							sql += "tlw.invoice_no in ( :invoiceNo" + i
									+ " ) or ";
						} else {
							sql += "tlw.invoice_no in ( :invoiceNo" + i + " )";
						}
					}
					sql += ")) and ";
				}
			}

			sql += "tlw.transaction_code = :transactionCode "
					+ "order by tlw.TRANSACTION_TIME DESC";

			Map<String, Object[]> mapResult = null;

			Query query = getSession().createSQLQuery(sql.toString());
			query.setParameter("trxDate",trxDate != null ? DateUtil.convertDateToString(trxDate,"dd-MM-yyyy") : new Date());
			query.setParameter("channelCode", channel);
			List<Set<String>> invoiceNoSetList = new ArrayList<Set<String>>(
					totalList);

			if (sizeInvoiceList > 0) {
				for (int i = 0; i < totalList; i++) {
					invoiceNoSetList.add(new HashSet<String>());
				}
			}

			int index = 0;
			int invoiceNoCount = 0;
			for (String no : invoiceNo) {
				if (invoiceNoCount < 999) {
					invoiceNoSetList.get(index).add(no);
					invoiceNoCount++;
				} else {
					index++;
					invoiceNoCount = 0;
					invoiceNoSetList.get(index).add(no);
				}
			}

			if (sizeInvoiceList > 0) {
				for (int i = 0; i < totalList; i++) {
					query.setParameterList("invoiceNo" + i, invoiceNoSetList
							.get(i) != null
							? invoiceNoSetList.get(i)
							: new ArrayList<String>());
				}
			}

			// query.setParameterList("invoiceNo", invoiceNo != null ? invoiceNo
			// : new ArrayList<String>() );
			query.setParameter("clientId", clientId != null ? clientId : "%%");
			query.setParameter("transactionCode", transactionCode != null
					? transactionCode
					: "%%");
			if (channelRc != null) {
				query.setParameterList("channelRc", channelRc);
			}
			if (billerRc != null) {
				query.setParameterList("billerRc", billerRc);
			}

			List<Object> result = query.list();
			if (result != null) {
				mapResult = new HashMap<String, Object[]>();
				for (Object obj : result) {
					Object[] objctArray = (Object[]) obj;
					mapResult.put((String) objctArray[3], objctArray);
				}
			}

			return mapResult;
		}else{
		    return new HashMap<String, Object[]>();	
		}
	}

	/*
	 * @Override public Map<String, Object[]>
	 * findAllTransactionLogsWebadminReconcileByTrxDate( String channel,
	 * Set<String> invoiceNo, String clientId, String transactionCode,String[]
	 * billerRc,String[] channelRc, Date trxDate) {
	 * 
	 * String sql ="select " + "tlw.TRANSACTION_ID, " + "tlw.client_id, " +
	 * "tlw.client_name, " + "tlw.invoice_no, " + "t.name as transaction_name, "
	 * + "tlw.channel_id, " + "tlw.biller_rc, " + "tlw.channel_rc, " +
	 * "to_char(tlw.TRANSACTION_TIME, 'dd-MM-yyyy HH24:MI:SS') as transaction_time, "
	 * + "ep.name as bank_name, " + "erc1.description as channel_response, " +
	 * "erc2.description as biller_response, " + "tlw.biller_code, " +
	 * "tlw.recon_flag, " + "tlw.channel_code " + "from " +
	 * "transaction_logs_webadmin tlw " +
	 * "left join transactions t on tlw.transaction_code = t.code " +
	 * "left join endpoints ep on tlw.channel_code = ep.code " +
	 * "left join endpoint_rcs erc1 on tlw.channel_rc = erc1.rc and erc1.endpoint_id = (select id from endpoints where code = tlw.channel_code) "
	 * +
	 * "left join endpoint_rcs erc2 on erc2.endpoint_id = (select id from endpoints where code = tlw.biller_code) and tlw.biller_rc = erc2.rc "
	 * + "where " // + "tlw.channel_code = :channelCode and " +
	 * "tlw.transaction_time  >= to_timestamp(:trxDate, 'dd-mm-yyyy') and " +
	 * "tlw.transaction_time  < to_timestamp(:trxDate, 'dd-mm-yyyy') + interval '1' day and "
	 * + "tlw.client_id like :clientId and " +
	 * "tlw.channel_rc in (:channelRc) and " +
	 * "tlw.biller_rc in (:billerRc) and " +
	 * "tlw.transaction_code = :transactionCode " +
	 * "order by tlw.TRANSACTION_TIME DESC";
	 * 
	 * Map<String, Object[]> mapResult = null;
	 * 
	 * Query query = getSession().createSQLQuery(sql.toString());
	 * //query.setParameter("channelCode", channel.equalsIgnoreCase("all") ?
	 * "%%" : channel); query.setParameter("trxDate", trxDate != null ?
	 * DateUtil.convertDateToString(trxDate, "dd-MM-yyyy") : new Date());
	 * query.setParameter("clientId", clientId != null ? clientId : "%%");
	 * query.setParameter("transactionCode", transactionCode!=null ?
	 * transactionCode : "%%"); query.setParameterList("channelRc", channelRc);
	 * query.setParameterList("billerRc", billerRc);
	 * 
	 * List<Object> result = query.list(); if(result != null){ mapResult = new
	 * HashMap<String, Object[]>(); for(Object obj : result){ Object []
	 * objctArray = (Object[]) obj; mapResult.put((String) objctArray[3],
	 * objctArray); } }
	 * 
	 * return mapResult; }
	 */

	@Override
	public void insertDummyDataWebadminReconcile(String trxCode,
			String channelCode, String billerCode, String invoiceNo,
			String reconFlag, Date transactionDate) {

		String sql = "insert into transaction_logs_webadmin ( "
				+ "transaction_id, " + "transaction_time, "
				+ "transaction_code, " + "channel_id, " + "channel_code, "
				+ "channel_rc, " + "biller_code, " + "biller_rc, " + "mx_rc, "
				+ "invoice_no, " + "client_id, " + "client_name, " + "raw, "
				+ "recon_flag" + ") values (" + ":trxId, "
				+ "to_timestamp(:trxTime, 'dd-MM-yyyy'), " + ":trxCode, "
				+ "'5', " + ":channelCode, " + "'00', " + ":billerCode, "
				+ "'00', " + "'00', " + ":invoiceNo, " + "'', " + "'', "
				+ "'', " + ":reconFlag)";

		Map<String, Object[]> mapResult = null;

		Query query = getSession().createSQLQuery(sql.toString());
		query.setParameter("trxId", "DUMMY-" + UUID.randomUUID());
		query.setParameter("trxTime",
				DateUtil.convertDateToString(transactionDate, "dd-MM-yyyy"));
		query.setParameter("trxCode", trxCode != null ? trxCode : "");
		query.setParameter("channelCode", channelCode != null
				? channelCode
				: "");
		query.setParameter("billerCode", billerCode != null ? billerCode : "");
		query.setParameter("invoiceNo", invoiceNo != null ? invoiceNo : "");
		query.setParameter("reconFlag", reconFlag != null ? reconFlag : "");

		query.executeUpdate();
	}

	@Override
	public List<?> findTransactionsPlusRcWithMap(String billerCode,
			String txCode, String klienID, Set<String> invoiceNo,
			Date startDate, Date endDate, EndpointRcs endRc,
			String channelCode, String respCodeChannel) {

		String billerRcStr = endRc != null ? endRc.getRc() : null;
		billerCode = billerCode == null ? "All" : billerCode;
		txCode = txCode == null ? "All" : txCode;

		String queryString = "select "
				+ "tlw.transaction_id, " //0
				+ "to_char(tlw.transaction_time, 'dd-MM-yyyy HH24:MI:SS') as transaction_time, " //1
				+ "tlw.transaction_code, " //2
				+ "t.name, " //3
				+ "tlw.channel_code, " //4
				+ "tlw.channel_rc, " //5
				+ "erc1.description as channel_response, " //6
				+ "tlw.channel_id, " //7
				+ "tlw.biller_code, " //8
				+ "tlw.biller_rc, " //9
				+ "erc2.description as biller_description, " //10
				+ "tlw.mx_rc, " //11
				+ "tlw.invoice_no, " //12
				+ "tlw.client_id, " //13
				+ "tlw.client_name," //14
				+ "SPLIT_PART(SUBSTR(tlw.raw,STRPOS(tlw.raw, '/custom/transactionType/text()') + "
					+ "LENGTH('/custom/transactionType/text()') + 1), E'"+StringEscapeUtils.unescapeJava("\n")+"', 1) as raw2, " //15
				+ "SPLIT_PART(SUBSTR(tlw.raw,STRPOS(tlw.raw, '/custom/amountTransaction2/text()') "
					+ "+ LENGTH('/custom/amountTransaction2/text()') + 1), E'"+StringEscapeUtils.unescapeJava("\n")+"', 1) as invoice_amount, " //16
				+ "SPLIT_PART(SUBSTR(tlw.raw,STRPOS(tlw.raw, '/data/amountTransaction/text()') "
					+ "+ LENGTH('/data/amountTransaction/text()') + 1), E'"+StringEscapeUtils.unescapeJava("\n")+"', 1) as invoice_amount_inq " //17
				+ "from "
				+ "transaction_logs_webadmin tlw "
				+ "left join transactions t on tlw.transaction_code = t.code "
				+ "left join endpoint_rcs erc1 on tlw.channel_rc = erc1.rc and erc1.endpoint_id = (select id from endpoints where code = tlw.channel_code) "
				+ "left join endpoint_rcs erc2 on erc2.endpoint_id = (select id from endpoints where code = tlw.biller_code) and tlw.biller_rc = erc2.rc "
				+ "where tlw.transaction_id not like '%DUMMY%' AND ";

		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String trxStartDate = null;
		String trxEndDate = null;

		if (startDate == null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate != null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate == null && endDate != null) {
			trxStartDate = null;
			trxEndDate = null;
		} else {
			trxStartDate = format.format(startDate);
			trxEndDate = format.format(endDate);
		}

		StringBuffer sql = new StringBuffer();
		sql.append(queryString);

		StringBuffer outLogger = new StringBuffer();

		if (billerCode.equalsIgnoreCase("ALL")) {
			outLogger.append("TRANSACTION TYPE ALL & ");
		} else {
			outLogger.append("TRANSACTION TYPE SPECIFIC & ");
		}
		if (invoiceNo == null) {
			outLogger.append("INVOICE NULL & ");
		} else {
			outLogger.append("INVOICE NOT NULL & ");
		}
		if (klienID == null) {
			outLogger.append("KLIEN ID NULL & ");
		} else {
			outLogger.append("KLIEN ID NOT NULL & ");
		}
		if (txCode.equalsIgnoreCase("ALL")) {
			txCode = "";
			outLogger.append("TRANSACTION CODE ALL & ");
		} else {
			outLogger.append("TRANSACTION CODE SPECIFIED & ");
		}
		if (billerRcStr == null || billerRcStr.equalsIgnoreCase("all")) {
			outLogger.append("RESPONSE CODE ALL & ");
		} else {
			outLogger.append("RESPONSE CODE SPECIFIED & ");
		}

		if (trxEndDate != null && trxStartDate != null) {
			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and ");
			outLogger.append("START DATE & END DATE SPECIFIED ");
		}

		sql.append("tlw.transaction_code like '%" + txCode + "%'");

		if (billerCode != null && billerCode.length() > 0
				&& !billerCode.equalsIgnoreCase("all")) {
			if (respCodeChannel != null && respCodeChannel.length() > 0
					&& !respCodeChannel.equalsIgnoreCase("all")) {
				if (respCodeChannel.equalsIgnoreCase("69")
						|| respCodeChannel.equalsIgnoreCase("89")
						|| respCodeChannel.equalsIgnoreCase("91")) {
					sql.append(" and tlw.biller_code in ('" + billerCode
							+ "','')");
				} else {
					sql.append(" and tlw.biller_code in ('" + billerCode + "')");
				}
			} else {
				sql.append(" and tlw.biller_code in ('" + billerCode + "')");
			}
		}

		if (billerRcStr != null && billerRcStr.length() > 0
				&& !billerRcStr.equalsIgnoreCase("all")) {
			if (respCodeChannel != null && respCodeChannel.length() > 0
					&& !respCodeChannel.equalsIgnoreCase("all")) {
				if (respCodeChannel.equalsIgnoreCase("69")
						|| respCodeChannel.equalsIgnoreCase("89")
						|| respCodeChannel.equalsIgnoreCase("91")) {
					sql.append(" and tlw.biller_rc in ('" + billerRcStr
							+ "','')");
				} else {
					sql.append(" and tlw.biller_rc in ('" + billerRcStr + "')");
				}
			} else {
				sql.append(" and tlw.biller_rc in ('" + billerRcStr + "')");
			}
		}

		if (respCodeChannel != null && respCodeChannel.length() > 0
				&& !respCodeChannel.equalsIgnoreCase("all")) {
			sql.append(" and tlw.channel_rc = '" + respCodeChannel + "'");
		}

		if (invoiceNo != null) {
			sql.append(" and tlw.invoice_no in ( :invoiceNo )");
		}

		if (channelCode != null && channelCode.length() > 0
				&& !channelCode.equalsIgnoreCase("all")) {
			sql.append(" and tlw.channel_code = '" + channelCode + "'");
		}

		if (klienID != null) {
			sql.append(" and tlw.client_id = '" + klienID + "'");
		}

		System.out.println(outLogger);

		sql.append(" order by tlw.transaction_time");

		Query query = getSession().createSQLQuery(sql.toString());
		List obj = new ArrayList();

		try {
			if (invoiceNo != null) {
				query.setParameterList("invoiceNo", invoiceNo);
			}

			obj = query.list();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			getSessionFactory().getCurrentSession().close();
		}

		return obj;
	}

	@Override
	public List<?> findTransactionsOrderByTimeAndInvoiceNo(String billerCode,
			String txCode, String klienID, Set<String> invoiceNo,
			Date startDate, Date endDate, EndpointRcs endRc,
			String channelCode, String respCodeChannel) {

		String billerRcStr = endRc != null ? endRc.getRc() : null;
		billerCode = billerCode == null ? "All" : billerCode;
		txCode = txCode == null ? "All" : txCode;
		channelCode = channelCode == null ? "" : channelCode;
		respCodeChannel = respCodeChannel == null ? "" : respCodeChannel;

		String queryString = "select "
				+ "tlw.transaction_id, "
				+ "to_char(tlw.transaction_time, 'dd-MM-yyyy HH24:MI:SS') as transaction_time, "
				+ "tlw.transaction_code, "
				+ "t.name, "
				+ "tlw.channel_code, "
				+ "tlw.channel_rc, "
				+ "erc1.description as channel_response, "
				+ "tlw.channel_id, "
				+ "tlw.biller_code, "
				+ "tlw.biller_rc, "
				+ "erc2.description as biller_description, "
				+ "tlw.mx_rc, "
				+ "tlw.invoice_no, "
				+ "tlw.client_id, "
				+ "tlw.client_name "
				+ "from "
				+ "transaction_logs_webadmin tlw "
				+ "left join transactions t on tlw.transaction_code = t.code "
				+ "left join endpoint_rcs erc1 on tlw.channel_rc = erc1.rc and erc1.endpoint_id = (select id from endpoints where code = tlw.channel_code) "
				+ "left join endpoint_rcs erc2 on erc2.endpoint_id = (select id from endpoints where code = tlw.biller_code) and tlw.biller_rc = erc2.rc "
				+ "where ";

		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String trxStartDate = null;
		String trxEndDate = null;

		if (startDate == null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate != null && endDate == null) {
			trxStartDate = null;
			trxEndDate = null;
		} else if (startDate == null && endDate != null) {
			trxStartDate = null;
			trxEndDate = null;
		} else {
			trxStartDate = format.format(startDate);
			trxEndDate = format.format(endDate);
		}

		StringBuffer sql = new StringBuffer();
		sql.append(queryString);

		StringBuffer outLogger = new StringBuffer();

		if (billerCode.equalsIgnoreCase("ALL")) {
			billerCode = "";
			outLogger.append("TRANSACTION TYPE ALL & ");
		} else {
			outLogger.append("TRANSACTION TYPE SPECIFIC & ");
		}
		if (invoiceNo == null) {
			outLogger.append("INVOICE NULL & ");
		} else {
			outLogger.append("INVOICE NOT NULL & ");
		}
		if (klienID == null) {
			klienID = "";
			outLogger.append("KLIEN ID NULL & ");
		} else {
			outLogger.append("KLIEN ID NOT NULL & ");
		}
		if (txCode.equalsIgnoreCase("ALL")) {
			txCode = "";
			outLogger.append("TRANSACTION CODE ALL & ");
		} else {
			outLogger.append("TRANSACTION CODE SPECIFIED & ");
		}
		if (billerRcStr == null || billerRcStr.equalsIgnoreCase("all")) {
			billerRcStr = "";
			outLogger.append("RESPONSE CODE ALL & ");
		} else {
			outLogger.append("RESPONSE CODE SPECIFIED & ");
		}

		if (trxEndDate != null && trxStartDate != null) {
			sql.append("transaction_time between '" + trxStartDate + "' and '"
					+ trxEndDate + "' and ");
			outLogger.append("START DATE & END DATE SPECIFIED ");
		}

		sql.append("tlw.transaction_code like '%" + txCode + "%'"
				+ " and tlw.client_id like '%" + klienID + "%'"
				+ " and tlw.biller_code like'%" + billerCode + "%'"
				+ " and tlw.biller_rc like '%" + billerRcStr + "%'"
				+ "and tlw.channel_code like '%" + channelCode + "%'"
				+ "and tlw.channel_rc like '%" + respCodeChannel + "%'");

		if (invoiceNo != null) {
			sql.append(" and tlw.invoice_no in ( :invoiceNo )");
		}

		System.out.println(outLogger);

		sql.append(" order by  tlw.invoice_no, tlw.transaction_time ");

		Query query = getSession().createSQLQuery(sql.toString());
		List obj = new ArrayList();

		try {
			if (invoiceNo != null) {
				query.setParameterList("invoiceNo", invoiceNo);
			}

			obj = query.list();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			getSessionFactory().getCurrentSession().close();
		}

		return obj;
	}

	@Override
	public boolean updateReconFlag(String invoiceNo, String reconFlag,
			String channelRc, String billerRc, String transactionCode) {

		if (invoiceNo == null) {
			return false;
		}

		String queryString = "UPDATE transaction_logs_webadmin SET recon_flag = :reconFlag WHERE invoice_no = :invoiceNo and "
				+ "channel_rc in (:channelRc) and "
				+ "biller_rc in (:billerRc) and "
				+ "transaction_code = :transactionCode ";

		Query query = getSession().createSQLQuery(queryString);

		query.setParameter("reconFlag", reconFlag);
		query.setParameter("invoiceNo", invoiceNo);
		query.setParameter("channelRc", channelRc);
		query.setParameter("billerRc", billerRc);
		query.setParameter("transactionCode", transactionCode);

		if (query.executeUpdate() > 0) {
			return true;
		} else {
			return false;
		}

	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object[]> findAllTransactionLogsWebadminReconcileByDate(
			String channel, String transactionCode, String[] billerRc, String[] channelRc, Date trxDate) {

		String sql = "select "
				+ "tlw.TRANSACTION_ID, " //0
				+ "tlw.client_id, " //1
				+ "tlw.client_name, " //2
				+ "tlw.invoice_no, " //3
				+ "t.name as transaction_name, " //4
				+ "tlw.channel_id, " //5
				+ "tlw.biller_rc, " //6
				+ "tlw.channel_rc, " //7
				+ "to_char(tlw.TRANSACTION_TIME, 'dd-MM-yyyy HH24:MI:SS') as transaction_time, " //8
				+ "ep.name as bank_name, " //9
				+ "erc1.description as channel_response, " //10
				+ "erc2.description as biller_response, " //11
				+ "tlw.biller_code, " //12
				+ "tlw.recon_flag, " //13
				+ "tlw.channel_code, " //14
				+ "SPLIT_PART(SUBSTR(tlw.RAW,STRPOS(tlw.RAW, '/custom/amountTransaction2/text()')+LENGTH('/custom/amountTransaction2/text()') + 1), E'\n', 1) as payment_amount " //15
				+ "from "
				+ "transaction_logs_webadmin tlw "
				+ "left join transactions t on tlw.transaction_code = t.code "
				+ "left join endpoints ep on tlw.channel_code = ep.code "
				+ "left join endpoint_rcs erc1 on tlw.channel_rc = erc1.rc and erc1.endpoint_id = (select id from endpoints where code = tlw.channel_code) "
				+ "left join endpoint_rcs erc2 on erc2.endpoint_id = (select id from endpoints where code = tlw.biller_code) and tlw.biller_rc = erc2.rc "
				+ "where "
				+ "tlw.channel_code = :channelCode and ";
//				+ "tlw.client_id like :clientId and ";

		if (channelRc != null) {
			sql += "tlw.channel_rc in (:channelRc) and ";}

		if (billerRc != null) {
			sql += "tlw.biller_rc in (:billerRc) and ";}

		sql += "((tlw.transaction_time  >= to_timestamp(:trxDate, 'dd-mm-yyyy') and "
				+ "tlw.transaction_time  < to_timestamp(:trxDate, 'dd-mm-yyyy') + interval '1' day) or ";

		sql += "tlw.transaction_code = :transactionCode) "
				+ "order by tlw.TRANSACTION_TIME DESC";

		Map<String, Object[]> mapResult = null;

		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("trxDate",trxDate != null ? DateUtil.convertDateToString(trxDate,"dd-MM-yyyy") : new Date());
		query.setParameter("channelCode", channel);
//		query.setParameter("clientId", clientId != null ? clientId : "%%");
		query.setParameter("transactionCode", transactionCode != null ? transactionCode : "%%");
		
		if (channelRc != null) {
			query.setParameterList("channelRc", channelRc);}
		if (billerRc != null) {
			query.setParameterList("billerRc", billerRc);}

		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objctArray = (Object[]) obj;
				mapResult.put((String) objctArray[3], objctArray);
			}
		}
		return mapResult;			
	}
}