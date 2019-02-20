package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.system.MonetList;
import client.core.system.fields.MultipleSelectField;
import client.core.system.fields.SelectField;

public class SelectFieldConstructor extends FieldConstructor<SelectFieldDefinition> {

    @Override
    protected Field constructSingle(SelectFieldDefinition definition) {
        return new SelectField();
    }

    @Override
    protected Field constructMultiple(SelectFieldDefinition definition) {
        MultipleSelectField field = new MultipleSelectField();
        field.setFields(new MonetList<client.core.model.fields.SelectField>());
        return field;
    }
}
