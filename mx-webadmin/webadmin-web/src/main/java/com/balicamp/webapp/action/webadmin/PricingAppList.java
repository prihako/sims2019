package com.balicamp.webapp.action.webadmin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.hibernate.criterion.Order;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.model.user.Approval;
import com.balicamp.service.EndpointsManager;
import com.balicamp.service.merchant.MerchantManager;
import com.balicamp.service.user.ApprovalManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.constant.PriorityConstant.ApprovalStatus;
import com.balicamp.webapp.constant.PriorityConstant.Parameter;
import com.balicamp.webapp.model.priority.ObjectModel;
import com.balicamp.webapp.tapestry.GenericPropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * 
 * @author snurma.wijayanti@gmail.com
 * 
 */
public abstract class PricingAppList extends BasePageList implements
		PageBeginRenderListener {
	private static final Logger LOGGER = Logger.getLogger(PricingAppList.class
			.getSimpleName());

	public abstract void setKeys(String keys);

	@Persist("client")
	public abstract String getKeys();

	private BeanPropertySelectionModel criteriaModel = null;

	public BeanPropertySelectionModel getCriteriaModel() {
		final List<ObjectModel> criteriaList = Parameter.getCriteria2();
		criteriaModel = new BeanPropertySelectionModel(criteriaList, "obj");
		return criteriaModel;
	}

	public abstract void setCriteria(ObjectModel criteria);

	public abstract ObjectModel getCriteria();

	public abstract String getErrormsg();

	public abstract void setErrormsg(String errormsg);

	@Persist("client")
	public abstract Endpoints getChannelCodeItem();

	public abstract void setChannelCodeItem(Endpoints channelItem);

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManager getEndpointsManager();

	@InjectSpring("merchantManager")
	public abstract MerchantManager getMerchantManager();

	@InjectSpring("approvalManager")
	public abstract ApprovalManager getApprovalManager();

	@InjectPage("pricingAppConfirm")
	public abstract PricingAppConfirm getPricingAppConfirm();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPropertySelectionModel getChannelSelectionModel() {
		SearchCriteria scEnd = SearchCriteria.createSearchCriteria("endpoints");
		scEnd.addOrder(Order.asc("code"));
		List endpointList = getEndpointsManager().findByCriteria(scEnd, -1, -1);
		return new GenericPropertySelectionModel(endpointList, null, null, true);
	}

	private BeanPropertySelectionModel merchantGroupModel = null;

	public BeanPropertySelectionModel getMerchantGroupModel() {
		final List<MerchantGroup> criteriaList = getMerchantManager()
				.findAllMerchantGroup();
		merchantGroupModel = new BeanPropertySelectionModel(criteriaList,
				"descTr");
		return merchantGroupModel;
	}

	public abstract void setMerchantGroup(MerchantGroup merchantGroup);

	@Persist("client")
	public abstract MerchantGroup getMerchantGroup();

	@Override
	public IBasicTableModel createTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				try {
					if (getChannelCodeItem() != null) {
						return getApprovalManager().findCountPricingApproval(
								getIdentifier());
					} else {
						return getApprovalManager().findCountPricingApproval();
					}
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}

			@Override
			public Iterator<TransactionFee> getCurrentPageRows(int nFirst,
					int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {
				try {
					List<TransactionFee> results = new ArrayList<TransactionFee>();
					int i = 1;
					List<Approval> apprs = null;
					if (getChannelCodeItem() != null) {
						apprs = getApprovalManager().findPricingApproval(
								getIdentifier(), nFirst, nPageSize);
					} else {
						apprs = getApprovalManager().findPricingApproval(
								nFirst, nPageSize);
					}

					if (apprs != null) {
						for (Approval appr : apprs) {
							TransactionFee trxFee = getApprovalManager()
									.getDataPricingByData(appr.getData());
							if (trxFee != null) {
								trxFee.setAppStatusTr(ApprovalStatus
										.getAppStatus(appr.getStatus()));
								trxFee.setNo(i);
								results.add(trxFee);
								i++;
							}
						}
					}
					Iterator<TransactionFee> rows = results.iterator();
					return rows;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new ArrayList<TransactionFee>().iterator();
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	private String getIdentifier() {
		StringBuilder result = new StringBuilder();
		result.append("channelId%").append(getChannelCodeItem().getId())
				.append("%channelId%");

		if (getMerchantGroup() != null) {
			result.append("identifier%").append(getMerchantGroup().getCode());
		}
		// nama biller/product code
		if (getCriteria() != null && getKeys() != null && !getKeys().equals("")) {
			result.append("%").append(getKeys());
		}

		return result.toString();
	}

	private String getIdentifier(String[] keys) {
		StringBuilder result = new StringBuilder();
		result.append("%transactionId%").append(keys[0])
				.append("%transactionId%");
		result.append("channelId%").append(keys[1]).append("%channelId%");
		result.append("identifier%").append(keys[2]).append("%identifier%");
		return result.toString();
	}

	/**
	 * Listener onSearch Button
	 * 
	 * @return
	 */
	public IPage onSearch() {

		if (getChannelCodeItem() == null) {
			setErrormsg(getText("trxfee.message.error.add.channel.notnull"));
			return null;
		} else {
			setErrormsg(null);
		}

		if (getMerchantGroup() == null) {
			setErrormsg(getText("trxfee.message.error.add.merchant.notnull"));
			return null;
		} else {
			setErrormsg(null);
		}

		setRealRender(true);
		if (getTableModel().getRowCount() <= 0) {
			setErrormsg(getText("trxfee.message.error.search.notfound",
					new Object[] { getMerchantGroup().getCode(),
							getChannelCodeItem().getCode(), getKeys() }));
		}
		return null;
	}

	/**
	 * format key >> transactionId#channelId#identifier
	 * 
	 * @param key
	 * @return
	 */
	public IPage onViewDetail(String key) { // set List<TransactionFee>
		// set List<TransactionFee>
		if (key != null && !key.equals("")) {
			PricingAppConfirm conf = getPricingAppConfirm();
			String keyData[] = key.split("-");

			conf.setTransactionFee(getApprovalManager().getListPricingByKeys(
					getIdentifier(keyData)));
			conf.setDataApproval(getDataAppr(keyData));
			return conf;
		}
		return null;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	private List<Approval> getDataAppr(String[] keys) {
		return getApprovalManager().getPricingDataByRefId(getIdentifier(keys));
	}
}