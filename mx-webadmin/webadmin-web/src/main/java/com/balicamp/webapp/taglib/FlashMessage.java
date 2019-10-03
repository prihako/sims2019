package com.balicamp.webapp.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class FlashMessage extends SimpleTagSupport {
	private String sessionKey;
	
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		String message = (String) getJspContext().findAttribute(sessionKey);
		
		if ( message != null ){
			out.write(message);						
		}
		getJspContext().setAttribute(sessionKey, null);
		
		out.flush();
	}
	
	
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
}
