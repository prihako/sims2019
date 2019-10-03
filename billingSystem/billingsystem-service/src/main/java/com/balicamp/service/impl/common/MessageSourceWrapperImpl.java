package com.balicamp.service.impl.common;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.balicamp.service.common.MessageSourceWrapper;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
@Service("messageSourceWrapper")
//@Service
public class MessageSourceWrapperImpl implements MessageSourceWrapper {

	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessage(String key, Object[] args, String defaultMessage, Locale locale) {
		if (locale == null)
			locale = LocaleContextHolder.getLocale();

		if (locale == null)
			locale = new Locale("in");

		return messageSource.getMessage(key, args, defaultMessage, locale);
	}

}
