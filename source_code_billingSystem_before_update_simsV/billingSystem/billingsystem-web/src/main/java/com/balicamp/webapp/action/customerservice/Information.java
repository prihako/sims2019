package com.balicamp.webapp.action.customerservice;

import com.balicamp.webapp.action.BasePage;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: Information.java 337 2013-02-19 09:16:01Z bagus.sugitayasa $
 */
public abstract class Information extends BasePage {
	public abstract String getInformationTitle();

	public abstract String getInformationBody();

	public abstract void setInformationTitle(String informationTitle);

	public abstract void setInformationBody(String informationBody);
}
