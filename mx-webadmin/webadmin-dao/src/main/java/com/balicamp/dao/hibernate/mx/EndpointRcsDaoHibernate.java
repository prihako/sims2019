/**
 * 
 */
package com.balicamp.dao.hibernate.mx;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.EndpointRcsDao;
import com.balicamp.model.mx.EndpointRcs;
import com.balicamp.model.mx.Transactions;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointRcsDaoHibernate.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Repository
public class EndpointRcsDaoHibernate extends MxGenericDaoHibernate<EndpointRcs, Long> implements EndpointRcsDao {

	public EndpointRcsDaoHibernate() {
		super(EndpointRcs.class);
	}

	@Override
	public List<EndpointRcs> findByEndpointId(int endId) {
		Query query = getSession().createQuery(
				"SELECT erc FROM EndpointRcs AS erc WHERE erc.endpoints.id = :endId");
		query.setParameter("endId",endId);
		return query.list();
	}

	@Override
	public EndpointRcs findbyEndpointIdPlusRc(int endId, String rc) {
		if(!rc.equalsIgnoreCase("all")){
			Query query = getSession().createQuery(
				"SELECT erc FROM EndpointRcs as erc WHERE erc.endpoints.id = :endId AND rc = :rc"
				);
			query.setParameter("endId", endId);
			query.setParameter("rc", rc);
			System.out.println(((EndpointRcs) query.uniqueResult()).toString());
			return (EndpointRcs) query.uniqueResult();
		}else{
			return new EndpointRcs();
		}
	}

	@Override
	public List<EndpointRcs> getEndpointRcsList() {
		Query query = getSession().createQuery("SELECT e.rc FROM EndpointRcs AS e");
		return query.list();
	}

	@Override
	public List<EndpointRcs> findByEndpointId(int endId, String flag) {
		Query query = getSession().createQuery(
				"SELECT erc FROM EndpointRcs AS erc WHERE erc.endpoints.id = :endId and erc.isHidden = :flag ");
		query.setParameter("endId",endId);
		query.setParameter("flag", flag);
		
		return query.list();
	}

	@Override
	public List<EndpointRcs> findByEndpointCode(String endCode) {
		Query query = getSession().createQuery(
				"SELECT erc FROM EndpointRcs AS erc WHERE erc.endpoints.id = (SELECT id from erc.endpoints WHERE erc.endpoints.code= :endCode) and erc.isHidden = 'N' ");
		query.setParameter("endCode",endCode);
		return query.list();
	}
	

	@Override
	public List<EndpointRcs> findAllChannelRc() {
		Query query = getSession().createQuery(
				"SELECT erc FROM EndpointRcs AS erc WHERE erc.endpoints.id in (SELECT id FROM erc.endpoints WHERE erc.endpoints.type = 'channel' ) and erc.isHidden = 'N' ");
				return query.list();
	}


	@Override
	public List<EndpointRcs> findByEndpointType(String type, String flag) {
		Query query = getSession().createQuery(
				"SELECT erc FROM EndpointRcs AS erc WHERE erc.endpoints.type = :type and erc.isHidden = :flag ");
		query.setParameter("type",type);
		query.setParameter("flag", flag);
		
		return query.list();
	}

	
	@Override
	public List<EndpointRcs> findAllBillerRc() {
		Query query = getSession().createQuery(
				"SELECT erc FROM EndpointRcs AS erc WHERE erc.endpoints.id in (SELECT id FROM erc.endpoints WHERE erc.endpoints.type = 'biller' ) and erc.isHidden = 'N' ");
				return query.list();
	}
	
}
