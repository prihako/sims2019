package com.balicamp.service.impl.mx;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mx.ReconcileMonthlyReportDAO;
import com.balicamp.model.mx.ReconcileReportMonthly;
import com.balicamp.service.ReconcileMonthlyReportManager;
import com.balicamp.service.impl.AbstractManager;

@Service("reconcileMonthlyReportManagerImpl")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ReconcileMonthlyReportManagerImpl extends AbstractManager implements ReconcileMonthlyReportManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ReconcileMonthlyReportDAO reconcileMonthlyReportDAO;

	@Autowired
	public ReconcileMonthlyReportManagerImpl(ReconcileMonthlyReportDAO mxReportDAO) {
		this.reconcileMonthlyReportDAO = mxReportDAO;
	}

	@Override
	public List<ReconcileReportMonthly> findByDate(ReconcileReportMonthly mxReport, Date startDate, Date endDate) {
		// TODO Auto-generated method stub

		List<ReconcileReportMonthly> list = reconcileMonthlyReportDAO.findByDate(mxReport, startDate, endDate);
		return list;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReconcileReportMonthly> findReconcileReportByDate(
			String bankName, String transactionType, Date startDate, Date endDate, String reportType) {
		String[] bankNameShort = bankName.split(" ");
		List<ReconcileReportMonthly> list = reconcileMonthlyReportDAO.
				findReconcileReportByDate(bankNameShort[1].toString(), transactionType, startDate, endDate, reportType);
		System.out.println(list.size());
		return list;
	}

}
