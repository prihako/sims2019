package com.balicamp.report.param;

import java.util.List;
import java.util.Map;

import com.balicamp.report.exception.RawColumnFormatException;

	public abstract class ColumnParam {

	private String fieldName;
	private String fieldString;
	private String tipeData;
	private boolean isSum;	
	private int length;
	private String margin;
	private String dataKeyFormater;
	
	private boolean isRowCount;
	private Map<String, String> conditional;

	
	public static final String  RAW_TIPE_NUMERIC = "NUMERIC";
	public static final String  RAW_TIPE_TEXT = "TEXT";
	public static final String  RAW_TIPE_DATE ="DATE";
	
	public static final String  RAW_MARGIN_LEFT = "LEFT";
	public static final String  RAW_MARGIN_RIGHT ="RIGHT";
	public static final String  RAW_MARGIN_CENTER ="CENTER";
	
	public static final String  RAW_SUMMARY_YES ="SUM_Y";
	public static final String  RAW_SUMMARY_NO ="SUM_N";
	
	public static final String  RAW_COUNT_YES ="ROW_COUNT_Y";
	public static final String  RAW_COUNT_NO ="ROW_COUNT_N";
	
	
	/**
	 * fungsi untuk memformat raw string atau pojo sehingga menghasilkan list of ColumParam 
	 * @return
	 */
	public abstract List<ColumnParam> format() throws RawColumnFormatException ;
	
	
	public String getFieldName() {
		return fieldName;
	}
	public String getFieldString() {
		return fieldString;
	}
	public String getTipeData() {
		return tipeData;
	}
	public boolean isSum() {
		return isSum;
	}
	public int getLength() {
		return length;
	}
	public String getMargin() {
		return margin;
	}
	
	public boolean isRowCount() {
		return isRowCount;
	}
	public Map<String, String> getConditional() {
		return conditional;
	}
	protected void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	protected void setFieldString(String fieldString) {
		this.fieldString = fieldString;
	}
	protected void setTipeData(String tipeData) {
		this.tipeData = tipeData;
	}
	protected void setSum(boolean isSum) {
		this.isSum = isSum;
	}
	protected void setLength(int length) {
		this.length = length;
	}
	protected void setMargin(String margin) {
		this.margin = margin;
	}
	protected void setRowCount(boolean isRowCount) {
		this.isRowCount = isRowCount;
	}
	protected void setConditional(Map<String, String> conditional) {
		this.conditional = conditional;
	}
	public String getKeyDataFormater() {
		return dataKeyFormater;
	}
	public void setKeyDataFormater(String dataKeyFormater) {
		this.dataKeyFormater = dataKeyFormater;
	}
	
	
}
