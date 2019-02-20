package client.core.model.definition.entity;

import client.core.model.Instance;
import client.core.model.definition.views.DocumentViewDefinition;

import static client.core.model.Instance.*;

public interface DocumentDefinition extends NodeDefinition<DocumentViewDefinition> {
    ClassName CLASS_NAME = new ClassName("DocumentDefinition");
}
