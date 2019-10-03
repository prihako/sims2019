package com.balicamp.util.wrapper;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public class ReportCriteriaWraper {

	public static final int EQUALS = 0;
	public static final int NOT_EQUALS = 1;
	public static final int FRONT_LIKE = 3;
	public static final int END_LIKE = 4;
	public static final int MIDDLE_LIKE = 5;
	public static final int BETWEEN = 6;
	public static final int IN = 8;
	public static final int GREATER_EQUALS = 9;
	public static final int LESS_EQUALS = 10;
	public static final int IS = 11;
	public static final int NULL = -1;

	private static final int NUMBER_FAMILY = 0;
	private static final int STRING_FAMILY = 1;
	private static final int DATE_FAMILY = 2;

	public static final String OR_MULTIPLE_VALUE_SEPARATOR = "[|]";
	public static final String AND_MULTIPLE_VALUE_SEPARATOR = "[&]";
	public static final String COMMA_MULTIPLE_VALUE_SEPARATOR = "[,]";

	public static final String getRestrictionsHQL(List<ReportCriteria> criteria,
			Class<?> entityClass) {
		return getRestrictionsHQL(null, criteria, entityClass);
	}

	public static final String getRestrictionsHQL(List<String> fieldsToSelect, List<ReportCriteria> criteria,
			Class<?> entityClass) {
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT ");
		if(fieldsToSelect != null && fieldsToSelect.size() > 0) {
			for(String field : fieldsToSelect)
				hql.append("a."+field+", ");
			String tmp = hql.toString().trim();
			hql = new StringBuffer();
			hql.append(tmp.substring(0, tmp.length()-1));
		} else {
			hql.append("a ");
		}

		hql.append(" FROM "+entityClass.getSimpleName()+" a WHERE 1=1");
		for(ReportCriteria rc : criteria) {
			if(rc.getValue()!=null)
			{
				hql.append(" AND ");
				hql.append(prepareParameters(rc, entityClass));
			}
		}
		return hql.toString();
	}

	private static String addEscape(String paramValue) {
		return paramValue.replaceAll("'", "\\'");
		//return paramValue;
	}

	private static int determineFieldType(Field field) {
		if(field.getType().getSuperclass().equals(Number.class) ||
				field.getType().equals(Number.class)) {
			return NUMBER_FAMILY;
		} else if(field.getType().getSuperclass().equals(Date.class)
				|| field.getType().equals(Date.class)) {
			return DATE_FAMILY;
		}
		return STRING_FAMILY;
	}

	private static int determineDataType(Class<?> entity, String fieldName) {
		int type = NUMBER_FAMILY;
		try {
			Class<?> nestedEntity = entity;
			String[] fields = fieldName.replaceAll("\"", "").split("[.]");
			if(fields.length > 1) {
				for(int index = 0; index < (fields.length -1); index++) {
					nestedEntity = entity.getDeclaredField(fields[index]).getType();
				}
			}
			Field field = nestedEntity.getDeclaredField(fields[fields.length-1]);
			type = determineFieldType(field);
		} catch (Exception e) {
			determineDataType(entity.getSuperclass(), fieldName);
		}
		return type;
	}

	private static String getMultipleValueSeparator(String value) {
		if(value.contains("&")) return "AND";
		if(value.contains("|")) return "OR";
		if(value.contains(",")) return ",";
		return "";
	}

	private static String getMultipleValueOperator(String separator) {
		if(separator.equals("AND")) return "&";
		if(separator.equals("OR")) return "|";
		if(separator.equals(",")) return ",";
		return "";
	}

	private static String checkSqlKeywordsMultiple(ReportCriteria criteria, String value) {
		String firstWord = value.trim().replaceFirst("\\s+.*", "");
		int rest = criteria.getRestriction();
		if(rest == IN || rest == IS || rest == BETWEEN) {
			return (value.trim().equals("NULL") ?
					value : "'"+value+"'");
		}
		if(firstWord.trim().matches("IN.*|IS.*|BETWEEN.*|NOT.*")) {
			return (value.trim().matches("IS.*|NULL") ?
					value : "'"+value+"'");
		}
		return "a."+criteria.getKey()+" = "+(value.trim().matches("NULL.*|IS.*") ?
				value : "'"+value+"'");
	}

	private static String checkSqlKeywordsSingle(ReportCriteria criteria, String value) {
		return criteria.getValue().trim().matches("NULL|IN.*|IS.*|BETWEEN.*|NOT.*") ?
				criteria.getRestriction() == -1 ? " a."+criteria.getKey()+" "+value+" " : value
					: criteria.getRestriction() == -1 ? " a."+criteria.getKey()+" = '"+value+"' " : " '"+value+"' ";
	}

	private static String getRestrictionValue(ReportCriteria criteria, int type,
			String left, String right, boolean multiValue) {

		int rstConstant = criteria.getRestriction();
		String value = criteria.getValue();

		String statement = "";

		switch (type) {
			case STRING_FAMILY:
				if(multiValue) {
					String valueSeparator = getMultipleValueSeparator(value);
					String[] params = value.split("["+getMultipleValueOperator(valueSeparator)+"]");
					for(String p : params) {
						statement +=  checkSqlKeywordsMultiple(criteria, addEscape(p))+" "+valueSeparator+" ";
					}
					statement = statement.substring(0, statement.length()-(valueSeparator.length()+2));
					statement = rstConstant != BETWEEN ? "("+statement+")" : statement;
					break;
				}
				statement = checkSqlKeywordsSingle(criteria, addEscape(value));
				break;
			case NUMBER_FAMILY:
				if(multiValue) {
					String valueSeparator = getMultipleValueSeparator(value);
					String[] params = value.split("["+getMultipleValueOperator(valueSeparator)+"]");
					for(String p : params) {
						statement += " "+p+" "+valueSeparator+" ";
					}
					statement = statement.substring(0, statement.length()-(valueSeparator.length()+2));
					statement = rstConstant != BETWEEN ? "("+statement+")" : statement;
					break;
				}
				statement = left+" "+addEscape(value)+" "+right;
				break;
			case DATE_FAMILY:
				if(multiValue) {
					String valueSeparator = getMultipleValueSeparator(value);
					String[] params = value.split("["+getMultipleValueOperator(valueSeparator)+"]");
					for(String p : params) {
						statement += " date('"+addEscape(p)+"') "+valueSeparator+" ";
					}
					statement = statement.substring(0, statement.length()-(valueSeparator.length()+2));
					statement = rstConstant != BETWEEN ? "("+statement+")" : statement;
					break;
				}
				statement = left+" date('"+addEscape(value)+"') "+right;
				break;
		}

		return statement;
	}

	private static String getCorrectKey(ReportCriteria criteria, int type) {
		if(criteria.getRestriction() == NULL) return "";
		if(type == DATE_FAMILY)
			return "date(a."+criteria.getKey()+")";
		return "a."+criteria.getKey();
	}

	private static boolean isMultiValueStatement(String value) {
		return !getMultipleValueSeparator(value).equals("");
	}

	private static String prepareParameters(ReportCriteria criteria, Class<?> entityType) {
		String value = criteria.getValue();

		String statement = "";
		int type = determineDataType(entityType, criteria.getKey());
		String hqlKey = getCorrectKey(criteria, type);
		switch (criteria.getRestriction()) {
			case EQUALS:
				statement = " = "+(getRestrictionValue(criteria, type, "",  "", isMultiValueStatement(value)));
				break;
			case NOT_EQUALS:
				statement = " <> "+(getRestrictionValue(criteria, type, "",  "", isMultiValueStatement(value)));
				break;
			case BETWEEN:
				statement = " BETWEEN "+(getRestrictionValue(criteria, type, "",  "", isMultiValueStatement(value)));
				break;
			case FRONT_LIKE:
				statement = " LIKE "+(getRestrictionValue(criteria, type, "",  "%", isMultiValueStatement(value)));
				break;
			case MIDDLE_LIKE:
				statement = " LIKE "+(getRestrictionValue(criteria, type, "%",  "%", isMultiValueStatement(value)));
				break;
			case END_LIKE:
				statement = " LIKE "+(getRestrictionValue(criteria, type, "%",  "", isMultiValueStatement(value)));
				break;
			case IN:
				statement = " IN "+(getRestrictionValue(criteria, type, "",  "", isMultiValueStatement(value)));
				break;
			case GREATER_EQUALS:
				statement = " >= "+(getRestrictionValue(criteria, type, "",  "", isMultiValueStatement(value)));
				break;
			case LESS_EQUALS:
				statement = " <= "+(getRestrictionValue(criteria, type, "",  "", isMultiValueStatement(value)));
				break;
			case IS:
				statement = " IS "+(getRestrictionValue(criteria, type, "",  "", isMultiValueStatement(value)));
				break;
			case NULL:
				statement = (getRestrictionValue(criteria, type, "",  "", isMultiValueStatement(value)));
				break;
		}
		return hqlKey+statement;
	}

}
