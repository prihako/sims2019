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
import com.balicamp.service.impl.iar.IarManagerImpl;
import com.balicamp.service.impl.ikrap.IkrapManagerImpl;
import com.balicamp.service.impl.kalibrasi.KalibrasiManagerImpl;
import com.balicamp.service.impl.pengujian.PengujianManagerImpl;
import com.balicamp.service.impl.pengujian.SertifikasiManagerImpl;
import com.balicamp.service.impl.reor.ReorManagerImpl;
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
import com.balicamp.webapp.action.report.XlstoStringConverter;
import com.balicamp.webapp.ftp.FTPManager;
import com.balicamp.webapp.thread.DoGetReconcileListByMt940New;
import com.balicamp.webapp.thread.DoGetReconcileListByMt940NewThread;
import com.balicamp.webapp.util.SendMail;

@SuppressWarnings("serial")
public class ReconcileEodTask extends HttpServlet {
	protected final Log log = LogFactory.getLog(getClass());
	private FTPManager ftpManager;
	private DataSource dataSource;
	private DataSource dataSourceWebapp;
	@Autowired
	public SystemParameterManager systemParameter;

	@Autowired
	public TransactionLogsManager trxLogManager;
	
	@Autowired
	public ArmgmtManagerImpl armgmtManagerImpl;

	@Autowired
	public ExternalBillingSystemManagerImpl externalBillingSystem;
	
	@Autowired
	public ReorManagerImpl reorManagerImpl;
	
	@Autowired
	public IarManagerImpl iarManagerImpl;
	
	@Autowired
	public IkrapManagerImpl ikrapManagerImpl;
	
	@Autowired
	public SertifikasiManagerImpl sertifikasiManagerImpl;
	
	@Autowired
	public PengujianManagerImpl pengujianManagerImpl;
	
	@Autowired
	public KalibrasiManagerImpl kalibrasiManagerImpl;
	
	@Autowired
	private ReorChannel reorChannel;
	
	@Autowired
	private UnarChannel unarChannel;
	
	@Autowired
	private IarChannel iarChannel;
	
	@Autowired
	private KlbsiChannel klbsiChannel;
	
	@Autowired
	private IkrapChannel ikrapChannel;
	
	@Autowired
	private PengujianChannel pengujianChannel;
	
	@Autowired
	private SertifikasiChannel sertifikasiChannel;

	@SuppressWarnings("unused")
	private XlstoStringConverter xlsFile;

	public ReconcileEodTask() {
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
			log.info("-----------" + fileNames[i] + "---------");
		}
		log.info("---------End Of List Of Files-----------");
	}

	public void init() throws ServletException {
		super.init();
	}

	public void reconcileEod() throws FileNotFoundException, JRException, IOException, ServletException, SQLException, ParseException {
		
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
		Date start = new Date();
		log.info("RECONCILE EOD START");
		Iterator<String> billerIterator = billerList.iterator();
		while (billerIterator.hasNext()) {			

			Calendar reconcileCalendar 		= Calendar.getInstance();
			reconcileCalendar.setTime(new Date());
			reconcileCalendar.add(Calendar.DATE, -1);
			
			Calendar createdOnCalendar 		= Calendar.getInstance();
			createdOnCalendar.setTime(new Date());
			
			String biller = billerIterator.next();
			try {
				DoGetReconcileListByMt940New task = new DoGetReconcileListByMt940New(
						systemParameter,
						trxLogManager,
						armgmtManagerImpl,
						dataSourceWebapp,
						reorChannel,
						unarChannel,
						iarChannel,
						klbsiChannel,
						ikrapChannel,
						pengujianChannel,
						sertifikasiChannel,
						dataSource,
						externalBillingSystem,
						sertifikasiManagerImpl,
						biller, 
						channelList,
						reconcileCalendar,
						createdOnCalendar);
				task.doGetReconcileListByMt940EodNew();
			}catch(Exception e) {
				log.error("EOD recon failed for biller : " + biller, e);
			}
		}
		Date end = new Date();
		Long diff = end.getTime() - start.getTime();
		log.info("RECONCILE EOD FINISH : " + diff);
		
	}


	public Boolean cekFileInFileSystem(String filepath, String transactionType, String bankName) {
		String errorMessage = null;
		File file 			= new File(filepath);

		if (!file.exists()) {
			errorMessage = "File MT940 " + transactionType + " pada bank " + bankName + " Tidak Ditemukan Pada Path : " + filepath;
			log.info(errorMessage);
			return false;
		}

		return true;
	}
}
