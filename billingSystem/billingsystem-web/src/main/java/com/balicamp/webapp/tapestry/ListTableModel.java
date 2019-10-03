package com.balicamp.webapp.tapestry;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;

/**
 * Generic implementation of IBasicTableModel
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: ListTableModel.java 114 2012-12-12 04:18:29Z bagus.sugitayasa $
 */
public class ListTableModel<T> implements IBasicTableModel, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7754008327413092187L;
	protected List<T> dataList;
		
	public ListTableModel(){	
	}

	public ListTableModel( List<T> dataList ){
		this.dataList = dataList;
	}

	public Iterator<T> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn, boolean bSortOrder) {
		return dataList.iterator();
	}

	public int getRowCount() {
		if ( dataList == null )
			return 0;
		return dataList.size();
	}

}
