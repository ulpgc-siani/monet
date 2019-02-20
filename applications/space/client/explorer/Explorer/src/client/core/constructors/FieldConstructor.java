package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public abstract class FieldConstructor<DefinitionType extends FieldDefinition> {

    public Field construct(DefinitionType definition) {
        Field field;
        if (definition instanceof MultipleableFieldDefinition && ((MultipleableFieldDefinition) definition).isMultiple())
            field = constructMultiple(definition);
        else
            field = constructSingle(definition);
        field.setDefinition(definition);
        field.setCode(definition.getCode());
        field.setLabel(definition.getLabel());
        return field;

    }

    protected abstract Field constructSingle(DefinitionType definition);

    protected abstract Field constructMultiple(DefinitionType definition);
}
