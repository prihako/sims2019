package com.balicamp.dao.hibernate.mastermaintenance.variable;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mastermaintenance.variable.VariableIPSFR;

/**
 * DAO for VariableIPSFR Entity
 *
 */
public interface VariableIPSFRDao extends AdminGenericDao<VariableIPSFR, Long>{


	VariableIPSFR findByVariableIPSFR(VariableIPSFR variableIPSFR);
	public void saveVariableIPSFR(VariableIPSFR variableIPSFR);

}
