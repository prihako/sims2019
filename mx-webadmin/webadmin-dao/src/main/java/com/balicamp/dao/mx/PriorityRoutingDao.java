package com.balicamp.dao.mx;

import java.util.List;
import java.util.Set;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.PriorityRouting;

/**
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 *
 */
public interface PriorityRoutingDao extends GenericDao<PriorityRouting, Long> {

	void saveOrUpdatePriority(PriorityRouting entity);

	void setIdentifier(PriorityRouting entity);

	List<PriorityRouting> findPriorityByProductCode(String productCode);
	
	List<PriorityRouting> findPriorityByProductCode(String productCode, String billerCode, String routingCode);

	PriorityRouting findPriorityRouting(String transactionCode, String projectCode, String description, String productCode, String routingCode);

	void savePriorityCollection(List<PriorityRouting> list);

	void deletePriorityCollection(Set<PriorityRouting> list);

	List<PriorityRouting> findPriorityForTrxFee(int nFirst, int nPageSize);

	int getPriorityCountForFee();

	void mergePriorityCollection(List<PriorityRouting> list);

	PriorityRouting findByTransactionFeeCriteria(String transactionCode, String projectCode, String productCode);

	int getPriorityCountForFee(String desc);

	List<PriorityRouting> getPriorityForFee(String desc,  int first, int max);

	Long getNextSeq();
}