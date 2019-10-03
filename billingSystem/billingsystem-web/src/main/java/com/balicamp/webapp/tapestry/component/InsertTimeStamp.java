package com.balicamp.webapp.tapestry.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

abstract public class InsertTimeStamp extends AbstractComponent {

	public static final String str_ddMMyyyy_HHmmss = "dd/MM/yyyy HH:mm:ss";
	public static final String str_ddMMyyyy = "dd-MM-yyyy";
	public static final String str_HH_mm_ss = "HH:mm:ss";

	abstract public Object getValue();
	abstract public void setValue(Object value);

	abstract public String getType();
	abstract public void setType(String format);

	abstract public String getFormat();
	abstract public void setFormat(String pattern);

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
	{
		SimpleDateFormat sdf = null;
		String sb = "";
		try
		{
			sdf = new SimpleDateFormat(determineFormat());

			Object object = getValue();
			Date resultValue = null;

			if(object instanceof Date)
			{
				resultValue = (Date)object;
			}
			if(resultValue!=null)

			sb = sdf.format(resultValue);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		writer.print(sb);
		renderBody(writer,cycle);
	}

	private String determineFormat()
	{
		if(getType()!=null)
		{
			if(getType().equals("date"))
			{
				return str_ddMMyyyy;
			}
			else if(getType().equals("time"))
			{
				return str_HH_mm_ss;
			}
			else
			{
				return str_ddMMyyyy_HHmmss;
			}
		}
		else
		{
			if(getFormat()==null)
				return str_ddMMyyyy_HHmmss;
			return getFormat();
		}
	}
}
