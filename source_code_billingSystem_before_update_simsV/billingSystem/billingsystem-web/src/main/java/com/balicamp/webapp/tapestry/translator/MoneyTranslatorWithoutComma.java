package com.balicamp.webapp.tapestry.translator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponentContributor;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.form.translator.Translator;
import org.apache.tapestry.valid.ValidatorException;

/**
 *
 * @author ttirta
 */
public class MoneyTranslatorWithoutComma extends AbstractFormComponentContributor implements Translator {

    public MoneyTranslatorWithoutComma() {
    }

    public MoneyTranslatorWithoutComma(String initializer) {
        super(initializer);
    }

    /* (non-Javadoc)
     * @see org.apache.tapestry.form.translator.Translator#format(org.apache.tapestry.form.IFormComponent, java.util.Locale, java.lang.Object)
     */
    /**
     * Presentation value
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
        String money = "";
        String pattern = "#,###";
        if( pattern != null )
            try{
            	DecimalFormat decimalFormat = getMoneyFormat(locale);
                money = decimalFormat.format((new BigDecimal(object.toString())).doubleValue());
            }
        catch(IllegalArgumentException iae){
        }

        return money;
    }

    private String getMoneyPattern(Locale locale){
    	return "#,###";
    }

    public DecimalFormat getMoneyFormat(Locale locale){
    	DecimalFormat formatter = new DecimalFormat(getMoneyPattern(locale));
        return formatter;
    }

    /**
     * For return type
     * @param field
     * @param messages
     * @param value
     * @return
     * @throws ValidatorException
     */
    private Object parseText(IFormComponent field, ValidationMessages messages, String value)throws ValidatorException {
        BigDecimal money = null;

        if( value != null ){
            try{
                DecimalFormat df = getMoneyFormat(messages.getLocale());
                df.setParseBigDecimal(true);
                Number isBigDecimal = df.parse(value);
                money = (BigDecimal)isBigDecimal;
                money.setScale(2,RoundingMode.HALF_UP);
            }
            catch (ParseException ex){
                throw new ValidatorException(buildMessage(messages, field));
            }
            catch( IllegalArgumentException iae){
            }
        }
        return money;
    }

    private String buildMessage(ValidationMessages messages,IFormComponent field) {
        return MessageFormat.format("Jumlah uang yang valid adalah mulai 0.00 sampai dengan 99,999,999,999.00",
                        new Object[] { field.getDisplayName() }
                );
    }

    @Override
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle, FormComponentContributorContext context, IFormComponent component) {
    	super.renderContribution(writer, cycle, context, component);

    	writer.attribute("onblur", "formatmoney(this, '.', ',')");
    	writer.attribute("onfocus", "unformatmoney(this, '.', ',')");
    	writer.attribute("onkeypress", "return keypressmoney(this, event, '')");
    }
}
