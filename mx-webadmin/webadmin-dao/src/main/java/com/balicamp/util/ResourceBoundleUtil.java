package com.balicamp.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;

import com.balicamp.Constants;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class ResourceBoundleUtil {
	public static String getMessage(String key) {
		Locale locale = LocaleContextHolder.getLocale();
		String value = key;
        try {
        	value = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale).getString(key);
        } catch (Exception exception) {
        	value = key;
        	//ignore
        }
        return value;
    }

}
