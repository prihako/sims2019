package com.balicamp.service.impl.mastermaintenance.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mastermaintenance.client.ClientDao;
import com.balicamp.model.mastermaintenance.client.Client;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.mastermaintenance.client.ClientManager;

@Service("clientManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ClientManagerImpl extends AbstractManager implements
		ClientManager, InitializingBean {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final ClientDao clientDao;

	private static final Log log = LogFactory.getLog(ClientManagerImpl.class);

	@Autowired
	public ClientManagerImpl(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Client> findAllClient(){
		List<Client> clientList = new ArrayList<Client>();
		clientList = clientDao.findAllClient();
		return clientList;
	}

	@Override
	public Client findClientByName(String clientName) {
		Client client = clientDao.findClientByName(clientName);
		return client;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return clientDao;
	}


}
