package client.core.system.fields;

import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.core.model.types.Picture;
import client.core.system.MonetList;

public class MultiplePictureField extends MultipleField<client.core.model.fields.PictureField, PictureFieldDefinition, Picture> implements client.core.model.fields.MultiplePictureField {

    public MultiplePictureField() {
        super(Type.MULTIPLE_PICTURE);
    }

    public MultiplePictureField(String code, String label, MonetList<client.core.model.fields.PictureField> fieldList) {
        super(code, label, Type.MULTIPLE_PICTURE, fieldList);
    }

    @Override
    public final ClassName getClassName() {
        return client.core.model.fields.MultiplePictureField.CLASS_NAME;
    }

    @Override
    public ClassName getClassNameOfValue() {
        return client.core.model.fields.PictureField.CLASS_NAME;
    }

    @Override
    public Picture getValue() {
        return null;
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public void setValue(Picture file) {
    }
}
