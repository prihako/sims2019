package com.balicamp.webapp.scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.Constants;

import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.impl.armgmt.ArmgmtManagerImpl;
import com.balicamp.service.impl.ebs.ExternalBillingSystemManagerImpl;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.report.ReportReconcileAction;
import com.balicamp.webapp.action.report.XlstoStringConverter;
import com.balicamp.webapp.ftp.FTPManager;
import com.balicamp.webapp.thread.DoGetReconcileListByMt940NewMonthly;
import com.balicamp.webapp.thread.DoGetReconcileListByMt940NewMonthlyThread;
import com.balicamp.webapp.thread.DoGetReconcileListByMt940NewThread;
import com.balicamp.webapp.util.SendMail;

@SuppressWarnings("serial")
public class ReconcileMonthlyTask extends HttpServlet {
	protected final Log log = LogFactory.getLog(getClass());
	private FTPManager ftpManager;
	private DataSource dataSource;
	private DataSource dataSourceWebapp;
	@Autowired
	public SystemParameterManager systemParameter;

	@Autowired
	public TransactionLogsManager trxLogManager;

	@Autowired
	public ExternalBillingSystemManagerImpl externalBillingSystem;

	@Autowired
	public ArmgmtManagerImpl armgmtManagerImpl;
	
	private XlstoStringConverter xlsFile;

	public ReconcileMonthlyTask() {
		this.xlsFile = new XlstoStringConverter();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDataSourceWebapp(DataSource dataSourceWebapp) {
		this.dataSourceWebapp = dataSourceWebapp;

	}

	public void setFtpManager(FTPManager ftpManager) {
		this.ftpManager = ftpManager;
	}

	public void listAllFiles() throws Exception {
		String[] fileNames = ftpManager.listFileNames(null);
		log.info("---------List Of Files-----------");
		for (int i = 0; i < fileNames.length; i++) {
			System.out.println("-----------" + fileNames[i] + "---------");
			log.info(fileNames[i]);
		}
		log.info("---------End Of List Of Files-----------");
	}

	public void init() throws ServletException {
		super.init();
	}

	public void reconcileMonthly() throws FileNotFoundException, JRException,
			IOException, ServletException, SQLException {
		
		init();
		String sql = "SELECT * FROM endpoints WHERE type = 'channel'";
		Connection con = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		// boolean flag = false;
		List<String> channelList = new ArrayList<String>();

		try {
			con = dataSource.getConnection();
			stat = con.prepareStatement(sql);
			rs = stat.executeQuery();
			while (rs.next()) {
				String chnl[] = rs.getString("name").split(" ");
				channelList.add(chnl[1] + "-" + rs.getString("code").trim());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			con.close();
		}

		// get Biller List
		sql = "SELECT * FROM endpoints WHERE type = 'biller'";
		con = null;
		stat = null;
		rs = null;
		// boolean flag = false;
		List<String> billerList = new ArrayList<String>();

		try {
			con = dataSource.getConnection();
			stat = con.prepareStatement(sql);
			rs = stat.executeQuery();
			while (rs.next()) {
				billerList.add(rs.getString("code"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			con.close();
		}

		Iterator<String> billerIterator = billerList.iterator();
		log.info("Monthly recon run");
		while (billerIterator.hasNext()) {

			DoGetReconcileListByMt940NewMonthly thread = new DoGetReconcileListByMt940NewMonthly(
					systemParameter,
					trxLogManager,
					armgmtManagerImpl,
					dataSourceWebapp,
					dataSource,
					billerIterator.next(), 
					channelList);
			thread.doGetReconcileListByMt940EodNew();
		}
		log.info("Monthly recon finish");
	}
	
	

	/*
	public void doGetReconcileListByMt940Eod(List<String> channelList,
			String transactionCode) throws FileNotFoundException, JRException,
			IOException, SQLException {

		String mt940BasePath = systemParameter
				.findParamValueByParamName("reconcileMonthly.mt940.path");
		String jrxmlRealPathSettled = systemParameter
				.findParamValueByParamName("reconcileMonthly.jrxml.settled.path");
		String jasperRealPathSettled = systemParameter
				.findParamValueByParamName("reconcileMonthly.jasper.settled.path");
		String jrxmlRealPathUnsettled = systemParameter
				.findParamValueByParamName("reconcileMonthly.jrxml.unsettled.path");
		String jasperRealPathUnsettled = systemParameter
				.findParamValueByParamName("reconcileMonthly.jasper.unsettled.path");
		Map<String, String> paramsSettled = new HashMap<String, String>();
		Map<String, String> paramsUnsettled = new HashMap<String, String>();

		Calendar reconcileCalendar = Calendar.getInstance();
		reconcileCalendar.setTime(DateUtil.substractMonth(new Date(), 1));
		int maxDay = 0;

		String trxDateNameString = "";
		// String trxDateNameString = "20151020";
		String filepath = null;
		String filename = null;
		String channelCode[] = null;
		String billerCodeNumber = null;
		String trxCode = null;

		Map<String, String> listFile = new HashMap<String, String>();
		Integer settled = 0;
		Integer unSettled = 0;
		Integer needConfirmation = 0;
		Long paymentAmountSettled = 0L;
		Long paymentAmountUnSettled = 0L;
		Long payemntAmountNeedConfirmation = 0L;

		List<String> channelCodeList = channelList;

		Iterator<String> channelCodeListIterator = channelCodeList.iterator();

		while (channelCodeListIterator.hasNext()) {
			List<ReconcileDto> searchResultSettled = new ArrayList<ReconcileDto>();
			List<ReconcileDto> searchResultUnsettled = new ArrayList<ReconcileDto>();

			Map<String, Integer> mapCountSettled = new HashMap<String, Integer>();
			mapCountSettled.put("settled", 0);
			mapCountSettled.put("notSettled", 0);
			mapCountSettled.put("unconfirmed", 0);
			Map<String, Long> mapCountAmountSettled = new HashMap<String, Long>();
			mapCountAmountSettled.put("amountSettled", (long) 0);
			mapCountAmountSettled.put("amountNotSettled", (long) 0);
			mapCountAmountSettled.put("amountUnconfirmed", (long) 0);
			mapCountAmountSettled.put("totalAmount", (long) 0);
			mapCountAmountSettled.put("totalAmountDenda", (long) 0);
			Map<String, Integer> mapCountUnsettled = new HashMap<String, Integer>();
			mapCountUnsettled.put("settled", 0);
			mapCountUnsettled.put("notSettled", 0);
			mapCountUnsettled.put("unconfirmed", 0);
			Map<String, Long> mapCountAmountUnsettled = new HashMap<String, Long>();
			mapCountAmountUnsettled.put("amountSettled", (long) 0);
			mapCountAmountUnsettled.put("amountNotSettled", (long) 0);
			mapCountAmountUnsettled.put("amountUnconfirmed", (long) 0);
			mapCountAmountUnsettled.put("totalAmount", (long) 0);
			mapCountAmountUnsettled.put("totalAmountDenda", (long) 0);

			HashMap<String, String> mt940Data = null;
			Set<String> listInvoiceCode = null;

			int i = 0;
			channelCode = channelCodeListIterator.next().split("-"); // 0= nama
																		// bank,
																		// 1=nama
																		// channel
																		// di mx

			reconcileCalendar.set(Calendar.MONTH,
					reconcileCalendar.get(Calendar.MONTH));
			reconcileCalendar.set(Calendar.DAY_OF_MONTH, 1);
			maxDay = reconcileCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			for (int j = 1; j <= maxDay; j++) {
				mt940Data = new HashMap<String, String>();
				listInvoiceCode = new HashSet<String>();

				reconcileCalendar.set(Calendar.DAY_OF_MONTH, j);
				trxDateNameString = DateUtil.convertDateToString(
						reconcileCalendar.getTime(), "yyyyMMdd");

				filepath = mt940BasePath + channelCode[0].toLowerCase() + "/";

				if (transactionCode
						.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
					filepath += Constants.DirectoryFTPConstants.BHP_DIR;
					billerCodeNumber = Constants.BillerNumberCode.BHP_CODE;
					trxCode = Constants.EndpointCode.BHP_CODE;
				} else if (transactionCode
						.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
					filepath += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
					billerCodeNumber = Constants.BillerNumberCode.PERANGKAT_CODE;
					trxCode = Constants.EndpointCode.PERANGKAT_CODE;
				} else if (transactionCode
						.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
					filepath += Constants.DirectoryFTPConstants.SKOR_DIR;
					billerCodeNumber = Constants.BillerNumberCode.SKOR_CODE;
					trxCode = Constants.EndpointCode.SKOR_CODE;
				} else if (transactionCode
						.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
					filepath += Constants.DirectoryFTPConstants.REOR_DIR;
					billerCodeNumber = Constants.BillerNumberCode.REOR_CODE;
					trxCode = Constants.EndpointCode.REOR_CODE;
				} else if (transactionCode
						.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
					filepath += Constants.DirectoryFTPConstants.IAR_DIR;
					billerCodeNumber = Constants.BillerNumberCode.IAR_CODE;
					trxCode = Constants.EndpointCode.IAR_CODE;
				} else if (transactionCode
						.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
					filepath += Constants.DirectoryFTPConstants.UNAR_DIR;
					billerCodeNumber = Constants.BillerNumberCode.UNAR_CODE;
					trxCode = Constants.EndpointCode.UNAR_CODE;
				} else if (transactionCode
						.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
					filepath += Constants.DirectoryFTPConstants.IKRAP_DIR;
					billerCodeNumber = Constants.BillerNumberCode.IKRAP_CODE;
					trxCode = Constants.EndpointCode.IKRAP_CODE;
				} else if (transactionCode
						.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
					filepath += Constants.DirectoryFTPConstants.PAP_DIR;
					billerCodeNumber = Constants.BillerNumberCode.PAP_CODE;
					trxCode = Constants.EndpointCode.PAP_CODE;
				}

				trxCode += ".PAY";
				filepath += "/MT940" + trxDateNameString + billerCodeNumber
						+ ".xls";

				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date trxDate = null;

				try {
					trxDate = formatter.parse(DateUtil.convertDateToString(
							reconcileCalendar.getTime(), "dd-MM-yyyy"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (cekFileInFileSystem(filepath, transactionCode,
						channelCode[0]) == true) {
					mt940Data.putAll(xlsFile.getMt940Data(filepath));
				} else {
					continue;
				}

				if (mt940Data.isEmpty()) {
					System.out.println("DATA IS EMPTY");
				} else {
					listInvoiceCode = mt940Data.keySet();

					searchResultSettled.addAll(trxLogManager
							.findTransactionLogsWebadmin(mt940Data,
									channelCode[1], listInvoiceCode, null,
									trxCode, trxDate, "Settled",
									mapCountSettled, mapCountAmountSettled));
					searchResultUnsettled
							.addAll(trxLogManager.findTransactionLogsWebadmin(
									mt940Data, channelCode[1], listInvoiceCode,
									null, trxCode, trxDate,
									"Unsettled/Need Confirmation",
									mapCountUnsettled, mapCountAmountUnsettled));
				}
			}

			ReportReconcileAction reportReconcileAction = new ReportReconcileAction();
			String reportDir = trxLogManager
					.findParamValueByParamName("reconcileMonthly.report.path")
					+ channelCode[0].toLowerCase() + "/" + transactionCode;

			// Date Laporan Reconcile Dilakukan

			Calendar cal = Calendar.getInstance();
			cal.setTime(DateUtil.substractMonth(new Date(), 1));
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Date startDate = cal.getTime();
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			Date endDate = cal.getTime();

			putToParam(
					paramsSettled,
					mapCountSettled,
					mapCountAmountSettled,
					startDate,
					endDate,
					channelCode[1],
					channelCode[0],
					"Settled",
					systemParameter
							.findParamValueByParamName("reconcileMonthly.report.image.path"),
					"");

			putToParam(
					paramsUnsettled,
					mapCountUnsettled,
					mapCountAmountUnsettled,
					startDate,
					endDate,
					channelCode[1],
					channelCode[0],
					"Unsettled/Need Confirmation",
					systemParameter
							.findParamValueByParamName("reconcileMonthly.report.image.path"),
					"");

			// result.put("params", params);
			// result.put("jrxmlPath", jrxmlRealPath);
			// result.put("jasperPath", jasperRealPath);
			// result.put("searchResult", searchResult);

			ArrayList<ReconcileDto> reconcileUnsettled = new ArrayList<ReconcileDto>();
			ArrayList<ReconcileDto> reconcileUnconfirmed = new ArrayList<ReconcileDto>();

			for (ReconcileDto dataReconcile : searchResultUnsettled) {
				if (dataReconcile.getReconcileStatus().equalsIgnoreCase(
						"Unsettled")) {
					reconcileUnsettled.add(dataReconcile);
				} else if (dataReconcile.getReconcileStatus().equalsIgnoreCase(
						"Need Confirmation/Manual Payment")) {
					reconcileUnconfirmed.add(dataReconcile);
				}
			}

			searchResultUnsettled.clear();
			searchResultUnsettled.addAll(reconcileUnsettled);
			searchResultUnsettled.addAll(reconcileUnconfirmed);

			reportReconcileAction.createReportWithoutContextByDataTypeMonthly(
					paramsSettled, searchResultSettled, jasperRealPathSettled,
					jrxmlRealPathSettled, reportDir, channelCode[0], "PDF",
					"settled");
			String reportPathSettled = reportReconcileAction
					.createReportWithoutContextByDataTypeMonthly(paramsSettled,
							searchResultSettled, jasperRealPathSettled,
							jrxmlRealPathSettled, reportDir, channelCode[0],
							"XLS", "settled");

			saveToLogQuery(new Date(), reportPathSettled, "Settled",
					transactionCode, channelCode[0]);

			reportReconcileAction.createReportWithoutContextByDataTypeMonthly(
					paramsUnsettled, searchResultUnsettled,
					jasperRealPathUnsettled, jrxmlRealPathUnsettled, reportDir,
					channelCode[0], "PDF", "unsettled");
			String reportPathUnsettled = reportReconcileAction
					.createReportWithoutContextByDataTypeMonthly(
							paramsUnsettled, searchResultUnsettled,
							jasperRealPathUnsettled, jrxmlRealPathUnsettled,
							reportDir, channelCode[0], "XLS", "unsettled");

			saveToLogQuery(new Date(), reportPathUnsettled,
					"Unsettled/Need Confirmation", transactionCode,
					channelCode[0]);

			// for detail in email
			settled += mapCountSettled.get("settled");
			unSettled += mapCountUnsettled.get("notSettled");
			needConfirmation += mapCountUnsettled.get("unconfirmed");
			paymentAmountSettled += mapCountAmountSettled.get("amountSettled");
			paymentAmountUnSettled += mapCountAmountUnsettled
					.get("amountNotSettled");

			// DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
			// Calendar calStartEmail = Calendar.getInstance();
			// calStartEmail.setTime(new Date());
			// calStartEmail.add(Calendar.DATE, -1);
			// String dateStartEmail =
			// dateFormat.format(calStartEmail.getTime());
			//
			// Calendar calEndEmail = Calendar.getInstance();
			// calEndEmail.setTime(new Date());
			// calEndEmail.add(Calendar.DATE, -5);
			// String dateEndEmail = dateFormat.format(cal.getTime());
			//
			// listFile.put(reportPathSettled + dateStartEmail + "_"
			// + dateEndEmail + "_" + "settled" + "_" + channelCode[0]
			// + ".pdf", reportDir + "/" + dateStartEmail + "_"
			// + dateEndEmail);
			// listFile.put(reportPathSettled + dateStartEmail + "_"
			// + dateEndEmail + "_" + "settled" + "_" + channelCode[0]
			// + ".xls", reportDir + "/" + dateStartEmail + "_"
			// + dateEndEmail);
			// listFile.put(reportPathUnsettled + dateStartEmail + "_"
			// + dateEndEmail + "_" + "unsettled" + "_" + channelCode[0]
			// + ".pdf", reportDir + "/" + dateStartEmail + "_"
			// + dateEndEmail);
			// listFile.put(reportPathUnsettled + dateStartEmail + "_"
			// + dateEndEmail + "_" + "unsettled" + "_" + channelCode[0]
			// + ".xls", reportDir + "/" + dateStartEmail + "_"
			// + dateEndEmail);

		}

		if (!(settled == 0 && unSettled == 0 && needConfirmation == 0)) {
			sendMail(listFile, settled, unSettled, needConfirmation,
					paymentAmountSettled, paymentAmountUnSettled,
					payemntAmountNeedConfirmation, transactionCode);
		}
	}*/

	public Boolean cekFileInFileSystem(String filepath, String transactionType,
			String bankName) {
		String errorMessage = null;

		File file = new File(filepath);

		if (!file.exists()) {
			System.out.println("File MT940 " + transactionType + " pada bank "
					+ bankName + " Tidak Ditemukan Pada Path : " + filepath);
			return false;
		}

		return true;
	}
}
