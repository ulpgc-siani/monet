package client.core.model.definition.entity;

import client.core.model.definition.views.FormViewDefinition;

import static client.core.model.Instance.ClassName;

public interface FormDefinition extends NodeDefinition<FormViewDefinition> {

    ClassName CLASS_NAME = new ClassName("FormDefinition");

    boolean isEnvironment();
    FieldDefinition getField(String key);
}
