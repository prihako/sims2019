package com.balicamp.dao.hibernate.mx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.dao.mx.TransactionLogHousekeepingDao;
import com.balicamp.model.mx.TransactionLogs;
import com.balicamp.model.report.ReportModel;
import com.balicamp.util.TransactionsQueryUtil;
import com.balicamp.util.wrapper.ReportCriteria;
import com.balicamp.util.wrapper.ReportCriteriaWraper;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionLogDaoHibernate.java 431 2013-04-22 11:06:28Z rudi.sadria $
 */
@Repository
public class TransactionLogHousekeepingDaoHibernate extends MXAdminDaoHibernate<TransactionLogs, Long> implements TransactionLogHousekeepingDao {

	@Autowired
	private TransactionsQueryUtil queryUtil;
	
	public TransactionLogHousekeepingDaoHibernate() {
		super(TransactionLogs.class);
	}

	@Override
	public List<String> findRawData(SearchCriteria searchCriteris) {
		List<String> raws = null;
		
		List<TransactionLogs> logs = findByCriteria(searchCriteris);
		if(null != logs){
			raws = new ArrayList<String>();
			for (TransactionLogs transactionLogs : logs) {
				raws.add(transactionLogs.getId().getRaw());
			}

		}
		return raws;
	}

	//TODO: Hapus Join ke message log untuk production
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findTransactions(String txCode, String txName,
			String channelCode, String txId, Date startDate,
			Date endDate, String rawKey, int first, int max) {
		
		StringBuffer hql = new StringBuffer();
		hql.append(
				"select distinct txl.id.transactionId, " +
					"txl.id.transactionTime, " +
					"txl.id.transactionCode, " +
					//"tx.name, " +
					"txl.id.channelCode, " +
					"txl.id.channelRc " +
					//"eprc.description " +
				"from " +
					//"Transactions tx, " +
					"TransactionLogs txl, " +
					//"EndpointRcs eprc, " +
					"MessageLogs msgl " +
				"where " +
					//"txl.id.channelCode=eprc.endpoints.code " +
					//"and txl.id.channelRc=eprc.rc " +
					//"and tx.code=txl.id.transactionCode " +
					"txl.id.transactionId=msgl.id.transactionId "
				);
		
		queryUtil.addParameters(hql, txId, txCode, txName, 
				channelCode, startDate, endDate, rawKey, 0);
		
		hql.append(" GROUP BY " +
					"txl.id.transactionId, " +
					"txl.id.transactionTime, " +
					"txl.id.transactionCode, " +
					//"tx.name, " +
					"txl.id.channelCode, " +
					"txl.id.channelRc " +
					//"eprc.description " +
					"order by txl.id.transactionTime ");
		Query query = getSession().createQuery(hql.toString());
		queryUtil.setQueryParameters(query, txId, txCode, txName, 
				channelCode, startDate, endDate, rawKey);
		
		query.setFirstResult(first);
		query.setMaxResults(max);
		
		return query.list();
	}

	//TODO: Hapus Join ke message log untuk production
	@Override
	public Long findTransactionCount(String txId, String txCode, String txName, String channelCode,
			Date startDate, Date endDate, String rawKey) {
		
		StringBuffer hql = new StringBuffer();
		hql.append(
				"select count(distinct txl.id.transactionId) " +
				"from " +
					//"Transactions tx, " +
					"TransactionLogs txl, " +
					//"EndpointRcs eprc, " +
					"MessageLogs msgl " +
				"WHERE " +
					//"txl.id.channelCode=eprc.endpoints.code " +
					//"and txl.id.channelRc=eprc.rc " +
					//"and tx.code=txl.id.transactionCode " +
					"txl.id.transactionId=msgl.id.transactionId ");
		queryUtil.addParameters(hql, txId, txCode, txName, channelCode, 
				startDate, endDate, rawKey, 0);
		Query query = getSession().createQuery(hql.toString());
		queryUtil.setQueryParameters(query, txId, txCode, txName, channelCode, 
				startDate, endDate, rawKey);
		
		return (Long) query.uniqueResult();
	}	
	

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findRawData(String txCode, String channelRc,
			Date startDate, Date endDate) {

		Query query = getSession().createQuery("select a.id.raw from TransactionLogs a " +
				" where  " +
				" a.id.transactionCode=:trxCode " +
				" and a.id.channelRc=:channelRc " +
				" and date(a.id.transactionTime) >= date(:startDate) "+
				" and date(a.id.transactionTime) <= date(:endDate) " +
				" ");
		query.setParameter("trxCode", txCode);
		query.setParameter("channelRc", channelRc);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
				
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> findReversalRecords(List<ReportCriteria> criteriasOri) {
		List<ReportCriteria> criterias = new ArrayList<ReportCriteria>(criteriasOri.size());
		for(int i = 2; i < criteriasOri.size(); i++) {
			criterias.add(criteriasOri.get(i));
		}

		//criterias.add(new ReportCriteria(ReportCriteria.MIDDLE_LIKE, "id.raw", "/data/logRetrievalKey/text()="+retrievalKey));
		criterias.add(new ReportCriteria(ReportCriteria.EQUALS, "id.channelRc", "00"));
		criterias.add(new ReportCriteria(ReportCriteria.EQUALS, "id.type", "reversal"));
		
		List<String> fieldToSelect = new ArrayList<String>();
		fieldToSelect.add("id.raw");
		fieldToSelect.add("id.retrievalKey");
		String hql = ReportCriteriaWraper.getRestrictionsHQL(fieldToSelect,criterias, TransactionLogs.class);
		Query query = getSession().createQuery(hql);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findSuccessRecords(List<ReportCriteria> criteriasOri) {
		List<ReportCriteria> criterias = new ArrayList<ReportCriteria>(criteriasOri.size());
		for(int i = 2; i < criteriasOri.size(); i++) {
			criterias.add(criteriasOri.get(i));
		}
		
		criterias.add(new ReportCriteria(ReportCriteria.EQUALS, "id.channelRc", "00"));
		List<String> fieldToSelect = new ArrayList<String>();
		fieldToSelect.add("id.raw");
		fieldToSelect.add("id.retrievalKey");
		String hql = ReportCriteriaWraper.getRestrictionsHQL(fieldToSelect,criterias, TransactionLogs.class);
		Query query = getSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findRawData(List<ReportCriteria> criterias, ReportModel model) {
		List<String> lstSelect = new ArrayList<String>();
		lstSelect.add("id.raw"); 
		lstSelect.add("id.retrievalKey");
		String strQry = ReportCriteriaWraper.getRestrictionsHQL(lstSelect,criterias, TransactionLogs.class);
		Query qry = getSession().createQuery(strQry);
		
		List<Object[]> result = qry.list();
		
		List<String> txLogs = new ArrayList<String>();
		List<Object[]> reversalRecords = findReversalRecords(criterias);
		
		if(model.getKodeReport().trim().endsWith("_SUK")) {
			for(int i = 0; i < result.size(); i++) {
				String txRetrievalKey = (String) result.get(i)[1];
				boolean revRetrievalKeyFound = false;
				
				for(Object[] revRaw : reversalRecords) {
					if(((String)revRaw[0]).contains(txRetrievalKey)) {
						revRetrievalKeyFound = true;
						break;
					}
				}
				if(!revRetrievalKeyFound)
					txLogs.add((String) result.get(i)[0]);
			}
		} else if(model.getKodeReport().trim().endsWith("_GAG")) {
			List<Object[]> successRecords = findSuccessRecords(criterias);
			for(int i = 0; i < result.size(); i++) {
				txLogs.add((String) result.get(i)[0]);
			}
			
			for(Object[] revRecord : reversalRecords) {
				for(Object[] sucRaw : successRecords) {
					if(((String)sucRaw[0]).contains((String)revRecord[1]))  {
						txLogs.add((String)revRecord[0]);
					}
				}
			}
		} else {
			for(int i = 0; i < result.size(); i++) {
				txLogs.add((String) result.get(i)[0]);
			}
		}
		return txLogs;
	}
}