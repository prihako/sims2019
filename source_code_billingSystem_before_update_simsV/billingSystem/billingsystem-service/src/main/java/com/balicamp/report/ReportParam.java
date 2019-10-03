package com.balicamp.report;

import java.util.Date;

/**
 * Class DTO untuk membungkus parameter untuk keperluan mengenerate report dari page ke service
 * 
 * @author Nyoman Budi Parwata
 *
 */
public class ReportParam {

	private Long reportId;
	
	private String userId;
	
	private Date tglAwal;
	private Date tglAkhir;

	private boolean txt;
	private boolean csv;	
	
	private boolean generated;
	
	public ReportParam(Long reportId,Date tglAwal,Date tglAkhir, boolean isTxt, boolean isCSV) {
		this.reportId = reportId;
		this.tglAkhir = tglAkhir;
		this.tglAwal = tglAwal;
		this.csv = isCSV;
		this.txt = isTxt;
	}
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	public Date getTglAwal() {
		return tglAwal;
	}
	public void setTglAwal(Date tglAwal) {
		this.tglAwal = tglAwal;
	}
	public Date getTglAkhir() {
		return tglAkhir;
	}
	public void setTglAkhir(Date tglAkhir) {
		this.tglAkhir = tglAkhir;
	}
	public boolean isTxt() {
		return txt;
	}
	public void setTxt(boolean txt) {
		this.txt = txt;
	}
	public boolean isCsv() {
		return csv;
	}
	public void setCsv(boolean csv) {
		this.csv = csv;
	}
	
	public void setGenerated(boolean generated) {
		this.generated = generated;
	}

	public boolean isGenerated() {
		return generated;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
