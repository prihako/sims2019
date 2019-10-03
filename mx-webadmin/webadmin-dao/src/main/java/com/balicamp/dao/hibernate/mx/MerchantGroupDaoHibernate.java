package com.balicamp.dao.hibernate.mx;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.MerchantGroupDao;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.MerchantGroupDetails;

@Repository
public class MerchantGroupDaoHibernate extends
		MxGenericDaoHibernate<MerchantGroup, Long> implements MerchantGroupDao {
	private static final Logger LOGGER = Logger
			.getLogger(MerchantGroupDaoHibernate.class.getName());
	public static final String DETAIL = "MerchantGroupDetails";

	public MerchantGroupDaoHibernate() {
		super(MerchantGroup.class);
	}

	@Override
	public MerchantGroup findMerchantGroup(final String code,
			final String description) {
		Query query = getSession().createQuery(
				"SELECT e FROM MerchantGroup AS e " + "WHERE e.code=:code AND "
						+ "UPPER(e.description)=:description ");
		query.setParameter("code", code);
		query.setParameter("description", description.toUpperCase());
		return (MerchantGroup) query.uniqueResult();
	}

	@Override
	public void deleteMerchantCollection(Set<MerchantGroup> list) {
		super.getHibernateTemplate().deleteAll(list);
		super.getHibernateTemplate().flush();
	}

	@Override
	public boolean detailIsExist(Set<MerchantGroup> list) {
		boolean stat = false;
		try {
			if(list != null){
				for(MerchantGroup mcg:list){
					List<MerchantGroupDetails> details = findMerchantDetails(mcg);
					if(details != null && details.size() > 0){
						stat = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return stat;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantGroupDetails> findMerchantDetails(MerchantGroup merchant) {
		Query query = getSession().createQuery(
				"SELECT e FROM MerchantGroupDetails AS e " + "WHERE e.merchantGroupId=:merchantGroupId ");
		query.setParameter("merchantGroupId", merchant.getId());
		return query.list();
	}

	@Override
	public MerchantGroup findMerchantGroup(String code) {
		try {
			Query query = getSession().createQuery(
					"SELECT e FROM MerchantGroup AS e " + "WHERE e.code=:code ");
			query.setParameter("code", code);
			return (MerchantGroup) query.uniqueResult();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,e.getMessage(),e);
		}
		return null;
	}


}
