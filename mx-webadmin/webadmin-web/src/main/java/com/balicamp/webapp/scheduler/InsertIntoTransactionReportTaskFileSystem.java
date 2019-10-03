package com.balicamp.webapp.scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balicamp.webapp.ftp.FTPManager;

public class InsertIntoTransactionReportTaskFileSystem {
	protected final Log log = LogFactory.getLog(getClass());
	private FTPManager ftpManager;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setFtpManager(FTPManager ftpManager) {
		this.ftpManager = ftpManager;
	}

	public void listAllFiles() throws Exception {
		String[] fileNames = ftpManager.listFileNames(null);
		for (int i = 0; i < fileNames.length; i++) {
			System.out.println("------------------------" + fileNames[i] + "-------------------------");
		}
	}

	public void updateTableTransactionReport(String directory, int transactionType) throws Exception {
		String[] fileNames = ftpManager.listFileNames(FTPManager.getDirectoryServer() + directory);
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

		if (fileNames.length > 0) {
			for (int i = 0; i < fileNames.length; i++) {
				if (fileNames[i].length() == 19) {
					String fileDate = fileNames[i];

					if (fileDate.indexOf(".xls") > 0) {
						fileDate = fileDate.substring(fileDate.indexOf(".xls") - 10, fileDate.indexOf(".xls") - 2);
						if (!checkDocument(transactionType, fileNames[i])) {
							saveQuery(yyyyMMdd.parse(fileDate), transactionType, fileNames[i]);
							System.out.println("file saved : " + fileNames[i]);
						}
					} else {
						System.out.println("file invalid, not excel : " + fileNames[i]);
					}

				} else {
					System.out.println("Ftp file name invalid, filename : " + fileNames[i]);
				}
			}
		} else {
			log.info(new Date() + " : Cant't Update database Transaction Report, check connection ftp");
		}
	}

	public void saveTransactionReport(String directory, int transactionType) throws Exception {
		Calendar cal = Calendar.getInstance();
		Date nowDate = new Date();
		cal.setTime(nowDate);
		cal.add(Calendar.DAY_OF_YEAR, -1);

		ArrayList<String> fileNameToSaved = new ArrayList<String>();
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		String[] fileNames = ftpManager.listFileNames(directory);
		String nowStr = yyyyMMdd.format(cal.getTime());

		for (int i = 0; i < fileNames.length; i++) {
			if (fileNames[i].length() == 17) {
				String fileDate = fileNames[i];

				if (fileDate.indexOf(".xls") > 0) {
					fileDate = fileDate.substring(fileDate.indexOf(".xls") - 8, fileDate.indexOf(".xls"));
					if (fileDate.equals(nowStr)) {
						fileNameToSaved.add(fileNames[i]);
					}
				} else {
					System.out.println("file invalid, not excel : " + fileNames[i]);
				}

			} else {
				System.out.println("Ftp file name invalid, filename : " + fileNames[i]);
			}
		}

		if (fileNameToSaved.size() > 0) {
			for (int i = 0; i < fileNameToSaved.size(); i++) {
				boolean exist = checkDocument(transactionType, fileNameToSaved.get(i));
				if (!exist) {
					saveQuery(cal.getTime(), transactionType, fileNameToSaved.get(i));
				}
			}
		}

	}

	public void saveQuery(Date reportTime, int transaction_type, String reportDocument) throws SQLException {
		String sql = "INSERT INTO transaction_report(report_time, transaction_type, report_document)"
				.concat(" VALUES(?, ?, ?)");
		Connection con = null;
		PreparedStatement s = null;
		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.setDate(1, new java.sql.Date(reportTime.getTime()));
			s.setLong(2, transaction_type);
			s.setString(3, reportDocument);
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		}
	}

	public boolean checkDocument(int transactionType, String reportDocument) throws SQLException {
		String sql = "SELECT COUNT(*) FROM transaction_report WHERE transaction_type = ? " + "AND report_document = ?";
		Connection con = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		boolean flag = false;

		try {
			con = dataSource.getConnection();
			stat = con.prepareStatement(sql);
			stat.setInt(1, transactionType);
			stat.setString(2, reportDocument);
			rs = stat.executeQuery();
			if (rs.next()) {
				long count = rs.getLong(1);
				if (count > 0) {
					flag = true;
					System.out.println("Data already exist");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stat != null) {
				stat.close();
			}
			if (con != null) {
				con.close();
			}
		}
		return flag;
	}

	public void deleteQuery() throws SQLException {
		String sql = "DELETE FROM transaction_report";
		Connection con = null;
		PreparedStatement s = null;
		try {
			con = dataSource.getConnection();
			s = con.prepareStatement(sql);
			s.executeUpdate();
			System.out.println("Delete All Data");
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		}
	}
}
