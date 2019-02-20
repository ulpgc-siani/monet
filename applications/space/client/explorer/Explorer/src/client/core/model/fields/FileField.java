package client.core.model.fields;

import client.core.model.definition.entity.field.FileFieldDefinition;
import client.core.model.Field;
import client.core.model.types.File;

public interface FileField extends Field<FileFieldDefinition, File> {

	ClassName CLASS_NAME = new ClassName("FileField");

	boolean isNullOrEmpty();
}
