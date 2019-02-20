package client.core.model.fields;

import client.core.model.MultipleField;
import client.core.model.definition.entity.field.SerialFieldDefinition;

public interface MultipleSerialField extends MultipleField<SerialField, SerialFieldDefinition, String> {

    ClassName CLASS_NAME = new ClassName("MultipleSerialField");
}
