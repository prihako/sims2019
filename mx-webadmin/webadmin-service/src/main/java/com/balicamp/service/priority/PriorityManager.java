package com.balicamp.service.priority;

import java.util.List;
import java.util.Set;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.model.mx.TransactionFee;

/**
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 * @version
 */
public interface PriorityManager {
	boolean saveOrUpdatePriority(PriorityRouting priority, boolean newData);

	Long getPriorityId(String transactionCode, String projectCode, String description, String productCode,
			String routingCode);

	List<PriorityRouting> findPriority(SearchCriteria searchCriteria, int first, int pageSize);

	Integer findByCriteriaCount(SearchCriteria searchCriteria);

	PriorityRouting findPriorityRouting(String transactionCode, String projectCode, String description,
			String productCode, String routingCode);

	PriorityRouting findById(Long id);

	boolean deletePriorities(Set<PriorityRouting> priorities);

	List<PriorityRouting> findPriorityForTrxFee(int nFirst, int nPageSize);

	int getPriorityCountForFee();

	PriorityRouting findByTransactionFeeCriteria(String transactionCode, String projectCode, String productCode);

	int getPriorityCountForFee(String desc);

	List<PriorityRouting> getPriorityForFee(String desc, int first, int max);

	Long getNextSeq();
	
	List<PriorityRouting> findByProductCode(PriorityRouting priority);
}