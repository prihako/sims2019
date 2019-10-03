package com.balicamp.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.balicamp.model.mx.Transactions;
import com.balicamp.model.mx.TransactionsDto;

/**
 * @author wayan.agustina
 * @version $Id: TransactionsManager.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
public interface TransactionsManager extends IManager {

	public List<Transactions> getEditableTransactionsList();

	public List<String> findAllTxCode();

	public List<TransactionsDto> findTransactions(Object txCode, Object txName, String channelCode, Date startDate,
			Date endDate, int first, int max);

	public int getRowCount(Object txCode, Object txName, String channelCode, Date startDate, Date endDate);

	public List<Transactions> findTransactionsByIds(Set<Integer> ids);

	public List<Transactions> findTransactionsForFee(int first, int max);

	public int getTransactionCountForFee();
}
