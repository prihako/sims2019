package com.balicamp.dao.mx;

import java.util.List;
import java.util.Set;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.Endpoints;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: $
 */
public interface EndpointsDao extends GenericDao<Endpoints, Long> {

	public String findRcDescByEndpCode(String endpointCode, String rc);

	List<Endpoints> findEndpointsByIds(Set<Integer> ids);

	Endpoints findEndpointsByCode(String code);

	public List<Endpoints> getEndpointsByState(String state);
	
	public List<Endpoints> getAllEndpointsByState(String state);
	
	public List<Endpoints> getEndpointsByType(String type);
	
	public List<Endpoints> getAllEndpointsByType(String type);
}
