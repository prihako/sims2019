package com.balicamp.util.wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class ListWrapper {
	private List<Object> list = new ArrayList<Object>();

	public List<Object> getList() {
		return list;
	}
	public void setList(List<Object> list) {
		this.list = list;
	}
	
}
