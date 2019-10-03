package com.balicamp.service.impl.mx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.mx.EndpointRcsDao;
import com.balicamp.dao.mx.TransactionLogDao;
import com.balicamp.dao.mx.TransactionLogHousekeepingDao;
import com.balicamp.dao.mx.TransactionsDao;
import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.model.mx.TransactionLogs;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.impl.AbstractManager;

@Service("transactionLogsManagerImpl")
public class TransactionLogsManagerImpl extends AbstractManager implements TransactionLogsManager {
	private static final long serialVersionUID = -3908230684618992868L;

	@Autowired
	private TransactionLogDao transactionLogDao;

	@Autowired
	private EndpointRcsDao endpointRcsDao;

	@Autowired
	private TransactionsDao transactionDao;

	@Autowired
	private TransactionLogHousekeepingDao transactionLogHousekeepingDao;

	@Override
	public Object getDefaultDao() {
		return transactionLogDao;
	}

	@Override
	public List<AnalisaTransactionLogsDto> findTransactions(String txId, String txCode, String txName,
			String channelCode, Date startDate, Date endDate, String rawKey, int first, int max) {
		List<Object> queryResultMx = (List<Object>) transactionLogDao.findTransactions(txCode, txName, channelCode,
				txId, startDate, endDate, rawKey, first, max);
		List<Object> queryResultHousekeeping = (List<Object>) transactionLogHousekeepingDao.findTransactions(txCode,
				txName, channelCode, txId, startDate, endDate, rawKey, first, max);

		queryResultMx.addAll(queryResultHousekeeping);
		
		List<EndpointRcs> endpointRcsRows = endpointRcsDao.findAll();
		
		List<AnalisaTransactionLogsDto> result = new ArrayList<AnalisaTransactionLogsDto>();
		for (Object o : queryResultMx) {
			Object[] row = (Object[]) o;
			AnalisaTransactionLogsDto dto = new AnalisaTransactionLogsDto();
			dto.setTrxId((String) row[0]);
			dto.setTrxTime((Date) row[1]);
			dto.setTrxCode((String) row[2]);
			
			if (row.length > 5) {
				dto.setChannelCode((String) row[4]);
				dto.setChannelRc((String) row[5]);
				dto.setTrxDesc((String) row[3]);
				dto.setRcDesc((String) row[6]);
			} else {
				dto.setChannelCode((String) row[3]);
				dto.setChannelRc((String) row[4]);
				dto.setTrxDesc(transactionDao.findByFieldName(dto.getTrxCode()));
				
				for (EndpointRcs endpointRcs : endpointRcsRows) {
					if (endpointRcs.getEndpoints() != null) {
						if (dto.getChannelRc().equals(endpointRcs.getRc())
								&& dto.getChannelCode().equals(endpointRcs.getEndpoints().getCode())) {
							dto.setRcDesc(endpointRcs.getDescription());
						}
					}
				}
			}
			
			result.add(dto);
		}
		return result;
	}

	@Override
	public Long getRowCount(String txId, String txCode, String txName, String channelCode, Date startDate,
			Date endDate, String rawKey) {
		return (transactionLogHousekeepingDao.findTransactionCount(txId, txCode, txName, channelCode, startDate,
				endDate, rawKey) + transactionLogDao.findTransactionCount(txId, txCode, txName, channelCode, startDate,
				endDate, rawKey));
		// return transactionLogDao.findTransactionCount(txId, txCode, txName,
		// channelCode, startDate, endDate, rawKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findMessageLogsByTxId(String txId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TransactionLogs.class);
		criteria.add(Restrictions.eq("id.transactionId", txId));
		criteria.setProjection(Projections.property("id.raw"));

		List<String> result = transactionLogHousekeepingDao.getCurrentHibernateTemplate().findByCriteria(criteria);
		result.addAll(transactionLogDao.getCurrentHibernateTemplate().findByCriteria(criteria));
		return result;
		// return
		// transactionLogDao.getCurrentHibernateTemplate().findByCriteria(criteria);
	}
}
