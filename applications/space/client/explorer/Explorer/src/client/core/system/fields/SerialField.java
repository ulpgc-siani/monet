package client.core.system.fields;

import client.core.model.definition.entity.field.SerialFieldDefinition;

public class SerialField extends Field<SerialFieldDefinition, String> implements client.core.model.fields.SerialField {
	private String value;

	public SerialField() {
		super(Type.SERIAL);
	}

	public SerialField(String code, String label) {
		super(code, label, Type.SERIAL);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.SerialField.CLASS_NAME;
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
