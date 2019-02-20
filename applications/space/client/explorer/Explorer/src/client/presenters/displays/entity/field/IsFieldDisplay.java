package client.presenters.displays.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.factory.TypeFactory;
import client.presenters.displays.EntityDisplay;

public interface IsFieldDisplay<Type> {

    String getCode();
    String getLabel();
	FieldDefinition.Template getTemplate();
	TypeFactory getTypeFactory();

	Type getValue();
	void setValue(Type value);

    String getInvalidValueCause();

    boolean hasValue();
	void removeValue();

	FieldDefinition.FieldType getFieldType();

	boolean isCollapsible();
	boolean isExtended();
    boolean isReadonly();
	boolean isPoll();

    void focus();
    void blur();

	void showDescription();
	String getEdition();
	String getDescription();
	String getMessageWhenEmpty();
	String getMessageWhenRequired();
    String getMessageWhenReadOnly();
	String getMessageWhenInvalid();

	void onValidValue(ValidValueListener listener);
	void onInvalidValue(InvalidValueListener listener);

    void addHook(EntityDisplay.Hook hook);

	interface ValidValueListener {
		void update();
	}

	interface InvalidValueListener {
		void update();
	}
}
