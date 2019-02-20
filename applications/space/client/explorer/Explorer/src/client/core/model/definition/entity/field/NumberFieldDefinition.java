package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface NumberFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("NumberFieldDefinition");

	enum Edition {
		BUTTON, SLIDER;

		public static Edition fromString(String format) {
			return Edition.valueOf(format.toUpperCase());
		}
	}

	String getFormat();
	RangeDefinition getRange();
	Edition getEdition();

	interface RangeDefinition {
		long getMin();
		long getMax();

	}
}
