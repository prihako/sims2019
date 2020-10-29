package id.co.sigma.mx.project.ftpreconcile.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class WebadminUtilBniSyariah {
	
	private static Logger logger = Logger.getLogger(PaymentUtil.class);

	private DataSource dataSourcePostgres;

	public void setDataSourcePostgres(DataSource dataSourcePosgres) {
		this.dataSourcePostgres = dataSourcePosgres;
	}
	
	public boolean isEverInserted(String filename, String transactionType, String bankName){
		
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			c = dataSourcePostgres.getConnection();
			String sql = "SELECT COUNT(*) from transaction_report where report_document = ? and transaction_type = ? and bank_name = ? ";
			s = c.prepareStatement(sql);
			s.setString(1, filename);
			s.setString(2, transactionType);
			s.setString(3, bankName);
			rs = s.executeQuery();
			if (rs.next()) {
				long count = rs.getLong(1);
				if (count > 0) {
					flag = true;
					logger.info("bni, Data already exist in table transaction_report webadmin, filename : " + filename + ", bank Name : " + bankName);
				}
			}
		} catch (SQLException e) {
			logger.info("bni, Error in webadminUtil.isEverInserted function");
			logger.trace(e);
		}finally {
			if(s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					logger.trace(e);  
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					logger.trace(e);
				}
			}
		}

		return flag;
	}
	
	public void insertData(Date transactionDate, String filename, String transactionType, String bankName){
		
		Connection c = null;
		PreparedStatement s = null;

		try {
			c = dataSourcePostgres.getConnection();
			String sql = "insert into transaction_report(report_time, report_document, transaction_type, bank_name) values (?, ?, ?, ?) ";
			s = c.prepareStatement(sql);
			s.setDate(1, new java.sql.Date(transactionDate.getTime()));
			s.setString(2, filename);
			s.setString(3, transactionType);
			s.setString(4, bankName);
			s.executeUpdate();
			logger.info("bni, Success insert into table transaction_report webadmin, filename : " + filename + ", bank name " + bankName);
		} catch (SQLException e) {
			logger.info("bni, Error in webadminUtil.insertData function");
			logger.trace(e);
		}finally {
			if(s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					logger.trace(e);
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					logger.trace(e);
				}
			}
		}
		
	}

}
