package com.balicamp.dao.mx;

import java.util.List;
import java.util.Set;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.MerchantGroupDetails;

public interface MerchantGroupDao extends GenericDao<MerchantGroup, Long> {
	MerchantGroup findMerchantGroup(final String code, final String description);

	MerchantGroup findMerchantGroup(final String code);

	void deleteMerchantCollection(final Set<MerchantGroup> list);

	boolean detailIsExist(final Set<MerchantGroup> list);

	List<MerchantGroupDetails> findMerchantDetails(final MerchantGroup merchant);
}
