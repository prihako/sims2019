package com.balicamp.webapp.action.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.tapestry.IPage;

import com.balicamp.model.report.ReportModel;
import com.balicamp.service.IManager;
import com.balicamp.service.report.DaftarReportService;
import com.balicamp.webapp.action.BaseCustomPageList;
import com.balicamp.webapp.tapestry.component.ActionListenerWrapper;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class AllReport extends BaseCustomPageList {

	@InjectSpring("DaftarReportServiceImpl")
	public abstract IManager getGridManager();
			
	public List<ReportModel> getGridData(){
		return (null == getFilterCriteria().getCriterionList()  && null == getFilterCriteria().getSubSearchCriteriaList() ? new ArrayList<ReportModel>(): ((DaftarReportService)getGridManager()).findGridData(getFilterCriteria(),
				getBaseCustomComponent().getFistPage(),getBaseCustomComponent().getMaxResult()));
	}
	public ActionListenerWrapper getGridActionButtonListener() {
		return new ActionListenerWrapper() {

			@Override
			public void onAction(Object parameter) {				
				Set<Object> set = (Set<Object>)parameter;
				if(null != set && !set.isEmpty()){
					List<Serializable> ids = new ArrayList<Serializable>();
					
					for (Object models : set) {
						ids.add(((ReportModel)models).getId());
					}
					
					IPage downloadPage = getRequestCycle().getPage("downloadReport");
					((DownloadReport)downloadPage).initPage(ids);
					getRequestCycle().activate(downloadPage);
				}
				
			}
		};

	}
}
