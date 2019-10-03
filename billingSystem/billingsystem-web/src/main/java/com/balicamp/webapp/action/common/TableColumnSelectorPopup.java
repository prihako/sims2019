package com.balicamp.webapp.action.common;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.webapp.action.AdminBasePage;

public abstract class TableColumnSelectorPopup extends AdminBasePage implements PageBeginRenderListener {
    public static class Entry implements Serializable {
        private static final long serialVersionUID = -3545430513513555527L;

        private int id;

        private boolean checked;

        private String text;

        private String settings;

        public Entry() {
            checked = true;
        }

        public Entry(int id, boolean checked, String text, String settings) {
            this.checked = checked;
            this.text = text;
            this.settings = settings;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getSettings() {
            return settings;
        }

        public void setSettings(String settings) {
            this.settings = settings;
        }

        @Override
        public boolean equals(Object o) {
            if ((o == null) || !(o instanceof Entry)) {
                return false;
            }

            if (o == this) {
                return true;
            }

            return id == ((Entry) o).id;
        }
    }

    public static List<Entry> parse(String columns) {
        List<Entry> result = new Vector<Entry>();

        if (columns == null) {
            // TODO !
        } else {
            StringTokenizer tokenizer = new StringTokenizer(columns, ",");
            int id = 0;

            while (tokenizer.hasMoreTokens()) {
                String settings = tokenizer.nextToken();
                StringTokenizer tokenizer2 = new StringTokenizer(settings, ":");

                String name = "";
                if (tokenizer2.hasMoreTokens()) {
                    name = tokenizer2.nextToken();
                }

                String expression = name;
                if (tokenizer2.hasMoreTokens()) {
                    expression = tokenizer2.nextToken();
                }

                String displayName = name;
                if (tokenizer2.hasMoreTokens()) {
                    displayName = expression;
                    expression = tokenizer2.nextToken();
                }

                result.add(new Entry(++id, true, displayName, settings));
            }
        }

        return result;
    }

    public static String toString(List<Entry> l) {
        StringBuffer sb = new StringBuffer();

        if (l != null) {
            boolean first = true;

            for (Entry e : l) {
                if (e.checked) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(",");
                    }

                    sb.append(e.settings);
                }
            }
        }

        return sb.toString();
    }

    @Persist("client")
    public abstract void setClosePopup(boolean status);

    public abstract boolean isClosePopup();

    @Persist("client")
    public abstract void setNotFirstLoad(boolean status);

    public abstract boolean isNotFirstLoad();

    @Persist("client")
    public abstract void setTableId(String tableId);

    public abstract String getTableId();

    @Persist("client")
    public abstract void setRefreshPage(String refreshPage);

    public abstract String getRefreshPage();

    @Persist("client")
    public abstract void setColumns(List<Entry> columns);

    public abstract List<Entry> getColumns();

    public abstract Entry getRow();

    public void pageBeginRender(PageEvent evt) {
        if (!evt.getRequestCycle().isRewinding()) {
            if (!isNotFirstLoad()) {
                initdata(evt.getRequestCycle());

                setNotFirstLoad(true);
            }
        } else if (getColumns() == null) {
            initdata(evt.getRequestCycle());
        }

        super.pageBeginRender(evt);
    }

    public String getRefreshPageURL() {
        return getEngineService().getLink(false, getRefreshPage()).getAbsoluteURL();
    }

    private void initdata(IRequestCycle cycle) {
        String tableId = cycle.getParameter("tableId");
        String refreshPage = cycle.getParameter("refreshPage");

        HttpSession session = getRequest().getSession(true);
        String key = tableId + "@TableColumnSelector";

        setTableId(tableId);
        setRefreshPage(refreshPage);

        setColumns((List<Entry>) session.getAttribute(key));
    }

    public void checkSelect(IRequestCycle cycle, int index) {
        if (cycle.isRewinding()) {
            List<Entry> columns = getColumns();
            Entry column = (Entry) getRow();

            if (columns.contains(column)) {
                column.setChecked(column.isChecked());
            }
        }
    }

    public ILink apply(IRequestCycle cycle) {
        if (getDelegate().getHasErrors()) {
            return null;
        }

        int checked = 0;
        List<Entry> columns = getColumns();

        for (Entry e : columns) {
            if (e.checked) {
                ++checked;
            }
        }

        if (checked == 0) {
            super.addError(getDelegate(), (IFormComponent) null, getText("tcsp.error.zeroChecked"),
                    ValidationConstraint.CONSISTENCY);

            setClosePopup(false);
        } else {
            HttpSession session = getRequest().getSession(true);
            String key = getTableId() + "@TableColumnSelector";

            session.setAttribute(key, columns);
            setClosePopup(true);
        }

        return null;
    }
}
