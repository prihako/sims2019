package com.balicamp.service.mastermaintenance.variable;

import java.math.BigDecimal;
import java.util.List;

import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.service.IManager;

public interface VariableAnnualRateManager extends IManager{
	
	public VariableAnnualRate findByAnnualRateId(final Long annualRateId);

	public List<VariableAnnualRate> findByStatus(final int activeStatus);
	public List<VariableAnnualRate> findByBaseOnNoteAndStatus(final String baseOnNote, final int activeStatus);

	public void save (VariableAnnualRate variableAnnualRate);
	public void update (VariableAnnualRate variableAnnualRate);

	public VariableAnnualRate findByYear(BigDecimal year);

}
