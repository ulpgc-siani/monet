package org.monet.metamodel;

// FieldProperty
// Declaración abstracta que se utiliza para  modelar un campo de un formulario

public abstract class FieldProperty extends FieldPropertyBase {

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
