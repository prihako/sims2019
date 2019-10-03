package com.balicamp.model.common;

import java.io.Serializable;

/**
 * This class keep paging for loaded list requirement.
 * @author Harun Al Rasyid
 *
 */
public class ListPaging implements Serializable{
	
	private static final long serialVersionUID = 176321849213L;
	
	private int maxRow;
	private int	nextRecord;
	private int countList;
	private boolean sort;
	private String sortedColumn;
	
	public ListPaging() {		
	}
	
	public ListPaging(int maxrow, int nextRow, boolean sort, String sortedColumn) {
		this.maxRow = maxrow;
		this.nextRecord = nextRow;		
		this.sort = sort;
		this.sortedColumn = sortedColumn;
	}
	
	public int getCountList() {
		return countList;
	}
	public void setCountList(int countList) {
		this.countList = countList;
	}
	public int getMaxRow() {
		return maxRow;
	}
	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}
	public int getNextRecord() {
		return nextRecord;
	}
	public void setNextRecord(int nextRecord) {
		this.nextRecord = nextRecord;
	}
	public boolean isSort() {
		return sort;
	}
	public void setSort(boolean sort) {
		this.sort = sort;
	}
	public String getSortedColumn() {
		return sortedColumn;
	}
	public void setSortedColumn(String sortedColumn) {
		this.sortedColumn = sortedColumn;
	}	
}
