package com.balicamp.service.impl.security.acegi;

import java.util.Date;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.providers.encoding.PlaintextPasswordEncoder;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;

import com.balicamp.Constants;
import com.balicamp.exception.UserBlockAdminException;
import com.balicamp.exception.UserBlockException;
import com.balicamp.exception.UserBlockTokenException;
import com.balicamp.exception.UserResetException;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserProperties;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.SecurityLogUtil;

/**
 * modified from acegi source
 */
public class DaoAuthenticationProvider extends
		AbstractUserDetailsAuthenticationProvider implements
		ApplicationContextAware {
	protected final Log securityLog = LogFactory
			.getLog(Constants.Log.SECURITY_LOG);// NOPMD

	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	private UserManager getUserManager() {
		return (UserManager) applicationContext.getBean("userManager");
	}

	private SystemParameterManager getSystemParameterManager() {
		return (SystemParameterManager) applicationContext
				.getBean("systemParameterManager");
	}

	// ~ Instance fields
	// ================================================================================================

	private PasswordEncoder passwordEncoder = new PlaintextPasswordEncoder();

	private SaltSource saltSource;

	private UserDetailsService userDetailsService;

	private boolean includeDetailsObject = true;

	// ~ Methods
	// ========================================================================================================

	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		Object salt = null;

		if (this.saltSource != null) {
			salt = this.saltSource.getSalt(userDetails);
		}

		if (authentication.getCredentials() == null) {
			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"), includeDetailsObject ? userDetails
					: null);
		}

		String presentedPassword = authentication.getCredentials() == null ? ""
				: authentication.getCredentials().toString();

		// cek if user block
		User user = getUserManager().findById(((User) userDetails).getId());
		getUserManager().loadUserProperties(user, true);
		if (user.getUserPropertiesObject() == null)
			user.setUserPropertiesObject(new UserProperties());
		if (user.getStatus().equals(ModelConstant.User.STATUS_BLOCKED)) {
			// update for ATMB, message untuk block salah password dengan block
			// salah masukin token dibedakan

			if (user.getBlockInterval() == null || user.getBlockTime() == null) {
				if (user.getUserPropertiesObject().getBlockReason()
						.equals(ModelConstant.User.BLOCK_BY_ADMIN)) {
					throw new UserBlockAdminException(
							"User block without interval");
				} else if (user.getUserPropertiesObject().getBlockReason()
						.equals(ModelConstant.User.BLOCK_WRONG_TOKEN)) {
					throw new UserBlockTokenException(
							"User block without interval");
				} else
					throw new UserBlockException("User block without interval");
			}
			if (user.getBlockInterval() == -1) {
				if (user.getUserPropertiesObject().getBlockReason()
						.equals(ModelConstant.User.BLOCK_BY_ADMIN)) {
					throw new UserBlockAdminException(
							"User block without interval");
				} else if (user.getUserPropertiesObject().getBlockReason()
						.equals(ModelConstant.User.BLOCK_WRONG_TOKEN)) {
					throw new UserBlockTokenException(
							"User block without interval");
				} else
					throw new UserBlockException("User block without interval");
			}
			if ((new Date()).getTime() - user.getBlockTime().getTime() < user
					.getBlockInterval() * 1000) {
				if (user.getUserPropertiesObject().getBlockReason()
						.equals(ModelConstant.User.BLOCK_BY_ADMIN)) {
					throw new UserBlockAdminException("User block from "
							+ user.getBlockTime() + " for "
							+ user.getBlockInterval() + " seconds ");
				} else if (user.getUserPropertiesObject().getBlockReason()
						.equals(ModelConstant.User.BLOCK_WRONG_TOKEN)) {
					throw new UserBlockTokenException("User block from "
							+ user.getBlockTime() + " for "
							+ user.getBlockInterval() + " seconds ");
				} else
					throw new UserBlockException("User block from "
							+ user.getBlockTime() + " for "
							+ user.getBlockInterval() + " seconds ");
			}
		} else if (user.getStatus().equals(ModelConstant.User.STATUS_RESET)) {
			UserResetException userResetException = new UserResetException(
					"User reset");
			throw userResetException;
		}

		if (!passwordEncoder.isPasswordValid(userDetails.getPassword(),
				presentedPassword, salt)) {
			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"), includeDetailsObject ? userDetails
					: null);
		} else if (user.getStatus().equals(ModelConstant.User.STATUS_BLOCKED)) {
			getUserManager().unblockUser(user);
		}
	}

	protected void doAfterPropertiesSet() throws Exception {
		if (userDetailsService == null) {
			userDetailsService = getUserManager();
		}
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public SaltSource getSaltSource() {
		return saltSource;
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	protected final UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails loadedUser = null;

		try {
			loadedUser = getUserManager().loadUserByUsername(username);
			User user = (User) loadedUser;

			String contextType = getSystemParameterManager()
					.getStringConfiguration(
							Constants.SystemParameter.Configuration.CONTEXT_TYPE,
							Constants.SystemParameter.Configuration.ContextType.PUBLIC);
			if (contextType
					.equals(Constants.SystemParameter.Configuration.ContextType.ALL)) {

			} else if (contextType
					.equals(Constants.SystemParameter.Configuration.ContextType.PUBLIC)
					&& user.getStatus().equals(ModelConstant.User.STATUS_ADMIN)) {
				loadedUser = null;
				throw new UsernameNotFoundException("user admin '" + username
						+ "' try login on public");
			} else if (contextType
					.equals(Constants.SystemParameter.Configuration.ContextType.ADMIN)
					&& !user.getStatus()
							.equals(ModelConstant.User.STATUS_ADMIN)) {
				loadedUser = null;
				throw new UsernameNotFoundException("user public '" + username
						+ "' try login on admin");
			}
		} catch (UsernameNotFoundException ex) {
			securityLog.info(SecurityLogUtil.generateSecurityLogMessage(
					"gagal login user '", username, "' tidak ditemukan"));

			throw ex;
		} catch (DataAccessException repositoryProblem) {
			throw new AuthenticationServiceException(
					repositoryProblem.getMessage(), repositoryProblem);
		}

		if (loadedUser == null) {
			throw new AuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}

		return loadedUser;
	}

	/**
	 * Sets the PasswordEncoder instance to be used to encode and validate
	 * passwords. If not set, {@link PlaintextPasswordEncoder} will be used by
	 * default.
	 * 
	 * @param passwordEncoder
	 *            The passwordEncoder to use
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * The source of salts to use when decoding passwords. <code>null</code> is
	 * a valid value, meaning the <code>DaoAuthenticationProvider</code> will
	 * present <code>null</code> to the relevant <code>PasswordEncoder</code>.
	 * 
	 * @param saltSource
	 *            to use when attempting to decode passwords via the
	 *            <code>PasswordEncoder</code>
	 */
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public boolean isIncludeDetailsObject() {
		return includeDetailsObject;
	}

	public void setIncludeDetailsObject(boolean includeDetailsObject) {
		this.includeDetailsObject = includeDetailsObject;
	}
}
