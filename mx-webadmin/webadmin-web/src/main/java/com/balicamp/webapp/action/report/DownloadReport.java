package com.balicamp.webapp.action.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.engine.RequestCycle;

import com.balicamp.model.report.ReportModel;
import com.balicamp.report.ReportParam;
import com.balicamp.report.ReportResult;
import com.balicamp.service.report.DaftarReportService;
import com.balicamp.service.report.IReportService;
import com.balicamp.webapp.action.BasePage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class DownloadReport extends BasePage {

	private List<ReportModel> reports;

	public List<ReportModel> getReports() {
		return reports;
	}

	public void setReports(List<ReportModel> reports) {
		this.reports = reports;
	}

	public abstract ReportModel getReport();

	public abstract void setReport(ReportModel report);

	public abstract Date getTglAwal();

	public abstract void setTglAwal(Date tglAwal);

	public abstract Date getTglAkhir();

	public abstract void setTglAkhir(Date tglAkhir);

	public abstract boolean isTxt();

	public abstract void setTxt(boolean txt);

	public abstract boolean isCsv();

	public abstract void setCsv(boolean csv);

	public abstract void setValidDate(String valid);

	public abstract String getValidDate();

	@InjectSpring("GenericReportService")
	public abstract IReportService getReportProcesClass();

	@InjectSpring("DaftarReportServiceImpl")
	public abstract DaftarReportService getReportService();

	public void initPage(List<Serializable> reportIds) {
		List<ReportModel> tmpReport = new ArrayList<ReportModel>();
		if (null != reportIds && !reportIds.isEmpty()) {
			tmpReport = (List<ReportModel>) getReportService().findById(reportIds);
		}
		setReports(tmpReport);
	}

	public void download(RequestCycle cycle) {
		setValidDate("TRUE");
		/*
		 * int validDate = getTglAwal().compareTo(getTglAkhir());
		 * if (validDate > 0) {
		 * addError(getDelegate(), (IFormComponent)
		 * cycle.getPage().getComponent("tglAwal"),
		 * getText("downloadReport.invalidDate"),
		 * ValidationConstraint.REQUIRED);
		 * return;
		 * }
		 */
		List<ReportParam> param = new ArrayList<ReportParam>();
		String userId = getUserLoginFromSession().getUserFullName();
		for (ReportModel tmpReport : getReports()) {
			ReportParam rpt = new ReportParam(tmpReport.getId(), getTglAwal(), getTglAkhir(), isTxt(), isCsv());
			rpt.setUserId(userId);
			param.add(rpt);
		}

		// TODO: lempar ke service dan service membalikkan nama file hasil
		// generatean yang sudah di zip
		ReportResult result = getReportProcesClass().generateReport(param);
		if (result.isGenerate()) {
			download(result.getFileName());
		}

	}
	/*
	 * public String getUrlDownload() {
	 * return constructUrl();
	 * }
	 */
	/*
	 * private String constructUrl(){
	 * StringBuffer bfr = new StringBuffer();
	 * if(null != getReportHeader().getFileName()){
	 * //generate string <a href=context/report/fileName.txt>download</a>
	 * bfr.append("<a href='");
	 * String context = getRequest().getContextPath();
	 * bfr.append(context);
	 * bfr.append("/download?fileName="+getReportHeader().getFileName());
	 * bfr.append("'>download</a>");
	 * 
	 * }else{
	 * bfr.append(
	 * "Tidak ditemukan report yang akan di download, silahkan hubungi admin");
	 * }
	 * return bfr.toString();
	 * }
	 */
}
