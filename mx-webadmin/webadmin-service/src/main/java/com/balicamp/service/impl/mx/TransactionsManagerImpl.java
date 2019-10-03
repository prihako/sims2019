package com.balicamp.service.impl.mx;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.mx.TransactionsDao;
import com.balicamp.model.mx.Transactions;
import com.balicamp.model.mx.TransactionsDto;
import com.balicamp.service.TransactionsManager;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.parameter.SystemParameterManager;

@Service("transactionsManagerImpl")
public class TransactionsManagerImpl extends AbstractManager implements TransactionsManager {
	private static final long serialVersionUID = 4532965580425560319L;

	@Autowired
	private TransactionsDao transactionsDao;

	@Autowired
	private SystemParameterManager parameterManager;

	@Override
	public Object getDefaultDao() {
		return transactionsDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findAllTxCode() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Transactions.class);
		criteria.setProjection(Projections.property("code"));
		criteria.addOrder(Order.asc("code"));
		return transactionsDao.getCurrentHibernateTemplate().findByCriteria(criteria);
	}

	public List<Transactions> findAll() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Transactions.class);
		criteria.addOrder(Order.asc("name"));
		return transactionsDao.getCurrentHibernateTemplate().findByCriteria(criteria);
	}

	private HashMap<String, HashMap<String, String>> getResponseCodeSuspect(String rawResponseCodeSuspect) {
		HashMap<String, HashMap<String, String>> listResponseCodeSuspect = new HashMap<String, HashMap<String, String>>();

		String[] listEndpoint = rawResponseCodeSuspect.split(";");
		for (int i = 0; i < listEndpoint.length; i++) {
			String[] rawPerEndpoint = listEndpoint[i].split(":");
			String endpoint = rawPerEndpoint[0];
			HashMap<String, String> responseCodes = new HashMap<String, String>();
			String listRc[] = rawPerEndpoint[1].split(",");
			for (int j = 0; j < listRc.length; j++) {
				responseCodes.put(listRc[j], listRc[j]);
			}
			listResponseCodeSuspect.put(endpoint, responseCodes);
		}

		return listResponseCodeSuspect;
	}

	@Override
	public List<TransactionsDto> findSummaryTransactions(Object txCode, Object txName, String channelCode,
			Date startDate, Date endDate, int first, int max) {

		List<Object> result = (List<Object>) transactionsDao.findSummaryTransactions(txCode, txName, channelCode,
				startDate, endDate, first, max);
		String rawResponseCodeSuspect = parameterManager.findByParamName("message.responseCode.suspect")
				.getParamValue();

		Map<String, TransactionsDto> logsMap = new HashMap<String, TransactionsDto>();
		HashMap<String, HashMap<String, String>> responseCode = getResponseCodeSuspect(rawResponseCodeSuspect);
		for (Object o : result) {
			Object[] row = (Object[]) o;
			String cc = (String) row[0];
			String tn = (String) row[1];
			String tc = (String) row[2];
			String cr = (String) row[3];

			if (cr == null)
				cr = "ZZ";

			int count = new BigDecimal(row[4].toString()).intValue();

			TransactionsDto dto = new TransactionsDto();
			dto.setChannelCode(cc);
			dto.setTransactionName(tn);
			dto.setTransactionCode(tc);
			dto.setChannelRc(cr);
			dto.setCount(count);

			String key = getLogKeys(row);
			TransactionsDto tmpDto = logsMap.get(key);
			if (tmpDto == null) {
				dto.setGagal(0);
				dto.setTimeout(0);
				dto.setSukses(0);
				setChannelRc(cr, count, dto, responseCode);
				logsMap.put(key, dto);
			} else {
				setChannelRc(cr, count, tmpDto, responseCode);
			}
		}
		return Collections.list(Collections.enumeration(logsMap.values()));
	}

	private void setChannelRc(String cr, int count, TransactionsDto dto,
			HashMap<String, HashMap<String, String>> responseCode) {
		if (cr.equals("00")) {
			dto.setSukses(dto.getSukses() + count);
		} else {
			if (responseCode.get(dto.getChannelCode()) != null) {
				HashMap<String, String> rcByChannel = responseCode.get(dto.getChannelCode());
				if (rcByChannel.get(cr) != null)
					dto.setTimeout(dto.getTimeout() + count);
				else
					dto.setGagal(dto.getGagal() + count);
			} else {
				if (responseCode.get("default") != null) {
					HashMap<String, String> rcByChannel = responseCode.get("default");
					if (rcByChannel.get(cr) != null)
						dto.setTimeout(dto.getTimeout() + count);
					else
						dto.setGagal(dto.getGagal() + count);
				}
			}
		}

		// else if (cr.equals("50") || cr.equals("ZZ") || cr.equals("68")) {
		// dto.setTimeout(dto.getTimeout() + count);
		// // dto.setGagal(dto.getGagal() + count);
		// } else {
		// dto.setGagal(dto.getGagal() + count);
		// }
	}

	private String getLogKeys(Object[] keys) {
		String key = "";
		for (int i = 0; i < 3; i++) {
			key += keys[i];
		}
		return key;
	}

	@Override
	public List<Transactions> getEditableTransactionsList() {
		return transactionsDao.findAll();
	}

	@Override
	public int getRowCount(Object txCode, Object txName, String channelCode, Date startDate, Date endDate) {
		return (transactionsDao.getRowCountSummary(txCode, txName, channelCode, startDate, endDate));
	}

	@Override
	public List<Transactions> findTransactionsByIds(Set<Integer> ids) {
		return transactionsDao.findTransactionsByIds(ids);
	}

	@Override
	public List<Transactions> findTransactionsForFee(int first, int max) {
		return transactionsDao.findTransactionsForFee(first, max);
	}

	@Override
	public int getTransactionCountForFee() {
		return transactionsDao.getTransactionCountForFee();
	}

	@Override
	public Transactions findTransactionsByCode(String trxCode) {
		return transactionsDao.findTransactionsByCode(trxCode);
	}

	@Override
	public List<Transactions> findByCode(String trxCode) {
		return transactionsDao.findByCode(trxCode);
	}

	@Override
	public List<Transactions> getTransactionsList() {
		return transactionsDao.getTransactionsList();
	}
}
