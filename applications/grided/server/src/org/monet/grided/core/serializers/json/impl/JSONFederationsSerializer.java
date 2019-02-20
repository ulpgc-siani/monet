package org.monet.grided.core.serializers.json.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.serializers.json.JSONSerializer;

public class JSONFederationsSerializer implements JSONSerializer<JSONArray, List<Federation>>{

    @Override
    public JSONArray serialize(List<Federation> federations) {
        JSONArray jsonArray = new JSONArray();
        
        for (Federation federation : federations) {            
            JSONObject json = new JSONObject();
            json.put(PropertyNames.ID, federation.getId());
            json.put(PropertyNames.NAME, federation.getName());
            
            JSONObject jsonState = new JSONObject();                
            jsonState.put(PropertyNames.RUNNING, federation.isRunning());
            jsonState.put(PropertyNames.TIME, federation.getState().getRunningFrom());            
            json.put(PropertyNames.STATE, jsonState);

            jsonArray.add(json);
        }
        return jsonArray;                
    }

    @Override
    public List<Federation> unserialize(String json) {
        return null;
    }
}

