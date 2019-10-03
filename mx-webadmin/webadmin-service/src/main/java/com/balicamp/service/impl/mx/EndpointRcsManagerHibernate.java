package com.balicamp.service.impl.mx;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.mx.EndpointRcsDao;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.service.EndpointRcsManager;
import com.balicamp.service.impl.AbstractManager;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointsManagerImpl.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
@Service("endpointRcsManagerImpl")
public class EndpointRcsManagerHibernate extends AbstractManager implements EndpointRcsManager {
	private static final long serialVersionUID = 3481699250836284923L;

	@Autowired
	private EndpointRcsDao endpointRcsDao;

	@Override
	public Object getDefaultDao() {
		return endpointRcsDao;
	}

	@Override
	public List<EndpointRcs> findByEndpointId(int endId) {
		return endpointRcsDao.findByEndpointId(endId);
	}

	@Override
	public EndpointRcs findByEndpointIdPlusRc(int endId, String rc) {
		return endpointRcsDao.findbyEndpointIdPlusRc(endId, rc);
	}

	@Override
	public List<EndpointRcs> findAll() {
		DetachedCriteria criteria = DetachedCriteria.forClass(EndpointRcs.class);
		criteria.addOrder(Order.asc("rc"));
		return endpointRcsDao.getCurrentHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<EndpointRcs> findByEndpointId(int endId, String flag) {
		return endpointRcsDao.findByEndpointId(endId, flag);
	}

	@Override
	public List<EndpointRcs> findAllChannelRc() {
		return endpointRcsDao.findAllChannelRc();
	}

	@Override
	public List<EndpointRcs> findAllBillerRc() {
		return endpointRcsDao.findAllBillerRc();
	}

	@Override
	public List<EndpointRcs> findByEndpointType(String type, String flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EndpointRcs> findByEndpointCode(String endCode) {
		return endpointRcsDao.findByEndpointCode(endCode);
	}

}
