/**
 * 
 */
package com.balicamp.dao.mx;

import java.util.Date;
import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.TransactionLogs;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionsDao.java 346 2013-02-21 07:03:47Z wayan.agustina $
 */
public interface TransactionsHousekeepingDao extends GenericDao<TransactionLogs, Long> {
	
	//public String findByFieldName(String transCode);
	
	public List<Object> findTransactions(Object txCode, Object txName, String channelCode, Date startDate,Date endDate, int first, int max);
	
	public int getRowCount(Object txCode, Object txName, String channelCode, Date startDate, Date endDate);

	//public List<Transactions> findTransactionsByIds(Set<Integer> ids);

	//public List<Transactions> findTransactionsForFee(int first, int max);

	//public int getTransactionCountForFee();
	
}
