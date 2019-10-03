package com.balicamp.service.merchant;

import java.util.List;
import java.util.Set;

import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.MerchantGroupDetails;

public interface MerchantDetailManager {
	boolean saveOrUpdateDetail(MerchantGroupDetails detail);

	MerchantGroupDetails findDetailsByAllField(MerchantGroup merchant, String termid, String channelCode);

	boolean deleteDetails(Set<MerchantGroupDetails> details);

	MerchantGroupDetails findDetailById(Long id);

	MerchantGroup findMerchantGroupById(Long id);

	int findDetailCountByCriteria(String criteria, String keys);

	List<MerchantGroupDetails> findDetailsByCriteria(String criteria, String keys, int first, int max);
}
