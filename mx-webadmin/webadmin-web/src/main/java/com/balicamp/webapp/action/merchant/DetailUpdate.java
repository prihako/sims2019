package com.balicamp.webapp.action.merchant;

import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.form.BeanPropertySelectionModel;

import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.MerchantGroupDetails;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.constant.MerchantConstant.FromPage;

public abstract class DetailUpdate extends AdminBasePage implements PageBeginRenderListener {
	private static Logger LOGGER = Logger.getLogger(DetailUpdate.class.getSimpleName());

	public abstract void setFrompage(String frompage);

	@Persist("client")
	public abstract String getFrompage();

	public abstract void setMerchantGroupModel(BeanPropertySelectionModel merchantGroupModel);

	@Persist("client")
	public abstract BeanPropertySelectionModel getMerchantGroupModel();

	public abstract void setMerchantGroup(MerchantGroup merchantGroup);

	@Persist("client")
	public abstract MerchantGroup getMerchantGroup();

	public abstract void setTermid(String termid);

	@Persist("client")
	public abstract String getTermid();

	// public abstract void setChannelCode(String channelCode);
	//
	// @Persist("client")
	// public abstract String getChannelCode();

	public abstract void setMerchantTr(String merchantTr);

	@Persist("client")
	public abstract String getMerchantTr();

	public abstract void setDetail(MerchantGroupDetails detail);

	@Persist("client")
	public abstract MerchantGroupDetails getDetail();

	@InjectPage("detailConfirm")
	public abstract DetailConfirm getDetailConfirm();

	public abstract void setErrormsg(String errormsg);

	public abstract String getErrormsg();

	// channel model
	public abstract void setChannelModel(BeanPropertySelectionModel channelModel);

	@Persist("client")
	public abstract BeanPropertySelectionModel getChannelModel();

	@Persist("client")
	public abstract Endpoints getChannel();

	public abstract void setChannel(Endpoints channel);

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
				nextPage.setFromPage(FromPage.UPDATE);
				nextPage.setMerchantGroup(getMerchantGroup());
				nextPage.setTermid(getTermid());
				nextPage.setChannelCode(getChannelCode());
				nextPage.setDetail(getDetail());
			}

			return nextPage;

		} catch (Exception e) {
			log.error(e.getCause(), e);
		}
		return null;
	}

	private String getChannelCode() {
		if (getChannel() != null) {
			return getChannel().getCode();
		}
		return null;
	}
}
