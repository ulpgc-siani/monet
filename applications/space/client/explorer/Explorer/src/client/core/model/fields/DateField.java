package client.core.model.fields;

import client.core.model.definition.entity.field.DateFieldDefinition;
import client.core.model.Field;
import cosmos.types.Date;

public interface DateField extends Field<DateFieldDefinition, Date> {

	ClassName CLASS_NAME = new ClassName("DateField");
	
	boolean isNullOrEmpty();
    void setFormattedValue(String formattedValue);
}
