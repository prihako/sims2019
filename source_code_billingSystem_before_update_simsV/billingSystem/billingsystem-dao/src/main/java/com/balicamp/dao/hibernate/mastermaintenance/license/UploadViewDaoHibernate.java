
package com.balicamp.dao.hibernate.mastermaintenance.license;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.mastermaintenance.license.UploadView;

@Repository
public class UploadViewDaoHibernate extends AdminGenericDaoImpl<UploadView, String>
		implements UploadViewDao {

	public UploadViewDaoHibernate() {
		super(UploadView.class);
		   
	}
 
	@Override
	public List<UploadView> findAllView() {
		Query query = getSession().createQuery("from UploadView");
		List result = query.list();
		return result;
	}
	
	@Override
	public List<UploadView> findBI(String source) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + source + "'");
		List result = query.list();
		return result;
	}

	@Override
	public List<UploadView> findBG(String source) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + source + "'");
		List result = query.list();
		return result;
	}

	@Override
	public List<UploadView> findIN(String source) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + source + "'");
		List result = query.list();
		return result;
	}

	@Override
	public List<UploadView> findDataByClientName(String source,
			String clientName) {
//		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + source + "' AND u.clientName '%" + clientName + "%' ORDER BY u.yearTo");
//		List result = query.list();
//		return result;
		
		Criteria crit = getSession().createCriteria(UploadView.class);
		crit.add(Restrictions.ilike("tSource", "%" + source + "%")).add(Restrictions.ilike("clientName", "%" + clientName + "%"));
		List<UploadView> results = crit.list();
		
		return  results;
	}
	
	@Override
	public List<UploadView> findDataByClientName(String source,
			String clientName, String docType) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + source 
				+ "' AND u.clientName LIKE '%" + clientName + "%' AND u.docType = '" + docType + "'");
		List result = query.list();
		return result;
	}

	@Override
	public List<UploadView> findDataByLicenseNo(String source, String licenseNo) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + source + "' AND u.licenseNo = '" + licenseNo + "' ORDER BY u.yearTo");
		List result = query.list();
		return result;
	}
	
	@Override
	public List<UploadView> findDataByLicenseNo(String source, String licenseNo, String docType) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + source 
				+ "' AND u.licenseNo = '" + licenseNo + "' AND u.docType = '" + docType + "'");
		List result = query.list();
		return result;
	}

	@Override
	public UploadView findUploadViewByLicenseNoUnique(String licenseNo) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.licenseNo = '" + licenseNo + "'");
		UploadView result = (UploadView) query.uniqueResult();
		return result;
	}

	@Override
	public List<UploadView> findDataBIByYear(String year) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = 'BI' AND u.year = '" + year + "'");
		List result = query.list();
		return result;
	} 

	@Override
	public List<UploadView> findDataBIByNoKM(String licenseNo) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = 'BI' AND u.licenseNo = '" + licenseNo + "'");
		List result = query.list();
		return result;
	}

	@Override
	public UploadView findDataBIByYearUnique(String year) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = 'BI' AND u.year = '" + year + "'");
		UploadView result = (UploadView) query.uniqueResult();
		return result;
	}

	@Override
	public UploadView findUploadView(BigDecimal referenceId, String licenseNo,
			String year) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.referenceId = " + 
			referenceId + " AND u.licenseNo = '" + licenseNo + "' AND u.year = '" + year + "'");
		UploadView result = (UploadView) query.uniqueResult();
		return result;
	}

	@Override
	public UploadView findUploadViewUnique(String tSource,
			BigDecimal referenceId, String licenseNo, String year,
			Integer yearTo) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + tSource 
				+ "' AND u.referenceId = " + referenceId + " AND u.licenseNo = '" + licenseNo 
				+ "' AND u.year = '" + year + "' AND u.yearTo = " + yearTo);
		UploadView result = (UploadView) query.uniqueResult();
		return result;
	}

	@Override
	public UploadView findDataByViewUploadId(String viewUploadId, String licenseNo, String year,
			Integer yearTo) {
		String query = "SELECT u FROM UploadView u WHERE u.vUploadId = '" + viewUploadId
						+ "' AND u.licenseNo = '" + licenseNo + "' AND u.yearTo = " + yearTo;
		if(!year.equals("")){
			query += " AND u.year = '" + year + "'";
		}
		Query finalQuery = getSession().createQuery(query);
		UploadView result = (UploadView) finalQuery.uniqueResult();
		return result;
	}

	@Override
	public List<UploadView> findDataBIByYear(String year, String tSource) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + tSource + "' AND u.year = '" + year + "'");
		List result = query.list();
		return result;
	}

	@Override
	public List<UploadView> findDataBIByNoKM(String licenseNo, String tSource) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + tSource + "' AND u.licenseNo = '" + licenseNo + "'");
		List result = query.list();
		return result;
	}

	@Override
	public List<UploadView> findBiByDocName(String tSource, String docName) {
		Query query = getSessionFactory().getCurrentSession().createQuery("SELECT u FROM UploadView u WHERE u.tSource = '" + tSource + "' AND u.docName LIKE '%" + docName + "%'");
		List result = query.list();
		return result;
	}

	@Override
	public UploadView findDataByViewUploadId(String viewUploadId, String year) {
		Query query = getSession().createQuery("SELECT u FROM UploadView u WHERE u.vUploadId = '" + viewUploadId
				+ "' AND u.year = '" + year + "'");
		UploadView result = (UploadView) query.uniqueResult();
		return result;
	}

	public static String getTimestampStr(){
		Date dateTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy_hmmss");
		String timestamp = formatter.format(dateTime);
		
		return timestamp;
	}

}
