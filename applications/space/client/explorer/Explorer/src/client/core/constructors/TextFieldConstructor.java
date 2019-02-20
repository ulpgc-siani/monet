package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.fields.TextField;
import client.core.system.MonetList;
import client.core.system.fields.MultipleTextField;

public class TextFieldConstructor extends FieldConstructor<TextFieldDefinition> {

    @Override
    public Field constructSingle(TextFieldDefinition definition) {
        return new client.core.system.fields.TextField(definition.getCode(), definition.getLabel());
    }

    @Override
    public Field constructMultiple(TextFieldDefinition definition) {
        MultipleTextField field = new MultipleTextField();
        field.setFields(new MonetList<TextField>());
        return field;
    }
}
