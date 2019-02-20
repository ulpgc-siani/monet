package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class FileFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.FileFieldDefinition {

    private long limit;

    @Override
    public Instance.ClassName getClassName() {
        return client.core.model.definition.entity.field.FileFieldDefinition.CLASS_NAME;
    }


    @Override
    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }
}
