package com.balicamp.report;

import java.util.List;

public class ReportContent {
	
	private StringBuffer sparatedDetail;
	private List<String> summary;
	private List<String> rowCount;
	private StringBuffer columName;
	private String line;
	private int jumlahData;
	
	private boolean header;
	
	public StringBuffer getSparatedDetail() {
		return sparatedDetail;
	}
	public void setSparatedDetail(StringBuffer sparatedDetail) {
		this.sparatedDetail = sparatedDetail;
	}
	
	public List<String> getSummary() {
		return summary;
	}
	
	public void setSummary(List<String> summary) {
		this.summary = summary;
	}
	public StringBuffer getColumName() {
		return columName;
	}
	public void setColumName(StringBuffer columName) {
		this.columName = columName;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public List<String> getRowCount() {
		return rowCount;
	}
	public void setRowCount(List<String> rowCount) {
		this.rowCount = rowCount;
	}
	
	public void setJumlahData(int jumlahData) {
		this.jumlahData = jumlahData;
	}
	public int getJumlahData() {
		return jumlahData;
	}
	public boolean isHeader() {
		return header;
	}
	public void setHeader(boolean header) {
		this.header = header;
	}
	
}
