package com.balicamp.service.merchant;

import java.util.List;
import java.util.Set;

import com.balicamp.model.mx.MerchantGroup;

public interface MerchantManager {
	boolean saveOrUpdateMerchant(MerchantGroup merchant, boolean newData);

	MerchantGroup findMerchantGroup(final String code, final String description);

	MerchantGroup findMerchantGroup(final String code);

	boolean deleteMerchants(Set<MerchantGroup> merchants);

	boolean detailIsExist(Set<MerchantGroup> merchants);

	MerchantGroup findMerchantById(final Long id);

	List<MerchantGroup> findAllMerchantGroup();
}
