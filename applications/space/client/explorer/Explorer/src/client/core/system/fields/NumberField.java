package client.core.system.fields;

import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.model.types.Number;

public class NumberField extends Field<NumberFieldDefinition, Number> implements client.core.model.fields.NumberField {
	private Number value;
	private String formattedValue;

	public NumberField() {
		super(Type.NUMBER);
	}

	public NumberField(String code, String label) {
		super(code, label, Type.NUMBER);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.NumberField.CLASS_NAME;
	}

	@Override
	public Number getValue() {
		return this.value;
	}

	@Override
	public String getValueAsString() {
		return formattedValue == null ? value.toString() : formattedValue;
	}

	@Override
	public void setValue(Number value) {
		this.value = value;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public void setFormattedValue(String value) {
		this.formattedValue = value;
	}

	@Override
	public final boolean isNullOrEmpty() {
		return getValue() == null;
	}

}
