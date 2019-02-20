package org.monet.grided.core.serializers.json.impl;

import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.MetaModelVersion;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.serializers.json.JSONSerializer;

public class JSONModelVersionSerializer implements JSONSerializer<JSONObject, ModelVersion> {
    
    @Override
    public JSONObject serialize(ModelVersion version) {
        JSONObject json = new JSONObject();
        json.put(PropertyNames.ID, version.getId());
        json.put(PropertyNames.LABEL, version.getLabel());
        json.put(PropertyNames.DATE, version.getDate());
        json.put(PropertyNames.METAMODEL, version.getMetaModelVersion());
                                
        return json;
    }

    @Override
    public ModelVersion unserialize(String text) {
        JSONObject json = (JSONObject) net.sf.json.JSONSerializer.toJSON(text);
        
        long id = json.getLong(PropertyNames.ID);
        String name = json.getString(PropertyNames.LABEL);
        String metaModel = json.getString(PropertyNames.METAMODEL);
        
        ModelVersion modelVersion = new ModelVersion(id, name,  new MetaModelVersion(metaModel));
        
        return modelVersion;
    }
}