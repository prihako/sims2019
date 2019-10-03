package com.balicamp.report;

import java.util.Date;



/**
 * Class DTO untuk membungkus parameter untuk keperluan mengenerate header report
 * 
 * @author Nyoman Budi Parwata
 *
 */
public class ReportParamHeader extends ReportParam{
	
	
	private int reportLength;
	private String line;
	private String judul;
	private Date tglCetak;
	private String bank;
	

	public ReportParamHeader(ReportParam param) {
		super(param.getReportId(), param.getTglAwal(), param.getTglAkhir(), param.isTxt(), param.isCsv());
	}


	public int getReportLength() {
		return reportLength;
	}


	public void setReportLength(int reportLength) {
		this.reportLength = reportLength;
	}


	public String getLine() {
		return line;
	}


	public void setLine(String line) {
		this.line = line;
	}


	public String getJudul() {
		return judul;
	}


	public void setJudul(String judul) {
		this.judul = judul;
	}


	public Date getTglCetak() {
		return tglCetak;
	}


	public void setTglCetak(Date tglCetak) {
		this.tglCetak = tglCetak;
	}


	public String getBank() {
		return bank;
	}


	public void setBank(String bank) {
		this.bank = bank;
	}
	
	
	
	


}
