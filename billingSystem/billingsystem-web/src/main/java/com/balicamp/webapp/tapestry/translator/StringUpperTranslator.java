package com.balicamp.webapp.tapestry.translator;

import java.util.Locale;

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.form.translator.StringTranslator;
import org.apache.tapestry.form.translator.Translator;
import org.apache.tapestry.valid.ValidatorException;

public class StringUpperTranslator extends StringTranslator implements Translator{
	public StringUpperTranslator() {
		
	}
	
    public StringUpperTranslator(String initializer)
    {
        PropertyUtils.configureProperties(this, initializer);
    }
	
	
	@Override
	public String format(IFormComponent field, Locale locale, Object object) {
		return super.format(field, locale, object);
	}

	@Override
	public Object parse(IFormComponent field, ValidationMessages messages, String value) throws ValidatorException {
		Object result = super.parse(field, messages, value);
		if (result != null && result.getClass().equals(String.class)) {
			result = result.toString().toUpperCase();
		}
        return result;
	}
}