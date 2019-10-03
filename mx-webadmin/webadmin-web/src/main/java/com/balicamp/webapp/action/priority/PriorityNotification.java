package com.balicamp.webapp.action.priority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.service.priority.PriorityManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 * @version 
 */
public abstract class PriorityNotification extends AdminBasePage implements PageBeginRenderListener {

	protected final static Log log = LogFactory.getLog(PriorityNotification.class);

	public abstract void setTitle(String title);

	@Persist("client")
	public abstract String getMsgnotif();

	public abstract void setMsgnotif(String msgnotif);

	@Persist("client")
	public abstract String getTitle();

	public abstract void setTransactionCode(String transactionCode);

	@Persist("client")
	public abstract String getTransactionCode();

	public abstract void setProjectCode(String projectCode);

	@Persist("client")
	public abstract String getProjectCode();

	public abstract void setDescription(String description);

	@Persist("client")
	public abstract String getDescription();

	public abstract void setProductCode(String productCode);

	@Persist("client")
	public abstract String getProductCode();

	public abstract void setRoutingCode(String routingCode);

	@Persist("client")
	public abstract String getRoutingCode();

	@InjectPage("priorityEntry")
	public abstract PriorityEntry getPriorityEntry();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}
}
