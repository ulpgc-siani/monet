package client.core.system.fields;

import client.core.model.definition.entity.field.MemoFieldDefinition;

public class MemoField extends Field<MemoFieldDefinition, String> implements client.core.model.fields.MemoField {
	private String value;

	public MemoField() {
		super(Type.MEMO);
		this.value = "";
	}

	public MemoField(String code, String label) {
		super(code, label, Type.MEMO);
		this.value = "";
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.MemoField.CLASS_NAME;
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
