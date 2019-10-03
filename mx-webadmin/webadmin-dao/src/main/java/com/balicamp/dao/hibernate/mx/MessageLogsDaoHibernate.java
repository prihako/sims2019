/**
 *
 */
package com.balicamp.dao.hibernate.mx;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.MessageLogsDao;
import com.balicamp.model.mx.MessageLogs;
import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.model.mx.MessageLogsParameter;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MessageLogsDaoHibernate.java 503 2013-05-24 08:13:16Z
 *          rudi.sadria $
 */
@Repository
public class MessageLogsDaoHibernate extends
		MxGenericDaoHibernate<MessageLogs, Long> implements MessageLogsDao {

	public MessageLogsDaoHibernate() {
		super(MessageLogs.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getRawMessageByParam(MessageLogsDto param) {
		Query query = getSession()
				.createQuery(
						"SELECT msg.id.raw FROM MessageLogs AS msg "
								+ "WHERE msg.id.transactionId=:transId AND msg.id.conversionTime=:convDate "
								+ "AND msg.id.endpointCode=:endpoint AND msg.id.mappingCode=:mappingCode "
								+ "AND msg.id.isRequest=:isRequest");
		query.setParameter("transId", param.getTransactionId())
				.setParameter("convDate", param.getConversionTime())
				.setParameter("endpoint", param.getEndpointCode())
				.setParameter("mappingCode", param.getMappingCode())
				.setParameter("isRequest", param.isRequest());

		String obj = "";

		List<String> queryResult = (List<String>) findByHQL(query, -1, -1);
		if (queryResult.size() == 0)
			return null;
		obj = queryResult.get(0);
		return obj;
	}

	@Override
	public List<?> getMessageLogsByProperty(MessageLogsParameter param,
			int firsResult, int maxResult) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select transaction_id,conversion_time,endpoint_code,mapping_code,is_request,raw,description from "
				+ "(   "
				+ "        select * "
				+ "        From   "
				+ "        ( "
				+ "                select * from message_logs "
				+ "                union ALL   "
				+ "                select * from message_logs_housekeeping "
				+ "        ) as temp ,mappings m "
				+ "        where temp.mapping_code = m.code "
				+ "        ORDER by conversion_time    "
				+ ") as tl where 1=1    ");
		setQueryArgument(buffer, param);
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(buffer.toString());
		setQueryParameter(query, param);

		if (firsResult >= 0)
			query.setFirstResult(firsResult);
		if (maxResult >= 0)
			query.setMaxResults(maxResult);
		List<?> results = query.list();
		return results;
	}

	@Override
	public Long getCountMessageLogsByProperty(MessageLogsParameter param) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(*) from " + "(   " + "        select * "
				+ "        From   " + "        ( "
				+ "                select * from message_logs "
				+ "                union ALL   "
				+ "                select * from message_logs_housekeeping "
				+ "        ) as temp ,mappings m "
				+ "        where temp.mapping_code = m.code "
				+ "        ORDER by conversion_time    "
				+ ") as tl where 1=1    ");
		setQueryArgument(buffer, param);
		Query query = getSession().createSQLQuery(buffer.toString());
		setQueryParameter(query, param);

		return Long.parseLong("" + query.uniqueResult());
	}

	public void fillParameter() {

	}

	protected void setQueryParameter(Query query, MessageLogsParameter param) {

		for (String p : query.getNamedParameters()) {
			if (p.startsWith("transactionId")) {
				query.setParameter(p, param.getTransactionId());
			}
			if (p.startsWith("endpointCode")) {
				query.setParameter(p, param.getEndpointCode());
			}
			if (p.startsWith("startDate")) {
				query.setParameter(p, param.getStartDate());
			}
			if (p.startsWith("endDate")) {
				query.setParameter(p, param.getEndDate());
			}
			if (p.startsWith("rawKey")) {
				if (param.getRawKey().length() > 0)
					query.setParameter(p, "%" + param.getRawKey() + "%");
			}
		}
	}

	protected void setQueryArgument(StringBuffer buffer,
			MessageLogsParameter param) {
		if (param.getTransactionId() != null) {
			buffer.append("AND transaction_id=:transactionId0 ");
		}
		if (param.getEndpointCode() != null) {
			buffer.append(" AND endpoint_code=:endpointCode0 ");
		}
		if (param.getStartDate() != null && param.getEndDate() != null) {
			buffer.append(" AND conversion_time BETWEEN :startDate0 AND :endDate0 ");
		}
		if (param.getRawKey() != null) {
			if (param.getRawKey().length() > 0)
				buffer.append(" AND raw like :rawKey0 ");
		}
	}

	@Override
	public Object getMessageLogsData(String trxId, String endpointCode,
			boolean isRequest) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
//		buffer.append("select transaction_id,conversion_time,endpoint_code,mapping_code,is_request,raw,description from "
//				+ " message_logs as mlogs , mappings m "
//				+ " where mlogs.mapping_code = m.code "
//				+ " and  transaction_id='"
//				+ trxId
//				+ "' and endpoint_code ='"
//				+ endpointCode + "' and is_request ='" + isRequest + "' ");
		
		buffer.append("select raw from "
				+ " message_logs as mlogs , mappings m "
				+ " where mlogs.mapping_code = m.code "
				+ " and  transaction_id='"
				+ trxId
				+ "' and endpoint_code ='"
				+ endpointCode + "' and is_request ='" + isRequest + "' ");

		Query query = getSession().createSQLQuery(buffer.toString());

		return query.uniqueResult();
	}
}
