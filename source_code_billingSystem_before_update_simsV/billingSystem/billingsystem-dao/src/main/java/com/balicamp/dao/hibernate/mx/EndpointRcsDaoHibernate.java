/**
 * 
 */
package com.balicamp.dao.hibernate.mx;

import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.EndpointRcsDao;
import com.balicamp.model.mx.EndpointRcs;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointRcsDaoHibernate.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Repository
public class EndpointRcsDaoHibernate extends MxGenericDaoHibernate<EndpointRcs, Long> implements EndpointRcsDao {

	public EndpointRcsDaoHibernate() {
		super(EndpointRcs.class);
	}
}
