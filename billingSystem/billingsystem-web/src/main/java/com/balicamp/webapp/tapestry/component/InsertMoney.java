package com.balicamp.webapp.tapestry.component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

abstract public class InsertMoney extends AbstractComponent {

	abstract public Object getValue();
	abstract public void setValue(Object value);

	public boolean getDefaultDecimalFlag(){
		return true;
	}

	abstract public boolean getDecimalFlag();
	abstract public void setDecimalFlag(boolean useDecimal);

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		DecimalFormat formatter = null;
		if(getDecimalFlag() == getDefaultDecimalFlag())
//			formatter = new DecimalFormat("#,##0.00");
			formatter = new DecimalFormat("Rp ###,###");

		else
			formatter = new DecimalFormat("Rp ###,###");

		String num = null;
		try {
			Object o = getValue();
			if(o instanceof String){
				num = formatter.format(new BigDecimal(String.valueOf(o)));
			}else if(o instanceof Double){
				num = formatter.format(o);
			}else if(o instanceof Long){
				num = formatter.format(o);
			}else if(o instanceof BigDecimal){
				num = formatter.format(o);
			}else if(o instanceof List){
				List list =(List) o;
				if (list.size()>0) {
					num=formatter.format(Long.parseLong(String.valueOf(list.get(0))));
					if (list.size()>1) {
						for (int i = 1; i < list.size(); i++) {
							num= num+", "+ formatter.format(Long.parseLong(String.valueOf(list.get(i))));
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(getValue());
			System.out.println(String.valueOf(getValue()));
		}
		writer.print(num);
		renderBody(writer,cycle);
	}
}
