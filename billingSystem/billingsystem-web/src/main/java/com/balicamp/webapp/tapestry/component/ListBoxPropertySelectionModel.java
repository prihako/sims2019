package com.balicamp.webapp.tapestry.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.form.DefaultOptionRenderer;
import org.apache.tapestry.form.IOptionRenderer;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.ValidatableField;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.valid.ValidatorException;

/**
 * 
 * @author Wayan Ari Agustina
 * @version $Id: $ 
 */
public abstract class ListBoxPropertySelectionModel extends AbstractFormComponent implements ValidatableField {

	public abstract void setSelectedList(Collection<?> selectedList);

	public abstract Collection<?> getSelectedList();

	/**
	 * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
	 */
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		renderDelegatePrefix(writer, cycle);

		writer.begin("select");
		writer.attribute("name", getName());
		writer.attribute("multiple", "true");

		if (isDisabled())
			writer.attribute("disabled", "disabled");

		renderIdAttribute(writer, cycle);

		renderDelegateAttributes(writer, cycle);

		getValidatableFieldSupport().renderContributions(this, writer, cycle);

		// Apply informal attributes.
		renderInformalParameters(writer, cycle);

		writer.println();

		IPropertySelectionModel model = getModel();

		if (model == null)
			throw Tapestry.createRequiredParameterException(this, "model");

		if (getSelectedList() == null)
			setSelectedList(new ArrayList());

		if (getOptionRenderer() == null)
			setOptionRenderer(new DefaultOptionRenderer());

		getOptionRenderer().renderOptions(writer, cycle, model, getSelectedList());

		writer.end(); // <select>

		renderDelegateSuffix(writer, cycle);
	}

	public abstract IPropertySelectionModel getModel();

	public abstract void setModel(IPropertySelectionModel model);

	/** Responsible for rendering individual options. */
	public abstract IOptionRenderer getOptionRenderer();

	public abstract void setOptionRenderer(IOptionRenderer renderer);

	/**
	 * Injected.
	 */
	public abstract ValidatableFieldSupport getValidatableFieldSupport();

	/**
	 * @see org.apache.tapestry.form.AbstractFormComponent#isRequired()
	 */
	public boolean isRequired() {
		return getValidatableFieldSupport().isRequired(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		// get all the values
		String[] optionValues = cycle.getParameters(getName());

		IPropertySelectionModel model = getModel();

		List selectedList = new ArrayList(getModel().getOptionCount());

		// Nothing was selected
		if (optionValues != null) {
			// Go through the array and translate and put back in the list
			for (int i = 0; i < optionValues.length; i++) {
				// Translate the new value
				Object selectedValue = model.translateValue(optionValues[i]);

				// Add this element in the list back
				selectedList.add(selectedValue);
			}
		}

		try {
			getValidatableFieldSupport().validate(this, writer, cycle, selectedList);
			setSelectedList(selectedList);
		} catch (ValidatorException e) {
			getForm().getDelegate().record(e);
		}
	}

}
