/**
 *
 */
package com.balicamp.dao.hibernate.mx;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.TransactionFeeDao;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.model.mx.TransactionFeeId;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeDaoHibernate.java 518 2013-06-25 10:13:37Z rudi.sadria $
 */
@Repository
public class TransactionFeeDaoHibernate extends MxGenericDaoHibernate<TransactionFee, TransactionFeeId> implements
		TransactionFeeDao {

	private static final Logger LOGGER = Logger.getLogger("TransactionFeeDaoHibernate");
	
	public TransactionFeeDaoHibernate() {
		super(TransactionFee.class);
	}
	
	@Override
	public void updateTrxFee(TransactionFee entity) {
		try {
			Query query = getSession().createQuery(
					"update TransactionFee t SET t.id.fee=:fee "
					+ " WHERE t.id.identifier=:identifier ");
			query.setParameter("fee", entity.getId().getFee());
			query.setParameter("identifier", entity.getId().getIdentifier());
			query.executeUpdate();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}
	}

	@Override
	public int deleteTransactionFeeByIds(int transactionId, int channelId, String identifier) {
		Query query = getSession().createQuery(
				"Delete TransactionFee as t WHERE t.id.transactionId=:transactionId "
						+ "AND t.id.channelId=:channelId AND t.id.identifier=:identifier");
		query.setInteger("transactionId", transactionId);
		query.setInteger("channelId", channelId);
		query.setString("identifier", identifier);
		return query.executeUpdate();
	}

	@Override
	public void updateTransactionFeeByIds(TransactionFee transactionFee) {
		Query query = getSession().createQuery(
				"UPDATE TransactionFee as t SET t.id.fee=:fee WHERE t.id.transactionId=:transactionId "
						+ "AND t.id.channelId=:channelId AND t.id.identifier=:identifier");
		query.setInteger("fee", transactionFee.getId().getFee());
		query.setInteger("transactionId", transactionFee.getId().getTransactionId());
		query.setInteger("channelId", transactionFee.getId().getChannelId());
		query.setString("identifier", transactionFee.getId().getIdentifier());
		query.executeUpdate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Endpoints> getEndpointsByCodes(List<Integer> channelCodes) {
		StringBuffer hql = new StringBuffer();
		hql.append("select e from Endpoints e where 1=1 ");
		if(channelCodes != null && channelCodes.size() >  0) {
			hql.append("AND e.code in ");
			String codes = "";
			for(Integer code : channelCodes){
				codes += code+", ";
			}
			hql.append(codes.subSequence(0, codes.length()-2));
		}

		Query query = getSession().createQuery(hql.toString());
		return query.list();
	}

	@Override
	public void saveAll(List<TransactionFee> allEntities) {
		getHibernateTemplate().saveOrUpdateAll(allEntities);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransactionFee> findAllTxFee(String identifier, String epCode, int first, int max) {
		StringBuffer hql = new StringBuffer();
		hql.append("select a from TransactionFee a ");
		
		if(identifier != null){
			hql.append("where a.id.identifier like :identifier ");
		}
		
		if(epCode != null){
			hql.append("and a.endpoints.code = :epCode ");
		}		
		hql.append("order by identifier ");
		Query query = getSession().createQuery(hql.toString());
		
		if(identifier != null){
			query.setParameter("identifier", "%"+identifier+"%");
		}
		
		if(epCode != null){
			query.setParameter("epCode", epCode);
		}
		
		query.setFirstResult(first);
		query.setMaxResults(max);
		List<?>result =  query.list();
		return (List<TransactionFee>) result;
	}

	@Override
	public int findAllTxFeeRowCount(String identifier, String epCode) {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from TransactionFee a ");
		
		if(identifier != null)
			hql.append("where a.id.identifier like :identifier ");
		
		if(epCode != null && !epCode.equals("")){
			hql.append("and a.endpoints.code = :epCode ");
		}	
		
		
		Query query = getSession().createQuery(hql.toString());
		if(identifier != null)
		query.setParameter("identifier", "%"+identifier+"%");
		
		if(epCode != null && !epCode.equals("")){
			query.setParameter("epCode", epCode);
		}		
		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	public boolean isIdentifierExists(TransactionFee dto) {
		StringBuffer hql = new StringBuffer();

		hql.append("select tf from TransactionFee tf WHERE 1=1 ");
		if(dto.getId().getIdentifier() != null && !dto.getId().getIdentifier().equals(""))
			hql.append("AND tf.id.identifier = :identifier ");

		hql.append("AND tf.id.transactionId = :txIds ");
		hql.append("AND tf.id.channelId = :channelId");
		Query query = getSession().createQuery(hql.toString());
		if(dto.getId().getIdentifier() != null && !dto.getId().getIdentifier().equals(""))
			query.setParameter("identifier", dto.getId().getIdentifier());
		query.setParameter("txIds", dto.getId().getTransactionId());
		query.setParameter("channelId", dto.getId().getChannelId());
		return query.list().size() > 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransactionFee> findAllTxFee(String identifier, String epCode,
			String description, int first, int max) {

		/*
		 * SELECT * FROM public.transaction_fee
where channel_id = 1 AND
identifier like '%MERCHANT_TOP%' AND transaction_id in
(select id as transaction_id from transactions where code in
(select transaction_code as code from priority_routing where description like '%simpati 25%')
)
		 * */
		StringBuffer hql = new StringBuffer("SELECT a from TransactionFee a ");
		hql.append("WHERE a.endpoints.code = :epCode ");

		if(identifier != null && !identifier.equals("")){
			hql.append("AND a.id.identifier like :identifier ");
		}

		if(description != null && !description.equals("")){
			hql.append("AND a.id.transactionId in  "
					+ "(SELECT t.id FROM Transactions t, TransactionFee a WHERE t.id = a.id.transactionId AND "
					+ "t.code in "
					+ "(SELECT p.transactionCode FROM PriorityRouting p, Transactions t WHERE t.code = p.transactionCode AND "
					+ "p.description like :description)"
					+ ") ");
		}

//		hql.append("select a from TransactionFee a ");
//		hql.append("where a.endpoints.code = :epCode ");
//		if(identifier != null)
//			hql.append("and a.id.identifier like :identifier ");
//
//		if(description != null && !description.equals("")){
//			hql.append("and a.id.priority.description like :description ");
//		}

		hql.append("order by identifier ");
		Query query = getSession().createQuery(hql.toString());
		if(identifier != null)
			query.setParameter("identifier", "%"+identifier+"%");
		if(description != null && !description.equals("")){
			query.setParameter("description", "%"+description+"%");
		}
		query.setParameter("epCode", epCode);

		query.setFirstResult(first);
		query.setMaxResults(max);
		List<?>result =  query.list();
		return (List<TransactionFee>) result;
	}

	@Override
	public int findAllTxFeeRowCount(String identifier, String epCode,
			String description) {
//		StringBuffer hql = new StringBuffer();
//		hql.append("select count(*) from TransactionFee a ");
//		hql.append("where a.endpoints.code = :epCode ");
//		if(identifier != null)
//			hql.append("and a.id.identifier like :identifier ");
//		if(description != null && !description.equals("")){
//			hql.append("and a.id.priority.description like :description ");
//		}

		StringBuffer hql = new StringBuffer("SELECT count(*) from TransactionFee a ");
		hql.append("WHERE a.endpoints.code = :epCode ");

		if(identifier != null && !identifier.equals("")){
			hql.append("AND a.id.identifier like :identifier ");
		}

		if(description != null && !description.equals("")){
			hql.append("AND a.id.transactionId in  "
					+ "(SELECT t.id FROM Transactions t, TransactionFee a WHERE t.id = a.id.transactionId AND "
					+ "t.code in "
					+ "(SELECT p.transactionCode FROM PriorityRouting p, Transactions t WHERE t.code = p.transactionCode AND "
					+ "p.description like :description)"
					+ ") ");
		}

		Query query = getSession().createQuery(hql.toString());
		if(identifier != null)
		query.setParameter("identifier", "%"+identifier+"%");
		if(description != null && !description.equals("")){
			query.setParameter("description", "%"+description+"%");
		}
		query.setParameter("epCode", epCode);
		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	public boolean isIdentifierExists(String identifier) {
		StringBuffer hql = new StringBuffer("SELECT a from TransactionFee a ");
		hql.append("WHERE a.id.identifier =:identifier ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("identifier", identifier);
		TransactionFee trxFee = (TransactionFee) query.uniqueResult();
		if(trxFee == null){
			return false;
		} else {
			return true;
		}
	}

	@Override
	public TransactionFee findTransactionFee(int trxId, String epCode, String identifier) {
		StringBuffer hql = new StringBuffer("SELECT a from TransactionFee a ");
		hql.append("WHERE a.transactions.id =:trxId AND a.endpoints.code = :epCode AND identifier = :identifier");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("trxId", trxId);
		query.setParameter("epCode", epCode);
		query.setParameter("identifier", identifier);
		return (TransactionFee) query.uniqueResult();
	}

	@Override
	public boolean isIdentifierExists(int trxId, String epCode,
			String identifier) {
		StringBuffer hql = new StringBuffer("SELECT a from TransactionFee a ");
		hql.append("WHERE a.transactions.id =:trxId AND "
				+ "a.endpoints.code =:epCode AND "
				+ "a.id.identifier =:identifier ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("trxId", trxId);
		query.setParameter("epCode", epCode);
		query.setParameter("identifier", identifier);

		TransactionFee trxFee = (TransactionFee) query.uniqueResult();
		if(trxFee == null){
			return false;
		} else {
			return true;
		}
	}
}
