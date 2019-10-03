package com.balicamp.service.impl.rpc.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class BalicampRMIClientSocketFactory 
	implements RMIClientSocketFactory, Serializable 
{	
	private static final long serialVersionUID = 4138676351162206407L;
	
	private String serverIp;
	public BalicampRMIClientSocketFactory(){
	}

	public Socket createSocket(String host, int port) throws IOException {
		if ( serverIp == null ){
			return new Socket(host, port);			
		} else {
			return new Socket(serverIp, port);
		}
	}

	//getter setter
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

}
