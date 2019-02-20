package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.system.MonetList;
import client.core.system.fields.CompositeField;
import client.core.system.fields.MultipleCompositeField;

public class CompositeFieldConstructor extends FieldConstructor<CompositeFieldDefinition> {

    @Override
    protected Field constructSingle(CompositeFieldDefinition definition) {
        return new CompositeField();
    }

    @Override
    protected Field constructMultiple(CompositeFieldDefinition definition) {
        MultipleCompositeField field = new MultipleCompositeField();
        field.setFields(new MonetList<client.core.model.fields.CompositeField>());
        return field;
    }
}
