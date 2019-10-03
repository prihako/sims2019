package com.balicamp.service.impl.mastermaintenance.variable;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mastermaintenance.variable.VariableAnnualPercentageDao;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;

@Service("variableManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class VariableManagerImpl extends AbstractManager implements
VariableManager, InitializingBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final VariableAnnualPercentageDao variableAnnualPercentageDao;
	
	private static final Log log = LogFactory.getLog(VariableManagerImpl.class);


	
	@Autowired
	public VariableManagerImpl(VariableAnnualPercentageDao variableAnnualPercentageDao) {
		this.variableAnnualPercentageDao = variableAnnualPercentageDao;
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
	public List<VariableAnnualPercentage> findByStatus(int variableStatus) {
		// TODO Auto-generated method stub
		List<VariableAnnualPercentage> variableAnnualPercentageList = variableAnnualPercentageDao.findByStatus(variableStatus);
		return variableAnnualPercentageList;
	}


	@Override
	public VariableAnnualPercentage saveMerge(VariableAnnualPercentage variableAnnualPercentage) {
		// TODO Auto-generated method stub
		List<VariableAnnualPercentage> variableAnnualPercentageList = variableAnnualPercentageDao.findByStatus(variableAnnualPercentage.getVariableStatus());

//		if  (variableAnnualPercentage.getVariableStatus()==1){
//			
//			 variableAnnualPercentageDao.replaceToInactive(0);
//		}

		VariableAnnualPercentage obj = variableAnnualPercentageDao.saveMerge(variableAnnualPercentage);
		return obj;
	}

	@Override
	public VariableAnnualPercentage findByAnnualPercentId(Long annualPercentId) {
		VariableAnnualPercentage obj = variableAnnualPercentageDao.findByAnnPercentId(annualPercentId);
		return obj;
	}

	@Override
	public void save(
			VariableAnnualPercentage variableAnnualPercentage) {
		
		List<VariableAnnualPercentage> variableAnnualPercentageList = variableAnnualPercentageDao.findByStatus(variableAnnualPercentage.getVariableStatus());

		if  (variableAnnualPercentage.getVariableStatus()==1){
//			 variableAnnualPercentageDao.replaceToInactive(0);
			 variableAnnualPercentageDao.replaceToActive(1,variableAnnualPercentage.getAnnualPercentId());
		}
		variableAnnualPercentageDao.saveOrUpdate(variableAnnualPercentage);
	}

	@Override
	public VariableAnnualPercentage findByYear(Integer year) {
		VariableAnnualPercentage obj = variableAnnualPercentageDao.findByYear(year);
		return obj;
	}

	@Override
	public List<VariableAnnualPercentage> findAll() {
		List<VariableAnnualPercentage> list = variableAnnualPercentageDao.findAll();
		return list;
	}

	@Override
	public VariableAnnualPercentage findByKmNo(String kmNo) {
		VariableAnnualPercentage result = variableAnnualPercentageDao.findByKmNo(kmNo);
		return result;
	}

}
