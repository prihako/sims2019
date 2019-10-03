package com.balicamp.dao.hibernate.mastermaintenance.client;

import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mastermaintenance.client.Client;



/**
 * DAO for Client Entity
 *
 * @author hendy yusprasetya
 *
 */
public interface ClientDao extends GenericDao<Client, Long>{

	List<Client> findAllClient();

	Client findClientByName(String clientName);
}
