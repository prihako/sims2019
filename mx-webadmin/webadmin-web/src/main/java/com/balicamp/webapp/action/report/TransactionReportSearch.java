package com.balicamp.webapp.action.report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
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
import com.balicamp.model.mx.TransactionReport;
import com.balicamp.model.user.Role;
import com.balicamp.service.TransactionReportManager;
import com.balicamp.service.impl.mx.EndpointsManagerImpl;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.webadmin.AnalisaTransactionLogs;
import com.balicamp.webapp.ftp.FTPManager;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class TransactionReportSearch extends AdminBasePage implements PageBeginRenderListener,
		PageEndRenderListener {

	protected final Log log = LogFactory.getLog(AnalisaTransactionLogs.class);

	public abstract String getTransactionType();

	public abstract void setTransactionType(String transactionType);

	public abstract String getBankName();

	public abstract void setBankName(String bankName);

	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	public abstract Date getEndDate();

	public abstract void setEndDate(Date endDate);

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract TransactionReport getTransactionReport();

	@InjectSpring("transactionReportManagerImpl")
	public abstract TransactionReportManager getTransactionReportManager();

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManagerImpl getEndpointsManager();

	@InjectSpring("systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				} else {
					getFields().remove("TRANSACTION_REPORT");
				}
			}
		}
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	public List getTransactionReportList() {
		List<TransactionReport> list = (List<TransactionReport>) getFields().get("TRANSACTION_REPORT");

		return list;
	}

	public IPropertySelectionModel getTransactionTypeModel() {
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
			map.put("All", "All");
			List<Endpoints> endpointList = getEndpointsManager().getAllEndpointsByType("biller");
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

//	penambahan bank BRI - 8/8/2018 - hhy
	public IPropertySelectionModel getBankModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("all", "All");
		map.put("mandiri", "Bank Mandiri");
		map.put("bri", "Bank BRI");
		map.put("bni", "Bank BNI");
		map.put("mandiri_syariah", "Bank Mandiri Syariah");
		map.put("bri_syariah", "Bank BRI Syariah");
		map.put("bni_syariah", "Bank BNI Syariah");
		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public void doSearch(IRequestCycle cycle) {

		String errorMessage = validation();

		if (errorMessage != null) {
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			getFields().put("TRANSACTION_REPORT", new ArrayList<TransactionReport>());
			return;
		}

		List<TransactionReport> list = getTransactionReportManager().findTransactionReportByDate(getTransactionType(),
				getStartDate(), getEndDate(), getBankName());

		// for add serial number, so it can display in table html
		int i = 0;
		for (TransactionReport trx : list) {
			++i;
			trx.setSerialNumber(i);
		}

		getFields().put("TRANSACTION_REPORT", list);
		getFields().put("SEARCH_TRANSACTION", "SEARCH_TRANSACTION");

		if (list == null || list.size() < 1) {
			addError(getDelegate(), "errorShadow",
					getText("leftMenu.transactionReportSearch.errorMessage.dataNotFound"),
					ValidationConstraint.CONSISTENCY);
			getFields().put("TRANSACTION_REPORT", new ArrayList<TransactionReport>());
			return;
		}

	}

	public String validation() {
		String errorMessage = null;

		if (getStartDate() == null) {
			errorMessage = getText("leftMenu.transactionReportSearch.startDate.null");
		} else if (getEndDate() == null) {
			errorMessage = getText("leftMenu.transactionReportSearch.endDate.null");
		} else if (getEndDate().before(getStartDate())) {
			errorMessage = getText("leftMenu.transactionReportSearch.startDate.bigger.endDate");
		}

		// check, is between start date and end date is more than 3 month
		if (errorMessage == null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getStartDate());
			cal.add(Calendar.MONTH, 3);
			if (getEndDate().after(cal.getTime())) {
				errorMessage = getText("leftMenu.transactionReportSearch.startDate.max3Month");
			}
		}

		return errorMessage;
	}

	public void retrieveFile(IRequestCycle cycle, TransactionReport transactionReport) {

		String cekFile = cekFileInFileSystem(transactionReport.getReportDocument(),
				transactionReport.getTransactionType(), transactionReport.getBankName());

		if (cekFile != null) {
			addError(getDelegate(), "errorShadow", cekFile, ValidationConstraint.CONSISTENCY);
			return;
		}

		String context = getBaseUrl();
		System.out.println("DOWNLAOD --> " + context + "/download?fileName=" + transactionReport.getReportDocument()
				+ "&transactionType=" + transactionReport.getTransactionType() + "&bankName="
				+ transactionReport.getBankName());

		cycle.sendRedirect(context + "/download?fileName=" + transactionReport.getReportDocument()
				+ "&transactionType=" + transactionReport.getTransactionType() + "&bankName="
				+ transactionReport.getBankName());
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

	public String cekFile(String remoteFile, String transactionType) {
		String errorMessage = null;

		String ftpServerAddress = FTPManager.getFtpServerAddress();
		int ftpPort = FTPManager.getFtpPort();
		String ftpUsername = FTPManager.getFtpUsername();
		String ftpPassword = FTPManager.getFtpPassword();

		// ditambahkan pd tgl 04-09-2014, untuk mengakomodir multiple file
		// reconcile
		String directoryServer = FTPManager.getDirectoryServer();
		if (transactionType.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.BHP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.SKOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.REOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.IAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.UNAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.IKRAP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.PAP_DIR;
		}

		FTPClient ftpClient = null;
		InputStream binaryFile = null;

		ftpClient = new FTPClient();
		try {

			ftpClient.connect(ftpServerAddress, ftpPort);
			ftpClient.login(ftpUsername, ftpPassword);
			ftpClient.changeWorkingDirectory(directoryServer);

			binaryFile = ftpClient.retrieveFileStream(remoteFile);

			if (binaryFile == null) {
				errorMessage = getText("leftMenu.transactionReportSearch.ftp.fileNotFound");
				return errorMessage;
			}
			// else {
			// byte[] bytes = IOUtils.toByteArray(binaryFile);
			//
			// HttpServletResponse respon = getResponse();
			//
			// respon.setContentType("application/excel");
			// respon.setHeader("Content-disposition", "attachment;filename=" +
			// remoteFile);
			//
			// OutputStream out = respon.getOutputStream();
			// out.write(bytes);
			// }

			ftpClient.logout();

		} catch (SocketException e) {
			e.printStackTrace();
			log.trace(e);
			errorMessage = getText("leftMenu.transactionReportSearch.ftp.error");
			return errorMessage;

		} catch (IOException e) {
			e.printStackTrace();
			log.trace(e);
			errorMessage = getText("leftMenu.transactionReportSearch.ftp.error");
			return errorMessage;

		} finally {

			if (ftpClient != null && ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {}

			if (binaryFile != null) {
				try {
					binaryFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public String cekFileInFileSystem(String remoteFile, String transactionType, String bankName) {
		String errorMessage = null;

		String reportPath = getSystemParameterManager().findParamValueByParamName(
				"webadmin.transactionReport.download.path.parsing.mt940." + bankName);

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

		File file = new File(reportPath + "/" + remoteFile);

		if (!file.exists()) {
			errorMessage = getText("leftMenu.transactionReportSearch.ftp.fileNotFound");
			return errorMessage;
		}

		return null;
	}
}
