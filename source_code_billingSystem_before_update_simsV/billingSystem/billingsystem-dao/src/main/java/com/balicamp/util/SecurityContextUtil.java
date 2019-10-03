package com.balicamp.util;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

import com.balicamp.model.user.User;

public abstract class SecurityContextUtil {

	public static User getCurrentUser() {
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx == null) {
			return null;
		}

		Authentication auth = ctx.getAuthentication();
		if (auth == null) {
			return null;
		}

		Object principal = auth.getPrincipal();
		if (principal == null) {
			return null;
		}

		if (principal instanceof User) {
			return (User) principal;
		}

		return null;
	}

	public static Long getCurrentUserId() {
		User loginUser = getCurrentUser();
		if (loginUser != null) {
			return loginUser.getId();
		}

		return null;
	}

	public static String getRequestIp() {
		return "";
	}
}
