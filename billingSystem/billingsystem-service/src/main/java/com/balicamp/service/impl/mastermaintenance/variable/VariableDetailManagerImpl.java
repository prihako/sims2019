package com.balicamp.service.impl.mastermaintenance.variable;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mastermaintenance.variable.VariableAnnualPercentageDetailDao;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;

@Service("variableDetailManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class VariableDetailManagerImpl extends AbstractManager implements
VariableDetailManager, InitializingBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final VariableAnnualPercentageDetailDao variableAnnualPercentageDetailDao;
	
	private static final Log log = LogFactory.getLog(VariableDetailManagerImpl.class);

	

	@Autowired
	public VariableDetailManagerImpl(VariableAnnualPercentageDetailDao variableAnnualPercentageDetailDao) {
		this.variableAnnualPercentageDetailDao = variableAnnualPercentageDetailDao;
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
	public List<VariableAnnualPercentageDetail> findByAnnualPercentId(
			Long annualPercentId) {
		// TODO Auto-generated method stub
		List<VariableAnnualPercentageDetail> list = variableAnnualPercentageDetailDao.findByAnnualPercentId(annualPercentId);
		return list;
	}

	@Override
	public void saveCollection(
			List<VariableAnnualPercentageDetail> variableAnnualPercentageDetail) {
		// TODO Auto-generated method stub
		variableAnnualPercentageDetailDao.saveCollection(variableAnnualPercentageDetail);

	}

	@Override
	public VariableAnnualPercentageDetail findByYear(
			Long annualPercentId, int yearTo) {
		VariableAnnualPercentageDetail obj = variableAnnualPercentageDetailDao.findByYear(annualPercentId, yearTo);
		
		return obj;
	}

}
