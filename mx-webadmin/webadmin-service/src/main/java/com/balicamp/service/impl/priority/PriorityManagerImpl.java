package com.balicamp.service.impl.priority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.dao.mx.PriorityRoutingDao;
import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.priority.PriorityConstant.Parameter;
import com.balicamp.service.priority.PriorityManager;

/**
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 * @version
 */
@Service("priorityManager")
public class PriorityManagerImpl extends AbstractManager implements PriorityManager {

	private static final Logger LOG = Logger.getLogger(PriorityManagerImpl.class.getName());

	private static final long serialVersionUID = 1L;
	private PriorityRoutingDao priorityRoutingDao;
	private ReadLock sharedLock;

	private WriteLock exclusiveLock;

	@Autowired
	public void setPriorityRoutingDao(PriorityRoutingDao priorityRoutingDao) {
		this.priorityRoutingDao = priorityRoutingDao;
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
		sharedLock = lock.readLock();
		exclusiveLock = lock.writeLock();
	}

	/**
	 * jika ada product code yg sama, set activeStatus yg lain jadi false
	 * 
	 * pls discuss with Rudi or ABet for priority concept.
	 * 
	 * step:
	 * 1. apakah ada ada product code yg sama
	 * 2. jika tidak ada >> langsung save
	 * 3. Jika ada >> save n update
	 * newdata = status nya aktif
	 * exitst data = statusnya jadi nggak aktif
	 * 4. perhatikan kondisi khusus JIKA dia POSTPAID (pasangan kedua nya musti active)
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized boolean saveOrUpdatePriority(PriorityRouting priority, boolean newdata) {
		boolean stat = false;

		if (priority != null) {
			// transaction_code project_code description product_code
			// routing_code is_active

			List<PriorityRouting> list = priorityRoutingDao.findPriorityByProductCode(priority.getProductCode());

			if (list == null || list.isEmpty()) { //jika list empty
				try {
					list = new ArrayList<PriorityRouting>();
					list.add(priority);
					exclusiveLock.lock();
					priorityRoutingDao.mergePriorityCollection(list);
					stat = true;
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString(), e);
				} finally {
					exclusiveLock.unlock();
				}

			} else { // jika list nya nggak ada isinya
				try {
					if (newdata) {
						priorityRoutingDao.setIdentifier(priority);
					} else {
						if (list.size() > 1) {
							PriorityRouting existing = findById(priority.getId());
							list.remove(existing);
						}
					}
					
					// CEK POSTPAID
					if (isRoutingPostpaid(priority)) {
						for (PriorityRouting prt : list) {
							if (isPostpaid(prt, priority)) {
								// set active status
								prt.setActiveStatus(priority.isActiveStatus());
							} else {
								// set reverse active status
								prt.setActiveStatus(getReversalActiveStatus(priority.isActiveStatus()));
							}
						}
					} else {
						// set reverse active status
						for (PriorityRouting prt : list) {
							prt.setActiveStatus(getReversalActiveStatus(priority.isActiveStatus()));
						}
					}
					
					list.add(priority);
					//
					if (exclusiveLock.tryLock()) {
						priorityRoutingDao.mergePriorityCollection(list);
						stat = true;
					}
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString(), e);
				} finally {
					exclusiveLock.unlock();
				}
			}
		}
		return stat;
	}

	/**
	 *
	 * @param priority
	 * @return
	 */
	private boolean isRoutingPostpaid(PriorityRouting priority) {
		boolean stat = false;
		for (String routingCode : Parameter.getRoutingPospaid()) {
			if (priority.getRoutingCode().equals(routingCode)) {
				stat = true;
				break;
			}
		}
		return stat;
	}

	/**
	 *
	 * @param prt
	 * @param priority
	 * @return
	 */
	private boolean isPostpaid(PriorityRouting prt, PriorityRouting priority) {
		boolean stat = false;
		if (prt.getProjectCode().equals(priority.getProjectCode()) && prt.getProductCode().equals(prt.getProductCode())) {
			stat = true;
		}
		return stat;
	}

	@Override
	public Object getDefaultDao() {
		return priorityRoutingDao;
	}

	@Override
	public Long getPriorityId(String transactionCode, String projectCode, String description, String productCode,
			String routingCode) {
		final PriorityRouting pri = priorityRoutingDao.findPriorityRouting(transactionCode, projectCode, description,
				productCode, routingCode);
		if (pri != null) {
			return pri.getId();
		}
		return null;
	}

	@Override
	public List<PriorityRouting> findPriority(SearchCriteria searchCriteria, int first, int pageSize) {
		return priorityRoutingDao.findByCriteria(searchCriteria, first, pageSize);
	}

	@Override
	public PriorityRouting findPriorityRouting(String transactionCode, String projectCode, String description,
			String productCode, String routingCode) {
		return priorityRoutingDao.findPriorityRouting(transactionCode, projectCode, description, productCode,
				routingCode);
	}

	@Override
	public PriorityRouting findById(Long id) {
		return priorityRoutingDao.findById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized boolean deletePriorities(Set<PriorityRouting> priorities) {
		boolean stat = false;
		try {
			exclusiveLock.lock();
			priorityRoutingDao.deletePriorityCollection(priorities);
			stat = true;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.toString(), e);
		} finally {
			exclusiveLock.unlock();
		}
		return stat;
	}

	@Override
	public List<PriorityRouting> findPriorityForTrxFee(int nFirst, int nPageSize) {
		return priorityRoutingDao.findPriorityForTrxFee(nFirst, nPageSize);
	}

	@Override
	public int getPriorityCountForFee() {
		return priorityRoutingDao.getPriorityCountForFee();
	}

	@Override
	public PriorityRouting findByTransactionFeeCriteria(String transactionCode, String projectCode, String productCode) {
		return priorityRoutingDao.findByTransactionFeeCriteria(transactionCode, projectCode, productCode);
	}

	@Override
	public int getPriorityCountForFee(String desc) {
		return priorityRoutingDao.getPriorityCountForFee(desc);
	}

	@Override
	public List<PriorityRouting> getPriorityForFee(String desc, int first, int max) {
		return priorityRoutingDao.getPriorityForFee(desc, first, max);
	}

	/**
	 *
	 * @param stat
	 * @return
	 */
	private boolean getReversalActiveStatus(boolean stat) {
		if (stat) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public synchronized Long getNextSeq() {
		return priorityRoutingDao.getNextSeq();
	}

	@Override
	public List<PriorityRouting> findByProductCode(PriorityRouting priority) {
		return priorityRoutingDao.findPriorityByProductCode(priority.getProductCode(), priority.getProjectCode(), priority.getRoutingCode());
	}
}