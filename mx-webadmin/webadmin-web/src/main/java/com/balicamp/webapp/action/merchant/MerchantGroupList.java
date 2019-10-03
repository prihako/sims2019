package com.balicamp.webapp.action.merchant;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.service.IManager;
import com.balicamp.service.merchant.MerchantManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.constant.MerchantConstant.Parameter;
import com.balicamp.webapp.model.priority.ObjectModel;
import com.balicamp.webapp.tapestry.GenericTableModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class MerchantGroupList extends BasePageList implements PageBeginRenderListener, PageEndRenderListener {
	public static Logger LOGGER = Logger.getLogger(MerchantGroupList.class.getSimpleName());

	public abstract void setMsgnotif(String msgnotif);

	public abstract String getMsgnotif();

	public abstract void setKeys(String keys);

	@Persist("client")
	public abstract String getKeys();

	@InjectSpring("merchantManager")
	public abstract MerchantManager getMerchantManager();

	@InjectPage("merchantDeleteConfirm")
	public abstract MerchantDeleteConfirm getMerchantDeleteConfirm();

	@InjectPage("merchantGroupUpdate")
	public abstract MerchantGroupUpdate getMerchantGroupUpdate();

	private BeanPropertySelectionModel criteriaModel = null;

	public BeanPropertySelectionModel getCriteriaModel() {
		final List<ObjectModel> criteriaList = Parameter.getCriteria();
		criteriaModel = new BeanPropertySelectionModel(criteriaList, "obj");
		return criteriaModel;
	}

	public abstract void setCriteria(ObjectModel criteria);

	public abstract ObjectModel getCriteria();

	public abstract void setUniqueSet(Set<MerchantGroup> selectedIds);

	public abstract Set<MerchantGroup> getUniqueSet();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (getUniqueSet() == null)
			setUniqueSet(new HashSet<MerchantGroup>());
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	/**
	 * for object looping
	 * 
	 * @return
	 */
	// public abstract Object getLoopObject();

	public void checkSelect(IRequestCycle cycle) {
		if (cycle.isRewinding()) {
			MerchantGroup merchant = (MerchantGroup) getLoopObject();
			if (merchant != null) {
				Set<MerchantGroup> uniqueSet = getUniqueSet();
				if (merchant.isSelected()) {
					uniqueSet.add(merchant);
				} else
					uniqueSet.remove(merchant);
				setUniqueSet(uniqueSet);
			}
		}
	}

	/**
	*
	* @return
	*/
	public IPage onSearch() {
		setRealRender(true);
		if (getTableModel().getRowCount() <= 0) {
			setMsgnotif(getText("merchant.message.notif.notfound", new Object[] { getKeys() }));
		} else {
			setMsgnotif(null);
		}
		return null;
	}

	/**
	 * construct searchcriteria
	 * @return
	 */
	public SearchCriteria getTableSearchCriteria() {
		SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("merchantGroup");

		if (getKeys() != null && !getKeys().equals("") && getCriteria() != null) {
			searchCriteria.addCriterion(Restrictions.ilike(getCriteria().getKey(), "%" + getKeys() + "%"));
		}
		searchCriteria.addOrder(Order.asc("code").ignoreCase());
		return searchCriteria;
	}

	/**
	 * create table based on seacrh criteria
	 */
	@Override
	public IBasicTableModel createTableModel() {
		return new GenericTableModel<MerchantGroup>((IManager) getMerchantManager(), getTableSearchCriteria(),
				emptyOrderMaping);
	}

	/**
	 * delete ke halaman konfirmasi delete
	 * @return
	 */
	public IPage doDelete() {
		MerchantDeleteConfirm delconf = getMerchantDeleteConfirm();
		delconf.setSelectedData(getUniqueSet());
		return delconf;
	}

	/**
	 * ke page update
	 * @param id
	 * @return
	 */
	public IPage onViewDetail(Long id) {
		MerchantGroupUpdate update = getMerchantGroupUpdate();

		MerchantGroup merchant = getMerchantManager().findMerchantById(id);

		update.setMerchantGroup(merchant);
		update.setCode(merchant.getCode());
		update.setDescription(merchant.getDescription());

		return update;
	}
}
