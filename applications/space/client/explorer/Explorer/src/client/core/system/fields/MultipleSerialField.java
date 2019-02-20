package client.core.system.fields;

import client.core.model.definition.entity.field.SerialFieldDefinition;
import client.core.system.MonetList;

public class MultipleSerialField extends MultipleField<client.core.model.fields.SerialField, SerialFieldDefinition, String> implements client.core.model.fields.MultipleSerialField {

    public MultipleSerialField() {
        super(Type.MULTIPLE_SERIAL);
    }

    public MultipleSerialField(String code, String label, MonetList<client.core.model.fields.SerialField> selectFields) {
        super(code, label, Type.MULTIPLE_SERIAL, selectFields);
    }

    @Override
    public ClassName getClassName() {
        return client.core.model.fields.MultipleSerialField.CLASS_NAME;
    }

    @Override
    public ClassName getClassNameOfValue() {
        return client.core.model.fields.SerialField.CLASS_NAME;
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
