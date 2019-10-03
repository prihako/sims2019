package com.balicamp.webapp.tapestry.component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

abstract public class InsertNumber extends AbstractComponent {

	abstract public Object getValue();
	abstract public void setValue(Object value);

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {

		String num = null;
		try {
			Object o = getValue();
			if(o instanceof String){
				num = String.valueOf(o);
			}else if(o instanceof Double){
				num = String.valueOf(((Double) o).intValue());
			}else if(o instanceof Long){
				num = ((Long)(o)).toString();
			}else if(o instanceof BigDecimal){
				num = String.valueOf(((BigDecimal) o).intValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error at @InsertNumber, getValue() =  " +  getValue());
			System.out.println("Error at @InsertNumber, getValue() =  " +String.valueOf(getValue()));
		}
		writer.print(num);
		renderBody(writer,cycle);
	}
}
