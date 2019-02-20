package client.core.system.fields;

import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.model.fields.CompositeField;
import client.core.model.types.Composite;
import client.core.system.MonetList;

public class MultipleCompositeField extends MultipleField<CompositeField, CompositeFieldDefinition, Composite> implements client.core.model.fields.MultipleCompositeField {

    private boolean conditioned;

    public MultipleCompositeField() {
        super(Type.MULTIPLE_COMPOSITE);
        this.conditioned = false;
    }

    public MultipleCompositeField(String code, String label, MonetList<CompositeField> fieldList) {
        super(code, label, Type.MULTIPLE_COMPOSITE, fieldList);
    }

    @Override
    public final ClassName getClassName() {
        return client.core.model.fields.MultipleCompositeField.CLASS_NAME;
    }

    @Override
    public ClassName getClassNameOfValue() {
        return client.core.model.fields.CompositeField.CLASS_NAME;
    }

    @Override
    public Composite getValue() {
        return null;
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public boolean getConditioned() {
        return conditioned;
    }

    @Override
    public void setValue(Composite fields) {
    }

    @Override
    public void setConditioned(boolean conditioned) {
        this.conditioned = conditioned;
    }
}
