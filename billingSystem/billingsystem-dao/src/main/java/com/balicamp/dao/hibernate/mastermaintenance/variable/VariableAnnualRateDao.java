package com.balicamp.dao.hibernate.mastermaintenance.variable;

import java.math.BigDecimal;
import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;


/**
 * DAO for VariableAnnualRate Entity
 *
 */
public interface VariableAnnualRateDao extends AdminGenericDao<VariableAnnualRate, Long>{
	
	public VariableAnnualRate findByAnnualRateId(final Long annualRateId);

	public List<VariableAnnualRate> findByStatus(final int activeStatus);
	public List<VariableAnnualRate> findByBaseOnNoteAndStatus(final String baseOnNote, final int activeStatus);

	public void replaceToInactive(final int activeStatus);
	public void replaceToActive(final int activeStatus, Long annualRateId);
	
	public VariableAnnualRate findByYear(BigDecimal year);

}
