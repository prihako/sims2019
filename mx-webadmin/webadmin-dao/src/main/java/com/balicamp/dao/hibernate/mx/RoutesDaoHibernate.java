package com.balicamp.dao.hibernate.mx;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.RoutesDao;
import com.balicamp.model.mx.Routes;
import com.balicamp.model.mx.Transactions;


/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: EndpointsDaoHibernate.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Repository
public class RoutesDaoHibernate extends MxGenericDaoHibernate<Routes, Long> implements RoutesDao {

	public RoutesDaoHibernate() {
		super(Routes.class);
	}

	@Override
	public Transactions findTransactionByEndpointCode(String endpointCode) {
		
		if(!endpointCode.equalsIgnoreCase("all")){
			Query query = getSession().createQuery("SELECT r from Routes r WHERE r.mappingsByNextInvocationId.endpoints.code = :code");
			query.setParameter("code", endpointCode);
			Transactions transaction = ((List<Routes>) query.list()).get(0).getTransactions();
			return transaction;
		}else{
			return new Transactions();
		}
		
	}

}
