package com.balicamp.webapp.action;

import org.apache.tapestry.html.BasePage;

public abstract class DHome extends BasePage{

	private String versi;
	
	public void setVersi(String versi) {
		this.versi = versi;
	}
	
	public String getVersi() {
		System.out.println("4.0.2");
		return "4.0.2";
	}
}
