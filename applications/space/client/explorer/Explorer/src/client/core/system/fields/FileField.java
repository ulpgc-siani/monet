package client.core.system.fields;

import client.core.model.definition.entity.field.FileFieldDefinition;
import client.core.model.types.File;

public class FileField extends Field<FileFieldDefinition, File> implements client.core.model.fields.FileField {
	private File value;

	public FileField() {
		super(Type.FILE);
	}

	public FileField(String code, String label) {
		super(code, label, Type.FILE);
	}

	@Override
	public File getValue() {
		return this.value;
	}

	@Override
	public String getValueAsString() {
		return getValue().getId();
	}

	@Override
	public void setValue(File value) {
		this.value = value;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
		File value = this.getValue();
		return value == null || value.getLabel() == null || value.getLabel().isEmpty();
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.FileField.CLASS_NAME;
	}

}
