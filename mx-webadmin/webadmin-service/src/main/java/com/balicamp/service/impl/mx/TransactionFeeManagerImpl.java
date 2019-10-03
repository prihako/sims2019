/**
 * 
 */
package com.balicamp.service.impl.mx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	public synchronized int deleteTransactionFeeByIds(int transactionId, int channelId, String identifier) {
		return transactionFeeDao.deleteTransactionFeeByIds(transactionId, channelId, identifier);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized void updateTransactionFeeByIds(TransactionFee transactionFee) {
		transactionFeeDao.updateTransactionFeeByIds(transactionFee);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized void addNewTransactionFee(List<TransactionFee> transactionFee) {
		transactionFeeDao.saveCollection(transactionFee);
	}

	@Override
	public synchronized List<Endpoints> getEndpointsByCode(List<Integer> channelCodes) {
		return transactionFeeDao.getEndpointsByCodes(channelCodes);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized void saveAll(List<TransactionFee> allEntities) {
		transactionFeeDao.saveCollection(allEntities);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized void deleteAll(List<TransactionFee> allEntities) {
		for(TransactionFee trxfee:allEntities){
			transactionFeeDao.delete(trxfee);
		}
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
			if (dto.getId() != null && dto.getId().getIdentifier() != null && !dto.getId().getIdentifier().equals("")) {
				if (transactionFeeDao.isIdentifierExists(dto.getTransactions().getId(), dto.getEndpoints().getCode(),
						dto.getId().getIdentifier())) {
					exists.add(dto);
				}
			}
		}
		return exists;
	}

	@Override
	public List<TransactionFee> findAllTxFee(String identifier, String epCode, String description, int first, int max) {
		return transactionFeeDao.findAllTxFee(identifier, epCode, description, first, max);
	}

	@Override
	public int findAllTxFeeRowCount(String identifier, String epCode, String description) {
		return transactionFeeDao.findAllTxFeeRowCount(identifier, epCode, description);
	}

	@Override
	public TransactionFee findTransactionFee(int trxId, String epCode, String identifier) {
		return transactionFeeDao.findTransactionFee(trxId, epCode, identifier);
	}

	/**
	 * by. antin
	 * nggak bisa pake method saveOrUpdate dari hibernate karena model TransactionFee @_@ (pls check the model, it's super complicated @_@)
	 * error terjadi saat update >> belum tahu knapa @_@ sorry ^^
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized void saveOrUpdateAll(HashMap<TransactionFee, Boolean> allEntities) {
		if(allEntities != null){
			Iterator<TransactionFee> iterasi = allEntities.keySet().iterator();
			
			while(iterasi.hasNext()){
				TransactionFee obj = (TransactionFee) iterasi.next();
				Boolean exist = allEntities.get(obj);
				if(exist){ //true = exist; false = not exist
					//edit
					transactionFeeDao.updateTrxFee(obj);
				} else {
					transactionFeeDao.save(obj);
				}
			}
		}		
	}
}