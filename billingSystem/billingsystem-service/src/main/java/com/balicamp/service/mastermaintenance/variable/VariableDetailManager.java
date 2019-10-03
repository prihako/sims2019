package com.balicamp.service.mastermaintenance.variable;

import java.util.List;

import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.service.IManager;

public interface VariableDetailManager extends IManager{

	List<VariableAnnualPercentageDetail> findByAnnualPercentId(Long annualPercentId);
	
	VariableAnnualPercentageDetail findByYear(Long annualPercentId, int yearTo);

	public void saveCollection(List<VariableAnnualPercentageDetail> variableAnnualPercentageDetail);


}