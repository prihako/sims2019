package com.balicamp.service;

import java.util.List;
import java.util.Set;

import com.balicamp.model.mx.Endpoints;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointsManager.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
public interface EndpointsManager extends IManager {
	public List<Endpoints> getEditabelChannelCodeList();

	List<Endpoints> findEndpointsByIds(Set<Integer> ids);
}
