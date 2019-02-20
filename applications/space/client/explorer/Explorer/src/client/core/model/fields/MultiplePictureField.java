package client.core.model.fields;

import client.core.model.Instance;
import client.core.model.MultipleField;
import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.core.model.types.Picture;

public interface MultiplePictureField extends MultipleField<PictureField, PictureFieldDefinition, Picture> {

    Instance.ClassName CLASS_NAME = new Instance.ClassName("MultiplePictureField");
}