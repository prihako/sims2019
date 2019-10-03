package com.balicamp.service.mastermaintenance.variable;

import com.balicamp.model.mastermaintenance.variable.VariableIPSFR;
import com.balicamp.service.IManager;

public interface VariableIPSFRManager extends IManager{
	
	VariableIPSFR findByVariableIPSFR(VariableIPSFR variableIPSFR);
	public void saveVariableIPSFR(VariableIPSFR variableIPSFR);

}
