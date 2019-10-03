package com.balicamp.service.user;

import java.util.List;
import java.util.Set;

import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.model.user.Approval;
import com.balicamp.model.user.User;
import com.balicamp.service.GenericManager;

public interface ApprovalManager extends GenericManager<Approval, Long> {
	List<Approval> findPriorityApproval();

	List<Approval> findPriorityApproval(String criteria, String keys, int first, int max);

	List<Approval> findPricingApproval();

	List<Approval> findPricingApproval(String keys, int first, int max);

	int findCountPriorityApproval();

	int findCountPricingApproval();

	int findCountPriorityApproval(String criteria, String keys);

	int findCountPricingApproval(String keys);

	List<Approval> findPriorityApproval(int first, int max);

	List<Approval> findPricingApproval(int first, int max);

	boolean saveOrUpdatePriority(PriorityRouting priority, User user, int processFlag);
	
	boolean deleteAllPriorities(Set<PriorityRouting> priority, User user, int processFlag);

	boolean saveOrUpdatePricing(List<TransactionFee> trxFee, User user, int processFlag);

	PriorityRouting getDataPriority(String data);

	PriorityRouting getDataPriority(Long refId);

	Approval getDataByRefId(Long refId);

	List<Approval> getDataAppPriority(Set<PriorityRouting> selectedData);

	boolean updateApprs(List<Approval> apprs, User userLoginFromSession, int appStatus);

	boolean updateApprs(Approval appr, User userLoginFromSession, int appStatus);

	boolean commitEntity();

	boolean rollbackEntity();
	
	TransactionFee getDataPricingByData(String data);
	
	List<TransactionFee> getListPricingByKeys(String keys);
	
	List<Approval> getPricingDataByRefId(String keys);
	
	/* cs only*/
	List<Approval> findPriorityUserApproval(Long userId);

	List<Approval> findPriorityUserApproval(Long userId, String criteria, String keys, int first, int max);
	
	List<Approval> findPriorityUserApproval(Long userId, int first, int max);

	List<Approval> findPricingUserApproval(Long userId);

	List<Approval> findPricingUserApproval(Long userId, String keys, int first, int max);
	
	List<Approval> findPricingUserApproval(Long userId, int first, int max);
	
	int findCountPriorityUserApproval(Long userId);

	int findCountPricingUserApproval(Long userId);

	int findCountPriorityUserApproval(Long userId, String criteria, String keys);

	int findCountPricingUserApproval(Long userId, String keys);
	/* cs only */
}