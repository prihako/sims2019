package com.balicamp.dao.hibernate.mastermaintenance.bankguarantee;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.exception.ApplicationException;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.util.DateUtil;

@Repository
public class BankGuaranteeDaoHibernate extends AdminGenericDaoImpl<BankGuarantee,Long> implements BankGuaranteeDao{

	public BankGuaranteeDaoHibernate(){
		super(BankGuarantee.class);
	}

	protected final Log daoLog = LogFactory.getLog("LOGIC_LOG");
	private static final String NATIVE_QUERY = "select * from  t_bank_guarantee where ";


	@SuppressWarnings({ "unchecked" })
	@Override
	public List<BankGuarantee> findByLicenceNo(String licenceNo, String type) {
		List<BankGuarantee> list = null;
		String queryString = "from BankGuarantee where License.licenceNo='"+licenceNo+"' ";
		
		if(type.equalsIgnoreCase("UPDATE")){
			queryString += "and saveStatus in ('D','S') ";
		}else{
			queryString += "and saveStatus='S' ";
		}
		
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
				
		list = query.list();
		return list;
	}

	private final String SEARCH_BY_KEYWORD =
		"SELECT "
	/*[0]*/	+ "L.T_LICENCE_ID, "
	/*[1]*/	+ "L.CLIENT_NAME, "
	/*[2]*/	+ "L.CLIENT_ID, "
	/*[3]*/	+ "L.BHP_METHOD, "
	/*[4]*/	+ "BG.SUBMIT_YEAR_TO, "
	/*[4]+ "I.YEAR_TO, " */	
	/*[5]*/	+ "L.LICENCE_NO, "
	/*[6]*/	+ "L.BG_AVAILABLE_STATUS, "
	/*[7]	+ "to_char(L.BG_DUE_DATE, 'dd-MM-yyyy'), "*/
	/*[7]*/	+ "to_char(BG.SUBMIT_DUE_DATE, 'dd-MM-yyyy'), "
	/*[8]*/	+ "to_char(BG.RECEIVED_DATE, 'dd-MM-yyyy'), "
	/*[9]*/	+ "BG.BHP_VALUE, "
	/*[10]*/+ "BG.BG_VALUE, "
	/*[11]*/+ "BG.SUBMIT_YEAR_TO, "
	/*[12]*/+ "I.INVOICE_NO, "
	/*[13]*/+ "I.INVOICE_TYPE, "
	/*[14]*/+ "BG.BG_DOCUMENT_NO, "
	/*[15]*/+ "BG.RECEIVED_STATUS,"
	/*[16]*/+ "BG.BG_ID, "
	/*[17]*/+ "BG.SAVE_STATUS, "
	/*[18]*/+ "P.DESCRIPTION "
		+ "FROM "
			+ "S_PARAMETER P, T_BANK_GUARANTEE BG "
			+ "INNER JOIN T_LICENCE L "
				+ "ON BG.T_LICENCE_ID=L.T_LICENCE_ID "
			+ "LEFT JOIN T_INVOICE I "
				+ "ON BG.T_LICENCE_ID=I.T_LICENCE_ID AND BG.INVOICE_ID=I.INVOICE_ID "//BG.SUBMIT_YEAR_TO=I.YEAR_TO "
		+ "WHERE L.BG_AVAILABLE_STATUS='Y' AND ";
	
	private final String SEARCH_BY_KEYWORD_PAYMENT_BG =
			"SELECT "
		/*[0]*/	+ "L.T_LICENCE_ID, "
		/*[1]*/	+ "L.CLIENT_NAME, "
		/*[2]*/	+ "L.CLIENT_ID, "
		/*[3]*/	+ "L.BHP_METHOD, "
		/*[4]*/ + "I.YEAR_TO, "	
		/*[5]*/	+ "L.LICENCE_NO, "
		/*[6]*/	+ "L.BG_AVAILABLE_STATUS, "
		/*[7]	+ "to_char(L.BG_DUE_DATE, 'dd-MM-yyyy'), "*/
		/*[7]*/	+ "to_char(BG.SUBMIT_DUE_DATE, 'dd-MM-yyyy'), "
		/*[8]*/	+ "to_char(BG.RECEIVED_DATE, 'dd-MM-yyyy'), "
		/*[9]*/	+ "BG.BHP_VALUE, "
		/*[10]*/+ "BG.BG_VALUE, "
		/*[11]*/+ "BG.SUBMIT_YEAR_TO, "
		/*[12]*/+ "I.INVOICE_NO, "
		/*[13]*/+ "I.INVOICE_TYPE, "
		/*[14]*/+ "BG.BG_DOCUMENT_NO, "
		/*[15]*/+ "BG.RECEIVED_STATUS,"
		/*[16]*/+ "BG.BG_ID, "
		/*[17]*/+ "BG.SAVE_STATUS, "
		/*[18]*/+ "P.DESCRIPTION "
			+ "FROM "
				+ "S_PARAMETER P, T_BANK_GUARANTEE BG "
				+ "INNER JOIN T_LICENCE L "
					+ "ON BG.T_LICENCE_ID=L.T_LICENCE_ID "
				+ "LEFT JOIN T_INVOICE I "
				+ "ON BG.T_LICENCE_ID=I.T_LICENCE_ID AND BG.INVOICE_NO_CLAIM=I.INVOICE_NO "
			+ "WHERE L.BG_AVAILABLE_STATUS='Y' AND ";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Object> searchByKeyword(final String searchMethod, final String searchKeyword, final String extraQuery) {
		List bgList = null;
		try {
			bgList = (List) getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					
					String queryString = null;
					
					if(extraQuery != null && extraQuery.contains("PAYMENT")){
						queryString 	= SEARCH_BY_KEYWORD_PAYMENT_BG;
					}else{
						queryString 	= SEARCH_BY_KEYWORD;
					}

					String method		= "";
					if(searchMethod.contains("INVOICE")){
						method = "i."+searchMethod;
					}else if(searchMethod.contains("BG")){
						method = "bg."+searchMethod;
					}else{
						method = "l."+searchMethod;
					}

					queryString += method+" like '%"+searchKeyword+"%' ";

					if(extraQuery!=null){
						//queryString += "AND BG.INVOICE_NO_CLAIM is not null "; AND BG.SAVE_STATUS='D'
						queryString += "AND BG.SAVE_STATUS in ('S','E') AND P.PARAM_GROUP='bgclaimstatus' AND P.PARAM_VALUE=BG.CLAIM_STATUS ";
					}else{
						queryString += "AND BG.SAVE_STATUS in ('D','S','E') AND P.PARAM_GROUP='bgsubmitstatus' AND P.PARAM_VALUE=BG.RECEIVED_STATUS ";
					}

					queryString += "ORDER BY l.CLIENT_ID asc, i.YEAR_TO asc, l.LICENCE_NO";
					Query query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
					return query.list();
				}
			});
		} catch (DataAccessException ex) {
			daoLog.error(ex);
			ex.printStackTrace();
			throw new ApplicationException(ex.getMessage());
		}
		return bgList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BankGuarantee searchByLicenseAndYear(String licenseNo,
			String submitYearTo) {
		List<BankGuarantee> list = null;
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"from BankGuarantee where licenceNo='"+licenseNo+"' "
						+ "and submitYearTo='"+submitYearTo+"'");
		list = query.list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public BankGuarantee searchById(Long bgId) {
		List<BankGuarantee> list = null;
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"from BankGuarantee where bgId='"+bgId+"'");
		list = query.list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public BankGuarantee findByBgId(Long id) {
		String extraQuery = "bgId = " + id;
		Query query = getSessionFactory().getCurrentSession().createQuery(
				" from BankGuarantee where " + extraQuery);
		BankGuarantee bg = (BankGuarantee) query.uniqueResult();
		return bg;
	}

	@Override
	public BankGuarantee findByInvoiceID(Long licenceID, Long invoiceID) {
		String extraQuery = "t_licence_id = '" + licenceID +"' and invoice_id = '"+invoiceID+"'";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				NATIVE_QUERY + extraQuery);
		BankGuarantee bg = (BankGuarantee) query.uniqueResult();
		return bg;
	}
	
	@Override
	public void saveBG(BankGuarantee bg) {
		super.saveOrUpdate(bg);
	}

	@Override
	public List<BankGuarantee> findByLicenseId(Long licenseId) {
		List<BankGuarantee> list = null;

		Query query = getSessionFactory().getCurrentSession().createQuery(" from BankGuarantee where License = " + licenseId);
		list = query.list();
		return list;
	}

	@Override
	public BankGuarantee findBgByInvoiceId(Long invoiceId) {
		Query query = getSessionFactory().getCurrentSession().createQuery(" from BankGuarantee where invoice = " + invoiceId);
		BankGuarantee result = (BankGuarantee) query.uniqueResult();
		return result;
	}

	@Override
	public BankGuarantee findBgNextYear(Long licenseId, BigDecimal submitYearTo) {
		Query query = getSessionFactory().getCurrentSession().createQuery(" from BankGuarantee b where b.License = " + licenseId +
				" and b.submitYearTo = " + submitYearTo);
		BankGuarantee result = (BankGuarantee) query.uniqueResult();
		return result;
	}

	@Override
	public Map<String, String> performPaymentBG(BankGuarantee bg, Invoice invoice, License license) {
		// TODO Auto-generated method stub
		Date date 				= new Date();
		String errorCode 		= null;

		Connection con = getSessionFactory().getCurrentSession().connection();
		
		try {
			CallableStatement cstmt = con.prepareCall("{call SP_PAY_INVOICE_BG(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
		      
			cstmt.setString(1, invoice.getInvoiceNo()); //invoiceNo
		    cstmt.setString(2, license.getClientNo().toString()); //clientId
		    cstmt.setString(3, "120"); //reversalTime
//		    cstmt.setString(4, invoice.getBhpTotal().toString()); //amount
		    if(invoice.getBhpTotal().compareTo(bg.getBgValueSubmitted())<0){
		    	cstmt.setString(4, invoice.getBhpTotal().toString()); //amount
		    }else{
		    	cstmt.setString(4, bg.getBgValueSubmitted().toString()); //amount
		    }
		    cstmt.setString(5, DateUtil.convertDateToString(new Date(), "MMddHHmmss")); //theTransmissionDateTime
		    cstmt.setString(6, DateUtil.convertDateToString(new Date(), "HHmmss")); //theLocalTransactionTime
		    cstmt.setString(7, DateUtil.convertDateToString(new Date(), "MMdd")); //theLocalTransactionDate
		    cstmt.setString(8, DateUtil.convertDateToString(new Date(), "MMdd")); //theSettlementDate);
		    cstmt.setString(9, null); //theMerchantType
		    cstmt.setString(10, null); //theAcquiringInstitutionIDCode
		    cstmt.setString(11, "0000000000000000"); //theCardAcceptorTerminalID
		    cstmt.setString(12, "360"); //theTransactionCurrencyCode - IDR
		    cstmt.setString(13, "999"); //theInstitutionIDCode
		    cstmt.setString(14, invoice.getInvoiceNo().substring(1, invoice.getInvoiceNo().length())); //theSystemAuditNumber
		    cstmt.setString(15, DateUtil.convertDateToString(new Date(), "MMdd")+
		    		invoice.getInvoiceNo().substring(1, invoice.getInvoiceNo().length())); //theRetrievalReferenceNumber
		    cstmt.registerOutParameter(16, Types.VARCHAR);
			cstmt.registerOutParameter(17, Types.VARCHAR);
		    cstmt.executeUpdate();

			errorCode 			= cstmt.getString(16);

			System.out.println("Error Code = " + errorCode);

			cstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("ERROR_CODE", errorCode);
		return map;
	}

	@Override
	public BankGuarantee findByInvoiceNo(String invoiceNo) {
		Query query = getSessionFactory().getCurrentSession().
				createQuery(" from BankGuarantee where invoiceNo = '" + invoiceNo + "' OR invoiceNoClaim = '" + invoiceNo + "' ");
		BankGuarantee result = (BankGuarantee) query.uniqueResult();
		return result;
	}

}
