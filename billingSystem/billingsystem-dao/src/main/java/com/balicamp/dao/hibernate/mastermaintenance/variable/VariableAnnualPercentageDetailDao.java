package com.balicamp.dao.hibernate.mastermaintenance.variable;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;

public interface VariableAnnualPercentageDetailDao extends
		AdminGenericDao<VariableAnnualPercentageDetail, Long> {

	List<VariableAnnualPercentageDetail> findByAnnualPercentId(
			Long annualPercentId);
	
	VariableAnnualPercentageDetail findByYear(Long annualPercentId, int yearTo);


	public void saveList(
			List<VariableAnnualPercentageDetail> variableAnnualPercentageDetail);

}
