package com.balicamp.soap;

import java.text.SimpleDateFormat;

public class WebServiceConstant {
	public static final SimpleDateFormat TRANSMISSION_DATE_TIME_FORMAT = new SimpleDateFormat("MMddHHmmss");
	public static final SimpleDateFormat TRANSACTION_DATE_TIME_FORMAT = new SimpleDateFormat("MMddHHmmss");
	public static final String LANGUAGE = "01";
	public static final String CHANNEL_ID = "99";
	public static final String COMPANY_CODE = "99";
	public static final String PAID_BILS = "00";
	public static final String CURRENCY = "360";
	
	public static final String REOR_INQ = "REOR.INQ";
	public static final String REOR_PAY = "REOR.PAY";
	
	public static final String IAR_INQ = "IAR.INQ";
	public static final String IAR_PAY = "IAR.PAY";
	
	public static final String IKRAP_INQ = "IKRAP.INQ";
	public static final String IKRAP_PAY = "IKRAP.PAY";
	
	public static final String KLBSI_INQ = "KLBSI.INQ";
	public static final String KLBSI_PAY = "KLBSI.PAY";
	
	
	public static final String REOR_TRANSACTION_TYPE = "20";
	public static final String IAR_TRANSACTION_TYPE = "50";
	public static final String IKRAP_TRANSACTION_TYPE = "60";
	public static final String KLBSI_TRANSACTION_TYPE = "90";
}
