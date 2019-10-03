package com.balicamp.dao.hibernate.mx;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.EndpointsDao;
import com.balicamp.model.mx.Endpoints;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointsDaoHibernate.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Repository
public class EndpointsDaoHibernate extends MxGenericDaoHibernate<Endpoints, Long> implements EndpointsDao {

	public EndpointsDaoHibernate() {
		super(Endpoints.class);
	}

	@Override
	public String findRcDescByEndpCode(String endpointCode, String rc) {

		Query query = getSession().createQuery(
				"SELECT erc.description FROM Endpoints AS e left outer join e.endpointRcses AS erc "
						+ "WHERE e.code=:endCode AND erc.rc=:rc");
		query.setParameter("endCode", endpointCode).setParameter("rc", rc);

		String obj = "";
		obj = (String) query.uniqueResult();
		/*
		 * List<?> queryResult = findByHQL(query, -1, -1);
		 * for (Object o : queryResult) {
		 * Object[] arrObject = (Object[]) o;
		 * obj = arrObject[1].toString();
		 * }
		 */
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Endpoints> findEndpointsByIds(Set<Integer> ids) {
		if (ids == null || ids.size() < 1)
			return new ArrayList<Endpoints>();

		StringBuffer hql = new StringBuffer("select a from Endpoints a where 1=1 ");
		int index = 0;
		for (; index < ids.size(); index++) {
			hql.append(" OR a.id=:id" + index);
		}
		Query query = getSession().createQuery(hql.toString());

		index--;
		List<Integer> listId = new ArrayList<Integer>(ids);
		for (; index >= 0; index--) {
			query.setParameter("id" + index, listId.get(index));
		}

		return query.list();
	}
}
