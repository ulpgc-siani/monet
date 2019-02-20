package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.DateFieldDefinition;
import client.core.system.MonetList;
import client.core.system.fields.DateField;
import client.core.system.fields.MultipleDateField;

public class DateFieldConstructor extends FieldConstructor<DateFieldDefinition> {
    @Override
    protected Field constructSingle(DateFieldDefinition definition) {
        return new DateField();
    }

    @Override
    protected Field constructMultiple(DateFieldDefinition definition) {
        MultipleDateField field = new MultipleDateField();
        field.setFields(new MonetList<client.core.model.fields.DateField>());
        return field;
    }
}
