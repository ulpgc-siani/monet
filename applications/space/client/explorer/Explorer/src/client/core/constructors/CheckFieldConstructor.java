package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.CheckFieldDefinition;
import client.core.system.fields.CheckField;

public class CheckFieldConstructor extends FieldConstructor<CheckFieldDefinition>{
    @Override
    protected Field constructSingle(CheckFieldDefinition definition) {
        return new CheckField();
    }

    @Override
    protected Field constructMultiple(CheckFieldDefinition definition) {
        throw new RuntimeException("Use SelectFieldEmbedded");
    }
}
