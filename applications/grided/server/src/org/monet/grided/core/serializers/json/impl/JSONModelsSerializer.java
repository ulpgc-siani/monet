package org.monet.grided.core.serializers.json.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.serializers.json.JSONSerializer;

public class JSONModelsSerializer implements JSONSerializer<JSONArray, List<Model>>{

    @Override
    public JSONArray serialize(List<Model> models) {
        JSONArray jsonArray = new JSONArray();
        
        for (Model model : models) {
            JSONObject json = new JSONObject();
            json.put(PropertyNames.ID, model.getId());
            json.put(PropertyNames.NAME, model.getName());
                        
            jsonArray.add(json);
        }
        
        return jsonArray;
    }

    @Override
    public List<Model> unserialize(String json) {
        // TODO Auto-generated method stub
        return null;
    }

}

