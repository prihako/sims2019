package com.balicamp.webapp.action.report;

import java.io.File;
import java.io.IOException;
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
import com.balicamp.model.mx.ReconcileReport;
import com.balicamp.model.user.Role;
import com.balicamp.service.ReconcileReportManager;
import com.balicamp.service.impl.mx.EndpointsManagerImpl;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.soap.ws.channel.ReorChannel;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.ftp.FTPManager;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class ReconcileEod extends AdminBasePage implements PageBeginRenderListener, PageEndRenderListener {

	@InjectSpring("reconcileReportManagerImpl")
	public abstract ReconcileReportManager getReconcileReportManager();

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManagerImpl getEndpointsManager();

	@InjectSpring("systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	private XlstoStringConverter xlsFile;

	public ReconcileEod() {
		super();
		this.xlsFile = new XlstoStringConverter();
	}

	protected final Log log = LogFactory.getLog(ReconcileEod.class);

	public abstract String getBankName();

	public abstract void setBankName(String bankName);

	public abstract String getTransactionType();

	public abstract void setTransactionType(String transactionType);

	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	public abstract Date getEndDate();

	public abstract void setEndDate(Date endDate);

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract ReconcileReport getReconcileReport();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				} else {
					getFields().remove("RECONCILE_REPORT");
				}
			}
		}
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	public List getReconcileReportList() {
		List<ReconcileReport> list = (List<ReconcileReport>) getFields().get("RECONCILE_REPORT");

		return list;
	}

	public IPropertySelectionModel getTransactionTypeModel() {
		HashMap<String, String> map = new HashMap<String, String>();

		List<Endpoints> endpointList = getEndpointsManager().getAllEndpointsByType("biller");
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
			if (getEndpointsManager().findEndpointsByCode(tempRole.getName().toLowerCase()) != null) {
				endpoints.add(getEndpointsManager().findEndpointsByCode(tempRole.getName().toLowerCase()));
			}
		}

		if (endpoints.size() > 0) {
			for (Endpoints tempEnd : endpoints) {
				map.put(tempEnd.getCode(), tempEnd.getName());
			}
		} else {
			// map.put("All", "All");
			List<Endpoints> endpointList = getEndpointsManager().getAllEndpointsByType("biller");
			// List<Endpoints> endpointList =
			// getEndpointsManager().getAllEndpointsByState("ready");
			for (Endpoints tempEndpoint : endpointList) {
				if (!tempEndpoint.getCode().equalsIgnoreCase("chws") && !tempEndpoint.getCode().equalsIgnoreCase("chws2")) {
					map.put(tempEndpoint.getCode(), tempEndpoint.getName());
				}
			}

		}

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public void doSearch(IRequestCycle cycle) {

		String errorMessage = validation();

		if (errorMessage != null) {
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			return;
		}

		List<ReconcileReport> list = getReconcileReportManager().findReconcileReportByDate(getBankName(), getTransactionType(), getStartDate(), getEndDate());

		// for add serial number, so it can display in table html
		int i = 0;
		for (ReconcileReport trx : list) {
			++i;
			trx.setSerialNumber(i);
		}

		if (list == null || list.size() < 1) {
			addError(getDelegate(), "errorShadow", getText("leftmenu.reconcileEod.errorMessage.emptySearchResult"), ValidationConstraint.CONSISTENCY);
		}

		getFields().put("RECONCILE_REPORT", list);
		getFields().put("SEARCH_RECONCILE_REPORT", "SEARCH_RECONCILE_REPORT");

	}

	public String validation() {
		String errorMessage = null;

		if (getStartDate() == null) {
			errorMessage = getText("leftMenu.reconcileEod.startDate.null");
		} else if (getEndDate() == null) {
			errorMessage = getText("leftMenu.reconcileEod.endDate.null");
		} else if (getEndDate().before(getStartDate())) {
			errorMessage = getText("leftMenu.reconcileEod.startDate.bigger.endDate");
		}

		// check, is between start date and end date is more than 3 month
		if (errorMessage == null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getStartDate());
			cal.add(Calendar.MONTH, 3);
			if (getEndDate().after(cal.getTime())) {
				errorMessage = getText("leftMenu.reconcileEod.startDate.max3Month");
			}
		}

		return errorMessage;
	}

	public void retrieveFilePdf(IRequestCycle cycle, ReconcileReport reconcileReport, String type) {

		String reportName = reconcileReport.getReportDocument() + "_" + type + "_" + reconcileReport.getBankName() + ".pdf";

		String cekFile = cekFileInFileSystem(reportName, reconcileReport.getTransactionType(), reconcileReport.getBankName().toLowerCase());
		System.out.println("DOWNLOAD --> /download?fileName=" + reportName + "&transactionType=" + reconcileReport.getTransactionType() + "&bankName=" + reconcileReport.getBankName());

		if (cekFile != null) {
			addError(getDelegate(), "errorShadow", cekFile, ValidationConstraint.CONSISTENCY);
			return;
		}

		String context = getBaseUrl();
		System.out.println("DOWNLOAD --> " + context + "/download?fileName=" + reportName + "&transactionType=" + reconcileReport.getTransactionType() + "&reconcileTypeReport=eod"  + "&bankName=" + reconcileReport.getBankName());

		cycle.sendRedirect(context + "/download?fileName=" + reportName + "&transactionType=" + reconcileReport.getTransactionType() + "&reconcileTypeReport=eod"  + "&bankName=" + reconcileReport.getBankName());
	}

	public void retrieveFileXls(IRequestCycle cycle, ReconcileReport reconcileReport, String type) {

		String reportName = reconcileReport.getReportDocument() + "_" + type + "_" + reconcileReport.getBankName() + ".xls";

		String cekFile = cekFileInFileSystem(reportName, reconcileReport.getTransactionType(), reconcileReport.getBankName().toLowerCase());

		if (cekFile != null) {
			addError(getDelegate(), "errorShadow", cekFile, ValidationConstraint.CONSISTENCY);
			return;
		}

		String context = getBaseUrl();
		System.out.println("DOWNLOAD --> " + context + "/download?fileName=" + reportName + "&transactionType=" + reconcileReport.getTransactionType() + "&bankName=" + reconcileReport.getBankName().toLowerCase());

		cycle.sendRedirect(context + "/download?fileName=" + reportName + "&transactionType=" + reconcileReport.getTransactionType() + "&reconcileTypeReport=eod"+ "&bankName=" + reconcileReport.getBankName());
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
	 * public String cekFile(String remoteFile, String transactionType) {
	 * String errorMessage = null;
	 * 
	 * String ftpServerAddress = FTPManager.getFtpServerAddress();
	 * int ftpPort = FTPManager.getFtpPort();
	 * String ftpUsername = FTPManager.getFtpUsername();
	 * String ftpPassword = FTPManager.getFtpPassword();
	 * 
	 * // ditambahkan pd tgl 04-09-2014, untuk mengakomodir multiple file
	 * // reconcile
	 * String directoryServer = FTPManager.getDirectoryServer();
	 * if (transactionType.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE))
	 * {
	 * directoryServer += Constants.DirectoryFTPConstants.BHP_DIR;
	 * } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE
	 * )) {
	 * directoryServer += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
	 * } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.SKOR_DIR;
	 * } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.REOR_DIR;
	 * } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.IAR_DIR;
	 * } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.UNAR_DIR;
	 * } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE))
	 * {
	 * directoryServer += Constants.DirectoryFTPConstants.IKRAP_DIR;
	 * } else if
	 * (transactionType.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
	 * directoryServer += Constants.DirectoryFTPConstants.PAP_DIR;
	 * }
	 * 
	 * FTPClient ftpClient = null;
	 * InputStream binaryFile = null;
	 * 
	 * ftpClient = new FTPClient();
	 * try {
	 * 
	 * ftpClient.connect(ftpServerAddress, ftpPort);
	 * ftpClient.login(ftpUsername, ftpPassword);
	 * ftpClient.changeWorkingDirectory(directoryServer);
	 * 
	 * binaryFile = ftpClient.retrieveFileStream(remoteFile);
	 * 
	 * if (binaryFile == null) {
	 * errorMessage = getText("leftMenu.reconcileEod.ftp.fileNotFound");
	 * return errorMessage;
	 * }
	 * 
	 * ftpClient.logout();
	 * 
	 * } catch (SocketException e) {
	 * e.printStackTrace();
	 * log.trace(e);
	 * errorMessage = getText("leftMenu.reconcileEod.ftp.error");
	 * return errorMessage;
	 * 
	 * } catch (IOException e) {
	 * e.printStackTrace();
	 * log.trace(e);
	 * errorMessage = getText("leftMenu.reconcileEod.ftp.error");
	 * return errorMessage;
	 * 
	 * } finally {
	 * 
	 * if (ftpClient != null && ftpClient.isConnected())
	 * try {
	 * ftpClient.disconnect();
	 * } catch (IOException e) {}
	 * 
	 * if (binaryFile != null) {
	 * try {
	 * binaryFile.close();
	 * } catch (IOException e) {
	 * e.printStackTrace();
	 * }
	 * }
	 * }
	 * 
	 * return null;
	 * }
	 */

	public String cekFileInFileSystem(String remoteFile, String transactionType, String bankName) {
		String errorMessage = null;

		String reportPath = (getSystemParameterManager().findParamValueByParamName("reconcileEod.report.path"));
		
		reportPath += bankName + "/";

		if (transactionType.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.BHP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.SKOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.REOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.IAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.UNAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.IKRAP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.PAP_DIR;
		}

		File file = new File(reportPath + "/" + remoteFile.substring(17, 27) + "/" + remoteFile);

		if (!file.exists()) {
			errorMessage = getText("leftMenu.reconcileEod.ftp.fileNotFound");
			return errorMessage;
		}

		return null;
	}

	public IPropertySelectionModel getChannelCodeModel() {
		Map<String, String> map = new HashMap<String, String>();
		List<Endpoints> endpoints = new ArrayList<Endpoints>();

		// map.put("all", "All");
		List<Endpoints> endpointChannel = new ArrayList<Endpoints>();
		endpointChannel.addAll(getEndpointsManager().getAllEndpointsByType("channel"));

		for (Endpoints tempEndpoint : endpointChannel) {
			map.put(tempEndpoint.getName(), tempEndpoint.getName());
		}

		return new PropertySelectionModel(getLocale(), map, false, false);
	}
}
