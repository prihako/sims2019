package com.balicamp.dao.hibernate.mx;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.MessageLogsHousekeepingDao;
import com.balicamp.model.mx.MessageLogs;
import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.model.mx.MessageLogsParameter;

@Repository
public class MessageLogsHousekeepingDaoHibernate extends MXAdminDaoHibernate<MessageLogs, Long> implements MessageLogsHousekeepingDao {


	public MessageLogsHousekeepingDaoHibernate() {
		super(MessageLogs.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getRawMessageByParam(MessageLogsDto param) {
		Query query = getSession().createQuery(
				"SELECT msg.id.raw FROM MessageLogs AS msg "
						+ "WHERE msg.id.transactionId=:transId AND msg.id.conversionTime=:convDate "
						+ "AND msg.id.endpointCode=:endpoint AND msg.id.mappingCode=:mappingCode "
						+ "AND msg.id.isRequest=:isRequest");
		query.setParameter("transId", param.getTransactionId()).setParameter("convDate", param.getConversionTime())
				.setParameter("endpoint", param.getEndpointCode()).setParameter("mappingCode", param.getMappingCode())
				.setParameter("isRequest", param.isRequest());

		String obj = "";

		List<String> queryResult = (List<String>) findByHQL(query, -1, -1);
		if(queryResult.size()==0)
			return null;
		obj = queryResult.get(0);
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getMessageLogsByProperty(MessageLogsParameter param, int firsResult, int maxResult) {
		StringBuffer buffer = new StringBuffer();
		setQueryArgument(buffer, param);
		Query query = getSession()
				.createQuery(
						"SELECT m.id.transactionId, " +
							"m.id.conversionTime, " +
							"m.id.endpointCode, " +
							"m.id.mappingCode, " +
							"m.id.isRequest," + 
							"tlog.id.channelRc, " +
							"tlog.id.transactionTime, " +
							"tlog.id.transactionCode, " +
							"m.id.raw " + 
							//"mp.description, " +
							//"tr.name," +
							//"e.id, " + 
						"FROM MessageLogs m, " +
							"TransactionLogs tlog " +
							//"Endpoints e, " +
							//"EndpointRcs rcs, " +
							//"Transactions tr, " +
							//"Mappings mp " +
						"WHERE (m.id.transactionId=tlog.id.transactionId) " +
							//"AND tlog.id.channelCode=e.code " +
							//"AND m.id.mappingCode=mp.code " +
							//"AND (rcs.endpoints.id=e.id AND rcs.rc=tlog.id.channelRc) " +
							//"AND tlog.id.transactionCode=tr.code " +
							buffer.toString() +
						" ORDER BY m.id.conversionTime");

		query.setFirstResult(firsResult);
		query.setMaxResults(maxResult);
		setQueryParameter(query, param);
		List<Object> results = query.list();
		return results;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getCountMessageLogsByProperty(MessageLogsParameter param) {
		StringBuffer buffer = new StringBuffer();
		setQueryArgument(buffer, param);
		Query query = getSession()
				.createQuery(
						"SELECT m.id.endpointCode, COUNT(*) AS row "
								+ "FROM MessageLogs m, "
								+ "TransactionLogs tlog "
								//+ "Endpoints e, "
								//+ "EndpointRcs rcs, "
								//+ "Transactions tr, "
								//+ "Mappings mp "
								+ "WHERE (m.id.transactionId=tlog.id.transactionId) "
								//+ "AND tlog.id.channelCode=e.code "
								//+ "AND m.id.mappingCode=mp.code "
								//+ "AND (rcs.endpoints.id=e.id AND rcs.rc=tlog.id.channelRc) "
								//+ "AND tlog.id.transactionCode=tr.code " 
								+ buffer.toString()
								+ " GROUP BY m.id.endpointCode");
		setQueryParameter(query, param);
		List<Object> results = query.list();
		return results;
	}
	
	public void fillParameter() {
		
	}

	protected void setQueryParameter(Query query, MessageLogsParameter param) {
		if (param.getTransactionId() != null) {
			query.setParameter("transactionId", param.getTransactionId());
		}
		//if (param.getTransactionCode() != null && param.getTransactionDesc() == null) {
		if (param.getTransactionCode() != null) {
			query.setParameter("transactionCode", param.getTransactionCode());
		}
		/*else if(param.getTransactionCode() == null && param.getTransactionDesc()!=null){
			query.setParameter("transactionName", param.getTransactionDesc());
		}*/
		/*if (param.getTransactionDesc() != null && param.getTransactionDesc() != null) {
			query.setParameter("transactionName", param.getTransactionDesc());
			query.setParameter("transactionCode", param.getTransactionCode());
		}*/
		if (param.getEndpointCode() != null) {
			query.setParameter("endpointCode", param.getEndpointCode());
		}
		if (param.getStartDate() != null && param.getEndDate() != null) {
			query.setParameter("startDate", param.getStartDate());
			query.setParameter("endDate", param.getEndDate());
		}
		if(param.getRawKey() != null ) {
			if(param.getRawKey().length()>0)
				query.setParameter("rawKey", "%"+param.getRawKey()+"%");
		}
	}

	protected void setQueryArgument(StringBuffer buffer, MessageLogsParameter param) {
		if (param.getTransactionId() != null) {
			buffer.append("AND m.id.transactionId=:transactionId");
		}
		//if (param.getTransactionCode() != null && param.getTransactionDesc() == null) {
		if (param.getTransactionCode() != null) {
			buffer.append(" AND tlog.id.transactionCode=:transactionCode");
		}
		/*else if(param.getTransactionCode() == null && param.getTransactionDesc()!=null){
			buffer.append(" AND tr.name=:transactionName");
		}*/
		/*if (param.getTransactionDesc() != null && param.getTransactionDesc() != null) {
			buffer.append(" AND (tr.name=:transactionName OR tlog.id.transactionCode=:transactionCode)" );
		}*/
		if (param.getEndpointCode() != null) {
			buffer.append(" AND m.id.endpointCode=:endpointCode");
		}
		if (param.getStartDate() != null && param.getEndDate() != null) {
			buffer.append(" AND m.id.conversionTime >= :startDate AND m.id.conversionTime <= :endDate");
		}
		if(param.getRawKey() != null) {
			if(param.getRawKey().length()>0)
				buffer.append(" AND m.id.raw like :rawKey");
		}
	}	

}
