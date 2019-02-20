package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface FileFieldDefinition extends MultipleableFieldDefinition {

    Instance.ClassName CLASS_NAME = new Instance.ClassName("FileFieldDefinition");

    long getLimit();
}
