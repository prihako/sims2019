package com.balicamp.service.report;

import java.util.List;

import com.balicamp.report.ReportParam;
import com.balicamp.report.ReportResult;
import com.balicamp.service.IManager;

public interface IReportService extends IManager{
	
	public ReportResult generateReport(List<ReportParam> reportParam); 	

}
