/**
 * 
 */
package com.balicamp.service;

import java.util.List;

import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.TransactionFee;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeManager.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
public interface TransactionFeeManager extends IManager {

	/**
	 * Delete to transaction_fee
	 * @param transactionId
	 * @param channelId
	 * @param identifier
	 */
	int deleteTransactionFeeByIds(int transactionId, int channelId, String identifier);

	/**
	 * Update to transaction_fee
	 * @param transactionFee
	 */
	public void updateTransactionFeeByIds(TransactionFee transactionFee);

	/**
	 * Save 1 record to transaction_fee
	 * @param transactionFee
	 */
	public void addNewTransactionFee(List<TransactionFee> transactionFee);

	public List<Endpoints> getEndpointsByCode(List<Integer> channelCodes);

	public void saveAll(List<TransactionFee> allEntities);

	int findAllTxFeeRowCount(String identifier, String epCode);

	List<TransactionFee> findAllTxFee(String identifier, String epCode, int first, int max);

	List<TransactionFee> isIdentifierExists(List<TransactionFee> fees);
}
