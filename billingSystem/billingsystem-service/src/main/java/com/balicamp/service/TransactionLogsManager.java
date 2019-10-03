package com.balicamp.service;

import java.util.Date;
import java.util.List;

import com.balicamp.model.mx.AnalisaTransactionLogsDto;

public interface TransactionLogsManager extends IManager {

	public List<AnalisaTransactionLogsDto> findTransactions(String txId, String txCode, String txName,
			String channelCode, Date startDate, Date endDate, String rawKey, int first, int max);

	public Long getRowCount(String txId, String txCode, String txName, String channelCode, Date startDate,
			Date endDate, String rawKey);

	List<String> findMessageLogsByTxId(String txId);

}
