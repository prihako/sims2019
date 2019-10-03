package com.balicamp.webapp.action.report;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import test.Constants;

import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.ReconcileReportMonthly;
import com.balicamp.model.user.Role;
import com.balicamp.service.ReconcileMonthlyReportManager;
import com.balicamp.service.impl.mx.EndpointsManagerImpl;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.ftp.FTPManager;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class ReconcileMonthly extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {

	@InjectSpring("reconcileMonthlyReportManagerImpl")
	public abstract ReconcileMonthlyReportManager getReconcileMonthlyReportManager();

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManagerImpl getEndpointsManager();

	@InjectSpring("systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	private XlstoStringConverter xlsFile;

	public ReconcileMonthly() {
		super();
		this.xlsFile = new XlstoStringConverter();
	}

	protected final Log log = LogFactory.getLog(ReconcileMonthly.class);

	public abstract String getBankName();

	public abstract void setBankName(String bankName);

	public abstract String getTransactionType();

	public abstract void setTransactionType(String transactionType);

	public abstract String getMonth();

	public abstract void setMonth(String month);

	public abstract String getReconcileStatus();

	public abstract void setReconcileStatus(String reconcileStatus);

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract ReconcileReportMonthly getReconcileReportMonthly();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				} else {
					getFields().remove("RECONCILE_REPORT_MONTHLY");
				}
			}
		}
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	public List getReconcileMonthlyReportList() {
		List<ReconcileReportMonthly> list = (List<ReconcileReportMonthly>) getFields()
				.get("RECONCILE_REPORT_MONTHLY");

		return list;
	}

	public IPropertySelectionModel getTransactionTypeModel() {
		HashMap<String, String> map = new HashMap<String, String>();

		List<Endpoints> endpointList = getEndpointsManager()
				.getAllEndpointsByType("biller");
		for (Endpoints tempEndpoint : endpointList) {
			if (!tempEndpoint.getCode().equalsIgnoreCase("chws")) {
				map.put(tempEndpoint.getCode(), tempEndpoint.getName());
			}
		}
		// }

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public IPropertySelectionModel getTransactionTypeModelBiller() {
		Map<String, String> map = new HashMap<String, String>();
		List<Endpoints> endpoints = new ArrayList<Endpoints>();

		// Check if session is null
		if (getUserLoginFromSession() == null) {
			try {
				getResponse().sendRedirect(getBaseUrl() + "/main.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Set<Role> roles = getUserLoginFromSession().getRoleSet();
		for (Role tempRole : roles) {
			if (getEndpointsManager().findEndpointsByCode(
					tempRole.getName().toLowerCase()) != null) {
				endpoints.add(getEndpointsManager().findEndpointsByCode(
						tempRole.getName().toLowerCase()));
			}
		}

		if (endpoints.size() > 0) {
			for (Endpoints tempEnd : endpoints) {
				map.put(tempEnd.getCode(), tempEnd.getName());
			}
		} else {
			// map.put("All", "All");
			List<Endpoints> endpointList = getEndpointsManager()
					.getAllEndpointsByType("biller");
			// List<Endpoints> endpointList =
			// getEndpointsManager().getAllEndpointsByState("ready");
			for (Endpoints tempEndpoint : endpointList) {
				if (!tempEndpoint.getCode().equalsIgnoreCase("chws")
						&& !tempEndpoint.getCode().equalsIgnoreCase("chws2")) {
					map.put(tempEndpoint.getCode(), tempEndpoint.getName());
				}
			}

		}

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public void doSearch(IRequestCycle cycle) {
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MMM-yyyy");

		String errorMessage = validation();

		if (errorMessage != null) {
			addError(getDelegate(), "errorShadow", errorMessage,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		try {
			cal.setTime(df.parse(getMonth()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDate = cal.getTime();

		String reportType = getReconcileStatus();

		List<ReconcileReportMonthly> list = getReconcileMonthlyReportManager().
				findReconcileReportByDate(getBankName(), getTransactionType(), startDate, endDate, reportType);

		// for add serial number, so it can display in table html
		int i = 0;
		for (ReconcileReportMonthly trx : list) {
			++i;
			trx.setSerialNumber(i);
		}

		if (list == null || list.size() < 1) {
			addError(
					getDelegate(),
					"errorShadow",
					getText("leftmenu.reconcileMonthly.errorMessage.emptySearchResult"),
					ValidationConstraint.CONSISTENCY);
		}

		getFields().put("RECONCILE_REPORT_MONTHLY", list);
		getFields().put("SEARCH_RECONCILE_MONTHLY_REPORT",
				"SEARCH_RECONCILE_MONTHLY_REPORT");

	}

	public IPropertySelectionModel getReconcileStatusModel() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("All", "All");
		map.put("Settled", "Settled");
		map.put("Unsettled/Need Confirmation", "Unsettled/Need Confirmation");

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public String validation() {
		String errorMessage = null;

		if (getMonth() == null) {
			errorMessage = getText("leftMenu.reconcileMonthly.month.null");
		}

		return errorMessage;
	}

	public void retrieveFilePdf(IRequestCycle cycle,
			ReconcileReportMonthly reconcileMonthlyReport, String type) {
		String reportType = type
				.equalsIgnoreCase("Unsettled/Need Confirmation") ? "unsettled"
				: type.toLowerCase();

		String reportName = reconcileMonthlyReport.getReportDocument() + "_"
				+ reportType + "_" + reconcileMonthlyReport.getBankName() + ".pdf";

		String cekFile = cekFileInFileSystem(reportName,
				reconcileMonthlyReport.getTransactionType(), reconcileMonthlyReport.getBankName().toLowerCase());

		if (cekFile != null) {
			addError(getDelegate(), "errorShadow", cekFile,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		String context = getBaseUrl();
		System.out.println("DOWNLOAD --> " + context + "/download?fileName="
				+ reportName + "&transactionType="
				+ reconcileMonthlyReport.getTransactionType() + "&bankName="
				+ reconcileMonthlyReport.getBankName());

		cycle.sendRedirect(context + "/download?fileName=" + reportName
				+ "&transactionType="
				+ reconcileMonthlyReport.getTransactionType() + "&reconcileTypeReport=monthly"
				+ "&bankName="
				+ reconcileMonthlyReport.getBankName());
	}

	public void retrieveFileXls(IRequestCycle cycle,
			ReconcileReportMonthly reconcileMonthlyReport, String type) {
		String reportType = type
				.equalsIgnoreCase("Unsettled/Need Confirmation") ? "unsettled"
				: type.toLowerCase();

		String reportName = reconcileMonthlyReport.getReportDocument() + "_"
				+ reportType + "_" + reconcileMonthlyReport.getBankName() + ".xls";

		String cekFile = cekFileInFileSystem(reportName,
				reconcileMonthlyReport.getTransactionType(), reconcileMonthlyReport.getBankName().toLowerCase());

		if (cekFile != null) {
			addError(getDelegate(), "errorShadow", cekFile,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		String context = getBaseUrl();
		System.out.println("DOWNLOAD --> " + context + "/download?fileName="
				+ reportName + "&transactionType="
				+ reconcileMonthlyReport.getTransactionType() + "&bankName="
				+ reconcileMonthlyReport.getBankName());

		cycle.sendRedirect(context + "/download?fileName=" + reportName
				+ "&transactionType="
				+ reconcileMonthlyReport.getTransactionType() + "&reconcileTypeReport=monthly"
				+ "&bankName="
				+ reconcileMonthlyReport.getBankName());
	}

	public boolean isEnableToAccess() {
		boolean result = true;
		FTPClient ftpClient = new FTPClient();
		String ftpServerAddress = FTPManager.getFtpServerAddress();
		int ftpPort = FTPManager.getFtpPort();
		String ftpUsername = FTPManager.getFtpUsername();
		String ftpPassword = FTPManager.getFtpPassword();

		try {
			ftpClient.connect(ftpServerAddress, ftpPort);
			ftpClient.login(ftpUsername, ftpPassword);
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// do nothing
				}
		}
		return result;
	}

	/*
	 * public String cekFile(String remoteFile, String transactionType) { String
	 * errorMessage = null;
	 * 
	 * String ftpServerAddress = FTPManager.getFtpServerAddress(); int ftpPort =
	 * FTPManager.getFtpPort(); String ftpUsername =
	 * FTPManager.getFtpUsername(); String ftpPassword =
	 * FTPManager.getFtpPassword();
	 * 
	 * // ditambahkan pd tgl 04-09-2014, untuk mengakomodir multiple file //
	 * reconcile String directoryServer = FTPManager.getDirectoryServer(); if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.BHP_DIR; } else if
	 * (transactionType
	 * .equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE )) {
	 * directoryServer += Constants.DirectoryFTPConstants.PERANGKAT_DIR; } else
	 * if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.SKOR_DIR; } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.REOR_DIR; } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.IAR_DIR; } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.UNAR_DIR; } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE))
	 * { directoryServer += Constants.DirectoryFTPConstants.IKRAP_DIR; } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.PAP_DIR; }
	 * 
	 * FTPClient ftpClient = null; InputStream binaryFile = null;
	 * 
	 * ftpClient = new FTPClient(); try {
	 * 
	 * ftpClient.connect(ftpServerAddress, ftpPort);
	 * ftpClient.login(ftpUsername, ftpPassword);
	 * ftpClient.changeWorkingDirectory(directoryServer);
	 * 
	 * binaryFile = ftpClient.retrieveFileStream(remoteFile);
	 * 
	 * if (binaryFile == null) { errorMessage =
	 * getText("leftMenu.reconcileMonthly.ftp.fileNotFound"); return
	 * errorMessage; }
	 * 
	 * ftpClient.logout();
	 * 
	 * } catch (SocketException e) { e.printStackTrace(); log.trace(e);
	 * errorMessage = getText("leftMenu.reconcileMonthly.ftp.error"); return
	 * errorMessage;
	 * 
	 * } catch (IOException e) { e.printStackTrace(); log.trace(e); errorMessage
	 * = getText("leftMenu.reconcileMonthly.ftp.error"); return errorMessage;
	 * 
	 * } finally {
	 * 
	 * if (ftpClient != null && ftpClient.isConnected()) try {
	 * ftpClient.disconnect(); } catch (IOException e) {}
	 * 
	 * if (binaryFile != null) { try { binaryFile.close(); } catch (IOException
	 * e) { e.printStackTrace(); } } }
	 * 
	 * return null; }
	 */

	public String cekFileInFileSystem(String remoteFile,
			String transactionType, String bankName) {
		String errorMessage = null;

		String reportPath = (getSystemParameterManager()
				.findParamValueByParamName("reconcileMonthly.report.path"));
		
		reportPath += bankName + "/";

		if (transactionType
				.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.BHP_DIR;
		} else if (transactionType
				.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
		} else if (transactionType
				.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.SKOR_DIR;
		} else if (transactionType
				.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.REOR_DIR;
		} else if (transactionType
				.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.IAR_DIR;
		} else if (transactionType
				.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.UNAR_DIR;
		} else if (transactionType
				.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.IKRAP_DIR;
		} else if (transactionType
				.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.PAP_DIR;
		}

		File file = new File(reportPath + "/" + remoteFile.substring(17, 38)
				+ "/" + remoteFile);

		if (!file.exists()) {
			errorMessage = getText("leftMenu.reconcileMonthly.ftp.fileNotFound");
			return errorMessage;
		}

		return null;
	}

	public IPropertySelectionModel getChannelCodeModel() {
		Map<String, String> map = new HashMap<String, String>();
		List<Endpoints> endpoints = new ArrayList<Endpoints>();

		// map.put("all", "All");
		List<Endpoints> endpointChannel = new ArrayList<Endpoints>();
		endpointChannel.addAll(getEndpointsManager().getAllEndpointsByType(
				"channel"));

		for (Endpoints tempEndpoint : endpointChannel) {
			map.put(tempEndpoint.getName(), tempEndpoint.getName());
		}

		return new PropertySelectionModel(getLocale(), map, false, false);
	}
}
