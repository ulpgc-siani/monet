package client.core.system.fields;

import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.core.model.types.Picture;

public class PictureField extends Field<PictureFieldDefinition, Picture> implements client.core.model.fields.PictureField {
	private Picture value;

	public PictureField() {
		super(Type.PICTURE);
	}

	public PictureField(String code, String label) {
		super(code, label, Type.PICTURE);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.PictureField.CLASS_NAME;
	}

	@Override
	public Picture getValue() {
		return this.value;
	}

	@Override
	public String getValueAsString() {
		return getValue().getId();
	}

	@Override
	public void setValue(Picture value) {
		this.value = value;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
		Picture value = this.getValue();
		return value == null || value.getId().isEmpty();
	}

}
