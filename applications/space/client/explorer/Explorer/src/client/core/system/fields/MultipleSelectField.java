package client.core.system.fields;

import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.model.Source;
import client.core.model.fields.SelectField;
import client.core.model.types.Term;
import client.core.system.MonetList;

public class MultipleSelectField extends MultipleField<SelectField, SelectFieldDefinition, Term> implements client.core.model.fields.MultipleSelectField {
    private Source source;

    public MultipleSelectField() {
        super(Type.MULTIPLE_SELECT);
    }

    public MultipleSelectField(String code, String label, MonetList<SelectField> selectFields) {
        super(code, label, Type.MULTIPLE_SELECT, selectFields);
    }

    @Override
    public Source getSource() {
        return this.source;
    }

    @Override
    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public final ClassName getClassName() {
        return client.core.model.fields.MultipleSelectField.CLASS_NAME;
    }

    @Override
    public ClassName getClassNameOfValue() {
        return client.core.model.fields.SelectField.CLASS_NAME;
    }

    @Override
    public Term getValue() {
        return null;
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public void setValue(Term term) {
    }
}
