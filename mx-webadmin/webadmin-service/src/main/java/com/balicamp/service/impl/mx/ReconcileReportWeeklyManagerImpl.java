package com.balicamp.service.impl.mx;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mx.ReconcileReportWeeklyDAO;
import com.balicamp.model.mx.ReconcileReportWeekly;
import com.balicamp.service.ReconcileReportWeeklyManager;
import com.balicamp.service.impl.AbstractManager;

@Service("reconcileReportWeeklyManagerImpl")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ReconcileReportWeeklyManagerImpl extends AbstractManager implements ReconcileReportWeeklyManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ReconcileReportWeeklyDAO reconcileReportWeeklyDAO;

	@Autowired
	public ReconcileReportWeeklyManagerImpl(ReconcileReportWeeklyDAO mxReportDAO) {
		this.reconcileReportWeeklyDAO = mxReportDAO;
	}

	@Override
	public List<ReconcileReportWeekly> findByDate(ReconcileReportWeekly mxReport, Date startDate, Date endDate) {
		// TODO Auto-generated method stub

		List<ReconcileReportWeekly> list = reconcileReportWeeklyDAO.findByDate(mxReport, startDate, endDate);
		return list;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReconcileReportWeekly> findReconcileReportWeeklyByDateAndReportType(
			String bankName, String transactionType, Date startDate, Date endDate, String reportType) {
		String[] bankNameShort = bankName.split(" ");
		List<ReconcileReportWeekly> list = reconcileReportWeeklyDAO.
				findReconcileReportWeeklyByDateAndReportType(bankNameShort[1].toString(), transactionType, startDate, endDate, reportType);
		System.out.println(list.size());
		return list;
	}

}
