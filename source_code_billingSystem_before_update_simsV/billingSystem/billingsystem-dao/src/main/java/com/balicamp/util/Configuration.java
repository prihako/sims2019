package com.balicamp.util;

import java.text.MessageFormat;
import java.util.Properties;

public class Configuration {
	public final static String PAREMETER_TABLE_GROUP = "applicationConfiguration";
	//key
	public final static String KEY_PAGING_SIZE			= "paging.size";
	public final static String KEY_DIR_REAL_CONTEXT_PATH	= "dir.realContextPath";
	public final static String KEY_SERVLET_CONTEXT_PATH		= "servlet.contextPath";
	
	public final static String KEY_PRINT_PAGE_WIDTH			= "print.page.width";
	public final static String KEY_PRINT_PAGE_HEIGHT		= "print.page.height";
	public final static String KEY_PRINT_PAGE_MARGIN_TOP	= "print.page.margin.top";
	public final static String KEY_PRINT_PAGE_MARGIN_BUTTOM	= "print.page.margin.buttom";
	public final static String KEY_PRINT_PAGE_MARGIN_LEFT	= "print.page.margin.left";
	public final static String KEY_PRINT_PAGE_MARGIN_RIGHT	= "print.page.margin.right";
	
	//complaint status
	public final static int COMPLAINT_STATUS_DONE = 2;

	private static Properties contextParamProperties;
	private static Properties parametersTableProperties;
	private static Properties[] propertiesList;
	
	//singeleton
	private static Configuration instance;
	static {
		instance = new Configuration();
	}
	public static Configuration getInstance(){
		return instance;
	}
	
	//private constructor
	private Configuration(){
		propertiesList = new Properties[2];
	}
	
	//properties setter
	public void setContextParamProperties(Properties contextParamProperties){
		Configuration.contextParamProperties = contextParamProperties;
		propertiesList[0] = Configuration.contextParamProperties;
	}
	public void setParametersTableProperties(Properties parametersTableProperties) {
		Configuration.parametersTableProperties = parametersTableProperties;
		propertiesList[1] = Configuration.parametersTableProperties;
	}

	//logic method
	public static String getConfig(String key){
		String result = null;
		for (Properties properties : propertiesList) {
			if( properties != null ){
				result = properties.getProperty(key);
				if ( result != null ){
					break;
				}
			}
		}
		return result;
	}
	
	public static String getConfig(String key,String def){
		String result = def;
		for (Properties properties : propertiesList) {
			if( properties != null ){
				result =  properties.getProperty(key, def);
				if ( def != null && result != null && !result.equals(def) ){
					break;
				}
			}
		}
	    return result;
	}
	
	public static int getIntConfig(String key, int def){
		int retval = def;
		String valueString = getConfig(key, String.valueOf(def));
		try {
			retval = Integer.parseInt(valueString);
		} catch ( Exception e ){
			retval = def;			
		}
		return retval;
	}

	public static long getLongConfig(String key, long def){
		long retval = def;
		String valueString = getConfig(key, String.valueOf(def));
		try {
			retval = Long.parseLong(valueString);
		} catch ( Exception e ){
			retval = def;			
		}
		return retval;
	}

	public static double getDoubleConfig(String key, double def){
		double retval = def;
		String valueString = getConfig(key, String.valueOf(def));
		try {
			retval = Double.parseDouble(valueString);
		} catch ( Exception e ){
			retval = def;			
		}
		return retval;
	}

	public static boolean getBooleanConfig(String key, boolean def){
		String valueString = getConfig(key, String.valueOf(def));
        return valueString.equalsIgnoreCase("true");
    }

	
 	public static String getConfig(String key, String def, String... argList){
 		String value = getConfig(key, def);
 		if ( value == null){
 			return value;
 		}
		MessageFormat messageFormat = new MessageFormat("");
		messageFormat.applyPattern(value);
		
		return messageFormat.format(argList);
	}
 	

}












