/**
 *
 */
package com.balicamp.dao.hibernate.mastermaintenance.client;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.GenericDaoHibernate;
import com.balicamp.model.mastermaintenance.client.Client;

/**
 * @author hendy yusprasetya
 *
 */
@Repository("clientDaoHibernate")
public class ClientDaoHibernate extends GenericDaoHibernate<Client, Long>
		implements ClientDao {

	private SessionFactory hibernateSessionFactory;

	@Autowired(required = true)
	public void setHibernateSessionFactory(
			@Qualifier("mxHibernateSessionFactory") SessionFactory hibernateSessionFactory) {
		this.hibernateSessionFactory = hibernateSessionFactory;
		super.setSessionFactory(hibernateSessionFactory);
	}

	public SessionFactory getHibernateSessionFactory() {
		return hibernateSessionFactory;
	}


	public ClientDaoHibernate() {
		super(Client.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Client> findAllClient() {
		Query query = getSession().createQuery("SELECT c FROM Client c");
		List<Client> clientList = query.list();
		return clientList;
	}

	@Override
	public Client findClientByName(String clientName) {
		Query query = getSession().createQuery("SELECT c FROM Client c "
				+ "where c.clientName = ?");
		List<Client> clientList = query.list();
		Client client = null;
		if(clientList.size()>0){
			client = clientList.get(0);
		}
		return client;
	}

}
