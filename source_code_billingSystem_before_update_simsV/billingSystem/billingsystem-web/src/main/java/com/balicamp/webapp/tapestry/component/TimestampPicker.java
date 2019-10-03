package com.balicamp.webapp.tapestry.component;

import java.text.ParseException;
import java.util.Date;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.balicamp.util.DateUtil;

public abstract class TimestampPicker extends BaseComponent {

	public abstract Date getDate();

	public abstract void setDate(Date date);

	public abstract String getHour();

	public abstract void setHour(String hour);

	public abstract String getMinutes();

	public abstract void setMinutes(String minutes);

	public abstract String getSeconds();

	public abstract void setSeconds(String seconds);

	public abstract Date getValue();

	public abstract void setValue(Date value);

	public abstract void setLabel(String label);

	public abstract String getLabel();

	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		super.renderComponent(writer, cycle);
		if (cycle.isRewinding()) {
			Date timestamp = null;
			try {
				if (getDate() != null) {
					if (getMinutes() == null)
						setMinutes("00");
					if (getHour() == null)
						setHour("00");
					if (getSeconds() == null)
						setSeconds("00");
					
					String inputDate = DateUtil.convertDateToString(getDate(), "dd-MM-yyyy");
					timestamp = DateUtil.convertStringToDate("dd-MM-yyyy HH:mm:ss", inputDate + " " + getHour() + ":"
							+ getMinutes() + ":" + getSeconds());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			setValue(timestamp);
		}
	}

}
