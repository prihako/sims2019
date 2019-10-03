package com.balicamp.dao.mx;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.Routes;
import com.balicamp.model.mx.Transactions;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: $
 */
public interface RoutesDao extends GenericDao<Routes, Long> {

	public Transactions findTransactionByEndpointCode(String endpointCode);
	
}
