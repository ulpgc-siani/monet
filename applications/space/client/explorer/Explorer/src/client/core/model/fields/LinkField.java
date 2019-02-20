package client.core.model.fields;

import client.core.model.AllowAnalyze;
import client.core.model.Field;
import client.core.model.Index;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.model.types.Link;

public interface LinkField extends Field<LinkFieldDefinition, Link>, AllowAnalyze {

	ClassName CLASS_NAME = new ClassName("LinkField");

	Index getIndex();
	void setIndex(Index index);
}
