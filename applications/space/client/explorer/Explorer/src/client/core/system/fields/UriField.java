package client.core.system.fields;

import client.core.model.definition.entity.field.UriFieldDefinition;
import client.core.model.types.Uri;

public class UriField extends Field<UriFieldDefinition, Uri> implements client.core.model.fields.UriField {
	private Uri value;

	public UriField() {
		super(Type.URI);
	}

	public UriField(String code, String label) {
		super(code, label, Type.URI);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.UriField.CLASS_NAME;
	}

	@Override
	public Uri getValue() {
		return this.value;
	}

	@Override
	public String getValueAsString() {
		return getValue().getValue();
	}

	@Override
	public void setValue(Uri value) {
		this.value = value;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
		Uri value = this.getValue();
		return value == null || value.toString().isEmpty();
	}

}
