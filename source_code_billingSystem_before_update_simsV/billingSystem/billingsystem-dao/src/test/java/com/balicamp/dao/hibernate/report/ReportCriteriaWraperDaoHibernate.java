package com.balicamp.dao.hibernate.report;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.mx.MxGenericDaoHibernate;
import com.balicamp.model.mx.TransactionLogs;

@Repository("reportCriteriaWraperDaoHibernate")
public class ReportCriteriaWraperDaoHibernate extends MxGenericDaoHibernate<TransactionLogs, Long> implements ReportCriteriaWraperDao {
	
	public ReportCriteriaWraperDaoHibernate() {
		super(TransactionLogs.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TransactionLogs> testCriteriaWrapper(String criteriaHQL) {
		return (List<TransactionLogs>) findByHQL(getSession().createQuery(criteriaHQL), 0,10);
	}

}
