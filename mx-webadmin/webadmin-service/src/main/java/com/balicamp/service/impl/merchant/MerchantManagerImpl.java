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

import com.balicamp.dao.mx.MerchantGroupDao;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.merchant.MerchantManager;

@Service("merchantManager")
public class MerchantManagerImpl extends AbstractManager implements MerchantManager {

	private static final Logger LOG = Logger.getLogger(MerchantManagerImpl.class.getName());

	private static final long serialVersionUID = 1L;
	private MerchantGroupDao merchantGroupDao;
	private ReadLock sharedLock;

	private WriteLock exclusiveLock;

	@Autowired
	public void setMerchantGroupDao(MerchantGroupDao merchantGroupDao) {
		this.merchantGroupDao = merchantGroupDao;
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
		sharedLock = lock.readLock();
		exclusiveLock = lock.writeLock();
	}

	@Override
	public Object getDefaultDao() {
		return merchantGroupDao;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized boolean saveOrUpdateMerchant(MerchantGroup entity, boolean newData) {
		boolean stat = false;
		if (entity != null) {
			try {
				exclusiveLock.lock();
				merchantGroupDao.saveOrUpdate(entity);
				stat = true;
			} catch (Exception e) {
				LOG.log(Level.SEVERE, e.getMessage(), e);
			} finally {
				exclusiveLock.unlock();
			}
		}
		return stat;
	}

	@Override
	public MerchantGroup findMerchantGroup(final String code, final String description) {
		return merchantGroupDao.findMerchantGroup(code, description);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public synchronized boolean deleteMerchants(Set<MerchantGroup> merchants) {
		boolean stat = false;
		try {
			exclusiveLock.lock();
			merchantGroupDao.deleteMerchantCollection(merchants);
			stat = true;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.toString(), e);
		} finally {
			exclusiveLock.unlock();
		}
		return stat;
	}

	@Override
	public boolean detailIsExist(Set<MerchantGroup> merchants) {
		return merchantGroupDao.detailIsExist(merchants);
	}

	@Override
	public MerchantGroup findMerchantById(Long id) {
		return merchantGroupDao.findById(id);
	}

	@Override
	public List<MerchantGroup> findAllMerchantGroup() {
		return merchantGroupDao.findAll();
	}

	@Override
	public MerchantGroup findMerchantGroup(String code) {
		return merchantGroupDao.findMerchantGroup(code);
	}

}
