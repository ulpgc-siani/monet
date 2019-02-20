package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface SerialFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("SerialFieldDefinition");

	SerialDefinition getSerial();

	interface SerialDefinition {
		String getFormat();
		String getName();
	}
}