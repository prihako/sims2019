package com.balicamp.service.impl.mastermaintenance.variable;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mastermaintenance.variable.VariableAnnualRateDao;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;

@Service("variableAnnualRateManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class VariableAnnualRateManagerImpl extends AbstractManager implements
VariableAnnualRateManager, InitializingBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final VariableAnnualRateDao variableAnnualRateDao;
	
	private static final Log log = LogFactory.getLog(VariableManagerImpl.class);


	@Autowired
	public VariableAnnualRateManagerImpl(VariableAnnualRateDao variableAnnualRateDao) {
		this.variableAnnualRateDao = variableAnnualRateDao;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableAnnualRate> findByStatus(int activeStatus) {
		// TODO Auto-generated method stub
		List<VariableAnnualRate> variableAnnualRateList= variableAnnualRateDao.findByStatus(activeStatus);
		return variableAnnualRateList;
	}

	@Override
	public List<VariableAnnualRate> findByBaseOnNoteAndStatus(
			String baseOnNote, int activeStatus) {
		List<VariableAnnualRate> variableAnnualRateList= variableAnnualRateDao.findByBaseOnNoteAndStatus(baseOnNote, activeStatus);
		return variableAnnualRateList;
	}
	@Override
	public void save(VariableAnnualRate variableAnnualRate) {
		
		System.out.println("save active status = " + variableAnnualRate.getActiveStatus());
		if  (variableAnnualRate.getActiveStatus()==1){
			
			variableAnnualRateDao.replaceToInactive(0);

		}
		variableAnnualRateDao.saveOrUpdate(variableAnnualRate);
		//variableAnnualRateDao.save(variableAnnualRate);
	}
	@Override
	public VariableAnnualRate findByAnnualRateId(Long annualRateId) {

		VariableAnnualRate obj = variableAnnualRateDao.findByAnnualRateId(annualRateId);
		return obj;
	}
	@Override
	public void update(VariableAnnualRate variableAnnualRate) {
		System.out.println(" update active status = "+variableAnnualRate.getActiveStatus());
		if  (variableAnnualRate.getActiveStatus()==1){
			
			variableAnnualRateDao.replaceToInactive(0);
			variableAnnualRateDao.replaceToActive(1, variableAnnualRate.getAnnualRateId());

		}
		variableAnnualRateDao.saveOrUpdate(variableAnnualRate);		
	}
	@Override
	public VariableAnnualRate findByYear(BigDecimal year) {
		VariableAnnualRate obj = variableAnnualRateDao.findByYear(year);
		return obj;
	}

}
