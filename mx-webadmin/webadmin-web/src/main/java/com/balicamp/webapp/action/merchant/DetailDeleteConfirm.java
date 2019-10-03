package com.balicamp.webapp.action.merchant;

import java.util.Set;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.MerchantGroupDetails;
import com.balicamp.service.merchant.MerchantDetailManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.NotificationPage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class DetailDeleteConfirm extends AdminBasePage {
	private static Logger LOOGER = Logger.getLogger(DetailDeleteConfirm.class.getSimpleName());

	@InjectPage("detailList")
	public abstract DetailList getDetailList();

	@InjectPage("notificationPage")
	public abstract NotificationPage getNotificationPage();

	@InjectSpring("merchantDetailManager")
	public abstract MerchantDetailManager getMerchantDetailManager();

	public abstract void setSelectedData(Set<MerchantGroupDetails> selectedData);

	public abstract Set<MerchantGroupDetails> getSelectedData();

	public abstract void setDetail(MerchantGroupDetails detail);

	public abstract MerchantGroupDetails getDetail();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	/**
	 * DELETE
	 * @return
	 */
	public IPage doDelete() {
		if (getSelectedData() == null)
			return null;

		NotificationPage notif = getNotificationPage();
		notif.setTitle(getText("leftMenu.merchant.detail.list"));

		boolean stat = getMerchantDetailManager().deleteDetails(getSelectedData());
		if (stat) {
			notif.setMsgnotif(getText("merchant.detail.message.notif.delete.success"));
		} else {
			notif.setMsgnotif(getText("merchant.detail.message.notif.delete.failed"));
		}
		return notif;
	}
}
