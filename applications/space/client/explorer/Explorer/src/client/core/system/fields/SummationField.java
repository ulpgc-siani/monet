package client.core.system.fields;

import client.core.model.definition.entity.field.SummationFieldDefinition;

public class SummationField extends Field<SummationFieldDefinition, String> implements client.core.model.fields.SummationField {
	private String value;

	public SummationField() {
		super(Type.SUMMATION);
	}

	public SummationField(String code, String label) {
		super(code, label, Type.SUMMATION);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.SummationField.CLASS_NAME;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getValueAsString() {
		return getValue();
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
		String value = this.getValue();
		return value == null || value.isEmpty();
	}

}
