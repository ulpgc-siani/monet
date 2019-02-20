package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.UriFieldDefinition;
import client.core.system.fields.UriField;

public class UriFieldConstructor extends FieldConstructor<UriFieldDefinition> {

    @Override
    protected Field constructSingle(UriFieldDefinition definition) {
        return new UriField();
    }

    @Override
    protected Field constructMultiple(UriFieldDefinition definition) {
        /*MultipleUriField field = new MultipleUriField();
        field.setFields(new MonetList<client.core.model.fields.UriField>());
        return field;*/
        return null;
    }
}
