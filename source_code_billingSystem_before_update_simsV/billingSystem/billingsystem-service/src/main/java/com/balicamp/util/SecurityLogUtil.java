package com.balicamp.util;

import com.balicamp.model.user.User;
import com.balicamp.util.SecurityContextUtil;

public class SecurityLogUtil {
	// security
	public static String generateSecurityLogMessage(Object... messages) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("ip '");
		stringBuilder.append(HttpServletRequestContextHolder.getRequest()
				.getRemoteAddr());
		stringBuilder.append("' ");

		User currentUser = SecurityContextUtil.getCurrentUser();
		if (currentUser != null) {
			stringBuilder.append("user '");
			stringBuilder.append(currentUser.getUsername());
		}
		stringBuilder.append("' ");

		for (Object tmpObject : messages) {
			stringBuilder.append(tmpObject);
		}
		return stringBuilder.toString();
	}

}
