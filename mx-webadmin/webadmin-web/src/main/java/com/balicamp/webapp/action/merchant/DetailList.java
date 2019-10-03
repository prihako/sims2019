package com.balicamp.webapp.action.merchant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;

import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.MerchantGroupDetails;
import com.balicamp.service.EndpointsManager;
import com.balicamp.service.merchant.MerchantDetailManager;
import com.balicamp.service.merchant.MerchantManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.constant.MerchantConstant.Parameter;
import com.balicamp.webapp.model.priority.ObjectModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class DetailList extends BasePageList implements PageBeginRenderListener, PageEndRenderListener {
	public static Logger LOGGER = Logger.getLogger(DetailList.class.getSimpleName());

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManager getEndpointsManager();

	@InjectPage("detailDeleteConfirm")
	public abstract DetailDeleteConfirm getDetailDeleteConfirm();

	@InjectPage("detailUpdate")
	public abstract DetailUpdate getDetailUpdate();

	public abstract void setMsgnotif(String msgnotif);

	public abstract String getMsgnotif();

	public abstract void setKeys(String keys);

	@Persist("client")
	public abstract String getKeys();

	@InjectSpring("merchantDetailManager")
	public abstract MerchantDetailManager getMerchantDetailManager();

	@InjectSpring("merchantManager")
	public abstract MerchantManager getMerchantManager();

	private BeanPropertySelectionModel criteriaModel = null;

	public BeanPropertySelectionModel getCriteriaModel() {
		final List<ObjectModel> criteriaList = Parameter.getDetailCriteria();
		criteriaModel = new BeanPropertySelectionModel(criteriaList, "obj");
		return criteriaModel;
	}

	public abstract void setCriteria(ObjectModel criteria);

	public abstract ObjectModel getCriteria();

	public abstract void setUniqueSet(Set<MerchantGroupDetails> selectedIds);

	public abstract Set<MerchantGroupDetails> getUniqueSet();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (getUniqueSet() == null)
			setUniqueSet(new HashSet<MerchantGroupDetails>());
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
			MerchantGroupDetails merchant = (MerchantGroupDetails) getLoopObject();
			if (merchant != null) {
				Set<MerchantGroupDetails> uniqueSet = getUniqueSet();
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
			setMsgnotif(getText("merchant.detail.message.notif.notfound", new Object[] { getKeys() }));
		} else {
			setMsgnotif(null);
		}
		return null;
	}

	@Override
	public IBasicTableModel createTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				try {
					if (getCriteria() != null && getKeys() != null && !getKeys().equals("")) {

						return getMerchantDetailManager().findDetailCountByCriteria(getCriteria().getKey(), getKeys());
					}
					return 0;

				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}

			@Override
			public Iterator<MerchantGroupDetails> getCurrentPageRows(int nFirst, int nPageSize,
					ITableColumn objSortColumn, boolean bSortOrder) {
				try {
					if (getCriteria() != null && getKeys() != null && !getKeys().equals("")) {
						List<MerchantGroupDetails> results = getMerchantDetailManager().findDetailsByCriteria(
								getCriteria().getKey(), getKeys(), nFirst, nPageSize);

						Iterator<MerchantGroupDetails> rows = results.iterator();
						return rows;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new ArrayList<MerchantGroupDetails>().iterator();
			}
		};
	}

	/**
	 * delete ke halaman konfirmasi delete
	 * @return
	 */
	public IPage doDelete() {
		DetailDeleteConfirm delconf = getDetailDeleteConfirm();
		delconf.setSelectedData(pack(getUniqueSet()));
		return delconf;
	}

	/**
	 * 
	 * @param uniqueSet
	 * @return
	 */
	private Set<MerchantGroupDetails> pack(Set<MerchantGroupDetails> uniqueSet) {
		for (MerchantGroupDetails detail : uniqueSet) {
			detail.setMerchantGroup(getMerchantManager().findMerchantById(detail.getMerchantGroupId()));
		}
		return uniqueSet;
	}

	/**
	 * ke page update
	 * @param id
	 * @return
	 */
	public IPage onViewDetail(Long id) {
		DetailUpdate update = getDetailUpdate();

		MerchantGroupDetails detail = getMerchantDetailManager().findDetailById(id);
		MerchantGroup merchant = getMerchantDetailManager().findMerchantGroupById(detail.getMerchantGroupId());
		//
		update.setMerchantGroup(merchant);
		update.setTermid(detail.getTermid());
		//
		update.setChannelModel(getChannelModel());
		update.setChannel(getChannel(detail.getChannelCode()));
		//
		update.setMerchantGroupModel(getMerchantGroup());
		update.setMerchantTr(merchant.getDescTr());

		update.setDetail(detail);
		return update;
	}

	private Endpoints getChannel(String channelCode) {
		return getEndpointsManager().findEndpointsByCode(channelCode);
	}

	/**
	 * 
	 * @return
	 */
	private BeanPropertySelectionModel getMerchantGroup() {
		final List<MerchantGroup> criteriaList = getMerchantManager().findAllMerchantGroup();
		return new BeanPropertySelectionModel(criteriaList, "descTr");
	}

	public BeanPropertySelectionModel getChannelModel() {
		final List<Endpoints> criteriaList = getEndpointsManager().getEditabelChannelCodeList();
		return new BeanPropertySelectionModel(criteriaList, "concatCodeName");
	}
}
