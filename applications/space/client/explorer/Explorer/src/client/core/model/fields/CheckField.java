package client.core.model.fields;

import client.core.model.definition.entity.field.CheckFieldDefinition;
import client.core.model.Field;
import client.core.model.Source;
import client.core.model.types.CheckList;

public interface CheckField extends Field<CheckFieldDefinition, CheckList> {

	ClassName CLASS_NAME = new ClassName("CheckField");

	boolean isNullOrEmpty();
	Source getSource();
}
