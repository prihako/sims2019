package com.balicamp.dao.hibernate.report;

import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.report.ReportDao;
import com.balicamp.model.report.ReportModel;

@Repository
public class ReportDaoHibernate extends AdminGenericDaoImpl<ReportModel, Long> 
			implements ReportDao{

	public ReportDaoHibernate() {
		super(ReportModel.class);
	}

}
