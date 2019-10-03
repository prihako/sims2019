package com.balicamp.dao.mx;

import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.ParameterMx;

public interface ParameterMxDao extends GenericDao<ParameterMx, Integer> {
	public List<ParameterMx> findAll(String description, int first, int max);

	public int getRowCount(String description);
	
	public void updateValues(List<ParameterMx> entites) throws Exception;
}
