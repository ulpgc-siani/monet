package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.NodeFieldDefinition;
import client.core.system.fields.NodeField;

public class NodeFieldConstructor extends FieldConstructor<NodeFieldDefinition> {
    @Override
    protected Field constructSingle(NodeFieldDefinition definition) {
        return new NodeField();
    }

    @Override
    protected Field constructMultiple(NodeFieldDefinition definition) {
        /*MultipleNodeField field = new MultipleNodeField();
        field.setFields(new MonetList<client.core.model.fields.NodeField>());
        return field;*/
        return null;
    }
}
