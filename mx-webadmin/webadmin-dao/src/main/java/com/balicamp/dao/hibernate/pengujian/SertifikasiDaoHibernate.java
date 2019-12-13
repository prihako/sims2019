package com.balicamp.dao.hibernate.pengujian;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.dao.pengujian.SertifikasiDao;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.mx.ReconcileDto;

@Repository @Transactional
public class SertifikasiDaoHibernate extends
		SertifikasiGenericDaoHibernate<BaseAdminModel, String> implements
		SertifikasiDao {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public SertifikasiDaoHibernate() {
		super(BaseAdminModel.class);
	}

	@Override
	public boolean updateInvoiceEodSertifikasi(ReconcileDto reconcile, 
			Date paymentDate, String remarks) {
		// TODO Auto-generated method stub
		
		String queryString = "update esertifikasi.dbo.T_APLIKASI "
				+ "set "
				+ "PREV_STAT=STATUS, "
				+ "STATUS='75', "
				+ "CATATAN=:remarks, "
				+ "LAST_UPDATE=:reconcileDate "
				+ "where CUST_ID=:clientId and apl_id in ("
					+ "select apl_id from esertifikasi.dbo.T_SP2 "
					+ "where sp2_no = :invoiceId)";
		
		String pattern 			= "yyyy-MM-dd HH:mm:ss";
		String reconcileDate	= null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		
		String invoiceSP2 	= reconcile.getInvoiceNo().substring(0,reconcile.getInvoiceNo().length()-6)+"/SP2/KSDP/";
		String invoiceMonth = reconcile.getInvoiceNo().substring(4,6); 
		if(invoiceMonth.equals("01")){
			invoiceSP2 += "I/";
		}else if(invoiceMonth.equals("02")){
			invoiceSP2 += "II/";
		}else if(invoiceMonth.equals("03")){
			invoiceSP2 += "III/";
		}else if(invoiceMonth.equals("04")){
			invoiceSP2 += "IV/";
		}else if(invoiceMonth.equals("05")){
			invoiceSP2 += "V/";
		}else if(invoiceMonth.equals("06")){
			invoiceSP2 += "VI/";
		}else if(invoiceMonth.equals("07")){
			invoiceSP2 += "VII/";
		}else if(invoiceMonth.equals("08")){
			invoiceSP2 += "VIII/";
		}else if(invoiceMonth.equals("09")){
			invoiceSP2 += "IX/";
		}else if(invoiceMonth.equals("10")){
			invoiceSP2 += "X/";
		}else if(invoiceMonth.equals("11")){
			invoiceSP2 += "XI/";
		}else if(invoiceMonth.equals("12")){
			invoiceSP2 += "XII/";
		}
		invoiceSP2 += reconcile.getInvoiceNo().substring(reconcile.getInvoiceNo().length()-4);
		
		reconcileDate = format.format(paymentDate);
		
		Query query = getSession().createSQLQuery(queryString);
		query.setParameter("remarks", remarks);
		query.setParameter("reconcileDate", reconcileDate);
		query.setParameter("clientId", reconcile.getClientId());
		query.setParameter("invoiceId", invoiceSP2);
		
		try {
			if (query.executeUpdate() > 0) {
				if(insertInvoiceEodSertifikasi(reconcile, invoiceSP2, paymentDate, remarks) == true){
					return true;
				}else{
					return false;
				}
			} else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean insertInvoiceEodSertifikasi(ReconcileDto reconcile, String invoiceSP2, Date paymentDate, String remarks) {

		String sql = "INSERT INTO esertifikasi.dbo.T_INFO_BAYAR( "
				+ "BAYAR_ID, "
				+ "SP2_ID, "
				+ "JML_BYR, "
				+ "NO_BYR, "
				+ "TGL_BYR, "
				+ "TGL_TRM_BYR, "
				+ "UPDATED_BY, "
				+ "LAST_UPDATE, "
				+ "BUKTI_BYR, "
				+ "CATATAN " 
				+ ") values (" 
					+ "select max(bayar_id)+1 from esertifikasi.dbo.T_INFO_BAYAR, "
					+ "select max(sp2_id)+1 from esertifikasi.dbo.T_INFO_BAYAR, " 
					+ ":amount, "
					+ ":invoiceId, "
					+ ":reconcileDate, " 
					+ ":reconcileDate, " 
					+ "'payment_gateway', "
					+ ":reconcileDate, " 
					+ "'', " 
					+ ":remarks "
				+ ")";

		String pattern 			= "yyyy-MM-dd HH:mm:ss";
		String reconcileDate	= null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		
		reconcileDate = format.format(paymentDate);
		
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("amount", reconcile.getTrxAmount());
		query.setParameter("invoiceId", invoiceSP2);
		query.setParameter("clientId", reconcile.getClientId());
		query.setParameter("reconcileDate", reconcileDate);
		query.setParameter("remarks", remarks);
		
		try {
			if (query.executeUpdate() > 0) {
				return true;
			} else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public List<BaseAdminModel> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseAdminModel findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BaseAdminModel saveMerge(BaseAdminModel object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrUpdate(BaseAdminModel object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(BaseAdminModel object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(BaseAdminModel object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveCollection(Collection<BaseAdminModel> object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(BaseAdminModel object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BaseAdminModel> findByExample(BaseAdminModel exampleEntity,
			List<String> excludeProperty, int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseAdminModel> findByCriteria(List<Criterion> criterionList,
			List<Order> orderList, int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseAdminModel findSingleByCriteria(List<Criterion> criterionList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseAdminModel findSingleByCriteria(Criterion criterion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseAdminModel findFirstByCriteria(List<Criterion> criterionList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseAdminModel findFirstByCriteria(Criterion criterion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findByCriteriaCount(List<Criterion> criterionList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseAdminModel> findByCriteria(SearchCriteria searchCriteria,
			int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseAdminModel> findByCriteria(SearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseAdminModel findSingleByCriteria(SearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findByCriteriaCount(SearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUniqueAvailable(String id, BaseAdminModel exampleEntity,
			List<String> excludeProperty) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUniqueAvailableByCriteria(String id,
			List<Criterion> criterionList) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void evict(BaseAdminModel object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setLocked(BaseAdminModel entity, boolean locked) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HibernateTemplate getCurrentHibernateTemplate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		String test = "4935082018";
		System.out.println(test.substring(0,test.length()-6));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] findBillingByInvoiceAndDate(String invoiceNo, Date trxDate, Object[] mt940Map) {
		// TODO Auto-generated method stub
		
		String sql = "select "
				+ "s.SP2_NO, "
				+ "a.CUST_ID, "
				+ "c.CUST_NAME, "
				+ "b.TGL_BYR, "
				+ "s.EXP_DATE, "
				+ "b.JML_BYR "
				+ "from "
				+ "esertifikasi.dbo.T_INFO_BAYAR b, "
				+ "esertifikasi.dbo.T_SP2 s, "
				+ "esertifikasi.dbo.T_APLIKASI a, "
				+ "esertifikasi.dbo.T_CUSTOMER c "
				+ "where "
//				+ "b.JML_BYR = :invoiceAmount "
				+ "and s.SP2_NO = :invoiceNo "
				+ "and b.UPDATED_BY like 'payment_gateway%' "
				+ "and b.SP2_ID = s.SP2_ID "
				+ "and s.APL_ID = a.APL_ID "
				+ "and a.CUST_ID = c.CUST_ID ";
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
//		query.setParameter("invoiceAmount", mt940Map[7].toString());
		query.setParameter("invoiceNo", invoiceNo);
			
		Object obj = new Object();
		Object[] objectArray = null;
		
		int maxTries = 10;
		int numTries = 0;
		
		while(numTries <= maxTries) {
			try {
				List<Object> result = query.list();
				if (result.size()>0) {
					obj = result.get(0);
					objectArray = (Object[]) obj;
				}
				break;
			}catch(Exception e) {
				numTries ++;
				log.info("Sleep 1 second, and try again later, numTries " + numTries + ", maxTries " + maxTries);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		return objectArray;
	}
}
