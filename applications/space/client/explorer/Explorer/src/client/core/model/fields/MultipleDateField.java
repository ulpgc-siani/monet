package client.core.model.fields;

import client.core.model.definition.entity.field.DateFieldDefinition;
import client.core.model.MultipleField;
import cosmos.types.Date;

public interface MultipleDateField extends MultipleField<DateField, DateFieldDefinition, Date> {

    ClassName CLASS_NAME = new ClassName("MultipleDateField");
}
