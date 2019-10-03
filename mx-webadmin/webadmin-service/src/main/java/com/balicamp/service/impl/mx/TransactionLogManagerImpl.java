package com.balicamp.service.impl.mx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mx.TransactionLogDAO;
import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.model.mx.TransactionLog;
import com.balicamp.model.mx.TransactionLogs;
import com.balicamp.service.TransactionLogManager;
import com.balicamp.service.impl.AbstractManager;

@Service("transactionLogManagerImpl")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class TransactionLogManagerImpl extends AbstractManager implements TransactionLogManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8468537116505364537L;

	TransactionLogDAO transactionLogDAO;

	@Autowired
	public TransactionLogManagerImpl(TransactionLogDAO transactionLogDAO) {
		this.transactionLogDAO = transactionLogDAO;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionLogs> findByClientID(String clientID) {
		// TODO Auto-generated method stub

		// List<TransactionLogs> list =
		// transactionLogDAO.findByClientID(clientID);
		return null;
	}

	@Override
	public List<TransactionLogs> findByInvoiceNo(String invoiceNo) {
		// TODO Auto-generated method stub
		// List<TransactionLog> list =
		// transactionLogDAO.findByInvoiceNo(invoiceNo);
		return null;
	}

	@Override
	public List<TransactionLogs> findByCriteriaTransaction(TransactionLog transactionLog, Date startDate, Date endDate) {
		// List<TransactionLog> list =
		// transactionLogDAO.findByCriteriaTransaction(transactionLog,
		// startDate, endDate);
		return null;
	}

	@Override
	public List<AnalisaTransactionLogsDto> findByTransaction(String klienID, String invoiceNo, String transactionType,
			String endpoint, String responseCode, Date startDate, Date endDate) {
		// TODO Auto-generated method stub

		List<Object> list = transactionLogDAO.findByTransaction(klienID, invoiceNo, transactionType, endpoint,
				responseCode, startDate, endDate);

		System.out.println("transaction logs manager impl endpoint ->" + endpoint);
		System.out.println("transaction logs manager impl responseCode ->" + responseCode);

		List<AnalisaTransactionLogsDto> result = new ArrayList<AnalisaTransactionLogsDto>();
		for (Object o : list) {

			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((String) row[1]);
			dto.setTrxCode((String) row[2]);
			dto.setTrxDesc((String) row[3]);
			dto.setChannelCode((String) row[4]);
			dto.setChannelRc((String) row[5]);
			dto.setMxRc((String) row[8]);
			dto.setRcDesc((String) row[6]);
			dto.setRaw((String) row[7]);
			dto.setKlienID((String) row[7], (String) row[5], (String) row[2], klienID);
			dto.setClientName((String) row[7], (String) row[5], (String) row[2]);
			dto.setInvoiceNo((String) row[7], (String) row[5], (String) row[2], invoiceNo);
			dto.setEndpoint((String) row[7], (String) row[5], (String) row[2]);

			if (dto.getEndpoint().equals(endpoint) && dto.getChannelRc().equals(responseCode)) {
				System.out.println("endpoint & response ->" + responseCode);

				result.add(dto);

			} else if (dto.getEndpoint().equals(endpoint) && !dto.getChannelRc().equals(responseCode)) {
				System.out.println("endpoint & response all->" + responseCode);

				result.add(dto);

			} else {
				System.out.println("endpoint & response null" + responseCode);

				result.add(new AnalisaTransactionLogsDto());
			}
		}
		return result;
	}

}
