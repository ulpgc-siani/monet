package client.core.system.fields;

import client.core.model.Index;
import client.core.model.List;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.model.types.Link;

public class MultipleLinkField extends MultipleField<client.core.model.fields.LinkField, LinkFieldDefinition, Link> implements client.core.model.fields.MultipleLinkField {

    private Index index;

    public MultipleLinkField() {
        super(Type.MULTIPLE_LINK);
    }

    public MultipleLinkField(String code, String label, List<client.core.model.fields.LinkField> fieldList) {
        super(code, label, Type.MULTIPLE_LINK, fieldList);
    }

    @Override
    public ClassName getClassName() {
        return client.core.model.fields.MultipleLinkField.CLASS_NAME;
    }

    @Override
    public ClassName getClassNameOfValue() {
        return client.core.model.fields.LinkField.CLASS_NAME;
    }

    @Override
    public Link getValue() {
        return null;
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public void setValue(Link link) {
    }

    @Override
    public Index getIndex() {
        return index;
    }

    @Override
    public void setIndex(Index index) {
        this.index = index;
    }
}
