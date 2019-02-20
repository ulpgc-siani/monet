package client.core.system.fields;

import client.core.model.definition.entity.field.DateFieldDefinition;
import client.core.model.fields.DateField;
import client.core.system.MonetList;
import cosmos.types.Date;

public class MultipleDateField extends MultipleField<DateField, DateFieldDefinition, Date> implements client.core.model.fields.MultipleDateField {

    public MultipleDateField() {
        super(Type.MULTIPLE_DATE);
    }

    public MultipleDateField(String code, String label, MonetList<DateField> fieldList) {
        super(code, label, Type.MULTIPLE_DATE, fieldList);
    }

    @Override
    public final ClassName getClassName() {
        return client.core.model.fields.MultipleDateField.CLASS_NAME;
    }

    @Override
    public ClassName getClassNameOfValue() {
        return client.core.model.fields.DateField.CLASS_NAME;
    }

    @Override
    public Date getValue() {
        return null;
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public void setValue(Date date) {
    }
}
