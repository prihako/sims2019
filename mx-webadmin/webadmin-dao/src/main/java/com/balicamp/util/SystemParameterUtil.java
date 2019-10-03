package com.balicamp.util;

import java.util.Map;

import com.balicamp.model.parameter.SystemParameter;

public class SystemParameterUtil {

	public static String getStringValue(Map<String, SystemParameter> systemParameterMap, String key, String defaultValue) {
		if (systemParameterMap == null) {
			return defaultValue;
		}

		SystemParameter systemParameter = systemParameterMap.get(key);

		if (systemParameter == null) {
			return defaultValue;
		}

		return systemParameter.getParamValue();
	}

	public static int getIntValue(Map<String, SystemParameter> systemParameterMap, String key, int defaultValue) {
		try {
			return Integer.valueOf(getStringValue(systemParameterMap, key, String.valueOf(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static boolean getBooleanValue(Map<String, SystemParameter> systemParameterMap, String key,
			boolean defaultValue) {
		try {
			String valueString = getStringValue(systemParameterMap, key, String.valueOf(defaultValue));

			return ((valueString != null) && (valueString.equalsIgnoreCase("true") || valueString.equalsIgnoreCase("1")));
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
