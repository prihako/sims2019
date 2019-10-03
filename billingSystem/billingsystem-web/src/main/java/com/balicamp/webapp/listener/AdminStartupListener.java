package com.balicamp.webapp.listener;

import static com.balicamp.Constants.SystemParameter.Configuration.CONTEXT_PATH_REAL_DIR;
import static com.balicamp.Constants.SystemParameter.Configuration.CONTEXT_TYPE;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.ProviderManager;
import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import org.acegisecurity.providers.rememberme.RememberMeAuthenticationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.webapp.constant.WebConstant;
import com.balicamp.webapp.util.ApplicationContextHolder;

/**
 * <p>StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 * 
 * <p>Keep in mind that this listener is executed outside of OpenSessionInViewFilter,
 * so if you're using Hibernate you'll have to explicitly initialize all loaded data at the 
 * Dao or service level to avoid LazyInitializationException. Hibernate.initialize() works
 * well for doing this.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class AdminStartupListener extends ContextLoaderListener implements ServletContextListener {

	private static final Log log = LogFactory.getLog(AdminStartupListener.class);

	public void contextInitialized(ServletContextEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("initializing context...");
		}

		// call Spring's context ContextLoaderListener to initialize
		// all the context files specified in web.xml
		super.contextInitialized(event);

		ServletContext context = event.getServletContext();

		// Orion starts Servlets before Listeners, so check if the config
		// object already exists
		Map config = (HashMap) context.getAttribute(WebConstant.CONFIG);

		if (config == null) {
			config = new HashMap();
		}

		if (context.getInitParameter(WebConstant.CSS_THEME) != null) {
			config.put(WebConstant.CSS_THEME, context.getInitParameter(WebConstant.CSS_THEME));
		}

		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		ApplicationContextHolder.context = ctx;

		boolean encryptPassword = false;
		try {
			ProviderManager provider = (ProviderManager) ctx.getBean("authenticationManager");
			for (Iterator it = provider.getProviders().iterator(); it.hasNext();) {
				AuthenticationProvider p = (AuthenticationProvider) it.next();
				if (p instanceof RememberMeAuthenticationProvider) {
					config.put("rememberMeEnabled", Boolean.TRUE);
				}
			}

			if (ctx.containsBean("passwordEncoder")) {
				encryptPassword = true;
				config.put(WebConstant.ENCRYPT_PASSWORD, Boolean.TRUE);
				String algorithm = "SHA";
				if (ctx.getBean("passwordEncoder") instanceof Md5PasswordEncoder) {
					algorithm = "MD5";
				}
				config.put(WebConstant.ENC_ALGORITHM, algorithm);
			}
		} catch (NoSuchBeanDefinitionException n) {
			// ignore, should only happen when testing
		}

		context.setAttribute(WebConstant.CONFIG, config);

		// output the retrieved values for the Init and Context Parameters
		if (log.isDebugEnabled()) {
			log.debug("Remember Me Enabled? " + config.get("rememberMeEnabled"));
			log.debug("Encrypt Passwords? " + encryptPassword);
			if (encryptPassword) {
				log.debug("Encryption Algorithm: " + config.get(WebConstant.ENC_ALGORITHM));
			}
			log.debug("Populating drop-downs...");
		}

		String contextPath = event.getServletContext().getRealPath("");
		if (contextPath == null) {
			contextPath = ".";
		} else {
			contextPath = contextPath.trim();

			if (contextPath.length() == 0) {
				contextPath = ".";
			} else if (!File.separator.equals(contextPath) && contextPath.endsWith(File.separator)) {
				contextPath = contextPath.substring(0, contextPath.length() - 1);
			}
		}

		SystemParameterManager systemParameterManager = (SystemParameterManager) ctx.getBean("systemParameterManager");
		systemParameterManager.addConfiguration(CONTEXT_PATH_REAL_DIR, contextPath);
		systemParameterManager.addConfiguration(CONTEXT_TYPE, context.getInitParameter(CONTEXT_TYPE));

	}
}
