package org.monet.grided.core.serializers.json.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.serializers.json.JSONSerializer;

public class JSONServersSerializer implements JSONSerializer<JSONArray, List<Server>> {

    @Override
    public JSONArray serialize(List<Server> servers) {
        JSONArray jsonArray = new JSONArray();
        for (Server server : servers) {            
            JSONObject json = new JSONObject();
            json.put(PropertyNames.ID, server.getId());
            json.put(PropertyNames.NAME, server.getName());                        
            jsonArray.add(json);
        }
        return jsonArray;        
    }

    @Override
    public List<Server> unserialize(String json) {
        // TODO Auto-generated method stub
        return null;
    }
}

