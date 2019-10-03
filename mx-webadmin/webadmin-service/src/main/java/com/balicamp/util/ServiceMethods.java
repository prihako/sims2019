package com.balicamp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.balicamp.service.common.TaskExecutorWrapper;

public abstract class ServiceMethods {

	private static final Log LOG = LogFactory.getLog(ServiceMethods.class);

	private static final Map<String, Map<String, ServiceMethod>> REGISTRY_MAP;
	private static final Map<String, List<ServiceMethod>> REGISTRY_LIST;

	static {
		REGISTRY_MAP = new HashMap<String, Map<String,ServiceMethod>>();
		REGISTRY_MAP.put("all", new TreeMap<String, ServiceMethod>());
		REGISTRY_MAP.put("public", new TreeMap<String, ServiceMethod>());
		REGISTRY_MAP.put("admin", new TreeMap<String, ServiceMethod>());
		
		REGISTRY_LIST = new HashMap<String, List<ServiceMethod>>();
		REGISTRY_LIST.put("all", new ArrayList<ServiceMethod>());
		REGISTRY_LIST.put("public", new ArrayList<ServiceMethod>());
		REGISTRY_LIST.put("admin", new ArrayList<ServiceMethod>());

		add("localSystemControl", "serviceMethod.caption.pause", "pauseSystem", "all", "public");
		add("systemParameterManager", "serviceMethod.caption.systemParameterManager", "softReset", "all", "public", "admin");
		add("menuTreeManager", "serviceMethod.caption.menuTreeManager", "softReset", "all", "public", "admin");
		add("functionRoleManager", "serviceMethod.caption.functionRoleManager", "softReset", "all", "public", "admin");
		add("transactionLimitManager", "serviceMethod.caption.transactionLimitManager", "softReset", "all", "public", "admin");
		add("productTreeManager", "serviceMethod.caption.productTreeManager", "softReset", "all", "public", "admin");
		add("productTreeRoleManager", "serviceMethod.caption.productTreeRoleManager", "softReset", "all", "public", "admin");
		add("prefixManager", "serviceMethod.caption.prefixManager", "softReset", "all", "public");
		//add("userPGroupManager", "serviceMethod.caption.userPGroupManager", "softReset", "admin");
		//add("houseKeepingManager", "serviceMethod.caption.houseKeepingManager", "automaticBackupAndClean", "admin");
		add("accountStatementReplicationManager", "leftMenu.system.replicate.accountMutation", "autoReplicateAccountStatement", "admin");
		add("localSystemControl", "serviceMethod.caption.continue", "continueSystem", "all", "public");
	}

	private static void add(String bean, String caption, String method, String... contextType) {
		ServiceMethod sm = new ServiceMethod(bean, caption, method);
		
		for (String ct : contextType) {
			REGISTRY_MAP.get(ct).put(sm.getIdent(), sm);
			REGISTRY_LIST.get(ct).add(sm);
		}
	}

	public static Map<String, ServiceMethod> entries(String contextType) {
		return REGISTRY_MAP.get(contextType);
	}

	public static List<ServiceMethod> entriesAsList(String contextType) {
		return REGISTRY_LIST.get(contextType);
	}
	
	public static void beginInvokeAll(TaskExecutorWrapper executor,
			final ApplicationContext springctx, final String contextType) {
		executor.executeTask(new Runnable() {
			
			public void run() {
				for (ServiceMethod sm : REGISTRY_LIST.get(contextType)) {
					if (!invoke(springctx, contextType, sm.getIdent())) {
						break;
					}
				}
			}
		});
	}
	
	public static void beginInvoke(TaskExecutorWrapper executor,
			final ApplicationContext springctx, final String contextType, final String ident) {
		executor.executeTask(new Runnable() {
			
			public void run() {
				invoke(springctx, contextType, ident);
			}
		});
	}

	public static boolean invoke(ApplicationContext springctx, String contextType, String ident) {
		ServiceMethod bean = null;
		boolean error = false;
		
		LOG.info("starting service #" + ident + ".");

		try {
			bean = REGISTRY_MAP.get(contextType).get(ident);
			if (bean == null) {
				throw new Exception("service not found");
			}
			
			if (!bean.getBusy().compareAndSet(false, true)) {
				throw new Exception("service is busy");
			}

			Object obj = springctx.getBean(bean.getBeanName());
			if (obj == null) {
				throw new Exception("referred bean named " + bean.getBeanName() + " not found");
			}

			Class<?> cla = obj.getClass();

			try {
				cla.getMethod(bean.getMethodName()).invoke(obj);
			} catch (NoSuchMethodException ex) {
				cla.getMethod(bean.getMethodName(), ServiceMethod.class).invoke(obj, bean);
			}
			
			LOG.info("service #" + ident + " started. info: beanName=" + bean.getBeanName() +
					", caption=" + bean.getCaption() + ",methodName=" + bean.getMethodName() + ".");
		} catch (Throwable t) {
			if (bean == null) {
				LOG.error("unable to start service #" + ident + ".", t);
			} else {
				LOG.error("unable to start service #" + ident + ". info: beanName=" + bean.getBeanName() +
						", caption=" + bean.getCaption() + ",methodName=" + bean.getMethodName() + ".", t);
			}
			
			error = true;
		}
		
		if (bean != null) {
			bean.getBusy().set(false);
			bean.getError().set(error);
		}
		
		return !error;
	}
}
