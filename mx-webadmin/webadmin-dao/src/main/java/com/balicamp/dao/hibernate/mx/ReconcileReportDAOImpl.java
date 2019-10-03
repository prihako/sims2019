package com.balicamp.dao.hibernate.mx;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.mx.ReconcileReport;

@Repository
public class ReconcileReportDAOImpl extends
AdminGenericDaoImpl<ReconcileReport, Long> implements ReconcileReportDAO {

	public ReconcileReportDAOImpl() {
		super(ReconcileReport.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<ReconcileReport> findByDate(ReconcileReport report,
			Date startDate, Date endDate) {
		List<ReconcileReport> list = null;
		System.out.println("tanggal Mulai "+startDate);
		System.out.println("tanggal akhir "+endDate);

		Criteria criteria = getSession().createCriteria(ReconcileReport.class);
		criteria.add(Restrictions.between("transactionTime", startDate, endDate)).add(Restrictions.eq("transactionType", report.getTransactionType() ));

		list = criteria.list();

		return list;

	}

	@Override
	public List<ReconcileReport> findReconcileReportByDate(String bankName,
			String transactionType, Date startDate, Date endDate) {
		List<ReconcileReport> list = null;
		
		Calendar cal = Calendar.getInstance(); 
	    cal.setTime(endDate); 
	    cal.add(Calendar.HOUR_OF_DAY, 23); 
	    cal.add(Calendar.MINUTE, 59);
	    cal.add(Calendar.SECOND, 59);
	    Date calDate = cal.getTime();
		
		Criteria criteria = getSession().createCriteria(ReconcileReport.class);
		criteria.add(Restrictions.between("reportTime", startDate, calDate)).
			add(Restrictions.ilike("transactionType",transactionType)).
			add(Restrictions.ilike("bankName",bankName));

		list = criteria.list();

		return list;
	}

}
