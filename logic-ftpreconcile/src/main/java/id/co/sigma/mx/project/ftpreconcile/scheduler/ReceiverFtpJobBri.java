package id.co.sigma.mx.project.ftpreconcile.scheduler;

import id.co.sigma.mx.project.ftpreconcile.model.JobMT940;
import id.co.sigma.mx.project.ftpreconcile.process.ExportToXlsBri;
import id.co.sigma.mx.project.ftpreconcile.process.ReceiverFTPBri;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ReceiverFtpJobBri extends QuartzJobBean{

	protected static transient Logger logger = Logger.getLogger(ReceiverFtpJobBri.class);

	private ReceiverFTPBri receiverFTP;
	private ExportToXlsBri exportToXls;
	private DataSource dataSource;
	private static boolean isRunning = false;

	public void setReceiverFTP(ReceiverFTPBri receiverFTP) {
		this.receiverFTP = receiverFTP;
	}
	
	public void setExportToXls(ExportToXlsBri exportToXls) {
		this.exportToXls = exportToXls;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Calendar startDate = Calendar.getInstance();

		if (!isRunning) {
			isRunning = true;
			logger.info("bri, Message Processing starts at: ".concat(startDate
					.getTime().toString()));
		} else {
			logger.info("bri, Previous Message Processing still running at "
					+ startDate.getTime().toString() + ", force to stop and start new one.");
			logger.info("bri, rerun processing");
			isRunning = true;
		}
		
		List<JobMT940> listJob = getJobs();
				
		while (isRunning) {
			try {
				
				int i =0;
				for(JobMT940 job : listJob){
					i++;
					logger.info("-------------------bri, Jobs Number : " + i + " Running----------------------");
					receiverFTP.execute(job.getTypeTrx(), job.getIsSameAccountNo(), job.getFilePattern(), job.getTransactionCode(), job.getEndpointCode(), "bri");
					exportToXls.execute(job.getAccountNo(), job.getTypeTrx(), job.getTransactionCode(), job.getIsSameAccountNo(), "bri");
				}

				Calendar endDate = Calendar.getInstance();
				logger.info("bri, Message Processing completes at: " + endDate.getTime()
						+ ", takes "
						+ (endDate.getTimeInMillis() - startDate.getTimeInMillis())
						+ " ms");
				isRunning = false;
			} catch (Exception e) {
				e.printStackTrace();
				logger.fatal(e);
				isRunning = false;
				break;
			}
		}
	}
	
	public List<JobMT940> getJobs(){
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		List<JobMT940> jobs = new ArrayList<JobMT940>();
		
		try {
			c = dataSource.getConnection();
			String sql = "SELECT * from job_mt940_bri";
			s = c.prepareStatement(sql);
			
			if(s.execute()) {
				rs = s.getResultSet();
				while (rs.next()) {
					String accountNo = rs.getString("ACCOUNT_NO");
					String isSameAccountNo = rs.getString("IS_SAME_ACCOUNT_NO");;
					int typeTrx = rs.getInt("TYPE_TRX");
					String transactionCode = rs.getString("TRANSACTION_CODE");
					String file_pattern = rs.getString("FILE_PATTERN");
					String endpointCode = rs.getString("ENDPOINT_CODE");
					JobMT940 job = new JobMT940(accountNo, isSameAccountNo, typeTrx, transactionCode, file_pattern);
					job.setEndpointCode(endpointCode);
					jobs.add(job);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}finally {
			if(s != null) {
				try {
					s.close();
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		
		return jobs;
	}
}
