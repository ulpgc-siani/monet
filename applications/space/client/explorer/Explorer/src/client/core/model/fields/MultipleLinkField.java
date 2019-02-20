package client.core.model.fields;

import client.core.model.Index;
import client.core.model.Instance;
import client.core.model.MultipleField;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.model.types.Link;

public interface MultipleLinkField extends MultipleField<LinkField, LinkFieldDefinition, Link> {

    Instance.ClassName CLASS_NAME = new Instance.ClassName("MultipleLinkField");

    Index getIndex();
    void setIndex(Index index);
}
