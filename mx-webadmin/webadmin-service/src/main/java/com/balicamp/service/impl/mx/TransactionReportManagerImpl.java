package com.balicamp.service.impl.mx;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mx.TransactionReportDAO;
import com.balicamp.model.mx.TransactionReport;
import com.balicamp.service.TransactionReportManager;
import com.balicamp.service.impl.AbstractManager;

@Service("transactionReportManagerImpl")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class TransactionReportManagerImpl extends AbstractManager implements TransactionReportManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TransactionReportDAO transactionReportDAO;

	@Autowired
	public TransactionReportManagerImpl(TransactionReportDAO mxReportDAO) {
		this.transactionReportDAO = mxReportDAO;
	}

	@Override
	public List<TransactionReport> findByDate(TransactionReport mxReport, Date startDate, Date endDate) {
		// TODO Auto-generated method stub

		List<TransactionReport> list = transactionReportDAO.findByDate(mxReport, startDate, endDate);
		return list;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionReport> findTransactionReportByDate(String transactionType, Date startDate, Date endDate,
			String bankName) {
		List<TransactionReport> list = transactionReportDAO.findTransactionReportByDate(transactionType, startDate,
				endDate, bankName);
		System.out.println(list.size());
		return list;
	}
}
