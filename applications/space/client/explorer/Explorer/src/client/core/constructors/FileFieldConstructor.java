package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.FileFieldDefinition;
import client.core.system.MonetList;
import client.core.system.fields.FileField;
import client.core.system.fields.MultipleFileField;

public class FileFieldConstructor extends FieldConstructor<FileFieldDefinition> {
    @Override
    protected Field constructSingle(FileFieldDefinition definition) {
        return new FileField();
    }

    @Override
    protected Field constructMultiple(FileFieldDefinition definition) {
        MultipleFileField field = new MultipleFileField();
        field.setFields(new MonetList<client.core.model.fields.FileField>());
        return field;
    }
}
