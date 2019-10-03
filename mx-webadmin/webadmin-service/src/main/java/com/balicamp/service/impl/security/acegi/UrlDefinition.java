package com.balicamp.service.impl.security.acegi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.intercept.web.PathBasedFilterInvocationDefinitionMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balicamp.model.function.Function;
import com.balicamp.service.function.FunctionManager;
import com.balicamp.util.CommonUtil;

public class UrlDefinition extends PathBasedFilterInvocationDefinitionMap {
	protected final Log log = LogFactory.getLog(UrlDefinition.class);
	private final String URL_PREFIX = "/**/";
	private final String URL_SUFFIX1 = ".*";
	private final String URL_SUFFIX2 = ",*";

	private void add(Map<String, ConfigAttributeDefinition> defs, String path,
			String fname) {
		ConfigAttributeDefinition def = defs.get(path);
		if (def == null) {
			def = new ConfigAttributeDefinition();
			defs.put(path, def);
		}

		def.addConfigAttribute(new SecurityConfig(fname));
	}

	public UrlDefinition(FunctionManager functionManager, String userUrl) {
		this.userUrl = userUrl;

		List<Function> functionList = functionManager.findAll();

		Map<String, ConfigAttributeDefinition> defs = new HashMap<String, ConfigAttributeDefinition>();

		for (Function function : functionList) {
			String fname = function.getName();

			if (function.getPages() != null) {
				String[] pages = function.getPages().split(";");

				for (String page : pages) {
					add(defs, URL_PREFIX + page.trim() + URL_SUFFIX1, fname);
					add(defs, URL_PREFIX + page.trim() + URL_SUFFIX2, fname);
				}
			}
		}

		List<String> allUserPageList = getUserUrlList();

		for (String allUserPage : allUserPageList) {
			add(defs, URL_PREFIX + allUserPage + URL_SUFFIX1, "user");
			add(defs, URL_PREFIX + allUserPage + URL_SUFFIX2, "user");
		}

		for (Map.Entry<String, ConfigAttributeDefinition> defEntry : defs
				.entrySet()) {
			addSecureUrl(defEntry.getKey(), defEntry.getValue());
		}

		// securing pages for each function
		// for (Function function : functionList) {
		// ConfigAttributeDefinition configDefinition = new
		// ConfigAttributeDefinition();
		// configDefinition.addConfigAttribute(new
		// SecurityConfig(function.getName()));
		//
		// if (function.getPages() != null) {
		// String[] pages = function.getPages().split(";");
		// for (String page : pages) {
		// addSecureUrl(URL_PREFIX + page.trim() + URL_SUFFIX1,
		// configDefinition);
		// addSecureUrl(URL_PREFIX + page.trim() + URL_SUFFIX2,
		// configDefinition);
		// log.debug("secure page: " + page + ", function: " +
		// function.getName());
		// }
		// }
		// }
		//
		// //allUser url
		// List<String> allUserPageList = getUserUrlList();
		// ConfigAttributeDefinition allUserconfigDefinition = new
		// ConfigAttributeDefinition();
		// allUserconfigDefinition.addConfigAttribute(new
		// SecurityConfig("user"));
		// for (String allUserPage : allUserPageList) {
		// addSecureUrl(URL_PREFIX + allUserPage + URL_SUFFIX1,
		// allUserconfigDefinition);
		// addSecureUrl(URL_PREFIX + allUserPage + URL_SUFFIX2,
		// allUserconfigDefinition);
		// }
	}

	private String userUrl;

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	private List<String> getUserUrlList() {
		List<String> userUrlList = new ArrayList<String>();

		if (CommonUtil.isEmpty(userUrl))
			return userUrlList;

		String userUrlSplited[] = userUrl.split(";");
		for (String userUrlItem : userUrlSplited) {
			userUrlItem = userUrlItem.trim();
			if (!CommonUtil.isEmpty(userUrlItem))
				userUrlList.add(userUrlItem);
		}

		return userUrlList;
	}

}
