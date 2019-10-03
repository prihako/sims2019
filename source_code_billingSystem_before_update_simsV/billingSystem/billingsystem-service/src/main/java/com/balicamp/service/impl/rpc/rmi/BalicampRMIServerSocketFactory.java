package com.balicamp.service.impl.rpc.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class BalicampRMIServerSocketFactory 
	implements RMIServerSocketFactory, Serializable
{

	private static final long serialVersionUID = -4333731399994780287L;

	public ServerSocket createServerSocket(int port) throws IOException {
		return new ServerSocket(port);
	}

}
