/**
 * 
 */
package com.balicamp.dao.mx;

import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.model.mx.Transactions;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointRcsDao.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
public interface EndpointRcsDao extends GenericDao<EndpointRcs, Long> {
	
	public List<EndpointRcs> findByEndpointId(int endId);
	
	public List<EndpointRcs> findByEndpointId(int endId, String flag);
	
	public List<EndpointRcs> findAllChannelRc();

	public List<EndpointRcs> findAllBillerRc();
	
	public EndpointRcs findbyEndpointIdPlusRc(int endId, String rc);
	
	public List<EndpointRcs> getEndpointRcsList();
	
	public List<EndpointRcs> findByEndpointType(String type, String flag);
	
	public List<EndpointRcs> findByEndpointCode(String endCode);

}
