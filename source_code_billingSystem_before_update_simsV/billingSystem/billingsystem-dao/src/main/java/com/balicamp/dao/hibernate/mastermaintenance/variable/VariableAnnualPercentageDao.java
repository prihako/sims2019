package com.balicamp.dao.hibernate.mastermaintenance.variable;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;

/**
 * DAO for VariableAnnualPercentage Entity
 *
 */
public interface VariableAnnualPercentageDao extends AdminGenericDao<VariableAnnualPercentage, Long>{

	List<VariableAnnualPercentage> findByStatus(final int variableStatus);
	VariableAnnualPercentage findByAnnPercentId(Long annualPercentId);

	void saveVariable(VariableAnnualPercentage variableAnnualPercentage);
	public void replaceToInactive(final int variableStatus);
	public void replaceToActive(final int variableStatus, Long annualPercentId);

	List<VariableAnnualPercentageDetail> findByAnnualPercentId(Long annualPercentId);

	VariableAnnualPercentage findByYear(Integer year);
	
	VariableAnnualPercentage findByKmNo(String kmNo);
}
