package com.balicamp.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balicamp.util.DateUtil;

/**
 * This class is converts a java.util.Date to a String and a String to a
 * java.util.Date.
 * 
 * <p>
 * <a href="DateConverter.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class DateConverter implements Converter {
	private static final Log log = LogFactory.getLog(DateConverter.class);

	public Object convert(Class type, Object value) {
		if (value == null) {
			return null;
		} else if (type == Timestamp.class) {
			return convertToDate(type, value, DateUtil.getDateTimePattern());
		} else if (type == Date.class) {
			return convertToDate(type, value, DateUtil.getDatePattern());
		} else if (type == String.class) {
			return convertToString(type, value);
		}

		throw new ConversionException("Could not convert "
				+ value.getClass().getName() + " to " + type.getName());
	}

	public static String dateToString(Date date, String pattern) {
		DateFormat df = null;
		try {
			df = new SimpleDateFormat(pattern);
		} catch (Exception ex) {
			log.error("Pattern invalid.");
			df = new SimpleDateFormat("ddMMyyyy");
		}
		return df.format(date);
	}

	protected Object convertToDate(Class type, Object value, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		if (value instanceof String) {
			try {
				if (StringUtils.isEmpty(value.toString())) {
					return null;
				}

				Date date = df.parse((String) value);
				if (type.equals(Timestamp.class)) {
					return new Timestamp(date.getTime());
				}
				return date;
			} catch (Exception pe) {
				log.error("convertToDate parseException", pe);
				throw new ConversionException("Error converting String to Date");
			}
		}

		throw new ConversionException("Could not convert "
				+ value.getClass().getName() + " to " + type.getName());
	}

	protected Object convertToString(Class type, Object value) {

		if (value instanceof Date) {
			DateFormat df = new SimpleDateFormat(DateUtil.getDatePattern());
			if (value instanceof Timestamp) {
				df = new SimpleDateFormat(DateUtil.getDateTimePattern());
			}

			try {
				return df.format(value);
			} catch (Exception e) {
				log.error("conver to string exception", e);
				throw new ConversionException("Error converting Date to String");
			}
		} else {
			return value.toString();
		}
	}
}
