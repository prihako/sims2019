package com.balicamp.util;

/**
 * MoneyFormat provides the interface for formatting numbers
 * based on specific Locale.
 * 
 * @author Wayan Ari Agustina
 */
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MoneyFormat {

	private static DecimalFormat formatter = new DecimalFormat();

	public static String getFormattedMoney(String decimal, Locale locale) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');
		formatter.setDecimalFormatSymbols(symbols);
		return formatter.format(new BigDecimal(decimal));
	}

	public static String getFormattedMoney(String decimal) {
		return getFormattedMoney(decimal, Locale.ROOT);
	}

	public static String getFormattedMoney(BigDecimal decimal) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');

		DecimalFormat decimalFormat = new DecimalFormat("###,###", symbols);

		// return formatter.format((decimal));
		return decimalFormat.format(decimal);
	}

	public static String getFormattedMoney(Double decimal) {
		Long lgDecimal = new Long(decimal.intValue());
		return getFormattedMoney(lgDecimal.toString(), Locale.ROOT);
	}

	public static String getFormattedMoney(Integer decimal) {
		return getFormattedMoney(decimal.toString(), Locale.ROOT);
	}

	public static void main(String[] args) {
		MoneyFormat moneyFormat = new MoneyFormat();
		// System.err.println(moneyFormat.getFormattedMoney("",Locale.US));
		String str = "64000000000";
		System.out.println(new BigDecimal(str));
		System.out.println("ddd : " + getFormattedMoney(new BigDecimal(str)));

		String tes = "1002023";
		System.out.println(tes.substring(0, tes.length() - 2) + "." + tes.substring(tes.length() - 2));
	}
}
