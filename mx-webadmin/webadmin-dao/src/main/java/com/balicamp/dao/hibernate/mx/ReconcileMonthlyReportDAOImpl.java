package com.balicamp.dao.hibernate.mx;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.mx.ReconcileReportMonthly;
import com.balicamp.model.mx.ReconcileReport;
import com.balicamp.model.mx.TransactionReport;
import com.balicamp.model.mx.TransactionLog;

@Repository
public class ReconcileMonthlyReportDAOImpl extends
AdminGenericDaoImpl<ReconcileReportMonthly, Long> implements ReconcileMonthlyReportDAO {

	public ReconcileMonthlyReportDAOImpl() {
		super(ReconcileReportMonthly.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<ReconcileReportMonthly> findByDate(ReconcileReportMonthly report,
			Date startDate, Date endDate) {
		List<ReconcileReportMonthly> list = null;
		System.out.println("tanggal mulai "+startDate);
		System.out.println("tanggal akhir "+endDate);

		Criteria criteria = getSession().createCriteria(ReconcileReportMonthly.class);
		criteria.add(Restrictions.between("transactionTime", startDate, endDate)).add(Restrictions.eq("transactionType", report.getTransactionType() ));

		list = criteria.list();

		return list;

	}

	@Override
	public List<ReconcileReportMonthly> findReconcileReportByDate(String bankName,
			String transactionType, Date startDate, Date endDate, String reportType) {
		List<ReconcileReportMonthly> list = null;
		
		Calendar cal = Calendar.getInstance(); 
	    cal.setTime(endDate); 
	    cal.add(Calendar.HOUR_OF_DAY, 23); 
	    cal.add(Calendar.MINUTE, 59);
	    cal.add(Calendar.SECOND, 59);
	    Date calDate = cal.getTime();
		
		Criteria criteria = getSession().createCriteria(ReconcileReportMonthly.class);
		if(reportType.equalsIgnoreCase("all")){
			criteria.add(Restrictions.between("reportTime", startDate, calDate)).
			add(Restrictions.ilike("transactionType",transactionType)).
			add(Restrictions.ilike("bankName", bankName));
		}else{
			criteria.add(Restrictions.between("reportTime", startDate, calDate)).
				add(Restrictions.ilike("transactionType",transactionType)).
				add(Restrictions.ilike("reportType", reportType)).
				add(Restrictions.ilike("bankName", bankName));
		}

		list = criteria.list();

		return list;
	}

}
