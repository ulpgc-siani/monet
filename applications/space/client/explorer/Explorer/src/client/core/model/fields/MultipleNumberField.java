package client.core.model.fields;

import client.core.model.Instance;
import client.core.model.MultipleField;
import client.core.model.definition.entity.field.NumberFieldDefinition;

public interface MultipleNumberField extends MultipleField<NumberField, NumberFieldDefinition, client.core.model.types.Number> {

    Instance.ClassName CLASS_NAME = new Instance.ClassName("MultipleNumberField");
}
