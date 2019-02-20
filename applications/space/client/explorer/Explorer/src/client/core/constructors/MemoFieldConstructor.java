package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.MemoFieldDefinition;
import client.core.system.fields.MemoField;

public class MemoFieldConstructor extends FieldConstructor<MemoFieldDefinition> {

    @Override
    protected Field constructSingle(MemoFieldDefinition definition) {
        return new MemoField();
    }

    @Override
    protected Field constructMultiple(MemoFieldDefinition definition) {
        /*MultipleMemoField field = new MultipleMemoField();
        field.setFields(new MonetList<client.core.model.fields.MemoField>());
        return field;*/
        return null;
    }
}
