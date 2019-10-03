package com.balicamp.dao.hibernate.mastermaintenance.variable;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;

@Repository("variableDetailDaoHibernate")
public class VariableAnnualPercentageDetailDaoHibernate extends
		AdminGenericDaoImpl<VariableAnnualPercentageDetail, Long> implements
		VariableAnnualPercentageDetailDao {

	public VariableAnnualPercentageDetailDaoHibernate() {
		super(VariableAnnualPercentageDetail.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<VariableAnnualPercentageDetail> findByAnnualPercentId(
			Long annualPercentId) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select s from VariableAnnualPercentageDetail as s where s.annualPercentId="
						+ annualPercentId + " order by s.yearTo");
		List<VariableAnnualPercentageDetail> detailList = query.list();
		System.out
				.println("VariableAnnualPercentageDetail annpercentDetailId = "
						+ detailList.get(0).getAnnPercentDtlId());

		return detailList;
	}

	@Override
	public void saveList(
			List<VariableAnnualPercentageDetail> variableAnnualPercentageDetail) {
		// TODO Auto-generated method stub

	}

	@Override
	public VariableAnnualPercentageDetail findByYear(Long annualPercentId,
			int yearTo) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select s from VariableAnnualPercentageDetail as s where s.annualPercentId="
						+ annualPercentId +" and s.yearTo="+yearTo);	
		
		
		
		Object o = query.uniqueResult();
		return (VariableAnnualPercentageDetail)o;
	}

}
