package com.balicamp.model.common;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * For save PendingExecution
 * @author Arif
 */
public class PendingExecution implements Serializable {
	private static final long serialVersionUID = -7027330214492840715L;
	
	protected String beanName;
	protected String methodName;
	protected Serializable[] paramList;
	protected Class[] classList;
	
	public PendingExecution(){
	}
	
	public String getBeanName() {
		return beanName;
	}
	public String getMethodName() {
		return methodName;
	}
	public Serializable[] getParamList() {
		return paramList;			//NOPMD
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public void setParamList(Serializable[] paramList) {
		this.paramList = paramList;
	}
	public Class[] getClassList() {
		return classList;	//NOPMD		
	}
	public void setClassList(Class[] classList) {
		this.classList = classList;		//NOPMD
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("paramList", this.paramList)
			.append("methodName", this.methodName)
			.append("beanName", this.beanName)
			.append("classList", this.classList)
			.toString();
	}	
	
	
}
