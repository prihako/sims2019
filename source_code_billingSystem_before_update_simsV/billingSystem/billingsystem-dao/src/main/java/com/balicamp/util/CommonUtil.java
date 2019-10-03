package com.balicamp.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.common.AuditModel;

/**
 * @version $Id: CommonUtil.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
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

	public static boolean isASCIICharacter(String object) {
		boolean isValid = false;

		CharSequence inputStr = object;
		Pattern pattern = Pattern.compile("[ A-Za-z0-9-]+");
		Matcher matcher = pattern.matcher(inputStr);
		if (!matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * set audit model
	 * 
	 * @param baseObject
	 * @return
	 */
	public static boolean interceptAuditModel(BaseAdminModel baseObject) {
		return interceptAuditModel(baseObject, SecurityContextUtil.getCurrentUserId());
	}

	public static boolean interceptAuditModel(BaseAdminModel baseObject, Long userId) {
		try {
			AuditModel auditModel = (AuditModel) PropertyUtils.getProperty(baseObject, "auditModel");
			if (auditModel == null) {
				auditModel = new AuditModel();
				PropertyUtils.setProperty(baseObject, "auditModel", auditModel);

				auditModel.setCreatedBy(userId);
				auditModel.setCreatedDate(new Date());
			}
			auditModel.setChangedBy(userId);
			auditModel.setChangedDate(new Date());

			return true;
		} catch (Exception e) {
			log.warn("Fail intercept interceptAuditModel", e);
			return false;
		}
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

	/**
	 * validate between password and confirmPassword
	 * @param Password
	 * @param confirmPassword
	 * @return true if match, false if not match
	 */
	public static boolean validatePassword(String password, String confirmPassword){

		if(password.equals(confirmPassword)){
			return true;
		} else {
			return false;	
		}		
	}
	
	/**
	 * Validation Numeric<br>
	 * @param number
	 * @return boolean isValid
	 */
	public static boolean isNumeric(String number) {
		boolean isValid = false;

		String expression = "^[-+]?[0-9]*\\.?[0-9]+$";
		CharSequence inputStr = number;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
}
