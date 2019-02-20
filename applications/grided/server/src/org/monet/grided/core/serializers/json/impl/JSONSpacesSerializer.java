package org.monet.grided.core.serializers.json.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.JSONSerializer;

import com.google.inject.Inject;

public class JSONSpacesSerializer implements JSONSerializer<JSONArray, List<Space>> {

    private JSONModelVersionSerializer modelVersionSerializer;

    @Inject
    public JSONSpacesSerializer(JSONModelVersionSerializer modelVersionSerializer) {
        this.modelVersionSerializer = modelVersionSerializer;
    }
    
    @Override
    public JSONArray serialize(List<Space> spaces) {
        JSONArray jsonArray = new JSONArray();
        
        for (Space space : spaces) {
            JSONObject json = new JSONObject();
            
            JSONObject jsonState = new JSONObject();                
            jsonState.put(PropertyNames.RUNNING, space.isRunning());
            jsonState.put(PropertyNames.TIME, space.getState().getRunningFrom());            
            json.put(PropertyNames.STATE, jsonState);
            
            if (space.getModelVersion() != null) {
                ModelVersion modelVersion = space.getModelVersion();
                json.put(PropertyNames.MODEL_VERSION, this.modelVersionSerializer.serialize(modelVersion));
            }
            
            if (space.getFederation() != null) {
                Federation federation = space.getFederation();
                
                Server server = federation.getServer();
                JSONObject jsonServer = new JSONObject();
                jsonServer.put(PropertyNames.ID, server.getId());
                jsonServer.put(PropertyNames.NAME, server.getName());
                                               
                JSONObject jsonFederation = new JSONObject();
                jsonFederation.put(PropertyNames.ID, federation.getId());
                jsonFederation.put(PropertyNames.NAME, federation.getName());
                jsonFederation.put(PropertyNames.SERVER, jsonServer);
                json.put(PropertyNames.FEDERATION, jsonFederation);                
            }
            
            json.put(PropertyNames.ID, space.getId());
            json.put(PropertyNames.NAME, space.getName());
            jsonArray.add(json);
        }        
        
        return jsonArray;
    }

    @Override
    public List<Space> unserialize(String json) {
        return null;
    }
}

