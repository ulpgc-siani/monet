package client.core.system.fields;

import client.core.model.definition.entity.field.CheckFieldDefinition;
import client.core.model.Source;
import client.core.model.types.CheckList;

public class CheckField extends Field<CheckFieldDefinition, CheckList> implements client.core.model.fields.CheckField {
	private CheckList value;
	private Source source;

	public CheckField() {
		super(Type.CHECK);
	}

	public CheckField(String code, String label) {
		this(code, label, null);
	}

	public CheckField(String code, String label, CheckList value) {
		super(code, label, Type.CHECK);
		this.value = value;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.CheckField.CLASS_NAME;
	}

	@Override
	public CheckList getValue() {
		return this.value;
	}

	@Override
	public String getValueAsString() {
		return "";
	}

	@Override
	public void setValue(CheckList value) {
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

	@Override
    public Source getSource() {
        return this.source;
    }

	public void setSource(Source source) {
		this.source = source;
	}

}
