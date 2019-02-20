package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.system.definition.entity.FieldDefinition;

public class BooleanFieldDefinition extends FieldDefinition implements client.core.model.definition.entity.field.BooleanFieldDefinition {

    private Edition edition = Edition.CHECK;

    @Override
    public Instance.ClassName getClassName() {
        return client.core.model.definition.entity.field.BooleanFieldDefinition.CLASS_NAME;
    }

    @Override
    public Edition getEdition() {
        return edition;
    }

    public void setEdition(Edition edition) {
        this.edition = edition;
    }
}
