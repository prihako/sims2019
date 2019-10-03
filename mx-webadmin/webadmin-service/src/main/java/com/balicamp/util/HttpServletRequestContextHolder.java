package com.balicamp.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class HttpServletRequestContextHolder {
	private static ThreadLocal<HttpServletRequest> contextHolder = new ThreadLocal();
	
	public static HttpServletRequest getRequest(){
		return contextHolder.get();
	}

	public static void setRequest( HttpServletRequest httpServletRequest ){
		contextHolder.set(httpServletRequest);
	}

}
