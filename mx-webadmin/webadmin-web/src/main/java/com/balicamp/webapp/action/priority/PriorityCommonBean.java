package com.balicamp.webapp.action.priority;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.form.BeanPropertySelectionModel;

import com.balicamp.model.mx.Transactions;
import com.balicamp.service.TransactionsManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.model.priority.ObjectModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class PriorityCommonBean extends BasePageList implements PageBeginRenderListener, PageEndRenderListener {
	private static final Logger LOGGER = Logger.getLogger(PriorityCommonBean.class.getSimpleName());

	@InjectSpring("transactionsManagerImpl")
	public abstract TransactionsManager getTransactionsManager();

	public BeanPropertySelectionModel getTrxCodeModel(final List<ObjectModel> criteriaList) {
		return new BeanPropertySelectionModel(criteriaList, "obj");
	}

	public BeanPropertySelectionModel getPrjCodeModel(final List<ObjectModel> criteriaList) {
		return new BeanPropertySelectionModel(criteriaList, "obj");
	}

	/**
	 * 
	 * @return
	 */
	protected List<ObjectModel> getTransactionCodeList() {
		List<ObjectModel> trxList = new ArrayList<ObjectModel>();
		List<Transactions> list = getTransactionsManager().getTransactionsList();
		for (Transactions trx : list) {
			trxList.add(new ObjectModel(trx.getCode(), (trx.getCode() + " - " + trx.getName())));
		}
		return trxList;
	}

	/**
	 * 
	 * @return
	 */
	protected List<ObjectModel> getProjectCodeList() {
		List<ObjectModel> list = new ArrayList<ObjectModel>();
		list.add(new ObjectModel(getText("project.code.finnet"), getText("project.code.finnet")));
		list.add(new ObjectModel(getText("project.code.mitracomm"), getText("project.code.mitracomm")));
		list.add(new ObjectModel(getText("project.code.sera"), getText("project.code.sera")));
		return list;
	}

	/**
	 * 
	 * @param list
	 * @param key
	 * @return
	 */
	protected ObjectModel getObjectModelFromList(List<ObjectModel> list, String key) {
		if (list != null) {
			for (ObjectModel om : list) {
				if (om.getKey().equals(key)) {
					return om;
				}
			}
		}
		return null;
	}
}