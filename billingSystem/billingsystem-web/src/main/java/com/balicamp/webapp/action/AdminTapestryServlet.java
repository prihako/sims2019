package com.balicamp.webapp.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.ApplicationServlet;

public class AdminTapestryServlet extends ApplicationServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7450528739769007079L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	
		super.doGet(request, response);
		//System.out.println("fafhsdakashf dsa ");
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.doPost(request, response);
	}
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}
}
