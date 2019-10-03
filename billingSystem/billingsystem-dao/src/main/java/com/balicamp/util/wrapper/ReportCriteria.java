package com.balicamp.util.wrapper;

import java.util.HashMap;
import java.util.Map;

public class ReportCriteria {
	
	public static final String EQUALS = "=";
	public static final String NOT_EQUALS = "<>";
	public static final String FRONT_LIKE = "X%";
	public static final String END_LIKE = "%X";
	public static final String MIDDLE_LIKE = "%X%";
	public static final String BETWEEN = "BETWEEN";
	public static final String IN = "IN";
	public static final String GREATER_EQUALS = ">=";
	public static final String LESS_EQUALS = "<=";
	public static final String IS = "IS";
	public static final String NULL = "NULL";
	
	
	private int restriction;
	private String key;
	private String paramName;
	private String value;
	
	private Map<String, Integer> RESTRICTION_MAP;
	
	{
		RESTRICTION_MAP = new HashMap<String, Integer>();
		
		RESTRICTION_MAP.put(EQUALS, ReportCriteriaWraper.EQUALS);
		RESTRICTION_MAP.put(NOT_EQUALS, ReportCriteriaWraper.NOT_EQUALS);		
		RESTRICTION_MAP.put(BETWEEN, ReportCriteriaWraper.BETWEEN);
		RESTRICTION_MAP.put(IN, ReportCriteriaWraper.IN );
		RESTRICTION_MAP.put(GREATER_EQUALS, ReportCriteriaWraper.GREATER_EQUALS);
		RESTRICTION_MAP.put(LESS_EQUALS, ReportCriteriaWraper.LESS_EQUALS);
		
		
		RESTRICTION_MAP.put(FRONT_LIKE, ReportCriteriaWraper.FRONT_LIKE);
		RESTRICTION_MAP.put(END_LIKE, ReportCriteriaWraper.END_LIKE);
		RESTRICTION_MAP.put(MIDDLE_LIKE, ReportCriteriaWraper.MIDDLE_LIKE);
		RESTRICTION_MAP.put("IS", ReportCriteriaWraper.IS);
		RESTRICTION_MAP.put("NULL", ReportCriteriaWraper.NULL);
	}
	
	public ReportCriteria(String strRestriction, String key, String value) {
		Integer rest = RESTRICTION_MAP.get(strRestriction);
		if(null == rest){
			throw new RuntimeException(strRestriction +" --> Restriction belum di support ");
		}else{
			this.restriction = rest;
		}
		
		this.key = key;
		this.value = value;
	}


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}


	public int getRestriction() {
		return restriction;
	}


	public void setRestriction(int restriction) {
		this.restriction = restriction;
	}
	
 
}
