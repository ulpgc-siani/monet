package client.core.model.definition.entity;

import client.core.model.definition.Definition;
import client.core.model.definition.Ref;

public interface ProviderDefinition extends Definition {
    Ref getRole();
}
