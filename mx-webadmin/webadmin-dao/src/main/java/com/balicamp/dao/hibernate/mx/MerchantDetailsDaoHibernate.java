package com.balicamp.dao.hibernate.mx;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.MerchantDetailsDao;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.MerchantGroupDetails;

@Repository
public class MerchantDetailsDaoHibernate extends MxGenericDaoHibernate<MerchantGroupDetails, Long> implements
		MerchantDetailsDao {

	public MerchantDetailsDaoHibernate() {
		super(MerchantGroupDetails.class);
	}

	private static final Logger LOGGER = Logger
			.getLogger(MerchantDetailsDaoHibernate.class.getName());

	@Override
	public void saveOrUpdateDetail(MerchantGroupDetails detail) {
		super.saveOrUpdate(detail);
	}

	@Override
	public MerchantGroupDetails findDetailsByAllField(Long id, String termid,
			String channelCode) {
		Query query = getSession().createQuery(
				"SELECT e FROM MerchantGroupDetails AS e "
						+ "WHERE e.merchantGroupId=:merchantGroupId AND "
						+ "e.termid=:termid AND "
						+ "e.channelCode=:channelCode");
		query.setParameter("merchantGroupId", id);
		query.setParameter("termid", termid);
		query.setParameter("channelCode", channelCode);
		return (MerchantGroupDetails) query.uniqueResult();
	}

	@Override
	public MerchantGroup findMerchantGroup(Long id) {
		Query query = getSession().createQuery(
				"SELECT e FROM MerchantGroup AS e "
						+ "WHERE e.id=:id ");
		query.setParameter("id", id);
		return (MerchantGroup) query.uniqueResult();
	}

	@Override
	public void deleteDetails(Set<MerchantGroupDetails> details) {
		super.getHibernateTemplate().deleteAll(details);
		super.getHibernateTemplate().flush();
	}

	@Override
	public int findDetailCountByCriteria(String criteria, String keys) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT count(m.id) from MerchantGroupDetails m WHERE ");

		if(criteria.equalsIgnoreCase("merchantGroup")){
			str.append("m.merchantGroupId in (SELECT id FROM  MerchantGroup WHERE UPPER(code) like :code )");
		} else {
			str.append("UPPER(m.").append(criteria).append(") like :keys ");
		}


		Query query = getSession().createQuery(str.toString());

		if(criteria.equalsIgnoreCase("merchantGroup")){
			query.setParameter("code", "%"+keys.toUpperCase()+"%");
		} else {
			query.setParameter("keys", "%"+keys.toUpperCase()+"%");
		}
		return ((Long) query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantGroupDetails> findDetailsByCriteria(String criteria,
			String keys, int first, int max) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT m from MerchantGroupDetails m WHERE ");

		if(criteria.equalsIgnoreCase("merchantGroup")){
			str.append("m.merchantGroupId in (SELECT id FROM  MerchantGroup WHERE UPPER(code) like :code )");
		} else {
			str.append("UPPER(m.").append(criteria).append(") like :keys ");
		}


		Query query = getSession().createQuery(str.toString());

		if(criteria.equalsIgnoreCase("merchantGroup")){
			query.setParameter("code", "%"+keys.toUpperCase()+"%");
		} else {
			query.setParameter("keys", "%"+keys.toUpperCase()+"%");
		}
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

}
