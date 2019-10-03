/**
 *
 */
package com.balicamp.dao.hibernate.mx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.TransactionsDao;
import com.balicamp.model.mx.Transactions;
import com.balicamp.util.TransactionsQueryUtil;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionsDaoHibernate.java 518 2013-06-25 10:13:37Z rudi.sadria $
 */
@Repository("transactionsDaoHibernate")
public class TransactionsDaoHibernate extends MxGenericDaoHibernate<Transactions, Long> implements TransactionsDao {

	private final int paramIndex = 0;

	@Autowired
	private TransactionsQueryUtil queryUtil;

	public TransactionsDaoHibernate() {
		super(Transactions.class);
	}

	@Override
	public String findByFieldName(String transCode) {
		Map<String, Object> filter = new HashMap<String, Object>(0);
		filter.put("code", transCode);
		return findSingleByFieldName(filter).getName();
	}

	/**
	 * Native PostgreSQL query
	 */
	@Override
	public List<?> findSummaryTransactions(Object txCode, Object txName, String channelCode,
			Date startDate,Date endDate, int first, int max) {

		long startTime = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer();
		sql.append("select temp.channel_code, "+
						"temp.transaction_description, "+
						"temp.transaction_code, " +
						"temp.channel_rc, "+
						"count(temp.channel_rc) as count "+
						"from "+
							"("+
							"select "+
							"tl.transaction_id, "+
							"tl.transaction_time, "+
							"tl.transaction_code, "+
							"ts.name as transaction_description, "+
							"ep.name as endpoint_name, "+
							"tl.channel_code, " +
							"tl.channel_rc, "+
							"er.description, "+
							"tl.raw "+
							"from "+
							"(select * from transaction_logs union ALL select * from transaction_logs_housekeeping) tl, "+
							"transactions ts, "+
							"endpoints ep, "+
							"endpoint_rcs er "+
							"where "+
							"tl.transaction_code=ts.code "+
							"and tl.channel_code=ep.code "+
							"and tl.channel_rc=er.rc "+
								"and ep.id=er.endpoint_id ");

				addNativeParameters(sql, txCode, txName, channelCode, startDate, endDate);

				sql.append("order by transaction_time"+
						") as temp "+
						"where 1=1 "+
						"group BY temp.transaction_code, " +
						"temp.transaction_description, " +
						"temp.channel_code,temp.channel_rc " +
						"order by temp.transaction_code");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		setNativateParamValues(query, txCode, txName, channelCode, startDate, endDate);
		query.setFirstResult(first);
		//query.setMaxResults(max);
		List<?> result = query.list();

		System.err.println("Query Time: "+(System.currentTimeMillis() - startTime));
		return result;
	}

	private void setNativateParamValues(SQLQuery query, Object txCode,
			Object txName, String channelCode,	Date startDate,Date endDate ) {
		if(txCode != null) {
			int index = 0;
			List<String> codes = (List<String>) txCode;
			for(String code : codes) {
				query.setString("txCode"+(index++), code);
			}
		}
		if(txName != null) {
			int index = 0;
			List<String> names = (List<String>) txName;
			for(String name : names) {
				query.setString("txName"+(index++), name);
			}
		}
		if(channelCode != null) query.setString("channelCode", channelCode);
		if(startDate != null) query.setTimestamp("startDate", startDate);
		if(endDate != null) query.setTimestamp("endDate", endDate);
	}

	private void addNativeParameters(StringBuffer sql, Object txCode,
			Object txName, String channelCode,	Date startDate,Date endDate) {
		if(txCode != null) {
			int index = 0;
			List<String> codes = (List<String>) txCode;
			if(!codes.isEmpty()) sql.append("and tl.transaction_code in (");
			for(; index < codes.size(); index++) {
				sql.append(":txCode"+(index)+", ");
			}
			sql.replace(sql.length()-2, sql.length(), ")");
		}
		if(txName != null) {
			int index = 0;
			List<String> names = (List<String>) txName;
			if(!names.isEmpty()) sql.append("and ts.name in (");
			for(;index < names.size(); index++) {
				sql.append(":txName"+(index)+", ");
			}
			sql.replace(sql.length()-2, sql.length(), ")");
		}
		sql.append(channelCode == null ? "" : "and tl.channel_code = :channelCode ");
		sql.append(startDate == null ? "" : "and tl.transaction_time >= :startDate ");
		sql.append(endDate == null ? "" : "and tl.transaction_time <= :endDate ");
	}

	/**
	 * Native PostgreSQL Query.
	 */
	@Override
	public int getRowCountSummary(Object txCode, Object txName, String channelCode,
			Date startDate, Date endDate) {

		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from (select distinct c.channel_code, c.transaction_description, c.transaction_code  from (select  temp.transaction_description, "+
				"temp.channel_code, "+
				"temp.channel_rc, "+
				"temp.transaction_code, "+
				"count(temp.channel_rc) as count "+
				"from "+
					"("+
					"select "+
					"tl.transaction_id, "+
					"tl.transaction_time, "+
					"tl.transaction_code, "+
					"ts.name as transaction_description, "+
					"ep.name as endpoint_name, "+
					"tl.channel_code, " +
					"tl.channel_rc, "+
					"er.description, "+
					"tl.raw "+
					"from "+
						"(select * from transaction_logs union ALL select * from transaction_logs_housekeeping) tl, "+
						"transactions ts, "+
						"endpoints ep, "+
						"endpoint_rcs er "+
						"where "+
						"tl.transaction_code=ts.code "+
						"and tl.channel_code=ep.code "+
						"and tl.channel_rc=er.rc "+
						"and ep.id=er.endpoint_id ");

		addNativeParameters(sql, txCode, txName, channelCode, startDate, endDate);

		sql.append("order by transaction_time"+
				") as temp "+
				"where 1=1 "+
				"group BY temp.transaction_description, " +
				"temp.channel_code,temp.channel_rc, temp.transaction_code) as c) as d");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		setNativateParamValues(query, txCode, txName, channelCode, startDate, endDate);

		return Long.valueOf(query.uniqueResult().toString()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Transactions> findTransactionsByIds(Set<Integer> ids) {
		if(ids == null || ids.size() < 1) return new ArrayList<Transactions>();

		StringBuffer hql = new StringBuffer("select a from Transactions a where 1=1 ");
		int index = 0;
		for (; index < ids.size(); index++) {
			hql.append(" OR a.id=:id" + index);
		}
		Query query = getSession().createQuery(hql.toString());

		index--;
		List<Integer> listId = new ArrayList<Integer>(ids);
		for(; index >= 0; index--) {
			query.setParameter("id"+index, listId.get(index));
		}

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Transactions> findTransactionsForFee(int first, int max) {
		StringBuffer hql = new StringBuffer();
		/*hql.append("select tx from Transactions tx " +
				"where tx.id not in (select distinct tf.id.transactionId " +
				"from TransactionFee tf)");*/
		hql.append("select tx from Transactions tx where 1=1 order by name");

		Query query = getSession().createQuery(hql.toString());
		if(first>0)
			query.setFirstResult(first);
		if(max>0)
			query.setMaxResults(max);
		return query.list();
	}

	@Override
	public int getTransactionCountForFee() {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(tx.id) from Transactions tx " +
				"where tx.id not in (select distinct tf.id.transactionId " +
				"from TransactionFee tf)");

		Query query = getSession().createQuery(hql.toString());
		return Long.valueOf(query.uniqueResult().toString()).intValue();
	}

	@Override
	public Transactions findTransactionsByCode(String trxCode) {
		Query query = getSession().createQuery(
				"SELECT e FROM Transactions AS e " + "WHERE e.code=:code ");
		query.setParameter("code", trxCode);
		return (Transactions) query.uniqueResult();
	}
	
	@Override
	public List<Transactions> findByCode(String trxCode) {
		
		if (trxCode.equals("ALL")){
			Query query = getSession().createQuery(
					"SELECT e FROM Transactions AS e " + "WHERE e.code=:code ");
//			query.setParameter("code", trxCode);
			return (List<Transactions>) query.list();

		}else{
			Query query = getSession().createQuery(
					"SELECT e FROM Transactions AS e " + "WHERE e.code like '%"+trxCode+"%'");
//			query.setParameter("code", trxCode);
			return (List<Transactions>) query.list();

		}
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Transactions> getTransactionsList() {
		Query query = getSession().createQuery("SELECT e FROM Transactions AS e ORDER BY e.code, e.name");
		return query.list();
	}
}