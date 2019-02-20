package org.monet.grided.core.serializers.json.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.serializers.json.JSONSerializer;

import com.google.inject.Inject;

public class JSONModelVersionsSerializer implements JSONSerializer<JSONArray, List<ModelVersion>> {

    private JSONSpacesSerializer spacesSerializer;

    @Inject
    public JSONModelVersionsSerializer(JSONSpacesSerializer spacesSerializer) {
        this.spacesSerializer = spacesSerializer;
    }
    
    @Override
    public JSONArray serialize(List<ModelVersion> versions) {
        JSONArray jsonVersions = new JSONArray();
        
        for (ModelVersion version : versions) {
            JSONObject jsonVersion = new JSONObject();
            jsonVersion.put(PropertyNames.ID, version.getId());
            jsonVersion.put(PropertyNames.LABEL, version.getLabel());
            jsonVersion.put(PropertyNames.DATE, version.getDate());
            jsonVersion.put(PropertyNames.METAMODEL, version.getMetaModelVersion());
                            
            jsonVersion.put(PropertyNames.SPACES, spacesSerializer.serialize(version.getSpaces()).toString());
            jsonVersions.add(jsonVersion);
        }
        
        return jsonVersions;
    }

    @Override
    public List<ModelVersion> unserialize(String json) {
        
        return null;
    }
}

