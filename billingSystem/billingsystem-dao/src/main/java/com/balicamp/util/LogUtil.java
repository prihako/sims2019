package com.balicamp.util;

/**
 * Log Util
 */
public class LogUtil {

	/**
	 * Util method for formating log message
	 * 
	 * @param identity
	 * @param messages
	 * @return
	 */
	public static String generateLog(String identity, Object... messages) {
		if (identity == null)
			return concatMessage(messages);

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append('[');
		stringBuilder.append(identity);
		stringBuilder.append("] ");

		for (Object tmpObject : messages) {
			stringBuilder.append(tmpObject);
		}

		return stringBuilder.toString();
	}

	/**
	 * Util method for formating log message
	 * 
	 * @param type
	 * @param identity
	 * @param messages
	 * @return
	 */
	public static String generateLogLongIdentity(String type, Long identity,
			Object... messages) {
		StringBuilder logIdentity = new StringBuilder(type);
		logIdentity.append("-");
		logIdentity.append(identity);

		return LogUtil.generateLog(logIdentity.toString(), messages);
	}

	/**
	 * Util method for formating log message
	 * 
	 * @param type
	 * @param identity
	 * @param messages
	 * @return
	 */
	public static String generateLogStringIdentity(String type,
			String identity, Object... messages) {
		StringBuilder logIdentity = new StringBuilder(type);
		logIdentity.append(identity);

		return LogUtil.generateLog(logIdentity.toString(), messages);
	}

	/**
	 * concat object
	 * 
	 * @param messages
	 * @return
	 */
	public static String concatMessage(Object... messages) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Object tmpObject : messages) {
			stringBuilder.append(tmpObject);
		}
		return stringBuilder.toString();
	}

}
