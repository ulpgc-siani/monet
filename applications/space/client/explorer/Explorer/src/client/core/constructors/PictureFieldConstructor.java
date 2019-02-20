package client.core.constructors;

import client.core.model.Field;
import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.core.system.MonetList;
import client.core.system.fields.MultiplePictureField;
import client.core.system.fields.PictureField;

public class PictureFieldConstructor extends FieldConstructor<PictureFieldDefinition> {

    @Override
    protected Field constructSingle(PictureFieldDefinition definition) {
        return new PictureField();
    }

    @Override
    protected Field constructMultiple(PictureFieldDefinition definition) {
        MultiplePictureField field = new MultiplePictureField();
        field.setFields(new MonetList<client.core.model.fields.PictureField>());
        return field;
    }
}
