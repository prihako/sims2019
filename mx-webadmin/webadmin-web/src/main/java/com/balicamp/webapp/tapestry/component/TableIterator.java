package com.balicamp.webapp.tapestry.component;

import java.util.Iterator;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.bean.EvenOdd;
import org.apache.tapestry.contrib.table.components.AbstractTableViewComponent;
import org.apache.tapestry.contrib.table.model.IFullTableModel;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableRowSource;

/**
 * @author mindbridge
 */
public abstract class TableIterator 
	extends AbstractTableViewComponent 
	implements ITableRowSource
{

    // Transient
    private Object m_objTableRow = null;
    private int m_nTableIndex;
    
    // Parameters
    public abstract Object getFullSourceParameter();

    /**
     * Returns the currently rendered table row. You can call this method to
     * obtain the current row.
     * 
     * @return Object the current table row
     */
    public Object getTableRow()
    {
        return m_objTableRow;
    }

    /**
     * Sets the currently rendered table row. This method is for internal use
     * only.
     * 
     * @param tableRow
     *            The current table row
     */
    public void setTableRow(Object tableRow)
    {
        m_objTableRow = tableRow;

        IBinding objRowBinding = getBinding("row");
        if (objRowBinding != null) objRowBinding.setObject(tableRow);
    }

    /**
     * Returns the index of the currently rendered table row. You can call this
     * method to obtain the index of the current row.
     * 
     * @return int the current table index
     */
    public int getTableIndex()
    {
        return m_nTableIndex;
    }

    /**
     * Sets the index of the currently rendered table row. This method is for
     * internal use only.
     * 
     * @param tableIndex
     *            The index of the current table row
     */
    public void setTableIndex(int tableIndex)
    {
        m_nTableIndex = tableIndex;

        IBinding objIndexBinding = getBinding("index");
        if (objIndexBinding != null)
            objIndexBinding.setObject(new Integer(tableIndex));
    }

    /**
     * Get the list of all table rows to be displayed on this page.
     * 
     * @return an iterator of all table rows
     */
    public Iterator getTableRowsIterator()
    {
        ITableModel objTableModel = getTableModelSource().getTableModel();
        return objTableModel.getCurrentPageRows();
    }

    public Integer getColumnCount()
    {
        ITableModel objTableModel = getTableModelSource().getTableModel();
        return objTableModel.getColumnModel().getColumnCount();
    }
    public Integer getRowCount()
    {
        ITableModel objTableModel = getTableModelSource().getTableModel();
        return objTableModel.getRowCount();
    }
    
    public Object getFullSource()
    {
        ITableModel objTableModel = getTableModelSource().getTableModel();
        if (objTableModel instanceof IFullTableModel)
            return ((IFullTableModel) objTableModel).getRows();
        return getFullSourceParameter();
    }
    
    public String[] getAdditionalData() {
    	return new String[ getColumnCount() - ((getTableIndex()+1)%getColumnCount())];
    }


    /**
     * @see org.apache.tapestry.BaseComponent#renderComponent(IMarkupWriter,
     *      IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Object objOldValue = cycle
                .getAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE);
        cycle.setAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE, this);

        super.renderComponent(writer, cycle);

        cycle.setAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE,
                objOldValue);

        // set the current row to null when the component is not active
        m_objTableRow = null;
    }

	@Parameter(required = false, defaultValue = "ognl:new org.apache.tapestry.bean.EvenOdd()")
	public abstract EvenOdd getEvenOdd();
	
	public String getTr(){
		return "<tr class=\"" + getEvenOdd().getNext() + "\" >";
	}

}
