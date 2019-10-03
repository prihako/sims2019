package com.balicamp.util;

import java.util.Collection;
import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

/**
 * Class ini digunakan untuk internal transaction logs.
 *  
 * @author wayan
 *
 */

@Component
public class TransactionsQueryUtil {
	
	public void addParameters(StringBuffer hql, String txId, Object txCode, Object txName, 
			String channelCode,	Date startDate,Date endDate, String rawKey, int paramIndex) {
		
		hql.append(txCode == null ? "" : "AND txl.id.transactionCode IN (:txCode"+paramIndex+") ");
		//hql.append(txName == null ? "" : "AND tx.name IN (:txName"+paramIndex+") ");
		hql.append(txId == null ? "" : "AND txl.id.transactionId=:txId"+paramIndex+" ");
		hql.append(channelCode == null ? "" : "AND txl.id.channelCode=:channelCode"+paramIndex+" ");
		
		if (rawKey != null)
			if (rawKey.length() > 0)
				hql.append("AND txl.id.raw like :rawKey"+paramIndex+" ");
		
		//20-8-2013 edited by Sangbas
//		if(startDate != null && endDate != null) {
//			hql.append("AND txl.id.transactionTime BETWEEN :startDate"+paramIndex+" AND :endDate"+paramIndex+" ");
//		}
		
		if(startDate != null) {
			hql.append("AND txl.id.transactionTime >= :startDate"+paramIndex+" ");
		}
		
		if(endDate != null) {
			hql.append("AND txl.id.transactionTime <= :endDate"+paramIndex+" ");
		}
		
		paramIndex++;
	}
	
	@SuppressWarnings("unchecked")
	public void setQueryParameters(Query query, String txId, Object txCode, Object txName, 
			String channelCode,	Date startDate,Date endDate, String rawKey) {
		for(String p : query.getNamedParameters()) {
			if(p.startsWith("txId")) query.setParameter(p, txId);
			if(p.startsWith("txCode"))  {
				if(txCode instanceof Collection) {
					query.setParameterList(p, (Collection) txCode);
				} else {
					query.setParameter(p, txCode);
				}
			}
			/*if(p.startsWith("txName")) {
				if(txName instanceof Collection) {
					query.setParameterList(p, (Collection) txName);
				} else {
					query.setParameter(p, txName);
				}
				
			}*/
			if(p.startsWith("channelCode")) query.setParameter(p, channelCode);
			if(p.startsWith("startDate")) query.setParameter(p, startDate);
			if(p.startsWith("endDate")) query.setParameter(p, endDate);
			if(p.startsWith("rawKey")) query.setParameter(p, rawKey);
		}
	}

}
