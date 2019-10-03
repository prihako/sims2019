package com.balicamp.webapp.action.bankadmin.report;

import java.util.Date;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.BasePageList;

/**
 * base class untuk report
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: ReportBasePage.java 368 2013-03-08 04:09:59Z wayan.agustina $
 */
public abstract class ReportBasePage extends BasePageList implements PageBeginRenderListener {

	@InjectObject("engine-service:report")
	public abstract IEngineService getReportService();

	public abstract String getReportUrl();

	public abstract void setReportUrl(String reportUrl);

	@Persist("client")
	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	@Persist("client")
	public abstract Date getEndDate();

	public abstract void setEndDate(Date endDate);

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	/**
	 * listener for ViewReportPdf button
	 * @param cycle
	 * @return
	 */
	public IPage onViewReportPdf(IRequestCycle cycle) {
		serverValidate();
		if (getDelegate().getHasErrors())
			return null;

		initReportUrl("pdf");
		return null;
	}

	/**
	 * listener for ViewReportXls button
	 * @param cycle
	 * @return
	 */
	public IPage onViewReportXls(IRequestCycle cycle) {
		serverValidate();
		if (getDelegate().getHasErrors())
			return null;

		initReportUrl("xls");
		return null;
	}

	public IPage onViewReportCsv(IRequestCycle cycle) {
		serverValidate();
		if (getDelegate().getHasErrors())
			return null;

		initReportUrl("csv");
		return null;
	}

	/**
	 * listener for ViewReportTxt button
	 * @param cycle
	 * @return
	 */
	public IPage onViewReportTxt(IRequestCycle cycle) {
		serverValidate();
		if (getDelegate().getHasErrors())
			return null;

		initReportUrl("txt");
		return null;
	}

	public IPage onViewReportHtml(IRequestCycle cycle) {
		serverValidate();
		if (getDelegate().getHasErrors())
			return null;

		initReportUrl("html");
		return null;
	}

	public IPage onViewReportSknRtgs(IRequestCycle cycle) {
		serverValidate();
		if (getDelegate().getHasErrors())
			return null;

		initReportUrl("sknRtgs");
		return null;
	}

	/**
	 * validate input, overwrite this method if required
	 */
	public void serverValidate() {
		// cek if error
		if (getDelegate().getHasErrors())
			return;
	}

	/**
	 * overwrite this method
	 * @param fileType
	 */
	public abstract void initReportUrl(String fileType);

}
