/**
 * 
 */
package com.balicamp.dao.hibernate.mx;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.TransactionFeeDao;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.model.mx.TransactionFeeId;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeDaoHibernate.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Repository
public class TransactionFeeDaoHibernate extends MxGenericDaoHibernate<TransactionFee, TransactionFeeId> implements
		TransactionFeeDao {

	public TransactionFeeDaoHibernate() {
		super(TransactionFee.class);
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
		hql.append("where a.endpoints.code = :epCode ");
		if(identifier != null)
			hql.append("and a.id.identifier = :identifier ");
		Query query = getSession().createQuery(hql.toString());
		if(identifier != null)
			query.setParameter("identifier", identifier);
		query.setParameter("epCode", epCode);
		query.setFirstResult(first);
		query.setMaxResults(max);
		List<?>result =  query.list();
		return (List<TransactionFee>) result;
	}
	
	@Override
	public int findAllTxFeeRowCount(String identifier, String epCode) {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from TransactionFee a ");
		hql.append("where a.endpoints.code = :epCode ");
		if(identifier != null)
			hql.append("and a.id.identifier = :identifier ");
		Query query = getSession().createQuery(hql.toString());
		if(identifier != null)
		query.setParameter("identifier", identifier);
		query.setParameter("epCode", epCode);
		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	public boolean isIdentifierExists(TransactionFee dto) {
		StringBuffer hql = new StringBuffer();
		hql.append("select tf from TransactionFee tf WHERE 1=1 ");
		hql.append(dto.getId().getIdentifier() != null && !dto.getId().getIdentifier().equals("") ? 
				"AND tf.id.identifier = :identifier " : "");
		hql.append("AND tf.id.transactionId = :txIds ");
		hql.append("AND tf.id.channelId = :channelId");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("identifier", dto.getId().getIdentifier());
		query.setParameter("txIds", dto.getId().getTransactionId());
		query.setParameter("channelId", dto.getId().getChannelId());
		return query.list().size() > 0;
	}
	
}
