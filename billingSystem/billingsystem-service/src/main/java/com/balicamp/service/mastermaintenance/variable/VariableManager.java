package com.balicamp.service.mastermaintenance.variable;

import java.util.List;

import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.service.IManager;

public interface VariableManager extends IManager{

	List<VariableAnnualPercentage> findByStatus(int variableStatus);
	VariableAnnualPercentage findByAnnualPercentId(Long annualPercentId);
	
	
	public VariableAnnualPercentage saveMerge(VariableAnnualPercentage variableAnnualPercentage);
	public void save(VariableAnnualPercentage variableAnnualPercentage);

	VariableAnnualPercentage findByYear(Integer year);
	
	List<VariableAnnualPercentage> findAll();

	VariableAnnualPercentage findByKmNo(String kmNo);
}
