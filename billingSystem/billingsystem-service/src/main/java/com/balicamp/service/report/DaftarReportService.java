package com.balicamp.service.report;

import java.util.List;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.report.ReportModel;
import com.balicamp.report.OldReportHeader;
import com.balicamp.service.IManager;

public interface DaftarReportService extends IManager{
	
	public OldReportHeader handleReportTXT(long idReport,SearchCriteria criteria,boolean isFixedLength);
	
	public OldReportHeader handleReportCSV(long idReport,SearchCriteria criteria,boolean isFixedLength);
	
	public List<ReportModel> findGridData(SearchCriteria kriteria,int first, int max);
	

}
