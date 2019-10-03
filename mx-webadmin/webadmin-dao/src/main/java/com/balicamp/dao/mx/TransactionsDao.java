/**
 *
 */
package com.balicamp.dao.mx;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.Transactions;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionsDao.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
public interface TransactionsDao extends GenericDao<Transactions, Long> {
	public String findByFieldName(String transCode);

	public List<?> findSummaryTransactions(Object txCode, Object txName, String channelCode,
			Date startDate,Date endDate, int first, int max);

	public int getRowCountSummary(Object txCode, Object txName, String channelCode, Date startDate, Date endDate);

	public List<Transactions> findTransactionsByIds(Set<Integer> ids);

	public List<Transactions> findTransactionsForFee(int first,
			int max);

	public int getTransactionCountForFee();

	Transactions findTransactionsByCode(String trxCode);
	List<Transactions> findByCode(String trxCode);


	public List<Transactions> getTransactionsList();
}
