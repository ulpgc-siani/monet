package client.core.system.definition.entity;

import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.views.FormViewDefinition;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.Instance.ClassName;

public class FormDefinition extends NodeDefinition<FormViewDefinition> implements client.core.model.definition.entity.FormDefinition {
    private boolean environment;
    private Map<String, FieldDefinition> fieldsMap = new HashMap<>();

    public FormDefinition() {
    }

    public FormDefinition(String code, String name, String label, String description) {
        super(code, name, label, description);
    }

    @Override
    public boolean isEnvironment() {
        return this.environment;
    }

    public void setEnvironment(boolean environment) {
        this.environment = environment;
    }

    @Override
    public FieldDefinition getField(String key) {
        return fieldsMap.get(key);
    }

    public void setFields(List<FieldDefinition> fields) {
        fieldsMap.clear();
        for (FieldDefinition fieldDefinition : fields) {
            fieldsMap.put(fieldDefinition.getCode(), fieldDefinition);
            fieldsMap.put(fieldDefinition.getName(), fieldDefinition);
        }
    }

    @Override
    public ClassName getClassName() {
        return client.core.model.definition.entity.FormDefinition.CLASS_NAME;
    }
}
