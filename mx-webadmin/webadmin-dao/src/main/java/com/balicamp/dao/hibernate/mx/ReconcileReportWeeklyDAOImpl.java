package com.balicamp.dao.hibernate.mx;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.mx.ReconcileReport;
import com.balicamp.model.mx.ReconcileReportWeekly;
import com.balicamp.model.mx.TransactionReport;
import com.balicamp.model.mx.TransactionLog;

@Repository
public class ReconcileReportWeeklyDAOImpl extends
AdminGenericDaoImpl<ReconcileReportWeekly, Long> implements ReconcileReportWeeklyDAO {

	public ReconcileReportWeeklyDAOImpl() {
		super(ReconcileReportWeekly.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<ReconcileReportWeekly> findByDate(ReconcileReportWeekly report,
			Date startDate, Date endDate) {
		List<ReconcileReportWeekly> list = null;
		System.out.println("tanggal Mulai "+startDate);
		System.out.println("tanggal akhir "+endDate);

		Criteria criteria = getSession().createCriteria(ReconcileReportWeekly.class);
		criteria.add(Restrictions.between("transactionTime", startDate, endDate)).add(Restrictions.eq("transactionType", report.getTransactionType() ));

		list = criteria.list();

		return list;

	}

	@Override
	public List<ReconcileReportWeekly> findReconcileReportWeeklyByDateAndReportType(String bankName,
			String transactionType, Date startDate, Date endDate, String reportType) {
		List<ReconcileReportWeekly> list = null;
		
		Calendar cal = Calendar.getInstance(); 
	    cal.setTime(endDate); 
	    cal.add(Calendar.HOUR_OF_DAY, 23); 
	    cal.add(Calendar.MINUTE, 59);
	    cal.add(Calendar.SECOND, 59);
	    Date calDate = cal.getTime();
		
		Criteria criteria = getSession().createCriteria(ReconcileReportWeekly.class);
		
		if(reportType.equalsIgnoreCase("all")){
			criteria.add(Restrictions.between("reportTime", startDate, calDate)).
			add(Restrictions.ilike("transactionType",transactionType)).
			add(Restrictions.ilike("bankName",bankName));
		}else{
			criteria.add(Restrictions.between("reportTime", startDate, calDate)).
				add(Restrictions.ilike("transactionType",transactionType)).
				add(Restrictions.ilike("bankName",bankName)).
				add(Restrictions.ilike("reportType",reportType));
		}
		
		criteria.addOrder(Order.asc("reportTime"));

		list = criteria.list();

		return list;
	}

}
