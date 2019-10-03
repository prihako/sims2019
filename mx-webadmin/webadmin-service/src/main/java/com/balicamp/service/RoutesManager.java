package com.balicamp.service;

import com.balicamp.model.mx.Transactions;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointsManager.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
public interface RoutesManager extends IManager {

	public Transactions findTransactionByEndpointCode(String endpointCode);

}
