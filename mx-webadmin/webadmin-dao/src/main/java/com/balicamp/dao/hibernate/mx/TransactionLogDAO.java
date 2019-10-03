package com.balicamp.dao.hibernate.mx;

import java.util.Date;
import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.model.mx.TransactionLog;
import com.balicamp.model.mx.TransactionLogs;
import com.balicamp.model.mx.Transactions;

public interface TransactionLogDAO extends AdminGenericDao<TransactionLogs, Long>{

	List<Object> findByTransaction (String klienID, String invoiceNo, String transactionType, String endpoint, String responseCode , Date startDate, Date endDate);

}
