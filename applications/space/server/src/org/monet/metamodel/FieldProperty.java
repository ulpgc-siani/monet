package org.monet.metamodel;

// FieldProperty
// Declaración abstracta que se utiliza para  modelar un campo de un formulario

import org.monet.space.kernel.model.FieldType;

public abstract class FieldProperty extends FieldPropertyBase {

	public String getType() {
		FieldType type = null;

		if (isBoolean()) type = FieldType._boolean;
		else if (isCheck()) type = FieldType._check;
		else if (isDate()) type = FieldType._date;
		else if (isFile()) type = FieldType._file;
		else if (isLink()) type = FieldType._link;
		else if (isSummation()) type = FieldType._summation;
		else if (isMemo()) type = FieldType._memo;
		else if (isNode()) type = FieldType._node;
		else if (isNumber()) type = FieldType._number;
		else if (isPicture()) type = FieldType._picture;
		else if (isComposite()) type = FieldType._composite;
		else if (isSelect()) type = FieldType._select;
		else if (isSerial()) type = FieldType._serial;
		else if (isText()) type = FieldType._text;

		if (type == null)
			return "";

		return type.toString().replace("_", "");
	}

	public boolean isBoolean() {
		return this instanceof BooleanFieldProperty;
	}

	public boolean isCheck() {
		return this instanceof CheckFieldProperty;
	}

	public boolean isDate() {
		return this instanceof DateFieldProperty;
	}

	public boolean isFile() {
		return this instanceof FileFieldProperty;
	}

	public boolean isLink() {
		return this instanceof LinkFieldProperty;
	}

	public boolean isSummation() {
		return this instanceof SummationFieldProperty;
	}

	public boolean isMemo() {
		return this instanceof MemoFieldProperty;
	}

	public boolean isNode() {
		return this instanceof NodeFieldProperty;
	}

	public boolean isNumber() {
		return this instanceof NumberFieldProperty;
	}

	public boolean isPicture() {
		return this instanceof PictureFieldProperty;
	}

	public boolean isComposite() {
		return this instanceof CompositeFieldProperty;
	}

	public boolean isSelect() {
		return this instanceof SelectFieldProperty;
	}

	public boolean isSerial() {
		return this instanceof SerialFieldProperty;
	}

	public boolean isText() {
		return this instanceof TextFieldProperty;
	}

	public boolean isMultiple() {
		if (this instanceof MultipleableFieldProperty)
			return ((MultipleableFieldProperty) this).isMultiple();
		return false;
	}

}
