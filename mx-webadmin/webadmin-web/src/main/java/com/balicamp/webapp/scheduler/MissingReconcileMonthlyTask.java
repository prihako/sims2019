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
import com.balicamp.webapp.thread.DoGetReconcileListByMt940NewMonthlyMissing;
import com.balicamp.webapp.thread.DoGetReconcileListByMt940NewMonthlyThread;
import com.balicamp.webapp.thread.DoGetReconcileListByMt940NewThread;
import com.balicamp.webapp.util.SendMail;

@SuppressWarnings("serial")
public class MissingReconcileMonthlyTask extends HttpServlet {
	protected final Log log = LogFactory.getLog(getClass());
	private FTPManager ftpManager;
	private DataSource dataSource;
	private DataSource dataSourceWebapp;
	private String listMissingMonths;
	@Autowired
	public SystemParameterManager systemParameter;

	@Autowired
	public TransactionLogsManager trxLogManager;

	@Autowired
	public ExternalBillingSystemManagerImpl externalBillingSystem;

	@Autowired
	public ArmgmtManagerImpl armgmtManagerImpl;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDataSourceWebapp(DataSource dataSourceWebapp) {
		this.dataSourceWebapp = dataSourceWebapp;

	}

	public void setFtpManager(FTPManager ftpManager) {
		this.ftpManager = ftpManager;
	}
	
	public void setListMissingMonths(String listMissingMonths) {
		this.listMissingMonths = listMissingMonths;
	}

	public void init() throws ServletException {
		super.init();
	}

	public void reconcileMonthly() throws FileNotFoundException, JRException,
			IOException, ServletException, SQLException {
		
		log.info("-------------------MISSING MONTHLY RECONCILE RUNNING----------------------");

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
		log.info("Monthly Missing recon run");
		String [] missingMonths = listMissingMonths.split(",");
		while (billerIterator.hasNext()) {
			String biller = billerIterator.next();
			try {
				DoGetReconcileListByMt940NewMonthlyMissing thread = new DoGetReconcileListByMt940NewMonthlyMissing(
						systemParameter,
						trxLogManager,
						armgmtManagerImpl,
						dataSourceWebapp,
						dataSource,
						biller, 
						channelList, 
						missingMonths);
				thread.doGetReconcileListByMt940EodNew();
			}catch(Exception e) {
				log.error("Monthly recon failed for biller : " + biller , e);
			}
		}
		log.info("------------Monthly Missing recon finish--------------");
		
	}

}
