package com.balicamp.webapp.action.common;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.model.page.WaitingPageCommand;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.BasePage;

public abstract class WaitingPage extends BasePage implements
		PageBeginRenderListener, PageEndRenderListener {
	private static final Log log = LogFactory.getLog(WaitingPage.class);

	/*
	 * @InjectObject("spring:mrm") public abstract MRM getMrm();
	 * 
	 * @InjectObject("spring:deliveryChannelRequestSender") public abstract
	 * DeliveryChannelRequestSender getSender();
	 */

	public abstract long getCheckInterval();

	public abstract void setCheckInterval(long checkInterval);

	@InjectObject("spring:systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	@Persist("session")
	public abstract WaitingPageCommand getCommand();

	public abstract void setCommand(WaitingPageCommand command);

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		setCheckInterval(getSystemParameterManager()
				.getLongValue(
						new SystemParameterId("other",
								"other.waitPage.refreshInterval"), 1));

		IRequestCycle cycle = pageEvent.getRequestCycle();

		/*
		 * ILink link = checkResponse(cycle); if (link != null) {
		 * cycle.sendRedirect(link.getAbsoluteURL()); }
		 */

		super.pageBeginRender(pageEvent);
	}

	/*
	 * public ILink send(WaitingPageCommand command, Bean request, IRequestCycle
	 * cycle) { if (command == null) { return getErrorLink(cycle); }
	 * 
	 * setCommand(command);
	 * 
	 * try { getSender().send(request, true); } catch (Throwable t) { return
	 * getErrorLink(cycle); }
	 * 
	 * command.setDeliveryChannelId(request.getDeliveryChannelId()); return
	 * getEngineService().getLink(false, getPageName()); }
	 */

	/*
	 * protected ILink checkResponse(IRequestCycle cycle) { WaitingPageCommand
	 * command = getCommand(); if (command == null) { return
	 * getErrorLink(cycle); }
	 * 
	 * String dcId = command.getDeliveryChannelId(); MRM mrm = getMrm();
	 * 
	 * if (mrm.isTimeOut(dcId)) { return getTimeoutLink(cycle); }
	 * 
	 * Bean response = mrm.getResponse(dcId); if (response == null) { return
	 * null; }
	 * 
	 * mrm.unregister(dcId); command.setResponse(response);
	 * 
	 * String responseCode = response.getResponseCode();
	 * 
	 * if (MessageConstant.ResponseCode.SUCCESS.equals(responseCode)) { return
	 * getNextLink(cycle); }
	 * 
	 * return getErrorLink(cycle); }
	 */

	protected ILink getNextLink(IRequestCycle cycle) {
		WaitingPageCommand command = getCommand();

		String nextPage = command.getNextPage();
		String nextPageInitMethod = command.getNextPageInitMethod();

		if (CommonUtil.isNotEmpty(nextPageInitMethod)) {
			try {
				MethodUtils.invokeMethod(cycle.getPage(nextPage),
						nextPageInitMethod, command.getResponse());
			} catch (Exception e) {
				if (e instanceof InvocationTargetException) {
					InvocationTargetException e2 = (InvocationTargetException) e;
					Throwable e3 = e2.getTargetException();

					if (e3 instanceof RedirectException) {
						throw (RedirectException) e3;
					}
				}

				log.error(
						"an error occured while invoking next page init method: "
								+ nextPageInitMethod + ".", e);
			}
		}

		// additional parameter --added by Rudi Sadria
		Object paramList = command.getParamList();
		if (paramList != null) {
			try {
				MethodUtils.invokeMethod(cycle.getPage(nextPage),
						"setParamList", paramList);
			} catch (Exception e) {
				if (e instanceof InvocationTargetException) {
					InvocationTargetException e2 = (InvocationTargetException) e;
					Throwable e3 = e2.getTargetException();

					if (e3 instanceof RedirectException) {
						throw (RedirectException) e3;
					}
				}

				log.error("an error occured while passing parameters", e);
			}
		}

		return getEngineService().getLink(false, nextPage);
	}

	protected ILink getErrorLink(IRequestCycle cycle) {
		WaitingPageCommand command = getCommand();

		String errorPage = command == null ? null : command.getErrorPage();
		String errorPageInitMethod = command == null ? null : command
				.getErrorPageInitMethod();

		if (CommonUtil.isNotEmpty(errorPage)) {
			if (CommonUtil.isNotEmpty(errorPageInitMethod)) {
				try {
					MethodUtils.invokeMethod(cycle.getPage(errorPage),
							errorPageInitMethod, command);
				} catch (Exception e) {
					log.error(
							"an error occured while invoking error page init method: "
									+ errorPageInitMethod + ".", e);
				}
			}

			return getEngineService().getLink(false, errorPage);
		}

		InfoPageCommand infoPageCommand = new InfoPageCommand();
		infoPageCommand.setTitle(getText("waitSendJms.failResponseCode.title"));

		/*
		 * Bean response = command == null ? null : command.getResponse(); if
		 * (response == null) { infoPageCommand
		 * .addMessage(getText("waitSendJms.failResponseCode.message.0")); }
		 * else { infoPageCommand.addMessage(getText(
		 * "waitSendJms.failResponseCode.message", getText("response.code." +
		 * command.getResponse().getResponseCode()))); }
		 */

		infoPageCommand.setPrevPage("wellcome.html");
		// if ((command != null) &&
		// CommonUtil.isNotEmpty(command.getPreviousPage())) {
		// infoPageCommand.addInfoPageButton(new
		// InfoPageButton(getText("button.back"),
		// infoPageCommand.getPrevPage()));
		// }
		// infoPageCommand.addInfoPageButton(new InfoPageButton(
		// getText("button.finish"), "wellcome.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		return getEngineService().getLink(false, "infoPage");
	}

	protected ILink getTimeoutLink(IRequestCycle cycle) {
		WaitingPageCommand command = getCommand();

		String timeoutPage = command.getTimeoutPage();
		String timeoutPageInitMethod = command.getTimeoutPageInitMethod();

		if (CommonUtil.isNotEmpty(timeoutPage)) {
			if (CommonUtil.isNotEmpty(timeoutPageInitMethod)) {
				try {
					MethodUtils.invokeMethod(cycle.getPage(timeoutPage),
							timeoutPageInitMethod, command);
				} catch (Exception e) {
					log.error(
							"an error occured while invoking timeout page init method: "
									+ timeoutPageInitMethod + ".", e);
				}
			}

			return getEngineService().getLink(false, timeoutPage);
		}

		InfoPageCommand infoPageCommand = new InfoPageCommand();
		infoPageCommand.setTitle(getText("waitSendJms.timeout.title"));
		infoPageCommand.addMessage(getText("waitSendJms.timeout.message"));
		infoPageCommand.setPrevPage(getPageName());
		// infoPageCommand.addInfoPageButton(new InfoPageButton(
		// getText("button.back"), command.getPreviousPage() + ".html"));
		// infoPageCommand.addInfoPageButton(new InfoPageButton(
		// getText("button.ok"), "wellcome.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		return getEngineService().getLink(false, "infoPage");
	}
}
