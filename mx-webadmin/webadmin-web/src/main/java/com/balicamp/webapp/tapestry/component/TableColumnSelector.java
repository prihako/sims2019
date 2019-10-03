package com.balicamp.webapp.tapestry.component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.engine.IEngineService;

import com.balicamp.webapp.action.common.TableColumnSelectorPopup;
import com.balicamp.webapp.action.common.TableColumnSelectorPopup.Entry;

public abstract class TableColumnSelector extends BaseComponent {
    private static final int POPUP_WIDTH = 450;

    private static final int POPUP_HEIGHT = 400;

    @InjectObject("service:tapestry.globals.HttpServletRequest")
    public abstract HttpServletRequest getRequest();

    @InjectObject("service:tapestry.globals.HttpServletResponse")
    public abstract HttpServletResponse getResponse();

    @InjectObject("engine-service:page")
    public abstract IEngineService getEngineService();

    public abstract IAsset getIcon();

    public abstract String getCaption();

    public abstract String getTableId();

    public abstract void setColumns(String columns);

    public abstract String getAllColumns();

    public String getPopupLink() {
        String popupLink = getEngineService().getLink(false, "tableColumnSelectorPopup").getAbsoluteURL();

        if (popupLink.indexOf('?') >= 0) {
            popupLink += "&";
        } else {
            popupLink += "?";
        }

        try {
            popupLink += "tableId=" + URLEncoder.encode(getTableId(), "UTF-8") + "&refreshPage="
                    + URLEncoder.encode(getPage().getPageName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            popupLink += "tableId=" + getTableId() + "&refreshPage=" + getPage().getPageName();
        }

        return "javascript:showPopWin('" + popupLink + "', " + POPUP_WIDTH + ", " + POPUP_HEIGHT + ", null, true)";
    }

    @Override
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        HttpSession session = getRequest().getSession(true);
        
        String key = getTableId() + "@TableColumnSelector";
        Object value = session.getAttribute(key);
        
        String columns;

        if (!cycle.isRewinding()) {
            if (value == null) {
                columns = getAllColumns();
                session.setAttribute(key, TableColumnSelectorPopup.parse(columns));
            } else {
                columns = TableColumnSelectorPopup.toString((List<Entry>) value);
            }

            setColumns(columns);
        } else {
            if (value != null) {
                columns = TableColumnSelectorPopup.toString((List<Entry>) value);
                setColumns(columns);
            }
        }

        super.renderComponent(writer, cycle);
    }
}
