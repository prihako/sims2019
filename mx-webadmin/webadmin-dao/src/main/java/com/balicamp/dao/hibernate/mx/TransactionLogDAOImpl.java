package com.balicamp.dao.hibernate.mx;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.mx.TransactionLog;
import com.balicamp.model.mx.TransactionLogs;

@Repository
public class TransactionLogDAOImpl extends
		AdminGenericDaoImpl<TransactionLogs, Long> implements TransactionLogDAO {

	private static final String QUERY = " from TransactionLog where ";

	public TransactionLogDAOImpl() {
		super(TransactionLogs.class);
	}

//	@Override
//	public List<TransactionLogs> findByClientID(String clientID) {
//		// TODO Auto-generated method stub
//
//		List<TransactionLogs> list = null;
//		String extraQuery = "clientID ='" + clientID + "'";
//		Query query = getSessionFactory().getCurrentSession().createQuery(
//				QUERY + extraQuery);
//		list = query.list();
//
//		return list;
//	}
//
//	@Override
//	public List<TransactionLogs> findByInvoiceNo(String invoiceNo) {
//		// TODO Auto-generated method stub
//
//		List<TransactionLogs> list = null;
//		String extraQuery = "invoiceNo ='" + invoiceNo + "'";
//		Query query = getSessionFactory().getCurrentSession().createQuery(
//				QUERY + extraQuery);
//		list = query.list();
//
//		return list;
//	}

	

	@Override
	public List<Object> findByTransaction(String klienID, String invoiceNo,
			String transactionType, String endpoint, String responseCode,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub

		String queryTransactionLog=null;
	
		
		if(transactionType.equals("0")  && responseCode.equals("0")){
			queryTransactionLog = "select transaction_id, transaction_time,code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select *   From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl  WHERE 1=1 ";
		}else if(transactionType.equals("0")  && responseCode.equals("0")){
			queryTransactionLog = "select transaction_id, transaction_time,code,name,channel_code,channel_rc,description,raw,mx_rc from "
					+ "(  "
					+ "        select *   From   transaction_logs as temp ,transactions tr,endpoint_rcs er "
					+ "        where temp.transaction_code = tr.code and temp.channel_rc = er.rc and er.endpoint_id = (select id from endpoints where code=temp.channel_code) "
					+ "        ORDER by transaction_time"
					+ ") as tl  WHERE 1=1 ";
		}

			
		
		List<Object> list=null;
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				queryTransactionLog);
		list = query.list();
		
		return list;
	}

//	@Override
//	public List<TransactionLogs> findByCriteriaTransaction(
//			TransactionLogs transactionLog, Date startDate, Date endDate) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
