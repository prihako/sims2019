package com.balicamp.service.common;

import java.util.Locale;

/**
 * Wrapper manager for resource boundle
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public interface MessageSourceWrapper {
	/**
	 * Get Message
	 * @param key
	 * @param args
	 * @param defaultMessage
	 * @param locale
	 * @return
	 */
	String getMessage(String key, Object[] args, String defaultMessage, Locale locale);
}
