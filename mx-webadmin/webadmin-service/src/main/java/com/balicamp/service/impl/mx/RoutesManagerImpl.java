package com.balicamp.service.impl.mx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.mx.RoutesDao;
import com.balicamp.model.mx.Transactions;
import com.balicamp.service.RoutesManager;
import com.balicamp.service.impl.AbstractManager;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointsManagerImpl.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
@Service("routesManagerImpl")
public class RoutesManagerImpl extends AbstractManager implements RoutesManager {
	private static final long serialVersionUID = 3481699250836284923L;

	@Autowired
	private RoutesDao routesDao;

	@Override
	public Object getDefaultDao() {
		return routesDao;
	}

	@Override
	public Transactions findTransactionByEndpointCode(String endpointCode) {
		return routesDao.findTransactionByEndpointCode(endpointCode);
	}

}
