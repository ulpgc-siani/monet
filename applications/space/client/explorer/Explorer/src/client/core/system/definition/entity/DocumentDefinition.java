package client.core.system.definition.entity;

import client.core.model.definition.views.DocumentViewDefinition;

import static client.core.model.Instance.ClassName;

public class DocumentDefinition extends NodeDefinition<DocumentViewDefinition> implements client.core.model.definition.entity.DocumentDefinition {

    public DocumentDefinition() {
    }

    public DocumentDefinition(String code, String name, String label, String description) {
        super(code, name, label, description);
    }

    @Override
    public ClassName getClassName() {
        return client.core.model.definition.entity.DocumentDefinition.CLASS_NAME;
    }
}
