package com.balicamp.service.mastermaintenance.client;

import java.util.List;

import com.balicamp.model.mastermaintenance.client.Client;
import com.balicamp.service.IManager;



public interface ClientManager extends IManager {

	List<Client> findAllClient();

	Client findClientByName(String clientName);

}
