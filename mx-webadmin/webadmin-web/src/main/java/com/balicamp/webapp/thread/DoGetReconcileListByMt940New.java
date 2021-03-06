package com.balicamp.webapp.thread;

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
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.impl.armgmt.ArmgmtManagerImpl;
import com.balicamp.service.impl.ebs.ExternalBillingSystemManagerImpl;
import com.balicamp.service.impl.pengujian.SertifikasiManagerImpl;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.soap.ws.channel.IarChannel;
import com.balicamp.soap.ws.channel.IkrapChannel;
import com.balicamp.soap.ws.channel.KlbsiChannel;
import com.balicamp.soap.ws.channel.PengujianChannel;
import com.balicamp.soap.ws.channel.ReorChannel;
import com.balicamp.soap.ws.channel.SertifikasiChannel;
import com.balicamp.soap.ws.channel.UnarChannel;
import com.balicamp.util.DateUtil;
import com.balicamp.util.LogHelper;
import com.balicamp.webapp.action.report.ReportReconcileAction;
import com.balicamp.webapp.util.SendMail;

import net.sf.jasperreports.engine.JRException;
import test.Constants;

public class DoGetReconcileListByMt940New{
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private SystemParameterManager systemParameter;
	private TransactionLogsManager trxLogManager;
	private ArmgmtManagerImpl armgmtManagerImpl;
	private DataSource dataSourceWebapp;
	private ReorChannel reorChannel;
	private UnarChannel unarChannel;
	private IarChannel iarChannel;
	private KlbsiChannel klbsiChannel;
	private IkrapChannel ikrapChannel;
	private PengujianChannel pengujianChannel;
	private SertifikasiChannel sertifikasiChannel;
	private DataSource dataSource;
	private ExternalBillingSystemManagerImpl externalBillingSystem;
	private SertifikasiManagerImpl sertifikasiManagerImpl;
	private String billerCode;
	private List<String> channelList;
	private Calendar reconcileCalendar;
	private Calendar createdOnCalendar;
	
	public DoGetReconcileListByMt940New(SystemParameterManager systemParameter,
			TransactionLogsManager trxLogManager,
			ArmgmtManagerImpl armgmtManagerImpl,
			DataSource dataSourceWebapp,
			ReorChannel reorChannel,
			UnarChannel unarChannel,
			IarChannel iarChannel,
			KlbsiChannel klbsiChannel,
			IkrapChannel ikrapChannel,
			PengujianChannel pengujianChannel,
			SertifikasiChannel sertifikasiChannel,
			DataSource dataSource,
			ExternalBillingSystemManagerImpl externalBillingSystem,
			SertifikasiManagerImpl sertifikasiManagerImpl,
			String billerCode, 
			List<String> channelList,
			Calendar reconcileCalendar, 
			Calendar createdOnCalendar){
		this.systemParameter = systemParameter;
		this.trxLogManager = trxLogManager;
		this.armgmtManagerImpl = armgmtManagerImpl;
		this.dataSourceWebapp = dataSourceWebapp;
		this.reorChannel = reorChannel;
		this.unarChannel = unarChannel;
		this.iarChannel=iarChannel;
		this.klbsiChannel=klbsiChannel;
		this.ikrapChannel=ikrapChannel;
		this.pengujianChannel=pengujianChannel;
		this.sertifikasiChannel=sertifikasiChannel;
		this.dataSource=dataSource;
		this.externalBillingSystem=externalBillingSystem;
		this.sertifikasiManagerImpl=sertifikasiManagerImpl;
		this.billerCode = billerCode;
		this.channelList = channelList;
		this.reconcileCalendar = reconcileCalendar;
		this.createdOnCalendar = createdOnCalendar;
	}
	
	public void doGetReconcileListByMt940EodNew() throws FileNotFoundException, JRException, IOException, SQLException, ParseException {

		ReportReconcileAction reportReconcileAction = new ReportReconcileAction();
		
		String mt940BasePath 			= systemParameter.findParamValueByParamName("reconcileEod.mt940.path");
		String jrxmlRealPath 			= systemParameter.findParamValueByParamName("reconcileEod.jrxml.path");
		String jasperRealPath 			= systemParameter.findParamValueByParamName("reconcileEod.jasper.path");
		Map<String, String> params 		= new HashMap<String, String>();
		Map<String, String> finalParams = new HashMap<String, String>();
		
		String trxCode 		= "";
		String paymentType 	= "";
		
		String filepath 	= null;
		String filename 	= null;
		String channelCode[]= null;
		
		Map<String, String> listFile = new HashMap<String, String>();
		Integer settled 					= 0;
		Integer unSettled 					= 0;
		Integer needConfirmation 			= 0;
		Long paymentAmountSettled 			= 0L;
		Long paymentAmountUnSettled 		= 0L;
		Long paymentAmountNeedConfirmation 	= 0L;
		
		Iterator<String> channelCodeListIterator = channelList.iterator();
		while (channelCodeListIterator.hasNext()) {
			List<ReconcileDto> searchResult 		= new ArrayList<ReconcileDto>();
			List<ReconcileDto> finalSearchResult 	= new ArrayList<ReconcileDto>();
			
			Map<String, Integer> mapCount = new HashMap<String, Integer>();
			mapCount.put("settled", 0);
			mapCount.put("notSettled", 0);
			mapCount.put("unconfirmed", 0);
			Map<String, Long> mapCountAmount = new HashMap<String, Long>();
			mapCountAmount.put("amountSettled", (long) 0);
			mapCountAmount.put("amountNotSettled", (long) 0);
			mapCountAmount.put("amountUnconfirmed", (long) 0);
			mapCountAmount.put("totalAmount", (long) 0);
			mapCountAmount.put("totalAmountDenda", (long) 0);
			
			channelCode = channelCodeListIterator.next().split("-");
			
			if (billerCode.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
				trxCode = Constants.EndpointCode.BHP_CODE;
				paymentType = Constants.BillerNumberCode.BHP_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
				trxCode = Constants.EndpointCode.PERANGKAT_CODE;
				paymentType = Constants.BillerNumberCode.PERANGKAT_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
				trxCode = Constants.EndpointCode.SKOR_CODE;
				paymentType = Constants.BillerNumberCode.SKOR_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
				trxCode = Constants.EndpointCode.REOR_CODE;
				paymentType = Constants.BillerNumberCode.REOR_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
				trxCode = Constants.EndpointCode.IAR_CODE;
				paymentType = Constants.BillerNumberCode.IAR_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
				trxCode = Constants.EndpointCode.UNAR_CODE;
				paymentType = Constants.BillerNumberCode.UNAR_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
				trxCode = Constants.EndpointCode.IKRAP_CODE;
				paymentType = Constants.BillerNumberCode.IKRAP_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
				trxCode = Constants.EndpointCode.PAP_CODE;
				paymentType = Constants.BillerNumberCode.PAP_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.KALIBRASI_CODE)) {
				trxCode = Constants.EndpointCode.KALIBRASI_CODE;
				paymentType = Constants.BillerNumberCode.KALIBRASI_CODE;
			}
			
			trxCode += ".PAY";
	
			HashMap<String, Object[]> mxData 	= new HashMap<String, Object[]>();
			HashMap<String, Object[]> mt940Data = new HashMap<String, Object[]>();
			Set<String> listInvoiceCode 		= new HashSet<String>();
	
			Date trxDate 				= null;
			SimpleDateFormat formatter 	= new SimpleDateFormat("dd-MM-yyyy");
			try {
				trxDate = formatter.parse(DateUtil.convertDateToString(reconcileCalendar.getTime(), "dd-MM-yyyy"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mxData.putAll(trxLogManager.
					findAllTransactionLogsWebadminReconcileByDate(channelCode[1], trxCode, new String[] { "00" }, new String[] { "00" }, trxDate));
			mt940Data.putAll(armgmtManagerImpl.getMT940(trxDate, null, paymentType, channelCode[0]));
			log.info("trxDate : " + trxDate);
			log.info("paymentType : " + paymentType);
			log.info("channelCode[0] : " + channelCode[0]);
			
			if(mt940Data.isEmpty() && mxData.isEmpty()) {
				log.info("DATA IS EMPTY");
			}else{
				if(!mt940Data.isEmpty()){
					listInvoiceCode = mt940Data.keySet();
				}
				log.info("MT940 Data #1 "+LogHelper.toString(mt940Data));
				
				if(!mxData.isEmpty()){
					listInvoiceCode = mxData.keySet();
				}
				
				searchResult.addAll(trxLogManager.findTransactionLogsWebadmin(mt940Data, mxData, channelCode[1], 
						listInvoiceCode, null, trxCode, trxDate, "all", mapCount, mapCountAmount));
			}

			log.info("searchResult : " + searchResult.size());
	
			log.info("SYNCRONIZED");
			synchronized (this){
	//			String reportDir = trxLogManager.findParamValueByParamName("reconcileEod.report.path") + billerCode;
				String reportDir = trxLogManager.findParamValueByParamName("reconcileEod.report.path") + channelCode[0].toLowerCase() + "/" + billerCode;
				putToParam(params, mapCount, mapCountAmount, createdOnCalendar.getTime(), channelCode[1], channelCode[0], "All", systemParameter.findParamValueByParamName("reconcileEod.report.image.path"), "");
		
				log.info("MT940 Data #2 "+LogHelper.toString(mt940Data));
				reportReconcileAction.createReportWithoutContextByDataType(params, searchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "PDF", "original");
				reportReconcileAction.createReportWithoutContextByDataType(params, searchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "XLS", "original");
				
				finalSearchResult.addAll(autoReconcileToPaid(searchResult, mapCount, mapCountAmount, billerCode));
		
				putToParam(finalParams, mapCount, mapCountAmount, createdOnCalendar.getTime(), channelCode[1], channelCode[0], "All", systemParameter.findParamValueByParamName("reconcileEod.report.image.path"), "Hasil Rekonsiliasi Otomatis");
		
				reportReconcileAction.createReportWithoutContextByDataType(finalParams, finalSearchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "PDF", "reconciled");
				String reportPath = reportReconcileAction.createReportWithoutContextByDataType(finalParams, finalSearchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "XLS", "reconciled");
		
				saveToLogQuery(createdOnCalendar.getTime(), reportPath, billerCode, channelCode[0]);
		
				// for detail in email
				settled += mapCount.get("settled");
				unSettled += mapCount.get("notSettled");
				needConfirmation += mapCount.get("unconfirmed");
				paymentAmountSettled += mapCountAmount.get("amountSettled");
				paymentAmountUnSettled += mapCountAmount.get("amountNotSettled");
				paymentAmountNeedConfirmation += mapCountAmount.get("amountUnconfirmed");
		
				if (!(settled == 0 && unSettled == 0 && needConfirmation == 0)) {
					log.info("Send email, please wait ........");
					sendMail(listFile, settled, unSettled, needConfirmation, paymentAmountSettled, paymentAmountUnSettled, paymentAmountNeedConfirmation, billerCode);
				}
			
			}
		}
	}
	
	Map<String, String> putToParam(Map<String, String> param, Map<String, Integer> paramCount, Map<String, Long> mapCountAmountSettled, Date trxDate, String channelCode, String bankName, String reconcileStatus, String headerImgPath, String reportTitle) {

		param.put("trxDate", DateUtil.convertDateToString(trxDate, "dd-MM-yyyy"));
		param.put("bankName", bankName);
		param.put("channelCode", channelCode);
		param.put("reconcileStatus", reconcileStatus);

		param.put("settled", paramCount.get("settled").toString());
		param.put("notSettled", paramCount.get("notSettled").toString());
		param.put("unconfirmed", paramCount.get("unconfirmed").toString());
		param.put("amountSettled", mapCountAmountSettled.get("amountSettled").toString());
		param.put("amountNotSettled", mapCountAmountSettled.get("amountNotSettled").toString());
		param.put("amountUnconfirmed", mapCountAmountSettled.get("amountUnconfirmed").toString());
		param.put("totalAmount", mapCountAmountSettled.get("totalAmount").toString());
		param.put("totalAmountDenda", mapCountAmountSettled.get("totalAmountDenda").toString());

		param.put("headerImg", headerImgPath);
		param.put("title", reportTitle);

		return param;
	}
	
	public void saveToLogQuery(Date reportTime, String reportDocument, String trxType, String bankName) throws SQLException {

		String sql = "INSERT INTO RECONCILE_REPORT_LOG (REPORT_TIME,REPORT_DOCUMENT,TRANSACTION_TYPE,BANK_NAME) " + "VALUES (?,?,?,?)";

		String sql2 = "SELECT * FROM "
				    + "RECONCILE_REPORT_LOG "
				    + "WHERE BANK_NAME = ? "
				    + "AND REPORT_DOCUMENT = ? "
				    + "AND TRANSACTION_TYPE = ? ";
		Connection con = null;
		PreparedStatement ps, s = null;
		ResultSet rs = null;
		
		try {
			con = dataSourceWebapp.getConnection();
			ps = con.prepareStatement(sql2);
			ps.setString(1, bankName);
			ps.setString(2, reportDocument);
			ps.setString(3, trxType);
			rs = ps.executeQuery();
			if (rs.next()) {
				log.info(reportTime + " Data already exist");
				log.info("Data already exist");
			} else {
				s = con.prepareStatement(sql);
				s.setDate(1, new java.sql.Date(reportTime.getTime()));
				s.setString(2, reportDocument);
				s.setString(3, trxType);
				s.setString(4, bankName);
				s.executeUpdate();
				s.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.trace(e);
			throw e;
		} finally {

			con.close();
		}
	}
	
	private List<ReconcileDto> autoReconcileToPaid(List<ReconcileDto> list, Map<String, Integer> mapCount, Map<String, Long> mapCountAmount, String billerCode) {

		SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);

		List<ReconcileDto> reconciledList = new ArrayList<ReconcileDto>();
		reconciledList.addAll(list);

		String trxDate = DateUtil.convertDateToString(cal.getTime(), "dd-MM-yyyy");

		Date paymentDateSims = null;
		Date invoiceDueDate = null;
		Integer newSettled = 0;
		long amountNewSettled = 0;
		Integer newUnconfirmed = 0;
		long amountNewUnconfirmed = 0;

		log.info("Auto Reconcile: "+reconciledList.size());
		
		for (int i = 0; i < reconciledList.size(); i++) {

			log.info("MT940: "+reconciledList.get(i).getMt940Status().toString());
			log.info("SIMS: "+reconciledList.get(i).getSimsStatus().toString());
			log.info("Trx ID: "+reconciledList.get(i).getTrxId().toString());
			
			if (reconciledList.get(i).getMt940Status().equalsIgnoreCase("Paid") && (reconciledList.get(i).getSimsStatus().equalsIgnoreCase("Unpaid") || reconciledList.get(i).getSimsStatus().equalsIgnoreCase("Cancelled"))
					&& (reconciledList.get(i).getTrxId() != null && reconciledList.get(i).getTrxId().equalsIgnoreCase("-"))) {

				log.info("Auto Reconcile Invoice No : " + reconciledList.get(i).getInvoiceNo());
				
				try {
					if(billerCode.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)){
						externalBillingSystem.updateInvoiceEod(reconciledList.get(i).getInvoiceNo(), cal.getTime(), "Auto Reconcile BHP By WebAdmin");
					}else if(billerCode.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)){
						reorChannel.payment(reconciledList.get(i), cal.getTime(), "Auto Reconcile REOR By WebAdmin");
					}else if(billerCode.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)){
						unarChannel.payment(reconciledList.get(i), cal.getTime(), "Auto Reconcile UNAR By WebAdmin");	
					}else if(billerCode.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)){
						iarChannel.payment(reconciledList.get(i), cal.getTime(), "Auto Reconcile IAR By WebAdmin");
					}else if(billerCode.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)){
						ikrapChannel.payment(reconciledList.get(i), cal.getTime(), "Auto Reconcile IKRAP By WebAdmin");
					}else if(billerCode.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)){
						sertifikasiChannel.payment(reconciledList.get(i), cal.getTime(), "Auto Reconcile SERTIFIKASI By WebAdmin");
					}else if(billerCode.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)){
						pengujianChannel.payment(reconciledList.get(i), cal.getTime(), "Auto Reconcile PENGUJIAN By WebAdmin");
					}else if(billerCode.equalsIgnoreCase(Constants.BillerConstants.KALIBRASI_CODE)){
						klbsiChannel.payment(reconciledList.get(i), cal.getTime(), "Auto Reconcile KALIBRASI By WebAdmin");
					}
	
					if (reconciledList.get(i).getInvoiceDendaNo() != null && !reconciledList.get(i).getInvoiceDendaNo().equalsIgnoreCase("-")) {
						reconciledList.get(i).setStatusDenda("Cancelled");
					}
	
					reconciledList.get(i).setSimsStatus("Paid");
					reconciledList.get(i).setPaymentDateSims(trxDate);
	
					if (reconciledList.get(i).getInvoiceDueDate() != null && !reconciledList.get(i).getInvoiceDueDate().equals("-")) {
	
						try {
							if(reconciledList.get(i).getPaymentDateSims().contains("-")) {
								paymentDateSims = formatter1.parse(reconciledList.get(i).getPaymentDateSims());
							}else {
								paymentDateSims = formatter2.parse(reconciledList.get(i).getPaymentDateSims());
							}
							if(reconciledList.get(i).getInvoiceDueDate().contains("-")) {
								invoiceDueDate 	= formatter1.parse(reconciledList.get(i).getInvoiceDueDate());
							}else {
								invoiceDueDate 	= formatter2.parse(reconciledList.get(i).getInvoiceDueDate());
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	
						if (invoiceDueDate.compareTo(paymentDateSims) >= 0) {
							reconciledList.get(i).setReconcileStatus("Settled");
							if (reconciledList.get(i).getInvoiceDendaNo() != null && reconciledList.get(i).getInvoiceDendaNo().equalsIgnoreCase("-") && reconciledList.get(i).getStatusDenda().equalsIgnoreCase("Paid")) {
								amountNewSettled = amountNewSettled + Long.parseLong(reconciledList.get(i).getTrxAmountDenda().replaceAll("(\\D)", ""));
							} else {
								amountNewSettled = amountNewSettled + Long.parseLong(reconciledList.get(i).getTrxAmount().replaceAll("(\\D)", ""));
							}
							newSettled++;
						} else {
							reconciledList.get(i).setReconcileStatus("Need Confirmation/Manual Payment");
							if (reconciledList.get(i).getInvoiceDendaNo() != null && reconciledList.get(i).getInvoiceDendaNo().equalsIgnoreCase("-") && reconciledList.get(i).getStatusDenda().equalsIgnoreCase("Paid")) {
								amountNewUnconfirmed = amountNewUnconfirmed + Long.parseLong(reconciledList.get(i).getTrxAmountDenda().replaceAll("(\\D)", ""));
							} else {
								amountNewUnconfirmed = amountNewUnconfirmed + Long.parseLong(reconciledList.get(i).getTrxAmount().replaceAll("(\\D)", ""));
							}
							newUnconfirmed++;
						}
					}
				}catch(Exception e) {
					log.error("Error when auto reconcile ", e);
				}
			}
		}

		log.info("Auto Reconcile SUCCESS !");
		mapCount.put("settled", mapCount.get("settled") + newSettled);
		mapCount.put("notSettled", mapCount.get("notSettled") - newSettled);
		mapCount.put("settledReport", mapCount.get("settled") + newSettled);
		mapCount.put("notSettledReport", mapCount.get("notSettled") - newSettled);

		mapCount.put("unconfirmed", mapCount.get("unconfirmed") + newUnconfirmed);
		mapCount.put("notSettled", mapCount.get("notSettled") - newUnconfirmed);
		mapCount.put("unconfirmedReport", mapCount.get("unconfirmed") + newUnconfirmed);
		mapCount.put("notSettledReport", mapCount.get("notSettled") - newUnconfirmed);

		mapCountAmount.put("amountSettled", mapCountAmount.get("amountSettled") + amountNewSettled);
		mapCountAmount.put("amountNotSettled", mapCountAmount.get("amountNotSettled") - amountNewSettled);
		mapCountAmount.put("amountSettledReport", mapCountAmount.get("amountSettled") + amountNewSettled);
		mapCountAmount.put("amountNotSettledReport", mapCountAmount.get("amountNotSettled") - amountNewSettled);

		mapCountAmount.put("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed") + amountNewUnconfirmed);
		mapCountAmount.put("amountNotSettled", mapCountAmount.get("amountNotSettled") - amountNewUnconfirmed);
		mapCountAmount.put("amountUnconfirmedReport", mapCountAmount.get("amountUnconfirmed") + amountNewUnconfirmed);
		mapCountAmount.put("amountNotSettledReport", mapCountAmount.get("amountNotSettled") - amountNewUnconfirmed);

		log.info("Settled: " + mapCount.get("settled"));
		log.info("Unsettled: " + mapCount.get("notSettled"));
		log.info("Need Confirmation: " + mapCount.get("unconfirmed"));

		log.info("Amount Settled: Rp " + mapCountAmount.get("amountSettled"));
		log.info("Amount Unsettled: Rp " + mapCountAmount.get("amountNotSettled"));
		log.info("Amount Need Confirmation: Rp " + mapCountAmount.get("amountUnconfirmed"));

		return reconciledList;
	}
	
	public boolean sendMail(Map<String, String> listFile, Integer settled, Integer unSettled, Integer needConfirmation, Long paymentSettled, Long paymentUnSettled, Long paymentNeedConfirmation, String endpointCode) {
		DecimalFormat numFormat = new DecimalFormat("#,###,###.00");

		Integer totalInvoice = settled + unSettled + needConfirmation;
		Long totalAmount = paymentNeedConfirmation + paymentSettled + paymentUnSettled;

		String emailList = systemParameter.findParamValueByParamName("webadmin.reconcileEod.emailList");
		String smtpHost = systemParameter.findParamValueByParamName("webadmin.reconcileEod.smtpHost");
		String smtpAuth = systemParameter.findParamValueByParamName("webadmin.reconcileEod.smtpAuth");
		String smptpPort = systemParameter.findParamValueByParamName("webadmin.reconcileEod.smtpPort");
		String mailServer = systemParameter.findParamValueByParamName("webadmin.reconcileEod.mailServer.mail");
		String passwordMailServer = systemParameter.findParamValueByParamName("webadmin.reconcileEod.mailServer.password");

		String endpointName = getEndpointName(endpointCode);

		SendMail mail = new SendMail();
		mail.setSubject("Reconcile Daily Report " + (endpointName != null ? endpointName : ""));
		mail.setUsername(mailServer);
		mail.setPassword(passwordMailServer);
		mail.setListFile(listFile);
		mail.setToEmail(emailList.split(","));
		mail.setSmtpHost(smtpHost);
		mail.setSmtpAuth(smtpAuth);
		mail.setSmtpPort(smptpPort);
		mail.setText("<h1>Reconcile Report</h1>" + "<table border='1'>" + "<tr>" + "<th>Status</th>" + "<th>Total Invoice</th>" + "<th>Total Amount</th>" + "</tr>" + "<tr>" + "<td>Settled</td> " + "<td align='right'>" + settled + "</td> " + "<td align='right'>"
				+ (paymentSettled != 0 ? numFormat.format(paymentSettled) : 0) + "</td> " + "</tr>" + "<tr>" + "<td>Unsettled</td>" + "<td align='right'>" + unSettled + "</td>" + "<td align='right'>" + (paymentUnSettled != 0 ? numFormat.format(paymentUnSettled) : 0) + "</td>" + "</tr>" + "<tr>"
				+ "<td>Need Confirmation</td>" + "<td align='right'>" + needConfirmation + "</td>" + "<td align='right'>" + (paymentNeedConfirmation != 0 ? numFormat.format(paymentNeedConfirmation) : 0) + "</td>" + "</tr>" + "<tr>" + "<td>Total</td>" + "<td align='right'>" + totalInvoice + "</td>"
				+ "<td align='right'>" + (totalAmount != 0 ? numFormat.format(totalAmount) : 0) + "</td>" + "</tr>" + "</table>");
		mail.setHtml(true);

		return mail.send();
	}
	
	public String getEndpointName(String endpointCode) {
		String sql = "SELECT name FROM endpoints WHERE code = ? ";
		Connection con = null;
		PreparedStatement stat = null;
		ResultSet rs = null;

		String endpointName = null;

		try {
			con = dataSource.getConnection();
			stat = con.prepareStatement(sql);
			stat.setString(1, endpointCode);
			rs = stat.executeQuery();
			while (rs.next()) {
				endpointName = rs.getString("name");
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return endpointName;
	}
}
