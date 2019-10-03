package com.balicamp.webapp.tapestry.translator;

import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.form.AbstractFormComponentContributor;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.form.translator.Translator;
import org.apache.tapestry.valid.ValidatorException;


/**
 *
 * @author ttirta
 */
public class DateTranslator extends AbstractFormComponentContributor implements Translator {

    public DateTranslator() {
    }

    public DateTranslator(String initializer) {
        super(initializer);
    }

    /* (non-Javadoc)
     * @see org.apache.tapestry.form.translator.Translator#format(org.apache.tapestry.form.IFormComponent, java.util.Locale, java.lang.Object)
     */
    @Override
	public String format(IFormComponent field, Locale locale, Object object) {
        if (object == null)
            return "";

        return formatObject(field, locale, object);
    }


    /* (non-Javadoc)
     * @see org.apache.tapestry.form.translator.Translator#parse(org.apache.tapestry.form.IFormComponent, org.apache.tapestry.form.ValidationMessages, java.lang.String)
     */
    @Override
	public Object parse(IFormComponent field, ValidationMessages messages, String text) throws ValidatorException {
        String value = text == null ? null : text;

        return HiveMind.isBlank(value) ? null : parseText(field, messages, value);
    }

    private String formatObject(IFormComponent field, Locale locale, Object object) {
        String stDate = "";
        String pattern = "pattern.date=yyyy/MM/dd";
        if( pattern != null )
            try{
                SimpleDateFormat df = new SimpleDateFormat(pattern);
                stDate = df.format((Date)object);
            }
        catch(IllegalArgumentException iae){
        }

        return stDate;
    }

    public SimpleDateFormat getDateFormat(Locale locale)
    {
        return new SimpleDateFormat("pattern.date=yyyy/MM/dd", new DateFormatSymbols(locale));
    }

    private Object parseText(IFormComponent field, ValidationMessages messages, String value)
                   throws ValidatorException {
        Date date = null;
        String pattern = "pattern.date=yyyy/MM/dd";
        if( value != null && pattern != null ){
            try{
                SimpleDateFormat df = new SimpleDateFormat(pattern);
                date = df.parse(value);
            }
            catch (ParseException ex){
                throw new ValidatorException(buildMessage(messages, field));
            }
            catch( IllegalArgumentException iae){
            }
        }
        return date;
    }

    private String buildMessage(ValidationMessages messages,IFormComponent field) {
        return MessageFormat.format("Format tanggal tidak valid",
                        new Object[] { field.getDisplayName() }
                );
    }
}
