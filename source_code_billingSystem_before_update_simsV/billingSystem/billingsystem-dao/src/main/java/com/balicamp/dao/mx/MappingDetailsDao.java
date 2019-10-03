package com.balicamp.dao.mx;

import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.MappingDetails;
import com.balicamp.model.mx.ParameterMx;

public interface MappingDetailsDao extends GenericDao<MappingDetails, Integer> {
	
	public void updateValues(List<ParameterMx> entites) throws Exception;

}
