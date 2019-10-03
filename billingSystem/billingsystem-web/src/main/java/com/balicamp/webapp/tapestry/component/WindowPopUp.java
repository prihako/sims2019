package com.balicamp.webapp.tapestry.component;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public abstract class WindowPopUp extends BaseComponent {
	@Parameter(required = false)
	public abstract String getUrl();

	@Parameter(required = false)
	public abstract boolean getShowAlternateLink();

	public String getJavaScriptUrl() {
		return "showReport('" + getUrl() + "')";
	}

	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		super.renderComponent(writer, cycle);
	}

}
