package com.balicamp.service.impl.security.acegi;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.concurrent.ConcurrentLoginException;
import org.acegisecurity.concurrent.SessionRegistry;
import org.acegisecurity.concurrent.SessionRegistryUtils;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.hibernate.QueryException;

import com.balicamp.Constants;
import com.balicamp.exception.UserBlockAdminException;
import com.balicamp.exception.UserBlockException;
import com.balicamp.exception.UserResetException;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.model.user.User;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.SecurityContextUtil;

/**
 * modified from acegi source
 * 
 * @version $Id: AuthenticationProcessingFilter.java 1534 2012-07-03 08:53:33Z
 *          arya.sutrisna $
 */
public class AuthenticationProcessingFilter extends AbstractProcessingFilter {

	private UserManager userManager;
	private SystemParameterManager systemParameterManager;
	private SessionRegistry sessionRegistry;

	// ~ Static fields/initializers
	// =====================================================================================

	public static final String ACEGI_SECURITY_FORM_USERNAME_KEY = "j_username";
	public static final String ACEGI_SECURITY_FORM_PASSWORD_KEY = "j_password";
	public static final String ACEGI_SECURITY_LAST_USERNAME_KEY = "ACEGI_SECURITY_LAST_USERNAME";

	// ~ Methods
	// ========================================================================================================

	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException {
		super.onSuccessfulAuthentication(request, response, authResult);

		// update last login
		request.getSession().setAttribute(Constants.HttpSessionAttribute.LAST_LOGIN_DATE,
				((User) authResult.getPrincipal()).getLastLogin());

		User user = userManager.findById(((User) authResult.getPrincipal()).getId());
		userManager.logLoginSuccess(user, new Date(), request.getRemoteHost());
		// userManager.loadUserProperties(user, true);
		// if(user.getUserPropertiesObject().getBlockReason()!=null)
		// {
		// user.getUserPropertiesObject().setBlockReason(null);
		// userManager.saveUserProperties(user, true, true);
		// }

		SecurityContextUtil.getCurrentUser().setStatus(user.getStatus());

		doClean(response);

		if ("A".equalsIgnoreCase(user.getStatus())) {
			doValidateChangePassword(user);
		}

		// fix session fixation attack
		doChangeSession(request);
	}

	public Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException {
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

		// Place the last username attempted into HttpSession for views
		request.getSession().setAttribute(ACEGI_SECURITY_LAST_USERNAME_KEY, username);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		Authentication authentication = null;
		try {
			authentication = getAuthenticationManager().authenticate(authRequest);
		} catch (AuthenticationException e) {
			if (e instanceof ConcurrentLoginException || e instanceof UserBlockException
					|| e instanceof UserBlockAdminException || e instanceof UserResetException) {
				request.getSession().invalidate();
				sessionRegistry.removeSessionInformation(request.getSession().getId());
				throw e;
			}

			// log if user fail login
			User user = userManager.findByUserName(username);
			if (user != null) {
				userManager.logLoginFail(user, new Date(), request.getRemoteHost());
			}

			throw e;
		} catch (QueryException ex) {
			userManager.tryingLogin(request.getRemoteHost());

			System.out.println("### >> BadCredentialsException : " + ex.getMessage());
			throw new BadCredentialsException(null);
		}

		return authentication;
	}

	/**
	 * This filter by default responds to <code>/j_acegi_security_check</code>.
	 * 
	 * @return the default
	 */
	public String getDefaultFilterProcessesUrl() {
		return "/j_acegi_security_check";
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * Enables subclasses to override the composition of the password, such as
	 * by including additional values and a separator.
	 * <p>
	 * This might be used for example if a postcode/zipcode was required in
	 * addition to the password. A delimiter such as a pipe (|) should be used
	 * to separate the password and extended value(s). The
	 * <code>AuthenticationDao</code> will need to generate the expected
	 * password in a corresponding manner.
	 * </p>
	 * 
	 * @param request
	 *            so that request attributes can be retrieved
	 * 
	 * @return the password that will be presented in the
	 *         <code>Authentication</code> request token to the
	 *         <code>AuthenticationManager</code>
	 */
	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(ACEGI_SECURITY_FORM_PASSWORD_KEY);
	}

	/**
	 * Enables subclasses to override the composition of the username, such as
	 * by including additional values and a separator.
	 * 
	 * @param request
	 *            so that request attributes can be retrieved
	 * 
	 * @return the username that will be presented in the
	 *         <code>Authentication</code> request token to the
	 *         <code>AuthenticationManager</code>
	 */
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(ACEGI_SECURITY_FORM_USERNAME_KEY);
	}

	/**
	 * Provided so that subclasses may configure what is put into the
	 * authentication request's details property.
	 * 
	 * @param request
	 *            that an authentication request is being created for
	 * @param authRequest
	 *            the authentication request object that should have its details
	 *            set
	 */
	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setSystemParameterManager(SystemParameterManager systemParameterManager) {
		this.systemParameterManager = systemParameterManager;
	}

	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	private void doClean(HttpServletResponse response) {
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "0");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	private synchronized void doChangeSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}

		String originalSessionId = session.getId();

		HashMap attributesToMigrate = null;

		if (true) {
			attributesToMigrate = new HashMap();

			Enumeration enumer = session.getAttributeNames();

			while (enumer.hasMoreElements()) {
				String key = (String) enumer.nextElement();
				attributesToMigrate.put(key, session.getAttribute(key));
			}
		}

		session.invalidate();
		session = request.getSession(true); // we now have a new session

		if (attributesToMigrate != null) {
			Iterator iter = attributesToMigrate.entrySet().iterator();

			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				session.setAttribute((String) entry.getKey(), entry.getValue());
			}
		}

		if (sessionRegistry != null) {
			sessionRegistry.removeSessionInformation(originalSessionId);
			Object principal = SessionRegistryUtils.obtainPrincipalFromAuthentication(SecurityContextHolder
					.getContext().getAuthentication());

			sessionRegistry.registerNewSession(session.getId(), principal);
		}

		// allow user open main.html
		session.setAttribute(Constants.HttpSessionAttribute.AUTH_MAIN_PAGE, Boolean.TRUE);
	}

	private void doValidateChangePassword(User user) {
		if (user.getChangePasswordDate() == null) {
			doResetPassword(user);
		} else {
			Calendar now = Calendar.getInstance();
			Calendar lastChangedPassword = Calendar.getInstance();

			lastChangedPassword.setTime(user.getChangePasswordDate());

			String expiredMode = systemParameterManager.getStringValue(new SystemParameterId(
					Constants.SystemParameter.Security.GROUP,
					Constants.SystemParameter.Security.ADMIN_PASS_EXPIRED_MODE), "0");

			if (expiredMode.equalsIgnoreCase("1")) {
				int expiredDays = systemParameterManager.getIntValue(new SystemParameterId(
						Constants.SystemParameter.Security.GROUP,
						Constants.SystemParameter.Security.ADMIN_PASS_EXPIRED_DAYS), 30);

				lastChangedPassword.add(Calendar.DATE, expiredDays);
				if (now.compareTo(lastChangedPassword) > 0) {
					doResetPassword(user);
				}
			}
		}
	}

	private void doResetPassword(User user) {
		user.setChangePasswordDate(new Date());
		user.setMustChangePassword(true);
		userManager.saveOrUpdate(user);
	}
}
