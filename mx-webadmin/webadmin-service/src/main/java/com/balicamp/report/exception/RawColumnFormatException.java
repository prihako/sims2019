package com.balicamp.report.exception;

public class RawColumnFormatException extends Exception {
	
	private String rawFormat;
	
	public RawColumnFormatException(String rawFormat) {
		this.rawFormat = rawFormat;	
	}
	
	@Override
	public String getMessage() {
		return "Kesalahan pada parameter Format Colum: "+rawFormat ;
	}

}
