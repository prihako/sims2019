package com.balicamp.service.impl.mx;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mx.ReconcileReportDAO;
import com.balicamp.model.mx.ReconcileReport;
import com.balicamp.service.ReconcileReportManager;
import com.balicamp.service.impl.AbstractManager;

@Service("reconcileReportManagerImpl")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ReconcileReportManagerImpl extends AbstractManager implements ReconcileReportManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ReconcileReportDAO reconcileReportDAO;

	@Autowired
	public ReconcileReportManagerImpl(ReconcileReportDAO mxReportDAO) {
		this.reconcileReportDAO = mxReportDAO;
	}

	@Override
	public List<ReconcileReport> findByDate(ReconcileReport mxReport, Date startDate, Date endDate) {
		// TODO Auto-generated method stub

		List<ReconcileReport> list = reconcileReportDAO.findByDate(mxReport, startDate, endDate);
		return list;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReconcileReport> findReconcileReportByDate(String bankName, String transactionType, Date startDate,
			Date endDate) {
		String[] bankNameShort = bankName.split(" ");
		List<ReconcileReport> list = reconcileReportDAO.findReconcileReportByDate(bankNameShort[1].toString().toLowerCase(), transactionType, startDate, endDate);
		System.out.println(list.size());
		return list;
	}

}
