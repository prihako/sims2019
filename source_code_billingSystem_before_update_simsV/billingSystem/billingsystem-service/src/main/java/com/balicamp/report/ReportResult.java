package com.balicamp.report;

/**
 * Class DTO untuk membungkus hasil generate report 
 * 
 * @author Nyoman Budi Parwata
 *
 */
public class ReportResult {
	
	private String fileName;
	private boolean generate;
	private String errorMessage;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isGenerate() {
		return generate;
	}
	public void setGenerate(boolean generate) {
		this.generate = generate;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


}
