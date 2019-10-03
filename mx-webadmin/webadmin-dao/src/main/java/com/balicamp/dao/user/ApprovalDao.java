package com.balicamp.dao.user;

import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.user.Approval;

public interface ApprovalDao extends GenericDao<Approval, Long> {
	List<Approval> findPriorityApproval();

	List<Approval> findPriorityApproval(String criteria, String keys, int first, int max);

	List<Approval> findPricingApproval();

	List<Approval> findPricingApproval(String keys, int first, int max);

	List<Approval> findPriorityApproval(int first, int max);

	List<Approval> findPricingApproval(int first, int max);
	
	List<Approval> findPricingApproval(String keys);

	int findCountPriorityApproval();

	int findCountPricingApproval();

	int findCountPriorityApproval(String criteria, String keys);

	int findCountPricingApproval(String keys);

	void saveEntity(Approval entity);

	void updateEntity(Approval entity);

	void saveEntity(List<Approval> entity);

	void updateEntity(List<Approval> entity);
	
	void deleteEntities(List<Approval> entities);

	Approval findPriorityByRefId(String id);

	Approval findPricingByRefId(Long id);

	boolean saveEntityWithoutCommit(Approval entity);

	boolean updateEntityWithoutCommit(Approval entity);

	boolean updateEntityWithoutCommit(List<Approval> apprs);

	boolean commitEntity();

	boolean rollbackEntity();
	
	/* cs only*/
	List<Approval> findPriorityUserApproval(Long userId);

	List<Approval> findPriorityUserApproval(Long userId, String criteria, String keys, int first, int max);
	
	List<Approval> findPriorityUserApproval(Long userId, int first, int max);

	List<Approval> findPricingUserApproval(Long userId);

	List<Approval> findPricingUserApproval(Long userId, String keys, int first, int max);
	
	List<Approval> findPricingUserApproval(Long userId,int first, int max);
	
	int findCountPriorityUserApproval(Long userId);

	int findCountPricingUserApproval(Long userId);

	int findCountPriorityUserApproval(Long userId, String criteria, String keys);

	int findCountPricingUserApproval(Long userId, String keys);
	/* spec user */
	
	Approval findPriorityForDuplicate(String data);
	
	Approval findPricingForDuplicate(String data);
}