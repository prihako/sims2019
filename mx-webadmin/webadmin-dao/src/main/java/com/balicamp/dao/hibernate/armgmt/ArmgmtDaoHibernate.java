package com.balicamp.dao.hibernate.armgmt;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.armgmt.ArmgmtDao;
import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.admin.BaseAdminModel;

@Repository
public class ArmgmtDaoHibernate extends
		ArmgmtGenericDaoHibernate<BaseAdminModel, String> implements
		ArmgmtDao {
	
	public ArmgmtDaoHibernate() {
		super(BaseAdminModel.class);
	}

	@SuppressWarnings({ })
	@Override
	@Transactional
	public HashMap<String, Object[]> getMT940(Date transDate, Date transEndDate, String paymentType, String bankName) {
		
		HashMap<String, Object[]> mapResult = null;
		
		String sql = "select filename,"
				+ "to_char(parse_date,'dd-MON-YYYY') parse_date, "
				+ "to_char(t.transaction_date,'dd-MON-YYYY') transaction_date, "
				+ "t.client_id, "
				+ "t.invoice_id, "
				+ "t.payment_channel, "
				+ "t.branch_code, "
				+ "t.transaction_amount, "
				+ "t.error_desc, "
				+ "ll.bank_name, "
				+ "t.RAW_TRANSACTION_MSG "
				+ "from transaction t "
				+ "join transaction_file tf on t.file_id = tf.file_id "
				+ "join parser_log pl on tf.file_id = pl.file_id "
				+ "join listener_log ll on ll.listener_log_id = tf.listener_log_id "
				+ "where "
				+ "t.transaction_code = :paymentType "
				+ "and t.payment_type = 'h2h' "
				+ "and ll.bank_name = :bankName "
				+ "and error_desc is null ";
		
		if(transEndDate !=null){
			sql += "and ll.transaction_date between :transDate and :transEndDate ";
		}else{
			sql += "and ll.transaction_date = :transDate ";
		}
			
		String orderByQuery = "order by ll.received_date desc ";
		sql += orderByQuery;

		Query query = getSession().createSQLQuery(sql.toString());
		query.setParameter("transDate", new java.sql.Date(transDate.getTime()));
		query.setParameter("paymentType", paymentType.toString());
		query.setParameter("bankName", bankName.toLowerCase());
		
		if(transEndDate !=null){
			query.setParameter("transEndDate", new java.sql.Date(transEndDate.getTime()));
		}
		
		@SuppressWarnings("unchecked")
		List<Object> result = query.list();
		if (result != null) {
			mapResult = new HashMap<String, Object[]>();
			for (Object obj : result) {
				Object[] objctArray = (Object[]) obj;
				mapResult.put((String) objctArray[4], objctArray);
			}
		}
		return mapResult;
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
}
