package com.balicamp.service.impl.mx;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.mx.MappingDetailsDao;
import com.balicamp.dao.mx.ParameterMxDao;
import com.balicamp.model.mx.ParameterMx;
import com.balicamp.service.ParameterMxManager;
import com.balicamp.service.impl.AbstractManager;

@Service("parameterMxManagerImpl")
public class ParameterMxManagerImpl extends AbstractManager implements ParameterMxManager {
	private static final long serialVersionUID = -8228843234354576276L;

	private static final Log log = LogFactory.getLog(ParameterMxManagerImpl.class);

	@Autowired
	private ParameterMxDao parameterMxDao;

	@Autowired
	private MappingDetailsDao mappingDetailsDao;

	@Override
	public Object getDefaultDao() {
		return parameterMxDao;
	}

	@Override
	public List<ParameterMx> findAll(String description, int first, int max) {
		return parameterMxDao.findAll(description, first, max);
	}

	@Override
	public int getRowCount(String description) {
		return parameterMxDao.getRowCount(description);
	}

	private List<ParameterMx> reverseValues(List<ParameterMx> entites) {
		List<ParameterMx> reverseValue = new ArrayList<ParameterMx>();
		for (ParameterMx entity : entites) {
			entity.setValueBaru(entity.getValueLama());
			reverseValue.add(entity);
		}
		return reverseValue;
	}

	private void revertMappingDetails(List<ParameterMx> entites) {
		try {
			mappingDetailsDao.updateValues(reverseValues(entites));
		} catch (Exception e) {
			log.error("Update parameter mx gagal, tidak bisa mengembalikan ke nilai semula.");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public void updateValues(List<ParameterMx> entites) {
		try {
			mappingDetailsDao.updateValues(entites);
		} catch (Exception e) {
			revertMappingDetails(entites);
			return;
		}

		try {
			parameterMxDao.updateValues(entites);
		} catch (Exception e) {
			revertMappingDetails(entites);
		}
	}

}
