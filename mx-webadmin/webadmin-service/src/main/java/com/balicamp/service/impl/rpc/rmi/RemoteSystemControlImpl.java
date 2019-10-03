package com.balicamp.service.impl.rpc.rmi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acegisecurity.concurrent.SessionInformation;
import org.acegisecurity.concurrent.SessionRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.balicamp.Constants;
import com.balicamp.service.common.TaskExecutorWrapper;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.rpc.RemoteSystemControl;
import com.balicamp.util.CommonUtil;
import com.balicamp.util.ServiceMethod;
import com.balicamp.util.ServiceMethods;
import com.google.gson.Gson;

@Service("localSystemControl")
// @Lazy(value = true)
public class RemoteSystemControlImpl implements RemoteSystemControl, ApplicationContextAware {

	private ApplicationContext springctx;

	private SessionRegistry sessionRegistry;

	private SystemParameterManager systemParameterManager;

	private TaskExecutorWrapper taskExecutorWrapper;

	@Autowired
	public void setSessionRegistry(@Qualifier("sessionRegistry") SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	@Autowired
	public void setSystemParameterManager(SystemParameterManager systemParameterManager) {
		this.systemParameterManager = systemParameterManager;
	}

	@Autowired
	public void setTaskExecutorWrapper(TaskExecutorWrapper taskExecutorWrapper) {
		this.taskExecutorWrapper = taskExecutorWrapper;
	}

	public void setApplicationContext(ApplicationContext springctx) throws BeansException {
		this.springctx = springctx;
	}

	public String[] getAllPrincipals() {
		Object[] principals = sessionRegistry.getAllPrincipals();

		List<String> activePrincipalList = new ArrayList<String>();
		for (Object principal : principals) {
			boolean expired = false;

			SessionInformation[] sessionInformations = sessionRegistry.getAllSessions(principal, true);
			for (SessionInformation sessionInformation : sessionInformations) {
				if (sessionInformation.isExpired())
					expired = true;
			}

			if (!expired) {
				activePrincipalList.add((String) principal);
			}

		}

		return activePrincipalList.toArray(new String[0]);
	}

	public String[] getPrincipals(String userId) {
		Object[] principals = sessionRegistry.getAllPrincipals();

		List<String> activePrincipalList = new ArrayList<String>();
		for (Object principal : principals) {

			System.out.println("principal : " + (String) principal);

			boolean expired = false;

			SessionInformation[] sessionInformations = sessionRegistry.getAllSessions(principal, true);

			for (SessionInformation sessionInformation : sessionInformations) {
				if (sessionInformation.isExpired()) {
					expired = true;
				}
			}

			if (!expired) {
				if (CommonUtil.isEmpty(userId, true)) {
					activePrincipalList.add((String) principal);
				} else {
					String principalString = (String) principal;
					if (principalString.indexOf(userId) != -1) {
						activePrincipalList.add((String) principal);
					}
				}
			}
		}

		return activePrincipalList.toArray(new String[0]);
	}

	public void forceLogout(String userName) {
		SessionInformation[] si = sessionRegistry.getAllSessions(userName, false);
		if (si != null) {
			for (int i = 0; i < si.length; i++) {
				si[i].expireNow();
			}
		}
	}

	public void forceLogoutAll() {
		Object[] principals = sessionRegistry.getAllPrincipals();

		for (Object principal : principals) {
			SessionInformation[] siList = sessionRegistry.getAllSessions(principal, true);

			for (SessionInformation si : siList) {
				if (!si.isExpired()) {
					si.expireNow();
				}
			}
		}
	}

	public void pauseSystem() {
		pause(true);
	}

	public void continueSystem() {
		pause(false);
	}

	public void pause(boolean paused) {
		systemParameterManager.setPause(paused);

		if (paused) {
			forceLogoutAll();
		}
	}

	public boolean isPaused() {
		return systemParameterManager.isPaused();
	}

	public ServiceMethod[] serviceMethods() {
		String contextType = systemParameterManager.getStringConfiguration(
				Constants.SystemParameter.Configuration.CONTEXT_TYPE, "all");

		return ServiceMethods.entriesAsList(contextType).toArray(new ServiceMethod[0]);
	}

	public void beginInvokeServiceMethod(String ident) {
		String contextType = systemParameterManager.getStringConfiguration(
				Constants.SystemParameter.Configuration.CONTEXT_TYPE, "all");

		ServiceMethods.beginInvoke(taskExecutorWrapper, springctx, contextType, ident);
	}

	public void beginInvokeAllServiceMethods() {
		String contextType = systemParameterManager.getStringConfiguration(
				Constants.SystemParameter.Configuration.CONTEXT_TYPE, "all");

		ServiceMethods.beginInvokeAll(taskExecutorWrapper, springctx, contextType);
	}

	@Override
	public String getEndpointPercentage() {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("edc01", 1L);
		Gson gson = new Gson();
		return gson.toJson(map);
	}
}
