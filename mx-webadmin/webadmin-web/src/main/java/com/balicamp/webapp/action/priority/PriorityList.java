package com.balicamp.webapp.action.priority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.service.IManager;
import com.balicamp.service.TransactionsManager;
import com.balicamp.service.priority.PriorityManager;
import com.balicamp.webapp.action.webadmin.PricingSchemeEntry;
import com.balicamp.webapp.constant.PriorityConstant.FromPage;
import com.balicamp.webapp.constant.PriorityConstant.Parameter;
import com.balicamp.webapp.model.priority.ObjectModel;
import com.balicamp.webapp.tapestry.GenericTableModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * 
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 * @version
 */
public abstract class PriorityList extends PriorityCommonBean {

	protected final static Logger LOGGER = Logger.getLogger(PriorityList.class.getName());

	public abstract void setMsgnotif(String msgnotif);

	public abstract String getMsgnotif();

	public abstract void setKeys(String keys);

	@Persist("client")
	public abstract String getKeys();

	@InjectPage("priorityNotFound")
	public abstract PriorityNotFound getPriorityNotFound();

	@InjectPage("priorityDeleteConfirm")
	public abstract PriorityDeleteConfirm getPriorityDeleteConfirm();

	@InjectPage("priorityUpdate")
	public abstract PriorityUpdate getPriorityUpdate();

	@InjectPage("priorityConfirm")
	public abstract PriorityConfirm getPriorityConfirm();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();

	@InjectSpring("transactionsManagerImpl")
	public abstract TransactionsManager getTransactionsManager();

//	private BeanPropertySelectionModel criteriaModel = null;
	
	public abstract void setCriteriaModel(BeanPropertySelectionModel criteriaModel);
	
	@Persist("client")
	public abstract BeanPropertySelectionModel getCriteriaModel();

//	@Persist("client")
//	public BeanPropertySelectionModel getCriteriaModel() {
//		final List<ObjectModel> criteriaList = Parameter.getCriteria();
//		criteriaModel = new BeanPropertySelectionModel(criteriaList, "obj");
//		return criteriaModel;
//	}

	public abstract void setCriteria(ObjectModel criteria);

	@Persist("client")
	public abstract ObjectModel getCriteria();

	public abstract void setUniqueSet(Set<PriorityRouting> selectedIds);

	public abstract Set<PriorityRouting> getUniqueSet();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (getUniqueSet() == null)
			setUniqueSet(new HashSet<PriorityRouting>());
		
		if(getCriteriaModel() == null){
			setCriteriaModel(constructCriteriaModel());
		}
	}

	/**
	 * 
	 * @return
	 */
	private BeanPropertySelectionModel constructCriteriaModel() {
		final List<ObjectModel> criteriaList = Parameter.getCriteria();
		BeanPropertySelectionModel criteriaModel = new BeanPropertySelectionModel(criteriaList, "obj");
		return criteriaModel;
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
			PriorityRouting priorityRouting = (PriorityRouting) getLoopObject();
			if (priorityRouting != null) {
				Set<PriorityRouting> uniqueSet = getUniqueSet();
				if (priorityRouting.isSelected()) {
					uniqueSet.add(priorityRouting);
				} else
					uniqueSet.remove(priorityRouting);
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
			setMsgnotif(getText("priority.message.notif.notfound", new Object[] { getKeys() }));
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
		SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("priorityRouting");

		if (getKeys() != null && !getKeys().equals("") && getCriteria() != null) {
			searchCriteria.addCriterion(Restrictions.ilike(getCriteria().getKey(), "%" + getKeys() + "%"));
		}
		searchCriteria.addOrder(Order.asc("transactionCode").ignoreCase());
		return searchCriteria;
	}

	/**
	 * create table based on seacrh criteria
	 */
	@Override
	public IBasicTableModel createTableModel() {
		return new GenericTableModel<PriorityRouting>((IManager) getPriorityManager(), getTableSearchCriteria(),
				emptyOrderMaping);
	}

	/**
	 * delete ke halaman konfirmasi delete
	 * @return
	 */
	public IPage doDelete() {
		PriorityDeleteConfirm delconf = getPriorityDeleteConfirm();
		delconf.setSelectedData(getUniqueSet());
		return delconf;
	}

	/**
	 * ke page update
	 * @param id
	 * @return
	 */
	public IPage onViewDetail(Long id) {
		PriorityUpdate update = getPriorityUpdate();

		PriorityRouting priority = getPriorityManager().findById(id);

		update.setPriorityRouting(priority);

		//
		List<ObjectModel> trxList = getTransactionCodeList();
		update.setTransactionCodeModel(getTrxCodeModel(trxList));
		update.setTransactionCodeTr(getObjectModelFromList(trxList, priority.getTransactionCode()));
		update.setTransactionCode(priority.getTransactionCode());
		//
		List<ObjectModel> prjList = getProjectCodeList();
		update.setProjectCodeModel(getPrjCodeModel(prjList));
		update.setProjectCodeTr(getObjectModelFromList(prjList, priority.getProjectCode()));

		update.setProjectCode(priority.getProjectCode());
		update.setDescription(priority.getDescription());
		update.setProductCode(priority.getProductCode());

		update.setProductCodeExt(priority.getProductCodeExt());
		update.setRoutingCode(priority.getRoutingCode());
		//
		update.setGlCode(priority.getGlCode());

		return update;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public IPage onUpdateStatus(Long id) {
		PriorityConfirm update = getPriorityConfirm();
		update.setFromPage(FromPage.UPDATE);

		PriorityRouting priority = getPriorityManager().findById(id);
		String msg = "menon-aktifkan ";

		if (!priority.isActiveStatus()) {
			msg = "meng-aktifkan status ";
			priority.setActiveStatus(true);
		} else {
			priority.setActiveStatus(false);
		}
		update.setErrormsg(getText("priority.message.conf.aktif", new Object[] { msg }));

		update.setPriorityRouting(priority);

		update.setTransactionCode(priority.getTransactionCode());

		update.setProjectCode(priority.getProjectCode());
		update.setDescription(priority.getDescription());
		update.setProductCode(priority.getProductCode());

		update.setProductCodeExt(priority.getProductCodeExt());
		update.setRoutingCode(priority.getRoutingCode());
		//
		update.setGlCode(priority.getGlCode());

		return update;
	}

	@InjectPage("pricingSchemeEntry")
	public abstract PricingSchemeEntry getPricingSchemeEntry();

	public IPage doCopyToPricingScheme() {
		PricingSchemeEntry entry = getPricingSchemeEntry();

		List<PriorityRouting> list = new ArrayList<PriorityRouting>(getUniqueSet());

		entry.setPriorities(list);
		entry.setRows(list.size());
		entry.setCategory(getTableSearchCriteria());
		return entry;
	}
	
	public static void main (String args[]){
		String a = "Informasi Hubungi Call Center 789 atau Hub. PLN Terdekat";
		System.out.println(a.length());
//		System.out.println(a.substring(48,57));
	}
}
