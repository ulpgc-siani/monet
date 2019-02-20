package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.definition.entity.MultipleableFieldDefinition;

abstract class DefaultMultipleableDefinition extends DefaultDefinition implements MultipleableFieldDefinition {

    @Override
    public Boundary getBoundary() {
        return null;
    }
}
