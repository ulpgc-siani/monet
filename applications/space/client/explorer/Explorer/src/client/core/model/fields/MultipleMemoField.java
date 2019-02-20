package client.core.model.fields;

import client.core.model.MultipleField;
import client.core.model.definition.entity.field.MemoFieldDefinition;

public interface MultipleMemoField extends MultipleField<MemoField, MemoFieldDefinition, String> {

    ClassName CLASS_NAME = new ClassName("MultipleMemoField");
}
