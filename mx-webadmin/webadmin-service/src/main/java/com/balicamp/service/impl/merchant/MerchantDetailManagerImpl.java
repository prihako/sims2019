package com.balicamp.service.impl.merchant;

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

import com.balicamp.dao.mx.MerchantDetailsDao;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.MerchantGroupDetails;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.merchant.MerchantDetailManager;

@Service("merchantDetailManager")
public class MerchantDetailManagerImpl extends AbstractManager implements MerchantDetailManager {

	private static final Logger LOG = Logger.getLogger(MerchantDetailManagerImpl.class.getName());

	private static final long serialVersionUID = 1L;
	private MerchantDetailsDao merchantDetailsDao;
	private ReadLock sharedLock;

	private WriteLock exclusiveLock;

	@Autowired
	public void setMerchantDetailsDao(MerchantDetailsDao merchantDetailsDao) {
		this.merchantDetailsDao = merchantDetailsDao;
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
		sharedLock = lock.readLock();
		exclusiveLock = lock.writeLock();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized boolean saveOrUpdateDetail(MerchantGroupDetails detail) {
		boolean stat = false;
		try {
			exclusiveLock.lock();
			merchantDetailsDao.saveOrUpdateDetail(detail);
			stat = true;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			exclusiveLock.unlock();
		}
		return stat;
	}

	@Override
	public MerchantGroupDetails findDetailsByAllField(MerchantGroup merchant, String termid, String channelCode) {
		MerchantGroupDetails detail = merchantDetailsDao.findDetailsByAllField(merchant.getId(), termid, channelCode);
		if (detail != null) {
			detail.setMerchantGroup(merchantDetailsDao.findMerchantGroup(detail.getMerchantGroupId()));
		}
		return detail;
	}

	@Override
	public Object getDefaultDao() {
		return merchantDetailsDao;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized boolean deleteDetails(Set<MerchantGroupDetails> details) {
		boolean stat = false;
		try {
			exclusiveLock.lock();
			merchantDetailsDao.deleteDetails(details);
			stat = true;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			exclusiveLock.unlock();
		}
		return stat;
	}

	@Override
	public MerchantGroupDetails findDetailById(Long id) {
		return merchantDetailsDao.findById(id);
	}

	@Override
	public MerchantGroup findMerchantGroupById(Long id) {
		return merchantDetailsDao.findMerchantGroup(id);
	}

	@Override
	public int findDetailCountByCriteria(String criteria, String keys) {
		return merchantDetailsDao.findDetailCountByCriteria(criteria, keys);
	}

	@Override
	public List<MerchantGroupDetails> findDetailsByCriteria(String criteria, String keys, int first, int max) {
		return merchantDetailsDao.findDetailsByCriteria(criteria, keys, first, max);
	}

}
