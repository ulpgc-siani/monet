package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.definition.entity.FieldDefinition;

public interface BooleanFieldDefinition extends FieldDefinition {

    Instance.ClassName CLASS_NAME = new Instance.ClassName("BooleanFieldDefinition");

    Edition getEdition();

    enum Edition {
        CHECK, OPTION
    }
}
