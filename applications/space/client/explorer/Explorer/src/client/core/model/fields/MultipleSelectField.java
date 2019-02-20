package client.core.model.fields;

import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.model.MultipleField;
import client.core.model.Source;
import client.core.model.types.Term;

public interface MultipleSelectField extends MultipleField<SelectField, SelectFieldDefinition, Term> {

    ClassName CLASS_NAME = new ClassName("MultipleSelectField");

    Source getSource();
    void setSource(Source source);
}
