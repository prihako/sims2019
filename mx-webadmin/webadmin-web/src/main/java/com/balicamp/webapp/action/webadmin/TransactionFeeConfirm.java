/**
 * 
 */
package com.balicamp.webapp.action.webadmin;

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.TransactionFee;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.TransactionFeeManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.constant.MerchantConstant.FromPage;
import com.balicamp.webapp.constant.WebConstant;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: TransactionFeeConfirm.java 505 2013-05-24 08:15:51Z rudi.sadria $
 */
public abstract class TransactionFeeConfirm extends AdminBasePage implements PageBeginRenderListener {

	public abstract void setFromPage(String fromPage);

	@Persist("client")
	public abstract String getFromPage();

	@InjectPage("pricingSchemeList")
	public abstract PricingSchemeList getPricingSchemeList();

	@InjectSpring("transactionFeeManagerImpl")
	public abstract TransactionFeeManager getTransactionFeeManager();

	@InjectPage("transactionFeeList")
	public abstract TransactionFeeList getTransactionFeeList();

	public abstract Set<TransactionFee> getTransactionFeeToConfirm();

	public abstract void setTransactionFeeToConfirm(Set<TransactionFee> transactionFeeToDelete);

	@Persist("client")
	public abstract String getIdentifier();

	public abstract void setIdentifier(String identifier);

	@Persist("client")
	public abstract Endpoints getChannelCode();

	public abstract void setChannelCode(Endpoints codeChannel);

	@Persist("client")
	public abstract String getStateMode();

	public abstract void setStateMode(String stateMode);

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (getTransactionFeeToConfirm() == null) {
			setTransactionFeeToConfirm(new HashSet<TransactionFee>());
		}
	}

	public IPage doSubmit(IRequestCycle cycle) {
		if (getTransactionFeeToConfirm() == null)
			return null;

		InfoPageCommand infoPageCommand = new InfoPageCommand();

		if (getStateMode().equals(WebConstant.EDITOR_STATE_DELETE)) { // delete
			for (TransactionFee fee : getTransactionFeeToConfirm()) {
				getTransactionFeeManager().deleteTransactionFeeByIds(fee.getId().getTransactionId(),
						fee.getId().getChannelId(), fee.getId().getIdentifier());
			}

			infoPageCommand.setTitle(getText("transactionFeeConfirm.deleteTitle"));
			infoPageCommand.addMessage(getText("transactionFeeConfirm.deleteInformation"));
			// infoPageCommand.addInfoPageButton(new
			// InfoPageCommand.InfoPageButton(getText("button.finish"),
			// "transactionFeeList.html"));
			infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"),
					"wellcome.html"));
		} else if (getStateMode().equals(WebConstant.EDITOR_STATE_EDIT)) { // edit
			for (TransactionFee fee : getTransactionFeeToConfirm()) {
				getTransactionFeeManager().updateTransactionFeeByIds(fee);
			}

			infoPageCommand.setTitle(getText("transactionFeeConfirm.editTitle"));
			infoPageCommand.addMessage(getText("transactionFeeConfirm.editInformation"));
			// infoPageCommand.addInfoPageButton(new
			// InfoPageCommand.InfoPageButton(getText("button.finish"),
			// "transactionFeeList.html"));
			infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"),
					"wellcome.html"));

		}

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
		return infoPage;
	}

	public IPage doCancel() {
		if (getFromPage() != null && getFromPage().equals(FromPage.UPDATE)) {
			PricingSchemeList transFeeList = getPricingSchemeList();
			// transFeeList.setChannelCodeItem(getChannelCode());
			// transFeeList.setIdentifier(getIdentifier());
			clearSession();
			return transFeeList;
		} else {
			TransactionFeeList transFeeList = getTransactionFeeList();
			transFeeList.setChannelCodeItem(getChannelCode());
			transFeeList.setIdentifier(getIdentifier());
			clearSession();
			return transFeeList;
		}

	}

	private void clearSession() {
		setChannelCode(null);
		setIdentifier(null);
		setStateMode(null);
	}
}
