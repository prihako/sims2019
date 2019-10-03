package com.balicamp.webapp.action.report;

import org.apache.tapestry.IPage;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.RequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.report.OldReportHeader;
import com.balicamp.report.ReportHeader;
import com.balicamp.service.report.DaftarReportService;
import com.balicamp.webapp.action.BasePage;
import com.balicamp.webapp.action.download.DownloadPage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class TestAllReport extends BasePage implements PageBeginRenderListener, PageEndRenderListener{
	
	
	@InjectSpring("DaftarReportServiceImpl")
	public abstract DaftarReportService getReportService();
	
		
	public void generateCSV(RequestCycle cycle){		
		
		IPage downloadPage = getRequestCycle().getPage("downloadPage");
		((DownloadPage)downloadPage).setFileName("axx.txt");
		((DownloadPage)downloadPage).setSuccessPage("allReport");
		((DownloadPage)downloadPage).setErrorPage("viewConnectionList");
		((DownloadPage)downloadPage).initPage();
		cycle.activate(downloadPage);
		
		
		
/*		System.out.println("##################");
		SearchCriteria sk = new SearchCriteria();
		
		sk.getParamMapStr().put("Label 1", "Value 1");
		sk.getParamMapStr().put("Label 2", "Value 2");
		sk.getParamMapStr().put("Label 3", "Value 3");
		sk.getParamMapStr().put("Label 4", "Value 4");
		
		sk.setEntityName("TransactionLogs");
		sk.addCriterion(Restrictions.eq("id.channelCode", "aj01"));
		
		ReportHeader header = getReportService().handleReportCSV(1,sk,false);
		
		if(header.isGenerate()){			
			IPage downloadPage = cycle.getPage("downloadReport");
			((DownloadReport)downloadPage).initPage(header);
			cycle.activate(downloadPage);
		}*/
	}
	
	public void generateTXT(RequestCycle cycle){
		System.out.println("##################");
		SearchCriteria sk = new SearchCriteria();
		
		sk.getParamMapStr().put("Label 1", "Value 1");
		sk.getParamMapStr().put("Label 2", "Value 2");
		sk.getParamMapStr().put("Label 3", "Value 3");
		sk.getParamMapStr().put("Label 4", "Value 4");
		
		sk.setEntityName("TransactionLogs");
		sk.addCriterion(Restrictions.eq("id.channelCode", "aj01"));		
		OldReportHeader header = getReportService().handleReportTXT(1,sk,false);
		
		if(header.isGenerate()){			
			IPage downloadPage = cycle.getPage("downloadReport");
			//((DownloadReport)downloadPage).initPage(header);
			cycle.activate(downloadPage);
		}
	}
	

}
