package com.balicamp.dao.hibernate.ebs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.ebs.ExternalBillingSystemDao;
import com.balicamp.model.admin.BaseAdminModel;

@Repository
public class ExternalBillingSystemDaoHibernate extends
		EbsGenericDaoHibernate<BaseAdminModel, String> implements
		ExternalBillingSystemDao {
	
	public ExternalBillingSystemDaoHibernate() {
		super(BaseAdminModel.class);
	}

	@Override
	@Transactional
	public Object findInvoiceByInvoiceNo(String invoiceNo) {
		String sql 		= "SELECT INVOICE_ID, T_LICENCE_ID, INVOICE_NO FROM T_INVOICE WHERE INVOICE_NO = :invoiceNo ";
		Query query 	= getSessionFactory().getCurrentSession().createSQLQuery(sql);
		Object result 	= new Object();
		
		query.setParameter("invoiceNo", invoiceNo);
		result = query.uniqueResult();
		
		getSessionFactory().getCurrentSession().close();
		
		return result;
	}

	@Override
	@Transactional
	public boolean updateInvoicePaid(String invoiceNo, Date paymentDate, String remarks, String username) {

		Connection con 					= getSessionFactory().getCurrentSession().connection();
		CallableStatement cstmt 		= null;
	    java.sql.Date sqlPaymentDate 	= new java.sql.Date(paymentDate.getTime());
		try {
			cstmt = con.prepareCall("{call SP_UPDATE_STATUS_SIMS_PAID(?,?,?,?,?)}");
			
			cstmt.setString(1, invoiceNo);
			cstmt.setDate(2, sqlPaymentDate);
			cstmt.setString(3, remarks);
			cstmt.setString(4, username);
			cstmt.registerOutParameter(5, Types.VARCHAR);
			cstmt.executeUpdate();
			
			String status = cstmt.getString(5);
			
			cstmt.close();
			con.close();
			
			if(status.equals("1")){
				return true;
			}else{
				return false;
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	@Transactional
	public boolean updateInvoiceUnpaid(String invoiceNo) {
		Connection con 			= getSessionFactory().getCurrentSession().connection();
		CallableStatement cstmt = null;

		try {

			cstmt = con.prepareCall("{call SP_UPDATE_STATUS_SIMS_UNPAID(?)}");
			cstmt.setString(1, invoiceNo);
			cstmt.executeUpdate();
			cstmt.close();
			con.close();
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			cstmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	@Transactional
	public boolean updateInvoiceEod(String invoiceNo, Date paymentDate, String remarks) {
		Connection con 					= getSessionFactory().getCurrentSession().connection();
		CallableStatement cstmt 		= null;
	    java.sql.Date sqlPaymentDate 	= new java.sql.Date(paymentDate.getTime());
		
		try {
			cstmt = con.prepareCall("{call SP_UPDATE_STATUS_SIMS_EOD(?,?,?,?)}");
			cstmt.setString(1, invoiceNo);
			cstmt.setDate(2, sqlPaymentDate);
			cstmt.setString(3, remarks);
			cstmt.registerOutParameter(4, Types.VARCHAR);
			cstmt.executeUpdate();
			
			String status = cstmt.getString(4);

			cstmt.close();
			con.close();
			
			if(status.equals("1")){
				return true;
			}else{
				return false;
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
