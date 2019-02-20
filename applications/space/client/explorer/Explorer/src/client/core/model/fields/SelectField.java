package client.core.model.fields;

import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.model.Field;
import client.core.model.Source;
import client.core.model.types.Term;

public interface SelectField extends Field<SelectFieldDefinition, Term> {

	ClassName CLASS_NAME = new ClassName("SelectField");

	boolean isNullOrEmpty();
	Source getSource();
	void setSource(Source source);

	boolean termIsOther(Term term);
}
