package client.core.model.definition.entity;

import client.core.model.Instance;
import client.core.model.List;

public interface FieldDefinition extends EntityDefinition, ReferenceableDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("FieldDefinition");

	enum Type {
		BOOLEAN, CHECK, COMPOSITE, DATE, FILE, LINK, MEMO, NODE, NUMBER, PICTURE, SELECT, SERIAL, SUMMATION, TEXT, URI;

		public static Type fromString(String type) {
			return Type.valueOf(type.toUpperCase());
		}
	}

	enum Template {
		INLINE, MULTILINE;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static Template fromString(String template) {
			return Template.valueOf(template.toUpperCase());
		}
	}

	enum FieldType {
		NORMAL, POLL;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	boolean is(String key);
	String getLabel();
	String getDescription();
	Template getTemplate();
	FieldType getFieldType();

	boolean isCollapsible();
	boolean isRequired();
	boolean isReadonly();
	boolean isExtended();
	boolean isSuperField();
	boolean isStatic();
	boolean isUnivocal();

	List<Display> getDisplays();
	Display getDisplay(Display.When when);

	interface Display {
		enum When {
			EMPTY, REQUIRED, READONLY, INVALID;

			public static When fromString(String when) {
				return When.valueOf(when.toUpperCase());
			}
		}

		String getMessage();
		When getWhen();
	}

}
