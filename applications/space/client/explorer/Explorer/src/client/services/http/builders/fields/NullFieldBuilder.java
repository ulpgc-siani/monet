package client.services.http.builders.fields;

import client.core.model.Entity;
import client.core.model.List;
import client.core.model.fields.BooleanField;
import client.core.system.fields.MultipleCompositeField;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;
import client.services.http.builders.EntityBuilder;

public class NullFieldBuilder extends EntityBuilder {

    @Override
    public Entity build(HttpInstance instance) {
        return null;
    }

}
