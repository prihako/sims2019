package com.balicamp.webapp.action.merchant;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;

import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.service.merchant.MerchantManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.NotificationPage;
import com.balicamp.webapp.constant.MerchantConstant;
import com.balicamp.webapp.constant.MerchantConstant.FromPage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class MerchantGroupConfirm extends AdminBasePage implements PageBeginRenderListener {
	protected final static Logger log = Logger.getLogger(MerchantGroupConfirm.class.getSimpleName());

	@Persist("client")
	public abstract MerchantGroup getMerchantGroup();

	public abstract void setMerchantGroup(MerchantGroup merchantGroup);

	public abstract void setFromPage(String fromPage);

	@Persist("client")
	public abstract String getFromPage();

	public abstract void setCode(String code);

	@Persist("client")
	public abstract String getCode();

	public abstract void setDescription(String description);

	@Persist("client")
	public abstract String getDescription();

	@InjectPage("merchantGroupAdd")
	public abstract MerchantGroupAdd getMerchantGroupAdd();

	@InjectPage("notificationPage")
	public abstract NotificationPage getNotificationPage();

	@InjectPage("merchantGroupList")
	public abstract MerchantGroupList getMerchantGroupList();

	@InjectSpring("merchantManager")
	public abstract MerchantManager getMerchantManager();

	/**
	 * back to entry page
	 * @param cycle
	 * @return
	 */
	public IPage onBack(IRequestCycle cycle) {

		if (getFromPage() != null && getFromPage().equals(FromPage.UPDATE)) {
			return getMerchantGroupList(); // balik ke list
		}
		return getMerchantGroupAdd();
	}

	/**
	 * save to DB n go to notification page
	 * @param cycle
	 * @return
	 */
	public IPage doSubmit(IRequestCycle cycle) {

		boolean newData = true;
		try {

			if (getDelegate().getHasErrors()) {
				return null;
			}
			// cekDoubleSubmit();
			if (getDelegate().getHasErrors()) {
				return null;
			}

			// save DB
			Map<String, Object> newdata = getEntityData();
			boolean stat = false;
			if (newdata != null) {
				stat = getMerchantManager().saveOrUpdateMerchant((MerchantGroup) newdata.get(MerchantConstant.OBJ),
						(Boolean) newdata.get(MerchantConstant.NEW_STATUS));

			}

			// contruct status page
			NotificationPage notif = getNotificationPage();
			notif.setTitle(getText("merchant.title.confirm"));
			if (stat) {
				notif.setMsgnotif(getText("merchant.message.notif.add.success"));

			} else {
				if (newdata == null) {
					notif.setMsgnotif(getText("merchant.message.notif.add.exist"));
				} else {
					notif.setMsgnotif(getText("merchant.message.notif.add.failed"));
				}
			}
			return notif;
		} catch (Exception e) {
			log.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}

	/**
	 * construct entity
	 * @return
	 */
	private Map<String, Object> getEntityData() {
		Map<String, Object> data = new HashMap<String, Object>();
		MerchantGroup entity = getMerchantManager().findMerchantGroup(getCode());

		if (getFromPage() != null && getFromPage().equals(FromPage.UPDATE)) {
			entity = getMerchantGroup();
			data.put(MerchantConstant.NEW_STATUS, false);
		} else {
			if (entity != null) {
				return null;

			} else {
				entity = new MerchantGroup();
				entity.setId(null);
				entity.setCode(getCode());
				data.put(MerchantConstant.NEW_STATUS, true);
			}
		}

		entity.setDescription(getDescription());
		data.put(MerchantConstant.OBJ, entity);
		return data;
	}
}
