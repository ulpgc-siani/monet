package client.core.model.fields;

import client.core.model.definition.entity.field.UriFieldDefinition;
import client.core.model.Field;
import client.core.model.types.Uri;

public interface UriField extends Field<UriFieldDefinition, Uri> {

	ClassName CLASS_NAME = new ClassName("UriField");

	boolean isNullOrEmpty();
}
