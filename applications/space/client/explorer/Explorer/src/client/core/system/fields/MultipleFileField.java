package client.core.system.fields;

import client.core.model.definition.entity.field.FileFieldDefinition;
import client.core.model.types.File;
import client.core.system.MonetList;

public class MultipleFileField extends MultipleField<client.core.model.fields.FileField, FileFieldDefinition, File> implements client.core.model.fields.MultipleFileField {

    public MultipleFileField() {
        super(Type.MULTIPLE_FILE);
    }

    public MultipleFileField(String code, String label, MonetList<client.core.model.fields.FileField> fieldList) {
        super(code, label, Type.MULTIPLE_FILE, fieldList);
    }

    @Override
    public final ClassName getClassName() {
        return client.core.model.fields.MultipleFileField.CLASS_NAME;
    }

    @Override
    public ClassName getClassNameOfValue() {
        return client.core.model.fields.FileField.CLASS_NAME;
    }

    @Override
    public File getValue() {
        return null;
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    public void setValue(File file) {
    }
}
