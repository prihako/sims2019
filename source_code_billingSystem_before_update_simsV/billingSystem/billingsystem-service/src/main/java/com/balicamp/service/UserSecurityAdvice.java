package com.balicamp.service;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationTrustResolver;
import org.acegisecurity.AuthenticationTrustResolverImpl;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import com.balicamp.Constants;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserRole;

public class UserSecurityAdvice implements MethodBeforeAdvice,
		AfterReturningAdvice {
	public final static String ACCESS_DENIED = "Access Denied: Only administrators are allowed to modify other users.";
	protected final Log log = LogFactory.getLog(UserSecurityAdvice.class);

	/**
	 * Method to enforce security and only allow administrators to modify users.
	 * Regular users are allowed to modify themselves.
	 */
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		SecurityContext ctx = SecurityContextHolder.getContext();

		if (ctx.getAuthentication() != null) {
			Authentication auth = ctx.getAuthentication();
			boolean administrator = false;
			GrantedAuthority[] roles = auth.getAuthorities();
			for (int i = 0; i < roles.length; i++) {
				if (roles[i].getAuthority().equals(Constants.ADMIN_ROLE)) {
					administrator = true;
					break;
				}
			}

			User user = (User) args[0];
			String username = user.getUsername();

			String currentUser;
			if (auth.getPrincipal() instanceof UserDetails) {
				currentUser = ((UserDetails) auth.getPrincipal()).getUsername();
			} else {
				currentUser = String.valueOf(auth.getPrincipal());
			}

			if (username != null && !username.equals(currentUser)) {
				AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
				// allow new users to signup - this is OK b/c Signup doesn't
				// allow setting of roles
				boolean signupUser = resolver.isAnonymous(auth);
				if (!signupUser) {
					if (log.isDebugEnabled()) {
						log.debug("Verifying that '" + currentUser
								+ "' can modify '" + username + "'");
					}
					if (!administrator) {
						log.warn("Access Denied: '" + currentUser
								+ "' tried to modify '" + username + "'!");
						throw new AccessDeniedException(ACCESS_DENIED);
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Registering new user '" + username + "'");
					}
				}
			}

			// fix for http://issues.appfuse.org/browse/APF-96
			// don't allow users with "user" role to upgrade to "admin" role
			else if (username != null && username.equalsIgnoreCase(currentUser)
					&& !administrator) {

				// get the list of roles the user is trying add
				Set userRoles = new HashSet();
				if (user.getUserRoleSet() != null) {
					for (Iterator<UserRole> it = user.getUserRoleSet()
							.iterator(); it.hasNext();) {
						UserRole userRole = it.next();
						userRoles.add(userRole.getUserRoleId().getRole()
								.getName());
					}
				}

				// get the list of roles the user currently has
				Set authorizedRoles = new HashSet();
				for (int i = 0; i < roles.length; i++) {
					authorizedRoles.add(roles[i].getAuthority());
				}

				// if they don't match - access denied
				// users aren't allowed to change their roles
				if (!CollectionUtils.isEqualCollection(userRoles,
						authorizedRoles)) {
					log.warn("Access Denied: '" + currentUser
							+ "' tried to change their role(s)!");
					throw new AccessDeniedException(ACCESS_DENIED);
				}
			}
		}
	}

	public void afterReturning(Object arg0, Method arg1, Object[] arg2,
			Object arg3) throws Throwable {
		// TODO Auto-generated method stub
	}

}
