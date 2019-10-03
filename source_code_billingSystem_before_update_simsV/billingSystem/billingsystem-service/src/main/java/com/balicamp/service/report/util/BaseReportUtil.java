package com.balicamp.service.report.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;

import com.balicamp.Constants;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: BaseReportUtil.java 347 2013-02-22 04:02:58Z bagus.sugitayasa $
 */
public class BaseReportUtil {
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private DateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	// private DateTimeFormatter dateFormat =
	// DateTimeFormat.forPattern("dd/MM/yyyy");
	// private DateTimeFormatter dateTimeFormat =
	// DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

	private DecimalFormat moneyFormat;

	public BaseReportUtil() {
		// init money format
		moneyFormat = new DecimalFormat("###,###");
		moneyFormat.setNegativePrefix("(");
		moneyFormat.setNegativeSuffix(")");
	}

	public String getMessage(String key) {
		Locale locale = LocaleContextHolder.getLocale();
		String value = key;
		try {
			value = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale).getString(key);
		} catch (Exception exception) {
			// ignore
		}
		return value;
	}

	// dateFormat
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String formatDate(Date inputDate) {
		return getDateFormat().format(inputDate);
	}

	// moneyFormat
	public DecimalFormat getMoneyFormat() {
		return moneyFormat;
	}

	public void setMoneyFormat(DecimalFormat moneyFormat) {
		this.moneyFormat = moneyFormat;
	}

	public String formatMoney(BigDecimal money) {
		if (money == null)
			return "";
		return getMoneyFormat().format(money);
	}

	// dateTimeFormat
	public DateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}

	public void setDateTimeFormat(DateFormat dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	public String formatDateTime(Date inputDate) {
		return getDateTimeFormat().format(inputDate);
	}
}
