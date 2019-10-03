package test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonUtil {
	private static final Log log = LogFactory.getLog(CommonUtil.class); // NOPMD

	/**
	 * Check if object empty
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(String object) {
		if (object == null)
			return true;
		if (object.toString().length() == 0)
			return true;
		return false;
	}

	public static boolean isEmpty(String object, boolean trim) {
		if (object == null)
			return true;

		if (trim)
			object = object.trim();

		if (object.toString().length() == 0)
			return true;
		return false;
	}

	public static boolean isNotEmpty(String object) {
		return !isEmpty(object);
	}

	public static boolean isNotEmpty(String object, boolean trim) {
		return !isEmpty(object, trim);
	}

	public static boolean isEmpty(Collection objectList) {
		if (objectList == null)
			return true;
		if (objectList.size() == 0)
			return true;
		return false;
	}

	public static boolean isEmpty(BigDecimal input) {
		if (input == null)
			return true;
		if (input.equals(BigDecimal.ZERO))
			return true;
		return false;
	}

	public static String[] splitString(String input, String delimiter) {
		List<String> resultList = new ArrayList<String>();

		String inputSplited[] = input.split(delimiter);
		for (String tmpString : inputSplited) {
			if (!isEmpty(tmpString))
				resultList.add(tmpString);
		}

		return resultList.toArray(new String[resultList.size()]);
	}

	public static Integer convertToInteger(String input) {
		if (CommonUtil.isEmpty(input))
			return null;
		return new Integer(input);
	}

	public static Object[] reverseArray(Object[] input) {
		List<Object> tmpList = Arrays.asList(input);
		Collections.reverse(tmpList);
		return tmpList.toArray();
	}

	public static Object getPropertySave(Object input, String propertyPath, Object defaultValue) {

		try {
			String[] propertyPathSplited = propertyPath.split("[.]");

			Object currentObject = input;
			for (String propertyName : propertyPathSplited) {

				if (CommonUtil.isEmpty(propertyName))
					continue;

				currentObject = PropertyUtils.getProperty(currentObject, propertyName);
				if (currentObject == null)
					return defaultValue;
			}
			return currentObject;
		} catch (Exception e) {
			log.error("", e);
			return defaultValue;
		}
	}

}
