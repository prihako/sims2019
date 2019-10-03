package com.balicamp.util;

/**
 * MoneyFormat provides the interface for formatting numbers
 * based on specific Locale.
 * 
 * @author Wayan Ari Agustina
 */
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MoneyFormat {

	private static DecimalFormat formatter = new DecimalFormat();

	public static String getFormattedMoney(String decimal, Locale locale) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
		symbols.setGroupingSeparator('.');
		formatter.setDecimalFormatSymbols(symbols);
		return formatter.format(new Long(decimal));
	}
	

	public static String getFormattedMoney(String decimal) {
		return getFormattedMoney(decimal, Locale.ROOT);
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
		//System.err.println(moneyFormat.getFormattedMoney("",Locale.US));
		
		String str = "640000.0";
		System.out.println("ddd : "+ getFormattedMoney(new Double(1009098)));
		System.out.println("ddd : "+ getFormattedMoney(new Integer(1009098)));
	}
}
