package com.balicamp.dao.hibernate.user;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.user.ApprovalDao;
import com.balicamp.model.user.Approval;

@Repository
public class ApprovalDaoHibernate extends AdminGenericDaoImpl<Approval, Long>
		implements ApprovalDao {

	private static final Logger LOGGER = Logger
			.getLogger(ApprovalDaoHibernate.class.getSimpleName());
	private static final int PRIORITY_FLAG = 1;
	private static final int PRICING_FLAG = 2;

	public ApprovalDaoHibernate() {
		super(Approval.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPriorityApproval() {
		Query query = getSession().createQuery(
				"SELECT e FROM Approval AS e WHERE e.flag=:flag AND e.status!=:status AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("status", 1);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPriorityApproval(String criteria, String keys,
			int first, int max) {
		Query query = getSession()
				.createQuery(
						"SELECT e FROM Approval AS e WHERE  e.flag=:flag AND UPPER(e.data) like :data AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		query.setParameter("data",
				"%" + criteria.toUpperCase() + "%" + keys.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPricingApproval() {
		Query query = getSession().createQuery(
				"SELECT e FROM Approval AS e WHERE e.flag=:flag AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPricingApproval(String keys,
			int first, int max) {
		Query query = getSession()
				.createQuery(
						"SELECT e FROM Approval AS e WHERE  e.flag=:flag AND UPPER(e.data) like :data AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		query.setParameter("data", "%" + keys.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	@Override
	public int findCountPriorityApproval() {
		Query query = getSession().createQuery(
				"SELECT COUNT(e.id) FROM Approval AS e WHERE e.flag=:flag AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	public int findCountPricingApproval() {
		Query query = getSession().createQuery(
				"SELECT COUNT(e.id) FROM Approval AS e WHERE e.flag=:flag AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	public int findCountPriorityApproval(String criteria, String keys) {
		Query query = getSession()
				.createQuery(
						"SELECT COUNT(e.id) FROM Approval AS e WHERE  e.flag=:flag AND UPPER(e.data) like :data AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		query.setParameter("data",
				"%" + criteria.toUpperCase() + "%" + keys.toUpperCase() + "%");
		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	public int findCountPricingApproval(String keys) {
		Query query = getSession()
				.createQuery(
						"SELECT COUNT(e.id) FROM Approval AS e WHERE  e.flag=:flag AND UPPER(e.data) like :data AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		query.setParameter("data", "%" + keys.toUpperCase() + "%");
		return ((Long) query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPriorityApproval(int first, int max) {
		Query query = getSession().createQuery(
				"SELECT e FROM Approval AS e WHERE e.flag=:flag AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPricingApproval(int first, int max) {
		Query query = getSession().createQuery(
				"SELECT e FROM Approval AS e WHERE e.flag=:flag AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	@Override
	public void saveEntity(Approval entity) {
		setIdentifier(entity);
		super.getHibernateTemplate().save(entity);
		super.getHibernateTemplate().flush();
	}

	@Override
	public void updateEntity(Approval entity) {
		super.getHibernateTemplate().update(entity);
		super.getHibernateTemplate().flush();
	}

	@Override
	public Approval findPriorityByRefId(String id) {
		Query query = getSession()
				.createQuery(
						"SELECT e FROM Approval AS e WHERE e.flag=:flag AND e.status!=:status AND e.status!=:reject AND e.refId=:refId");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		query.setParameter("refId", id);
		return (Approval) query.uniqueResult();
	}

	@Override
	public Approval findPricingByRefId(Long id) {
		Query query = getSession()
				.createQuery(
						"SELECT e FROM Approval AS e WHERE e.flag=:flag AND e.refId=:refId");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("refId", id);
		return (Approval) query.uniqueResult();
	}

	@Override
	public boolean saveEntityWithoutCommit(Approval entity) {
		boolean stat = false;
		try {
			getSession().beginTransaction();
			getSession().persist(entity);
			stat = true;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	@Override
	public boolean updateEntityWithoutCommit(Approval entity) {
		boolean stat = false;
		try {
			getSession().beginTransaction();
			getSession().merge(entity);
			stat = true;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	@Override
	public boolean commitEntity() {
		boolean stat = false;
		try {
			getSession().beginTransaction();
			getSession().getTransaction().commit();
			stat = true;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	@Override
	public boolean rollbackEntity() {
		boolean stat = false;
		try {
			getSession().beginTransaction();
			getSession().getTransaction().rollback();
			stat = true;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	@Override
	public boolean updateEntityWithoutCommit(List<Approval> apprs) {
		boolean stat = false;
		try {
			getSession().beginTransaction();
			for (Approval appr : apprs) {
				getSession().merge(appr);
			}
			stat = true;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	@Override
	public void saveEntity(List<Approval> apprs) {
		setIdentifier(apprs);
		for(Approval appr: apprs){
			super.getHibernateTemplate().save(appr);
		}
		super.getHibernateTemplate().flush();

	}

	@Override
	public void updateEntity(List<Approval> apprs) {
		super.setIdentifier(apprs);
		for(Approval appr: apprs){			
			super.getHibernateTemplate().merge(appr);			
		}
		super.getHibernateTemplate().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPricingApproval(String keys) {
		Query query = getSession()
				.createQuery(
						"SELECT e FROM Approval AS e WHERE  e.flag=:flag AND UPPER(e.data) like :data AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		query.setParameter("data", "%" + keys.toUpperCase() + "%");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPriorityUserApproval(Long userId) {
		Query query = getSession().createQuery(
				"SELECT e FROM Approval AS e WHERE e.flag=:flag AND e.createdBy=:user");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("user", userId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPriorityUserApproval(Long userId, String criteria,
			String keys, int first, int max) {
		Query query = getSession()
				.createQuery(
						"SELECT e FROM Approval AS e WHERE  e.flag=:flag AND UPPER(e.data) like :data AND e.createdBy=:user");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("user", userId);
		query.setParameter("data",
				"%" + criteria.toUpperCase() + "%" + keys.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPricingUserApproval(Long userId) {
		Query query = getSession().createQuery(
				"SELECT e FROM Approval AS e WHERE e.flag=:flag AND e.createdBy=:user");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("user", userId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPricingUserApproval(Long userId, String keys,
			int first, int max) {
		Query query = getSession()
				.createQuery(
						"SELECT e FROM Approval AS e WHERE  e.flag=:flag AND UPPER(e.data) like :data AND e.createdBy=:user");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("user", userId);
		query.setParameter("data", "%" + keys.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	@Override
	public int findCountPriorityUserApproval(Long userId) {
		Query query = getSession().createQuery(
				"SELECT COUNT(e.id) FROM Approval AS e WHERE e.flag=:flag AND e.createdBy=:user");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("user", userId);
		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	public int findCountPricingUserApproval(Long userId) {
		Query query = getSession().createQuery(
				"SELECT COUNT(e.id) FROM Approval AS e WHERE e.flag=:flag AND e.createdBy=:user");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("user", userId);
		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	public int findCountPriorityUserApproval(Long userId, String criteria,
			String keys) {
		Query query = getSession()
				.createQuery(
						"SELECT COUNT(e.id) FROM Approval AS e WHERE  e.flag=:flag AND UPPER(e.data) like :data AND e.createdBy=:user");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("user", userId);
		query.setParameter("data",
				"%" + criteria.toUpperCase() + "%" + keys.toUpperCase() + "%");
		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	public int findCountPricingUserApproval(Long userId, String keys) {
		Query query = getSession()
				.createQuery(
						"SELECT COUNT(e.id) FROM Approval AS e WHERE  e.flag=:flag AND UPPER(e.data) like :data AND e.createdBy=:user");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("user", userId);
		query.setParameter("data", "%" + keys.toUpperCase() + "%");
		return ((Long) query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPriorityUserApproval(Long userId, int first,
			int max) {
		Query query = getSession().createQuery(
				"SELECT e FROM Approval AS e WHERE e.flag=:flag AND e.createdBy=:user");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("user", userId);
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Approval> findPricingUserApproval(Long userId, int first,
			int max) {
		Query query = getSession().createQuery(
				"SELECT e FROM Approval AS e WHERE e.flag=:flag AND e.createdBy=:user");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("user", userId);
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	@Override
	public Approval findPriorityForDuplicate(String data) {
		Query query = getSession()
				.createQuery(
						"SELECT e FROM Approval AS e WHERE  e.flag=:flag AND UPPER(e.data) = :data AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRIORITY_FLAG);
		query.setParameter("data", data.toUpperCase());
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		return (Approval) query.uniqueResult();
	}

	@Override
	public Approval findPricingForDuplicate(String data) {
		Query query = getSession()
				.createQuery(
						"SELECT e FROM Approval AS e WHERE  e.flag=:flag AND e.refId = :data AND e.status!=:status AND e.status!=:reject");
		query.setParameter("flag", PRICING_FLAG);
		query.setParameter("data", data);
		query.setParameter("status", 1);
		query.setParameter("reject", 2);
		return (Approval) query.uniqueResult();
	}

	@Override
	public void deleteEntities(List<Approval> entities) {
		super.getHibernateTemplate().deleteAll(entities);
		super.getHibernateTemplate().flush();		
	}
}