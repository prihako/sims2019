package com.balicamp.dao.hibernate.sims;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mx.SimsGenericDaoHibernate;
import com.balicamp.dao.sims.BillingDao;
import com.balicamp.model.admin.BaseAdminModel;

@Repository
public class BillingDaoHibernate extends
		SimsGenericDaoHibernate<BaseAdminModel, String> implements BillingDao {

	public BillingDaoHibernate() {
		super(BaseAdminModel.class);

	}

	@Override
	@Transactional
	public Object findBillingByInvoiceNo(String invoiceNo, String[] biTypes) {
		String sql = "select TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy'), B.BI_TYPE, TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy'), AD.AD_COMPANY "
				+ "from billing b, lut_num l, application ap, payment_terms p, licence li, address ad  "
				+ "where b.bi_ref_no = :biRefNo and l.col_name = 'bi_type'  and l.num_id = b.bi_type "
				+ "and b.pt_pt_id = p.pt_id and p.ap_ap_id = ap.ap_id and ap.li_li_id = li.li_id "
				+ "and li.ad_ad_id = ad.ad_id AND BI_TYPE IN ( :biTypes ) ";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql);

		query.setParameter("biRefNo", invoiceNo);
		query.setParameterList("biTypes", biTypes);

		return query.uniqueResult();
	}

	@Override
	@Transactional
	public Object findBillingByInvoiceNo(String invoiceNo) {
		String sql = "select TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy'), B.BI_TYPE, TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy'), AD.AD_COMPANY from "
				+ "billing b, lut_num l, application ap, payment_terms p, licence li, address ad "
				+ "where b.bi_ref_no = :biRefNo and l.col_name = 'bi_type' and l.num_id = b.bi_type "
				+ "and b.pt_pt_id = p.pt_id and p.ap_ap_id = ap.ap_id and ap.li_li_id = li.li_id and li.ad_ad_id = ad.ad_id ";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql);

		query.setParameter("biRefNo", invoiceNo);

		return query.uniqueResult();
	}

	@Override
	@Transactional
	public Map<String, Object[]> findBillingByInvoiceNo(Set<String> invoiceNo,
			String[] biTypes, String clientId) {
		Map<String, Object[]> mapResult = null;

		String sql = "select TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy'), B.BI_TYPE, TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy') as bi_pay_until, AD.AD_COMPANY, B.BI_REF_NO, B.BI_ID_REMINDER_ORIG, "
				+ "AD.AD_COMPANY, AD.AD_MAN_NUMBER "
				+ "from billing b, lut_num l, application ap, payment_terms p, licence li, address ad  "
				+ "where b.bi_ref_no in ( :biRefNo ) and l.col_name = 'bi_type'  and l.num_id = b.bi_type "
				+ "and b.pt_pt_id = p.pt_id and p.ap_ap_id = ap.ap_id and ap.li_li_id = li.li_id "
				+ "and li.ad_ad_id = ad.ad_id AND BI_TYPE IN ( :biTypes ) ";

		if(clientId != null ){
			sql += "and ad.ad_man_number ='" + clientId + "' ";
		}
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql);

		query.setParameterList("biRefNo", invoiceNo);
		query.setParameterList("biTypes", biTypes);

		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objectArray = (Object[]) obj;
				mapResult.put(objectArray[4].toString(), objectArray);
			}
		}

		return mapResult;
	}

	@Override
	@Transactional
	public Map<String, Object[]> findBillingByInvoiceNo(Set<String> invoiceNo, String clientId) {
		Map<String, Object[]> mapResult = null;

		String sql = "select TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy'), B.BI_TYPE, TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy') as bi_pay_until, AD.AD_COMPANY, B.BI_REF_NO, B.BI_ID_REMINDER_ORIG, "
				+ "AD.AD_COMPANY, AD.AD_MAN_NUMBER "
				+ "from billing b, lut_num l, application ap, payment_terms p, licence li, address ad  "
				+ "where b.bi_ref_no in ( :biRefNo ) and l.col_name = 'bi_type'  and l.num_id = b.bi_type "
				+ "and b.pt_pt_id = p.pt_id and p.ap_ap_id = ap.ap_id and ap.li_li_id = li.li_id "
				+ "and li.ad_ad_id = ad.ad_id ";

		if(clientId != null ){
			sql += "and ad.ad_man_number ='" + clientId + "' ";
		}
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql);

		query.setParameterList("biRefNo", invoiceNo);

		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objectArray = (Object[]) obj;
				mapResult.put(objectArray[4].toString(), objectArray);
			}
		}

		return mapResult;
	}


	@Override
	@Transactional
	public Map<String, Object[]> findAllBillingPaidByInvoiceNoAndDate(Set<String> invoiceNo, Date trxDate, String[] biTypes) {
		Map<String, Object[]> mapResult = null;

		String sql = "select TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy'), B.BI_TYPE, TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy'), AD.AD_COMPANY, B.BI_REF_NO, B.BI_ID_REMINDER_ORIG, AD.AD_MAN_NUMBER "
				+ "from billing b, lut_num l, application ap, payment_terms p, licence li, address ad  "
				+ "where l.col_name = 'bi_type'  and l.num_id = b.bi_type "
				+ "and b.pt_pt_id = p.pt_id and p.ap_ap_id = ap.ap_id and ap.li_li_id = li.li_id "
				+ "and li.ad_ad_id = ad.ad_id AND B.BI_TYPE IN ( :biTypes )"
				+ "and (b.bi_ref_no in ( :biRefNo ) or TO_DATE(b.bi_money_received) = (:biMoneyRec))"
				+ "order by B.BI_MONEY_RECEIVED ASC";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql);
		
		query.setParameterList("biRefNo", invoiceNo);
		query.setParameter("biMoneyRec", trxDate);
		query.setParameterList("biTypes", biTypes);

		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objectArray = (Object[]) obj;
				mapResult.put(objectArray[4].toString(), objectArray);
			}
		}
		return mapResult;
	}
		
		@Override
		@Transactional
		public Map<String, Object[]> findAllBillingPaidByInvoiceNoAndDateNotSettle(Set<String> invoiceNo, Date trxDate, String[] biTypes) {
			Map<String, Object[]> mapResult = null;

			String sql = "select TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy'), B.BI_TYPE, TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy'), AD.AD_COMPANY, B.BI_REF_NO, B.BI_ID_REMINDER_ORIG, AD.AD_MAN_NUMBER "
					+ "from billing b, lut_num l, application ap, payment_terms p, licence li, address ad  "
					+ "where b.bi_ref_no not in ( :biRefNo )and l.col_name = 'bi_type'  and l.num_id = b.bi_type "
					+ "and b.pt_pt_id = p.pt_id and p.ap_ap_id = ap.ap_id and ap.li_li_id = li.li_id "
					+ "and li.ad_ad_id = ad.ad_id AND B.BI_TYPE IN ( :biTypes )"
					+ "and TO_DATE(b.bi_money_received) = (:biMoneyRec) "
					+ "order by B.BI_MONEY_RECEIVED ASC";

			Query query = getSessionFactory().getCurrentSession().createSQLQuery(
					sql);
			
			query.setParameterList("biRefNo", invoiceNo);
			query.setParameter("biMoneyRec", trxDate);
			query.setParameterList("biTypes", biTypes);

			List<Object> result = query.list();
			if (result != null) {
				mapResult = new HashMap<String, Object[]>();
				for (Object obj : result) {
					Object[] objectArray = (Object[]) obj;
					mapResult.put(objectArray[4].toString(), objectArray);
				}
			}

		return mapResult;
	}
	
	@Override
	@Transactional
	public Map<String, Object[]> findAllBillingUnpaidByInvoiceNoAndDate(Set<String> invoiceNo, Date trxDate, String[] biTypes) {
		Map<String, Object[]> mapResult = null;

		String sql = "select TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy'), B.BI_TYPE, TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy'), AD.AD_COMPANY, B.BI_REF_NO, B.BI_ID_REMINDER_ORIG, AD.AD_MAN_NUMBER "
				+ "from billing b, lut_num l, application ap, payment_terms p, licence li, address ad  "
				+ "where l.col_name = 'bi_type'  and l.num_id = b.bi_type "
				+ "and b.pt_pt_id = p.pt_id and p.ap_ap_id = ap.ap_id and ap.li_li_id = li.li_id "
				+ "and li.ad_ad_id = ad.ad_id AND B.BI_TYPE NOT IN ( :biTypes )"
				+ "and (b.bi_ref_no in ( :biRefNo ) or TO_DATE(b.bi_money_received) = (:biMoneyRec))"
				+ "order by B.BI_MONEY_RECEIVED ASC";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql);
		
		query.setParameterList("biRefNo", invoiceNo);
		query.setParameter("biMoneyRec", trxDate);
		query.setParameterList("biTypes", biTypes);

		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objectArray = (Object[]) obj;
				mapResult.put(objectArray[4].toString(), objectArray);
			}
		}

		return mapResult;
	}
	
	@Override
	@Transactional
	public void addRemarks(String invoiceNo, Date trxDate, String[] biTypes, String remarks) {
		Map<String, Object[]> mapResult = null;

		String sql = "update billing b SET b.BI_COMMENT= (:biComment)"
				+ "where b.bi_ref_no = (:biRefNo)";
//				+ "and B.BI_TYPE IN (:biTypes)";
//				+ "and TO_DATE(b.bi_money_received) = (:biMoneyRec) ";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql);
		
		query.setParameter("biComment", remarks);
		query.setParameter("biRefNo", invoiceNo);
//		query.setParameter("biMoneyRec", trxDate);
//		query.setParameterList("biTypes", biTypes);
		
		query.executeUpdate();
	}

	@Override
	@Transactional
	public Map<String, Object[]> findAllBillingByInvoiceNoAndDate(
			Set<String> invoiceNo, Date trxDate) {
		Map<String, Object[]> mapResult = null;

		String sql = "select "
				+ "TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy HH24:MI:SS'), "
				+ "B.BI_TYPE, "
				+ "TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy'), "
				+ "AD.AD_COMPANY, "
				+ "B.BI_REF_NO, "
				+ "B.BI_ID_REMINDER_ORIG, "
				+ "AD.AD_MAN_NUMBER, "
				+ "B.BI_MANUAL, "
				+ "B.BI_ID, "
				+ "L.NUM_TXT, "
				+ "B.BI_COMMENT, "
				+ "B.BI_COST_BILL "
				+ "from billing b, lut_num l, application ap, payment_terms p, licence li, address ad  "
				+ "where l.col_name = 'bi_type'  and l.num_id = b.bi_type "
				+ "and b.pt_pt_id = p.pt_id and p.ap_ap_id = ap.ap_id and ap.li_li_id = li.li_id "
				+ "and li.ad_ad_id = ad.ad_id and (";
		
		int sizeInvoiceList = invoiceNo.size();
		int totalList = 1;
		
		if(sizeInvoiceList > 999){
			totalList += (sizeInvoiceList/999);
		}
		
		System.out.println((new Date()) + " sizeInvoiceList : " + sizeInvoiceList);
		System.out.println((new Date()) + " totalList : " + totalList);
		
		if(totalList == 1){
			sql += "b.bi_ref_no in ( :biRefNo0 ) and ";
		}else{
			sql += "(";
			for(int i=0;i<totalList;i++){
				if(i<totalList-1){
					sql += "b.bi_ref_no in ( :biRefNo"+i+" ) or ";
				}else{
					sql += "b.bi_ref_no in ( :biRefNo"+i+" )";
				}
			}
//			sql += ") and ";
			sql += ") ) ";
		}
				
//		sql += "(b.bi_money_received >= (:biMoneyRec) and "
//		+ "b.bi_money_received < (:biMoneyRec) + interval '1' day )) "
//		+ "order by B.BI_MONEY_RECEIVED DESC";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql);
		
		List<Set<String>> invoiceNoSetList = new ArrayList<Set<String>>(totalList);
		for (int i = 0; i < totalList; i++) {
			invoiceNoSetList.add(new HashSet<String>());
		}
		
		int index = 0;
		int invoiceNoCount = 0;
		for (String no : invoiceNo) {
			if (invoiceNoCount<999){
				invoiceNoSetList.get(index).add(no);
				invoiceNoCount++;
			}else{
				index++;
				invoiceNoCount = 0;
				invoiceNoSetList.get(index).add(no);
			}
		}
		
		System.out.println((new Date()) + " totalInvoice : " + invoiceNoSetList.size());
		System.out.println((new Date()) + " invoiceIndex0 : " + invoiceNoSetList.get(0));
		
		for(int i=0;i < totalList;i++){
			query.setParameterList("biRefNo"+i, invoiceNoSetList.get(i) != null ? invoiceNoSetList.get(i) : new ArrayList<String>() );
		}	
		
		System.out.println((new Date()) + " trxDate : " + trxDate);
		query.setParameter("biMoneyRec", trxDate);
		System.out.println((new Date()) + " sql : " + sql);
		List<Object> result = query.list();
		if (result != null) {
			System.out.println((new Date()) + " resultQueryBHP : " + result.toString());
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objectArray = (Object[]) obj;
				mapResult.put(objectArray[4].toString(), objectArray);
			}
		}
		return mapResult;
	}
	
	@Override
	@Transactional
	public Map<String, Object[]> findAllBillingDendaByInvoiceNo(
			Set<String> biId) {
		Map<String, Object[]> mapResult = null;

		String sql = "select "
				+ "TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy'), "
				+ "B.BI_TYPE, "
				+ "TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy'), "
				+ "AD.AD_COMPANY, "
				+ "B.BI_REF_NO, "
				+ "B.BI_ID_REMINDER_ORIG, "
				+ "AD.AD_MAN_NUMBER, "
				+ "B.BI_MANUAL, "
				+ "MAX(B.BI_ID), "
				+ "L.NUM_TXT, "
				+ "B.BI_COMMENT, "
				+ "B.BI_COST_BILL "
				+ "from billing b, lut_num l, application ap, payment_terms p, licence li, address ad "
				+ "where l.col_name = 'bi_type' "
				+ "and l.num_id = b.bi_type "
				+ "and b.pt_pt_id = p.pt_id "
				+ "and p.ap_ap_id = ap.ap_id "
				+ "and ap.li_li_id = li.li_id "
				+ "and li.ad_ad_id = ad.ad_id and ";
		
		int sizeInvoiceList = biId.size();
		int totalList = (sizeInvoiceList/999)+1;
		
		if(totalList == 1){
			sql += "b.bi_id_reminder_orig in ( :biId0 ) ";
		}else{
			sql += "(";
			for(int i=0;i<totalList;i++){
				if(i<totalList-1){
					sql += "b.bi_id_reminder_orig in ( :biId"+i+" ) or ";
				}else{
					sql += "b.bi_id_reminder_orig in ( :biId"+i+" ) ";
				}
			}
			sql += ") ";
		}
				
				sql+= "group by "
				+ "B.BI_MONEY_RECEIVED, "
				+ "B.BI_TYPE, "
				+ "B.BI_PAY_UNTIL, "
				+ "AD.AD_COMPANY, "
				+ "B.BI_REF_NO, "
				+ "B.BI_ID_REMINDER_ORIG, "
				+ "AD.AD_MAN_NUMBER, "
				+ "B.BI_MANUAL, "
				+ "L.NUM_TXT, "
				+ "B.BI_COMMENT, "
				+ "B.BI_COST_BILL "
				+ "order by B.BI_MONEY_RECEIVED DESC";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql);
		
		List<Set<String>> biIdSetList = new ArrayList<Set<String>>(totalList);
		for (int i = 0; i < totalList; i++) {
			biIdSetList.add(new HashSet<String>());
		}
		
		int index = 0;
		int biIdCount = 0;
		for (String no : biId) {
			if (biIdCount<999){
				biIdSetList.get(index).add(no);
				biIdCount++;
			}else{
				index++;
				biIdCount = 0;
				biIdSetList.get(index).add(no);
			}
		}
		
		for(int i=0;i < totalList;i++){
			query.setParameterList("biId"+i, biIdSetList.get(i) != null ? biIdSetList.get(i) : new ArrayList<String>() );
		}	
		
		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objectArray = (Object[]) obj;
				mapResult.put(objectArray[5].toString(), objectArray);
			}
		}
		return mapResult;
	}

	@Override
	@Transactional
	public Map<String, Object[]> findAllBillingReconciledByInvoiceNo(
			Set<String> invoiceNo) {
		Map<String, Object[]> mapResult = null;

		String sql = "select "
				+ "TO_CHAR(B.BI_MONEY_RECEIVED,'dd-MM-yyyy'), "
				+ "B.BI_TYPE, "
				+ "TO_CHAR(B.BI_PAY_UNTIL,'dd-MM-yyyy'), "
				+ "AD.AD_COMPANY, "
				+ "B.BI_REF_NO, "
				+ "B.BI_ID_REMINDER_ORIG, "
				+ "AD.AD_MAN_NUMBER, "
				+ "B.BI_MANUAL, "
				+ "B.BI_ID, "
				+ "L.NUM_TXT, "
				+ "B.BI_COMMENT, "
				+ "B.BI_COST_BILL "
				+ "from billing b, lut_num l, application ap, payment_terms p, licence li, address ad  "
				+ "where l.col_name = 'bi_type'  and l.num_id = b.bi_type "
				+ "and b.pt_pt_id = p.pt_id and p.ap_ap_id = ap.ap_id and ap.li_li_id = li.li_id "
				+ "and li.ad_ad_id = ad.ad_id ";
				
				int sizeInvoiceList = invoiceNo.size();
				int totalList = (sizeInvoiceList/999)+1;
				
				if(totalList == 1){
					sql += "and b.bi_ref_no in ( :biRefNo0 ) ";
				}else{
					sql += "(";
					for(int i=0;i<totalList;i++){
						if(i<totalList-1){
							sql += "b.bi_ref_no in ( :biRefNo"+i+" ) or ";
						}else{
							sql += "b.bi_ref_no in ( :biRefNo"+i+" )";
						}
					}
					sql += ") ";
				}

				sql += "order by B.BI_MONEY_RECEIVED DESC";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				sql);
		
		List<Set<String>> invoiceNoSetList = new ArrayList<Set<String>>(totalList);
		for (int i = 0; i < totalList; i++) {
			invoiceNoSetList.add(new HashSet<String>());
		}
		
		int index = 0;
		int invoiceNoCount = 0;
		for (String no : invoiceNo) {
			if (invoiceNoCount<999){
				invoiceNoSetList.get(index).add(no);
				invoiceNoCount++;
			}else{
				index++;
				invoiceNoCount = 0;
				invoiceNoSetList.get(index).add(no);
			}
		}
		
		for(int i=0;i < totalList;i++){
			query.setParameterList("biRefNo"+i, invoiceNoSetList.get(i) != null ? invoiceNoSetList.get(i) : new ArrayList<String>() );
		}		

		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objectArray = (Object[]) obj;
				mapResult.put(objectArray[4].toString(), objectArray);
			}
		}
		return mapResult;
	}

}