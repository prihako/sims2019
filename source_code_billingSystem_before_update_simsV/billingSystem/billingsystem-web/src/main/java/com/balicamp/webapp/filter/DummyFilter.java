package com.balicamp.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DummyFilter implements Filter {

	Log logger = LogFactory.getLog(DummyFilter.class);
	
	@Override
	public void destroy() {
		logger.info("destroy dummy filter...");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		logger.info("doFilter dummy filter...");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("init dummy filter...");
	}

}
