/**
 *
 */
package com.balicamp.dao.mx;

import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.model.mx.TransactionFeeId;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeDao.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
public interface TransactionFeeDao extends GenericDao<TransactionFee, TransactionFeeId> {

	int deleteTransactionFeeByIds(int transactionId, int channelId, String identifier);

	public void updateTransactionFeeByIds(TransactionFee transactionFee);

	public List<Endpoints> getEndpointsByCodes(List<Integer> channelCodes);

	void saveAll(List<TransactionFee> allEntities);

	void updateTrxFee(TransactionFee entity);
	
	List<TransactionFee> findAllTxFee(String identifier, String epCode,
			int first, int max);

	List<TransactionFee> findAllTxFee(String identifier, String epCode, String description,
			int first, int max);

	int findAllTxFeeRowCount(String identifier, String epCode);

	int findAllTxFeeRowCount(String identifier, String epCode, String description);

	public boolean isIdentifierExists(TransactionFee dto);

	public boolean isIdentifierExists(String identifier);

	public boolean isIdentifierExists(int trxId, String epCode, String identifier);

	TransactionFee findTransactionFee(int trxId, String epCode, String identifier);
	
	void setIdentifier(TransactionFee entity);
}