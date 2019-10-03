package com.balicamp.dao.hibernate.mx;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.PriorityRoutingDao;
import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.util.admin.PostgreSQLSequenceGenerator;

/**
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 *
 */
@Repository
public class PriorityRoutingDaoHibernate  extends MxGenericDaoHibernate<PriorityRouting, Long> implements  PriorityRoutingDao {

	private static final Logger LOGGER = Logger.getLogger(PriorityRoutingDaoHibernate.class.getName());

	public PriorityRoutingDaoHibernate() {
		super(PriorityRouting.class);
	}

	/**
	 *
	 */
	@Override
	public void saveOrUpdatePriority(PriorityRouting entity) {
		saveOrUpdate(entity);
	}

	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PriorityRouting> findPriorityByProductCode(String productCode) {
		Query query = getSession().createQuery(
				"SELECT e FROM PriorityRouting AS e "
						+ "WHERE e.productCode=:productCode");
		query.setParameter("productCode", productCode);
		return query.list();
	}

	/**
	 *
	 */
	@Override
	public PriorityRouting findPriorityRouting(String transactionCode,
			String projectCode, String description, String productCode,
			String routingCode) {

		Query query = getSession().createQuery(
				"SELECT e FROM PriorityRouting AS e "
						+ "WHERE e.transactionCode=:transactionCode AND "
						+ "e.projectCode=:projectCode AND "
						+ "UPPER(e.description)=:description AND "
						+ "e.productCode=:productCode AND "
						+ "e.routingCode=:routingCode ");
		query.setParameter("transactionCode", transactionCode);
		query.setParameter("projectCode", projectCode);
		query.setParameter("description", description.toUpperCase());
		query.setParameter("productCode", productCode);
		query.setParameter("routingCode", routingCode);
		return (PriorityRouting) query.uniqueResult();
	}

	/**
	 *
	 */
	@Override
	public void savePriorityCollection(List<PriorityRouting> list) {
		saveCollection(list);
	}

	/**
	 *
	 */
	@Override
	public void deletePriorityCollection(Set<PriorityRouting> list) {
		super.getHibernateTemplate().deleteAll(list);
		super.getHibernateTemplate().flush();
	}

	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PriorityRouting> findPriorityForTrxFee(int nFirst, int nPageSize) {
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT e FROM PriorityRouting e where 1=1 order by transactionCode");

		Query query = getSession().createQuery(hql.toString());
		if(nFirst > 0)
			query.setFirstResult(nFirst);
		if(nPageSize > 0)
			query.setMaxResults(nPageSize);
		return query.list();
	}

	@Override
	public int getPriorityCountForFee() {
		Query query = getSession().createQuery("SELECT count(e.id) FROM PriorityRouting e ");
		return Long.valueOf(query.uniqueResult().toString()).intValue();
	}

	@Override
	public void mergePriorityCollection(List<PriorityRouting> list) {
		for(PriorityRouting pri:list){
			super.getHibernateTemplate().merge(pri);
		}
		super.getHibernateTemplate().flush();
	}

	@Override
	public PriorityRouting findByTransactionFeeCriteria(String transactionCode,
			String projectCode, String productCode) {
		Query query = getSession().createQuery(
				"SELECT e FROM PriorityRouting AS e "
						+ "WHERE e.transactionCode=:transactionCode AND "
						+ "e.projectCode=:projectCode AND "
						+ "e.productCode=:productCode ");
		query.setParameter("transactionCode", transactionCode);
		query.setParameter("projectCode", projectCode);
		query.setParameter("productCode", productCode);
		return (PriorityRouting) query.uniqueResult();
	}

	@Override
	public int getPriorityCountForFee(String desc) {
		Query query = getSession().createQuery("SELECT count(p.id) from PriorityRouting p WHERE UPPER(p.description) like :description ");
		query.setParameter("description", "%"+desc.toUpperCase()+"%");
		return ((Long) query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PriorityRouting> getPriorityForFee(String desc,  int first, int max) {
		Query query = getSession().createQuery("SELECT p from PriorityRouting p WHERE UPPER(p.description) like :description ORDER BY p.description ");
		query.setParameter("description", "%"+desc.toUpperCase()+"%");
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	@Override
	public Long getNextSeq() {
		PostgreSQLSequenceGenerator seqGenerator = new PostgreSQLSequenceGenerator(getSession());
		return seqGenerator.next("priority_routing_id_seq");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PriorityRouting> findPriorityByProductCode(String productCode,
			String billerCode, String routingCode) {
		Query query = getSession().createQuery(
				"SELECT e FROM PriorityRouting AS e "
						+ "WHERE e.productCode=:productCode AND e.projectCode=:billerCode AND e.routingCode=:routingCode");
		query.setParameter("productCode", productCode);
		query.setParameter("billerCode", billerCode);
		query.setParameter("routingCode", routingCode);
		return query.list();
	}
}