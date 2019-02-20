package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class UriFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.UriFieldDefinition {

    @Override
    public Instance.ClassName getClassName() {
        return client.core.model.definition.entity.field.UriFieldDefinition.CLASS_NAME;
    }
}
