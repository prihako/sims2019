package com.balicamp.webapp.tapestry;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class EmptyTableModel implements IBasicTableModel {
	
	private Iterator dataIterator;
	public EmptyTableModel(){
		dataIterator = (new ArrayList()).iterator();
	}

	public Iterator getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn, boolean bSortOrder) {		
		return dataIterator;
	}

	public int getRowCount() {
		return 0;
	}

}
