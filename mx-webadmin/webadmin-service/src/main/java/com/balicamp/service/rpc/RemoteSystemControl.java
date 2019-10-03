package com.balicamp.service.rpc;

import com.balicamp.util.ServiceMethod;

public interface RemoteSystemControl {
	String[] getAllPrincipals();

	String[] getPrincipals(String userId);

	void forceLogout(String userName);

	void pauseSystem();

	void continueSystem();

	void pause(boolean paused);

	boolean isPaused();

	ServiceMethod[] serviceMethods();

	void beginInvokeServiceMethod(String ident);

	void beginInvokeAllServiceMethods();

	String getEndpointPercentage();
}
