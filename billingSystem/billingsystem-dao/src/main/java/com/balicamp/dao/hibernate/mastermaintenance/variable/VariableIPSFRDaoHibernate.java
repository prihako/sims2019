package com.balicamp.dao.hibernate.mastermaintenance.variable;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.mastermaintenance.variable.VariableIPSFR;

@Repository("variableIPSFRDaoHibernate")
public class VariableIPSFRDaoHibernate extends
		AdminGenericDaoImpl<VariableIPSFR, Long> implements VariableIPSFRDao {



	public VariableIPSFRDaoHibernate() {
		super(VariableIPSFR.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public VariableIPSFR findByVariableIPSFR(VariableIPSFR variableIPSFR) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select v from VariableIPSFR as v where v.serviceId="
						+ variableIPSFR.getServiceId()+" and v.subserviceId="+variableIPSFR.getSubserviceId()+" and v.bhpMethod='"+variableIPSFR.getBhpMethod()+"' and v.clientName like '%"+variableIPSFR.getClientName()+"%' and  v.frequency="+variableIPSFR.getFrequency());		
		Object obj = query.uniqueResult();
		return (VariableIPSFR) obj;
	}

	@Override
	public void saveVariableIPSFR(VariableIPSFR variableIPSFR) {
		// TODO Auto-generated method stub
		
	}

}
