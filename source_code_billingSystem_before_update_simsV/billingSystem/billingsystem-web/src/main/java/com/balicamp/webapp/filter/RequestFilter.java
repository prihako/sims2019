package com.balicamp.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.balicamp.Constants;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.HttpServletRequestContextHolder;

public class RequestFilter implements Filter {
	private SystemParameterManager systemParameterManager;

	public void setSystemParameterManager(SystemParameterManager systemParameterManager) {
		this.systemParameterManager = systemParameterManager;
	}

	public void init(FilterConfig cfg) throws ServletException {
		// do nothing
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
		throws IOException, ServletException {
		// intercept session timeout
		doFilterSessionTimeOut((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);

		// put HttpServletRequest to HttpServletRequestContextHolder 
		HttpServletRequestContextHolder.setRequest((HttpServletRequest) servletRequest);

		// next chain
		filterChain.doFilter(servletRequest, servletResponse);
	}

	public void destroy() {
		// do nothing
	}

	protected void doFilterSessionTimeOut(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		if (systemParameterManager.isPaused()) {
			String requestURL = servletRequest.getRequestURL().toString();

			if ((requestURL.indexOf("/remoteSystemControl.httpinvoker") < 0) &&
					(requestURL.indexOf("/maintenance.jsp") < 0)) {
				servletResponse.sendRedirect(servletRequest.getContextPath() + "/maintenance.jsp");
			}
		}
	}

	protected int getLoginTimeOut() {
		return systemParameterManager.getIntValue(new SystemParameterId(Constants.SystemParameter.Security.GROUP,
				Constants.SystemParameter.Security.SESION_TIMEOUT_PERIODE), 1800);
	}
}