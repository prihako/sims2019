package com.balicamp.webapp.action.merchant;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;

import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.model.mx.MerchantGroupDetails;
import com.balicamp.service.merchant.MerchantDetailManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.NotificationPage;
import com.balicamp.webapp.constant.MerchantConstant.FromPage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class DetailConfirm extends AdminBasePage implements PageBeginRenderListener {
	private static Logger LOGGER = Logger.getLogger(DetailConfirm.class.getSimpleName());

	public abstract void setMerchantGroup(MerchantGroup merchantGroup);

	@Persist("client")
	public abstract MerchantGroup getMerchantGroup();

	public abstract void setTermid(String termid);

	@Persist("client")
	public abstract String getTermid();

	public abstract void setChannelCode(String channelCode);

	@Persist("client")
	public abstract String getChannelCode();

	public abstract void setFromPage(String fromPage);

	@Persist("client")
	public abstract String getFromPage();

	public abstract void setDetail(MerchantGroupDetails detail);

	@Persist("client")
	public abstract MerchantGroupDetails getDetail();

	@InjectSpring("merchantDetailManager")
	public abstract MerchantDetailManager getMerchantDetailManager();

	@InjectPage("detailEntry")
	public abstract DetailEntry getDetailEntry();

	@InjectPage("notificationPage")
	public abstract NotificationPage getNotificationPage();

	@InjectPage("detailList")
	public abstract DetailList getDetailList();

	/**
	 * back to entry page
	 * @param cycle
	 * @return
	 */
	public IPage onBack(IRequestCycle cycle) {

		if (getFromPage() != null && getFromPage().equals(FromPage.UPDATE)) {
			return getDetailList(); // balik ke list
		}
		return getDetailEntry();
	}

	/**
	 * save to DB n go to notification page
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

			// save DB
			boolean stat = getMerchantDetailManager().saveOrUpdateDetail(getDetailEntity());

			// contruct status page
			NotificationPage notif = getNotificationPage();
			notif.setTitle(getText("merchant.detail.title.confirm"));
			if (stat) {
				notif.setMsgnotif(getText("merchant.detail.message.notif.add.success"));
			} else {
				notif.setMsgnotif(getText("merchant.detail.message.notif.add.failed"));
			}
			return notif;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private MerchantGroupDetails getDetailEntity() {
		MerchantGroupDetails detail = null;
		if (getFromPage() != null && getFromPage().equals(FromPage.UPDATE)) {
			detail = getDetail();
		} else {
			detail = new MerchantGroupDetails();
			detail.setId(null);
			detail.setMerchantGroupId(getMerchantGroup().getId());
		}

		detail.setTermid(getTermid());
		detail.setChannelCode(getChannelCode());
		return detail;
	}

}
