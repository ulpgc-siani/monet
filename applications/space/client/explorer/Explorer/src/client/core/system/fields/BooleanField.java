package client.core.system.fields;

import client.core.model.definition.entity.field.BooleanFieldDefinition;

public class BooleanField extends Field<BooleanFieldDefinition, Boolean> implements client.core.model.fields.BooleanField {
	private boolean value;

	public BooleanField() {
		super(Type.BOOLEAN);
		this.value = false;
	}

	public BooleanField(String code, String label) {
		this(code, label, false);
	}

	public BooleanField(String code, String label, Boolean value) {
		super(code, label, Type.BOOLEAN);
		this.value = value;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.BooleanField.CLASS_NAME;
	}


	@Override
	public Boolean getValue() {
		return value;
	}

	@Override
	public String getValueAsString() {
		return String.valueOf(getValue());
	}

	@Override
	public void setValue(Boolean value) {
		this.value = value;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
		return this.getValue() == null;
	}

}
