package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.SerialFieldDefinition;
import client.core.system.fields.SerialField;

public class SerialFieldConstructor extends FieldConstructor<SerialFieldDefinition> {

    @Override
    protected Field constructSingle(SerialFieldDefinition definition) {
        return new SerialField();
    }

    @Override
    protected Field constructMultiple(SerialFieldDefinition definition) {
        /*MultipleSerialField field = new MultipleSerialField();
        field.setFields(new MonetList<client.core.model.fields.SerialField>());
        return field;*/
        return null;
    }
}
