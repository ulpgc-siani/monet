package client.core.system.fields;

import client.core.model.List;
import client.core.model.definition.entity.field.MemoFieldDefinition;

public class MultipleMemoField extends MultipleField<client.core.model.fields.MemoField, MemoFieldDefinition, String> implements client.core.model.fields.MultipleMemoField {

    public MultipleMemoField() {
        super(Type.MULTIPLE_MEMO);
    }

    public MultipleMemoField(String code, String label, List<client.core.model.fields.MemoField> fieldList) {
        super(code, label, Type.MULTIPLE_MEMO, fieldList);
    }

    @Override
    public ClassName getClassName() {
        return client.core.model.fields.MultipleMemoField.CLASS_NAME;
    }

    @Override
    public ClassName getClassNameOfValue() {
        return client.core.model.fields.MemoField.CLASS_NAME;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public void setValue(String s) {
    }

}
