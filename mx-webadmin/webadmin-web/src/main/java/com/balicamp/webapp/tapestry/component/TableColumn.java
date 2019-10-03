package com.balicamp.webapp.tapestry.component;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableModelSource;

public abstract class TableColumn extends AbstractComponent {
    public ITableModelSource getTableModelSource() {
        IRequestCycle objCycle = getPage().getRequestCycle();

        ITableModelSource objSource = (ITableModelSource) objCycle
                .getAttribute("org.apache.tapestry.contrib.table.model.ITableModelSource");

        if (objSource == null) {
            throw new ApplicationRuntimeException("The component " + getId()
                    + " must be contained within an ITableModelSource component, such as TableView", this, null, null);
        }

        return objSource;
    }

    public abstract String getElement();
    public abstract void setElement(String element);

    @Override
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        String element = isParameterBound("element") ? getElement() : getTemplateTagName();
        boolean rewinding = cycle.isRewinding();

        if (!rewinding) {
            if (getId() != null) {
                if (getTableModelSource().getTableModel().getColumnModel().getColumn(getId()) == null) {
                    return;
                }
            }
            
            writer.begin(element);
            renderInformalParameters(writer, cycle);

            if ((getId() != null) && !isParameterBound("id")) {
                renderIdAttribute(writer, cycle);
            }
        }

        renderBody(writer, cycle);

        if (!rewinding) {
            writer.end();
        }
    }
}
