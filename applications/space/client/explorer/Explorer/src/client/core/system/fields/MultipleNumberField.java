package client.core.system.fields;

import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.model.fields.NumberField;
import client.core.model.types.Number;
import client.core.system.MonetList;

public class MultipleNumberField extends MultipleField<client.core.model.fields.NumberField, NumberFieldDefinition, Number> implements client.core.model.fields.MultipleNumberField {

    public MultipleNumberField() {
        super(Type.MULTIPLE_NUMBER);
    }

    public MultipleNumberField(String code, String label, MonetList<NumberField> fieldList) {
        super(code, label, Type.MULTIPLE_NUMBER, fieldList);
    }

    @Override
    public ClassName getClassName() {
        return client.core.model.fields.MultipleNumberField.CLASS_NAME;
    }

    @Override
    public ClassName getClassNameOfValue() {
        return client.core.model.fields.NumberField.CLASS_NAME;
    }

    @Override
    public Number getValue() {
        return null;
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public void setValue(Number number) {
    }

}
