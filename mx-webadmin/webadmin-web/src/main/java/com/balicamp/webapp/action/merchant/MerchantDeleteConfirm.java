package com.balicamp.webapp.action.merchant;

import java.util.Set;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.service.merchant.MerchantManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.NotificationPage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class MerchantDeleteConfirm extends AdminBasePage {
	private static Logger LOOGER = Logger.getLogger(MerchantDeleteConfirm.class.getSimpleName());

	@InjectPage("merchantGroupList")
	public abstract MerchantGroupList getMerchantGroupList();

	@InjectPage("notificationPage")
	public abstract NotificationPage getNotificationPage();

	@InjectSpring("merchantManager")
	public abstract MerchantManager getMerchantManager();

	public abstract void setSelectedData(Set<MerchantGroup> selectedData);

	public abstract Set<MerchantGroup> getSelectedData();

	public abstract void setMerchant(MerchantGroup merchant);

	public abstract MerchantGroup getMerchant();

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
		notif.setTitle(getText("leftMenu.merchant.crudParameter.list"));

		if (detailIsExist()) { // ada detail nggak bisa delete
			notif.setMsgnotif(getText("merchant.message.notif.delete.detail.failed"));
		} else {
			boolean stat = getMerchantManager().deleteMerchants(getSelectedData());
			if (stat) {
				notif.setMsgnotif(getText("merchant.message.notif.delete.success"));
			} else {
				notif.setMsgnotif(getText("merchant.message.notif.delete.failed"));
			}
		}
		return notif;
	}

	/**
	 * 
	 * @return
	 */
	private boolean detailIsExist() {
		return getMerchantManager().detailIsExist(getSelectedData());
	}
}
