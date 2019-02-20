package client.core.model.fields;

import client.core.model.MultipleField;
import client.core.model.definition.entity.field.FileFieldDefinition;
import client.core.model.types.File;

public interface MultipleFileField extends MultipleField<FileField, FileFieldDefinition, File> {

    ClassName CLASS_NAME = new ClassName("MultipleFileField");
}
