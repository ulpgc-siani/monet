package org.monet.grided.core.serializers.json.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.PublicationService;
import org.monet.grided.core.model.PublicationServiceList;
import org.monet.grided.core.serializers.json.JSONSerializer;

public class JSONPublicationServiceListSerializer implements JSONSerializer<JSONArray, PublicationServiceList> {

    @Override
    public JSONArray serialize(PublicationServiceList services) {        
        JSONArray jsonArray = new JSONArray();
        
        for (PublicationService service : services) {
            JSONObject json = new JSONObject();
            
            json.put(PropertyNames.ID, service.getName());
            json.put(PropertyNames.NAME, service.getName());
            json.put(PropertyNames.TYPE, service.getType().toString());
            json.put(PropertyNames.PUBLISHED, service.isPublished());
            jsonArray.add(json);
        }
        
        return jsonArray;
    }

    @Override
    public PublicationServiceList unserialize(String text) {       
        return null;
    }
}

