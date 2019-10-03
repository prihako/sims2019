package com.balicamp.service.impl.user;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.user.ApprovalDao;
import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.model.user.Approval;
import com.balicamp.model.user.User;
import com.balicamp.service.common.ObjectSerializerManager;
import com.balicamp.service.impl.GenericManagerImpl;
import com.balicamp.service.impl.common.ObjectSerializerManagerXStream;
import com.balicamp.service.user.ApprovalManager;

@Service("approvalManager")
public class ApprovalManagerImpl extends GenericManagerImpl<Approval, Long>
		implements ApprovalManager {

	private static final Logger LOGGER = Logger
			.getLogger(ApprovalManagerImpl.class.getSimpleName());
	private static final int PRIORITY_FLAG = 1;
	private static final int PRICING_FLAG = 2;
	public static final int WAITING = 0;
	public static final int APPROVE = 1;
	public static final int REJECT = 2;

	public static final int DELETE = 2;

	private final ApprovalDao approvalDao;

	private final ObjectSerializerManager parser;
	private final WriteLock exclusiveLock;

	@Autowired
	public ApprovalManagerImpl(ApprovalDao genericDao) {
		super(genericDao);
		this.approvalDao = genericDao;
		this.parser = new ObjectSerializerManagerXStream();
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
		exclusiveLock = lock.writeLock();

	}

	@Override
	public List<Approval> findPriorityApproval() {
		return approvalDao.findPriorityApproval();
	}

	@Override
	public List<Approval> findPriorityApproval(String criteria, String keys,
			int first, int max) {
		return approvalDao.findPriorityApproval(criteria, keys, first, max);
	}

	@Override
	public List<Approval> findPricingApproval() {
		return approvalDao.findPricingApproval();
	}

	@Override
	public List<Approval> findPricingApproval(String keys, int first, int max) {
		return approvalDao.findPricingApproval(keys, first, max);
	}

	@Override
	public int findCountPriorityApproval() {
		return approvalDao.findCountPriorityApproval();
	}

	@Override
	public int findCountPricingApproval() {
		return approvalDao.findCountPricingApproval();
	}

	@Override
	public int findCountPriorityApproval(String criteria, String keys) {
		return approvalDao.findCountPriorityApproval(criteria, keys);
	}

	@Override
	public int findCountPricingApproval(String keys) {
		return approvalDao.findCountPricingApproval(keys);
	}

	@Override
	public List<Approval> findPriorityApproval(int first, int max) {
		return approvalDao.findPriorityApproval(first, max);
	}

	@Override
	public List<Approval> findPricingApproval(int first, int max) {
		return approvalDao.findPricingApproval(first, max);
	}

	/**
	 * save ke table t_approval
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized boolean saveOrUpdatePriority(
			final PriorityRouting priority, final User user, int processFlag) {
		boolean stat = false;
		try {

			Approval appr = constructPriorityEntity(priority, user, processFlag);
			if (appr != null) {
				exclusiveLock.lock();
				approvalDao.saveOrUpdate(appr);
				stat = true;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		} finally {
			exclusiveLock.unlock();
		}
		return stat;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized boolean deleteAllPriorities(
			Set<PriorityRouting> priority, User user, int processFlag) {
		boolean stat = false;
		try {
			if (priority != null && priority.size() > 0 && user != null) {
				List<Approval> list = new ArrayList<Approval>();
				for (PriorityRouting pre : priority) {
					Approval appr = constructPriorityEntity(pre, user,
							processFlag);
					list.add(appr);
				}

				if (list != null && list.size() > 0) {
					exclusiveLock.lock();
					for (Approval pri : list) {
						approvalDao.saveOrUpdate(pri);
					}
					stat = true;
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		} finally {
			exclusiveLock.unlock();
		}
		return stat;
	}

	/**
	 * 
	 * @param priority
	 * @param user
	 * @return
	 */
	private Approval constructPriorityEntity(final PriorityRouting priority,
			User user, int processFlag) {
		try {
			if (priority != null && user != null) {
				Approval appr = cekForDuplicateData(priority);
				if (appr == null) {
					appr = new Approval();
					appr.setId(null);
				}

				Timestamp time = getSystemTime();
				LOGGER.info(time + " << timestamp");

				appr.setFlag(PRIORITY_FLAG);
				appr.setCreatedBy(user.getId());
				appr.setCreatedDate(time);
				appr.setData(parser.parseToString(priority));
				appr.setRefId(String.valueOf(priority.getId()));

				appr.setStatus(WAITING);
				appr.setProcessFlag(processFlag);
				return appr;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}

	/**
	 * 
	 * @param priority
	 * @return
	 */
	private Approval cekForDuplicateData(PriorityRouting priority) {
		try {
			if (priority != null) {
				String data = parser.parseToString(priority);
				Approval appr = approvalDao.findPriorityForDuplicate(data);
				return appr;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private Timestamp getSystemTime() {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss a");

			Timestamp timestamp = new java.sql.Timestamp(
					System.currentTimeMillis());
			String dateTs = dateFormat.format(timestamp);
			Date date = dateFormat.parse(dateTs);
			Timestamp ts = new Timestamp(date.getTime());
			return ts;
		} catch (ParseException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}

	@Override
	public PriorityRouting getDataPriority(final String data) {
		try {
			Object obj = parser.parseToObject(data);
			if (obj instanceof PriorityRouting) {
				return (PriorityRouting) obj;
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}

	/**
	 *
	 */
	@Override
	public List<Approval> getDataAppPriority(
			final Set<PriorityRouting> selectedData) {
		try {
			final List<Approval> list = new ArrayList<Approval>();
			for (PriorityRouting priority : selectedData) {
				Approval app = approvalDao.findPriorityByRefId(String
						.valueOf(priority.getId()));
				list.add(app);
			}
			return list;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}

	/**
	 * ubah status approval jadi approve/reject
	 */
	@Override
	public synchronized boolean updateApprs(List<Approval> apprs, User user,
			int appStatus) {
		boolean stat = false;
		try {
			for (Approval appr : apprs) {
				appr.setProcessBy(user.getId());
				appr.setProcessDate(getSystemTime());
				appr.setStatus(appStatus);
			}
			if (approvalDao.updateEntityWithoutCommit(apprs)) {
				stat = true;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	/**
	 * ubah status approval jadi approve/reject
	 */
	@Override
	public synchronized boolean updateApprs(Approval appr, User user,
			int appStatus) {
		boolean stat = false;
		try {
			appr.setProcessBy(user.getId());
			appr.setProcessDate(getSystemTime());
			appr.setStatus(appStatus);
			if (approvalDao.updateEntityWithoutCommit(appr)) {
				stat = true;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return stat;
	}

	@Override
	public synchronized boolean commitEntity() {
		return approvalDao.commitEntity();
	}

	@Override
	public synchronized boolean rollbackEntity() {
		return approvalDao.rollbackEntity();
	}

	@Override
	public PriorityRouting getDataPriority(Long refId) {
		Approval appr = approvalDao.findPriorityByRefId(String.valueOf(refId));
		if (appr != null) {
			return getDataPriority(appr.getData());
		}
		return null;
	}

	@Override
	public Approval getDataByRefId(Long refId) {
		return approvalDao.findPriorityByRefId(String.valueOf(refId));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized boolean saveOrUpdatePricing(
			List<TransactionFee> trxFees, User user, int processFlag) {
		boolean stat = false;
		try {

			List<Approval> apprs = constructPricingEntity(trxFees, user,
					processFlag);
			if (apprs != null && apprs.size() > 0) {
				exclusiveLock.lock();
				approvalDao.updateEntity(apprs);
				stat = true;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		} finally {
			exclusiveLock.unlock();
		}
		return stat;
	}

	/**
	 * 
	 * @param trxFee
	 * @param user
	 * @return
	 */
	private List<Approval> constructPricingEntity(
			final List<TransactionFee> trxFees, User user, int processFlag) {
		try {
			if (trxFees != null && user != null) {
				Timestamp time = getSystemTime();
				List<Approval> apprs = new ArrayList<Approval>();

				for (TransactionFee trxFee : trxFees) {
					Approval appr = approvalDao.findPricingForDuplicate(trxFee
							.getId().getIdentifier());
					if (appr == null) {
						appr = new Approval();
						appr.setId(null);
					}

					appr.setFlag(PRICING_FLAG);
					appr.setCreatedBy(user.getId());
					appr.setCreatedDate(time);
					appr.setData(parser.parseToString(trxFee));
					appr.setRefId(trxFee.getId().getIdentifier());

					appr.setStatus(WAITING);
					appr.setProcessFlag(processFlag);

					apprs.add(appr);
				}
				return apprs;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}

	@Override
	public TransactionFee getDataPricingByData(String data) {
		try {
			Object obj = parser.parseToObject(data);
			if (obj instanceof TransactionFee) {
				return (TransactionFee) obj;
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}

	@Override
	/**
	 * format key >> transactionId|channelId|identifier
	 */
	public List<TransactionFee> getListPricingByKeys(String keys) {
		try {
			List<Approval> apprs = approvalDao.findPricingApproval(keys);
			if (apprs != null) {
				List<TransactionFee> fees = new ArrayList<TransactionFee>();
				for (Approval appr : apprs) {
					Object obj = parser.parseToObject(appr.getData());
					if (obj instanceof TransactionFee) {
						TransactionFee fee = (TransactionFee) obj;
						fees.add(fee);
					}
				}
				return fees;
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<Approval> getPricingDataByRefId(String keys) {
		return approvalDao.findPricingApproval(keys);
	}

	@Override
	public List<Approval> findPriorityUserApproval(Long userId) {
		return approvalDao.findPriorityUserApproval(userId);
	}

	@Override
	public List<Approval> findPriorityUserApproval(Long userId,
			String criteria, String keys, int first, int max) {
		return approvalDao.findPriorityUserApproval(userId, criteria, keys,
				first, max);
	}

	@Override
	public List<Approval> findPricingUserApproval(Long userId) {
		return approvalDao.findPricingUserApproval(userId);
	}

	@Override
	public List<Approval> findPricingUserApproval(Long userId, String keys,
			int first, int max) {
		return approvalDao.findPricingUserApproval(userId, keys, first, max);
	}

	@Override
	public int findCountPriorityUserApproval(Long userId) {
		return approvalDao.findCountPriorityUserApproval(userId);
	}

	@Override
	public int findCountPricingUserApproval(Long userId) {
		return approvalDao.findCountPricingUserApproval(userId);
	}

	@Override
	public int findCountPriorityUserApproval(Long userId, String criteria,
			String keys) {
		return approvalDao
				.findCountPriorityUserApproval(userId, criteria, keys);
	}

	@Override
	public int findCountPricingUserApproval(Long userId, String keys) {
		return approvalDao.findCountPricingUserApproval(userId, keys);
	}

	@Override
	public List<Approval> findPriorityUserApproval(Long userId, int first,
			int max) {
		return approvalDao.findPriorityUserApproval(userId, first, max);
	}

	@Override
	public List<Approval> findPricingUserApproval(Long userId, int first,
			int max) {
		return approvalDao.findPricingUserApproval(userId, first, max);
	}

}