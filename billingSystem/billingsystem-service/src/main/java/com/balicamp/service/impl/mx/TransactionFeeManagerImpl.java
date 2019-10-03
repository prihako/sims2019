/**
 * 
 */
package com.balicamp.service.impl.mx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.mx.TransactionFeeDao;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.service.TransactionFeeManager;
import com.balicamp.service.impl.AbstractManager;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeManagerImpl.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
@Service("transactionFeeManagerImpl")
public class TransactionFeeManagerImpl extends AbstractManager implements TransactionFeeManager {

	private static final long serialVersionUID = 1L;

	@Autowired
	private TransactionFeeDao transactionFeeDao;

	@Override
	public TransactionFeeDao getDefaultDao() {
		return transactionFeeDao;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int deleteTransactionFeeByIds(int transactionId, int channelId, String identifier) {
		return transactionFeeDao.deleteTransactionFeeByIds(transactionId, channelId, identifier);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateTransactionFeeByIds(TransactionFee transactionFee) {
		transactionFeeDao.updateTransactionFeeByIds(transactionFee);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addNewTransactionFee(List<TransactionFee> transactionFee) {
		transactionFeeDao.saveCollection(transactionFee);
	}

	@Override
	public List<Endpoints> getEndpointsByCode(List<Integer> channelCodes) {
		return transactionFeeDao.getEndpointsByCodes(channelCodes);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveAll(List<TransactionFee> allEntities) {
		transactionFeeDao.saveAll(allEntities);
	}

	@Override
	public List<TransactionFee> findAllTxFee(String identifier, String epCode, int first, int max) {
		return transactionFeeDao.findAllTxFee(identifier, epCode, first, max);
	}

	@Override
	public int findAllTxFeeRowCount(String identifier, String epCode) {
		return transactionFeeDao.findAllTxFeeRowCount(identifier, epCode);
	}

	@Override
	public List<TransactionFee> isIdentifierExists(List<TransactionFee> fees) {
		List<TransactionFee> exists = new ArrayList<TransactionFee>();
		for (TransactionFee dto : fees) {
			if (transactionFeeDao.isIdentifierExists(dto)) {
				exists.add(dto);
			}
		}
		return exists;
	}

}