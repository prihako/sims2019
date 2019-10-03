package com.balicamp.service.impl.mx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.mx.BankDao;
import com.balicamp.model.mx.Bank;
import com.balicamp.service.BankManager;
import com.balicamp.service.impl.GenericManagerImpl;

@Service("bankManager")
public class BankManagerImpl extends GenericManagerImpl<Bank, Long> implements BankManager {
	private BankDao bankDao;

	@Autowired
	public BankManagerImpl(BankDao genericDao) {
		super(genericDao);
		this.bankDao = genericDao;
	}

}
