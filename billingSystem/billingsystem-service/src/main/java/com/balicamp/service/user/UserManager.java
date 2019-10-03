package com.balicamp.service.user;

import java.util.Date;
import java.util.List;

import org.acegisecurity.userdetails.UserDetailsService;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.common.MethodResponse;
import com.balicamp.model.log.AuditLog;
import com.balicamp.model.user.Role;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserDisplay;
import com.balicamp.model.user.UserGroupDisplay;
import com.balicamp.model.user.UserRegister;
import com.balicamp.service.IManager;

/**
 * @version $Id: UserManager.java 113 2012-12-12 04:15:16Z bagus.sugitayasa $
 */
public interface UserManager extends IManager, UserDetailsService {

	void softReset();

	// List<Serializable> findAll();

	// void delete(Long id);

	User findById(Long id);

	void saveOrUpdate(User user);

	User findByCardnumber(String cardNumber);

	User findByUserName(String userName);

	User addNew(String fullName, Date dob, String cardNumber,
			String plainPassword, List<String> accountList);

	User addNew(User user, String plainPassword, List<String> accountList);

	User addNewAdmin(String userName, String name, String email,
			String plainPassword, Role role);

	String generateUserName(String fullName, Date birthDate);

	String generateUserName(String fullName, List<String> accountList);

	void savePassword(User user, String plainPassword);

	String encodePassword(User user, String plainPassword);

	boolean isPasswordValid(User user, String plainPassword);

	void loadUserProperties(User user, boolean initIfNull);

	void saveUserProperties(User user, boolean initIfNull, boolean saveToDb);

	MethodResponse<?> unblockUser(User user);

	List<User> findChildUserByParentId(Long parentId);

	void logLoginSuccess(User user, Date lastLoginDate, String ipAddress);

	User reRegistration(User existingUser, String fullName, Date dob,
			String plainPassword, List<String> accountList);

	String logLoginFail(User user, Date failLoginDate, String ipAddress);

	AuditLog activateUserSToken(User user, String msisdn);

	AuditLog changePassword(User user, String newPassword);

	AuditLog changeEmail(User user, String newEmail);

	String generatePassword(User user);

	String generateActivationCode(User user);

	List<UserDisplay> findUser(String userName, String email, String roleName,
			int firstResult, int maxResult);

	int findUserCount(String userName, String email, String roleName);

	List<UserDisplay> findUser(SearchCriteria searchCriteria, int firstResult,
			int maxResult);

	List<UserGroupDisplay> findAdminUser();

	List<UserGroupDisplay> findByGroupId(Long groupId);

	AuditLog linkNewCard(User user, String cardNumber, String newCardNumber,
			User actor);

	User resetUserAdminPassword(User actor, User user, String ipAddress);

	User resetUserToInquiryOnly(User user);

	AuditLog actionTransaction(User user, String type, Long TransactionId,
			String ReffNo);

	AuditLog actionTransaction(User user, Long TransactionId, String ReffNo);

	AuditLog viewTransactionHistoryFail(User user, String ipAddress);

	AuditLog viewTransferBatchHistoryFail(User user, String ipAddress);

	AuditLog viewAccountBalance(User user, String ipAddress);

	AuditLog viewAccountBalanceDetail(User user, String accountNo,
			String ipAddress);

	AuditLog viewDeposito(User user, String ipAddress);

	AuditLog viewDepositoDetail(User user, String depositoNo, String ipAddress);

	AuditLog viewMutationDetail(User user, String accountNo, String ipAddress);

	AuditLog viewTransactionHistory(User user, String ipAddress);

	AuditLog RequestHardToken(User user, String ipAddress);

	AuditLog DeleteEmail(User user, String ipAddress);

	AuditLog SendEmailToBank(User user, String ipAddress);

	AuditLog DeletePreRegisteredPayment(User user, String ipAddress);

	AuditLog addAccountSuccess(User user, String accountNo, String ipAddress);

	AuditLog addAccountFail(User user, String ipAddress);

	AuditLog activateAccountSuccess(User user, String ipAddress);

	AuditLog activateAccountFail(User user, String ipAddress);

	AuditLog viewExchangeRateList(User user, String ipAddress);

	AuditLog viewInterestRateList(User user, String ipAddress);

	AuditLog changeProfile(User user, String ipAddress);

	AuditLog adminEntryUser(User actor, User newUser, String ipAddress);

	AuditLog adminEditUser(User actor, User newUser, String ipAddress);

	AuditLog adminDeleteUser(User actor, Long deletedUserId, String ipAddress);

	AuditLog adminEntryRole(User actor, Role role, String ipAddress);

	AuditLog adminEditRole(User actor, Role role, String ipAddress);

	AuditLog adminResetRole(User actor, Role role, String ipAddress);

	AuditLog adminDeleteRole(User actor, Long deletedRoleId, String ipAddress);

	AuditLog SendEmailToCustomer(User user, String ipAddress);

	AuditLog SendBroadcastEmailToCustomer(User user, String ipAddress);

	AuditLog viewUserIb(User user, String ipAddress);

	AuditLog changeUserSignUpStatus(User actor, UserRegister user,
			String ipAddress);

	AuditLog testSystemMonitor(User user, String testType, String ipAddress);

	AuditLog viewUserLogin(User user, String ipAddress);

	AuditLog kickUser(User actor, User user, String ipAddress);

	AuditLog csResetUser(User actor, User user, String ipAddress);

	AuditLog csBlockUser(User actor, User user, String ipAddress);

	AuditLog viewTransactionPage(User user, String ipAddress);

	AuditLog viewTransactionFailPage(User user, String ipAddress);

	AuditLog viewSMSLog(User user, String Biller, String ipAddress);

	AuditLog viewAuditLog(User user, String ipAddress);

	AuditLog tryingLogin(String ipAddress);
	
	AuditLog viewInitialInvoiceSearch(User user, String ipAddress);
	
	AuditLog viewInitialInvoiceCreate(User user, String ipAddress, String BhpMethod, String licenseNo);
	
	AuditLog saveInvoice(User user, String ipAddress, String saveMethod, String licenseNo, String invoiceNo, String bhpMethod);
	
	AuditLog viewManageInvoiceSearch(User user, String ipAddress);
	
	AuditLog viewManageInvoiceView(User user, String ipAddress, String BhpMethod, String licenseNo, boolean isFine);
	
	AuditLog viewManageInvoiceEdit(User user, String ipAddress, String bhpMethod, String licenseNo);
	
	AuditLog viewSimulasi(User user, String ipAddress, String desc);
	
	AuditLog biRateAuditLog(User user, String ipAddress, String desc, String year);
	
	AuditLog bhpRateAuditLog(User user, String ipAddress, String desc, String year);
	
	AuditLog viewHistoryInvoice(User user, String ipAddress);
	
	AuditLog uploadDokumen(User user, String ipAddress, String desc);
	
	AuditLog userAuditLog(User user, String ipAddress, String userName, boolean isAddNew);
	
	AuditLog changePasswordAuditLog(User user, String ipAddress);

	Integer findByCriteriaCount(SearchCriteria searchCriteria);

	void save(User memberUser);

	String encodeResetPassword(User user);
}
