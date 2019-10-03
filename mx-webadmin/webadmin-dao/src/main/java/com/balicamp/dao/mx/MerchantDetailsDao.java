package com.balicamp.dao.mx;

import java.util.List;
import java.util.Set;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.MerchantGroupDetails;

public interface MerchantDetailsDao extends GenericDao<MerchantGroupDetails, Long>{
	void saveOrUpdateDetail(MerchantGroupDetails detail);

	MerchantGroupDetails findDetailsByAllField(Long id, String termid, String channelCode);

	MerchantGroup findMerchantGroup(Long id);

	void deleteDetails(Set<MerchantGroupDetails> details);

	int findDetailCountByCriteria(String criteria, String keys);

	List<MerchantGroupDetails> findDetailsByCriteria(String criteria, String keys, int first, int max);
}
