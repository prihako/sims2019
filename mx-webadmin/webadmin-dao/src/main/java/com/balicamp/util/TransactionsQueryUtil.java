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

	public void addParameters(StringBuffer hql, String txId, Object txCode,
			String channelCode,	Date startDate,Date endDate, String rawKey, int paramIndex) {

		hql.append(txCode == null ? "" : "AND transaction_code IN (:txCode"+paramIndex+") ");
		hql.append(txId == null ? "" : "AND transaction_id=:txId"+paramIndex+" ");
		hql.append(channelCode == null ? "" : "AND channel_Code=:channelCode"+paramIndex+" ");
		if(rawKey!=null)
			if(rawKey.length()>0)
				hql.append("AND raw like :rawKey"+paramIndex+" ");
		if(startDate != null) {
			hql.append("AND transaction_time >= :startDate"+paramIndex+" ");
		}
		if(endDate != null) {
			hql.append("AND transaction_time <= :endDate"+paramIndex+" ");
		}

		paramIndex++;
	}

	public void setQueryParameters(Query query, String txId, Object txCode,
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
			if(p.startsWith("channelCode")) query.setParameter(p, channelCode);
			if(p.startsWith("startDate")) query.setParameter(p, startDate);
			if(p.startsWith("endDate")) query.setParameter(p, endDate);
			if(p.startsWith("rawKey")) query.setParameter(p, rawKey);
		}
	}

}
