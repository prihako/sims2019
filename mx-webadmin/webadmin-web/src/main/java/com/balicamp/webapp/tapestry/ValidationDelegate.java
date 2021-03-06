package com.balicamp.webapp.tapestry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IFieldTracking;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.RenderString;

/**
 * Custom Validation Delegate - based on one found in Tapestry in Action.
 *
 * @author Matt Raible
 */
public class ValidationDelegate extends org.apache.tapestry.valid.ValidationDelegate {
	private static final long serialVersionUID = 6658594142293597652L;
	protected final Log log = LogFactory.getLog(getClass());

	public void writeLabelPrefix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle) {
		//    	super.writeLabelPrefix(component, writer, cycle);
		// does nothing put prevent <font color="red"> from getting written
	}

	public void writeLabelSuffix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle) {
		//        if (component.isRequired()) {
		//            writer.begin("span");
		//            writer.attribute("class", "req");
		//            writer.printRaw(" *");
		//            writer.end();
		//        }
	}

	public void writeAttributes(IMarkupWriter writer, IRequestCycle cycle, IFormComponent component,
			IValidator validator) {
		if (isInError()) {
			String cssClass = ((component.getBinding("class") != null) ? component.getBinding("class").getObject()
					.toString() : "");
			writer.attribute("class", cssClass + " error");
		}
	}

	public void writePrefix(IMarkupWriter writer, IRequestCycle cycle, IFormComponent component, IValidator validator) {
		if (isInError(component)) {
			writer.begin("span");
			writer.attribute("class", "fieldError");

			writer.begin("img");
			String ctxPath = cycle.getInfrastructure().getContextPath();
			writer.attribute("src", ctxPath + "/images/iconWarning.gif");
			writer.attribute("class", "validationWarning");
			writer.attribute("alt", cycle.getPage().getMessages().getMessage("icon.warning"));
			writer.end("img");

			writer.printRaw("&nbsp;");

			IFieldTracking tracking = getComponentTracking();
			IRender render = tracking.getErrorRenderer();
			String error = "";

			if (render instanceof RenderString) {
				error = ((RenderString) render).getString();
			}

			writer.printRaw(error);
			writer.end("span");

			writer.begin("br");
			writer.end("br");
		}
	}

	public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle, IFormComponent component, IValidator validator) {
		// prevent <font> tags from getting written when there's an error
	}
}
