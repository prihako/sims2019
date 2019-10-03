package com.balicamp.service;

import java.util.List;

import com.balicamp.model.mx.EndpointRcs;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointsManager.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
public interface EndpointRcsManager extends IManager {

	public List<EndpointRcs> findByEndpointId(int endId);

	public List<EndpointRcs> findByEndpointId(int endId, String flag);

	public List<EndpointRcs> findAllChannelRc();

	public List<EndpointRcs> findAllBillerRc();

	public EndpointRcs findByEndpointIdPlusRc(int endId, String rc);

	public List<EndpointRcs> findAll();

	public List<EndpointRcs> findByEndpointType(String type, String flag);

	public List<EndpointRcs> findByEndpointCode(String endCode);
}
