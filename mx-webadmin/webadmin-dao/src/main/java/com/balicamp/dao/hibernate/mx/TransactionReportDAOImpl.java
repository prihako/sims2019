package com.balicamp.dao.hibernate.mx;

import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.mx.TransactionReport;
import com.balicamp.model.mx.TransactionLog;

@Repository
public class TransactionReportDAOImpl extends
AdminGenericDaoImpl<TransactionReport, Long> implements TransactionReportDAO {

	public TransactionReportDAOImpl() {
		super(TransactionReport.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<TransactionReport> findByDate(TransactionReport report,
			Date startDate, Date endDate) {
		List<TransactionReport> list = null;
		System.out.println("tanggal Mulai "+startDate);
		System.out.println("tanggal akhir "+endDate);

		Criteria criteria = getSession().createCriteria(TransactionReport.class);
		criteria.add(Restrictions.between("transactionTime", startDate, endDate)).add(Restrictions.eq("transactionType", report.getTransactionType() ));

		list = criteria.list();

		return list;

	}

	@Override
	public List<TransactionReport> findTransactionReportByDate(
			String transactionType, Date startDate, Date endDate, String bankName) {
		List<TransactionReport> list = null;
		
		Calendar cal = Calendar.getInstance(); 
	    cal.setTime(endDate); 
	    cal.add(Calendar.HOUR_OF_DAY, 23); 
	    cal.add(Calendar.MINUTE, 59);
	    cal.add(Calendar.SECOND, 59);
	    Date calDate = cal.getTime();
		
		Criteria criteria = getSession().createCriteria(TransactionReport.class);
		if(transactionType.equalsIgnoreCase("all")){
			criteria.add(Restrictions.between("reportTime", startDate, calDate));
		}else{
			criteria.add(Restrictions.between("reportTime", startDate, calDate)).add(Restrictions.ilike("transactionType",transactionType));
		}
		
		if(!bankName.equalsIgnoreCase("all")){
			criteria.add(Restrictions.ilike("bankName", bankName));
		}

		list = criteria.addOrder(Order.asc("reportTime")).addOrder(Order.asc("bankName")).list();

		return list;
	}

}
