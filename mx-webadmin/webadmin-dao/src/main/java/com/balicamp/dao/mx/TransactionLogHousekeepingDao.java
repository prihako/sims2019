/**
 *
 */
package com.balicamp.dao.mx;

import java.util.Date;
import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.TransactionLogsHousekeeping;
import com.balicamp.model.report.ReportModel;
import com.balicamp.util.wrapper.ReportCriteria;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionLogDao.java 394 2013-04-02 07:04:55Z wayan.agustina $
 */
public interface TransactionLogHousekeepingDao extends GenericDao<TransactionLogsHousekeeping, Long> {

	public List<String> findRawData(SearchCriteria searchCriteris);

	public List<?> findTransactions(String txCode, String txDesc,
			String channelCode, String txId, Date startDate, Date endDate,
			String rawKey,int first, int max);

	public Long findTransactionCount(String txId, String txCode, String txName,
			String channelCode, Date startDate, Date endDate, String rawKey);


	public List<String> findRawData(String txCode, String channelRc, Date startDate, Date endDate);

	public List<String> findRawData(List<ReportCriteria> criterias, ReportModel model);

}
