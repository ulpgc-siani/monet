package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.SummationFieldDefinition;
import client.core.system.fields.SummationField;

public class SummationFieldConstructor extends FieldConstructor<SummationFieldDefinition> {

    @Override
    protected Field constructSingle(SummationFieldDefinition definition) {
        return new SummationField();
    }

    @Override
    protected Field constructMultiple(SummationFieldDefinition definition) {
        /*MultipleSummationField field = new MultipleSummationField();
        field.setFields(new MonetList<client.core.model.fields.SummationField>());
        return field;*/
        return null;
    }
}
