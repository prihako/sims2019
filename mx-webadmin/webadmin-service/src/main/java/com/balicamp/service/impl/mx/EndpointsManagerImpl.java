package com.balicamp.service.impl.mx;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.mx.EndpointsDao;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.service.EndpointsManager;
import com.balicamp.service.impl.AbstractManager;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointsManagerImpl.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
@Service("endpointsManagerImpl")
public class EndpointsManagerImpl extends AbstractManager implements EndpointsManager {
	private static final long serialVersionUID = 3481699250836284923L;

	@Autowired
	private EndpointsDao endpointsDao;

	@Override
	public Object getDefaultDao() {
		return endpointsDao;
	}

	@Override
	public List<Endpoints> getEditabelChannelCodeList() {
		return endpointsDao.findAll();
	}

	@Override
	public List<Endpoints> findEndpointsByIds(Set<Integer> ids) {
		return endpointsDao.findEndpointsByIds(ids);
	}

	public Endpoints findEndpointsByCode(String code) {
		return endpointsDao.findEndpointsByCode(code);
	}

	@Override
	public List<Endpoints> getListEndpointsByState(String state) {
		return endpointsDao.getEndpointsByState(state);
	}

	@Override
	public List<Endpoints> getAll() {
		List<Endpoints> obj = endpointsDao.findAll();
		return obj;
	}

	@Override
	public List<Endpoints> getAllEndpointsByState(String state) {
		return endpointsDao.getAllEndpointsByState(state);
	}

	@Override
	public List<Endpoints> getListEndpointsByType(String type) {
		// TODO Auto-generated method stub
		return endpointsDao.getEndpointsByType(type);
	}

	@Override
	public List<Endpoints> getAllEndpointsByType(String type) {
		// TODO Auto-generated method stub
		return endpointsDao.getAllEndpointsByType(type);
	}
}
