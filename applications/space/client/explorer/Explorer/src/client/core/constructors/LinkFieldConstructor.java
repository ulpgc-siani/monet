package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.system.MonetList;
import client.core.system.fields.LinkField;
import client.core.system.fields.MultipleLinkField;

public class LinkFieldConstructor extends FieldConstructor<LinkFieldDefinition> {
    @Override
    protected Field constructSingle(LinkFieldDefinition definition) {
        return new LinkField();
    }

    @Override
    protected Field constructMultiple(LinkFieldDefinition definition) {
        MultipleLinkField field = new MultipleLinkField();
        field.setFields(new MonetList<client.core.model.fields.LinkField>());
        return field;
    }
}
