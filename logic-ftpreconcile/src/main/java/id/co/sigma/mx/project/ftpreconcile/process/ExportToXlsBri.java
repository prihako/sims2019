package id.co.sigma.mx.project.ftpreconcile.process;

import id.co.sigma.mx.project.ftpreconcile.constant.PostelConstant;
import id.co.sigma.mx.project.ftpreconcile.model.JobMT940;
import id.co.sigma.mx.project.ftpreconcile.util.WebadminUtilBri;
import id.co.sigma.mx.project.ftpreconcile.util.XlsUtil;
import id.co.sigma.mx.project.ftpreconcile.util.XlsUtilBri;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class ExportToXlsBri {

	private static Logger logger = Logger.getLogger(ExportToXlsBri.class);

	private DataSource dataSource;
	private FTPManagerBri ftpManager;
	private WebadminUtilBri webadminUtil;
	
	public void setWebadminUtil(WebadminUtilBri webadminUtil){
		this.webadminUtil = webadminUtil;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setFtpManager(FTPManagerBri ftpManager) {
		this.ftpManager = ftpManager;
	}

	public void execute(String accountNo, int type, String transactionCode, String sameAccountNo, String bankName) {
		
		logger.info("bri, ExportToXls.execute,  accountNo : " + accountNo);
		logger.info("bri, ExportToXls.execute,  type : " + type);
		logger.info("bri, ExportToXls.execute,  transactionCode : " + transactionCode);
		logger.info("bri, ExportToXls.execute,  bankName : " + bankName);
 
		Map<Integer, Map<String, Object>> sheetsDetail = new HashMap<Integer, Map<String, Object>>();
		Map<Integer, Integer> indexTransactionPaymentSheet = new HashMap<Integer, Integer>();
		indexTransactionPaymentSheet.put(4, 4);
		indexTransactionPaymentSheet.put(5, 5);
		indexTransactionPaymentSheet.put(6, 6);

		String directory = getDirectoryPath(accountNo, type);
		
		String endpointCode = getEndpointCode(accountNo, type);
		
		logger.info("bri, ExportToXls.execute, endpointCode : " + endpointCode);

		if (directory != null) {
			try {
				List<Date> UnprocessedTransactionDate = getProcessedData(accountNo, bankName);

				for (Date transDate : UnprocessedTransactionDate) {
					// sheet status file valid
					Map<String, Object> sheetFileStatusValid = new HashMap<String, Object>();
					sheetFileStatusValid.put(XlsUtilBri.XLS_SHEET_NAME,
							"Status File - Valid");
					sheetFileStatusValid.put(XlsUtilBri.XLS_SHEET_TITLE_COLUMN,
							"Laporan Harian Status File MT940 - Valid");
					sheetFileStatusValid.put(XlsUtilBri.XLS_DATA,
							getFileStatusValid(transDate, accountNo, bankName));
					sheetsDetail.put(1, sheetFileStatusValid);

					// sheet status file invalid
					Map<String, Object> sheetFileStatusInvalid = new HashMap<String, Object>();
					sheetFileStatusInvalid.put(XlsUtilBri.XLS_SHEET_NAME,
							"Status File - Invalid");
					sheetFileStatusInvalid.put(XlsUtilBri.XLS_SHEET_TITLE_COLUMN,
							"Laporan Harian Status File MT940 - Invalid");
					sheetFileStatusInvalid.put(XlsUtilBri.XLS_DATA,
							getFileStatusInvalid(transDate, accountNo, bankName));
					sheetsDetail.put(2, sheetFileStatusInvalid);

					// sheet proses parsing
					Map<String, Object> sheetParsingProcess = new HashMap<String, Object>();
					sheetParsingProcess.put(XlsUtilBri.XLS_SHEET_NAME,
							"Proses MT940");
					sheetParsingProcess.put(XlsUtilBri.XLS_SHEET_TITLE_COLUMN,
							"Laporan Proses MT940");
					sheetParsingProcess.put(XlsUtilBri.XLS_DATA,
							getParsingProcess(transDate, accountNo, bankName, transactionCode, sameAccountNo));
					sheetsDetail.put(3, sheetParsingProcess);

					// sheet parsing data transaction valid Host To Host
					Map<String, Object> sheetParsingDataHostToHost = new HashMap<String, Object>();
					sheetParsingDataHostToHost.put(XlsUtilBri.XLS_SHEET_NAME,
							"Parsing data trx - Valid h2h");
					sheetParsingDataHostToHost
							.put(XlsUtilBri.XLS_SHEET_TITLE_COLUMN,
									"Laporan Parsing Data Transaksi Pembayaran - Valid Host To Host BRI");
					sheetParsingDataHostToHost.put(XlsUtilBri.XLS_DATA,
							getTransactionDataValidHostToHost(transDate, transactionCode, accountNo, 
									sameAccountNo, bankName, PostelConstant.HOST_TO_HOST, PostelConstant.CREDIT));
					sheetsDetail.put(4, sheetParsingDataHostToHost);
					
					// sheet parsing data transaction valid non Host To Host
					Map<String, Object> sheetParsingDataNotHostToHost = new HashMap<String, Object>();
					sheetParsingDataNotHostToHost.put(XlsUtilBri.XLS_SHEET_NAME,
							"Parsing data trx - Valid NonH2H");
					sheetParsingDataNotHostToHost
							.put(XlsUtilBri.XLS_SHEET_TITLE_COLUMN,
									"Laporan Parsing Data Transaksi Pembayaran - Valid Non Host To Host BRI");
					sheetParsingDataNotHostToHost.put(XlsUtilBri.XLS_DATA,
							getTransactionDataValidNotHostToHost(transDate, accountNo, bankName, PostelConstant.NOT_HOST_TO_HOST));
					sheetsDetail.put(5, sheetParsingDataNotHostToHost);
					
					// sheet parsing data transaction Debet
					Map<String, Object> sheetParsingDataDebit = new HashMap<String, Object>();
					sheetParsingDataDebit.put(XlsUtil.XLS_SHEET_NAME,
							"Parsing data trx - MPN Billing");
					sheetParsingDataDebit
							.put(XlsUtil.XLS_SHEET_TITLE_COLUMN,
									"Laporan Parsing Data Transaksi MPN Billing");
					sheetParsingDataDebit.put(XlsUtil.XLS_DATA, 
							getTransactionDataValidDebit(transDate, transactionCode, accountNo, sameAccountNo, 
									bankName, PostelConstant.DEBIT));
					sheetsDetail.put(6, sheetParsingDataDebit);

					// sheet parsing data transaction invalid
					Map<String, Object> sheetParsingDataInvalid = new HashMap<String, Object>();
					sheetParsingDataInvalid.put(XlsUtilBri.XLS_SHEET_NAME,
							"Parsing data trx - Invalid");
					sheetParsingDataInvalid
							.put(XlsUtilBri.XLS_SHEET_TITLE_COLUMN,
									"Laporan Parsing Data Transaksi Pembayaran - Invalid");
					sheetParsingDataInvalid.put(XlsUtilBri.XLS_DATA, 
							getTransactionDataInvalid(transDate, transactionCode, accountNo, 
									sameAccountNo, bankName, PostelConstant.CREDIT));
					sheetsDetail.put(7, sheetParsingDataInvalid);

					SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
					String fileName = "MT940" + sd.format(transDate) + endpointCode +  ".xls";

					XlsUtilBri.createXls(sheetsDetail, directory, fileName, indexTransactionPaymentSheet);
					
					JobMT940 job = getJobsMT940Object(accountNo, type);
					if(!webadminUtil.isEverInserted(fileName, job.getTransactionType(), bankName)){
						webadminUtil.insertData(transDate, fileName, job.getTransactionType(), bankName);
					}
				}

			} catch (Exception e) {
				logger.error("error : ", e);
			}
		}

	}

	private String getDirectoryPath(String accountNo, int type) {
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");

		String subdirectory = getFolderName(accountNo, type);
		subdirectory = subdirectory + "/";

		// diubah karena, butuh d list ditabel Transaction_report milik webadmin
		// agar proses list lebih mudah
		String directory = ftpManager.getFtpReportDirectory() + subdirectory;

		File file = new File(directory);
		if (!file.exists()) {
			if (file.mkdirs()) {
				logger.info("bri, Directory is created!");
			} else {
				logger.fatal("bri, Failed to create directory!");
				return null;
			}
		}

		logger.info("bri, directory : " + directory);
		return directory;
	}

	private List<Date> getProcessedData(String accountNo, String bankName) throws SQLException {
		List<Date> transDates = new ArrayList<Date>();

		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;

		String sql = "select transaction_date from listener_log "
				+ "where to_date(received_date) = ? " 
				+ "and filename_transaction like ? "
				+ "and bank_name = ? "
				+ "order by transaction_date";

		java.util.Date utilDate = Calendar.getInstance().getTime();
		java.sql.Date now = new java.sql.Date(utilDate.getTime());

		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, now);
			s.setString(2, "%" + accountNo + "%");
			s.setString(3, bankName);
			rs = s.executeQuery();
			while (rs.next()) {
				transDates.add(rs.getDate("transaction_date"));
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}

		return transDates;
	}

	private List<Object[]> getFileStatusValid(Date transDate, String accountNo, String bankName)
			throws SQLException {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[] { "No.", "Nama File", "Tanggal Penerimaan",
				"Tanggal Transaksi" });

		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;

		int no = 1;
		String sql = "select filename, "
				+ "to_char(received_date,'dd-MON-YYYY hh:mi:ss') received_date, "
				+ "to_char(transaction_date,'dd-MON-YYYY') trans_date "
				+ "from listener_log ll "
				+ "left join transaction_file tf on tf.listener_log_id = ll.listener_log_id "
				+ "where to_date(ll.received_date) = ? "
				+ "and transaction_date = ? " + "and listener_status = ? "
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "order by trans_date desc";

		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, PostelConstant.STATUS_VALID);
			s.setString(4, "%" + accountNo + "%");
			s.setString(5, bankName);
			rs = s.executeQuery();
			while (rs.next()) {
				data.add(new Object[] { no++, rs.getString("filename"),
						rs.getString("received_date"),
						rs.getString("trans_date") });
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}

		return data;
	}

	private List<Object[]> getFileStatusInvalid(Date transDate, String accountNo, String bankName)
			throws SQLException {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[] { "No.", "Nama File", "Tanggal Penerimaan",
				"Tanggal Transaksi", "Status File" });

		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;

		int no = 1;
		String sql = "select filename, "
				+ "to_char(received_date,'dd-MON-YYYY hh:mi:ss') received_date, "
				+ "to_char(transaction_date,'dd-MON-YYYY') trans_date, "
				+ "decode(listener_status, 'VA', "
				+ "'Valid', 'EP', 'Gagal parsing', "
				+ "'NA', 'File tidak ditemukan', "
				+ "'FD', 'FTP server down', "
				+ "'IG', 'Tidak digunakan', "
				+ "'DI', 'Tanggal tidak valid', "
				+ "'FI', 'Tanggal antara tag 20 dan nama file berbeda', "
				+ "'DN', 'Tanggal tidak ditemukan', "
				+ "'AI', 'Account tidak valid', "
				+ "'AN', 'Account tidak ditemukan', "
				+ "'CN', 'Content file tidak ditemukan', "
				+ "'IF', 'File tidak utuh // corrupt', "
				+ "'Error decrypt') listener_status "
				+ "from listener_log ll "
				+ "left join transaction_file tf on tf.listener_log_id = ll.listener_log_id "
				+ "where to_date(ll.received_date) = ? "
				+ "and transaction_date = ? " 
				+ "and listener_status != ? "
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "order by trans_date desc";

		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, PostelConstant.STATUS_VALID);
			s.setString(4, "%" + accountNo + "%");
			s.setString(5, bankName);
			rs = s.executeQuery();
			while (rs.next()) {
				data.add(new Object[] { no++, rs.getString("filename"),
						rs.getString("received_date"),
						rs.getString("trans_date"),
						rs.getString("listener_status") });
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}

		return data;
	}

	private List<Object[]> getParsingProcess(Date transDate, String accountNo, String bankName,
			String transactionCode, String sameAccountNo)
			throws SQLException {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[] { "No.", "Id Parsing", "Nama File",
				"Tanggal Proses", "Jumlah Transaksi", "Nominal Total",
				"Jumlah Host To Host","Nominal H2H", 
				"Jumlah Non Host To Host", "Nominal Non H2H",
				"Jumlah Debet", "Nominal Debet",
				"Jumlah Gagal", "Status Parsing" });
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;

		int no = 1;
		String sql = "select parser_log_id, "
				+ "filename, "
				+ "to_char(parse_date,'dd-MON-YYYY') parse_date, "
				+ "total_record, "
				+ "total_processed, "
				+ "total_host_to_host, "
				+ "total_not_host_to_host, "
				+ "total_failed, "
				+ "decode(parse_status, 'S', 'Sukses', 'Gagal') parse_status "
				+ "from parser_log pl "
				+ "join transaction_file tf on pl.file_id = tf.file_id "
				+ "join listener_log ll on ll.listener_log_id = tf.listener_log_id "
				+ "where to_date(pl.parse_date) = ? "
				+ "and transaction_date = ? " 
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "order by filename";
		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, "%" + accountNo + "%");
			s.setString(4, bankName);
			rs = s.executeQuery();
			
			Double totalValidH2H = getTotalPaymentValidHostToHost(transDate, transactionCode, accountNo, 
					sameAccountNo, bankName, PostelConstant.HOST_TO_HOST, PostelConstant.CREDIT);
			
			Double totalValidNotH2H = getTotalPaymentValidNotHostToHost(transDate, accountNo, bankName, 
					PostelConstant.NOT_HOST_TO_HOST);
			
			Map <String, Object> resultDebit = getTotalDataAndTotalPaymentValidDebit(transDate, 
					transactionCode, accountNo, sameAccountNo, 
					bankName, PostelConstant.HOST_TO_HOST, PostelConstant.DEBIT);
			
			Double totalPaymentDebit = Double.valueOf(resultDebit.get("totalPayment").toString());
			Integer totalDebit = Integer.valueOf(resultDebit.get("totalData").toString());
			
			Double totalPayment = totalValidH2H + totalValidNotH2H + totalPaymentDebit;
			
			Integer totalInvalid = getTotalDataInvalid(transDate, transactionCode, accountNo, 
					sameAccountNo, bankName, PostelConstant.CREDIT);
			
			while (rs.next()) {
				data.add(new Object[] { no++, rs.getLong("parser_log_id"),
						rs.getString("filename"), rs.getString("parse_date"),
						rs.getInt("total_record"), totalPayment,
						rs.getInt("total_host_to_host"), totalValidH2H,
						rs.getInt("total_not_host_to_host"), totalValidNotH2H,
						totalDebit, totalPaymentDebit,
						totalInvalid, rs.getString("parse_status") });
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}

		return data;
	}

	private List<Object[]> getTransactionDataValidHostToHost(Date transDate, String transactionCode, 
			String accountNo, String sameAccountNo, String bankName, String paymentType,
			String transactionType)
			throws SQLException {
		List<Object[]> data = new ArrayList<Object[]>();
		
		List<Object[]> dataResult = new ArrayList<Object[]>();

		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		double paymentAmount = 0;
		String namaFile = "";
		String tanggalParsing = "";

		String sql;
		int no = 1;
		
//		penambahan kode unit kerja pada report MT940 BRI
		data.add(new Object[] { "No.", 
				"Tanggal Transaksi", "Id Klien", "No. Invoice", "Pembayaran Melalui", "Kode Unit Kerja", "Nilai Bayar" });

		sql = "select filename,"
				+ "to_char(parse_date,'dd-MON-YYYY') parse_date, "
				+ "to_char(t.transaction_date,'dd-MON-YYYY') transaction_date, "
				+ "t.client_id, "
				+ "t.invoice_id, "
				+ "t.payment_channel, "
				+ "t.branch_code, "
				+ "t.transaction_amount, "
				+ "t.error_desc "
				+ "from transaction t "
				+ "join transaction_file tf on t.file_id = tf.file_id "
				+ "join parser_log pl on tf.file_id = pl.file_id "
				+ "join listener_log ll on ll.listener_log_id = tf.listener_log_id "
				+ "where to_date(pl.parse_date) = ? "
				+ "and ll.transaction_date = ? " 
				+ "and error_desc is null "
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "and payment_type = ? "
				+ "and t.transaction_Type = ? ";
				
		if( sameAccountNo != null ){
			if(sameAccountNo.equalsIgnoreCase("y") || sameAccountNo.equalsIgnoreCase("yes") || sameAccountNo.equalsIgnoreCase("1")){
				sql += "and t.transaction_code = '" + transactionCode + "' ";
			}
		}
		
		String orderByQuery = "order by filename";
		sql += orderByQuery;

		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, "%" + accountNo + "%");
			s.setString(4, bankName);
			s.setString(5, paymentType);
			s.setString(6, transactionType);
			rs = s.executeQuery();
			while (rs.next()) {
				
				namaFile = rs.getString("filename");
				tanggalParsing = rs.getString("parse_date");
				
				try{
					paymentAmount += rs.getDouble("transaction_amount");
				}catch(Exception e){
					logger.error(e);
				}

				data.add(new Object[] { no++, 
						rs.getString("transaction_date"),
						rs.getString("client_id"), rs.getString("invoice_id"), rs.getString("payment_channel"),
//						penambahan kode cabang untuk BRI - 8/8/2018 - hhy
						rs.getString("branch_code"),
						rs.getDouble("transaction_amount") });
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}
		
		dataResult.add(new Object[] {"","", "", "", "", "",""});
		dataResult.add(new Object[] {"","", "", "", "","Total Invoice", data.size() - 1});
		dataResult.add(new Object[] {"","", "", "", "","Total Pembayaran", paymentAmount});
		dataResult.add(new Object[] {"","", "", "", "","", ""});
		dataResult.add(new Object[] {"","", "", "", "","Nama File", namaFile});
		dataResult.add(new Object[] {"","", "", "", "","Tanggal Penarikan Data", tanggalParsing});
		dataResult.addAll(data);


		return dataResult;
	}
	
	private Double getTotalPaymentValidHostToHost(Date transDate, String transactionCode, String accountNo, 
			String sameAccountNo, String bankName, String paymentType, String transactionType)
			throws SQLException {
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		double paymentAmount = 0;

		String sql;
		
		sql = "select sum(transaction_amount) transaction_amount "
				+ "from transaction t "
				+ "join transaction_file tf on t.file_id = tf.file_id "
				+ "join parser_log pl on tf.file_id = pl.file_id "
				+ "join listener_log ll on ll.listener_log_id = tf.listener_log_id "
				+ "where to_date(pl.parse_date) = ? "
				+ "and ll.transaction_date = ? " 
				+ "and error_desc is null "
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "and payment_type = ? "
				+ "and t.transaction_Type = ? ";
				
		if( sameAccountNo != null ){
			if(sameAccountNo.equalsIgnoreCase("y") || sameAccountNo.equalsIgnoreCase("yes") || sameAccountNo.equalsIgnoreCase("1")){
				sql += "and t.transaction_code = '" + transactionCode + "' ";
			}
		}

		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, "%" + accountNo + "%");
			s.setString(4, bankName);
			s.setString(5, paymentType);
			s.setString(6, transactionType);
			rs = s.executeQuery();
			while (rs.next()) {
				
				try{
					paymentAmount += rs.getDouble("transaction_amount");
				}catch(Exception e){
					logger.error(e);
				}

			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}
		
		return paymentAmount;
	}

	private List<Object[]> getTransactionDataValidNotHostToHost(Date transDate, String accountNo, 
			String bankName, String paymentType)
			throws SQLException {
		List<Object[]> data = new ArrayList<Object[]>();
		List<Object[]> dataResult = new ArrayList<Object[]>();

		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		double paymentAmount = 0;
		String namaFile = "";
		String tanggalParsing = "";

		String sql;
		int no = 1;

//		penambahan kode unit kerja pada report MT940 BRI
		data.add(new Object[] { "No.", 
				"Tanggal Transaksi","Raw Data 86", "Id Klien", "No. Invoice", "Kantor Cabang", "Kode Unit Kerja", "Nilai Bayar" });

		sql = "select filename,"
				+ "to_char(parse_date,'dd-MON-YYYY') parse_date, "
				+ "to_char(t.transaction_date,'dd-MON-YYYY') transaction_date, "
				+ "t.raw_transaction_msg, "
				+ "t.client_id, "
				+ "t.invoice_id, "
				+ "t.payment_channel, "
				+ "t.branch_code, "
				+ "t.transaction_amount, "
				+ "t.error_desc "
				+ "from transaction t "
				+ "join transaction_file tf on t.file_id = tf.file_id "
				+ "join parser_log pl on tf.file_id = pl.file_id "
				+ "join listener_log ll on ll.listener_log_id = tf.listener_log_id "
				+ "where to_date(pl.parse_date) = ? "
				+ "and ll.transaction_date = ? " 
				+ "and error_desc is null "
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "and payment_type = ? ";
		
		String orderByQuery = "order by filename";
		sql += orderByQuery;

		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, "%" + accountNo + "%");
			s.setString(4, bankName);
			s.setString(5, paymentType);
			rs = s.executeQuery();
			while (rs.next()) {
//				String branchCode = rs.getString("branch_code");
				
				namaFile = rs.getString("filename");
				tanggalParsing = rs.getString("parse_date");
				
				String rawTransactionMsg = null;
				if(rs.getString("raw_transaction_msg") != null){
					String rawTransactionMsgs[] = rs.getString("raw_transaction_msg").split("\\|");
				
					if(rawTransactionMsgs.length == 2){
						rawTransactionMsg = rawTransactionMsgs[1];
					}else{
						rawTransactionMsg = rs.getString("raw_transaction_msg");
					}
				}
				
				data.add(new Object[] { no++,
						rs.getString("transaction_date"),
						rs.getString("client_id"), rs.getString("invoice_id"), rs.getString("payment_channel"),
//						penambahan kode cabang untuk BRI - 8/8/2018 - hhy
						rs.getString("branch_code"),
						rs.getDouble("transaction_amount") });
				
//				if(branchCode != null && branchCode.trim().length() > 0){
//					String branchOffice = getBranchOffice(branchCode);
//					data.add(new Object[] { no++, rs.getString("filename"),
//							rs.getString("parse_date"),
//							rs.getString("transaction_date"), rawTransactionMsg != null ? rawTransactionMsg : " ",
//							rs.getString("client_id"), rs.getString("invoice_id"), branchOffice != null ? branchOffice : " - ",
//							rs.getDouble("transaction_amount") });
//				}else{
//					data.add(new Object[] { no++, rs.getString("filename"),
//							rs.getString("parse_date"),
//							rs.getString("transaction_date"), rawTransactionMsg != null ? rawTransactionMsg : " ",
//							rs.getString("client_id"), rs.getString("invoice_id"), "-",
//							rs.getDouble("transaction_amount") });
//				}
				
				try{
					paymentAmount += rs.getDouble("transaction_amount");
				}catch(Exception e){
					logger.error("error : ", e);
				}
				
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}
		
		dataResult.add(new Object[] {"","","", "", "", "", "", ""});
		dataResult.add(new Object[] {"","","", "", "", "", "Total Invoice", data.size() - 1});
		dataResult.add(new Object[] {"","","", "", "", "", "Total Pembayaran", paymentAmount});
		dataResult.add(new Object[] {"","","", "", "", "", "",""});
		dataResult.add(new Object[] {"","","", "", "", "", "Nama File", namaFile});
		dataResult.add(new Object[] {"","","", "", "", "", "Tanggal Penarikan Data", tanggalParsing});
		dataResult.addAll(data);

		return dataResult;

	}
	
	private Double getTotalPaymentValidNotHostToHost(Date transDate, String accountNo, String bankName, String paymentType)
			throws SQLException {

		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		double paymentAmount = 0;

		String sql;

		sql = "select t.transaction_amount "
				+ "from transaction t "
				+ "join transaction_file tf on t.file_id = tf.file_id "
				+ "join parser_log pl on tf.file_id = pl.file_id "
				+ "join listener_log ll on ll.listener_log_id = tf.listener_log_id "
				+ "where to_date(pl.parse_date) = ? "
				+ "and ll.transaction_date = ? " 
				+ "and error_desc is null "
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "and payment_type = ? ";

		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, "%" + accountNo + "%");
			s.setString(4, bankName);
			s.setString(5, paymentType);
			rs = s.executeQuery();
			while (rs.next()) {
				
				try{
					paymentAmount += rs.getDouble("transaction_amount");
				}catch(Exception e){
					logger.error("error : ", e);
				}
				
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}

		return paymentAmount;

	}
	
	private List<Object[]> getTransactionDataValidDebit(Date transDate, String transactionCode, String accountNo, 
			String sameAccountNo, String bankName, String transactionType)
			throws SQLException {
		List<Object[]> data = new ArrayList<Object[]>();
		
		List<Object[]> dataResult = new ArrayList<Object[]>();

		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		double paymentAmount = 0;
		String namaFile = "";
		String tanggalParsing = "";

		String sql;
		int no = 1;
		
//		penambahan kode unit kerja pada report MT940 BRI
		data.add(new Object[] { "No.", 
				"Tanggal Transaksi", "Id Klien", "No. Invoice", "Pembayaran Melalui", "Kode Unit Kerja", "Nilai Bayar" });

		sql = "select filename,"
				+ "to_char(parse_date,'dd-MON-YYYY') parse_date, "
				+ "to_char(t.transaction_date,'dd-MON-YYYY') transaction_date, "
				+ "t.client_id, "
				+ "t.invoice_id, "
				+ "t.payment_channel, "
				+ "t.branch_code, "
				+ "t.transaction_amount, "
				+ "t.error_desc "
				+ "from transaction t "
				+ "join transaction_file tf on t.file_id = tf.file_id "
				+ "join parser_log pl on tf.file_id = pl.file_id "
				+ "join listener_log ll on ll.listener_log_id = tf.listener_log_id "
				+ "where to_date(pl.parse_date) = ? "
				+ "and ll.transaction_date = ? " 
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "and t.transaction_type = ? ";
		
		/*
		if( sameAccountNo != null ){
			if(sameAccountNo.equalsIgnoreCase("y") || sameAccountNo.equalsIgnoreCase("yes") || sameAccountNo.equalsIgnoreCase("1")){
				sql += "and t.transaction_code = '" + transactionCode + "' ";
			}
		}
		*/
		
		String orderByQuery = "order by filename";
		sql += orderByQuery;

		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, "%" + accountNo + "%");
			s.setString(4, bankName);
			s.setString(5, transactionType);
			rs = s.executeQuery();
			while (rs.next()) {
				
				namaFile = rs.getString("filename");
				tanggalParsing = rs.getString("parse_date");
				
				try{
					paymentAmount += rs.getDouble("transaction_amount");
				}catch(Exception e){
					logger.error("error : ", e);
				}

				data.add(new Object[] { no++, 
						rs.getString("transaction_date"),
						rs.getString("client_id"), rs.getString("invoice_id"), rs.getString("payment_channel"),
//						penambahan kode cabang untuk BRI - 8/8/2018 - hhy
						rs.getString("branch_code"),
						rs.getDouble("transaction_amount") });
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}
		
		dataResult.add(new Object[] {"","", "", "", "", "", ""});
		dataResult.add(new Object[] {"","", "", "", "", "Total Invoice", data.size() - 1});
		dataResult.add(new Object[] {"","", "", "", "", "Total Pembayaran", paymentAmount});
		dataResult.add(new Object[] {"","", "", "", "", "", ""});
		dataResult.add(new Object[] {"","", "", "", "", "Nama File", namaFile});
		dataResult.add(new Object[] {"","", "", "", "", "Tanggal Penarikan Data", tanggalParsing});
		
		dataResult.addAll(data);


		return dataResult;
	}
	
	private Map<String, Object> getTotalDataAndTotalPaymentValidDebit(Date transDate, String transactionCode, String accountNo, 
			String sameAccountNo, String bankName, String paymentType, String transactionType)
			throws SQLException {
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		double paymentAmount = 0;
		Map<String, Object> result = new HashMap<String, Object>();
		String sql;
		int no = 0;
		
		sql = "select filename,"
				+ "to_char(parse_date,'dd-MON-YYYY') parse_date, "
				+ "to_char(t.transaction_date,'dd-MON-YYYY') transaction_date, "
				+ "t.client_id, "
				+ "t.invoice_id, "
				+ "t.payment_channel, "
				+ "t.branch_code, "
				+ "t.transaction_amount, "
				+ "t.error_desc "
				+ "from transaction t "
				+ "join transaction_file tf on t.file_id = tf.file_id "
				+ "join parser_log pl on tf.file_id = pl.file_id "
				+ "join listener_log ll on ll.listener_log_id = tf.listener_log_id "
				+ "where to_date(pl.parse_date) = ? "
				+ "and ll.transaction_date = ? " 
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "and t.transaction_type = ? ";
				
//		if( sameAccountNo != null ){
//			if(sameAccountNo.equalsIgnoreCase("y") || sameAccountNo.equalsIgnoreCase("yes") || sameAccountNo.equalsIgnoreCase("1")){
//				sql += "and t.transaction_code = '" + transactionCode + "' ";
//			}
//		}
		
		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, "%" + accountNo + "%");
			s.setString(4, bankName);
			s.setString(5, transactionType);
			rs = s.executeQuery();
			while (rs.next()) {
				
				try{
					paymentAmount += rs.getDouble("transaction_amount");
				}catch(Exception e){
					logger.error("error : ", e);
				}
				no++;
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}
		
		result.put("totalPayment", paymentAmount);
		result.put("totalData", no);

		return result;
	}

	
	private List<Object[]> getTransactionDataInvalid(Date transDate, String transactionCode, String accountNo, 
			String sameAccountNo, String bankName, String transactionType)
			throws SQLException {
		List<Object[]> data = new ArrayList<Object[]>();
		
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;

		String sql;
		int no = 1;

		data.add(new Object[] { "No.", "Nama File", "Tanggal Parsing",
				"Tanggal Transaksi", "Raw Data", "Id Klien", "No. Invoice", "Pembayaran Melalui", "Kode Unit Kerja",
				"Nilai Bayar", "Deskripsi Error" });

		sql = "select filename, "
				+ "to_char(parse_date,'dd-MON-YYYY') parse_date, "
				+ "to_char(t.transaction_date,'dd-MON-YYYY') transaction_date, "
				+ "raw_transaction_msg, "
				+ "t.client_id, "
				+ "t.invoice_id, "
				+ "t.payment_channel, "
				+ "t.branch_code, "
				+ "t.transaction_amount, "
				+ "t.error_desc "
				+ "from transaction t "
				+ "join transaction_file tf on t.file_id = tf.file_id "
				+ "join parser_log pl on tf.file_id = pl.file_id "
				+ "join listener_log ll on ll.listener_log_id = tf.listener_log_id "
				+ "where to_date(pl.parse_date) = ? "
				+ "and ll.transaction_date = ? "
				+ "and error_desc is not null " 
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "and t.transaction_type = ? ";
		
//		if( sameAccountNo != null ){
//			if(sameAccountNo.equalsIgnoreCase("y") || sameAccountNo.equalsIgnoreCase("yes") || sameAccountNo.equalsIgnoreCase("1")){
//				sql += "and t.transaction_code = '" + transactionCode + "' ";
//			}
//		}
		
		String orderByQuery = "order by filename";
		sql += orderByQuery;

		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, "%" + accountNo + "%");
			s.setString(4, bankName);
			s.setString(5, transactionType);
			rs = s.executeQuery();
			while (rs.next()) {
				data.add(new Object[] { no++, rs.getString("filename"),
						rs.getString("parse_date"),
						rs.getString("transaction_date"),
						rs.getString("raw_transaction_msg"),
						rs.getString("client_id"), rs.getString("invoice_id"),
						rs.getString("payment_channel"),
						rs.getString("branch_code"),
						rs.getDouble("transaction_amount"),
						rs.getString("error_desc") });
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}

		return data;
	}
	
	private Integer getTotalDataInvalid(Date transDate, String transactionCode, String accountNo, 
			String sameAccountNo, String bankName, String transactionType)
			throws SQLException {
		Connection con = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		Integer totalData = 0;
		String sql;
		int no = 1;

		sql = "select count(*) total_data "
				+ "from transaction t "
				+ "join transaction_file tf on t.file_id = tf.file_id "
				+ "join parser_log pl on tf.file_id = pl.file_id "
				+ "join listener_log ll on ll.listener_log_id = tf.listener_log_id "
				+ "where to_date(pl.parse_date) = ? "
				+ "and ll.transaction_date = ? "
				+ "and error_desc is not null " 
				+ "and tf.filename like ? "
				+ "and ll.bank_name = ? "
				+ "and t.transaction_type = ? ";
		
//		if( sameAccountNo != null ){
//			if(sameAccountNo.equalsIgnoreCase("y") || sameAccountNo.equalsIgnoreCase("yes") || sameAccountNo.equalsIgnoreCase("1")){
//				sql += "and t.transaction_code = '" + transactionCode + "' ";
//			}
//		}
		
		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, getCurrentDate());
			s.setDate(2, new java.sql.Date(transDate.getTime()));
			s.setString(3, "%" + accountNo + "%");
			s.setString(4, bankName);
			s.setString(5, transactionType);
			rs = s.executeQuery();
			while (rs.next()) {
				totalData = Integer.valueOf(rs.getString("total_data").toString());
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (s != null)
					s.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error("error : ", e);
			}
		}

		return totalData;
	}
	
	private java.sql.Date getCurrentDate() {
		java.util.Date utilDate = Calendar.getInstance().getTime();
		java.sql.Date now = new java.sql.Date(utilDate.getTime());

		return now;
	}
	
	public String getEndpointCode(String accountNo, int type){
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		String endpointCode = null;
		
		try {
			c = dataSource.getConnection();
			String sql = "SELECT * from job_mt940_bri where ACCOUNT_NO = ? AND TYPE_TRX = ?";
			s = c.prepareStatement(sql);
			s.setString(1, accountNo);
			s.setInt(2, type);
			
			if(s.execute()) {
				rs = s.getResultSet();
				if (rs.next()) {
					endpointCode = rs.getString("ENDPOINT_CODE");
				}
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
		}finally {
			if(s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					logger.error("error : ", e);
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					logger.error("error : ", e);
				}
			}
		}
		
		return endpointCode;
	}
	
	public String getFolderName(String accountNo, int type){
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		String endpointCode = null;
		
		try {
			c = dataSource.getConnection();
			String sql = "SELECT * from job_mt940_bri where ACCOUNT_NO = ? AND TYPE_TRX = ?";
			s = c.prepareStatement(sql);
			s.setString(1, accountNo);
			s.setInt(2, type);
			
			if(s.execute()) {
				rs = s.getResultSet();
				if (rs.next()) {
					endpointCode = rs.getString("FOLDER_NAME");
				}
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
		}finally {
			if(s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					logger.error("error : ", e);
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					logger.error("error : ", e);
				}
			}
		}
		
		return endpointCode;
	}
	
	public JobMT940 getJobsMT940Object(String accountNo, int type){
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		JobMT940 job = new JobMT940();
		
		try {
			c = dataSource.getConnection();
			String sql = "SELECT * from job_mt940_bri where ACCOUNT_NO = ? AND TYPE_TRX = ?";
			s = c.prepareStatement(sql);
			s.setString(1, accountNo);
			s.setInt(2, type);
			
			if(s.execute()) {
				rs = s.getResultSet();
				if (rs.next()) {
					job.setAccountNo(rs.getString("ACCOUNT_NO"));
					job.setFilePattern(rs.getString("FILE_PATTERN"));
					job.setIsSameAccountNo(rs.getString("IS_SAME_ACCOUNT_NO"));
					job.setTransactionCode(rs.getString("TRANSACTION_CODE"));
					job.setTypeTrx(rs.getInt("TYPE_TRX"));
					job.setEndpointCode(rs.getString("ENDPOINT_CODE"));
					job.setBitFlag(rs.getString("BIT_FLAG"));
					job.setTransactionType(rs.getString("TRANSACTION_TYPE"));
					job.setFolderName(rs.getString("FOLDER_NAME"));
				}
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
		}finally {
			if(s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					logger.error("error : ", e);
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					logger.error("error : ", e);
				}
			}
		}
		
		logger.info("bri, getJobsMT940Object, job.accountNo : " + job.getAccountNo());
		
		return job;
	}
	
	public String getBranchOffice(String branchCode){
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		String branchOffice = null;
		
		try {
			c = dataSource.getConnection();
			String sql = "SELECT branch_office from branch_code where BRANCH_CODE = ? ";
			s = c.prepareStatement(sql);
			s.setString(1, branchCode);
			
			if(s.execute()) {
				rs = s.getResultSet();
				if (rs.next()) {
					branchOffice = rs.getString("BRANCH_OFFICE");
				}
			}
		} catch (SQLException e) {
			logger.error("error : ", e);
		}finally {
			if(s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					logger.error("error : ", e);
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					logger.error("error : ", e);
				}
			}
		}
		
		return branchOffice;
	}
}
