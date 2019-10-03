package com.balicamp.webapp.action.merchant;

import java.util.List;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.form.BeanPropertySelectionModel;

import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.service.EndpointsManager;
import com.balicamp.service.merchant.MerchantManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.constant.MerchantConstant.FromPage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class DetailEntry extends AdminBasePage implements PageBeginRenderListener {
	private static Logger LOGGER = Logger.getLogger(DetailEntry.class.getSimpleName());

	@InjectSpring("merchantManager")
	public abstract MerchantManager getMerchantManager();

	private BeanPropertySelectionModel merchantGroupModel = null;

	public BeanPropertySelectionModel getMerchantGroupModel() {
		final List<MerchantGroup> criteriaList = getMerchantList();
		merchantGroupModel = new BeanPropertySelectionModel(criteriaList, "descTr");
		return merchantGroupModel;
	}

	public abstract void setMerchantGroup(MerchantGroup merchantGroup);

	@Persist("client")
	public abstract MerchantGroup getMerchantGroup();

	public abstract void setTermid(String termid);

	@Persist("client")
	public abstract String getTermid();

	// public abstract void setChannelCode(String channelCode);

	// public abstract String getChannelCode();

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManager getEndpointsManager();

	private BeanPropertySelectionModel channelModel = null;

	public BeanPropertySelectionModel getChannelModel() {
		final List<Endpoints> criteriaList = getChannelList();
		channelModel = new BeanPropertySelectionModel(criteriaList, "concatCodeName");
		return channelModel;
	}

	@Persist("client")
	public abstract Endpoints getChannel();

	public abstract void setChannel(Endpoints channel);

	public abstract void setErrormsg(String errormsg);

	public abstract String getErrormsg();

	@InjectPage("detailConfirm")
	public abstract DetailConfirm getDetailConfirm();

	/**
	 * 
	 * @return
	 */
	private List<MerchantGroup> getMerchantList() {
		return getMerchantManager().findAllMerchantGroup();
	}

	private List<Endpoints> getChannelList() {
		return getEndpointsManager().getEditabelChannelCodeList();
	}

	/**
	 * go to confirm page
	 * @param cycle
	 * @return
	 */
	public IPage doSubmit(IRequestCycle cycle) {
		try {
			if (getDelegate().getHasErrors()) {
				return null;
			}

			// cekDoubleSubmit();
			if (getDelegate().getHasErrors()) {
				return null;
			}
			// serverValidate();
			if (getDelegate().getHasErrors()) {
				return null;
			}

			// cek code merchant && termid && channel code
			if (getMerchantGroup() == null
					|| (getTermid() == null || (getTermid() != null && getTermid().trim().equals("")))
					&& (getChannelCode() == null || (getChannelCode() != null && getChannelCode().trim().equals("")))) {
				setErrormsg(getText("merchant.detail.message.error.add.notnull"));
				return null;
			}

			DetailConfirm nextPage = getDetailConfirm();
			if (nextPage != null) {
				nextPage.setFromPage(FromPage.ADD);
				nextPage.setMerchantGroup(getMerchantGroup());
				nextPage.setTermid(getTermid());
				nextPage.setChannelCode(getChannelCode());
				return nextPage;
			}
		} catch (Exception e) {
			log.error(e.getCause(), e);
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private String getChannelCode() {
		if (getChannel() != null) {
			return getChannel().getCode();
		}
		return null;
	}
}
