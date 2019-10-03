package com.balicamp.report;

import java.util.Map;

public class OldReportHeader {
	
	public static final String REPORT_TYPE_TXT = "TXT";	
	public static final String REPORT_TYPE_CSV = "CSV";
	
	private String reportTipe;
	private String reportName;
	private String fileName;
	private String kodeReport;
	
	private Map<String, String> paramStrMap;
	private boolean generate;
	
	
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Map<String, String> getParamStrMap() {
		return paramStrMap;
	}
	public void setParamStrMap(Map<String, String> paramStrMap) {
		this.paramStrMap = paramStrMap;
	}
	public String getReportTipe() {
		return reportTipe;
	}
	public void setReportTipe(String reportTipe) {
		this.reportTipe = reportTipe;
	}
	public boolean isGenerate() {
		return generate;
	}
	public void setGenerate(boolean generate) {
		this.generate = generate;
	}
	public String getKodeReport() {
		return kodeReport;
	}
	public void setKodeReport(String kodeReport) {
		this.kodeReport = kodeReport;
	}

}
