package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.system.fields.NumberField;

public class NumberFieldConstructor extends FieldConstructor<NumberFieldDefinition> {

    @Override
    protected Field constructSingle(NumberFieldDefinition definition) {
        return new NumberField();
    }

    @Override
    protected Field constructMultiple(NumberFieldDefinition definition) {
        /*MultipleNumberField field = new MultipleNumberField();
        field.setFields(new MonetList<client.core.model.fields.NumberField>());
        return field;*/
        return null;
    }
}
