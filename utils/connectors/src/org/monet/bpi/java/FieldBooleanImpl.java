package org.monet.bpi.java;

import org.monet.api.space.backservice.impl.model.Attribute;
import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.bpi.FieldBoolean;
import org.monet.metamodel.BooleanFieldProperty;

public class FieldBooleanImpl extends FieldImpl<Boolean> implements FieldBoolean {

	public static Boolean get(Attribute attribute) {
		FieldBooleanImpl fieldBoolean = new FieldBooleanImpl();
		fieldBoolean.attribute = attribute;
		return fieldBoolean.get();
	}

	public static void set(Attribute attribute, BooleanFieldProperty fieldDefinition, Boolean value) {
		FieldBooleanImpl fieldBoolean = new FieldBooleanImpl();
		fieldBoolean.attribute = attribute;
		fieldBoolean.fieldDefinition = fieldDefinition;
		fieldBoolean.set(value);
	}

	@Override
	public Boolean get() {
		Attribute optionAttribute = this.getAttribute(Attribute.OPTION);

		// Compatibility mode
		if (optionAttribute != null) this.attribute = optionAttribute;

		String value = this.getIndicatorValue(this.attribute, Indicator.CODE);
		Boolean boolValue;

		if (value.equals("")) boolValue = false;
		else boolValue = Boolean.parseBoolean(value);
		return boolValue;
	}

	@Override
	public boolean isChecked() {
		return this.get();
	}

	@Override
	public void set(Boolean state) {
		String code, value;
		if (state) {
			code = "true";
			value = "X";
		} else {
			code = "false";
			value = "";
		}

		if (this.attribute != null) {
			Attribute optionAttribute = this.attribute.getAttributeList().get(Attribute.OPTION);
			if (optionAttribute == null) {
				optionAttribute = new Attribute();
				optionAttribute.setCode(Attribute.OPTION);
				this.attribute.getAttributeList().add(optionAttribute);
			}
			optionAttribute.addOrSetIndicatorValue(Indicator.CODE, 0, code);
			optionAttribute.addOrSetIndicatorValue(Indicator.VALUE, 1, value);
			this.attribute.getIndicatorList().delete(Indicator.CODE);
			this.attribute.getIndicatorList().delete(Indicator.VALUE);
		}

	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof Boolean)
			return this.get().equals(value);
		else
			return false;
	}

	@Override
	public void clear() {
		this.set(false);
	}

}