package com.balicamp.report.exception;

public class RawDataFormatException extends Exception {
	
	private String dataFormat;
	
	public RawDataFormatException(String dataFormat) {
		this.dataFormat = dataFormat;	
	}
	
	@Override
	public String getMessage() {
		return "Ada kesalahan pada data : "+dataFormat ;
	}

}
