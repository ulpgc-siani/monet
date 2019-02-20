package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.BooleanFieldDefinition;
import client.core.system.fields.BooleanField;

public class BooleanFieldConstructor extends FieldConstructor<BooleanFieldDefinition> {

    @Override
    protected Field constructSingle(BooleanFieldDefinition definition) {
        return new BooleanField();
    }

    @Override
    protected Field constructMultiple(BooleanFieldDefinition definition) {
        throw new RuntimeException("MultipleBooleanField is not supported.");
    }
}
