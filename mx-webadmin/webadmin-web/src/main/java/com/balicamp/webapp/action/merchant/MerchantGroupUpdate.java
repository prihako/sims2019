package com.balicamp.webapp.action.merchant;

import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;

import com.balicamp.model.mx.MerchantGroup;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.constant.MerchantConstant.FromPage;

public abstract class MerchantGroupUpdate extends AdminBasePage implements PageBeginRenderListener {
	private static Logger LOGGER = Logger.getLogger(MerchantGroupUpdate.class.getSimpleName());

	public abstract void setDescription(String description);

	@Persist("client")
	public abstract MerchantGroup getMerchantGroup();

	public abstract void setMerchantGroup(MerchantGroup merchantGroup);

	@Persist("client")
	public abstract String getDescription();

	public abstract void setCode(String code);

	@Persist("client")
	public abstract String getCode();

	public abstract void setFrompage(String frompage);

	@Persist("client")
	public abstract String getFrompage();

	@InjectPage("merchantGroupConfirm")
	public abstract MerchantGroupConfirm getMerchantGroupConfirm();

	public abstract String getErrormsg();

	public abstract void setErrormsg(String errormsg);

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

			// cek code merchant && description
			if ((getCode() == null || (getCode() != null && getCode().trim().equals("")))
					&& (getDescription() == null || (getDescription() != null && getDescription().trim().equals("")))) {
				setErrormsg(getText("merchant.message.error.add.code.notnull"));
				return null;
			}

			MerchantGroupConfirm nextPage = getMerchantGroupConfirm();
			if (nextPage != null) {
				nextPage.setFromPage(FromPage.UPDATE);
				nextPage.setMerchantGroup(getMerchantGroup());
				nextPage.setCode(getCode());
				nextPage.setDescription(getDescription());
			}

			return nextPage;

		} catch (Exception e) {
			log.error(e.getCause(), e);
		}
		return null;
	}

}
